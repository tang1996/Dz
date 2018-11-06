package com.dz.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Parcel;
import com.dz.service.IParcelService;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/parcel")
public class ParcelController {

	@Autowired
	private IParcelService parcelService;
	
	// 查询列表
//	@RequestMapping(params = "view", method = RequestMethod.POST)
//	public void view(HttpServletRequest request, HttpServletResponse response,
//			Parcel parcel) {
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
//		Parcel parcelList = parcelService.getparcel(user.getId());
//
//		for (Parcel parcels : parcelList) {
//			JSonGridRecord record = new JSonGridRecord();
//			record.addColumn("id", parcels.getId());
//			record.addColumn("name", parcels.getAttitude());
//			record.addColumn("is_show", parcels.getTaste());
//			record.addColumn("url", parcels.getUrl());
//
//			grid.addRecord(record);
//		}
//		grid.addProperties("totalCount", parcelList.size());
//		
//		new PushJson().outString(grid.toJSonString("list"), response);
//
//	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Parcel parcel,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(parcel.getId() + "") || parcel.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
		} else {
			parcelService.delete(parcel.getId() + "");
			message.addProperty("message", "删除成功");
			message.addProperty("success", true);
			new PushJson().outString(message.toJSonString(), response);
		}
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, Parcel parcel,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		parcelService.saveORupdate(parcel);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}