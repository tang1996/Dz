package com.dz.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Opinion;
import com.dz.entity.User;
import com.dz.service.IOpinionService;
import com.dz.service.IUserService;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/opinion")
public class OpinionController {

	@Autowired
	private IOpinionService opinionService;

	@Autowired
	private IUserService userService;

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Opinion opinion, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(opinion.getId() + "") || opinion.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		opinionService.delete(opinion.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 用户反馈
	@RequestMapping(params = "userSave", method = RequestMethod.POST)
	public void userSave(HttpServletRequest request, HttpServletResponse response) {
		// String token = request.getParameter("token");// 用户token
		String phone = request.getParameter("phone");// 联系方式
		String content = request.getParameter("content");// 内容

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(phone) || StringUtil.isEmpty(content)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		// User user = userService.gettoken(token);
		// if (user == null) {
		// message.addProperty("message", "token验证失败");
		// message.addProperty("success", false);
		// new PushJson().outString(message.toJSonString(), response);
		// return;
		// }

		Opinion opinion = new Opinion();
		// opinion.setUserName(user.getUserName());
		opinion.setPhone(phone);
		opinion.setContent(content);
		opinion.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		opinionService.saveORupdate(opinion);

		message.addProperty("message", "提交成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 商家反馈
	@RequestMapping(params = "companySave", method = RequestMethod.POST)
	public void companySave(HttpServletRequest request, HttpServletResponse response) {
		// String companyId = request.getParameter("companyId");// 商家
		String phone = request.getParameter("phone");// 联系方式
		String content = request.getParameter("content");// 内容

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(phone) || StringUtil.isEmpty(content)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Opinion opinion = new Opinion();
		// opinion.setCompanyId(companyId);
		opinion.setPhone(phone);
		opinion.setContent(content);
		opinion.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		opinionService.saveORupdate(opinion);

		message.addProperty("message", "提交成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 跑腿员反馈
	@RequestMapping(params = "riderSave", method = RequestMethod.POST)
	public void riderSave(HttpServletRequest request, HttpServletResponse response) {
		// String token = request.getParameter("token");// 跑腿token
		String phone = request.getParameter("phone");// 联系方式
		String content = request.getParameter("content");// 内容

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(phone) || StringUtil.isEmpty(content)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		// User rider = userService.gettoken(token);
		// if (rider == null) {
		// message.addProperty("message", "token验证失败");
		// message.addProperty("success", false);
		// new PushJson().outString(message.toJSonString(), response);
		// return;
		// }

		Opinion opinion = new Opinion();
		// opinion.setRiderId(rider.getId() + "");
		opinion.setPhone(phone);
		opinion.setContent(content);
		opinion.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		opinionService.saveORupdate(opinion);

		message.addProperty("message", "提交成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加反馈
	@RequestMapping(params = "save", method = RequestMethod.POST)
	public void save(HttpServletRequest request, HttpServletResponse response) {
		String phone = request.getParameter("phone");// 联系方式
		String content = request.getParameter("content");// 内容

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(phone) || StringUtil.isEmpty(content)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Opinion opinion = new Opinion();
		opinion.setPhone(phone);
		opinion.setContent(content);
		opinion.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		opinionService.saveORupdate(opinion);

		message.addProperty("message", "提交成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}