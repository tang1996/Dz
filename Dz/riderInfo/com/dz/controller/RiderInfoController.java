package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.RiderInfo;
import com.dz.service.IRiderInfoService;
import com.dz.service.IUserService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/base/riderInfo")
public class RiderInfoController {

	@Autowired
	private IUserService userService;

	@Autowired
	private IRiderInfoService riderInfoService;

	// 用户列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response, RiderInfo riderInfo) {

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<RiderInfo> riderInfoList = riderInfoService.riderInfoList(riderInfo);

		for (RiderInfo riderInfos : riderInfoList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", riderInfos.getId());
			record.addColumn("accountOpening", riderInfos.getAccountOpening());
			record.addColumn("bankCard", riderInfos.getBankCard());
			record.addColumn("bankType", riderInfos.getBankType());
			record.addColumn("accountBank", riderInfos.getAccountBank());
			record.addColumn("cardNo", riderInfos.getCardNo());
			record.addColumn("education", riderInfos.getEducation());
			record.addColumn("city", riderInfos.getCity());
			record.addColumn("sex", riderInfos.getSex());
			record.addColumn("name", riderInfos.getUserId().getName());

			grid.addRecord(record);
		}
		grid.addProperties("totalCount", riderInfoList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除用户
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, RiderInfo user, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(user.getId() + "") || user.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		userService.delete(user.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 修改信息
	@RequestMapping(params = "update", method = RequestMethod.POST)
	public void update(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		// 选填
		String accountOpening = request.getParameter("accountOpening");
		String bankCard = request.getParameter("bankCard");
		String bankType = request.getParameter("bankType");
		String accountBank = request.getParameter("accountBank");
		String city = request.getParameter("city");
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		RiderInfo riderInfo = riderInfoService.getid(Integer.valueOf(id));
		if (riderInfo == null) {
			message.addProperty("message", "骑手信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if (!StringUtil.isEmpty(accountOpening)) {
			riderInfo.setAccountOpening(accountOpening);
		}
		if (!StringUtil.isEmpty(bankCard)) {
			riderInfo.setBankCard(bankCard);
		}
		if (!StringUtil.isEmpty(bankType)) {
			riderInfo.setBankType(bankType);
		}
		if (!StringUtil.isEmpty(accountBank)) {
			riderInfo.setAccountBank(accountBank);
		}
		if (!StringUtil.isEmpty(city)) {
			riderInfo.setCity(city);
		}
		riderInfoService.saveORupdate(riderInfo);

		message.addProperty("message", "修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 个人信息
	@RequestMapping(params = "info", method = RequestMethod.POST)
	public void info(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		RiderInfo riderInfo = riderInfoService.getid(Integer.valueOf(id));
		if (riderInfo == null) {
			message.addProperty("message", "token验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		JSonGridRecord record = new JSonGridRecord();
		record.addColumn("id", riderInfo.getId());
		record.addColumn("accountOpening", riderInfo.getAccountOpening());
		record.addColumn("bankCard", riderInfo.getBankCard());
		record.addColumn("bankType", riderInfo.getBankType());
		record.addColumn("accountBank", riderInfo.getAccountBank());
		record.addColumn("cardNo", riderInfo.getCardNo());
		record.addColumn("education", riderInfo.getEducation());
		record.addColumn("city", riderInfo.getCity());
		record.addColumn("sex", riderInfo.getSex());
		record.addColumn("name", riderInfo.getUserId().getName());

		grid.addRecord(record);

		grid.addProperties("totalCount", 1);
		new PushJson().outString(grid.toJSonString("list"), response);

	}

}
