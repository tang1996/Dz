package com.dz.controller;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dz.entity.Salesman;
import com.dz.service.ISalesmanService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.MD5Util;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/salesman")
public class SalesmanController {

	@Autowired
	private ISalesmanService salesmanService;

	private String sign(StringBuilder builder) {
		return MD5Util.MD5(builder.toString());
	}

	// 登陆
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public void login(Salesman salesman, HttpServletResponse response) {

		JSonMessage message = new JSonMessage();

		Salesman salesmans = salesmanService.login(salesman.getUserName());
		if (salesmans != null) {
			StringBuilder builder = new StringBuilder();
			builder.append(salesmans.getRandomCode() + salesman.getPassword());
			String localSign = sign(builder);
			if (salesmans.getUserName().equals(salesman.getUserName()) && salesmans.getPassword().equals(localSign)) {
				message.addProperty("message", "./index.html");
				message.addProperty("success", true);
			} else {
				message.addProperty("message", "账号或密码不正确!");
				message.addProperty("success", false);
			}
		} else {
			message.addProperty("message", "该帐号不存在!");
			message.addProperty("success", false);
		}

		new PushJson().outString(message.toJSonString(), response);

	}

	// 管理员列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response, Salesman salesman) {

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<Salesman> salesmanList = salesmanService.salesmanList(salesman);

		if (salesmanList != null) {
			for (Salesman salesmans : salesmanList) {
				JSonGridRecord record = new JSonGridRecord();
				record.addColumn("id", salesmans.getId());
				record.addColumn("user_name", salesmans.getUserName());
				record.addColumn("password", salesmans.getPassword());
				record.addColumn("random_code", salesmans.getRandomCode());
				record.addColumn("name", salesmans.getName());
				record.addColumn("e_mail", salesmans.getEmail());

				grid.addRecord(record);
				grid.addProperties("totalCount", salesmanList.size());
				grid.addProperties("totalSum", salesmanList.size());
			}
		} else {
			JSonGridRecord record = new JSonGridRecord();
			grid.addRecord(record);
			grid.addProperties("totalCount", 0);
			grid.addProperties("totalSum", 0);
		}
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除管理员
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Salesman salesman, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(salesman.getId() + "") || salesman.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
		} else {
			salesmanService.delete(salesman.getId() + "");
			message.addProperty("message", "删除成功");
			message.addProperty("success", true);
			new PushJson().outString(message.toJSonString(), response);
		}
	}

	// 添加或修改管理员 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, Salesman salesman, HttpServletResponse response) {
		int id = salesman.getId();
		JSonMessage message = new JSonMessage();

		Random random = new Random();
		int random_numb = random.nextInt(99999999) % (99999999 - 10000000 + 1) + 10000000;

		StringBuilder builder = new StringBuilder();
		if (id == 0) {
			List<Salesman> salesman_name = salesmanService.getuserName(salesman.getUserName());
			if (salesman_name.size() > 0) {
				message.addProperty("message", "该账号已存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			builder.append(random_numb + salesman.getPassword());
			String localSign = sign(builder);
			Salesman salesmans = new Salesman();
			salesmans.setUserName(salesman.getUserName());
			salesmans.setPassword(localSign);
			salesmans.setRandomCode(random_numb + "");
			salesmans.setName(salesman.getName());
			salesmans.setEmail(salesman.getEmail());
			salesmanService.saveORupdate(salesmans);
		} else {
			Salesman salesmann = salesmanService.getid(Integer.valueOf(id));
			if (!salesmann.getUserName().equals(salesman.getUserName())) {
				message.addProperty("message", "不能修改用户账号");
				message.addProperty("success", true);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			if (salesman.getPassword().equals(salesmann.getPassword())) {
				salesman.setPassword(salesman.getPassword());
			} else {
				builder.append(salesmann.getRandomCode() + salesman.getPassword());
				String localSign = sign(builder);
				salesmann.setPassword(localSign);
			}
			salesmann.setName(salesman.getName());
			salesmann.setEmail(salesman.getEmail());
			salesmanService.saveORupdate(salesmann);
		}
		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}