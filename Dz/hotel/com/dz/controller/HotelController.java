package com.dz.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Hotel;
import com.dz.entity.User;
import com.dz.service.IHotelService;
import com.dz.service.IUserService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/hotel")
public class HotelController {

	@Autowired
	private IHotelService hotelService;

	@Autowired
	private IUserService userService;

	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response,
			Hotel hotel) {
		String token = request.getParameter("token");
		// token = "cOZ6cjmF9NF";

		if (StringUtil.isEmpty(token)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "token不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);
		if (user == null) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "token验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		Hotel hotelList = hotelService.getHotel(user.getId());

//		for (Hotel hotels : hotelList) {
			JSonGridRecord record = new JSonGridRecord();
//			record.addColumn("id", hotels.getId());
//			record.addColumn("tableNo", hotels.getTableNo());
//			record.addColumn("createTime", hotels.getCreateTime());
//			record.addColumn("endTime", hotels.getEndTime());
//			record.addColumn("isReserve", hotels.getIsReserve());
//			record.addColumn("deposit", hotels.getDeposit());
//			record.addColumn("seat", hotels.getSeat());
//			record.addColumn("userName", hotels.getUserId().getName());

			grid.addRecord(record);
//		}
		grid.addProperties("totalCount", 0);

		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Hotel hotel,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(hotel.getId() + "") || hotel.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		hotelService.delete(hotel.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, Hotel hotel,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		hotelService.saveORupdate(hotel);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}