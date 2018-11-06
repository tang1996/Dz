package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.OrderType;
import com.dz.entity.User;
import com.dz.service.IOrderTypeService;
import com.dz.service.IUserService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/base/orderType")
public class BaseOrderTypeController {
	
	@Autowired
	private IOrderTypeService orderTypeService;
	
	@Autowired
	private IUserService userService;

	// 骑手订单
		@RequestMapping(params = "riderView", method = RequestMethod.POST)
		public void riderView(HttpServletRequest request, HttpServletResponse response) {
			String userName = request.getParameter("userName");
			
			String start = request.getParameter("start");	/*ynw start*/
			String limit = request.getParameter("limit");	/*ynw end*/
			
			JSonMessage message = new JSonMessage();
			OrderType orderType = new OrderType();
			if (!StringUtil.isEmpty(userName)) {
				User user = userService.getPhone(userName);
				if (user == null) {
					message.addProperty("message", "骑手不存在");
					message.addProperty("success", false);
					new PushJson().outString(message.toJSonString(), response);
					return;
				}
				orderType.setUserId(user);
			}
			
			if(StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)){		/*ynw start*/
				message.addProperty("message", "缺少必要参数");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}	/*ynw end*/
			
			List<OrderType> count = orderTypeService.getBastList(orderType);	/*ynw*/
			List<OrderType> orderTypeList = orderTypeService.getBastList(orderType ,Integer.valueOf(start), Integer.valueOf(limit));	/*ynw*/
			JSonGrid grid = new JSonGrid();
			grid.addProperties("success", true);
			for (OrderType orderTypes : orderTypeList) {
				JSonGridRecord record = new JSonGridRecord();
				if (orderTypes.getAddressId() != null) {
					record.addColumn("userPhone", orderTypes.getAddressId().getPhone());
					record.addColumn("userName", orderTypes.getAddressId().getName());
					record.addColumn("userAddress", orderTypes.getAddressId().getAddress());
				}

				if (orderTypes.getOrderId() != null) {
					record.addColumn("addTime", orderTypes.getOrderId().getAddTime());
					record.addColumn("orderId", orderTypes.getOrderId().getId());
					record.addColumn("orderNo", orderTypes.getOrderId().getOrderNo());
					record.addColumn("status", orderTypes.getOrderId().getOrderStatus());
					record.addColumn("finishTime", orderTypes.getOrderId().getFinishTime());
				}

				if (orderTypes.getUserId() != null) {
					record.addColumn("riderPhone", orderTypes.getUserId().getPhone());
					record.addColumn("riderName", orderTypes.getUserId().getName());
				}

				record.addColumn("cost", orderTypes.getPrice());
				record.addColumn("shippingTime", orderTypes.getShippingTime());

				grid.addRecord(record);
			}
			grid.addProperties("totalCount", count.size());		/*ynw*/
			new PushJson().outString(grid.toJSonString("list"), response);
		}

}