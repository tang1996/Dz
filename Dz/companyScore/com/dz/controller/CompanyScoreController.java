package com.dz.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.CompanyScore;
import com.dz.service.ICompanyScoreService;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/companyScore")
public class CompanyScoreController {

	@Autowired
	private ICompanyScoreService companyScoreService;
	
	// 查询列表
//	@RequestMapping(params = "view", method = RequestMethod.POST)
//	public void view(HttpServletRequest request, HttpServletResponse response,
//			CompanyScore companyScore) {
//		String token = request.getParameter("token");
//		// token = "cOZ6cjmF9NF";
//		
//		if(StringUtil.isEmpty(token)){
//			JSonMessage message = new JSonMessage();
//			message.addProperty("message", "token不能为空");
//			message.addProperty("success", false);
//			new PushJson().outString(message.toJSonString(), response);
//			return;
//		}
//		
//		
//		User user = userService.gettoken(token);
//		if (user.equals(null)) {
//			JSonMessage message = new JSonMessage();
//			message.addProperty("message", "token验证失败");
//			message.addProperty("success", false);
//			new PushJson().outString(message.toJSonString(), response);
//			return;
//		}
//		
//		JSonGrid grid = new JSonGrid();
//		grid.addProperties("success", true);
//
//		CompanyScore companyScoreList = companyScoreService.getCompanyScore(user.getId());
//
//		for (CompanyScore companyScores : companyScoreList) {
//			JSonGridRecord record = new JSonGridRecord();
//			record.addColumn("id", companyScores.getId());
//			record.addColumn("name", companyScores.getName());
//			record.addColumn("is_show", companyScores.getIsShow());
//			record.addColumn("url", companyScores.getUrl());
//
//			grid.addRecord(record);
//		}
//		grid.addProperties("totalCount", companyScoreList.size());
//		
//		new PushJson().outString(grid.toJSonString("list"), response);
//
//	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, CompanyScore companyScore,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyScore.getId() + "") || companyScore.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
		} else {
			companyScoreService.delete(companyScore.getId() + "");
			message.addProperty("message", "删除成功");
			message.addProperty("success", true);
			new PushJson().outString(message.toJSonString(), response);
		}
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, CompanyScore companyScore,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		companyScoreService.saveORupdate(companyScore);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}