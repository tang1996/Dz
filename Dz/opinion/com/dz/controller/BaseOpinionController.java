package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Opinion;
import com.dz.service.IOpinionService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/base/opinion")
public class BaseOpinionController {

	@Autowired
	private IOpinionService opinionService;

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Opinion opinion, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(opinion.getId() + "") || opinion.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		opinionService.delete(opinion.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 反馈列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void save(HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");// 起始页
		String limit = request.getParameter("limit");// 容量

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		Opinion opinions = new Opinion();
		List<Opinion> opinionList = opinionService.opinionList(opinions,Integer.valueOf(start), Integer.valueOf(limit));

		for (Opinion opinion : opinionList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", opinion.getId());
			record.addColumn("phone", opinion.getPhone());
			record.addColumn("create_time", opinion.getCreateTime());
			record.addColumn("content", opinion.getContent());

			grid.addRecord(record);
		}
		grid.addProperties("totalCount", opinionList.size());
		new PushJson().outString(grid.toJSonString("list"), response);
	}

}