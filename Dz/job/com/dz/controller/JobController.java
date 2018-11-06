package com.dz.controller;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Cate;
import com.dz.entity.Company;
import com.dz.entity.ComputerCate;
import com.dz.entity.Goods;
import com.dz.entity.InsideOrder;
import com.dz.entity.Job;
import com.dz.entity.Order;
import com.dz.entity.OrderType;
import com.dz.entity.Relating;
import com.dz.entity.Staff;
import com.dz.service.ICateService;
import com.dz.service.IComputerCateService;
import com.dz.service.IGoodsService;
import com.dz.service.IInsideOrderService;
import com.dz.service.IJobService;
import com.dz.service.IOrderService;
import com.dz.service.IOrderTypeService;
import com.dz.service.IRelatingService;
import com.dz.service.IStaffService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/job")
public class JobController {

	@Autowired
	private IJobService jobService;

	@Autowired
	private IRelatingService relatingService;

	@Autowired
	private IGoodsService goodsService;

	@Autowired
	private IOrderService orderService;

	@Autowired
	private IOrderTypeService orderTypeService;

	@Autowired
	private ICateService cateService;

	@Autowired
	private IStaffService staffService;

	@Autowired
	private IInsideOrderService insideOrderService;

	@Autowired
	private IComputerCateService computerCateService;

