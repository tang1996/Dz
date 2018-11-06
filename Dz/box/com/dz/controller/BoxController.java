package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Box;
import com.dz.service.IBoxService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/box")
public class BoxController {

	@Autowired
	private IBoxService boxService;

	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response) {
		String goodsId = request.getParameter("goodsId");
		// token = "cOZ6cjmF9NF";

		if (StringUtil.isEmpty(goodsId)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "goodsId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<Box> boxList = boxService.getBox(Integer.valueOf(goodsId));

		for (Box boxs : boxList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", boxs.getId());
			record.addColumn("startTime", boxs.getStartTime());
			record.addColumn("continuedTime", boxs.getContinuedTime());
			record.addColumn("isFull", boxs.getIsFull());
			record.addColumn("consumption", boxs.getConsumption());
			record.addColumn("theme", boxs.getTheme());
			record.addColumn("goodsName", boxs.getGoodsId().getName());
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", boxList.size());

		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Box box,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(box.getId() + "") || box.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		boxService.delete(box.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, Box box,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		boxService.saveORupdate(box);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}