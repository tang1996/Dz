package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Company;
import com.dz.entity.Include;
import com.dz.entity.User;
import com.dz.service.ICompanyService;
import com.dz.service.IIncludeService;
import com.dz.service.IUserService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/base/include")
public class BaseIncludeController {

	@Autowired
	private IIncludeService includeService;

	@Autowired
	private IUserService userService;

	@Autowired
	private ICompanyService companyService;

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		String ids = request.getParameter("ids");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(ids)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		String[] id = ids.split(",");
		for (int i = 0; i < id.length; i++) {
			Include include = includeService.include(Integer.valueOf(id[i]));

			includeService.delete(include.getId() + "");
			message.addProperty("message", "删除成功");
			message.addProperty("success", true);
		}
		new PushJson().outString(message.toJSonString(), response);
	}

	// 后台列表
	@RequestMapping(params = "bastView", method = RequestMethod.POST)
	public void bastView(HttpServletRequest request, HttpServletResponse response) {
		String userName = request.getParameter("userName");
		String companyId = request.getParameter("companyId");
		
		String start  = request.getParameter("start");	/*ynw*/
		String limit = request.getParameter("limit");	/*ynw*/

		JSonMessage message = new JSonMessage();
		Include include = new Include();
		if (!StringUtil.isEmpty(userName)) {
			User user = userService.getUserName(userName);
			if (user == null) {
				message.addProperty("message", "用户不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			include.setUserId(user);
		}
		if (!StringUtil.isEmpty(companyId)) {
			Company company = companyService.getCompany(Integer.valueOf(companyId));
			if (company == null) {
				message.addProperty("message", "商家不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			include.setCompanyId(company);
		}
		List<Include> count = includeService.includeList(include);	/*ynw*/
		List<Include> includeList = includeService.includeList(include, Integer.valueOf(start), Integer.valueOf(limit));	/*ynw*/
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (Include includes : includeList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", includes.getId());
			if (includes.getCompanyId() != null) {
				Company com = includes.getCompanyId();
				record.addColumn("companyName", com.getName());
				record.addColumn("companyId", com.getId());
			}
			if (includes.getUserId() != null) {
				User us = includes.getUserId();
				record.addColumn("userName", us.getUserName());
				record.addColumn("userId", us.getId());
			}
			grid.addRecord(record);

		}
		grid.addProperties("totalCount", count.size());	/*ynw*/
		new PushJson().outString(grid.toJSonString("list"), response);

	}
}