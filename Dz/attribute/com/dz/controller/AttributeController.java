package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Attribute;
import com.dz.entity.Goods;
import com.dz.entity.Nature;
import com.dz.service.IAttributeService;
import com.dz.service.IGoodsService;
import com.dz.service.INatureService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.JSonTree;
import com.dz.util.JSonTreeNode;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/attribute")
public class AttributeController {

	@Autowired
	private IAttributeService attributeService;

	@Autowired
	private INatureService natureService;

	@Autowired
	private IGoodsService goodsService;

	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response,
			Attribute attribute) {
		String goodsId = request.getParameter("goodId");

		if (StringUtil.isEmpty(goodsId)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "goodId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
		}
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<Attribute> attributeList = attributeService.getattribute(Integer
				.valueOf(goodsId));

		for (Attribute attributes : attributeList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", attributes.getId());
			record.addColumn("name", attributes.getName());
			List<Nature> natures = natureService.getnature(attributes.getId());
			StringBuilder content = new StringBuilder();
			int count = 0;
			for (Nature nature : natures) {
				content
						.append(nature.getId() + "," + nature.getContent()
								+ ";");
				record.addColumn("content", content.toString());
				count++;
			}
			record.addColumn("count", count);
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", attributeList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 查询列表
	@RequestMapping(params = "getView", method = RequestMethod.POST)
	public void getView(HttpServletRequest request, HttpServletResponse response) {
		String goodsId = request.getParameter("goodId");

		if (StringUtil.isEmpty(goodsId)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "goodId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<Attribute> attributeList = attributeService.getattribute(Integer
				.valueOf(goodsId));

		JSonTree json = new JSonTree();
		for (Attribute attributes : attributeList) {
			JSonTreeNode node = new JSonTreeNode();
			node.addProperty("id", attributes.getId());
			node.addProperty("name", attributes.getName());
			List<Nature> natures = natureService.getnature(attributes.getId());
			for (Nature nature : natures) {
				JSonTreeNode node1 = new JSonTreeNode();
				node1.addProperty("id", nature.getId());
				node1.addProperty("content", nature.getContent());
				node.addProperty("leaf", true);
				node.addChild(node1);
			}
			json.addNode(node);
		}

		new PushJson().outString(json.toJSonString(), response);
	}

	// 查询列表
	@RequestMapping(params = "getAttribute", method = RequestMethod.POST)
	public void getAttribute(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Attribute attribute = attributeService
				.getAttribute(Integer.valueOf(id));
		if (attribute == null) {
			message.addProperty("message", "属性不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		message.addProperty("success", true);
		message.addProperty("id", attribute.getId());
		message.addProperty("name", attribute.getName());
		new PushJson().outString(message.toJSonString(), response);

	}

	// 商品属性详情
	@RequestMapping(params = "find", method = RequestMethod.POST)
	public void find(HttpServletRequest request, HttpServletResponse response) {
		String goodsId = request.getParameter("goodId");

		if (StringUtil.isEmpty(goodsId)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "goodId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
		}
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<Attribute> attributeList = attributeService.getattribute(Integer
				.valueOf(goodsId));

		for (Attribute attributes : attributeList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", attributes.getId());
			record.addColumn("name", attributes.getName());
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", attributeList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Attribute attribute,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(attribute.getId() + "")
				|| attribute.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		} else {
			attributeService.delete(attribute.getId() + "");
			message.addProperty("message", "删除成功");
			message.addProperty("success", true);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request,
			HttpServletResponse response) {
		String goodsId = request.getParameter("goodsId");
		String name = request.getParameter("name");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(goodsId) || StringUtil.isEmpty(name)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Goods goods = goodsService.getGoods(Integer.valueOf(goodsId));

		if (goods == null) {
			message.addProperty("message", "商品不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Attribute attribute = new Attribute();
		attribute.setGoodsId(goods);
		attribute.setName(name);
		attributeService.saveORupdate(attribute);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		message.addProperty("attributeId", attribute.getId());
		new PushJson().outString(message.toJSonString(), response);
	}

	// 修改信息
	@RequestMapping(params = "save", method = RequestMethod.POST)
	public void save(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String name = request.getParameter("name");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(name)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Attribute attribute = attributeService
				.getAttribute(Integer.valueOf(id));
		if (attribute == null) {
			message.addProperty("message", "属性不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		attribute.setName(name);
		attributeService.saveORupdate(attribute);

		message.addProperty("message", "修改信息成功");
		message.addProperty("success", true);
		message.addProperty("attributeId", attribute.getId());
		new PushJson().outString(message.toJSonString(), response);
	}

}