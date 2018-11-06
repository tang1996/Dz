package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Attribute;
import com.dz.entity.Nature;
import com.dz.service.IAttributeService;
import com.dz.service.INatureService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/nature")
public class NatureController {

	@Autowired
	private INatureService natureService;

	@Autowired
	private IAttributeService attributeService;

	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response,
			Nature nature) {
		String goodsId = request.getParameter("goodsId");

		if (StringUtil.isEmpty(goodsId)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "goodsId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
		}
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		List<Attribute> list = attributeService.getattribute(Integer
				.valueOf(goodsId));
		if (list != null) {
			for (Attribute attribute : list) {
				JSonGridRecord record = new JSonGridRecord();
				record.addColumn("attributeName", attribute.getName());
				record.addColumn("attributeId", attribute.getId());
				StringBuilder content = new StringBuilder();
				List<Nature> natureList = natureService.getnature(attribute
						.getId());
				for (Nature natures : natureList) {
					content.append(natures.getId() + "," + natures.getContent()
							+ ".");
				}
				record.addColumn("natures", content.toString());
				grid.addRecord(record);
			}
		}

		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 商品属性详情
	@RequestMapping(params = "find", method = RequestMethod.POST)
	public void find(HttpServletRequest request, HttpServletResponse response) {
		String attributeId = request.getParameter("attributeId");

		if (StringUtil.isEmpty(attributeId)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "attributeId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
		}

		List<Nature> natureList = natureService.getnature(Integer
				.valueOf(attributeId));
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (Nature natures : natureList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", natures.getId());
			record.addColumn("content", natures.getContent());
			grid.addRecord(record);
		}
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Nature nature,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(nature.getId() + "") || nature.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
			natureService.delete(nature.getId() + "");
			message.addProperty("message", "删除成功");
			message.addProperty("success", true);
			new PushJson().outString(message.toJSonString(), response);
	}

	// 添加信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, HttpServletResponse response) {
		
		String attributeId = request.getParameter("attributeId");
		String content = request.getParameter("content");
		JSonMessage message = new JSonMessage();
		if(StringUtil.isEmpty(attributeId) || StringUtil.isEmpty(content)){
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Attribute attribute = attributeService.getAttribute(Integer.valueOf(attributeId));
		if(attribute == null){
			message.addProperty("message", "找不到对应的属性");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		Nature nature = new Nature();
		nature.setAttributeId(attribute);
		nature.setContent(content);
		natureService.saveORupdate(nature);

		message.addProperty("message", "添加成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}
	
}