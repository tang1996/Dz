package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Label;
import com.dz.service.ILabelService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/label")
public class LabelController {

	@Autowired
	private ILabelService labelService;

	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response) {
		String customId = request.getParameter("customId");
		
		if (StringUtil.isEmpty(customId)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "customId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		Label label = new Label();
		label.setCustomId(customId);
		label.setType("comDefect");
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<Label> labelList = labelService.labelList(label);

		for (Label labels : labelList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", labels.getId());
			record.addColumn("type", labels.getType());
			record.addColumn("content", labels.getContent());

			grid.addRecord(record);
		}
		grid.addProperties("totalCount", labelList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}
	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Label label,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(label.getId() + "") || label.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		labelService.delete(label.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, Label label,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		labelService.saveORupdate(label);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}