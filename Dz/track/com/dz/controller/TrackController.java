package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Order;
import com.dz.entity.OrderType;
import com.dz.entity.Track;
import com.dz.service.IOrderService;
import com.dz.service.IOrderTypeService;
import com.dz.service.ITrackService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/track")
public class TrackController {

	@Autowired
	private ITrackService trackService;

	@Autowired
	private IOrderService orderService;

	@Autowired
	private IOrderTypeService orderTypeService;

	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		// orderNo = "201805012111441";
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Order order = orderService.track(id);
		if (order == null) {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Track> tracks = trackService.getTrack(order.getId());
		if (tracks.size() == 0) {
			message.addProperty("message", "tracks不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		OrderType orderType = orderTypeService.getOrderType(order.getId());
		if (orderType == null) {
			message.addProperty("message", "orderType不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (Track track : tracks) {

			JSonGridRecord record = new JSonGridRecord();
			if (track.getStatus().equals("taking")) {
				if (orderType.getUserId() != null) {
					record.addColumn("runName", orderType.getUserId().getName()
							+ "抢到订单");
					record.addColumn("runPhone", orderType.getUserId()
							.getPhone());
				}
			}
			record.addColumn("status", track.getStatus());
			record.addColumn("bewrite", track.getBewrite());
			record.addColumn("createTime", track.getCreateTime());
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", tracks.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 预定结果
	@RequestMapping(params = "resutl", method = RequestMethod.POST)
	public void resutl(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		// orderNo = "201805012111441";
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "orderId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Track> tracks = trackService.getTracks(Integer.valueOf(orderId));
		if (tracks == null) {
			message.addProperty("message", "tracks不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		message.addProperty("success", true);
		String payTime = "";
		String confirmTime = "";
		String content = "";
		String status = "1";
		for (Track track : tracks) {
			if (track.getStatus().equals("pay")) {
				payTime = track.getCreateTime();
				status = "2";
				confirmTime = "等待商家确认";
			}
			if (track.getStatus().equals("confirm")) {
				confirmTime = track.getCreateTime();
				content = track.getBewrite();
				status = "3";
			}
			if (track.getStatus().equals("backBalance")) {
				confirmTime = track.getCreateTime();
				content = track.getBewrite();
				status = "3";
			}
			if (track.getStatus().equals("unconfirm")) {
				confirmTime = track.getCreateTime();
				content = track.getBewrite();
				status = "3";
			}
		}
		Order order = orderService.getOrder(Integer.valueOf(orderId));
		if (order != null) {
			message.addProperty("orderNo", order.getOrderNo());
			if (order.getCompanyId() != null) {
				message.addProperty("companyName", order.getCompanyId()
						.getName());
				message.addProperty("companyPhone", order.getCompanyId()
						.getPhone());
			}
		}

		message.addProperty("content", content);
		message.addProperty("payTime", payTime);
		message.addProperty("confirmTime", confirmTime);
		message.addProperty("status", status);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Track track,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(track.getId() + "") || track.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		trackService.delete(track.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, Track track,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		trackService.saveORupdate(track);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}