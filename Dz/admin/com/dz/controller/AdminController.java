package com.dz.controller;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dz.entity.Admin;
import com.dz.service.IAdminService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.MD5Util;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private IAdminService adminService;

	private String sign(StringBuilder builder) {
		return MD5Util.MD5(builder.toString());
	}

	// 登陆
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public void login(Admin admin, HttpServletResponse response) {

		JSonMessage message = new JSonMessage();

		Admin admins = adminService.login(admin.getUserName());
		if (admins != null) {
			StringBuilder builder = new StringBuilder();
			builder.append(admins.getRandomCode() + admin.getPassword());
			String localSign = sign(builder);
			if (admins.getUserName().equals(admin.getUserName()) && admins.getPassword().equals(localSign)) {
				message.addProperty("message", "./index.html");
				message.addProperty("success", true);
			} else {
				message.addProperty("message", "账号或密码不正确!");
				message.addProperty("success", false);
			}
		} else {
			message.addProperty("message", "该帐号不存在!");
			message.addProperty("success", false);
		}

		new PushJson().outString(message.toJSonString(), response);

	}

	// 管理员列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response, Admin admin) {

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<Admin> adminList = adminService.adminList(admin);

		if (adminList != null) {
			for (Admin admins : adminList) {
				JSonGridRecord record = new JSonGridRecord();
				record.addColumn("id", admins.getId());
				record.addColumn("user_name", admins.getUserName());
				record.addColumn("password", admins.getPassword());
				record.addColumn("random_code", admins.getRandomCode());
				record.addColumn("name", admins.getName());
				record.addColumn("e_mail", admins.getEmail());
				record.addColumn("create_time", admins.getCreateTime());
				record.addColumn("last_ip", admins.getLastIp());
				record.addColumn("last_login", admins.getLastLogin());

				grid.addRecord(record);
				grid.addProperties("totalCount", adminList.size());
				grid.addProperties("totalSum", adminList.size());
			}
		} else {
			JSonGridRecord record = new JSonGridRecord();
			grid.addRecord(record);
			grid.addProperties("totalCount", 0);
			grid.addProperties("totalSum", 0);
		}
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除管理员
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Admin admin, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(admin.getId() + "") || admin.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
		} else {
			adminService.delete(admin.getId() + "");
			message.addProperty("message", "删除成功");
			message.addProperty("success", true);
			new PushJson().outString(message.toJSonString(), response);
		}
	}

	// 添加或修改管理员 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, Admin admin, HttpServletResponse response) {
		int id = admin.getId();
		JSonMessage message = new JSonMessage();

		Random random = new Random();
		int random_numb = random.nextInt(99999999) % (99999999 - 10000000 + 1) + 10000000;

		StringBuilder builder = new StringBuilder();
		if (id == 0) {
			List<Admin> admin_name = adminService.getuserName(admin.getUserName());
			if (admin_name.size() > 0) {
				message.addProperty("message", "该账号已存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			builder.append(random_numb + admin.getPassword());
			String localSign = sign(builder);
			Admin admins = new Admin();
			admins.setUserName(admin.getUserName());
			admins.setPassword(localSign);
			admins.setRandomCode(random_numb + "");
			admins.setName(admin.getName());
			admins.setEmail(admin.getEmail());
			admins.setCreateTime(admin.getCreateTime());
			admins.setLastIp(admin.getLastIp());
			admins.setLastLogin(admin.getLastLogin());
			adminService.saveORupdate(admins);
		} else {
			Admin adminn = adminService.getid(Integer.valueOf(id));
			if (!adminn.getUserName().equals(admin.getUserName())) {
				message.addProperty("message", "不能修改用户账号");
				message.addProperty("success", true);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			if (admin.getPassword().equals(adminn.getPassword())) {
				admin.setPassword(admin.getPassword());
			} else {
				builder.append(adminn.getRandomCode() + admin.getPassword());
				String localSign = sign(builder);
				adminn.setPassword(localSign);
			}
			adminn.setName(admin.getName());
			adminn.setEmail(admin.getEmail());
			adminn.setCreateTime(admin.getCreateTime());
			adminn.setLastIp(admin.getLastIp());
			adminn.setLastLogin(admin.getLastLogin());
			adminService.saveORupdate(adminn);
		}
		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}