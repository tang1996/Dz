package com.dz.controller;

import java.io.UnsupportedEncodingException;
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

import com.dz.entity.SaleExamine;
import com.dz.service.ISaleExamineService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/saleExamine")
public class SaleExamineController {

	@Autowired
	private ISaleExamineService saleExamineService;

	// 用户列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response,
			SaleExamine saleExamine) {

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<SaleExamine> saleExamineList = saleExamineService
				.saleExamineList(saleExamine);

		for (SaleExamine saleExamines : saleExamineList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", saleExamines.getId());
			record.addColumn("name", saleExamines.getName());
			record.addColumn("phone", saleExamines.getPhone());
			record.addColumn("cardNo", saleExamines.getCardNo());
			record.addColumn("create_time", saleExamines.getCreateTime());
			record.addColumn("age", saleExamines.getAge());

			grid.addRecord(record);
		}
		grid.addProperties("totalCount", saleExamineList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除用户
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, SaleExamine saleExamine,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(saleExamine.getId() + "")
				|| saleExamine.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		saleExamineService.delete(saleExamine.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加用户
	@RequestMapping(params = "save", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request,
			HttpServletResponse response) {
		String sex = request.getParameter("sex");// 性别
		String city = request.getParameter("city");// 城市
		String phone = request.getParameter("phone");// 手机
		String cardNo = request.getParameter("cardNo");// 身份证号
		String name = request.getParameter("name");// 姓名
		String education = request.getParameter("education");// 学历
		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(sex) || StringUtil.isEmpty(education)
				|| StringUtil.isEmpty(city) || StringUtil.isEmpty(phone)
				|| StringUtil.isEmpty(cardNo) || StringUtil.isEmpty(name)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if(!NumberUtils.isNumber(phone) || phone.length() != 11){
			message.addProperty("message", "请输入正确的手机号");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if(cardNo.length() != 18){
			message.addProperty("message", "请输入正确的身份证号");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		SaleExamine saleExamine = new SaleExamine();
		saleExamine.setSex(sex);
		saleExamine.setCity(city);
		saleExamine.setName(name);
		saleExamine.setEducation(education);
		saleExamine.setPhone(phone);
		saleExamine.setCardNo(cardNo);
		saleExamine.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date()));

		saleExamineService.saveORupdate(saleExamine);
		message.addProperty("message", "申请成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 个人信息
	@RequestMapping(params = "info", method = RequestMethod.POST)
	public void info(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token)) {
			message.addProperty("message", "暂未登录,请重新登录哦");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		SaleExamine saleExamines = saleExamineService.gettoken(token);
		if (saleExamines == null) {
			message.addProperty("message", "token验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		JSonGridRecord record = new JSonGridRecord();
		record.addColumn("id", saleExamines.getId());
		record.addColumn("name", saleExamines.getName());
		record.addColumn("token", saleExamines.getToken());
		record.addColumn("phone", saleExamines.getPhone());
		record.addColumn("cardNo", saleExamines.getCardNo());
		record.addColumn("create_time", saleExamines.getCreateTime());
		record.addColumn("age", saleExamines.getAge());

		grid.addRecord(record);

		grid.addProperties("totalCount", 1);
		new PushJson().outString(grid.toJSonString("list"), response);

	}

}
