package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Stay;
import com.dz.service.IStayService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/stay")
public class StayController {

	@Autowired
	private IStayService stayService;
	
	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String haveRoom = request.getParameter("haveRoom");
		// token = "cOZ6cjmF9NF";
		
		if (StringUtil.isEmpty(companyId)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "comId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<Stay> stayList = stayService.getStay(Integer.valueOf(companyId),Integer.valueOf(haveRoom));

		for (Stay stays : stayList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", stays.getId());
			record.addColumn("isReserve", stays.getIsReserve());
			record.addColumn("level", stays.getLevel());
			record.addColumn("type", stays.getType());
			record.addColumn("goodsId", stays.getGoodsId().getName());
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", stayList.size());

		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Stay stay, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(stay.getId() + "") || stay.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		stayService.delete(stay.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, Stay stay, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		stayService.saveORupdate(stay);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}