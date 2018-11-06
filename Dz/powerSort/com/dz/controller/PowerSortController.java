package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.PowerSort;
import com.dz.service.IPowerSortService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/powerSort")
public class PowerSortController {

	@Autowired
	private IPowerSortService powerSortService;

	// 用户列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response, PowerSort powerSort) {
//		String userName = request.getParameter("userName");
//		String userName = request.getParameter("userName");
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<PowerSort> powerSortList = powerSortService.powerSortList(powerSort);

		for (PowerSort powerSorts : powerSortList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", powerSorts.getId());
			record.addColumn("name", powerSorts.getName());
			record.addColumn("info", powerSorts.getInfo());

			grid.addRecord(record);
		}
		grid.addProperties("totalCount", powerSortList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除用户
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, PowerSort powerSort, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(powerSort.getId() + "") || powerSort.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		powerSortService.delete(powerSort.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加
	@RequestMapping(params = "save", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, PowerSort powerSort, HttpServletResponse response) {
		int id = powerSort.getId();
		JSonMessage message = new JSonMessage();

		PowerSort powerSorts = new PowerSort();
		powerSortService.saveORupdate(powerSorts);
		PowerSort powerSortn = powerSortService.getid(Integer.valueOf(id));
		powerSortn.setName(powerSort.getName());
		powerSortn.setInfo(powerSort.getInfo());
		powerSortService.saveORupdate(powerSortn);
		message.addProperty("message", "添加成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}


}
