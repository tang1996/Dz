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
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/base/companyExamine")
public class BaseCompanyExamineController {

	@Autowired
	private ICompanyExamineService companyExamineService;
	
	@Autowired
	private ISalerInfoService salerService;

	// 用户列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response,
			CompanyExamine companyExamine) {

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<CompanyExamine> companyExamineList = companyExamineService
				.companyExamineList(companyExamine);

		for (CompanyExamine companyExamines : companyExamineList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", companyExamines.getId());
			record.addColumn("name", companyExamines.getName());
			record.addColumn("phone", companyExamines.getPhone());
			record.addColumn("create_time", companyExamines.getCreateTime());
			record.addColumn("address", companyExamines.getAddress());
			record.addColumn("shopPhone", companyExamines.getShopPhone());
			record.addColumn("location", companyExamines.getLocation());

			grid.addRecord(record);
		}
		grid.addProperties("totalCount", companyExamineList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除用户
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request,
			CompanyExamine companyExamine, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyExamine.getId() + "")
				|| companyExamine.getId() == 0) {
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
	public void saveORupdate(HttpServletRequest request,
			HttpServletResponse response) {
		String phone = request.getParameter("phone");// 手机
		String name = request.getParameter("name");// 姓名
		String address = request.getParameter("address");// 地址
		String shopType = request.getParameter("shopType");// 商家类型
		String shopPhone = request.getParameter("shopPhone");// 商家手机号
		String location = request.getParameter("location");// 位置坐标

		String main = request.getParameter("main");// 主营业务
		String deputy = request.getParameter("deputy");// 副营业务

		String code = request.getParameter("code");// 推广码
		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(address) || StringUtil.isEmpty(phone)
				|| StringUtil.isEmpty(name) || StringUtil.isEmpty(shopType)
				|| StringUtil.isEmpty(shopPhone)
				|| StringUtil.isEmpty(location)) {
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
		companyExamine.setPhone(phone);
		if (!StringUtil.isEmpty(main)) {
			companyExamine.setMain(main);
		}
		if (!StringUtil.isEmpty(deputy)) {
			companyExamine.setDeputy(deputy);
		}
		companyExamine.setShopPhone(shopPhone);
		companyExamine.setLocation(location);
		if (StringUtil.isEmpty(code)) {
			companyExamine.setSalerId(0);
		} else {
			SalerInfo saler = salerService.getCode(code);
			if (saler == null) {
				message.addProperty("message", "推广码验证失败");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			companyExamine.setSalerId(saler.getId());
		}
		companyExamine
				.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date()));

		companyExamineService.saveORupdate(companyExamine);
		message.addProperty("message", "保存成功");
		message.addProperty("id", companyExamine.getId());
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 上传店铺头像
	@RequestMapping(params = "logoUpload", method = RequestMethod.POST)
	public void logoUpload(HttpServletRequest request,
			@RequestParam("file") MultipartFile file,
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
		System.out.println("path001====>" + path);

		path = path.substring(0, path.length() - 9)
				+ "DzClient\\WebRoot\\common\\images\\";
		System.out.println("path====>" + path);

		// 上传文件名
		String name = file.getOriginalFilename();
		String prefix = name.substring(name.lastIndexOf("."));

		String filename = MD5Util.MD5(System.currentTimeMillis() + "") + prefix;
		File filepath = new File(path, filename);
		// 判断路径是否存在，如果不存在就创建一个
		if (!filepath.getParentFile().exists()) {
			filepath.getParentFile().mkdirs();
		}

		String newPath = "http://118.190.149.109:8081/DzClient/WebRoot/common/images/"
				+ filename;
		System.out.println("newPath====>" + newPath);
		CompanyExamine companyExamine = companyExamineService.getid(Integer
				.valueOf(id));
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
	}

}
