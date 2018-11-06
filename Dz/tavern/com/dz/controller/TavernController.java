package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Tavern;
import com.dz.service.ITavernService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/tavern")
public class TavernController {

	@Autowired
	private ITavernService tavernService;

	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response) {
		String stayId = request.getParameter("stayId");
		// token = "cOZ6cjmF9NF";

		if (StringUtil.isEmpty(stayId)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "stayId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<Tavern> tavernList = tavernService.getTavern(Integer.valueOf(stayId));

		for (Tavern taverns : tavernList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", taverns.getId());
			record.addColumn("startTime", taverns.getStartTime());
			record.addColumn("endTime", taverns.getEndTime());
			record.addColumn("stayId", taverns.getStayId().getId());
			record.addColumn("userName", taverns.getUserId().getName());
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", tavernList.size());

		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Tavern tavern,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(tavern.getId() + "") || tavern.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		tavernService.delete(tavern.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, Tavern tavern,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		tavernService.saveORupdate(tavern);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}