package com.dz.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Company;
import com.dz.entity.CompanyDetailed;
import com.dz.entity.CompanyExamine;
import com.dz.entity.Delicacy;
import com.dz.entity.Distribution;
import com.dz.entity.Order;
import com.dz.entity.PowerSort;
import com.dz.entity.SalerCompany;
import com.dz.entity.SalerInfo;
import com.dz.entity.Staff;
import com.dz.service.ICompanyDetailedService;
import com.dz.service.ICompanyExamineService;
import com.dz.service.ICompanyService;
import com.dz.service.IDelicacyService;
import com.dz.service.IDistributionService;
import com.dz.service.IOrderService;
import com.dz.service.IPowerSortService;
import com.dz.service.ISalerCompanyService;
import com.dz.service.ISalerInfoService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.JsonUtil;
import com.dz.util.MD5Util;
import com.dz.util.PushJson;
import com.dz.util.SmessageUtils;
import com.dz.util.StringUtil;
import com.dz.util.TokenUtil;

@Controller
@RequestMapping("/base/company")
public class BaseCompanyController {

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private IDistributionService distributionService;

	@Autowired
	private IDelicacyService delicacyService;

	@Autowired
	private ICompanyExamineService companyExamineService;

	@Autowired
	private ISalerCompanyService salerCompanyService;

	@Autowired
	private IPowerSortService powerSortService;

	@Autowired
	private IOrderService orderService;

	@Autowired
	private ISalerInfoService salerService;

	@Autowired
	private ICompanyDetailedService companyDetailedService;

	private static String sign(StringBuilder builder) {
		return MD5Util.MD5(builder.toString());
	}

