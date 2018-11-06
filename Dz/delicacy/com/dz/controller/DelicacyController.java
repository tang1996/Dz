package com.dz.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Delicacy;
import com.dz.service.IDelicacyService;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/delicacy")
public class DelicacyController {

	@Autowired
	private IDelicacyService delicacyService;

	// 属性详情
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");// 商家id
		// token = "cOZ6cjmF9NF";
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Delicacy delicacy = delicacyService.getDelicacy(Integer
				.valueOf(companyId));
		if (delicacy == null) {
			message.addProperty("message", "商家信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		message.addProperty("GDP", delicacy.getGdp());// 人均消费
		message.addProperty("mealFee", delicacy.getMealFee());// 餐位费
		message.addProperty("id", delicacy.getId());// 属性id

		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);

	}

	// 添加
	@RequestMapping(params = "save", method = RequestMethod.POST)
	public void save(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");// 商家id
		String mealFee = request.getParameter("mealFee");// 餐位费
		String GDP = request.getParameter("GDP");// 人均消费
		String miniPrice = request.getParameter("miniPrice");// 最低消费

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(GDP)
				|| StringUtil.isEmpty(mealFee) || StringUtil.isEmpty(miniPrice)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Delicacy delicacy = new Delicacy();
		delicacy.setGdp(GDP);
		delicacy.setCompanyId(companyId);
		delicacy.setMealFee(mealFee);
		delicacyService.saveORupdate(delicacy);

		message.addProperty("message", "添加信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 修改
	@RequestMapping(params = "update", method = RequestMethod.POST)
	public void update(HttpServletRequest request, HttpServletResponse response) {
		// 必填参数
		String companyId = request.getParameter("companyId");// 商家id
		// 选填参数
		String mealFee = request.getParameter("mealFee");// 餐位费
		String GDP = request.getParameter("GDP");// 人均消费
		//String miniPrice = request.getParameter("miniPrice");// 最低消费
 
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Delicacy delicacy = delicacyService.getDelicacy(Integer
				.valueOf(companyId));
		if (delicacy == null) {
			message.addProperty("message", "商家信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if (!StringUtil.isEmpty(mealFee)) {
			delicacy.setMealFee(mealFee);
		}
		System.out.println(GDP);
		if (!StringUtil.isEmpty(GDP)) {
			delicacy.setGdp(GDP);
		}
		
		delicacyService.saveORupdate(delicacy);

		message.addProperty("message", "修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}