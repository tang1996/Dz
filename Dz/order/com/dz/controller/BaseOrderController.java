package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Company;
import com.dz.entity.Order;
import com.dz.entity.OrderType;
import com.dz.entity.User;
import com.dz.service.ICompanyService;
import com.dz.service.IOrderService;
import com.dz.service.IOrderTypeService;
import com.dz.service.IUserService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/base/order")
public class BaseOrderController {

	@Autowired
	private IOrderService orderService;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IOrderTypeService orderTypeService;
	
	// 美食订单列表	ynw
	@RequestMapping(params = "cateView", method = RequestMethod.POST)
	public void cateView(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String orderNo = request.getParameter("orderNo");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		JSonMessage message = new JSonMessage();
		if(StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)){
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		System.out.println(companyId);
		Order order = new Order();
		if(!StringUtil.isEmpty(orderNo)){
			order.setOrderNo(orderNo);
		}
		if(!StringUtil.isEmpty(companyId)){
			Company company = companyService.getCompany(Integer.valueOf(companyId));
			if(company == null){
				message.addProperty("message", "商家不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			System.out.println(company.getName());
			order.setCompanyId(company);
		}
		order.setOrderType("2");
		List<Order> count = orderService.orderList(order);
		List<Order> orderList = orderService.orderList(order, Integer.valueOf(start), Integer.valueOf(limit));
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (Order orders : orderList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("orderId", orders.getId());
			record.addColumn("companyName", orders.getCompanyId().getName());
			record.addColumn("addTime", orders.getAddTime());
			record.addColumn("orderNo", orders.getOrderNo());
			record.addColumn("orderStatus", orders.getOrderStatus());
			record.addColumn("total", orders.getTotal());
			record.addColumn("pay", orders.getPay());
			record.addColumn("discount", orders.getDiscount());
			if(orders.getUserId() != null){
				record.addColumn("userPhone",orders.getUserId().getPhone());
				record.addColumn("userName",orders.getUserId().getName() );
			}
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", count.size());
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Order order, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(order.getId() + "") || order.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		orderService.delete(order.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 订单列表
	@RequestMapping(params = "bastView", method = RequestMethod.POST)
	public void bastView(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String orderNo = request.getParameter("orderNo");
		String userName = request.getParameter("userName");
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		JSonMessage message = new JSonMessage();
		if(StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)){
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		Order order = new Order();
		if(!StringUtil.isEmpty(orderNo)){
			order.setOrderNo(orderNo);
		}
		if(!StringUtil.isEmpty(companyId)){
			Company company = companyService.getCompany(Integer.valueOf(companyId));
			if(company == null){
				message.addProperty("message", "商家不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			order.setCompanyId(company);
		}

		if(!StringUtil.isEmpty(userName)){
			User user = userService.getUserName(userName);
			if(user == null){
				message.addProperty("message", "用户不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			order.setUserId(user);
		}
		List<Order> count = orderService.orderList(order);
		List<Order> orderList = orderService.orderList(order, Integer.valueOf(start), Integer.valueOf(limit));
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (Order orders : orderList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("orderId", orders.getId());
			record.addColumn("companyName", orders.getCompanyId().getName());
			record.addColumn("addTime", orders.getAddTime());
			record.addColumn("orderNo", orders.getOrderNo());
			record.addColumn("orderStatus", orders.getOrderStatus());
			String type = "";
			if("1".equals(orders.getOrderType())){
				type = "外卖";
			}
			if("2".equals(orders.getOrderType())){
				type = "美食";
			}
			record.addColumn("orderType", type);
			
			record.addColumn("total", orders.getTotal());
			record.addColumn("pay", orders.getPay());
			record.addColumn("discount", orders.getDiscount());
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", count.size());
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 订单列表
	@RequestMapping(params = "takeView", method = RequestMethod.POST)
	public void takeView(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String orderNo = request.getParameter("orderNo");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		JSonMessage message = new JSonMessage();
		if(StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)){
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		System.out.println(companyId);
		Order order = new Order();
		if(!StringUtil.isEmpty(orderNo)){
			order.setOrderNo(orderNo);
		}
		if(!StringUtil.isEmpty(companyId)){
			Company company = companyService.getCompany(Integer.valueOf(companyId));
			if(company == null){
				message.addProperty("message", "商家不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			System.out.println(company.getName());
			order.setCompanyId(company);
		}
		order.setOrderType("1");
		List<Order> count = orderService.orderList(order);
		List<Order> orderList = orderService.orderList(order, Integer.valueOf(start), Integer.valueOf(limit));
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (Order orders : orderList) {
			OrderType orderType = orderTypeService.getOrderType(orders.getId());
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("orderId", orders.getId());
			record.addColumn("companyName", orders.getCompanyId().getName());
			record.addColumn("addTime", orders.getAddTime());
			record.addColumn("orderNo", orders.getOrderNo());
			record.addColumn("orderStatus", orders.getOrderStatus());
			if (orderType != null) {
				if (orderType.getUserId() != null) {
					record.addColumn("runName", orderType.getUserId().getName());
					record.addColumn("runPhone", orderType.getUserId().getPhone());
				} else {
					record.addColumn("runName", "暂无骑手接单");
					record.addColumn("runPhone", "暂无骑手接单");
				}
				if (orderType.getAddressId() != null) {
					record.addColumn("userPhone", orderType.getAddressId().getPhone());
					record.addColumn("userName", orderType.getAddressId().getName());
					record.addColumn("userAddress", orderType.getAddressId().getAddress());
				} else {
					record.addColumn("userPhone", "自取订单");
					record.addColumn("userName", "自取订单");
					record.addColumn("userAddress", "自取订单");
				}
			} else {
				record.addColumn("userPhone", "自取订单");
				record.addColumn("userName", "自取订单");
				record.addColumn("userAddress", "自取订单");
				record.addColumn("runName", "暂无骑手接单");
				record.addColumn("runPhone", "暂无骑手接单");
			}
			record.addColumn("total", orders.getTotal());
			record.addColumn("pay", orders.getPay());
			record.addColumn("discount", orders.getDiscount());
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", count.size());
		new PushJson().outString(grid.toJSonString("list"), response);
	}

}
