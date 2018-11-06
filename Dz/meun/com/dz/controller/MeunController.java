package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Meun;
import com.dz.entity.User;
import com.dz.service.IMeunService;
import com.dz.service.IUserService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/meun")
public class MeunController {

	@Autowired
	private IMeunService meunService;

	@Autowired
	private IUserService userService;

	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response,
			Meun meun) {
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

		List<Meun> meunList = meunService.getmeun(user.getId());
		int i = 0;

		for (Meun meuns : meunList) {
			if (meuns.getIsShow()) {
				JSonGridRecord record = new JSonGridRecord();
				record.addColumn("id", meuns.getId());
				record.addColumn("name", meuns.getName());
//				record.addColumn("is_show", meuns.getIsShow());
				record.addColumn("url", meuns.getUrl());
				i++;
				grid.addRecord(record);
			}
		}
		grid.addProperties("totalCount", i);

		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Meun meun,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(meun.getId() + "") || meun.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		meunService.delete(meun.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, Meun meun,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		meunService.saveORupdate(meun);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}