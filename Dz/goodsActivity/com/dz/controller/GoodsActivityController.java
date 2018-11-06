package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.GoodsActivity;
import com.dz.service.IGoodsActivityService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/goodsActivity")
public class GoodsActivityController {

	@Autowired
	private IGoodsActivityService goodsActivityService;

	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response,
			GoodsActivity goodsActivity) {

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<GoodsActivity> goodsActivityList = goodsActivityService.goodsActivityList(goodsActivity);

		for (GoodsActivity goodsActivitys : goodsActivityList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", goodsActivitys.getId());
			record.addColumn("activity_id", goodsActivitys.getActivityId());

			grid.addRecord(record);
		}
		grid.addProperties("totalCount", goodsActivityList.size());
		grid.addProperties("totalSum", goodsActivityList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 通过首页活动查询搞活动商品
	@RequestMapping(params = "getGoods", method = RequestMethod.POST)
	public void getGoods(HttpServletRequest request,
			HttpServletResponse response, GoodsActivity goodsActivity) {
		String id = request.getParameter("id");// 总活动ID
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		JSonGrid grid = new JSonGrid();
		List<GoodsActivity> goodsActivityList = goodsActivityService.getList(Integer
				.valueOf(id));

		if (goodsActivityList != null) {
			for (GoodsActivity goodsActivitys : goodsActivityList) {
				JSonGridRecord record = new JSonGridRecord();
				record.addColumn("id", goodsActivitys.getId());
				record.addColumn("benefit", goodsActivitys.getBenefit());
				record.addColumn("balance", goodsActivitys.getBalance());
				record.addColumn("content", goodsActivitys.getContent());
				record.addColumn("depict", goodsActivitys.getDepict());
				record.addColumn("startTime", goodsActivitys.getStartTime());
				record.addColumn("endTime", goodsActivitys.getEndTime());
				record.addColumn("img", goodsActivitys.getImg());
				record.addColumn("svg", goodsActivitys.getSvg());
				record.addColumn("goodsName", goodsActivitys.getGoodsId().getName());
				record.addColumn("goodsPrice", goodsActivitys.getGoodsId()
						.getPrice());
				record.addColumn("goodsZoom", goodsActivitys.getGoodsId()
						.getZoomUrl());
				record.addColumn("goodsId", goodsActivitys.getGoodsId());

				grid.addRecord(record);

			}
		}

		grid.addProperties("totalCount", goodsActivityList.size());

		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, GoodsActivity goodsActivity,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(goodsActivity.getId() + "") || goodsActivity.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		goodsActivityService.delete(goodsActivity.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, GoodsActivity goodsActivity,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		goodsActivityService.saveORupdate(goodsActivity);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}