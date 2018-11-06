package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.RunEvaluate;
import com.dz.entity.User;
import com.dz.service.IRunEvaluateService;
import com.dz.service.IUserService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/base/runEvaluate")
public class BaseRunEvaluateController {

	@Autowired
	private IRunEvaluateService runEvaluateService;
	
	@Autowired
	private IUserService userService;
	
	//综合平均评分 ynw
	@RequestMapping(params = "multiple", method = RequestMethod.POST)
	public void multiple(HttpServletRequest request, HttpServletResponse response){

		String user_Name = request.getParameter("user_Name");
		String user_Phone = request.getParameter("user_Phone");
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
	
		JSonGrid grid = new JSonGrid();
		
		User user = new User();
		user.setIsDistribution(true);
		user.setName(user_Name);
		user.setPhone(user_Phone);
		
		List<User> count = userService.userList(user);	/*ynw*/
		List<User> userList = userService.userList(user, Integer.valueOf(start), Integer.valueOf(limit));	/*ynw*/
		
		for(User u : userList){
			JSonGridRecord record = new JSonGridRecord();
			RunEvaluate runEvaluate = new RunEvaluate();
			runEvaluate.setRunId(u.getId());
			List<RunEvaluate> runEvaluateList =  runEvaluateService.getrunEvaluate(runEvaluate, Integer.valueOf(start), Integer.valueOf(limit));
			if(runEvaluateList.size()>0){
				double total_manner = 5.0;
				double total_speen = 5.0;
				for(RunEvaluate r : runEvaluateList){
					total_manner =  total_manner + Double.valueOf(r.getManner()) ;
					total_speen = total_speen + Double.valueOf(r.getSpeen()) ;
				}
				record.addColumn("avg_speen", String.format("%.1f", total_speen / (runEvaluateList.size()+1)));
				record.addColumn("avg_manner", String.format("%.1f", total_manner / (runEvaluateList.size()+1)) );

			}else{
				record.addColumn("avg_speen", "5.0");
				record.addColumn("avg_manner", "5.0");
			}
			record.addColumn("order_count", runEvaluateList.size());
			record.addColumn("runid", u.getId());
			record.addColumn("user_phone", u.getPhone());
			record.addColumn("user_name", u.getName());
			record.addColumn("user_nickname", u.getNickname());
			record.addColumn("username", u.getUserName());
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", count.size());	/*ynw*/
		grid.addProperties("success", true);
		new PushJson().outString(grid.toJSonString("list"), response);
	}
	
	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, RunEvaluate runEvaluate,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(runEvaluate.getId() + "") || runEvaluate.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
		} else {
			runEvaluateService.delete(runEvaluate.getId() + "");
			message.addProperty("message", "删除成功");
			message.addProperty("success", true);
			new PushJson().outString(message.toJSonString(), response);
		}
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, RunEvaluate runEvaluate,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		runEvaluateService.saveORupdate(runEvaluate);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}