package com.dz.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dz.entity.SalerInfo;
import com.dz.entity.SalerPower;
import com.dz.service.ISalerInfoService;
import com.dz.service.ISalerPowerService;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/salerPower")
public class SalerPowerController {

	@Autowired
	private ISalerPowerService salerPowerService;

	@Autowired
	private ISalerInfoService salerService;

	// 修改职位
	@RequestMapping(params = "update", method = RequestMethod.POST)
	@ResponseBody
	public void update(HttpServletRequest request, HttpServletResponse response) {

		String salerPowerId = request.getParameter("salerPowerId");// 职位id
		String salerId = request.getParameter("salerId");// 推广员id

		String boosId = request.getParameter("boosId");// 推广员上司id
		JSonMessage message = new JSonMessage();

		if(StringUtil.isEmpty(boosId) || StringUtil.isEmpty(salerId) || StringUtil.isEmpty(salerPowerId)){
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		SalerInfo saler = salerService.getid(Integer.valueOf(salerId));
		if (saler == null) {
			message.addProperty("message", "业务员不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		SalerPower salerPower = salerPowerService.getid(Integer.valueOf(salerPowerId));
		if (salerPower != null) {
			if (!salerPower.getSort().equals("888")) {
				if (StringUtil.isEmpty(boosId)) {
					message.addProperty("message", "上司id不能为空");
					message.addProperty("success", false);
					new PushJson().outString(message.toJSonString(), response);
					return;
				}
				saler.setBossId(Integer.valueOf(boosId));
			} else {
				saler.setBossId(0);
			}
			saler.setSalerPowerId(salerPower);
		}

		message.addProperty("message", "操作成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);

	}
	
	

}
