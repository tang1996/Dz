package com.dz.controller;

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

import com.dz.entity.RiderExamine;
import com.dz.service.IRiderExamineService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.JsonUtil;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/riderExamine")
public class RiderExamineController {

	@Autowired
	private IRiderExamineService riderExamineService;

	// 用户列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response,
			RiderExamine riderExamine) {

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<RiderExamine> riderExamineList = riderExamineService
				.riderExamineList(riderExamine);

		for (RiderExamine riderExamines : riderExamineList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", riderExamines.getId());
			record.addColumn("name", riderExamines.getName());
			record.addColumn("phone", riderExamines.getPhone());
			record.addColumn("cardNo", riderExamines.getCardNo());
			record.addColumn("create_time", riderExamines.getCreateTime());
			record.addColumn("age", riderExamines.getAge());

			grid.addRecord(record);
		}
		grid.addProperties("totalCount", riderExamineList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除用户
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, RiderExamine riderExamine,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(riderExamine.getId() + "")
				|| riderExamine.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		riderExamineService.delete(riderExamine.getId() + "");
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
		String bankType = request.getParameter("bank_type");// 银行类型
		String accountOpening = request.getParameter("account_opening");// 开户人
		String accountBank = request.getParameter("account_bank");// 开户支行
		String bankCard = request.getParameter("bank_card");// 银行卡号
		JSonMessage message = new JSonMessage();
		
		if (StringUtil.isEmpty(sex) || StringUtil.isEmpty(education)
				|| StringUtil.isEmpty(city) || StringUtil.isEmpty(phone)
				|| StringUtil.isEmpty(bankType) || StringUtil.isEmpty(accountOpening)
				|| StringUtil.isEmpty(accountBank) || StringUtil.isEmpty(bankCard)
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
		RiderExamine riderExamine = new RiderExamine();
		riderExamine.setSex(sex);
		riderExamine.setCity(city);
		riderExamine.setName(name);
		riderExamine.setEducation(education);
		riderExamine.setPhone(phone);
		riderExamine.setCardNo(cardNo);
		riderExamine.setBankType(bankType);
		riderExamine.setAccountOpening(accountOpening);
		riderExamine.setAccountBank(accountBank);
		//2018-10-22 @Tyy   银行卡验证
		if(bankCard.length() != 16 && bankCard.length() != 19){
			message.addProperty("message", "请输入正确的银行卡号");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if(!JsonUtil.getCard(bankCard)){
			message.addProperty("message", "请绑定储蓄卡，信用卡无法转帐");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		//end
		riderExamine.setBankCard(bankCard);
		riderExamine.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date()));

		riderExamineService.saveORupdate(riderExamine);
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

		RiderExamine riderExamines = riderExamineService.gettoken(token);
		if (riderExamines == null) {
			message.addProperty("message", "token验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		JSonGridRecord record = new JSonGridRecord();
		record.addColumn("id", riderExamines.getId());
		record.addColumn("name", riderExamines.getName());
		record.addColumn("phone", riderExamines.getPhone());
		record.addColumn("cardNo", riderExamines.getCardNo());
		record.addColumn("create_time", riderExamines.getCreateTime());
		record.addColumn("age", riderExamines.getAge());

		grid.addRecord(record);

		grid.addProperties("totalCount", 1);
		new PushJson().outString(grid.toJSonString("list"), response);

	}

}
