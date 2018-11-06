package com.dz.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dz.entity.Company;
import com.dz.entity.PowerSort;
import com.dz.entity.Staff;
import com.dz.entity.User;
import com.dz.service.ICompanyService;
import com.dz.service.IPowerSortService;
import com.dz.service.IStaffService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.MD5Util;
import com.dz.util.PushJson;
import com.dz.util.SmessageUtils;
import com.dz.util.StringUtil;
import com.dz.util.TokenUtil;

@Controller
@RequestMapping("/staff")
public class StaffController {

	@Autowired
	private IStaffService staffService;

	@Autowired
	private IPowerSortService powerSortService;

	@Autowired
	private ICompanyService companyService;

	private static final Map<String, String> temp = new ConcurrentHashMap<String, String>();
	
	private static String sign(StringBuilder builder) {
		return MD5Util.MD5(builder.toString());
	}

	// 登陆
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public void login(HttpServletRequest request, HttpServletResponse response) {
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(password)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Staff staffs = staffService.login(userName);
		if (staffs == null) {
			message.addProperty("message", "该帐号不存在!");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		StringBuilder builder = new StringBuilder();
		builder.append(staffs.getRandomCode() + password);
		String localSign = sign(builder);
		if (!staffs.getUserName().equals(userName)
				|| !staffs.getPassword().equals(localSign)) {
			message.addProperty("message", "账号或密码不正确!");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		String token = TokenUtil.getRandomChar(11);
		staffs.setToken(token);
		staffService.saveORupdate(staffs);
		message.addProperty("token", token);
		message.addProperty("companyId", staffs.getCompanyId().getId());
		message.addProperty("success", true);

		new PushJson().outString(message.toJSonString(), response);
	}

	// 用户列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response,
			Staff staff) {

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<Staff> staffList = staffService.staffList(staff);

		for (Staff staffs : staffList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", staffs.getId());
			record.addColumn("user_name", staffs.getUserName());
			record.addColumn("password", staffs.getPassword());
			record.addColumn("random_code", staffs.getRandomCode());
			record.addColumn("name", staffs.getName());
			record.addColumn("token", staffs.getToken());
			record.addColumn("phone", staffs.getPhone());
			record.addColumn("company", staffs.getCompanyId().getName());
			record.addColumn("position", staffs.getPowerSortId().getPosition());
			record.addColumn("powerSort", staffs.getPowerSortId().getSort());

			grid.addRecord(record);
		}
		grid.addProperties("totalCount", staffList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除用户
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(id) || id.equals("0")) {
			message.addProperty("message", "id不正确!");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Staff staff = staffService.getid(Integer.valueOf(id));

		if (staff == null) {
			message.addProperty("message", "员工不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		staffService.delete(id);
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加
	@RequestMapping(params = "save", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request,
			HttpServletResponse response) {
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String companyId = request.getParameter("companyId");
		String powerSortId = request.getParameter("powerSortId");
		String phone = request.getParameter("phone");

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(password)
				|| StringUtil.isEmpty(name) || StringUtil.isEmpty(companyId)
				|| StringUtil.isEmpty(powerSortId) || StringUtil.isEmpty(phone)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = companyService.getCompany(Integer.valueOf(companyId));
		if (company == null) {
			message.addProperty("message", "商家不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		PowerSort powerSort = powerSortService.getid(Integer
				.valueOf(powerSortId));
		if (powerSort == null) {
			message.addProperty("message", "职位不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		String random_numb = TokenUtil.getRandomChar(8);
		StringBuilder builder = new StringBuilder();
		List<Staff> user_name = staffService.getuserName(userName);
		if (user_name.size() > 0) {
			message.addProperty("message", "该账号已存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		builder.append(random_numb + password);
		String localSign = sign(builder);
		password = localSign;
		Staff staffs = new Staff();
		staffs.setUserName(userName);
		staffs.setPassword(localSign);
		staffs.setRandomCode(random_numb);
		staffs.setName(name);
		staffs.setCompanyId(company);
		staffs.setPowerSortId(powerSort);
		staffs.setPhone(phone);
		staffService.saveORupdate(staffs);
		message.addProperty("message", "添加成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}
	
	// 获取和验证短信验证玛
	@RequestMapping(params = "getCode", method = RequestMethod.POST)
	public void getCode(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String userName = request.getParameter("userName");

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(userName)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Staff staff = staffService.login(userName);
		if (staff == null) {
			message.addProperty("message", "该手机未注册");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		String sms = new SmessageUtils().getCode(userName);
		System.out.println(sms);
		temp.put(userName, sms);

		message.addProperty("message", "验证码发送成功.");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 修改信息
	@RequestMapping(params = "update", method = RequestMethod.POST)
	public void update(HttpServletRequest request, HttpServletResponse response) {
		String userName = request.getParameter("userName");
		String phone = request.getParameter("phone");
		String powerSortId = request.getParameter("powerSortId");

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(userName)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Staff staff = staffService.login(userName);
		if (staff == null) {
			message.addProperty("message", "token验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if (!StringUtil.isEmpty(phone)) {
			staff.setPhone(phone);
		}
		if (!StringUtil.isEmpty(powerSortId)) {
			PowerSort powerSort = powerSortService.getid(Integer
					.valueOf(powerSortId));
			if (powerSort == null) {
				message.addProperty("message", "职位不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			staff.setPowerSortId(powerSort);
		}
		staffService.saveORupdate(staff);
		message.addProperty("message", "修改成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 修改密码
	@RequestMapping(params = "updatePassword", method = RequestMethod.POST)
	public void updatePassword(HttpServletRequest request,
			HttpServletResponse response) {
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String code = request.getParameter("code");

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(password)|| StringUtil.isEmpty(code)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		if(code.equals(temp.get(userName))){
			List<Staff> stafflist = staffService.getuserName(userName);
			if (stafflist.size() == 0) {
				message.addProperty("message", "帐号不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}

			StringBuilder builder = new StringBuilder();
			builder.append(stafflist.get(0).getRandomCode() + password);
			String localSign = sign(builder);
			String newPassword = localSign;
			stafflist.get(0).setPassword(newPassword);

			staffService.saveORupdate(stafflist.get(0));
			message.addProperty("message", "修改成功");
			message.addProperty("success", true);
		}else{
			message.addProperty("message", "验证码不正确.");
			message.addProperty("success", false);
		}
		
		new PushJson().outString(message.toJSonString(), response);
	}

	// 个人信息
	@RequestMapping(params = "info", method = RequestMethod.POST)
	public void info(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Staff staffs = staffService.gettoken(token);
		if (staffs == null) {
			message.addProperty("message", "token验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		message.addProperty("success", true);

		message.addProperty("id", staffs.getId());
		message.addProperty("user_name", staffs.getUserName());
		message.addProperty("password", staffs.getPassword());
		message.addProperty("random_code", staffs.getRandomCode());
		message.addProperty("name", staffs.getName());
		message.addProperty("token", staffs.getToken());
		message.addProperty("phone", staffs.getPhone());
		message.addProperty("company", staffs.getCompanyId().getName());
		message.addProperty("position", staffs.getPowerSortId().getPosition());

		new PushJson().outString(message.toJSonString(), response);

	}

}