	// ==========================================
	// 后台管理端============================================
	// 后台经营数据 ynw
	@RequestMapping(params = "operatingview", method = RequestMethod.POST)
	public void operatingview(HttpServletRequest request,
			HttpServletResponse response) {
		String name = request.getParameter("name");// 商家名称
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String info = request.getParameter("info"); // 区域

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime)) {
			startTime = new SimpleDateFormat("yyyy-MM-dd 00:00:00")
					.format(new Date());
			endTime = new SimpleDateFormat("yyyy-MM-dd 23:59:59")
					.format(new Date());
		} else {
			try {
				Date startDate = new SimpleDateFormat("yyyy-MM-dd")
						.parse(startTime);
				Date endDate = new SimpleDateFormat("yyyy-MM-dd")
						.parse(endTime);

				startTime = new SimpleDateFormat("yyyy-MM-dd 00:00:00")
						.format(startDate);
				endTime = new SimpleDateFormat("yyyy-MM-dd 23:59:59")
						.format(endDate);

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		Company company = new Company();
		if (!StringUtil.isEmpty(name)) {
			company.setName(name);
		}
		if (!StringUtil.isEmpty(info)) {
			company.setInfo(info);
		}

		List<Company> count = companyService.basecompanyList(company); /* ynw */
		List<Company> companyList = companyService.basecompanyList(company,
				Integer.valueOf(start), Integer.valueOf(limit)); /* ynw */
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (Company companys : companyList) {
			JSonGridRecord record = new JSonGridRecord();
			List<Order> orderList = orderService.getStatus(companys.getId(),
					startTime, endTime, "finish"); // 获得对应商家该时间段内已结账的订单
			double balance = 0;
			for (Order order : orderList) {
				balance = balance + Double.valueOf(order.getTotal()); // 得到总金额
			}
			record.addColumn("balance", String.format("%.2f", balance));
			record.addColumn("id", companys.getId());
			record.addColumn("name", companys.getName());
			record.addColumn("info", companys.getInfo());
			record.addColumn("startTime", startTime);
			record.addColumn("endTime", endTime);
			record.addColumn("orderCount", orderList.size());
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", count.size()); /* ynw */
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request,
			HttpServletResponse response) {
		String coordinates = request.getParameter("coordinates");// 坐标
		String info = request.getParameter("info");// 所属区域
		String name = request.getParameter("name");// 店名
		String phone = request.getParameter("phone");// 联系方式
		String position = request.getParameter("position");// 地理位置
		String classifyId = request.getParameter("classifyId");// 商家分类
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(coordinates) || StringUtil.isEmpty(name)
				|| StringUtil.isEmpty(info) || StringUtil.isEmpty(phone)
				|| StringUtil.isEmpty(position)
				|| StringUtil.isEmpty(classifyId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = new Company();
		company.setCoordinates(coordinates);
		company.setInfo(info);
		company.setName(name);
		company.setPhone(phone);
		company.setPosition(position);
		company.setClassifyId(classifyId);
		company.setReceipt("0");
		company.setAudit(0);
		company.setAssess("5");
		company.setOpen(false);
		companyService.saveORupdate(company);
		Distribution distribution = new Distribution();
		distribution.setCompanyId(company.getId() + "");
		distribution.setGDP("0");
		distribution.setMiniPrice("0");
		distribution.setMode("商家配送");
		distribution.setTime("30");
		distribution.setDistributionPrice("3");

		Delicacy delicacy = new Delicacy();
		delicacy.setCompanyId(company.getId() + "");
		delicacy.setGdp("0");
		delicacy.setMealFee("0");
		if (company.getClassifyId().equals("1")) {
			distributionService.saveORupdate(distribution);
		} else if (company.getClassifyId().equals("2")) {
			delicacyService.saveORupdate(delicacy);
		} else if (company.getClassifyId().equals("1,2")) {
			distributionService.saveORupdate(distribution);
			delicacyService.saveORupdate(delicacy);
		}

		message.addProperty("message", "开店成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加
	@RequestMapping(params = "save", method = RequestMethod.POST)
	public void save(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");// id
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		CompanyExamine companyExamine = companyExamineService.getid(Integer
				.valueOf(id));
		if (companyExamine == null) {
			message.addProperty("message", "申请信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = new Company();
		SalerCompany salerCompany = new SalerCompany();

		if (!StringUtil.isEmpty(companyExamine.getMain())) {
			company.setClassifyId(companyExamine.getMain());
			salerCompany.setMain(companyExamine.getMain());
		}
		if (!StringUtil.isEmpty(companyExamine.getDeputy())) {
			company = companyService.getCompany(companyExamine.getShopPhone());
			if (company != null) {
				company.setClassifyId("1,2");
				companyService.saveORupdate(company);
				message.addProperty("message", "添加成功");
				message.addProperty("success", true);
				new PushJson().outString(message.toJSonString(), response);
				return;
			} else {
				company = new Company();
				company.setClassifyId("1,2");
				salerCompany.setDeputy(companyExamine.getDeputy());
			}
		}

		company.setCoordinates(companyExamine.getLocation());
		company.setName(companyExamine.getName());
		company.setPhone(companyExamine.getShopPhone());
		company.setPosition(companyExamine.getAddress());
		company.setReceipt("0");
		company.setAudit(0);
		company.setAssess("5");
		company.setMonSales("0");
		company.setOpen(false);
		company.setHonor("资质");

		try {
			String district = "";
			String result = JsonUtil.getDistrict(companyExamine.getAddress());
			if (!StringUtil.isEmpty(result)) {
				district = JsonUtil.getDistrict(companyExamine.getAddress());
			}
			company.setInfo(district);
		} catch (Exception e) {
			company.setInfo("");
		}
		companyService.save(company);

		salerCompany.setCompanyId(company.getId());
		salerCompany.setSalerId(companyExamine.getSalerId());
		salerCompany.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date()));
		salerCompanyService.saveORupdate(salerCompany);

		Distribution distribution = new Distribution();
		distribution.setCompanyId(company.getId() + "");
		distribution.setGDP("0");
		distribution.setMiniPrice("0");
		distribution.setMode("商家配送");
		distribution.setTime("30");
		distribution.setDistributionPrice("3");

		Delicacy delicacy = new Delicacy();
		delicacy.setCompanyId(company.getId() + "");
		delicacy.setGdp("0");
		delicacy.setMealFee("0");
		if (company.getClassifyId().equals("1")) {
			distributionService.saveORupdate(distribution);
		} else if (company.getClassifyId().equals("2")) {
			delicacyService.saveORupdate(delicacy);
		} else if (company.getClassifyId().equals("1,2")
				|| company.getClassifyId().equals("2,1")) {
			distributionService.saveORupdate(distribution);
			delicacyService.saveORupdate(delicacy);
		}

		String randomCode = TokenUtil.getRandomChar(8);
		PowerSort powerSort = powerSortService.getid(1);
		StringBuilder builder = new StringBuilder();
		builder.append(randomCode + "123456");
		String localSign = sign(builder);

		Staff staff = new Staff();
		staff.setCompanyId(company);
		staff.setName(companyExamine.getName());
		staff.setUserName(companyExamine.getPhone());
		staff.setRandomCode(randomCode);
		staff.setPowerSortId(powerSort);
		staff.setPhone(companyExamine.getPhone());
		staff.setPassword(localSign);

		companyExamineService.delete(id);

		CompanyDetailed companyDetailed = new CompanyDetailed();
		if (companyExamine.getSalerId() != 0) {
			SalerInfo saler = salerService.getid(companyExamine.getSalerId());
			if (saler != null) {
				companyDetailed.setSalerId(saler);
			}
		}
		companyDetailed.setCompanyId(company);
		companyDetailedService.saveORupdate(companyDetailed);

		message.addProperty("message", "开店成功");
		message.addProperty("success", true);
		new SmessageUtils().sendToCompany(companyExamine.getPhone(), companyExamine.getPhone(), localSign);	//将对应推广员的商家账号密码发送过去  ynw
		new PushJson().outString(message.toJSonString(), response);
	}

	// 后台商家列表
	@RequestMapping(params = "baseview", method = RequestMethod.POST)
	public void baseview(HttpServletRequest request,
			HttpServletResponse response) {
		String name = request.getParameter("name");// 商家名称
		String info = request.getParameter("info"); // add ynw 商家区域

		String start = request.getParameter("start"); /* ynw */
		String limit = request.getParameter("limit"); /* ynw */

		JSonMessage message = new JSonMessage(); /* ynw start */
		if (StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		} /* ynw end */

		Company company = new Company();
		if (!StringUtil.isEmpty(name)) {
			company.setName(name);
		}
		if (!StringUtil.isEmpty(info)) { // add ynw 商家区域
			company.setPosition(info);
		}

		List<Company> count = companyService.basecompanyList(company);
		List<Company> companyList = companyService.basecompanyList(company,
				Integer.valueOf(start), Integer.valueOf(limit)); /* ynw */
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (Company companys : companyList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", companys.getId());
			record.addColumn("logo", companys.getLogo());
			record.addColumn("name", companys.getName());
			record.addColumn("phone", companys.getPhone());
			record.addColumn("position", companys.getPosition());
			record.addColumn("coordinates", companys.getCoordinates());
			record.addColumn("business_time", companys.getBusinessTimeStart()
					+ "-" + companys.getBusinessTimeEnd());
			record.addColumn("honor", companys.getHonor());
			record.addColumn("notice", companys.getNotice());
			record.addColumn("info", companys.getInfo());
			record.addColumn("assess", companys.getAssess());
			String isOpen = "";
			if (companys.getIsOpen()) {
				isOpen = "开店中";
			} else {
				isOpen = "未开店";
			}
			record.addColumn("isOpen", isOpen);
			String isBusiness = "";
			if (companys.getIsBusiness()) {
				isBusiness = "营业中";
			} else {
				isBusiness = "休息中";
			}
			record.addColumn("is_business", isBusiness);
			record.addColumn("img", companys.getImg());
			record.addColumn("classify_id", companys.getClassifyId());
			grid.addRecord(record);

		}
		grid.addProperties("totalCount", count.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 后台商家详情
	@RequestMapping(params = "companyview", method = RequestMethod.POST)
	public void companyview(HttpServletRequest request,
			HttpServletResponse response, Company company) {
		String id = request.getParameter("id");// 容量

		JSonMessage message = new JSonMessage();
		message.addProperty("success", true);

		Company companys = companyService.getCompany(Integer.valueOf(id));
		message.addProperty("id", companys.getId());
		message.addProperty("logo", companys.getLogo());
		message.addProperty("name", companys.getName());
		message.addProperty("phone", companys.getPhone());
		message.addProperty("position", companys.getPosition());
		message.addProperty("coordinates", companys.getCoordinates());
		message.addProperty("business_time", companys.getBusinessTimeStart()
				+ "-" + companys.getBusinessTimeEnd());
		message.addProperty("honor", companys.getHonor());
		message.addProperty("notice", companys.getNotice());
		message.addProperty("info", companys.getInfo());
		message.addProperty("assess", companys.getAssess());
		message.addProperty("is_close", companys.getIsOpen());
		message.addProperty("isBusiness", companys.getIsBusiness());
		message.addProperty("audit", companys.getAudit());
		message.addProperty("img", companys.getImg());
		message.addProperty("type", companys.getClassifyId());
		message.addProperty("monSales", companys.getMonSales());

		new PushJson().outString(message.toJSonString(), response);

	}

	// 审核列表
	@RequestMapping(params = "auditview", method = RequestMethod.POST)
	public void auditview(HttpServletRequest request,
			HttpServletResponse response, Company company) {

		JSonGrid grid = new JSonGrid();

		grid.addProperties("success", true);

		List<Company> companyList = companyService.auditList();

		for (Company companys : companyList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", companys.getId());
			record.addColumn("logo", companys.getLogo());
			record.addColumn("name", companys.getName());
			record.addColumn("phone", companys.getPhone());
			record.addColumn("position", companys.getPosition());
			record.addColumn("coordinates", companys.getCoordinates());
			record.addColumn("business_time", companys.getBusinessTimeStart()
					+ "-" + companys.getBusinessTimeEnd());
			record.addColumn("honor", companys.getHonor());
			record.addColumn("notice", companys.getNotice());
			record.addColumn("info", companys.getInfo());
			record.addColumn("assess", companys.getAssess());
			record.addColumn("is_close", companys.getIsOpen());
			record.addColumn("isBusiness", companys.getIsBusiness());
			record.addColumn("audit", companys.getAudit());
			record.addColumn("img", companys.getImg());
			grid.addRecord(record);

		}
		grid.addProperties("totalCount", companyList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 通过审核
	@RequestMapping(params = "auditUpdate", method = RequestMethod.POST)
	public void auditUpdate(HttpServletRequest request,
			HttpServletResponse response, Company company) {

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(company.getId() + "") || company.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		companyService.auditUpdate(company.getId() + "");
		message.addProperty("message", "审核成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);

	}

	// 店铺开关
	@RequestMapping(params = "close", method = RequestMethod.POST)
	public void close(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");// 商家id
		String isOpen = request.getParameter("isOpen");// 开关状态
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(isOpen)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		Company company = companyService.getCompany(Integer.valueOf(id));
		if (company != null) {
			if (isOpen.equals("true")) {
				company.setOpen(true);
			} else if (isOpen.equals("false")) {
				company.setOpen(false);
			} else {
				message.addProperty("message", "isOpen状态不正确");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			companyService.saveORupdate(company);
		}
		message.addProperty("message", "操作成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);

	}

	// 删除店铺
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");// 商家id
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		companyService.delete(Integer.valueOf(id));
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);

	}

}