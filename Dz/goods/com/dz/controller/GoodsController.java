package com.dz.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dz.entity.Attribute;
import com.dz.entity.Company;
import com.dz.entity.Goods;
import com.dz.entity.Ification;
import com.dz.entity.Label;
import com.dz.entity.Nature;
import com.dz.entity.Stay;
import com.dz.service.IAttributeService;
import com.dz.service.ICompanyService;
import com.dz.service.IGoodsService;
import com.dz.service.IIficationService;
import com.dz.service.ILabelService;
import com.dz.service.INatureService;
import com.dz.service.IStayService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.MD5Util;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;
import com.dz.util.URL;

@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	private IGoodsService goodsService;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private IIficationService ificationService;

	@Autowired
	private IAttributeService attributeService;

	@Autowired
	private INatureService natureService;

	@Autowired
	private IStayService stayService;

	@Autowired
	private ILabelService labelService;

	// =====================================================
	// 手机端=================================================
	// 美食商品查询列表(上架) ynw
	@RequestMapping(params = "computerGoodsMs", method = RequestMethod.POST)
	public void computerGoodsMs(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("id");
		String ificationId = request.getParameter("ifiId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(ificationId)) {
			message.addProperty("message", "ificationId和companyId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		JSonGrid grid = new JSonGrid();

		List<Goods> goodsList = goodsService.computerGoodsWm(Integer.valueOf(companyId), Integer.valueOf(ificationId),
				Integer.valueOf(2));

		for (Goods goodss : goodsList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", goodss.getId());
			record.addColumn("code", goodss.getCode());
			record.addColumn("name", goodss.getName());
			record.addColumn("price", goodss.getPrice());
			if (StringUtil.isEmpty(goodss.getSvgPrice())) {
				record.addColumn("svg_price", "0");
			} else {
				record.addColumn("svg_price", goodss.getSvgPrice());
				record.addColumn("start_time", goodss.getStartTime());
				record.addColumn("end_time", goodss.getEndTime());
			}
			record.addColumn("unit", goodss.getUnit());
			record.addColumn("ksy_word", goodss.getKsyWord());
			record.addColumn("details", goodss.getDetails());
			if (StringUtil.isEmpty(goodss.getBrief())) {
				record.addColumn("brief", goodss.getBrief());
			}
			record.addColumn("zoom_url", goodss.getZoomUrl());
			record.addColumn("type", goodss.getClassIf());
			record.addColumn("original_url", goodss.getOriginalUrl());
			record.addColumn("create_time", goodss.getCreateTime());
			record.addColumn("click", goodss.getClick());
			record.addColumn("mon_sales", goodss.getMon_sales());
			record.addColumn("total_sales", goodss.getTotal_sales());
			record.addColumn("recommend", goodss.getRecommend());
			String shelves = "0";
			if (goodss.getShelves()) {
				shelves = "1";
			}
			record.addColumn("shelves", shelves);
			record.addColumn("fine", goodss.getFine());
			record.addColumn("is_new", goodss.getIsNew());
			record.addColumn("stock", goodss.getStock());
			record.addColumn("Selling", goodss.getSelling());
			record.addColumn("is_svg", goodss.getIsSvg());
			List<Attribute> attributes = attributeService.getattribute(goodss.getId());
			StringBuilder natureContent = new StringBuilder();
			for (Attribute attribute : attributes) {
				List<Nature> natures = natureService.getnature(attribute.getId());
				natureContent.append(attribute.getName() + "#");
				for (int i = 0; i < natures.size(); i++) {
					if ((i + 1) == natures.size()) {
						natureContent.append(natures.get(i).getContent() + "@");
					} else {
						natureContent.append(natures.get(i).getContent() + ",");
					}
					record.addColumn("natureContent", natureContent.toString());
				}
			}

			grid.addRecord(record);

		}

		if (goodsList.size() > 0) {
			grid.addProperties("totalCount", goodsList.size());
		} else {
			grid.addProperties("totalCount", 0);
		}

		new PushJson().outString(grid.toJSonString("list"), response);

	}

	/* 修改商品库存信息 start ynw */
	@RequestMapping(params = "updateStock", method = RequestMethod.POST)
	public void updateStock(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");// 商品id
		String stock = request.getParameter("stock");// 库存

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		Goods goods = goodsService.getGoods(Integer.valueOf(id));
		if (!NumberUtils.isNumber(stock)) {
			message.addProperty("message", "库存值不正确");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		goods.setStock(stock);
		goodsService.update(goods);
		message.addProperty("message", "修改成功");
		message.addProperty("success", true);
		new PushJson().upload(message.toJSonString(), response);
	}
	/* end ynw */

	@RequestMapping(params = "cTogoods", method = RequestMethod.POST)
	public void cTogoods(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("id");// 商家ID

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "companyId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Goods> goodsList = goodsService.getCGoods(Integer.valueOf(companyId), 1);

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		for (Goods goodss : goodsList) {
			JSonGridRecord record = new JSonGridRecord();
			List<Attribute> attributes = attributeService.getattribute(goodss.getId());
			StringBuilder natureContent = new StringBuilder();
			for (Attribute attribute : attributes) {
				List<Nature> natures = natureService.getnature(attribute.getId());
				natureContent.append(attribute.getName() + "#");
				for (int i = 0; i < natures.size(); i++) {
					if ((i + 1) == natures.size()) {
						natureContent.append(natures.get(i).getContent() + "@");
					} else {
						natureContent.append(natures.get(i).getContent() + ",");
					}
					record.addColumn("natureContent", natureContent.toString());
				}
			}
			record.addColumn("id", goodss.getId());
			record.addColumn("code", goodss.getCode());
			record.addColumn("name", goodss.getName());
			record.addColumn("price", goodss.getPrice());
			record.addColumn("svg_price", goodss.getSvgPrice());
			record.addColumn("start_time", goodss.getStartTime());
			record.addColumn("end_time", goodss.getEndTime());
			record.addColumn("unit", goodss.getUnit());
			record.addColumn("ificationName", goodss.getIficationId().getName());
			record.addColumn("ificationId", goodss.getIficationId().getId());
			record.addColumn("ksy_word", goodss.getKsyWord());
			record.addColumn("details", goodss.getDetails());
			record.addColumn("brief", goodss.getBrief());
			record.addColumn("zoom_url", goodss.getZoomUrl());
			record.addColumn("original_url", goodss.getOriginalUrl());
			record.addColumn("create_time", goodss.getCreateTime());
			record.addColumn("click", goodss.getClick());
			record.addColumn("mon_sales", goodss.getMon_sales());
			record.addColumn("total_sales", goodss.getTotal_sales());
			record.addColumn("recommend", goodss.getRecommend());
			record.addColumn("shelves", goodss.getShelves());
			record.addColumn("fine", goodss.getFine());
			record.addColumn("is_new", goodss.getIsNew());
			record.addColumn("Selling", goodss.getSelling());
			record.addColumn("is_svg", goodss.getIsSvg());
			record.addColumn("stock", goodss.getStock()); // 得到库存 ynw
			grid.addRecord(record);
		}

		grid.addProperties("totalCount", goodsList.size());
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 美食商品查询列表
	@RequestMapping(params = "cTogoodsMS", method = RequestMethod.POST)
	public void cTogoodsMS(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("id");// 商家ID

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "companyId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Goods> goodsList = goodsService.getCGoods(Integer.valueOf(companyId), 2);

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		for (Goods goodss : goodsList) {
			JSonGridRecord record = new JSonGridRecord();
			List<Attribute> attributes = attributeService.getattribute(goodss.getId());
			StringBuilder natureContent = new StringBuilder();
			for (Attribute attribute : attributes) {
				List<Nature> natures = natureService.getnature(attribute.getId());
				natureContent.append(attribute.getName() + "#");
				for (int i = 0; i < natures.size(); i++) {
					if ((i + 1) == natures.size()) {
						natureContent.append(natures.get(i).getContent() + "@");
					} else {
						natureContent.append(natures.get(i).getContent() + ",");
					}
					record.addColumn("natureContent", natureContent.toString());
				}
			}
			record.addColumn("id", goodss.getId());
			record.addColumn("name", goodss.getName());
			record.addColumn("price", goodss.getPrice());
			if (StringUtil.isEmpty(goodss.getSvgPrice())) {
				record.addColumn("svg_price", "0");
			} else {
				record.addColumn("svg_price", goodss.getSvgPrice());
			}

			record.addColumn("ificationName", goodss.getIficationId().getName());
			record.addColumn("ificationId", goodss.getIficationId().getId());
			if (!StringUtil.isEmpty(goodss.getKsyWord())) {
				record.addColumn("ksy_word", goodss.getKsyWord());
			}
			if (!StringUtil.isEmpty(goodss.getUnit())) {
				record.addColumn("unit", goodss.getUnit());
			}
			if (!StringUtil.isEmpty(goodss.getCode())) {
				record.addColumn("code", goodss.getCode());
			}
			if (!StringUtil.isEmpty(goodss.getDetails())) {
				record.addColumn("details", goodss.getDetails());
			}
			record.addColumn("brief", goodss.getBrief());
			record.addColumn("zoom_url", goodss.getZoomUrl());
			record.addColumn("original_url", goodss.getOriginalUrl());
			record.addColumn("create_time", goodss.getCreateTime());
			record.addColumn("click", goodss.getClick());
			record.addColumn("mon_sales", goodss.getMon_sales());
			record.addColumn("total_sales", goodss.getTotal_sales());
			record.addColumn("recommend", goodss.getRecommend());
			record.addColumn("shelves", goodss.getShelves());
			record.addColumn("fine", goodss.getFine());
			record.addColumn("is_new", goodss.getIsNew());
			record.addColumn("Selling", goodss.getSelling());
			record.addColumn("is_svg", goodss.getIsSvg());
			record.addColumn("stock", goodss.getStock()); // 得到库存 ynw
			grid.addRecord(record);
		}

		grid.addProperties("totalCount", goodsList.size());

		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 多个商品查询
	@RequestMapping(params = "getGoods", method = RequestMethod.POST)
	public void getGoods(HttpServletRequest request, HttpServletResponse response) {
		String ids = request.getParameter("id");
		String[] id = ids.split(",");

		if (StringUtil.isEmpty(ids)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		for (int i = 0; i < id.length; i++) {
			Goods good = goodsService.getGoods(Integer.valueOf(id[i]));
			if (good == null) {
				JSonMessage message = new JSonMessage();
				message.addProperty("message", "数据为空.");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			JSonGridRecord record = new JSonGridRecord();
			List<Attribute> attributes = attributeService.getattribute(good.getId());
			StringBuilder natureContent = new StringBuilder();
			for (Attribute attribute : attributes) {
				List<Nature> natures = natureService.getnature(attribute.getId());
				natureContent.append(attribute.getName() + "#");
				for (int j = 0; j < natures.size(); j++) {
					if ((j + 1) == natures.size()) {
						natureContent.append(natures.get(j).getContent() + "@");
					} else {
						natureContent.append(natures.get(j).getContent() + ",");
					}
					record.addColumn("natureContent", natureContent.toString());
				}
			}
			record.addColumn("id", good.getId());
			record.addColumn("code", good.getCode());
			record.addColumn("name", good.getName());
			record.addColumn("price", good.getPrice());
			record.addColumn("svg_price", good.getSvgPrice());
			record.addColumn("start_time", good.getStartTime());
			record.addColumn("end_time", good.getEndTime());
			record.addColumn("unit", good.getUnit());
			record.addColumn("ificationName", good.getIficationId().getName());
			record.addColumn("ksy_word", good.getKsyWord());
			record.addColumn("details", good.getDetails());
			record.addColumn("brief", good.getBrief());
			record.addColumn("zoom_url", good.getZoomUrl());
			record.addColumn("original_url", good.getOriginalUrl());
			record.addColumn("create_time", good.getCreateTime());
			record.addColumn("click", good.getClick());
			record.addColumn("mon_sales", good.getMon_sales());
			record.addColumn("total_sales", good.getTotal_sales());
			record.addColumn("recommend", good.getRecommend());
			record.addColumn("shelves", good.getShelves());
			record.addColumn("fine", good.getFine());
			record.addColumn("is_new", good.getIsNew());
			record.addColumn("Selling", good.getSelling());
			record.addColumn("is_svg", good.getIsSvg());
			grid.addRecord(record);
		}

		grid.addProperties("totalCount", id.length);

		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 商品详情
	@RequestMapping(params = "getGood", method = RequestMethod.POST)
	public void getGood(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");

		if (StringUtil.isEmpty(id)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		Goods good = goodsService.getGoods(Integer.valueOf(id));
		if (good == null) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "数据为空.");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		JSonGridRecord record = new JSonGridRecord();
		List<Attribute> attributes = attributeService.getattribute(good.getId());
		StringBuilder natureContent = new StringBuilder();
		for (Attribute attribute : attributes) {
			List<Nature> natures = natureService.getnature(attribute.getId());
			natureContent.append(attribute.getName() + "#");
			for (int j = 0; j < natures.size(); j++) {
				if ((j + 1) == natures.size()) {
					natureContent.append(natures.get(j).getContent() + "@");
				} else {
					natureContent.append(natures.get(j).getContent() + ",");
				}
				record.addColumn("natureContent", natureContent.toString());
			}
		}

		if (good.getCompanyId().getClassifyId().equals("3")) {
			Stay stay = stayService.Stay(good.getId());
			if (Integer.valueOf(stay.getCancelTime()) > Integer
					.valueOf(new SimpleDateFormat("HH").format(new Date()))) {
				grid.addProperties("status", "入住当天" + stay.getCancelTime() + "前免费取消");
			} else {
				grid.addProperties("status", "现已过可取消时间,不可取消");
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			Date date = new Date();
			date = calendar.getTime();
			grid.addProperties("intoTime", new SimpleDateFormat("MM月dd日").format(new Date()));
			grid.addProperties("leaveTime", new SimpleDateFormat("MM月dd日").format(date));
			grid.addProperties("days", 1);
		}

		List<Label> labellist = labelService.getLabel("goods", good.getId() + "");
		for (Label labels : labellist) {
			JSonGridRecord recordc = new JSonGridRecord();
			recordc.addColumn("label", labels.getContent());
			grid.addRecord(recordc);
		}
		grid.addProperties("id", good.getId());
		if (!StringUtil.isEmpty(good.getCode())) {
			grid.addProperties("code", good.getCode());
		}
		grid.addProperties("name", good.getName());

		if (StringUtil.isEmpty(good.getSvgPrice())) {
			grid.addProperties("price", good.getPrice());
			grid.addProperties("svg_price", "0");
		} else {
			grid.addProperties("price", good.getPrice());
			grid.addProperties("svg_price", good.getSvgPrice());
		}

		if (good.getUnit() == null) {
			// grid.addProperties("unit", "0");
		} else {
			grid.addProperties("unit", good.getUnit());
		}

		grid.addProperties("ificationName", good.getIficationId().getName());
		grid.addProperties("ksy_word", good.getKsyWord() == null ? "" : good.getKsyWord());
		if (!StringUtil.isEmpty(good.getBrief())) {
			grid.addProperties("brief", good.getBrief());
		}
		if (!StringUtil.isEmpty(good.getDetails())) {
			grid.addProperties("details", good.getDetails());
		}
		grid.addProperties("zoom_url", good.getZoomUrl());
		grid.addProperties("original_url", good.getOriginalUrl());
		grid.addProperties("create_time", good.getCreateTime());
		grid.addProperties("click", good.getClick());
		grid.addProperties("mon_sales", good.getMon_sales());
		grid.addProperties("total_sales", good.getTotal_sales());
		grid.addProperties("recommend", good.getRecommend());
		grid.addProperties("shelves", good.getShelves());
		grid.addProperties("fine", good.getFine());
		grid.addProperties("is_new", good.getIsNew());
		grid.addProperties("Selling", good.getSelling());
		grid.addProperties("printName", good.getPrintName());
		grid.addProperties("classif", good.getClassIf());
		if (StringUtil.isEmpty(good.getBoxPrice())) {
			grid.addProperties("boxPrice", "0");
		} else {
			grid.addProperties("boxPrice", good.getBoxPrice());
		}
		if (!StringUtil.isEmpty(good.getCode())) {
			grid.addProperties("code", good.getCode());
		}
		grid.addProperties("stock", good.getStock());
		record.addColumn("is_svg", good.getIsSvg());

		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 查询商品列表接口
	@RequestMapping(params = "getGodsList", method = RequestMethod.POST)
	public void getGoodsList(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("id");
		String ificationId = request.getParameter("ifiId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(ificationId)) {
			message.addProperty("message", "ificationId和companyId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		JSonGrid grid = new JSonGrid();

		List<Goods> goodsList = goodsService.goodsList(Integer.valueOf(companyId), Integer.valueOf(ificationId));

		for (Goods goodss : goodsList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", goodss.getId());
			record.addColumn("code", goodss.getCode());
			record.addColumn("name", goodss.getName());
			if (StringUtil.isEmpty(goodss.getSvgPrice())) {
				record.addColumn("svg_price", "0");
			} else {
				record.addColumn("svg_price", goodss.getSvgPrice());

			}
			record.addColumn("price", goodss.getPrice());
			record.addColumn("unit", goodss.getUnit());
			record.addColumn("ksy_word", goodss.getKsyWord());
			record.addColumn("details", goodss.getDetails());
			record.addColumn("brief", goodss.getBrief());
			record.addColumn("zoom_url", goodss.getZoomUrl());
			record.addColumn("original_url", goodss.getOriginalUrl());
			record.addColumn("create_time", goodss.getCreateTime());
			record.addColumn("click", goodss.getClick());
			record.addColumn("mon_sales", goodss.getMon_sales());
			record.addColumn("total_sales", goodss.getTotal_sales());
			record.addColumn("recommend", goodss.getRecommend());
			record.addColumn("shelves", goodss.getShelves());
			record.addColumn("fine", goodss.getFine());
			record.addColumn("is_new", goodss.getIsNew());
			record.addColumn("Selling", goodss.getSelling());
			record.addColumn("is_svg", goodss.getIsSvg());
			record.addColumn("stock", goodss.getStock()); // 增加库存 ynw
			List<Attribute> attributes = attributeService.getattribute(goodss.getId());
			StringBuilder natureContent = new StringBuilder();
			for (Attribute attribute : attributes) {
				List<Nature> natures = natureService.getnature(attribute.getId());
				natureContent.append(attribute.getName() + "#");
				for (int i = 0; i < natures.size(); i++) {
					if ((i + 1) == natures.size()) {
						natureContent.append(natures.get(i).getContent() + "@");
					} else {
						natureContent.append(natures.get(i).getContent() + ",");
					}
					record.addColumn("natureContent", natureContent.toString());
				}
			}

			grid.addRecord(record);

		}

		if (goodsList.size() > 0) {
			grid.addProperties("totalCount", goodsList.size());
		} else {
			grid.addProperties("totalCount", 0);
		}

		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除  2018-11-02 ·Tyy
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		Goods goods = goodsService.getGoods(Integer.valueOf(id));
		if(goods == null){
			message.addProperty("message", "商品不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		goods.setIsDelete(true);
		try{
			goodsService.update(goods);
		}catch (Exception e) {
			message.addProperty("message", "连接超时，请稍候重试");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 上传并添加
	@RequestMapping(params = "uploadSave", method = RequestMethod.POST)
	public void uploadSave(HttpServletRequest request, @RequestParam("file") MultipartFile file,
			HttpServletResponse response) {

		String companyId = request.getParameter("companyId");// 商家id
		String type = request.getParameter("type");// 图片类型
		String name = request.getParameter("name");// 商品名称
		String price = request.getParameter("price");// 商品价格
		String ificationId = request.getParameter("ificationId");// 分类id
		String printName = request.getParameter("printName");// 打印机名称
		String classIf = request.getParameter("classIf");// 商家分类

		// 选填项
		String brief = request.getParameter("brief");// 描述
		String svgPrice = request.getParameter("promotion_price");// 促销价格
		String details = request.getParameter("details");// 商品详情
		String code = request.getParameter("code");// 商品编码
		String unit = request.getParameter("unit");// 单位
		String recommend = request.getParameter("recommend");// 是否推荐
		String ksyWord = request.getParameter("ksy_word");// 关键字
		String stock = request.getParameter("stock");// 库存
		String boxPrice = request.getParameter("boxPrice");// 包装费

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(name) || StringUtil.isEmpty(price) || StringUtil.isEmpty(companyId)
				|| StringUtil.isEmpty(ificationId) || StringUtil.isEmpty(printName) || StringUtil.isEmpty(classIf)
				|| StringUtil.isEmpty(type)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		// 如果文件为空，返回失败
		if (file.isEmpty()) {
			message.addProperty("message", "请选择要上传的文件");
			message.addProperty("success", false);
			return;
		}

		Company company = companyService.getCompany(Integer.valueOf(companyId));
		if (company == null) {
			message.addProperty("message", "商家不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Ification ification = ificationService.find(Integer.valueOf(ificationId));
		if (company == null) {
			message.addProperty("message", "商品分类不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Goods goods = new Goods();
		if (classIf.equals("外卖")) {
			classIf = "1";
		}
		if (classIf.equals("美食")) {
			classIf = "2";
		}
		goods.setClassIf(classIf);
		goods.setPrintName(printName);
		goods.setName(name);
		goods.setPrice(price);
		goods.setCompanyId(company);
		goods.setIficationId(ification);
		goods.setIsDelete(false);
		goods.setShelves(true);
		goods.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		if (!StringUtil.isEmpty(recommend)) {
			if (recommend.equals("0")) {
				goods.setRecommend(false);
			} else if (recommend.equals("1")) {
				goods.setRecommend(false);
			} else {
				message.addProperty("message", "推荐状态不正确");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
		} else {
			goods.setRecommend(false);
		}
		if (!StringUtil.isEmpty(details)) {
			goods.setDetails(details);
		}
		if (!StringUtil.isEmpty(details)) {
			goods.setDetails(details);
		}
		if (!StringUtil.isEmpty(unit)) {
			goods.setUnit(unit);
		}
		if (!StringUtil.isEmpty(ksyWord)) {
			goods.setKsyWord(ksyWord);
		}
		if (!StringUtil.isEmpty(boxPrice)) {
			goods.setBoxPrice(boxPrice);
		} else {
			goods.setBoxPrice("0");
		}
		if (!StringUtil.isEmpty(details)) {
			goods.setDetails(details);
		}
		if (!StringUtil.isEmpty(brief)) {
			goods.setBrief(brief);
		}
		if (!StringUtil.isEmpty(code)) {
			goods.setCode(code);
		}
		if (!svgPrice.trim().equals("0")) {
			if (!NumberUtils.isNumber(svgPrice)) {
				message.addProperty("message", "促销金额不正确");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}

			goods.setSvg(true);
			goods.setSvgPrice(svgPrice);

		}
		if (!StringUtil.isEmpty(stock)) {
			if (!NumberUtils.isNumber(stock)) {
				message.addProperty("message", "库存值不正确");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
		}

		goods.setStock(stock);
		goodsService.saveORupdate(goods);

		// 上传文件路径
		String path = request.getSession().getServletContext().getRealPath("");

		path = path.substring(0, path.indexOf("Dz\\")) + "DzClient\\common\\images\\";
		// 上传文件名
		String uploadName = file.getOriginalFilename();
		String prefix = uploadName.substring(uploadName.lastIndexOf("."));
		String filename = MD5Util.MD5(System.currentTimeMillis() + "") + prefix;
		File filepath = new File(path, filename);

		// 判断路径是否存在，如果不存在就创建一个
		if (!filepath.getParentFile().exists()) {
			filepath.getParentFile().mkdirs();
		}

		String newPath = URL.FIND_URL + filename;

		goods.setZoomUrl(newPath);
		goods.setOriginalUrl(newPath);

		// 将上传文件保存到一个目标文件当中
		try {
			file.transferTo(new File(path + File.separator + filename));
			goodsService.saveORupdate(goods);
			message.addProperty("message", "上传成功");
			message.addProperty("success", true);
			new PushJson().upload(message.toJSonString(), response);
			return;
		} catch (IOException e) {
			message.addProperty("message", "上传失败");
			message.addProperty("success", false);
			e.printStackTrace();
			new PushJson().upload(message.toJSonString(), response);
			return;
		}

	}

	// 修改商品信息
	@RequestMapping(params = "update", method = RequestMethod.POST)
	public void update(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");// 商品id
		// 选填项
		String name = request.getParameter("name");// 商品名称
		String price = request.getParameter("price");// 商品价格
		String unit = request.getParameter("unit");// 单位
		String printName = request.getParameter("printName");// 打印机名称
		String classIf = request.getParameter("classIf");// 商家分类
		String recommend = request.getParameter("recommend");// 是否推荐
		String ksyWord = request.getParameter("ksy_word");// 关键字
		String stock = request.getParameter("stock");// 库存
		String brief = request.getParameter("brief");// 描述
		String svgPrice = request.getParameter("promotion_price");// 促销价格
		
		String ificationId = request.getParameter("ificationId");// 商品分类id   2018-11-06 @Tyy

		String details = request.getParameter("details");// 商品详情
		String code = request.getParameter("code");// 商品编码
		String boxPrice = request.getParameter("boxPrice");// 包装费

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if (StringUtil.isEmpty(name)) {
			message.addProperty("message", "商品名称不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (StringUtil.isEmpty(price)) {
			message.addProperty("message", "商品价格不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if (StringUtil.isEmpty(printName)) {
			message.addProperty("message", "打印机名称不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (StringUtil.isEmpty(classIf)) {
			message.addProperty("message", "商家分类不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if(StringUtil.isEmpty(ificationId)){//2018-11-06 @Tyy
			message.addProperty("message", "商家分类Id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		Goods goods = goodsService.getGoods(Integer.valueOf(id));
		if (!StringUtil.isEmpty(classIf)) {
			goods.setClassIf(classIf);
		}
		if (!StringUtil.isEmpty(printName)) {
			goods.setPrintName(printName);
		}
		if (!StringUtil.isEmpty(name)) {
			goods.setName(name);
		}
		if (!StringUtil.isEmpty(price)) {
			goods.setPrice(price);
		}
		if (!StringUtil.isEmpty(unit)) {
			goods.setUnit(unit);
		}
		if (!StringUtil.isEmpty(ksyWord)) {
			goods.setKsyWord(ksyWord);
		}
		if (!StringUtil.isEmpty(boxPrice)) {
			goods.setBoxPrice(boxPrice);
		}
		if(!StringUtil.isEmpty(ificationId)){//2018-11-06 @Tyy
			Ification ification = ificationService.find(Integer.valueOf(ificationId));
			if(ification != null){
				goods.setIficationId(ification);
			}
		}
		if (!StringUtil.isEmpty(recommend)) {
			if (recommend.equals("0")) {
				goods.setRecommend(false);
			} else if (recommend.equals("1")) {
				goods.setRecommend(true);
			} else {
				message.addProperty("message", "推荐状态不正确");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
		}
		if (!StringUtil.isEmpty(classIf)) {
			goods.setClassIf(classIf);
		}

		if (!StringUtil.isEmpty(details)) {
			goods.setDetails(details);
		}
		if (!StringUtil.isEmpty(brief)) {
			goods.setBrief(brief);
		}
		if (!StringUtil.isEmpty(code)) {
			goods.setCode(code);
		}

		if (!svgPrice.trim().equals("0")) {
			if (!NumberUtils.isNumber(svgPrice)) {
				message.addProperty("message", "促销金额不正确");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}

			goods.setSvg(true);
			goods.setSvgPrice(svgPrice);
		} else {
			goods.setSvg(false);
			goods.setSvgPrice(null);
		}

		if (!NumberUtils.isNumber(stock)) {
			message.addProperty("message", "库存值不正确");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		goods.setStock(stock);
		goodsService.update(goods);
		message.addProperty("message", "修改成功");
		message.addProperty("success", true);
		new PushJson().upload(message.toJSonString(), response);
	}

	// 修改商品图片
	@RequestMapping(params = "updateImg", method = RequestMethod.POST)
	public void updateImg(HttpServletRequest request, @RequestParam("file") MultipartFile file,
			HttpServletResponse response) {
		String id = request.getParameter("id");// 商品id
		String type = request.getParameter("type");// 图片类型

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(type)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Goods goods = goodsService.getGoods(Integer.valueOf(id));

		if (goods == null) {
			message.addProperty("message", "商品不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		// 如果文件为空，返回失败
		if (file.isEmpty()) {
			message.addProperty("message", "请选择要上传的文件");
			message.addProperty("success", false);
			return;
		}
		// 上传文件路径
		String path = request.getSession().getServletContext().getRealPath("");

		path = path.substring(0, path.indexOf("Dz\\")) + "DzClient\\common\\images\\";
		// 上传文件名
		String uploadName = file.getOriginalFilename();
		String prefix = uploadName.substring(uploadName.lastIndexOf("."));
		String filename = MD5Util.MD5(System.currentTimeMillis() + "") + prefix;

		File filepath = new File(path, filename);

		// 判断路径是否存在，如果不存在就创建一个
		if (!filepath.getParentFile().exists()) {
			filepath.getParentFile().mkdirs();
		}

		String newPath = URL.FIND_URL + filename;

		goods.setZoomUrl(newPath);
		goods.setOriginalUrl(newPath);

		// 将上传文件保存到一个目标文件当中
		try {
			file.transferTo(new File(path + File.separator + filename));
			goodsService.update(goods);
			message.addProperty("message", "上传成功");
			message.addProperty("success", true);
			new PushJson().upload(message.toJSonString(), response);
			return;
		} catch (IOException e) {
			message.addProperty("message", "上传失败");
			message.addProperty("success", false);
			e.printStackTrace();
			new PushJson().upload(message.toJSonString(), response);
			return;
		}
	}

	// 添加
	@RequestMapping(params = "save", method = RequestMethod.POST)
	public void save(HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		String price = request.getParameter("price");
		String brief = request.getParameter("brief");
		String unit = request.getParameter("unit");
		String companyId = request.getParameter("companyId");
		String ificationId = request.getParameter("ificationId");
		// 选填
		String boxPrice = request.getParameter("boxPrice");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(name) || StringUtil.isEmpty(price) || StringUtil.isEmpty(unit)
				|| StringUtil.isEmpty(companyId) || StringUtil.isEmpty(ificationId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = companyService.getCompany(Integer.valueOf(companyId));
		if (company == null) {
			message.addProperty("message", "商家不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Ification ification = ificationService.find(Integer.valueOf(ificationId));
		if (company == null) {
			message.addProperty("message", "商品分类不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Goods goods = new Goods();
		goods.setName(name);
		goods.setPrice(price);
		goods.setBrief(brief);
		goods.setUnit(unit);
		goods.setCompanyId(company);
		goods.setIficationId(ification);
		if (!boxPrice.trim().equals("0")) {
			goods.setBoxPrice(boxPrice);
		} else {
			goods.setBoxPrice("0");
		}
		goods.setIsDelete(false);
		goodsService.saveORupdate(goods);
		message.addProperty("message", "添加商品成功");
		message.addProperty("newGoodsId", goods.getId());
		message.addProperty("companyId", company.getId());
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 商家app 查询商品列表接口外卖
	@RequestMapping(params = "companyGoods", method = RequestMethod.POST)
	public void companyGoods(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("id");
		String ificationId = request.getParameter("ifiId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(ificationId)) {
			message.addProperty("message", "ificationId和companyId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		JSonGrid grid = new JSonGrid();

		List<Goods> goodsList = goodsService.companyGoodsWm(Integer.valueOf(companyId), Integer.valueOf(ificationId),
				Integer.valueOf(1));

		for (Goods goodss : goodsList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", goodss.getId());
			record.addColumn("code", goodss.getCode());
			record.addColumn("name", goodss.getName());
			record.addColumn("price", goodss.getPrice());
			if (StringUtil.isEmpty(goodss.getSvgPrice())) {
				record.addColumn("svg_price", "0");
			} else {
				record.addColumn("svg_price", goodss.getSvgPrice());
				record.addColumn("start_time", goodss.getStartTime());
				record.addColumn("end_time", goodss.getEndTime());
			}
			record.addColumn("unit", goodss.getUnit());
			record.addColumn("ksy_word", goodss.getKsyWord());
			record.addColumn("details", goodss.getDetails());
			if (StringUtil.isEmpty(goodss.getBrief())) {
				record.addColumn("brief", goodss.getBrief());
			}
			record.addColumn("zoom_url", goodss.getZoomUrl());
			record.addColumn("type", goodss.getClassIf());
			record.addColumn("original_url", goodss.getOriginalUrl());
			record.addColumn("create_time", goodss.getCreateTime());
			record.addColumn("click", goodss.getClick());
			record.addColumn("mon_sales", goodss.getMon_sales());
			record.addColumn("total_sales", goodss.getTotal_sales());
			record.addColumn("recommend", goodss.getRecommend());
			String shelves = "0";
			if (goodss.getShelves()) {
				shelves = "1";
			}
			record.addColumn("shelves", shelves);
			record.addColumn("fine", goodss.getFine());
			record.addColumn("is_new", goodss.getIsNew());
			record.addColumn("stock", goodss.getStock());
			record.addColumn("Selling", goodss.getSelling());
			record.addColumn("is_svg", goodss.getIsSvg());
			List<Attribute> attributes = attributeService.getattribute(goodss.getId());
			StringBuilder natureContent = new StringBuilder();
			for (Attribute attribute : attributes) {
				List<Nature> natures = natureService.getnature(attribute.getId());
				natureContent.append(attribute.getName() + "#");
				for (int i = 0; i < natures.size(); i++) {
					if ((i + 1) == natures.size()) {
						natureContent.append(natures.get(i).getContent() + "@");
					} else {
						natureContent.append(natures.get(i).getContent() + ",");
					}
					record.addColumn("natureContent", natureContent.toString());
				}
			}

			grid.addRecord(record);

		}

		if (goodsList.size() > 0) {
			grid.addProperties("totalCount", goodsList.size());
		} else {
			grid.addProperties("totalCount", 0);
		}

		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 商家app 查询商品列表接口外卖
	@RequestMapping(params = "companyGoodsMs", method = RequestMethod.POST)
	public void companyGoodsMs(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("id");
		String ificationId = request.getParameter("ifiId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(ificationId)) {
			message.addProperty("message", "ificationId和companyId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		JSonGrid grid = new JSonGrid();

		List<Goods> goodsList = goodsService.companyGoodsWm(Integer.valueOf(companyId), Integer.valueOf(ificationId),
				Integer.valueOf(2));

		for (Goods goodss : goodsList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", goodss.getId());
			record.addColumn("code", goodss.getCode());
			record.addColumn("name", goodss.getName());
			record.addColumn("price", goodss.getPrice());
			if (StringUtil.isEmpty(goodss.getSvgPrice())) {
				record.addColumn("svg_price", "0");
			} else {
				record.addColumn("svg_price", goodss.getSvgPrice());
				record.addColumn("start_time", goodss.getStartTime());
				record.addColumn("end_time", goodss.getEndTime());
			}
			record.addColumn("unit", goodss.getUnit());
			record.addColumn("ksy_word", goodss.getKsyWord());
			record.addColumn("details", goodss.getDetails());
			if (StringUtil.isEmpty(goodss.getBrief())) {
				record.addColumn("brief", goodss.getBrief());
			}
			record.addColumn("zoom_url", goodss.getZoomUrl());
			record.addColumn("type", goodss.getClassIf());
			record.addColumn("original_url", goodss.getOriginalUrl());
			record.addColumn("create_time", goodss.getCreateTime());
			record.addColumn("click", goodss.getClick());
			record.addColumn("mon_sales", goodss.getMon_sales());
			record.addColumn("total_sales", goodss.getTotal_sales());
			record.addColumn("recommend", goodss.getRecommend());
			String shelves = "0";
			if (goodss.getShelves()) {
				shelves = "1";
			}
			record.addColumn("shelves", shelves);
			record.addColumn("fine", goodss.getFine());
			record.addColumn("is_new", goodss.getIsNew());
			record.addColumn("stock", goodss.getStock());
			record.addColumn("Selling", goodss.getSelling());
			record.addColumn("is_svg", goodss.getIsSvg());
			List<Attribute> attributes = attributeService.getattribute(goodss.getId());
			StringBuilder natureContent = new StringBuilder();
			for (Attribute attribute : attributes) {
				List<Nature> natures = natureService.getnature(attribute.getId());
				natureContent.append(attribute.getName() + "#");
				for (int i = 0; i < natures.size(); i++) {
					if ((i + 1) == natures.size()) {
						natureContent.append(natures.get(i).getContent() + "@");
					} else {
						natureContent.append(natures.get(i).getContent() + ",");
					}
					record.addColumn("natureContent", natureContent.toString());
				}
			}

			grid.addRecord(record);

		}

		if (goodsList.size() > 0) {
			grid.addProperties("totalCount", goodsList.size());
		} else {
			grid.addProperties("totalCount", 0);
		}

		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 下架
	@RequestMapping(params = "down", method = RequestMethod.POST)
	public void down(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String status = request.getParameter("status");

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(status)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		String shelves = "";
		if (status.equals("0")) {
			shelves = "下架";
		} else if (status.equals("1")) {
			shelves = "上架";
		} else {
			message.addProperty("message", "status值不正确");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		Goods goods = new Goods();
		goods.setShelves(false);

		goodsService.down(Integer.valueOf(id), Integer.valueOf(status));

		message.addProperty("message", shelves + "操作成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 后台商品列表
	@RequestMapping(params = "backGoods", method = RequestMethod.POST)
	public void backGoods(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("id");// 商家ID
		String type = request.getParameter("type");// 商品类型

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(type)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Goods> goodsList = goodsService.getCGoods(Integer.valueOf(companyId), Integer.valueOf(type));

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		for (Goods goodss : goodsList) {
			JSonGridRecord record = new JSonGridRecord();
			List<Attribute> attributes = attributeService.getattribute(goodss.getId());
			StringBuilder natureContent = new StringBuilder();
			for (Attribute attribute : attributes) {
				List<Nature> natures = natureService.getnature(attribute.getId());
				natureContent.append(attribute.getName() + "#");
				for (int i = 0; i < natures.size(); i++) {
					if ((i + 1) == natures.size()) {
						natureContent.append(natures.get(i).getContent() + "@");
					} else {
						natureContent.append(natures.get(i).getContent() + ",");
					}
					record.addColumn("natureContent", natureContent.toString());
				}
			}
			record.addColumn("id", goodss.getId());
			record.addColumn("name", goodss.getName());
			if (StringUtil.isEmpty(goodss.getSvgPrice())) {
				record.addColumn("price", goodss.getPrice());
			} else {
				record.addColumn("price", goodss.getSvgPrice());
			}
			if (goodss.getIsSvg()) {
				record.addColumn("svg_price", goodss.getSvgPrice());
				record.addColumn("start_time", goodss.getStartTime());
				record.addColumn("end_time", goodss.getEndTime());
			}
			record.addColumn("ificationName", goodss.getIficationId().getName());
			record.addColumn("ificationId", goodss.getIficationId().getId());
			if (!StringUtil.isEmpty(goodss.getKsyWord())) {
				record.addColumn("ksy_word", goodss.getKsyWord());
			}
			if (!StringUtil.isEmpty(goodss.getUnit())) {
				record.addColumn("unit", goodss.getUnit());
			}
			if (!StringUtil.isEmpty(goodss.getCode())) {
				record.addColumn("code", goodss.getCode());
			}
			if (!StringUtil.isEmpty(goodss.getDetails())) {
				record.addColumn("details", goodss.getDetails());
			}
			record.addColumn("brief", goodss.getBrief());
			record.addColumn("zoom_url", goodss.getZoomUrl());
			record.addColumn("original_url", goodss.getOriginalUrl());
			record.addColumn("create_time", goodss.getCreateTime());
			record.addColumn("click", goodss.getClick());
			record.addColumn("mon_sales", goodss.getMon_sales());
			record.addColumn("total_sales", goodss.getTotal_sales());
			record.addColumn("recommend", goodss.getRecommend());
			record.addColumn("shelves", goodss.getShelves());
			record.addColumn("fine", goodss.getFine());
			record.addColumn("is_new", goodss.getIsNew());
			record.addColumn("Selling", goodss.getSelling());
			record.addColumn("is_svg", goodss.getIsSvg());
			grid.addRecord(record);
		}

		grid.addProperties("totalCount", goodsList.size());
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 查询商品列表接口
	@RequestMapping(params = "getGoodsList", method = RequestMethod.POST)
	public void getGodsList(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("id");
		String ificationId = request.getParameter("ifiId");
		String type = request.getParameter("type");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(ificationId) || StringUtil.isEmpty(type)) {
			message.addProperty("message", "ificationId、type和companyId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		JSonGrid grid = new JSonGrid();

		List<Goods> goodsList = goodsService.computerGoodsWm(Integer.valueOf(companyId), Integer.valueOf(ificationId),
				Integer.valueOf(type));

		for (Goods goodss : goodsList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", goodss.getId());
			record.addColumn("code", goodss.getCode());
			record.addColumn("name", goodss.getName());
			if (StringUtil.isEmpty(goodss.getSvgPrice())) {
				record.addColumn("svg_price", "0");
			} else {
				record.addColumn("svg_price", goodss.getSvgPrice());

			}
			record.addColumn("price", goodss.getPrice());
			record.addColumn("unit", goodss.getUnit());
			record.addColumn("ksy_word", goodss.getKsyWord());
			record.addColumn("details", goodss.getDetails());
			record.addColumn("brief", goodss.getBrief());
			record.addColumn("zoom_url", goodss.getZoomUrl());
			record.addColumn("original_url", goodss.getOriginalUrl());
			record.addColumn("create_time", goodss.getCreateTime());
			record.addColumn("click", goodss.getClick());
			record.addColumn("mon_sales", goodss.getMon_sales());
			record.addColumn("total_sales", goodss.getTotal_sales());
			record.addColumn("recommend", goodss.getRecommend());
			record.addColumn("shelves", goodss.getShelves());
			record.addColumn("fine", goodss.getFine());
			record.addColumn("is_new", goodss.getIsNew());
			record.addColumn("Selling", goodss.getSelling());
			record.addColumn("is_svg", goodss.getIsSvg());
			record.addColumn("stock", goodss.getStock()); // 增加库存 ynw
			List<Attribute> attributes = attributeService.getattribute(goodss.getId());
			StringBuilder natureContent = new StringBuilder();
			for (Attribute attribute : attributes) {
				List<Nature> natures = natureService.getnature(attribute.getId());
				natureContent.append(attribute.getName() + "#");
				for (int i = 0; i < natures.size(); i++) {
					if ((i + 1) == natures.size()) {
						natureContent.append(natures.get(i).getContent() + "@");
					} else {
						natureContent.append(natures.get(i).getContent() + ",");
					}
					record.addColumn("natureContent", natureContent.toString());
				}
			}

			grid.addRecord(record);

		}

		if (goodsList.size() > 0) {
			grid.addProperties("totalCount", goodsList.size());
		} else {
			grid.addProperties("totalCount", 0);
		}

		new PushJson().outString(grid.toJSonString("list"), response);

	}

}