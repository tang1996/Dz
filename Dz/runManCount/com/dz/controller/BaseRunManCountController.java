package com.dz.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
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
@RequestMapping("/base/runManCount")
public class BaseRunManCountController {

	@Autowired
	private IOrderTypeService orderTypeService;

	@Autowired
	private IUserService userService;

	// 骑手订单统计
	@RequestMapping(params = "riderCount", method = RequestMethod.POST)
	public void riderCount(HttpServletRequest request, HttpServletResponse response) {

		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String userName = request.getParameter("userName");

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime)) {
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			startTime = date;
			endTime = date;
		}
		OrderType orderType = new OrderType();
		if (!StringUtil.isEmpty(userName)) {
			User user = userService.getUserName(userName);
			if (user == null) {
				message.addProperty("message", "用户不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			orderType.setUserId(user);
		}

		List<OrderType> totalCount = orderTypeService.getBastCountList(startTime, endTime, orderType);
		List<OrderType> list = orderTypeService.getBastCountList(startTime, endTime, orderType, Integer.valueOf(start),
				Integer.valueOf(limit));
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (OrderType orderTypes : list) {
			User rider = orderTypes.getUserId();
			if (rider != null) {
				JSonGridRecord record = new JSonGridRecord();
				Object[] count = orderTypeService.getBastCount(startTime, endTime, rider.getId());
				if (count == null) {
					record.addColumn("balance", 0 + "元");// 本月收益
					record.addColumn("num", 0);// 本月单量
				} else {
					record.addColumn("balance", count[1] + "元");// 本月收益
					record.addColumn("num", count[0]);// 本月收益
				}
				record.addColumn("name", rider.getName());// 骑手姓名
				record.addColumn("phone", rider.getPhone());// 联系方式
				grid.addRecord(record);
			}

		}
		grid.addProperties("totalCount", totalCount.size());
		new PushJson().outString(grid.toJSonString("list"), response);
	}
}