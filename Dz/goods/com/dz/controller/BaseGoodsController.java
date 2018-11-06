package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Attribute;
import com.dz.entity.Company;
import com.dz.entity.Goods;
import com.dz.entity.Nature;
import com.dz.service.IAttributeService;
import com.dz.service.ICompanyService;
import com.dz.service.IGoodsService;
import com.dz.service.INatureService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/base/goods")
public class BaseGoodsController {

	@Autowired
	private IGoodsService goodsService;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private IAttributeService attributeService;

	@Autowired
	private INatureService natureService;

	// 商品列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");// 商家ID
		String name = request.getParameter("name");// 商品名称

		Goods goods = new Goods();
		if (!StringUtil.isEmpty(companyId)) {
			Company company = companyService.getCompany(Integer.valueOf(companyId));
			goods.setCompanyId(company);
		}
		if (!StringUtil.isEmpty(name)) {
			goods.setName(name);
		}
		List<Goods> goodsList = goodsService.bastGoods(goods);
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		if (goodsList != null) {
			for (Goods goodss : goodsList) {
				JSonGridRecord record = new JSonGridRecord();
				record.addColumn("id", goodss.getId());
				record.addColumn("code", goodss.getCode());
				record.addColumn("name", goodss.getName());
				record.addColumn("price", goodss.getPrice());
				record.addColumn("svg_price", goodss.getSvgPrice());
				record.addColumn("start_time", goodss.getStartTime());
				record.addColumn("end_time", goodss.getEndTime());
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
				record.addColumn("companyName", goodss.getCompanyId().getName());
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
		}

		grid.addProperties("totalCount", goodsList.size());

		new PushJson().outString(grid.toJSonString("list"), response);

	}

}