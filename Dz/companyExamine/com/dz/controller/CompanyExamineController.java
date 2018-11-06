package com.dz.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dz.entity.CompanyExamine;
import com.dz.entity.SalerInfo;
import com.dz.service.ICompanyExamineService;
import com.dz.service.ISalerInfoService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.MD5Util;
import com.dz.util.PhoneUtil;
import com.dz.util.PushJson;
import com.dz.util.SmessageUtils;
import com.dz.util.StringUtil;
import com.dz.util.URL;

@Controller
@RequestMapping("/companyExamine")
public class CompanyExamineController {

	@Autowired
	private ICompanyExamineService companyExamineService;

	@Autowired
	private ISalerInfoService salerService;

	// 用户列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response, CompanyExamine companyExamine) {

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<CompanyExamine> companyExamineList = companyExamineService.companyExamineList(companyExamine);

		for (CompanyExamine companyExamines : companyExamineList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", companyExamines.getId());
			record.addColumn("name", companyExamines.getName());
			record.addColumn("phone", companyExamines.getPhone());
			record.addColumn("create_time", companyExamines.getCreateTime());

			grid.addRecord(record);
		}
		grid.addProperties("totalCount", companyExamineList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除用户
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, CompanyExamine companyExamine, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyExamine.getId() + "") || companyExamine.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		companyExamineService.delete(companyExamine.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加商家
	@RequestMapping(params = "save", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, HttpServletResponse response) {
		String phone = request.getParameter("phone");// 手机
		String name = request.getParameter("name");// 姓名
		String address = request.getParameter("address");// 地址
		String shopPhone = request.getParameter("shopPhone");// 商家手机号
		String main = request.getParameter("main");// 主营业务
		String deputy = request.getParameter("deputy");// 副营业务

		String code = request.getParameter("code");// 推广码
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(address) || StringUtil.isEmpty(phone) || StringUtil.isEmpty(name)
				|| StringUtil.isEmpty(shopPhone)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if (!NumberUtils.isNumber(phone) || phone.length() != 11) {
			message.addProperty("message", "请输入正确的手机号");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		CompanyExamine companyExamine = new CompanyExamine();
		companyExamine.setName(name);
		companyExamine.setAddress(address);
		if(!PhoneUtil.isMobileNO(phone)){
			message.addProperty("message", "手机号校验失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		companyExamine.setPhone(phone);
		if (!StringUtil.isEmpty(main)) {
			companyExamine.setMain(main);
		}
		if (!StringUtil.isEmpty(deputy)) {
			companyExamine.setDeputy(deputy);
		}
		companyExamine.setShopPhone(shopPhone);
		if (!StringUtil.isEmpty(code)) {
			SalerInfo saler = salerService.getCode(code);
			if (saler == null) {
				message.addProperty("message", "推广码不存在,请留空或核实业务员推广码");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			companyExamine.setSalerId(saler.getId());
		} else {
			companyExamine.setSalerId(0);
		}
		companyExamine.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		companyExamineService.saveORupdate(companyExamine);
		message.addProperty("message", "保存成功");
		message.addProperty("id", companyExamine.getId());
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 上传图片 2018-10-21 @Tyy 商家入驻上传图片
	@RequestMapping(params = "logoUpload", method = RequestMethod.POST)
	public void logoUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		String type = request.getParameter("type");// health 卫生许可证, charter
		// 营业执照, card 身份证正反面照片, take 手持身份证照片,

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(type)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			System.out.println("缺少必要参数");
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		// 如果文件为空，返回失败
		if (file.isEmpty()) {
			message.addProperty("message", "请选择要上传的文件");
			message.addProperty("success", false);
			System.out.println("请选择要上传的文件");
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		// 上传文件路径
		String path = request.getSession().getServletContext().getRealPath("");
		path = path.substring(0, path.indexOf("Dz\\")) + "DzClient\\common\\images\\";

		// 上传文件名
		String name = file.getOriginalFilename();
		String prefix = name.substring(name.lastIndexOf("."));

		if (prefix.equals(".jpg") || prefix.equals(".png") || prefix.equals(".jpeg") || prefix.equals(".bmp")
				|| prefix.equals(".PNG") || prefix.equals(".JPEG") || prefix.equals(".BMP") || prefix.equals(".JPG")) {

			String filename = MD5Util.MD5(System.currentTimeMillis() + "") + prefix;

			File filepath = new File(path, filename);
			// 判断路径是否存在，如果不存在就创建一个
			if (!filepath.getParentFile().exists()) {
				filepath.getParentFile().mkdirs();
			}

			String newPath = URL.FIND_URL + filename;
			CompanyExamine companyExamine = companyExamineService.getid(Integer.valueOf(id));
			if (companyExamine == null) {
				message.addProperty("message", "商家不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			if (type.equals("charter")) {
				companyExamine.setCharter(newPath);
			}
			if (type.equals("card")) {
				companyExamine.setCard(newPath);
			}
			if (type.equals("take")) {
				companyExamine.setTakeCard(newPath);
			}
			if (type.equals("health")) {
				companyExamine.setHealthCard(newPath);
			}
			// 将上传文件保存到一个目标文件当中
			try {
				file.transferTo(new File(path + File.separator + filename));
				companyExamineService.saveORupdate(companyExamine);
			} catch (IOException e) {
				message.addProperty("message", "上传失败");
				message.addProperty("success", false);
				e.printStackTrace();
				new PushJson().outString(message.toJSonString(), response);
				return;
			}

			message.addProperty("message", "上传成功");
			message.addProperty("success", true);
			new PushJson().outString(message.toJSonString(), response);
		} else {
			message.addProperty("message", "目前只支持jpg,png,jpeg,bmp 几种格式且不超过1M的图片.");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
		}

	}

	// 图片详情 2018-10-21 @Tyy 商家入驻图片详情
	@RequestMapping(params = "find", method = RequestMethod.POST)
	public void find(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		CompanyExamine companyExamines = companyExamineService.getid(Integer.valueOf(id));
		if (companyExamines == null) {
			message.addProperty("message", "找不到对应的申请记录");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		message.addProperty("id", companyExamines.getId());
		message.addProperty("charter", companyExamines.getCharter());
		message.addProperty("health", companyExamines.getHealthCard());
		message.addProperty("card", companyExamines.getCard());
		message.addProperty("take", companyExamines.getTakeCard());
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);

	}

	// 提交申请 2018-10-21 @Tyy 商家入驻提交申请
	@RequestMapping(params = "submit", method = RequestMethod.POST)
	public void submit(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		CompanyExamine companyExamines = companyExamineService.getid(Integer.valueOf(id));
		if (companyExamines == null) {
			message.addProperty("message", "找不到对应的申请记录");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (StringUtil.isEmpty(companyExamines.getCharter())) {
			message.addProperty("message", "请上传营业执照");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		if (StringUtil.isEmpty(companyExamines.getHealthCard())) {
			message.addProperty("message", "请上传卫生许可证");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		if (StringUtil.isEmpty(companyExamines.getCard())) {
			message.addProperty("message", "请上传法人身份证正反面");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		if (StringUtil.isEmpty(companyExamines.getTakeCard())) {
			message.addProperty("message", "请上传法人手持身份证照片");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		new SmessageUtils().sendToCompanyExamine(companyExamines.getPhone());
		message.addProperty("message", "申请提交成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);

	}

}
