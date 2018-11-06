package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Company;
import com.dz.entity.Delicacy;
import com.dz.entity.Goods;
import com.dz.entity.Ification;
import com.dz.service.ICompanyService;
import com.dz.service.IDelicacyService;
import com.dz.service.IGoodsService;
import com.dz.service.IIficationService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/ification")
public class IficationController {

	@Autowired
	private IIficationService ificationService;

	@Autowired
	private IGoodsService goodsService;

	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private IDelicacyService delicacyService;

	@RequestMapping(params = "getFication", method = RequestMethod.POST)
	// 获得商品分类
	public void getFication(HttpServletRequest request,
			HttpServletResponse response, Ification ification) {
		String companyId = request.getParameter("id");// 商家ID

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "companyId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
	
		JSonGrid grid = new JSonGrid();

		List<Ification> ificationList = ificationService.getIfication(Integer
				.valueOf(companyId));
		if (ificationList != null) {
			for (Ification ifications : ificationList) {
				JSonGridRecord record = new JSonGridRecord();
				record.addColumn("id", ifications.getId());
				record.addColumn("name", ifications.getName());
				record.addColumn("details", ifications.getDetails());
				record.addColumn("keyWord", ifications.getKeyWord());
				record.addColumn("unit", ifications.getUnit());
				grid.addRecord(record);

			}
			Delicacy delicacy = delicacyService.getDelicacy(Integer.valueOf(companyId));	//得到对应商家的属性表
			grid.addProperties("mealFee", delicacy.getMealFee());
			new PushJson().outString(grid.toJSonString("list"), response);
		}
	}

	// 商品分类管理
	@RequestMapping(params = "getIfication", method = RequestMethod.POST)
	public void getIfication(HttpServletRequest request,
			HttpServletResponse response, Ification ification) {
		String companyId = request.getParameter("id");// 商家ID

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "companyId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		JSonGrid grid = new JSonGrid();

		List<Ification> ificationList = ificationService.getIfication(Integer
				.valueOf(companyId));
		if (ificationList != null) {
			for (Ification ifications : ificationList) {
				List<Goods> goodsList = goodsService.companyGoods(Integer
						.valueOf(companyId), ifications.getId());
				JSonGridRecord record = new JSonGridRecord();
				if (goodsList == null) {
					record.addColumn("totail", 0);
				} else {
					record.addColumn("totail", goodsList.size());
				}
				record.addColumn("id", ifications.getId());
				record.addColumn("name", ifications.getName());
				grid.addRecord(record);

			}
		}
		new PushJson().outString(grid.toJSonString("list"), response);

	}
	
