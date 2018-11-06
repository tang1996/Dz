package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.DisTime;
import com.dz.service.IDisTimeService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/disTime")
public class DisTimeController {

	@Autowired
	private IDisTimeService disTimeService;

	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response,
			DisTime disTime) {
		if (StringUtil.isEmpty(disTime.getCompanyId())) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "companyId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<DisTime> disTimeList = disTimeService.disTimeList(disTime);

		 for (DisTime disTimes : disTimeList) {
		JSonGridRecord record = new JSonGridRecord();
		 record.addColumn("id", disTimes.getId());
		 record.addColumn("time", disTimes.getTime());
		 record.addColumn("price", disTimes.getPrice());

		grid.addRecord(record);
		 }
		grid.addProperties("totalCount", disTimeList.size());

		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, DisTime disTime,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(disTime.getId() + "") || disTime.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		disTimeService.delete(disTime.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, DisTime disTime,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		disTimeService.saveORupdate(disTime);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}