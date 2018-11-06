package com.dz.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Distribution;
import com.dz.service.IDistributionService;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/distribution")
public class DistributionController {

	@Autowired
	private IDistributionService distributionService;

	//属性详情 
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

		Distribution distribution = distributionService.getDistribution(Integer
				.valueOf(companyId));
		if (distribution == null) {
			message.addProperty("message", "商家信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		message.addProperty("id", distribution.getId());//属性id
//		message.addProperty("boxPrice", distribution.getBoxPrice());//餐盒费
		message.addProperty("distributionPrice", distribution
				.getDistributionPrice());//配送费
		message.addProperty("GDP", distribution.getGDP());//人均消费
		message.addProperty("miniPrice", distribution.getMiniPrice());//起送价格
		message.addProperty("time", distribution.getTime());//配送时间
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);

	}

	// 添加
	@RequestMapping(params = "save", method = RequestMethod.POST)
	public void save(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");// 商家id
//		String time = request.getParameter("time");// 配送时间
		String distributionPrice = request.getParameter("distributionPrice");// 配送价格
		String GDP = request.getParameter("GDP");// 人均消费
		String miniPrice = request.getParameter("miniPrice");// 起送价格

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(distributionPrice)
				|| StringUtil.isEmpty(companyId)
//				|| StringUtil.isEmpty(time) || StringUtil.isEmpty(boxPrice)
				|| StringUtil.isEmpty(GDP) || StringUtil.isEmpty(miniPrice)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Distribution distribution = new Distribution();
//		distribution.setBoxPrice(boxPrice);
		distribution.setCompanyId(companyId);
		distribution.setDistributionPrice(distributionPrice);
		distribution.setGDP(GDP);
		distribution.setMiniPrice(miniPrice);
		distribution.setMode("商家配送");
		distribution.setTime("30");
		distributionService.saveORupdate(distribution);

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
//		String time = request.getParameter("time");//配送时间
		String distributionPrice = request.getParameter("distributionPrice");//配送费
		String GDP = request.getParameter("GDP");//人均消费
		String miniPrice = request.getParameter("miniPrice");//起送价格

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Distribution distribution = distributionService.getDistribution(Integer
				.valueOf(companyId));
		if (distribution == null) {
			message.addProperty("message", "商家信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if (!StringUtil.isEmpty(distributionPrice)) {
			distribution.setDistributionPrice(distributionPrice);
		}
		if (!StringUtil.isEmpty(GDP)) {
			distribution.setGDP(GDP);
		}
		if (!StringUtil.isEmpty(miniPrice)) {
			distribution.setMiniPrice(miniPrice);
		}
//		if (!StringUtil.isEmpty(time)) {
//			distribution.setTime(time);
//		}
		distributionService.saveORupdate(distribution);

		message.addProperty("message", "修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}