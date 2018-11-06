package com.dz.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.RunManCount;
import com.dz.entity.User;
import com.dz.service.IOrderTypeService;
import com.dz.service.IRunManCountService;
import com.dz.service.IUserService;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/runManCount")
public class RunManCountController {

	@Autowired
	private IRunManCountService runManCountService;
	
	@Autowired
	private IOrderTypeService orderTypeService;

	@Autowired
	private IUserService userService;

	// 配送员当日跑腿统计
	@RequestMapping(params = "dayCount", method = RequestMethod.POST)
	public void dayCount(HttpServletRequest request,
			HttpServletResponse response) {

		String token = request.getParameter("token");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token)) {
			message.addProperty("message", "token不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);
		if (user == null) {
			message.addProperty("message", "token验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		Object[] count = orderTypeService.getDayCount(user.getId(), date);
		if (count == null) {
			message.addProperty("success", true);
			message.addProperty("balance", 0);//本日收益
			message.addProperty("num", 0);//本日单量 
		} else {
			message.addProperty("success", true);
			message.addProperty("num", count[0]);// 本日单量
			if (count[1] != null) {
				DecimalFormat dFormat = new DecimalFormat("######0.00");
				message.addProperty("balance", count[1]);//本日收益
				message.addProperty("balance", dFormat.format(Double.valueOf(count[1].toString())));// /本日收益
			} else {
				message.addProperty("balance", count[1]);//本日收益
			}
		}
		new PushJson().outString(message.toJSonString(), response);
	}
	
	// 配送员当月跑腿统计
	@RequestMapping(params = "monCount", method = RequestMethod.POST)
	public void monCount(HttpServletRequest request,
			HttpServletResponse response) {

		String token = request.getParameter("token");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token)) {
			message.addProperty("message", "token不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);
		if (user == null) {
			message.addProperty("message", "token验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		String date = new SimpleDateFormat("yyyy-MM").format(new Date());

		Object[] count = orderTypeService.getMonCount(user.getId(), date);
		if (count == null) {
			message.addProperty("success", true);
			message.addProperty("balance", 0);//本月收益
			message.addProperty("num", 0);//本月单量 
		} else {
			message.addProperty("success", true);
			message.addProperty("num", count[0]);// 本月单量
			if (count[1] != null) {
				DecimalFormat dFormat = new DecimalFormat("######0.00");
				message.addProperty("balance", count[1]);//本月收益
				message.addProperty("balance", dFormat.format(Double.valueOf(count[1].toString())));// /本月收益
			} else {
				message.addProperty("balance", count[1]);//本月收益
			}
		}
		new PushJson().outString(message.toJSonString(), response);
	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, RunManCount runManCount,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(runManCount.getId() + "")
				|| runManCount.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		runManCountService.delete(runManCount.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}