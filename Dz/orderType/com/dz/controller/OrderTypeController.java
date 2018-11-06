package com.dz.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.OrderType;
import com.dz.service.IOrderTypeService;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/orderType")
public class OrderTypeController {

	@Autowired
	private IOrderTypeService orderTypeService;

	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		JSonMessage message = new JSonMessage();

		OrderType orderTypr = orderTypeService.getOrderType(Integer.valueOf(orderId));
		if (orderTypr == null) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		message.addProperty("success", true);
		message.addProperty("addTime", orderTypr.getOrderId().getAddTime());
		message.addProperty("status", orderTypr.getStatus());
		new PushJson().outString(message.toJSonString(), response);

	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, OrderType orderType,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderType.getId() + "") || orderType.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
			orderTypeService.delete(orderType.getId() + "");
			message.addProperty("message", "删除成功");
			message.addProperty("success", true);
			new PushJson().outString(message.toJSonString(), response);
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request,
			OrderType orderType, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		orderTypeService.saveORupdate(orderType);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}