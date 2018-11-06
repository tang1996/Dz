package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Activity;
import com.dz.service.IActivityService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/activity")
public class ActivityController {

	@Autowired
	private IActivityService activityService;

	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response,
			Activity activity) {

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<Activity> activityList = activityService.activityList(activity);

		for (Activity activitys : activityList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", activitys.getId());
			record.addColumn("name", activitys.getName());
			record.addColumn("logo", activitys.getLogo());

			grid.addRecord(record);
		}
		grid.addProperties("totalCount", activityList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Activity activity,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(activity.getId() + "") || activity.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		activityService.delete(activity.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, Activity activity,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		activityService.saveORupdate(activity);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}