	// 按订单添加
	@RequestMapping(params = "saveInside", method = RequestMethod.POST)
	public void saveInside(HttpServletRequest request, HttpServletResponse response) {

		String insideOrderId = request.getParameter("insideOrderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(insideOrderId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Relating> relatingList = relatingService.getrelatingbyinsideID(Integer.valueOf(insideOrderId));
		InsideOrder insideOrder = insideOrderService.getInsideOrder(Integer.valueOf(insideOrderId));
		if (insideOrder == null) {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		for (Relating relating : relatingList) {
			Goods goods = goodsService.getGoods(relating.getGoodsId());
			if (goods == null) {
				message.addProperty("message", "商品不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			Job job = new Job();
			job.setGoodsId(relating.getGoodsId() + "");
			job.setCompanyId(relating.getCompanyId() + "");
			job.setOrderId(relating.getOrderId());
			job.setNum(relating.getNumb() + "");

			job.setPrintName(goods.getPrintName());
			String price = goods.getPrice();
			if (!StringUtil.isEmpty(goods.getSvgPrice())) {
				if (!goods.getSvgPrice().equals("0")) {
					price = goods.getSvgPrice();
				}
			}
			job.setPrice(price);
			job.setGoodsName(goods.getName());
			job.setInsideOrderId(insideOrder);
			job.setIsPrint("0");
			job.setIsPrintBase("1");

			jobService.saveORupdate(job);

		}

		message.addProperty("message", "打印成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response) {
		// String companyId = request.getParameter("companyId");
		String userName = request.getParameter("userName");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(userName)) {
			message.addProperty("message", "companyId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
		}

		Staff staff = staffService.login(userName);
		if (staff == null) {
			message.addProperty("message", "帐户不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (staff.getCompanyId() == null) {
			message.addProperty("message", "商家不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		Company company = staff.getCompanyId();

		List<Job> list = jobService.getOrderId(company.getId() + "");
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		String name = "";
		String num = "";
		String price = "";
		String total = "";
		String printName = "";
		DecimalFormat dFormat = new DecimalFormat("######0.00");
		for (Job jobs : list) {
			if (jobs.getOrderId() != null) {
				List<Job> jobList = jobService.getjobList(jobs.getOrderId().getId() + "");
				JSonGridRecord record = new JSonGridRecord();
				Order order = jobs.getOrderId();
				record.addColumn("storeName", company.getName());
				record.addColumn("remarks", order.getRemarks());
				record.addColumn("takeawayOrderNum", order.getOrderNo());
				record.addColumn("allTatal", order.getPay());
				if (order.getOrderType().equals("1")) {
					record.addColumn("orderCategory", "外卖订单");
					OrderType orderType = orderTypeService.getOrderType(order.getId());
					record.addColumn("shippingFee", orderType.getPrice());
					record.addColumn("lunchBoxFee", orderType.getBoxPrice());
					record.addColumn("code", orderType.getCode());
					if (orderType.getAddressId() != null) {
						record.addColumn("clientAddress",
								orderType.getAddressId().getAddress() + orderType.getAddressId().getRoom());
						record.addColumn("clientPhone", orderType.getAddressId().getPhone());
						record.addColumn("clientName", orderType.getAddressId().getName());
					} else {
						record.addColumn("clientAddress", "自取订单");
						record.addColumn("clientPhone", "自取订单");
						record.addColumn("clientName", "自取订单");
					}
					double subTotal = Double.valueOf(order.getTotal());
					double subPay = Double.valueOf(order.getPay());
					record.addColumn("allTatal", dFormat.format(subTotal));
					record.addColumn("pay", dFormat.format(subPay));
					record.addColumn("discount", dFormat.format(subTotal - subPay));
				}
				if (order.getOrderType().equals("2")) {
					Cate cate = cateService.getCate(order.getId());
					record.addColumn("orderCategory", "网络预约菜单");
					record.addColumn("tableware", "餐位费");
					record.addColumn("tablewareNum", cate.getMeals());
					record.addColumn("tablewareTatal", cate.getMealFee());
					record.addColumn("clientPhone", cate.getPhone());
					record.addColumn("destineTime", cate.getEndTime());
					if (cate.getSeat().equals("bx")) {
						record.addColumn("tableNum", "包厢:" + cate.getName() + "," + cate.getTableNo());
					}
					if (cate.getSeat().equals("dt")) {
						record.addColumn("tableNum", "大厅:(" + cate.getTableNo() + ")");
					}
				}
				for (Job job : jobList) {
					name = name + job.getGoodsName() + ",";
					num = num + job.getNum() + ",";
					price = price + job.getPrice() + ",";
					total = total + dFormat.format(Integer.valueOf(job.getNum()) * Double.valueOf(job.getPrice()))
							+ ",";
					printName = printName + company.getPrint() + ",";
					job.setIsPrint("1");
					jobService.saveORupdate(job);
				}
				record.addColumn("name", name);
				record.addColumn("price", price);
				record.addColumn("num", num);
				record.addColumn("printName", printName);
				grid.addRecord(record);
			}
			if (jobs.getInsideOrderId() != null) {
				List<Job> jobList = jobService.getInsidejobList(jobs.getInsideOrderId().getId() + "");
				JSonGridRecord record = new JSonGridRecord();
				InsideOrder order = jobs.getInsideOrderId();
				record.addColumn("storeName", order.getCompanyId().getName());
				record.addColumn("remarks", order.getRemarks());
				record.addColumn("takeawayOrderNum", order.getOrderNo());
				record.addColumn("allTatal", order.getPay());
				ComputerCate cate = computerCateService.getCate(order.getId());
				record.addColumn("orderCategory", "前台点菜单");
				record.addColumn("tableware", "餐位费");
				record.addColumn("tablewareNum", cate.getMeals());
				record.addColumn("tablewareTatal",
						dFormat.format(Double.valueOf(cate.getMealFee()) * Double.valueOf(cate.getMeals())));
				record.addColumn("clientPhone", null);
				if (cate.getSeat().equals("bx")) {
					record.addColumn("tableNum", "包厢:" + cate.getName() + "," + cate.getTableNo());
				}
				if (cate.getSeat().equals("dt")) {
					record.addColumn("tableNum", "大厅:(" + cate.getTableNo() + ")");
				}
				for (Job job : jobList) {
					name = name + job.getGoodsName() + ",";
					num = num + job.getNum() + ",";
					price = price + job.getPrice() + ",";
					total = total + dFormat.format(Integer.valueOf(job.getNum()) * Double.valueOf(job.getPrice()))
							+ ",";
					printName = printName + company.getPrint() + ",";
					job.setIsPrint("1");
					jobService.saveORupdate(job);
				}
				record.addColumn("name", name);
				record.addColumn("price", price);
				record.addColumn("num", num);
				record.addColumn("printName", printName);
				grid.addRecord(record);
			}
			name = "";
			num = "";
			price = "";
			total = "";
			printName = "";
		}

		grid.addProperties("totailCount", list.size());
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 后厨查询列表
	@RequestMapping(params = "singleView", method = RequestMethod.POST)
	public void singleView(HttpServletRequest request, HttpServletResponse response) {
		// String companyId = request.getParameter("companyId");
		String userName = request.getParameter("userName");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(userName)) {
			message.addProperty("message", "companyId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
		}

		Staff staff = staffService.login(userName);
		if (staff == null) {
			message.addProperty("message", "帐户不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (staff.getCompanyId() == null) {
			message.addProperty("message", "商家不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		Company company = staff.getCompanyId();

		List<Job> list = jobService.getList(company.getId());
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		JSonGridRecord record = new JSonGridRecord();
		for (Job job : list) {
			if (job.getOrderId() != null) {
				Order order = job.getOrderId();
				if (order.getOrderType().equals("2")) {
					record.addColumn("remarks", order.getRemarks());
					record.addColumn("orderCategory", "厨房点菜单");
					record.addColumn("storeName", order.getCompanyId().getName());
					record.addColumn("takeawayOrderNum", order.getOrderNo());
					Cate cate = cateService.getCate(order.getId());
					if (cate != null) {
						if (cate.getSeat().equals("bx")) {
							record.addColumn("tableNum", "包厢:" + cate.getName() + "," + cate.getTableNo());
						}
						if (cate.getSeat().equals("dt")) {
							record.addColumn("tableNum", "大厅:(" + cate.getTableNo() + ")");
						}
						record.addColumn("destineTime", cate.getEndTime());
					}
					record.addColumn("name", job.getGoodsName());
					record.addColumn("price", job.getPrice());
					record.addColumn("num", job.getNum());
					record.addColumn("printName", job.getPrintName());
					if (!"0".equals(job.getRemark())) {
						record.addColumn("remarks", job.getRemark());
					}
					job.setIsPrintBase("1");
					jobService.saveORupdate(job);
					grid.addRecord(record);
					grid.addProperties("totailCount", 1);
					grid.addProperties("totailCount", list.size());
					grid.addProperties("success", true);
					new PushJson().outString(grid.toJSonString("list"), response);
					return;
				}
				if (order.getOrderType().equals("1")) {
					String name = "";
					String num = "";
					String price = "";
					String total = "";
					String printName = "";
					record.addColumn("storeName", order.getCompanyId().getName());
					record.addColumn("remarks", order.getRemarks());
					record.addColumn("takeawayOrderNum", order.getOrderNo());
					record.addColumn("allTatal", order.getPay());
					record.addColumn("orderCategory", "外卖订单");
					OrderType orderType = orderTypeService.getOrderType(order.getId());
					record.addColumn("shippingFee", orderType.getPrice());
					record.addColumn("lunchBoxFee", orderType.getBoxPrice());
					record.addColumn("code", orderType.getCode());
					if (orderType.getAddressId() != null) {
						record.addColumn("clientAddress",
								orderType.getAddressId().getAddress() + orderType.getAddressId().getRoom());
						record.addColumn("clientPhone", orderType.getAddressId().getPhone());
						record.addColumn("clientName", orderType.getAddressId().getName());
					} else {
						record.addColumn("clientAddress", "自取订单");
						record.addColumn("clientPhone", "自取订单");
						record.addColumn("clientName", "自取订单");
					}
					DecimalFormat dFormat = new DecimalFormat("######.00");
					double subTotal = Double.valueOf(order.getTotal());
					double subPay = Double.valueOf(order.getPay());
					record.addColumn("allTatal", dFormat.format(subTotal));
					record.addColumn("pay", dFormat.format(subPay));
					record.addColumn("discount", dFormat.format(subTotal - subPay));
					List<Job> jobList = jobService.getjobListBase(order.getId() + "");
					for (Job jobs : jobList) {
						name = name + jobs.getGoodsName() + ",";
						num = num + jobs.getNum() + ",";
						price = price + jobs.getPrice() + ",";
						total = total + dFormat.format(Integer.valueOf(jobs.getNum()) * Double.valueOf(jobs.getPrice()))
								+ ",";
						printName = printName + jobs.getPrintName() + ",";
						jobs.setIsPrintBase("1");
						jobService.saveORupdate(jobs);
					}
					record.addColumn("name", name);
					record.addColumn("price", price);
					record.addColumn("num", num);
					record.addColumn("printName", printName);
					grid.addRecord(record);
					grid.addProperties("totailCount", list.size());
					grid.addProperties("success", true);
					new PushJson().outString(grid.toJSonString("list"), response);
					return;
				}
			}

			if (job.getInsideOrderId() != null) {
				InsideOrder order = job.getInsideOrderId();
				record.addColumn("remarks", order.getRemarks());
				record.addColumn("orderCategory", "厨房点菜单");
				record.addColumn("storeName", order.getCompanyId().getName());
				record.addColumn("takeawayOrderNum", order.getOrderNo());
				ComputerCate cate = computerCateService.getCate(order.getId());
				if (cate != null) {
					if (cate.getSeat().equals("bx")) {
						record.addColumn("tableNum", "包厢:" + cate.getName() + "," + cate.getTableNo());
					}
					if (cate.getSeat().equals("dt")) {
						record.addColumn("tableNum", "大厅:(" + cate.getTableNo() + ")");
					}
				}
				record.addColumn("name", job.getGoodsName());
				record.addColumn("price", job.getPrice());
				record.addColumn("num", job.getNum());
				record.addColumn("printName", job.getPrintName());
				if (!"0".equals(job.getRemark())) {
					record.addColumn("remarks", job.getRemark());
				}
				job.setIsPrintBase("1");
				jobService.saveORupdate(job);
				grid.addRecord(record);
				grid.addProperties("totailCount", 1);
				grid.addProperties("totailCount", list.size());
				grid.addProperties("success", true);
				new PushJson().outString(grid.toJSonString("list"), response);
				return;
			}
		}
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 按订单添加
	@RequestMapping(params = "save", method = RequestMethod.POST)
	public void save(HttpServletRequest request, HttpServletResponse response) {

		String orderId = request.getParameter("orderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Relating> relatingList = relatingService.getrelating(Integer.valueOf(orderId));
		Order order = orderService.getOrder(Integer.valueOf(orderId));
		if (order == null) {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		for (Relating relating : relatingList) {
			Goods goods = goodsService.getGoods(relating.getGoodsId());
			if (goods == null) {
				message.addProperty("message", "商品不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			Job job = new Job();
			job.setGoodsId(relating.getGoodsId() + "");
			job.setCompanyId(relating.getCompanyId() + "");
			job.setOrderId(relating.getOrderId());
			job.setNum(relating.getNumb() + "");

			job.setPrintName(goods.getPrintName());
			String price = goods.getPrice();
			if (!StringUtil.isEmpty(goods.getSvgPrice())) {
				if (!goods.getSvgPrice().equals("0")) {
					price = goods.getSvgPrice();
				}
			}
			job.setPrice(price);
			job.setGoodsName(goods.getName());
			job.setOrderId(order);
			job.setIsPrint("0");
			job.setIsPrintBase("0");
			jobService.saveORupdate(job);
		}

		message.addProperty("message", "打印成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 单个添加
	@RequestMapping(params = "saveGoods", method = RequestMethod.POST)
	public void saveGoods(HttpServletRequest request, HttpServletResponse response) {

		String orderId = request.getParameter("orderId");
		String goodsIdList = request.getParameter("goodsId");
		String num = request.getParameter("num");
		// String price = request.getParameter("price");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(goodsIdList) || StringUtil.isEmpty(num)
		// || StringUtil.isEmpty(price)
		) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		// if (!NumberUtils.isNumber(price)) {
		// message.addProperty("message", "价格不正确");
		// message.addProperty("success", false);
		// new PushJson().outString(message.toJSonString(), response);
		// return;
		// }

		Order order = orderService.getOrder(Integer.valueOf(orderId));
		if (order == null) {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		String[] goodsIds = goodsIdList.split(",");
		for (int i = 0; i < goodsIds.length; i++) {
			Goods goods = goodsService.getGoods(Integer.valueOf(goodsIds[i].toString()));
			if (goods == null) {
				message.addProperty("message", "商品不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			Job job = new Job();
			job.setGoodsId(goods.getId() + "");
			job.setCompanyId(goods.getCompanyId().getId() + "");
			job.setOrderId(order);
			job.setNum(num);
			job.setPrintName(goods.getPrintName());
			String price = goods.getPrice();
			if (!StringUtil.isEmpty(goods.getSvgPrice())) {
				if (!goods.getSvgPrice().equals("0")) {
					price = goods.getSvgPrice();
				}
			}
			job.setPrice(price);
			job.setGoodsName(goods.getName());
			job.setIsPrintBase("0");
			job.setIsPrint("0");

			jobService.saveORupdate(job);
		}

		message.addProperty("message", "添加成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 预定接单
	@RequestMapping(params = "destine", method = RequestMethod.POST)
	public void destine(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Relating> relatingList = relatingService.getrelating(Integer.valueOf(orderId));
		Order order = orderService.getOrder(Integer.valueOf(orderId));
		if (order == null) {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		for (Relating relating : relatingList) {
			Goods goods = goodsService.getGoods(relating.getGoodsId());
			if (goods == null) {
				message.addProperty("message", "商品不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			Job job = new Job();
			job.setGoodsId(relating.getGoodsId() + "");
			job.setCompanyId(relating.getCompanyId() + "");
			job.setOrderId(relating.getOrderId());
			job.setNum(relating.getNumb() + "");
			job.setPrintName(goods.getPrintName());
			String price = goods.getPrice();
			if (!StringUtil.isEmpty(goods.getSvgPrice())) {
				if (!goods.getSvgPrice().equals("0")) {
					price = goods.getSvgPrice();
				}
			}
			job.setPrice(price);
			job.setGoodsName(goods.getName());
			job.setOrderId(order);
			job.setIsPrint("0");
			job.setIsPrintBase("1");
			jobService.saveORupdate(job);

		}

		message.addProperty("message", "打印成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// ===================================线下订单==================================
	// 按订单添加 加到job表 ynw
	@RequestMapping(params = "onsideSave", method = RequestMethod.POST)
	public void onsideSave(HttpServletRequest request, HttpServletResponse response) {

		String orderId = request.getParameter("orderId");// 线下订单id

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Relating> relatingList = relatingService.getrelating(Integer.valueOf(orderId));
		Order order = orderService.getOrder(Integer.valueOf(orderId));
		if (order == null) {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		for (Relating relating : relatingList) {
			Goods goods = goodsService.getGoods(relating.getGoodsId());
			if (goods == null) {
				message.addProperty("message", "商品不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			Job job = new Job();
			job.setGoodsId(relating.getGoodsId() + "");
			job.setCompanyId(relating.getCompanyId() + "");
			job.setOrderId(relating.getOrderId());
			job.setNum(relating.getNumb() + "");
			job.setPrintName(goods.getPrintName());
			String price = goods.getPrice();
			if (!StringUtil.isEmpty(goods.getSvgPrice())) {
				if (!goods.getSvgPrice().equals("0")) {
					price = goods.getSvgPrice();
				}
			}
			job.setPrice(price);
			job.setGoodsName(goods.getName());
			job.setIsPrint("0");
			job.setIsPrintBase("0");
			jobService.saveORupdate(job);

		}

		message.addProperty("message", "打印成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 按订单添加
	@RequestMapping(params = "insideSave", method = RequestMethod.POST)
	public void insideSave(HttpServletRequest request, HttpServletResponse response) {

		String orderId = request.getParameter("insideorderId");// 线下订单id

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Relating> relatingList = relatingService.getrelating(Integer.valueOf(orderId));
		InsideOrder insideOrder = insideOrderService.getInsideOrder(Integer.valueOf(orderId));
		if (insideOrder == null) {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		for (Relating relating : relatingList) {
			Goods goods = goodsService.getGoods(relating.getGoodsId());
			if (goods == null) {
				message.addProperty("message", "商品不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			Job job = new Job();
			job.setGoodsId(relating.getGoodsId() + "");
			job.setCompanyId(relating.getCompanyId() + "");
			job.setOrderId(relating.getOrderId());
			job.setNum(relating.getNumb() + "");
			job.setPrintName(goods.getPrintName());
			job.setRemark(relating.getRemark());// 2018-10-24 @Tyy
			String price = goods.getPrice();
			if (!StringUtil.isEmpty(goods.getSvgPrice())) {
				if (!goods.getSvgPrice().equals("0")) {
					price = goods.getSvgPrice();
				}
			}
			job.setPrice(price);
			job.setGoodsName(goods.getName());
			job.setInsideOrderId(insideOrder);
			job.setIsPrint("1");
			job.setIsPrintBase("0");
			jobService.saveORupdate(job);

		}

		message.addProperty("message", "打印成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 单个添加
	@RequestMapping(params = "insideSaveGoods", method = RequestMethod.POST)
	public void insideSaveGoods(HttpServletRequest request, HttpServletResponse response) {

		String orderId = request.getParameter("insideOrderId");// 线下订单id
		String goodsIdList = request.getParameter("goodsId");// 商品id
		String nums = request.getParameter("nums");// 商品数量
		String natureList = request.getParameter("nature");// 商品属性

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(goodsIdList) || StringUtil.isEmpty(nums)
		/* || StringUtil.isEmpty(natureList) */) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		InsideOrder insideOrder = insideOrderService.getInsideOrder(Integer.valueOf(orderId));
		if (insideOrder == null) {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		String[] goodsIds = goodsIdList.split(",");
		String[] numbArr = nums.split(",");
		String[] nature = null;
		if (!StringUtil.isEmpty(natureList)) {
			if (natureList.length() > 0) {
				nature = natureList.split(";");
			}
		}

		Map<String, Integer> mapIDNUM = new HashMap<String, Integer>(); // 将ID和数量绑定的数组
																		// ynw
		for (int i = 0; i < goodsIds.length; i++) {
			mapIDNUM.put(goodsIds[i], Integer.valueOf(numbArr[i])); // 将ID和数量绑定
																	// ynw
		}
		for (int i = 0; i < goodsIds.length; i++) {
			Goods goods = goodsService.getGoods(Integer.valueOf(goodsIds[i].toString()));
			if (goods == null) {
				message.addProperty("message", "商品不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			Job job = new Job();
			job.setGoodsId(goods.getId() + "");
			job.setCompanyId(goods.getCompanyId().getId() + "");
			job.setInsideOrderId(insideOrder);
			job.setNum(mapIDNUM.get(goodsIds[i]) + "");
			job.setPrintName(goods.getPrintName());
			String price = goods.getPrice();
			if (!StringUtil.isEmpty(goods.getSvgPrice())) {
				if (!goods.getSvgPrice().equals("0")) {
					price = goods.getSvgPrice();
				}
			}
			job.setPrice(price);
			job.setGoodsName(goods.getName());
			if (nature != null) {
				if (nature[i] != null) {
					job.setRemark(nature[i]);
				}
			}

			job.setIsPrintBase("0");
			job.setIsPrint("1");

			jobService.saveORupdate(job);
		}

		message.addProperty("message", "添加成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 退菜
	@RequestMapping(params = "insideDeleteGoods", method = RequestMethod.POST)
	public void insideDeleteGoods(HttpServletRequest request, HttpServletResponse response) {

		String orderId = request.getParameter("insideOrderId");// 线下订单id
		String goodsIdList = request.getParameter("goodsId");// 商品id
		String nums = request.getParameter("nums");// 商品数量

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(goodsIdList) || StringUtil.isEmpty(nums)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		InsideOrder insideOrder = insideOrderService.getInsideOrder(Integer.valueOf(orderId));
		if (insideOrder == null) {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		String[] goodsIds = goodsIdList.split(",");
		String[] numbArr = nums.split(",");

		Map<String, Integer> mapIDNUM = new HashMap<String, Integer>(); // 将ID和数量绑定的数组
																		// ynw
		for (int i = 0; i < goodsIds.length; i++) {
			mapIDNUM.put(goodsIds[i], Integer.valueOf(numbArr[i])); // 将ID和数量绑定
																	// ynw
		}
		String str = "(退菜)";
		for (int i = 0; i < goodsIds.length; i++) {
			Goods goods = goodsService.getGoods(Integer.valueOf(goodsIds[i].toString()));
			if (goods == null) {
				message.addProperty("message", "商品不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			Job job = new Job();
			job.setGoodsId(goods.getId() + "");
			job.setCompanyId(goods.getCompanyId().getId() + "");
			job.setInsideOrderId(insideOrder);
			job.setNum(mapIDNUM.get(goodsIds[i]) + "");
			job.setPrintName(goods.getPrintName());
			String price = goods.getPrice();
			if (!StringUtil.isEmpty(goods.getSvgPrice())) {
				if (!goods.getSvgPrice().equals("0")) {
					price = goods.getSvgPrice();
				}
			}
			job.setPrice(price);
			job.setGoodsName(str + goods.getName());
			job.setIsPrintBase("0");
			job.setIsPrint("1");

			jobService.saveORupdate(job);
		}

		message.addProperty("message", "退菜成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 线上退菜
	@RequestMapping(params = "deleteGoods", method = RequestMethod.POST)
	public void deleteGoods(HttpServletRequest request, HttpServletResponse response) {

		String orderId = request.getParameter("orderId");// 线上订单id
		String goodsIdList = request.getParameter("goodsId");// 商品id
		String nums = request.getParameter("nums");// 商品数量

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(goodsIdList) || StringUtil.isEmpty(nums)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Order order = orderService.getOrder(Integer.valueOf(orderId));
		if (order == null) {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		String[] goodsIds = goodsIdList.split(",");
		String[] numbArr = nums.split(",");

		Map<String, Integer> mapIDNUM = new HashMap<String, Integer>(); // 将ID和数量绑定的数组
																		// ynw
		for (int i = 0; i < goodsIds.length; i++) {
			mapIDNUM.put(goodsIds[i], Integer.valueOf(numbArr[i])); // 将ID和数量绑定
																	// ynw
		}
		String str = "(退菜)";
		for (int i = 0; i < goodsIds.length; i++) {
			Goods goods = goodsService.getGoods(Integer.valueOf(goodsIds[i].toString()));
			if (goods == null) {
				message.addProperty("message", "商品不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			Job job = new Job();
			job.setGoodsId(goods.getId() + "");
			job.setCompanyId(goods.getCompanyId().getId() + "");
			job.setOrderId(order);
			job.setNum(mapIDNUM.get(goodsIds[i]) + "");
			job.setPrintName(goods.getPrintName());
			String price = goods.getPrice();
			if (!StringUtil.isEmpty(goods.getSvgPrice())) {
				if (!goods.getSvgPrice().equals("0")) {
					price = goods.getSvgPrice();
				}
			}
			job.setPrice(price);
			job.setGoodsName(str + goods.getName());
			job.setIsPrintBase("0");
			job.setIsPrint("1");

			jobService.saveORupdate(job);
		}

		message.addProperty("message", "退菜成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 开台，打印到厨房
	@RequestMapping(params = "open", method = RequestMethod.POST)
	public void open(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Relating> relatingList = relatingService.getrelating(Integer.valueOf(orderId));
		Order order = orderService.getOrder(Integer.valueOf(orderId));
		if (order == null) {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		for (Relating relating : relatingList) {
			Goods goods = goodsService.getGoods(relating.getGoodsId());
			if (goods == null) {
				message.addProperty("message", "商品不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			Job job = new Job();
			job.setGoodsId(relating.getGoodsId() + "");
			job.setCompanyId(relating.getCompanyId() + "");
			job.setOrderId(relating.getOrderId());
			job.setNum(relating.getNumb() + "");

			job.setPrintName(goods.getPrintName());
			job.setPrice(goods.getPrice());
			job.setGoodsName(goods.getName());
			job.setOrderId(order);
			job.setIsPrint("1");
			job.setIsPrintBase("0");
			jobService.saveORupdate(job);

		}

		message.addProperty("message", "打印成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}