	// 商品分类管理(外卖和美食新加的方法)
		@RequestMapping(params = "getIficationCommon", method = RequestMethod.POST)
		public void getIficationCommon(HttpServletRequest request,
				HttpServletResponse response, Ification ification) {
			String companyId = request.getParameter("id");// 商家ID
			String type = request.getParameter("type");// 商家类型

			JSonMessage message = new JSonMessage();
			if (StringUtil.isEmpty(companyId)) {
				message.addProperty("message", "companyId不能为空");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			if (StringUtil.isEmpty(type)) {
				message.addProperty("message", "商家类型不能为空");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			JSonGrid grid = new JSonGrid();

			List<Ification> ificationList = ificationService.getIfication(Integer
					.valueOf(companyId));
			if (ificationList != null) {
				for (Ification ifications : ificationList) {
					List<Goods> goodsList = goodsService.companyGoodsWm(Integer
							.valueOf(companyId), ifications.getId(),Integer
							.valueOf(type));
					JSonGridRecord record = new JSonGridRecord();
					if (goodsList == null) {
						record.addColumn("totail", 0);
					} else {
						record.addColumn("totail", goodsList.size());
					}
					record.addColumn("id", ifications.getId());
					record.addColumn("name", ifications.getName());
					grid.addRecord(record);

				}
			}
			new PushJson().outString(grid.toJSonString("list"), response);

		}
	
	// 商品分类管理
	@RequestMapping(params = "getType", method = RequestMethod.POST)
	public void getType(HttpServletRequest request,
			HttpServletResponse response, Ification ification) {
		String companyId = request.getParameter("id");// 商家ID

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "companyId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		JSonGrid grid = new JSonGrid();

		List<Ification> ificationList = ificationService.getIfication(Integer
				.valueOf(companyId));
		if (ificationList != null) {
			for (Ification ifications : ificationList) {
				JSonGridRecord record = new JSonGridRecord();
				record.addColumn("id", ifications.getId());
				record.addColumn("value", ifications.getName());
				grid.addRecord(record);

			}
		}
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Ification ification,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(ification.getId() + "")
				|| ification.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		ificationService.delete(ification.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request,
			HttpServletResponse response) {
		String companyId = request.getParameter("companyId");// 商家ID
		String name = request.getParameter("name");// 分类名称
		String details = request.getParameter("details");// 描述
		String isView = request.getParameter("isView");// 是否展示
		String keyWord = request.getParameter("keyWord");// 关键字
		String unit = request.getParameter("unit");// 单位

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(name)
				|| StringUtil.isEmpty(isView)) {
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

		Ification ification = new Ification();
		ification.setCompanyId(company);
		ification.setName(name);
		if (!StringUtil.isEmpty(details)) {
			ification.setDetails(details);
		}
		if (!StringUtil.isEmpty(keyWord)) {
			ification.setKeyWord(keyWord);
		}
		if (!StringUtil.isEmpty(unit)) {
			ification.setUnit(unit);
		}
		if (isView.equals("0")) {
			ification.setIsView(false);
		} else if (isView.equals("1")) {
			ification.setIsView(true);
		} else {
			message.addProperty("message", "isView值不正确");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		ificationService.saveORupdate(ification);

		message.addProperty("message", "添加成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 修改信息
	@RequestMapping(params = "update", method = RequestMethod.POST)
	public void update(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");// 分类
		String name = request.getParameter("name");// 分类名称
		String details = request.getParameter("details");// 描述
		String keyWord = request.getParameter("keyWord");// 关键字
		String unit = request.getParameter("unit");// 单位
		String isView = request.getParameter("isView");// 是否展示

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);			
			return;
		}
		if (StringUtil.isEmpty(name)) {
			message.addProperty("message", "分类名称不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		Ification ification = ificationService.find(Integer.valueOf(id));
		if (ification == null) {
			message.addProperty("message", "商家不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (!StringUtil.isEmpty(name)) {
			ification.setName(name);
		}
		if (!StringUtil.isEmpty(details)) {
			ification.setDetails(details);
		}
		if (!StringUtil.isEmpty(keyWord)) {
			ification.setKeyWord(keyWord);
		}
		if (!StringUtil.isEmpty(unit)) {
			ification.setUnit(unit);
		}
		if (!StringUtil.isEmpty(unit)) {
			if (isView.equals("0")) {
				ification.setIsView(false);
			} else if (isView.equals("1")) {
				ification.setIsView(true);
			} else {
				message.addProperty("message", "isView值不正确");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
		}
		
		ificationService.saveORupdate(ification);

		message.addProperty("message", "添加成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 分类详情
	@RequestMapping(params = "find", method = RequestMethod.POST)
	public void find(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");// 分类

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Ification ification = ificationService.find(Integer.valueOf(id));
		if (ification == null) {
			message.addProperty("message", "分类不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		message.addProperty("id", ification.getId());
		message.addProperty("name", ification.getName());
		message.addProperty("details", ification.getDetails());
		message.addProperty("keyWord", ification.getKeyWord());
		message.addProperty("unit", ification.getUnit());
		message.addProperty("isView", ification.getIsView());
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}