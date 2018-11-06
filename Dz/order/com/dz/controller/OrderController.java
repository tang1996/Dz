package com.dz.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Address;
import com.dz.entity.BookTime;
import com.dz.entity.Cate;
import com.dz.entity.Company;
import com.dz.entity.CompanyActivity;
import com.dz.entity.Delicacy;
import com.dz.entity.Distribution;
import com.dz.entity.Evaluate;
import com.dz.entity.Goods;
import com.dz.entity.Label;
import com.dz.entity.Nature;
import com.dz.entity.Order;
import com.dz.entity.OrderType;
import com.dz.entity.Relating;
import com.dz.entity.Reserve;
import com.dz.entity.Shop;
import com.dz.entity.ShopGoods;
import com.dz.entity.Staff;
import com.dz.entity.Track;
import com.dz.entity.User;
import com.dz.entity.Wallet;
import com.dz.service.IAddressService;
import com.dz.service.IBookTimeService;
import com.dz.service.ICateService;
import com.dz.service.ICompanyActivityService;
import com.dz.service.ICompanyService;
import com.dz.service.IDelicacyService;
import com.dz.service.IDistributionService;
import com.dz.service.IEvaluateService;
import com.dz.service.IGoodsService;
import com.dz.service.ILabelService;
import com.dz.service.INatureService;
import com.dz.service.IOrderService;
import com.dz.service.IOrderTypeService;
import com.dz.service.IRelatingService;
import com.dz.service.IReserveService;
import com.dz.service.IShopGoodsService;
import com.dz.service.IStaffService;
import com.dz.service.ITrackService;
import com.dz.service.IUserService;
import com.dz.service.IWalletService;
import com.dz.util.CollectMoneyUtils;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.JSonTree;
import com.dz.util.JSonTreeNode;
import com.dz.util.JiguangConfig;
import com.dz.util.MapUtil;
import com.dz.util.PushJson;
import com.dz.util.PushUtil;
import com.dz.util.StringUtil;
import com.dz.util.TokenUtil;
import com.dz.util.XmlUtils;
import com.dz.util.wxPay;
import com.dz.util.wxRefund;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private IOrderService orderService;

	@Autowired
	private IGoodsService goodsService;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IShopGoodsService shopgoodsService;

	@Autowired
	private IAddressService addressService;

	@Autowired
	private IRelatingService relatingService;

	@Autowired
	private IReserveService reserveService;

	@Autowired
	private IOrderTypeService orderTypeService;

	@Autowired
	private IStaffService staffService;

	@Autowired
	private INatureService natureService;

	@Autowired
	private ICateService cateService;

	@Autowired
	private IDelicacyService delicacyService;

	@Autowired
	private ICompanyActivityService companyActivityService;

	@Autowired
	private IWalletService walletService;

	@Autowired
	private ITrackService trackService;

	@Autowired
	private ILabelService labelService;

	@Autowired
	private IDistributionService distributionService;

	@Autowired
	private IBookTimeService bookTimeService;

	@Autowired
	private IEvaluateService evaluateService;

	private Double temp = 0d;

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Order order, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(order.getId() + "") || order.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		orderService.delete(order.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 外卖提交订单
	@RequestMapping(params = "createOrder", method = RequestMethod.POST)
	public void createOrder(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");
		String cid = request.getParameter("cid");
		String remarks = request.getParameter("remarks");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token) || StringUtil.isEmpty(cid)) {
			message.addProperty("message", "token,cid不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);

		if (user == null) {
			message.addProperty("message", "token验证失败");
			message.addProperty("isout", true);
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Shop shop = user.getShop();

		if (shop == null) {
			message.addProperty("message", "购物车不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<ShopGoods> shopList = shopgoodsService.getshop(Integer.valueOf(cid), shop.getId());
		Company company = companyService.getCompany(Integer.valueOf(cid));

		Order order = new Order();
		order.setCompanyId(company);
		order.setPayStatus("0");
		order.setUserId(user);
		String orderNo = "WM";
		order.setOrderNo(orderNo + company.getId() + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		order.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		order.setCustomId("1");
		order.setOrderType("1");// company.getClassifyId().getId() + "");
		order.setIsAccount("0");
		order.setOrderStatus("unpay");
		order.setRemarks(remarks);
		orderService.saveORupdate(order);
		for (ShopGoods gshop : shopList) {
			Relating relating = new Relating();
			relating.setCompanyId(Integer.valueOf(cid));
			relating.setGoodsId(gshop.getNgoodsId());
			relating.setNumb(gshop.getNumb());
			relating.setOrderId(order);
			relatingService.saveORupdate(relating);
		}

		Track track = new Track();
		track.setOrderId(order);
		track.setStatus("submit");
		track.setBewrite("订单提交成功");
		track.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		trackService.saveORupdate(track);

		message.addProperty("message", "订单添加成功");
		message.addProperty("orderId", order.getId());
		message.addProperty("orderNo", order.getOrderNo());
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);

	}

	// 美食定桌
	@RequestMapping(params = "reserve", method = RequestMethod.POST)
	public void reserve(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");
		String cid = request.getParameter("cid");
		String remarks = request.getParameter("Remarks");
		String tableNo = request.getParameter("tableNo");// 餐桌 ID
		String endTime = request.getParameter("endTime");
		String seat = request.getParameter("seat");
		String meals = request.getParameter("meals");
		String phone = request.getParameter("phone");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token) || StringUtil.isEmpty(cid) || StringUtil.isEmpty(cid)
				|| StringUtil.isEmpty(tableNo) || StringUtil.isEmpty(endTime) || StringUtil.isEmpty(seat)
				|| StringUtil.isEmpty(meals) || StringUtil.isEmpty(phone)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);

		if (user == null) {
			message.addProperty("message", "token验证失败");
			message.addProperty("isout", true);
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = companyService.getCompany(Integer.valueOf(cid));
		if (company == null) {
			message.addProperty("message", "company不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (!NumberUtils.isDigits(phone)) {
			message.addProperty("message", "请输入正确手机号");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (phone.length() != 11) {
			message.addProperty("message", "手机号格式不正确");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Reserve reserve = reserveService.find(Integer.valueOf(tableNo));

		if (reserve != null) {
			Order order = new Order();
			order.setCompanyId(company);
			order.setPayStatus("0");
			order.setUserId(user);
			String orderNo = "MS";
			order.setOrderNo(orderNo + company.getId() + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			order.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			order.setCustomId("1");
			order.setOrderType("2");
			order.setTotal(reserve.getDeposit());
			order.setPay(reserve.getDeposit());
			order.setOrderStatus("unpay");
			order.setRemarks(remarks);
			order.setIsAccount("0");
			orderService.saveORupdate(order);

			Cate cate = new Cate();

			if (reserve != null) {
				cate.setPrice(reserve.getDeposit() + "");
				cate.setName(reserve.getName());
			}
			cate.setUserId(user);
			cate.setCompanyId(company);
			cate.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			cate.setEndTime(endTime);
			cate.setSeat(seat);
			cate.setTableNo(reserve.getTableNo());
			cate.setReserveId(tableNo);
			cate.setOrderId(order);
			cate.setPhone(phone);
			cate.setMeals(meals);

			String recode = TokenUtil.getRandomNum(4);
			cate.setRecode(recode);

			cateService.saveORupdate(cate);

			Track track = new Track();
			track.setOrderId(order);
			track.setStatus("submit");
			track.setBewrite("订单提交成功");
			track.setCreateTime(new SimpleDateFormat("MM-dd HH:mm").format(new Date()));
			trackService.saveORupdate(track);

			shopgoodsService.delete(order.getCompanyId().getId(), order.getUserId().getShop().getId());

			message.addProperty("message", "订单添加成功");
			message.addProperty("orderId", order.getId());
			message.addProperty("orderNo", order.getOrderNo());
			message.addProperty("cateId", cate.getId());
			message.addProperty("success", true);
			new PushJson().outString(message.toJSonString(), response);
		} else {
			message.addProperty("message", "餐桌 ID不正确");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

	}

	// 美食定餐
	@RequestMapping(params = "destine", method = RequestMethod.POST)
	public void destine(HttpServletRequest request, HttpServletResponse response) {

		String token = request.getParameter("token");
		String orderId = request.getParameter("orderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token) || StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);

		if (user == null) {
			message.addProperty("message", "token验证失败");
			message.addProperty("isout", true);
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Shop shop = user.getShop();

		if (shop == null) {
			message.addProperty("message", "购物车不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Order order = orderService.track(orderId);
		if (order == null) {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<ShopGoods> shopList = shopgoodsService.getshop(order.getCompanyId().getId(), shop.getId());

		for (ShopGoods gshop : shopList) {
			List<Relating> relist = relatingService.getGoods(gshop.getNgoodsId(), order.getCompanyId().getId(),
					order.getId());
			if (relist.size() == 0) {
				Relating relating = new Relating();
				relating.setCompanyId(order.getCompanyId().getId());
				relating.setGoodsId(gshop.getNgoodsId());
				relating.setNumb(gshop.getNumb());
				relating.setOrderId(order);
				relatingService.saveORupdate(relating);
			}
		}

		List<Relating> relatingList = relatingService.getrelating(Integer.valueOf(order.getId()),
				Integer.valueOf(order.getCompanyId().getId()));
		DecimalFormat dFormat = new DecimalFormat("######0.00");
		double total = 0;
		for (Relating relating : relatingList) {
			Goods good = goodsService.getGoods(relating.getGoodsId());
			if (good == null) {
				message.addProperty("message", "商品已下架或不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			String price = "0";
			if (StringUtil.isEmpty(good.getSvgPrice())) {
				price = good.getPrice();
			} else {
				price = good.getSvgPrice();
			}
			total = Double.valueOf(dFormat.format(total + relating.getNumb() * Double.parseDouble(price)));
		}

		List<CompanyActivity> list = companyActivityService.getList(Integer.valueOf(order.getCompanyId().getId()), "2");

		double pay = total;
		String discount = "";
		for (CompanyActivity companyActivity : list) {
			if (companyActivity.getActivityId().getId() == 1 && companyActivity.getIsOpen()) {
				if (total >= Double.parseDouble(companyActivity.getBalance())) {
					discount = Double.parseDouble(companyActivity.getBenefit()) + "元";
					pay = Double.valueOf(dFormat.format(total - Double.parseDouble(companyActivity.getBenefit())));
				}
			}

			if (companyActivity.getActivityId().getId() == 2 && companyActivity.getIsOpen()) {
				if (user.getIsNewuser()) {
					discount = Double.parseDouble(companyActivity.getNewUser()) + "元";
					pay = Double.valueOf(dFormat.format(total - Double.parseDouble(companyActivity.getNewUser())));
				}
			}

			if (companyActivity.getActivityId().getId() == 3 && companyActivity.getIsOpen()) {
				discount = Double.parseDouble(companyActivity.getSvg()) + "折";
				pay = Double.valueOf(dFormat.format(total * Double.parseDouble(companyActivity.getSvg()) / 10));
			}

			if (companyActivity.getActivityId().getId() == 4 && companyActivity.getIsOpen()) {
				Wallet wallet = walletService.userwallet(user.getId());
				if (Double.parseDouble(wallet.getBalance()) >= Double.parseDouble(companyActivity.getCoupon())) {
					discount = Double.parseDouble(companyActivity.getCoupon()) + "元";
					pay = Double.valueOf(dFormat.format(total - Double.parseDouble(companyActivity.getCoupon())));
				}
			}
		}

		if (StringUtil.isEmpty(discount)) {
			order.setIsDiscount(false);
		} else {
			order.setIsDiscount(true);
		}

		Cate cate = cateService.getCate(Integer.valueOf(orderId));
		Delicacy delicacy = delicacyService.getDelicacy(order.getCompanyId().getId());

		double mealFee = Integer.valueOf(cate.getMeals()) * Double.valueOf(delicacy.getMealFee());
		cate.setMealFee(mealFee + "");
		cateService.saveORupdate(cate);

		pay = pay + mealFee;
		total = total + mealFee;

		order.setTotal(dFormat.format(total));
		order.setDiscount(discount);
		order.setPay(dFormat.format(pay));
		orderService.saveORupdate(order);

		Track track = new Track();
		track.setOrderId(order);
		track.setStatus("submit");
		track.setBewrite("订单提交成功");
		track.setCreateTime(new SimpleDateFormat("MM-dd HH:mm").format(new Date()));
		trackService.saveORupdate(track);

		message.addProperty("message", "订单添加成功");
		message.addProperty("orderId", order.getId());
		message.addProperty("orderNo", order.getOrderNo());
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 用户订单
	@RequestMapping(params = "nuserView", method = RequestMethod.POST)
	public void nuserView(HttpServletRequest request, HttpServletResponse response) {

		String token = request.getParameter("token");
		String orderId = request.getParameter("orderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token) || StringUtil.isEmpty(orderId) || orderId.equals("null")) {
			message.addProperty("message", "token和orderId为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);
		if (user == null) {
			message.addProperty("message", "用户验证失败");
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

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		grid.addProperties("companyId", order.getCompanyId().getId());
		grid.addProperties("companyName", order.getCompanyId().getName());
		grid.addProperties("companyLogo", order.getCompanyId().getLogo());
		grid.addProperties("orderNo", order.getOrderNo());
		grid.addProperties("companyPhone", order.getCompanyId().getPhone());
		grid.addProperties("addTime", order.getAddTime());

		Cate cate = cateService.getCate(Integer.valueOf(orderId));
		if (cate != null) {
			grid.addProperties("seat", cate.getSeat() == null ? "" : cate.getSeat());
		}

		List<Relating> relatingList = relatingService.getrelating(Integer.valueOf(order.getId()),
				Integer.valueOf(order.getCompanyId().getId()));
		Double boxPrice = 0d;
		int totalCount = relatingList.size();
		DecimalFormat dFormat = new DecimalFormat("######0.00");
		double total = 0;
		for (Relating relating : relatingList) {
			Goods good = goodsService.getGoods(relating.getGoodsId());
			if (good == null) {
				message.addProperty("message", "商品已下架或不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}

			boxPrice = boxPrice + relating.getNumb() * Double.valueOf(good.getBoxPrice());
			String price = "0";
			if (StringUtil.isEmpty(good.getSvgPrice())) {
				price = good.getPrice();
			} else {
				price = good.getSvgPrice();
			}

			total = Double.valueOf(dFormat.format(total + relating.getNumb() * Double.parseDouble(price)));
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("name", good.getName());
			record.addColumn("logo", good.getZoomUrl());
			record.addColumn("price", good.getPrice());
			record.addColumn("number", relating.getNumb());
			record.addColumn("subtotal", relating.getNumb() * Double.parseDouble(good.getPrice()));
			grid.addRecord(record);
		}

		double act = Double.valueOf(order.getTotal()) - Double.valueOf(order.getPay());
		if (!StringUtil.isEmpty(order.getTotal()) && !StringUtil.isEmpty(order.getPay())) {
			grid.addProperties("activity", dFormat.format(act));
		} else {
			grid.addProperties("activity", 0);
		}

		Distribution disteibution = distributionService.getDistribution(order.getCompanyId().getId());

		OrderType type = orderTypeService.getOrderType(Integer.valueOf(orderId));

		if (type != null) {
			if (type.getShippingTime() != null) {
				grid.addProperties("shoptime", type.getShippingTime());
			}

			if (disteibution != null && !type.getType().equals("self")) {
				grid.addProperties("distribution", disteibution.getDistributionPrice());
				grid.addProperties("mode", disteibution.getMode());
			} else {
				grid.addProperties("distribution", 0);
			}
		} else {
			if (disteibution != null) {
				grid.addProperties("mode", disteibution.getMode());
				grid.addProperties("distribution", disteibution.getDistributionPrice());
			} else {
				grid.addProperties("distribution", 0);
			}
		}

		grid.addProperties("remarks", order.getRemarks());
		grid.addProperties("boxprice", dFormat.format(boxPrice));
		grid.addProperties("sumPrice", order.getTotal());
		grid.addProperties("total", order.getTotal() == null ? "0" : order.getTotal());
		grid.addProperties("pay", order.getPay() == null ? "0" : order.getPay());
		grid.addProperties("discount", order.getDiscount() == null ? "0" : order.getDiscount());
		grid.addProperties("totalCount", totalCount);
		grid.addProperties("orderId", order.getId());

		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 用户订单
	@RequestMapping(params = "userView", method = RequestMethod.POST)
	public void userView(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");
		String orderId = request.getParameter("orderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token) || StringUtil.isEmpty(orderId) || orderId.equals("null")) {
			message.addProperty("message", "token和orderId为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);
		if (user == null) {
			message.addProperty("message", "用户验证失败");
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

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		grid.addProperties("companyName", order.getCompanyId().getName());
		grid.addProperties("companyLogo", order.getCompanyId().getLogo());
		grid.addProperties("orderNo", order.getOrderNo());
		grid.addProperties("companyPhone", order.getCompanyId().getPhone());
		grid.addProperties("addTime", order.getAddTime());
		grid.addProperties("remarks", order.getRemarks() == null ? "" : order.getRemarks());
		List<Relating> relatingList = relatingService.getrelating(Integer.valueOf(order.getId()),
				Integer.valueOf(order.getCompanyId().getId()));
		double boxPrice = 0;
		int totalCount = relatingList.size();
		DecimalFormat dFormat = new DecimalFormat("######0.00");
		double total = 0;
		for (Relating relating : relatingList) {
			Goods good = goodsService.getGoods(relating.getGoodsId());
			if (good == null) {
				message.addProperty("message", "商品已下架或不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}

			boxPrice = boxPrice + relating.getNumb() * Double.valueOf(good.getBoxPrice());
			String price = "0";
			if (StringUtil.isEmpty(good.getSvgPrice())) {
				price = good.getPrice();
			} else {
				price = good.getSvgPrice();
			}
			total = Double.valueOf(dFormat.format(total + relating.getNumb() * Double.parseDouble(price)));
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("name", good.getName());
			record.addColumn("logo", good.getZoomUrl());
			record.addColumn("price", good.getPrice());
			record.addColumn("svg_price", good.getSvgPrice());
			record.addColumn("number", relating.getNumb());
			record.addColumn("subtotal", relating.getNumb() * Double.parseDouble(good.getPrice()));
			grid.addRecord(record);
		}

		double pay = total;
		String discount = "";

		if (order.getOrderType().equals("1")) {
			OrderType orderType = orderTypeService.getOrderType(Integer.valueOf(orderId));
			Distribution disteibution = distributionService.getDistribution(order.getCompanyId().getId());
			if (orderType != null) {
				if (!StringUtil.isEmpty(orderType.getPrice())) {
					pay = Double.valueOf(dFormat
							.format(pay + Double.parseDouble(orderType.getPrice() == null ? "0" : orderType.getPrice())
									+ boxPrice));
					total = Double.valueOf(dFormat.format(
							total + Double.parseDouble(orderType.getPrice() == null ? "0" : orderType.getPrice())
									+ boxPrice));
					grid.addProperties("shipTime",
							orderType.getShippingTime() == null ? "" : orderType.getShippingTime());
					grid.addProperties("boxPrice", dFormat.format(boxPrice));//2018-10-24 @Tyy
				}
				if (!StringUtil.isEmpty(orderType.getWay())) {
					grid.addProperties("way", orderType.getWay());
				} else {
					grid.addProperties("way", "商家配送");
				}
			}

			if (disteibution != null) {
				pay = Double.valueOf(dFormat.format(pay
						+ Double.parseDouble(
								disteibution.getDistributionPrice() == null ? "0" : disteibution.getDistributionPrice())
						+ boxPrice));
				total = Double.valueOf(dFormat.format(total
						+ Double.parseDouble(
								disteibution.getDistributionPrice() == null ? "0" : disteibution.getDistributionPrice())
						+ boxPrice));
				grid.addProperties("distributionPrice",
						disteibution.getDistributionPrice() == null ? "0" : disteibution.getDistributionPrice());
				grid.addProperties("boxPrice",dFormat.format(boxPrice));//2018-10-24 @Tyy
			}
		}

		order.setTotal(String.valueOf(total));
		order.setDiscount(discount);
		order.setPay(String.valueOf(pay));
		orderService.saveORupdate(order);
		grid.addProperties("total", order.getTotal() == null ? "0" : order.getTotal());
		grid.addProperties("pay", order.getPay() == null ? "0" : order.getPay());
		grid.addProperties("discount", order.getDiscount() == null ? "0" : order.getDiscount());
		grid.addProperties("totalCount", totalCount);
		grid.addProperties("orderId", order.getId());

		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 获取美食优惠
	@RequestMapping(params = "getCompanyActivity", method = RequestMethod.POST)
	public void getCompanyActivity(HttpServletRequest request, HttpServletResponse response) {

		String token = request.getParameter("token");
		String cid = request.getParameter("cid");
		String orderId = request.getParameter("orderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token) || StringUtil.isEmpty(cid) || StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "token和orderId为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);
		if (user == null) {
			message.addProperty("message", "用户验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Order order = orderService.getOrder(Integer.valueOf(orderId));
		if (order == null) {
			message.addProperty("message", "该订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		grid.addProperties("companyName", order.getCompanyId().getName());

		int count = 0;
		temp = 0d;
		JSonGridRecord trecord = new JSonGridRecord();

		double total = Double.valueOf(order.getTotal());

		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		List<CompanyActivity> list = companyActivityService.getTime(Integer.valueOf(cid), date, "2");
		if (list.size() > 0) {
			CompanyActivity company = list.get(0);
			if (company.getIsOpen()) {// 新人立减
				if (!StringUtil.isEmpty(company.getNewUser())) {// 新加判断
					if (total > Double.valueOf(company.getNewUser())) {
						JSonGridRecord record = new JSonGridRecord();
						record.addColumn("name", "新人立减: " + company.getNewUser() + " 元");
						record.addColumn("id", company.getId());
						record.addColumn("aid", company.getActivityId().getId());
						record.addColumn("numb", company.getNewUser());
						grid.addRecord(record);
						count++;
					}
				}
			}
		}
		// 除了新人立减其他的都查询出来
		for (CompanyActivity coma : list) {

			if (coma.getIsOpen()) {
				switch (coma.getActivityId().getId()) {
				case 1:// 满减活动

					double acbalance = Double.valueOf(coma.getBalance());

					if (total > acbalance && acbalance >= temp) {
						temp = acbalance;
						trecord.addColumn("name", "满" + coma.getBalance() + "元 立减: " + coma.getBenefit() + " 元");
						trecord.addColumn("id", coma.getId());
						trecord.addColumn("aid", coma.getActivityId().getId());
						trecord.addColumn("numb", coma.getBenefit());
						count++;
					}

					break;
				case 3:// 折扣

					JSonGridRecord zrecord = new JSonGridRecord();
					zrecord.addColumn("name", "优惠: " + coma.getSvg() + " 折");
					zrecord.addColumn("id", coma.getId());
					zrecord.addColumn("aid", coma.getActivityId().getId());
					zrecord.addColumn("numb", coma.getSvg());
					grid.addRecord(zrecord);
					count++;
					break;
				case 4:// 优惠券

					double coup = Double.valueOf(coma.getCoupon());

					if (coup < total) {
						JSonGridRecord drecord = new JSonGridRecord();
						drecord.addColumn("name", "立即使用代金券: " + coma.getCoupon() + " 元");
						drecord.addColumn("id", coma.getId());
						drecord.addColumn("aid", coma.getActivityId().getId());
						drecord.addColumn("numb", coma.getCoupon());
						grid.addRecord(drecord);
						count++;
					}

					break;
				}
			}
		}

		grid.addRecord(trecord);
		grid.addProperties("totalCount", count);
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 获取外卖优惠
	@RequestMapping(params = "getCompanyActivityW", method = RequestMethod.POST)
	public void getCompanyActivityW(HttpServletRequest request, HttpServletResponse response) {

		String token = request.getParameter("token");
		String cid = request.getParameter("cid");
		String orderId = request.getParameter("orderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token) || StringUtil.isEmpty(cid) || StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "token和orderId为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);
		if (user == null) {
			message.addProperty("message", "用户验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Order order = orderService.getOrder(Integer.valueOf(orderId));
		if (order == null) {
			message.addProperty("message", "该订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Distribution disteibution = distributionService.getDistribution(order.getCompanyId().getId());
		if (disteibution == null) {
			message.addProperty("message", "该外卖订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		grid.addProperties("companyName", order.getCompanyId().getName());

		int count = 0;
		temp = 0d;
		JSonGridRecord trecord = new JSonGridRecord();

		double total = Double.valueOf(order.getTotal()) - Double.valueOf(disteibution.getDistributionPrice());

		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		List<CompanyActivity> list = companyActivityService.getTime(Integer.valueOf(cid), date, "1");
		if (list.size() > 0) {
			CompanyActivity company = list.get(0);
			if (company.getIsOpen()) {// 新人立减
				String newtemp = StringUtil.isEmpty(company.getNewUser()) ? "0" : company.getNewUser();
				if (total > Double.valueOf(newtemp) && Double.valueOf(newtemp) > 0) {
					JSonGridRecord record = new JSonGridRecord();
					record.addColumn("name", "新人立减: " + company.getNewUser() + " 元");
					record.addColumn("id", company.getId());
					record.addColumn("aid", company.getActivityId().getId());
					record.addColumn("numb", company.getNewUser());
					grid.addRecord(record);
					count++;
				}
			}
		}

		// 除了新人立减其他的都查询出来
		for (CompanyActivity coma : list) {

			if (coma.getIsOpen()) {
				switch (coma.getActivityId().getId()) {
				case 1:// 满减活动

					double acbalance = Double.valueOf(coma.getBalance());

					if (total > acbalance && acbalance >= temp) {
						temp = acbalance;
						trecord.addColumn("name", "满" + coma.getBalance() + "元 立减: " + coma.getBenefit() + " 元");
						trecord.addColumn("id", coma.getId());
						trecord.addColumn("aid", coma.getActivityId().getId());
						trecord.addColumn("numb", coma.getBenefit());
						count++;
					}

					break;
				case 3:// 折扣

					JSonGridRecord zrecord = new JSonGridRecord();
					zrecord.addColumn("name", "优惠: " + coma.getSvg() + " 折");
					zrecord.addColumn("id", coma.getId());
					zrecord.addColumn("aid", coma.getActivityId().getId());
					zrecord.addColumn("numb", coma.getSvg());
					grid.addRecord(zrecord);
					count++;
					break;
				case 4:// 优惠券

					double coup = Double.valueOf(coma.getCoupon());

					if (coup < total) {
						JSonGridRecord drecord = new JSonGridRecord();
						drecord.addColumn("name", "立即使用代金券: " + coma.getCoupon() + " 元");
						drecord.addColumn("id", coma.getId());
						drecord.addColumn("aid", coma.getActivityId().getId());
						drecord.addColumn("numb", coma.getCoupon());
						grid.addRecord(drecord);
						count++;
					}

					break;
				}
			}
		}

		grid.addRecord(trecord);
		grid.addProperties("totalCount", count);
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 用户美食订单
	@RequestMapping(params = "nuserViewMS", method = RequestMethod.POST)
	public void nuserViewMS(HttpServletRequest request, HttpServletResponse response) {

		String token = request.getParameter("token");
		String orderId = request.getParameter("orderId");

		Double total = 0d;
		Double pay = 0D;

		DecimalFormat dFormat = new DecimalFormat("######0.00");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token) || StringUtil.isEmpty(orderId) || orderId.equals("null")) {
			message.addProperty("message", "token和orderId为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);
		if (user == null) {
			message.addProperty("message", "用户验证失败");
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

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		grid.addProperties("companyName", order.getCompanyId().getName());
		grid.addProperties("companyLogo", order.getCompanyId().getLogo());
		grid.addProperties("orderNo", order.getOrderNo());
		grid.addProperties("companyPhone", order.getCompanyId().getPhone());
		grid.addProperties("addTime", order.getAddTime());
		grid.addProperties("remarks", order.getRemarks() == null ? "" : order.getRemarks());

		List<Relating> relatingList = relatingService.getrelating(Integer.valueOf(orderId),
				order.getCompanyId().getId());

		for (Relating relating : relatingList) {
			Goods good = goodsService.getGoods(relating.getGoodsId());
			if (good == null) {
				message.addProperty("message", "商品已下架或不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}

			String price = "0";
			if (StringUtil.isEmpty(good.getSvgPrice())) {
				price = good.getPrice();
			} else {
				price = good.getSvgPrice();
			}

			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("name", good.getName());
			record.addColumn("logo", good.getZoomUrl());
			record.addColumn("price", price);
			record.addColumn("number", relating.getNumb());

			Double real = relating.getNumb() * Double.parseDouble(price);

			pay = pay + real;
			total = total + real;

			record.addColumn("subtotal", dFormat.format(real));
			grid.addRecord(record);
		}

		// 获取桌子信息
		Cate cate = cateService.getCate(Integer.valueOf(orderId));

		if (cate != null) {

			// 获取商家属性获取餐位费
			Delicacy delicacy = delicacyService.getDelicacy(order.getCompanyId().getId());

			Reserve reserve = reserveService.find(Integer.valueOf(cate.getReserveId()));

			if (reserve == null) {
				message.addProperty("message", "餐桌信息不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}

			if (reserve.getCancelTime() != null) {
				grid.addProperties("shipTime", reserve.getCancelTime());
			} else {
				grid.addProperties("shipTime", "");
			}

			grid.addProperties("seat", reserve.getSeat());
			if (reserve.getSeat().equals("bx")) {
				grid.addProperties("reserveName", cate.getName());
			}

			grid.addProperties("phone", cate.getPhone());
			grid.addProperties("createTime", cate.getEndTime());
			grid.addProperties("tableNo", reserve.getTableNo());
			grid.addProperties("meals", cate.getMeals());
			grid.addProperties("remarks", order.getRemarks());

			if (relatingList.size() == 0) {
				if (!StringUtil.isEmpty(cate.getPrice())) {
					pay = pay + Double.parseDouble(cate.getPrice());
					total = total + Double.parseDouble(cate.getPrice());
				}
			} else {
				if (delicacy != null) {

					pay = pay + Double.parseDouble(cate.getMeals()) * Double.parseDouble(delicacy.getMealFee());
					total = total + Double.parseDouble(cate.getMeals()) * Double.parseDouble(delicacy.getMealFee());
					// 2018-10-15 @Tyy start
					grid.addProperties("mealFee",
							dFormat.format(Integer.valueOf(cate.getMeals()) * Double.valueOf(delicacy.getMealFee())));
					if (!StringUtil.isEmpty(delicacy.getMealFee())) {
						grid.addProperties("fee", delicacy.getMealFee());
					}
					// end
				}
			}

			order.setTotal(dFormat.format(total));
			order.setPay(dFormat.format(pay));
			orderService.saveORupdate(order);

			grid.addProperties("total", dFormat.format(total));
			grid.addProperties("pay", dFormat.format(pay));
			grid.addProperties("totalCount", 0);
			grid.addProperties("orderId", orderId);
		} else {
			message.addProperty("message", "暂未找到该桌子");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 用户美食订单
	@RequestMapping(params = "userViewMS", method = RequestMethod.POST)
	public void userViewMS(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");
		String orderId = request.getParameter("orderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token) || StringUtil.isEmpty(orderId) || orderId.equals("null")) {
			message.addProperty("message", "token和orderId为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);
		if (user == null) {
			message.addProperty("message", "用户验证失败");
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

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		grid.addProperties("companyName", order.getCompanyId().getName());
		grid.addProperties("companyLogo", order.getCompanyId().getLogo());
		grid.addProperties("orderNo", order.getOrderNo());
		grid.addProperties("companyPhone", order.getCompanyId().getPhone());
		grid.addProperties("addTime", order.getAddTime());
		grid.addProperties("remarks", order.getRemarks() == null ? "" : order.getRemarks());
		List<Relating> relatingList = relatingService.getrelating(Integer.valueOf(order.getId()),
				Integer.valueOf(order.getCompanyId().getId()));
		int boxPrice = 0;
		int totalCount = relatingList.size();
		DecimalFormat dFormat = new DecimalFormat("######0.00");
		double total = 0;
		for (Relating relating : relatingList) {
			Goods good = goodsService.getGoods(relating.getGoodsId());
			if (good == null) {
				message.addProperty("message", "商品已下架或不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}

			boxPrice = boxPrice + relating.getNumb() * Integer.valueOf(good.getBoxPrice());
			String price = "0";
			if (StringUtil.isEmpty(good.getSvgPrice())) {
				price = good.getPrice();
			} else {
				price = good.getSvgPrice();
			}
			total = Double.valueOf(dFormat.format(total + relating.getNumb() * Double.parseDouble(price)));
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("name", good.getName());
			record.addColumn("logo", good.getZoomUrl());
			if (StringUtil.isEmpty(good.getSvgPrice())) {
				record.addColumn("price", good.getPrice());
			} else {
				record.addColumn("price", good.getSvgPrice());
			}
			record.addColumn("number", relating.getNumb());
			record.addColumn("subtotal", relating.getNumb() * Double.parseDouble(good.getPrice()));
			String natureIds = relating.getNatureId();

			if (natureIds != null) {
				String[] natureid = natureIds.split(",");
				StringBuilder natureinfo = new StringBuilder();
				for (String natures : natureid) {
					if (!natures.equals("0")) {
						Nature nature = natureService.nature(Integer.valueOf(natures.toString()));
						if (nature == null) {
							message.addProperty("message", "商品属性已修改或不存在");
							message.addProperty("success", false);
							new PushJson().outString(message.toJSonString(), response);
							return;
						}
						natureinfo.append(nature.getAttributeId().getName() + ":" + nature.getContent() + ";");
					}
				}
				record.addColumn("natureinfo", natureinfo.toString());
				grid.addRecord(record);
			} else {
				grid.addRecord(record);
				continue;
			}
		}

		List<CompanyActivity> list = companyActivityService.getList(Integer.valueOf(order.getCompanyId().getId()), "2");

		double pay = total;
		String discount = "";
		for (CompanyActivity companyActivity : list) {
			if (companyActivity.getActivityId().getId() == 1) {
				if (total >= Double.parseDouble(companyActivity.getBalance())) {
					grid.addProperties("activityName", companyActivity.getActivityId().getName());
					grid.addProperties("activityId", companyActivity.getActivityId().getId());
					grid.addProperties("type", "减");
					discount = Double.parseDouble(companyActivity.getBenefit()) + "元";
					pay = Double.valueOf(dFormat.format(total - Double.parseDouble(companyActivity.getBenefit())));
				}
			}

			if (companyActivity.getActivityId().getId() == 2) {
				if (user.getIsNewuser()) {
					grid.addProperties("activityName", companyActivity.getActivityId().getName());
					grid.addProperties("activityId", companyActivity.getActivityId().getId());
					grid.addProperties("type", "新");
					discount = Double.parseDouble(companyActivity.getNewUser()) + "元";
					pay = Double.valueOf(dFormat.format(total - Double.parseDouble(companyActivity.getNewUser())));
				}
			}

			if (companyActivity.getActivityId().getId() == 3) {
				grid.addProperties("activityName", companyActivity.getActivityId().getName());
				grid.addProperties("activityId", companyActivity.getActivityId().getId());
				grid.addProperties("type", "折");
				discount = Double.parseDouble(companyActivity.getSvg()) + "折";
				pay = Double.valueOf(dFormat.format(total * Double.parseDouble(companyActivity.getSvg()) / 10));
			}

			if (companyActivity.getActivityId().getId() == 4) {
				Wallet wallet = walletService.userwallet(user.getId());
				if (wallet != null) {
					if (Double.parseDouble(wallet.getRedBalance()) >= Double.parseDouble(companyActivity.getCoupon())) {
						grid.addProperties("activityName", companyActivity.getActivityId().getName());
						grid.addProperties("activityId", companyActivity.getActivityId().getId());
						grid.addProperties("type", "券");
						discount = Double.parseDouble(companyActivity.getCoupon()) + "元";
						pay = Double.valueOf(dFormat.format(total - Double.parseDouble(companyActivity.getCoupon())));
					}
				}
			}
		}

		if (order.getOrderType().equals("2")) {
			Cate cate = cateService.getCate(Integer.valueOf(orderId));
			Delicacy delicacy = delicacyService.getDelicacy(order.getCompanyId().getId());
			if (cate != null) {
				if (relatingList.size() == 0) {
					if (!StringUtil.isEmpty(cate.getPrice())) {
						pay = Double.valueOf(dFormat.format(pay + Double.parseDouble(cate.getPrice())));
						total = Double.valueOf(dFormat.format(total + Double.parseDouble(cate.getPrice())));
					}
				} else {
					if (delicacy != null) {
						pay = Double.valueOf(dFormat.format(pay
								+ Double.parseDouble(cate.getMealFee()) * Double.parseDouble(delicacy.getMealFee())));
						total = Double.valueOf(dFormat.format(total
								+ Double.parseDouble(cate.getMealFee()) * Double.parseDouble(delicacy.getMealFee())));
						// 2018-10-15 @Tyy start
						grid.addProperties("mealFee", dFormat
								.format(Integer.valueOf(cate.getMeals()) * Double.valueOf(delicacy.getMealFee())));
						if (!StringUtil.isEmpty(delicacy.getMealFee())) {
							grid.addProperties("fee", delicacy.getMealFee());
						}
						// end
					}
				}

				/*
				 * Reserve reserve = reserveService.getTable(cate.getTableNo(),
				 * cate.getCompanyId().getId(), cate.getSeat(), cate
				 * .getName());
				 */

				Reserve reserve = reserveService.find(Integer.valueOf(cate.getReserveId()));

				if (reserve == null) {
					message.addProperty("message", "餐桌信息不存在");
					message.addProperty("success", false);
					new PushJson().outString(message.toJSonString(), response);
					return;
				}
				grid.addProperties("shipTime", reserve.getCancelTime());
				grid.addProperties("seat", reserve.getSeat());
				if (reserve.getSeat().equals("bx")) {
					grid.addProperties("reserveName", cate.getName());
				}
				grid.addProperties("phone", cate.getPhone());
				grid.addProperties("createTime", cate.getEndTime());
				grid.addProperties("tableNo", reserve.getTableNo());
				grid.addProperties("meals", cate.getMeals());
				grid.addProperties("remarks", order.getRemarks());

			}
		}

		if (StringUtil.isEmpty(discount)) {
			order.setIsDiscount(false);
		} else {
			order.setIsDiscount(true);
		}
		order.setTotal(String.valueOf(total));
		order.setDiscount(discount);
		order.setPay(String.valueOf(pay));
		orderService.saveORupdate(order);
		grid.addProperties("total", order.getTotal() == null ? "0" : order.getTotal());
		grid.addProperties("pay", order.getPay() == null ? "0" : order.getPay());
		grid.addProperties("discount", order.getDiscount() == null ? "0" : order.getDiscount());
		grid.addProperties("totalCount", totalCount);
		grid.addProperties("orderId", order.getId());

		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 订单支付
	@RequestMapping(params = "pay", method = RequestMethod.POST)
	public void pay(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");
		String orderId = request.getParameter("orderId");
		String remarks = request.getParameter("remarks");
		String addressId = request.getParameter("addressId");
		String shoppingTime = request.getParameter("shoppingTime");
		String quantity = request.getParameter("quantity");
		String activit = request.getParameter("activit");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token) || StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "token和companyId为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);
		if (user == null) {
			message.addProperty("message", "用户验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Order order = orderService.getOrder(Integer.valueOf(orderId));
		if (order != null) {
			if (!StringUtil.isEmpty(activit)) {
				DecimalFormat dFormat = new DecimalFormat("######0.00");

				CompanyActivity company = companyActivityService.getCompanyActivity(Integer.valueOf(activit));

				double total = Double.valueOf(order.getTotal());

				switch (company.getActivityId().getId()) {
				case 1:
					total = total - Double.valueOf(company.getBenefit());
					break;
				case 2:
					total = total - Double.valueOf(company.getNewUser());
					break;
				case 3:
					total = (total * Double.valueOf(company.getSvg())) / 10;
					break;
				case 4:
					total = total - Double.valueOf(company.getCoupon());
					break;
				}

				order.setPay(dFormat.format(total) + "");
			}
		} else {

			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		order.setRemarks(remarks);
		orderService.saveORupdate(order);
		List<Relating> relatingList = relatingService.getrelating(Integer.valueOf(order.getId()),
				Integer.valueOf(order.getCompanyId().getId()));
		Double boxPrice = 0d;
		for (Relating relating : relatingList) {
			Goods good = goodsService.getGoods(relating.getGoodsId());
			if (good == null) {
				message.addProperty("message", "商品已下架或不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			boxPrice = boxPrice + relating.getNumb() * Double.valueOf(good.getBoxPrice());
		}
		if (order.getOrderType().equals("1")) {
			if (quantity != null)
				quantity = quantity.substring(0, 1);

			Address address = null;
			if (!addressId.equals("self")) {
				address = addressService.find(Integer.valueOf(addressId));
				if (address == null) {
					message.addProperty("message", "地址不存在");
					message.addProperty("success", false);
					new PushJson().outString(message.toJSonString(), response);
					return;
				}

				String location = address.getLocation();
				Company company = order.getCompanyId();

				int distance = MapUtil.getdistance(location, company.getCoordinates());
				if (distance >= 3000000) {
					message.addProperty("message", "已超出配送范围.配送范围3公里内");
					message.addProperty("success", false);
					new PushJson().outString(message.toJSonString(), response);
					return;
				}
			}

			OrderType orderType = orderTypeService.getOrderType(order.getId());
			Distribution distribution = distributionService.getDistribution(order.getCompanyId().getId());
			if (orderType != null) {
				if (!addressId.equals("self")) {
					orderType.setAddressId(address);
					orderType.setType("company");
					orderType.setWay("商家配送");

					if (distribution != null) {
						orderType.setPrice(distribution.getDistributionPrice());
					} else {
						orderType.setPrice("0");
					}
				} else {
					String newpay = order.getPay();
					String total = order.getTotal();

					double temppay = Double.valueOf(newpay) - Double.valueOf(distribution.getDistributionPrice());
					double temptotal = Double.valueOf(total) - Double.valueOf(distribution.getDistributionPrice());

					DecimalFormat df = new DecimalFormat("######0.00");
					order.setPay(df.format(temppay));
					order.setTotal(df.format(temptotal));
					orderService.saveORupdate(order);
					orderType.setPrice("0");
					orderType.setType("self");
					orderType.setWay("到店自取");
				}
				orderType.setBoxPrice(boxPrice);
				orderType.setOrderId(order);
				orderType.setQuantity(Integer.valueOf(quantity));
				orderType.setShippingTime(shoppingTime);
				orderType.setStatus("unpay");
				orderType.setIsReminder(false);
				orderTypeService.saveORupdate(orderType);
			} else {
				OrderType newoType = new OrderType();
				if (!addressId.equals("self")) {
					newoType.setAddressId(address);
					newoType.setType("company");
					if (distribution != null) {
						newoType.setPrice(distribution.getDistributionPrice());
					} else {
						newoType.setPrice("0");
					}
				} else {
					newoType.setType("self");

					String newpay = order.getPay();
					String total = order.getTotal();

					double temppay = Double.valueOf(newpay) - Double.valueOf(distribution.getDistributionPrice());
					double temptotal = Double.valueOf(total) - Double.valueOf(distribution.getDistributionPrice());

					DecimalFormat df = new DecimalFormat("######0.00");
					order.setPay(df.format(temppay));
					order.setTotal(df.format(temptotal));
					orderService.saveORupdate(order);
				}

				newoType.setBoxPrice(boxPrice);
				newoType.setOrderId(order);
				newoType.setQuantity(Integer.valueOf(quantity));
				newoType.setShippingTime(shoppingTime);
				newoType.setStatus("unpay");
				newoType.setIsReminder(false);
				orderTypeService.saveORupdate(newoType);
			}
		}
		message.addProperty("success", true);
		message.addProperty("total", order.getTotal());
		message.addProperty("pay", order.getPay());
		message.addProperty("orderId", order.getId());
		message.addProperty("orderNo", order.getOrderNo());
		message.addProperty("discount", order.getDiscount());
		new PushJson().outString(message.toJSonString(), response);
	}

	// 用户所有订单
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response) {

		String token = request.getParameter("token");
		String start = request.getParameter("start");// 起始页
		String limit = request.getParameter("limit");// 容量

		String status = request.getParameter("status");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(start) || StringUtil.isEmpty(limit) || StringUtil.isEmpty(token)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);
		if (user == null) {
			message.addProperty("message", "用户验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		List<Order> orderList = new ArrayList<Order>();
		if (!StringUtil.isEmpty(status)) {
			if (status.equals("isSend")) {
				orderList = orderService.getIsSend(user.getId(), Integer.valueOf(start), Integer.valueOf(limit));
			} else {
				orderList = orderService.getAll(user.getId(), status, Integer.valueOf(start), Integer.valueOf(limit));
			}
		} else {
			orderList = orderService.getDoing(user.getId(), Integer.valueOf(start), Integer.valueOf(limit));
		}

		JSonTree json = new JSonTree();
		for (Order orders : orderList) {
			if (orders.getOrderType().equals("1") || orders.getOrderType().equals("2")) {
				JSonTreeNode node = new JSonTreeNode();
				String newStatus = "";
				if (orders.getOrderStatus().equals("unreceiption")) {
					newStatus = "4";
				} else if (orders.getOrderStatus().equals("finish")) {
					newStatus = "1";
				} else if (orders.getOrderStatus().equals("backBalance")) {
					newStatus = "3";
				} else if (orders.getOrderStatus().equals("unusual")) {
					newStatus = "2";
				} else if (orders.getOrderStatus().equals("doing")) {
					newStatus = "0";
				} else if (orders.getOrderStatus().equals("paysuccess")) {
					newStatus = "6";
				}

				node.addProperty("addTime", orders.getAddTime());

				if (orders.getOrderType().equals("2")) {
					Cate cate = cateService.getCate(orders.getId());
					if (cate != null) {
						String seat = cate.getSeat();
						if (seat.equals("dt")) {
							seat = "大厅";
						} else {
							seat = "包间";
						}
						node.addProperty("recode", cate.getRecode() == null ? "" : cate.getRecode());
						node.addProperty("tableNo", seat + " :" + cate.getTableNo() + "号桌");
						node.addProperty("addTime", cate.getEndTime());
					} else {
						node.addProperty("recode", "");
						node.addProperty("tableNo", "");
						node.addProperty("addTime", orders.getAddTime());
					}
				}

				node.addProperty("cid", orders.getCompanyId().getId());
				node.addProperty("companyName", orders.getCompanyId().getName());
				node.addProperty("logo", orders.getCompanyId().getLogo());
				node.addProperty("orderNo", orders.getOrderNo());
				node.addProperty("remarks", orders.getRemarks());
				node.addProperty("id", orders.getId());

				List<Evaluate> elist = evaluateService.getevaluate(orders.getId());

				if (elist.size() > 0) {
					node.addProperty("isSend", "1");
				} else {
					node.addProperty("isSend", "0");
				}

				node.addProperty("type", orders.getOrderType());
				node.addProperty("pay", orders.getPay());
				if (orders.getOrderType().equals("1")) {
					OrderType orderType = orderTypeService.getOrderType(orders.getId());
					if (orderType != null) {
						if (orderType.getStatus().equals("paysuccess") || orderType.getStatus().equals("unusual")
								|| orderType.getStatus().equals("unpay") || orderType.getStatus().equals("untaking")) {
							node.addProperty("takeStatus", true);
						} else {
							node.addProperty("takeStatus", false);
						}
						orderType.getStatus();
						node.addProperty("code", orderType.getCode());
					}
				}

				node.addProperty("status", newStatus);
				List<Relating> relatingList = relatingService.getrelating(Integer.valueOf(orders.getId()),
						Integer.valueOf(orders.getCompanyId().getId()));
				node.addProperty("count", relatingList.size());
				if (relatingList.size() > 0) {
					for (Relating relating : relatingList) {
						Goods good = goodsService.getGoods(Integer.valueOf(relating.getGoodsId()));
						if (good != null) {
							JSonTreeNode node1 = new JSonTreeNode();
							node1.addProperty("name", good.getName());
							node1.addProperty("zoomUrl", good.getZoomUrl());
							node1.addProperty("price", good.getPrice());
							node1.addProperty("much", relating.getNumb());
							node.addProperty("leaf", true);
							node.addChild(node1);
						}
					}
				}
				json.addNode(node);
			}
		}

		new PushJson().outString(json.toJSonString(), response);

	}

	// 再来一单
	@RequestMapping(params = "recur", method = RequestMethod.POST)
	public void recur(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");
		String orderId = request.getParameter("orderId");
		// String token = request.getParameter("token");
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token) || StringUtil.isEmpty(orderId)) {
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

		Order orders = new Order();

		message.addProperty("success", true);
		message.addProperty("companyName", orders.getCompanyId().getName());
		message.addProperty("logo", orders.getCompanyId().getLogo());
		message.addProperty("orderNo", orders.getOrderNo());
		message.addProperty("addTime", orders.getAddTime());
		message.addProperty("remarks", orders.getRemarks());

		new PushJson().outString(message.toJSonString(), response);
	}

	// 用户申请退款
	@RequestMapping(params = "refund", method = RequestMethod.POST)
	public void refund(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");// 订单id

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "订单id不能为空");
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

		synchronized (this) {
			if (order.getOrderStatus().equals("doing") || order.getOrderStatus().equals("paysuccess")) {//2018-11-01 @Tyy
				if (order.getOrderType().equals("1")) {
					OrderType orderType = orderTypeService.getOrderType(Integer.valueOf(orderId));
					if (orderType == null) {
						message.addProperty("message", "订单信息不存在");
						message.addProperty("success", false);
						new PushJson().outString(message.toJSonString(), response);
						return;
					}

					if (orderType.getStatus().equals("untaking") || orderType.getStatus().equals("paysuccess")) {//2018-11-01 @Tyy
						order.setOrderStatus("backBalance");
						orderService.saveORupdate(order);

						Track track = new Track();
						track.setOrderId(order);
						track.setStatus("backBalance");
						track.setBewrite("客户申请退款");
						track.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						trackService.saveORupdate(track);
						message.addProperty("success", true);
						message.addProperty("message", "申请退款成功");
					} else if (orderType.getType().equals("self")) {

						order.setOrderStatus("backBalance");
						orderService.saveORupdate(order);

						Track track = new Track();
						track.setOrderId(order);
						track.setStatus("backBalance");
						track.setBewrite("客户申请退款");
						track.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						trackService.saveORupdate(track);
						message.addProperty("success", true);
						message.addProperty("message", "申请退款成功");

					} else {
						message.addProperty("success", false);
						message.addProperty("message", "骑手已接单,无法发起退款.");
					}
				} else if (order.getOrderType().equals("2")) {
					order.setOrderStatus("backBalance");
					orderService.saveORupdate(order);
					message.addProperty("success", true);
					message.addProperty("message", "申请退款成功");
				}
			} else {
				if (order.getOrderStatus().equals("unpay")) {
					message.addProperty("message", "您未支付无法退款");
					message.addProperty("success", false);
				} else if (order.getOrderStatus().equals("unreceiption")) {
					message.addProperty("message", "订单已取消");
					message.addProperty("success", false);

				} else if (order.getOrderStatus().equals("finish")) {
					message.addProperty("message", "订单已完成无法退款");
					message.addProperty("success", false);
				}

				new PushJson().outString(message.toJSonString(), response);
				return;
			}
		}

		// 推送订单信息
		List<String> userlist = new ArrayList<String>();
		List<Staff> adminList = staffService.getList(order.getCompanyId().getId(), 1);
		for (Staff admin : adminList) {
			userlist.add(admin.getUserName());
		}
		List<Staff> cashierList = staffService.getList(order.getCompanyId().getId(), 2);
		for (Staff cashier : cashierList) {
			userlist.add(cashier.getUserName());
		}

		PushUtil.sendAlias(order.getOrderNo() + "订单申请退款,请及时处理", userlist, JiguangConfig.companyKey,
				JiguangConfig.companySecret);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 抢单
	@RequestMapping(params = "snatch", method = RequestMethod.POST)
	public void snatch(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");// 配送员token
		String orderId = request.getParameter("orderId");// 订单id
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "订单id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		synchronized (this) {
			Order order = orderService.getOrder(Integer.valueOf(orderId));
			if (order == null) {
				message.addProperty("message", "订单不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}

			User user = userService.gettoken(token);
			if (user == null) {
				message.addProperty("message", "配送员不存在");
				message.addProperty("success", false);
				message.addProperty("isLogin", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			OrderType orderType = orderTypeService.getOrderType(Integer.valueOf(orderId));
			if (orderType == null) {
				message.addProperty("message", "找不到订单信息");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			orderType.setUserId(user);
			orderType.setStatus("takeMeal");
			orderTypeService.saveORupdate(orderType);

			Track track = new Track();
			track.setOrderId(order);
			track.setStatus("taking");
			track.setBewrite("骑手已接单");
			track.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			trackService.saveORupdate(track);
			// 推送订单信息
			List<String> sss = new ArrayList<String>();
			List<Staff> adminList = staffService.getList(order.getCompanyId().getId(), 1);
			for (Staff admin : adminList) {
				sss.add(admin.getUserName());
			}
			List<Staff> cashierList = staffService.getList(order.getCompanyId().getId(), 2);
			for (Staff cashier : cashierList) {
				sss.add(cashier.getUserName());
			}
			new PushUtil();
			PushUtil.sendAlias("骑手已接单", sss, JiguangConfig.companyKey, JiguangConfig.companySecret);
			if (order.getUserId() != null) {
				sss.add(order.getUserId().getUserName());
			}

			PushUtil.sendAlias("骑手已接单", sss, JiguangConfig.userKey, JiguangConfig.userSecret);
		}

		message.addProperty("success", true);
		message.addProperty("message", "抢单成功");

		new PushJson().outString(message.toJSonString(), response);
	}

	// 取餐
	@RequestMapping(params = "distribution", method = RequestMethod.POST)
	public void distribution(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");// 订单id
		String token = request.getParameter("token");// 配送员token
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "订单id不能为空");
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
		User user = userService.gettoken(token);
		OrderType orderType = orderTypeService.getOrderType(Integer.valueOf(orderId));
		orderType.setUserId(user);
		orderType.setStatus("taking");
		orderTypeService.saveORupdate(orderType);

		Track track = new Track();
		track.setOrderId(order);
		track.setStatus("distribution");
		track.setBewrite("骑手已取货,正在路途中");
		track.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		trackService.saveORupdate(track);

		message.addProperty("success", true);
		message.addProperty("message", "取货成功");

		List<String> sss = new ArrayList<String>();
		sss.add(order.getUserId().getUserName());
		new PushUtil();
		PushUtil.sendAlias("骑手已取餐,正在路途中", sss, JiguangConfig.userKey, JiguangConfig.userSecret);

		new PushJson().outString(message.toJSonString(), response);
	}

	// 送达
	@RequestMapping(params = "finish", method = RequestMethod.POST)
	public void finish(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");// 订单id
		String token = request.getParameter("token");// 配送员token

		synchronized (this) {
			JSonMessage message = new JSonMessage();
			if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(token)) {
				message.addProperty("message", "订单id,token不能为空");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			try {
				String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				Order order = orderService.getOrder(Integer.valueOf(orderId));
				if (order != null) {
					if (order.getOrderStatus().equals("doing")) {
						order.setOrderStatus("finish");
						order.setFinishTime(date);
						order.setIsAccount("1");

						Company company = order.getCompanyId();
						String monSale = company.getMonSales();
						if (!StringUtil.isEmpty(monSale)) {
							int mon = Integer.valueOf(monSale);
							company.setMonSales(String.valueOf(mon++));
						} else {
							company.setMonSales("0");
						}

						companyService.saveORupdate(company);

						User user = userService.gettoken(token);
						if (user == null) {
							message.addProperty("success", false);
							message.addProperty("message", "token验证失败");
							message.addProperty("isout", true);
							new PushJson().outString(message.toJSonString(), response);
							return;
						}

						Track track = new Track();
						track.setOrderId(order);
						track.setStatus("finish");
						track.setBewrite("客户已拿到外卖");
						track.setCreateTime(date);
						trackService.saveORupdate(track);

						OrderType orderType = orderTypeService.getOrderType(Integer.valueOf(orderId));
						if (orderType != null) {
							orderType.setUserId(user);
							orderType.setStatus("finish");
//							orderType.setAddressId(null);	//2018-11-02 ·Tyy
							orderType.setShippingTime(date);
							orderTypeService.saveORupdate(orderType);

							String price = orderType.getPrice();
							double total = Double.valueOf(order.getPay()) * 100 - Double.valueOf(price) * 100;
							if (!StringUtil.isEmpty(company.getOpenId())) {
								if (total >= 1000) {
									total = total - (total * 0.006);
								} else {
									total = total - 10;
								}

								String money = (int) total + "";
								String[] result = CollectMoneyUtils.CollectMoney(money, company.getOpenId());
								if (result != null) {
									order.setContent(result[1]);
									if (result[0].equals("true")) {
										order.setIsAccount("1");
									} else {
										order.setIsAccount("0");
									}
								} else {
									order.setContent("发送请求失败");
								}
							} else {
								order.setContent("openId不存在");
							}
							orderService.saveORupdate(order);
						}

						// 推送商家订单已送达
						List<String> sss = new ArrayList<String>();
						List<Staff> adminList = staffService.getList(order.getCompanyId().getId(), 1);
						for (Staff admin : adminList) {
							sss.add(admin.getUserName());
						}
						List<Staff> cashierList = staffService.getList(order.getCompanyId().getId(), 2);
						for (Staff cashier : cashierList) {
							sss.add(cashier.getUserName());
						}

						PushUtil.sendAlias(order.getOrderNo() + "订单已送达", sss, JiguangConfig.companyKey,
								JiguangConfig.companySecret);

						if (order.getUserId() != null) {
							sss.add(order.getUserId().getUserName());
						}
						PushUtil.sendAlias(order.getOrderNo() + "订单已送达", sss, JiguangConfig.userKey,
								JiguangConfig.userSecret);

						message.addProperty("success", true);
						message.addProperty("message", "完成订单");
						new PushJson().outString(message.toJSonString(), response);
					} else {
						message.addProperty("success", false);
						message.addProperty("message", "状态已变更");
						new PushJson().outString(message.toJSonString(), response);
					}
				} else {
					message.addProperty("success", false);
					message.addProperty("message", "订单不存在");
					new PushJson().outString(message.toJSonString(), response);
				}
			} catch (Exception e) {
				e.printStackTrace();
				message.addProperty("success", false);
				message.addProperty("message", "请求超时，请稍候重试.");
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
		}
	}

	// 更改状态
	@RequestMapping(params = "changeStatus", method = RequestMethod.POST)
	public void changeStatus(HttpServletRequest request, HttpServletResponse response) {

		String orderId = request.getParameter("orderId");// 订单id

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "订单id不能为空");
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

		double total = Double.valueOf(order.getPay()) * 100;// -
															// Double.valueOf(price)
															// * 100;
		Company company = order.getCompanyId();
		if (!StringUtil.isEmpty(company.getOpenId())) {
			if (total >= 1000) {
				total = total - (total * 0.006);
			} else {
				total = total - 10;
			}

			String money = (int) total + "";
			String[] result = CollectMoneyUtils.CollectMoney(money, company.getOpenId());
			if (result != null) {
				order.setContent(result[1]);
				if (result[0].equals("true")) {
					order.setIsAccount("1");
				} else {
					order.setIsAccount("0");
				}
			} else {
				order.setContent("发送请求失败");
			}
		} else {
			order.setContent("openId不存在");
		}
		orderService.saveORupdate(order);
		order.setOrderStatus("finish");
		order.setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		orderService.saveORupdate(order);

		OrderType type = orderTypeService.getOrderType(Integer.valueOf(orderId));
		if (type == null) {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		type.setStatus("finish");
		orderTypeService.saveORupdate(type);

		message.addProperty("message", "确认完成");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 可抢订单
	@RequestMapping(params = "snatchView", method = RequestMethod.POST)
	public void snatchView(HttpServletRequest request, HttpServletResponse response) {
		List<OrderType> orderTypes = orderTypeService.getorderTypeList("untaking");
		String location = request.getParameter("location");
		// location = "25.2560978214,110.2188938856";

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(location.trim())) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		int count = 0;
		for (OrderType orderType : orderTypes) {
			Order order = orderType.getOrderId();
			if (order.getOrderStatus().equals("doing")) {
				int distance = MapUtil.getdistance(location, orderType.getOrderId().getCompanyId().getCoordinates());

				String range = "";
				if (distance >= 1000) {
					range = Double.valueOf(distance) / 1000 + "km";
				} else {
					range = distance + "m";
				}
				if (Integer.valueOf(distance) <= 5000000) {// 距离
					if (orderType != null) {
						if (orderType.getAddressId() != null) {
							JSonGridRecord record = new JSonGridRecord();
							record.addColumn("logo", orderType.getOrderId().getCompanyId().getLogo());
							record.addColumn("location", orderType.getOrderId().getCompanyId().getCoordinates());
							record.addColumn("companyName", orderType.getOrderId().getCompanyId().getName());
							record.addColumn("companyPosition", orderType.getOrderId().getCompanyId().getPosition());
							record.addColumn("companyPhone", orderType.getOrderId().getCompanyId().getPhone());
							record.addColumn("userPhone", orderType.getAddressId().getPhone());
							record.addColumn("userName", orderType.getAddressId().getName());
							record.addColumn("cost", orderType.getPrice());
							record.addColumn("userAddress", orderType.getAddressId().getAddress());
							record.addColumn("room", orderType.getAddressId().getRoom());
							record.addColumn("time", orderType.getShippingTime());
							record.addColumn("code", orderType.getCode());
							record.addColumn("addTime", order.getAddTime());
							record.addColumn("distance", range);
							int userDistance = MapUtil.getdistance(location, orderType.getAddressId().getLocation());

							String urange = "";
							if (userDistance >= 1000) {
								urange = Double.valueOf(userDistance) / 1000 + "km";
							} else {
								urange = distance + "m";
							}
							record.addColumn("userDistance", urange);
							record.addColumn("orderId", orderType.getOrderId().getId());
							grid.addRecord(record);
							count++;
						}
					}
				}
			}
		}

		grid.addProperties("totalCount", count);
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 待取货和待确认订单
	@RequestMapping(params = "orderView", method = RequestMethod.POST)
	public void orderView(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");// 配送员token
		String location = request.getParameter("location");
		String status = request.getParameter("status");
		// location = "25.2560978214,110.2188938856";

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(status) || StringUtil.isEmpty(location) || StringUtil.isEmpty(status)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);

		if (user == null) {
			message.addProperty("message", "配送员不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<OrderType> orderTypes = orderTypeService.getorderTypeList(user.getId() + "", status);

		JSonTree tree = new JSonTree();

		for (OrderType orderType : orderTypes) {
			Order order = orderService.getOrder(orderType.getOrderId().getId());
			int distance = MapUtil.getdistance(location, order.getCompanyId().getCoordinates());
			String range = distance + "m";
			if (distance >= 1000) {
				range = Double.valueOf(distance) / 1000 + "km";
			}
			if (Integer.valueOf(distance) <= 10000000) {// 距离
				if (!orderType.getType().equals("self")) {
					JSonTreeNode node = new JSonTreeNode();
					node.addProperty("companyName", order.getCompanyId().getName());
					node.addProperty("logo", order.getCompanyId().getLogo());
					node.addProperty("totalCount", orderTypes.size());
					node.addProperty("companyPosition", order.getCompanyId().getPosition());
					node.addProperty("companyPhone", order.getCompanyId().getPhone());
					node.addProperty("coordinates", orderType.getAddressId().getLocation());
					node.addProperty("companyCoordinates", order.getCompanyId().getCoordinates());
					node.addProperty("time", orderType.getShippingTime());
					node.addProperty("code", orderType.getCode());
					node.addProperty("distance", range);
					node.addProperty("userPhone", orderType.getAddressId().getPhone());
					node.addProperty("orderId", orderType.getOrderId().getId());
					node.addProperty("userName", orderType.getAddressId().getName());
					node.addProperty("userAddress", orderType.getAddressId().getAddress());
					node.addProperty("room", orderType.getAddressId().getRoom());
					node.addProperty("addTime", order.getAddTime());
					List<Relating> relatinglist = relatingService.getrelating(order.getId());
					for (Relating relating : relatinglist) {
						JSonTreeNode child = new JSonTreeNode();

						Goods good = goodsService.getGoods(relating.getGoodsId());
						if (good == null) {
							message.addProperty("message", "商品已下架或不存在");
							message.addProperty("success", false);
							new PushJson().outString(message.toJSonString(), response);
							return;
						}

						child.addProperty("name", good.getName());
						child.addProperty("number", relating.getNumb());
						child.addProperty("leaf", true);
						node.addChild(child);
					}

					tree.addNode(node);
				}
			}
		}

		new PushJson().outString(tree.toJSonString(), response);
	}

	// 订单结果查询
	@RequestMapping(params = "restulView", method = RequestMethod.POST)
	public void restulView(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");
		String orderId = request.getParameter("orderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token) || StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "token和companyId为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);
		if (user == null) {
			message.addProperty("message", "用户验证失败");
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

		Cate cate = cateService.getCate(Integer.valueOf(orderId));
		if (cate == null) {
			message.addProperty("message", "订餐信息获取失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Reserve reserve = reserveService.getTable(cate.getTableNo(), cate.getCompanyId().getId(), cate.getSeat());
		if (reserve == null) {
			message.addProperty("message", "订餐信息获取失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		String status = "";
		message.addProperty("success", true);
		message.addProperty("companyName", order.getCompanyId().getName());
		message.addProperty("companyphone", order.getCompanyId().getPhone());
		message.addProperty("orderNo", order.getOrderNo());
		message.addProperty("pay", reserve.getDeposit());
		if (order.getOrderStatus().equals("paysuccess")) {
			status = "订单预定成功！";
			message.addProperty("meale", cate.getMeals());
			message.addProperty("time", cate.getEndTime());
		} else if (order.getOrderStatus().equals("payfailure")) {
			status = "订单预定失败！";
		} else {
			status = "获取数据失败";
		}
		message.addProperty("status", status);

		new PushJson().outString(message.toJSonString(), response);
	}

	// 支付结果
	@RequestMapping(params = "paySuccess", method = RequestMethod.POST)
	public void paySuccess(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");
		String orderId = request.getParameter("orderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token) || StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "token和companyId为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);
		if (user == null) {
			message.addProperty("message", "用户验证失败");
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

		JSonGrid grid = new JSonGrid();
		if (order.getOrderStatus().equals("paysuccess")) {
			grid.addProperties("success", true);
			grid.addProperties("companyName", order.getCompanyId().getName());
			grid.addProperties("companyphone", order.getCompanyId().getPhone());
			grid.addProperties("orderNo", order.getOrderNo());

			List<Label> labelList = labelService.getLabel("comDefect", order.getCompanyId().getId() + "");
			for (Label labels : labelList) {
				JSonGridRecord record = new JSonGridRecord();
				record.addColumn("id", labels.getId());
				record.addColumn("content", labels.getContent());
				grid.addRecord(record);
			}
			grid.addProperties("totalCount", labelList.size());
		} else {
			message.addProperty("message", "订单尚未支付成功");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 清空购物车
	@RequestMapping(params = "clearCar", method = RequestMethod.POST)
	public void clearCar(HttpServletRequest request, HttpServletResponse response) {

		String token = request.getParameter("token");
		String cid = request.getParameter("cid");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token) || StringUtil.isEmpty(cid)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);
		if (user == null) {
			message.addProperty("message", "token效验失效 .");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Shop shop = user.getShop();
		shopgoodsService.delete(Integer.valueOf(cid), shop.getId());

		message.addProperty("message", "清空成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 恢复配送
	@RequestMapping(params = "rebackRunning", method = RequestMethod.POST)
	public void rebackRunning(HttpServletRequest request, HttpServletResponse response) {

		String orderId = request.getParameter("orderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Order order = orderService.getOrder(Integer.valueOf(orderId));

		order.setOrderStatus("doing");
		orderService.saveORupdate(order);

		OrderType type = orderTypeService.getOrderType(Integer.valueOf(orderId));
		type.setStatus("taking");
		orderTypeService.saveORupdate(type);

		message.addProperty("message", "恢复成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 微信支付统一下单
	@RequestMapping(params = "wxpay", method = RequestMethod.POST)
	public void wxpay(HttpServletRequest request, HttpServletResponse response) {

		String body = request.getParameter("body");
		String attach = request.getParameter("attach");
		String out_trade_no = request.getParameter("out_trade_no");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(body) || StringUtil.isEmpty(attach) || StringUtil.isEmpty(out_trade_no)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Order order = orderService.getOrderNo(out_trade_no);
		if (order == null) {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		shopgoodsService.delete(order.getCompanyId().getId(), order.getUserId().getShop().getId());

		String total_fee = order.getPay();
		if (!StringUtil.isEmpty(total_fee)) {
			double newfee = Double.valueOf(total_fee) * 100;// 有几率精度损失0.01
			total_fee = ((int) newfee) + "";
		} else {
			total_fee = "0";
		}

		try {
			String wxOrder = new wxPay().order(body, attach, out_trade_no, total_fee);

			Map<String, String> ordermap = XmlUtils.orderXmlOut(wxOrder);
			if (!StringUtil.isEmpty(ordermap.get("return_code"))) {
				if (ordermap.get("return_code").equals("SUCCESS")) {
					if (!StringUtil.isEmpty(ordermap.get("result_code"))) {
						if (ordermap.get("result_code").equals("SUCCESS")) {
							message.addProperty("out_trade_no", out_trade_no);
							message.addProperty("nonce_str", ordermap.get("nonce_str"));
							message.addProperty("prepay_id", ordermap.get("prepay_id"));
						} else {
							message.addProperty("err_code_des", ordermap.get("err_code_des"));
							message.addProperty("message", "获取结果失败");
							message.addProperty("success", false);
							new PushJson().outString(message.toJSonString(), response);
							return;
						}
					} else {
						message.addProperty("message", "结果为空");
						message.addProperty("success", false);
						new PushJson().outString(message.toJSonString(), response);
						return;
					}
				} else {
					message.addProperty("return_msg", ordermap.get("return_msg"));
					message.addProperty("message", "获取返回信息失败");
					message.addProperty("success", false);
					new PushJson().outString(message.toJSonString(), response);
					return;
				}
			} else {
				message.addProperty("message", "连接微信支付api失败");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
			message.addProperty("message", "IO流错误");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		} catch (DocumentException e) {
			e.printStackTrace();
			message.addProperty("message", "xml解析错误");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		message.addProperty("message", "获取prepay_id成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 微信支付订单查询
	@RequestMapping(params = "wxSelect", method = RequestMethod.POST)
	public void wxSelect(HttpServletRequest request, HttpServletResponse response) {

		String out_trade_no = request.getParameter("out_trade_no");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(out_trade_no)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Order order = orderService.track(out_trade_no);
		if (order == null) {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		String total_fee = order.getPay();
		double payPrice = Double.valueOf(total_fee) * 100;
		if (Math.round(payPrice) - payPrice == 0) {
			total_fee = String.valueOf((long) payPrice);
		}
		try {
			String wxSelect = new wxPay().select(out_trade_no);
			Map<String, String> selectmap = XmlUtils.selectXmlOut(wxSelect);
			if (!StringUtil.isEmpty(selectmap.get("return_code"))) {
				if (selectmap.get("return_code").equals("SUCCESS")) {
					if (!StringUtil.isEmpty(selectmap.get("result_code"))) {
						if (selectmap.get("result_code").equals("SUCCESS")) {
							message.addProperty("out_trade_no", out_trade_no);
							message.addProperty("total_fee", selectmap.get("total_fee"));
							message.addProperty("trade_state", selectmap.get("trade_state"));
							message.addProperty("transaction_id", selectmap.get("trade_state"));
							message.addProperty("time_end", selectmap.get("time_end"));
							message.addProperty("trade_state_desc", selectmap.get("trade_state_desc"));
						} else {
							message.addProperty("err_code_des", selectmap.get("err_code_des"));
							message.addProperty("message", "获取结果失败");
							message.addProperty("success", false);
							new PushJson().outString(message.toJSonString(), response);
							return;
						}
					} else {
						message.addProperty("message", "结果为空");
						message.addProperty("success", false);
						new PushJson().outString(message.toJSonString(), response);
						return;
					}
				} else {
					message.addProperty("return_msg", selectmap.get("return_msg"));
					message.addProperty("message", "获取返回信息失败");
					message.addProperty("success", false);
					new PushJson().outString(message.toJSonString(), response);
					return;
				}
			} else {
				message.addProperty("message", "连接微信支付api失败");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
			message.addProperty("message", "IO流错误");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		} catch (DocumentException e) {
			e.printStackTrace();
			message.addProperty("message", "xml解析错误");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		message.addProperty("message", "订单查询成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 订单追踪
	@RequestMapping(params = "track", method = RequestMethod.POST)
	public void track(HttpServletRequest request, Order order, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(order.getOrderNo())) {
			message.addProperty("message", "OrderNo不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Order orders = orderService.track(order.getOrderNo());

		message.addProperty("success", true);
		message.addProperty("companyName", orders.getCompanyId().getName());
		message.addProperty("logo", orders.getCompanyId().getLogo());
		message.addProperty("orderNo", orders.getOrderNo());
		message.addProperty("addTime", orders.getAddTime());
		message.addProperty("remarks", orders.getRemarks());

		new PushJson().outString(message.toJSonString(), response);
	}

	// 商家接单
	@RequestMapping(params = "receipt", method = RequestMethod.POST)
	public void receipt(HttpServletRequest request, HttpServletResponse response) {
		String status = request.getParameter("status");// 状态
		String orderId = request.getParameter("orderId");// 订单id

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "订单id不能为空");
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
		Track track = new Track();
		track.setOrderId(order);
		track.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		if (status.equals("companyTaking")) {
			if (!order.getOrderStatus().equals("paysuccess")) {
				message.addProperty("message", "订单状态不能接单,请刷新页面");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			order.setOrderStatus("doing");
			if (order.getOrderType().equals("1")) {
				OrderType ordertype = orderTypeService.getOrderType(order.getId());
				if (ordertype != null) {
					Random random = new Random();
					int code = random.nextInt(9999) % (9999 - 1001) + 1000;
					ordertype.setCode(code + "");
					ordertype.setStatus("untaking");
					ordertype.setReceiptTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					orderTypeService.saveORupdate(ordertype);
				}
				track.setStatus(status);
				track.setBewrite("商家已接单");
			} else if (order.getOrderType().equals("2")) {
				track.setStatus("confirm");
				track.setBewrite("商家已确认");
				track.setCreateTime(new SimpleDateFormat("MM-dd HH:mm").format(new Date()));
				BookTime booktime = new BookTime();
				Cate cate = cateService.getCate(Integer.valueOf(orderId));
				booktime.setOrderId(Integer.valueOf(orderId));
				booktime.setReserveTime(cate.getEndTime());
				booktime.setUserName(order.getUserId().getUserName());
				booktime.setPeople(cate.getMeals());
				booktime.setUserPhone(cate.getPhone());
				booktime.setCompanyId(order.getCompanyId().getId());
				booktime.setCreatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				/*
				 * Reserve reserve = reserveService.getTable(cate.getTableNo(),
				 * cate.getCompanyId().getId(), cate.getSeat());
				 */

				Reserve reserve = reserveService.find(Integer.valueOf(cate.getReserveId()));

				if (reserve == null) {
					message.addProperty("message", "餐桌不存在");
					message.addProperty("success", false);
					new PushJson().outString(message.toJSonString(), response);
					return;
				}
				booktime.setReserveId(reserve);
				bookTimeService.saveORupdate(booktime);
				if (!reserve.getStatus().equals("2")) {
					reserve.setStatus("1");
				}
				reserveService.saveORupdate(reserve);
			}

			List<String> sss = new ArrayList<String>();
			sss.add(order.getUserId().getUserName());
			new PushUtil();
			PushUtil.sendAlias("商家已接单", sss, JiguangConfig.userKey, JiguangConfig.userSecret);
		} else {
			track.setStatus("unconfirm");
			track.setBewrite("商家已取消");
			order.setOrderStatus("unreceiption");
			if (order.getOrderType().equals("1")) {
				OrderType ordertype = orderTypeService.getOrderType(order.getId());
				if (ordertype != null) {
					ordertype.setStatus("unreceiption");
					order.setIsAccount("4");
					orderTypeService.saveORupdate(ordertype);
				}
			}
			if (order.getOrderType().equals("2")) {
				track.setCreateTime(new SimpleDateFormat("MM-dd HH:mm").format(new Date()));
			}
			String out_refund_no = "B" + order.getId() + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String refund_fee = "";
			String total_fee = order.getPay();
			if (!StringUtil.isEmpty(total_fee)) {
				double newfee = Double.valueOf(total_fee) * 100;
				total_fee = ((int) newfee) + "";
			} else {
				total_fee = "0";
			}
			refund_fee = total_fee;
			boolean fig = false;
			if (!fig) {
				for (int i = 0; i <= 3; i++) {
					boolean restul = false;
					try {
						restul = wxRefund.doRefund(out_refund_no, refund_fee + "", order.getOrderNo(), refund_fee + "");
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (restul) {
						order.setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						order.setBack_no(out_refund_no);
						orderService.saveORupdate(order);
						fig = true;
						break;
					}
					orderService.saveORupdate(order);
				}
			}
			List<String> sss = new ArrayList<String>();
			sss.add(order.getUserId().getUserName());
			new PushUtil();
			PushUtil.sendAlias("商家取消订单", sss, JiguangConfig.userKey, JiguangConfig.userSecret);
		}
		orderService.saveORupdate(order);
		trackService.saveORupdate(track);

		message.addProperty("success", true);
		message.addProperty("message", "接单成功");

		new PushJson().outString(message.toJSonString(), response);
	}

	// 商家退款确认
	@RequestMapping(params = "back", method = RequestMethod.POST)
	public void back(HttpServletRequest request, HttpServletResponse response) {
		String status = request.getParameter("status");// 状态
		String orderId = request.getParameter("orderId");// 订单id

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(status)) {
			message.addProperty("message", "订单id不能为空");
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

		String out_refund_no = "B" + order.getId() + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

		String total_fee = order.getPay();
		if (!StringUtil.isEmpty(total_fee)) {
			double newfee = Double.valueOf(total_fee) * 100;
			total_fee = ((int) newfee) + "";
		} else {
			total_fee = "0";
		}

		Track track = new Track();
		track.setOrderId(order);
		track.setStatus(status);
		if (!status.equals("backBalance")) {
			order.setOrderStatus("doing");
			orderService.saveORupdate(order);

			track.setStatus("unback");
			track.setBewrite("商家拒绝退款");
			message.addProperty("success", true);
			message.addProperty("message", "操作成功");

			// 推送订单信息
			List<String> sss = new ArrayList<String>();
			if (order.getUserId() != null) {
				sss.add(order.getUserId().getUserName());
			}

			PushUtil.sendAlias(order.getOrderNo() + "订单,商家拒绝退款,请与商家协商或申请琢呗客服介入", sss, JiguangConfig.userKey,
					JiguangConfig.userSecret);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		try {
			boolean fig = false;
			if (!fig) {
				for (int i = 0; i <= 3; i++) {

					boolean restul = wxRefund.doRefund(out_refund_no, total_fee + "", order.getOrderNo(),
							total_fee + "");
					if (restul) {

						order.setOrderStatus("unreceiption");
						order.setBack_no(out_refund_no);
						order.setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						order.setIsAccount("4");
						orderService.saveORupdate(order);

						if (order.getOrderType().equals("2")) {
							Cate cate = cateService.getCate(Integer.valueOf(orderId));
							if (cate == null) {
								message.addProperty("message", "订单信息不存在");
								message.addProperty("success", false);
								new PushJson().outString(message.toJSonString(), response);
								return;
							}

							Reserve reserve = reserveService.find(Integer.valueOf(cate.getReserveId()));

							List<Order> list = cateService.cateRes(cate.getReserveId());
							if (list.size() > 1) {
								reserve.setStatus("1");
							} else {

								reserve.setStatus("0");
							}

							reserveService.saveORupdate(reserve);
						}

						track.setStatus("backing");
						track.setBewrite("商家同意退款");
						// 推送订单信息
						List<String> sss = new ArrayList<String>();
						if (order.getUserId() != null) {
							sss.add(order.getUserId().getUserName());
						}

						PushUtil.sendAlias(order.getOrderNo() + "订单,商家已同意退款申请,退款金额将原路返回", sss, JiguangConfig.userKey,
								JiguangConfig.userSecret);
					} else {
						message.addProperty("success", false);
						message.addProperty("message", "微信端退款失败");
						new PushJson().outString(message.toJSonString(), response);

						order.setOrderStatus("backBalance");
						order.setBack_no(out_refund_no);
						order.setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						orderService.saveORupdate(order);
						return;
					}
				}
			}
			if (!fig) {
				message.addProperty("success", false);
				message.addProperty("message", "订单已经全额退款");
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			message.addProperty("success", false);
			message.addProperty("message", "微信端连接失败");
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		track.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		trackService.saveORupdate(track);

		message.addProperty("success", true);
		message.addProperty("message", "申请成功");

		new PushJson().outString(message.toJSonString(), response);
	}

	// 异常订单列表
	@RequestMapping(params = "getUnusual", method = RequestMethod.POST)
	public void getStatus(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Order> orders = orderService.getAllOrder(Integer.valueOf(companyId), "unusual", Integer.valueOf(start),
				Integer.valueOf(limit));
		// location = "25.2560978214,110.2188938856";
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		int i = 0;
		for (Order order : orders) {
			OrderType orderType = orderTypeService.getOrderType(order.getId(), "unusual");
			if (orderType == null) {
				break;
			}
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("orderId", order.getId());
			if (orderType.getUserId() != null) {
				record.addColumn("runningName", orderType.getUserId().getName());
				record.addColumn("runningphone", orderType.getUserId().getUserName());
			}
			if (orderType.getAddressId() != null) {
				record.addColumn("userPhone", orderType.getAddressId().getPhone());
				record.addColumn("userName", orderType.getAddressId().getName());
				record.addColumn("address", orderType.getAddressId().getAddress());
				record.addColumn("room", orderType.getAddressId().getRoom());
			}
			record.addColumn("orderNo", order.getOrderNo());
			record.addColumn("total_new", order.getTotal());
			record.addColumn("time", order.getAddTime());
			record.addColumn("content", "订单配送异常,已送回本店");
			grid.addRecord(record);
			i++;
		}
		grid.addProperties("totalCount", i);
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 催单列表
	@RequestMapping(params = "getReminder", method = RequestMethod.POST)
	public void getReminder(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Order> orders = orderService.getAllOrder(Integer.valueOf(companyId), "doing", Integer.valueOf(start),
				Integer.valueOf(limit));
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		int i = 0;
		for (Order order : orders) {
			OrderType orderType = orderTypeService.getOrderType(order.getId());
			if (orderType == null) {
				break;
			}
			if (orderType.getIsReminder()) {
				JSonGridRecord record = new JSonGridRecord();
				record.addColumn("userPhone", orderType.getAddressId().getPhone());
				record.addColumn("userName", orderType.getAddressId().getName());
				record.addColumn("time", order.getAddTime());
				record.addColumn("content", "请尽快处理");

				grid.addRecord(record);
				i++;
			}

		}
		grid.addProperties("totalCount", i);
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 异常
	@RequestMapping(params = "unusual", method = RequestMethod.POST)
	public void unusual(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId)) {
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

		OrderType orderType = orderTypeService.getOrderType(Integer.valueOf(orderId));
		if (orderType == null) {
			message.addProperty("message", "订单信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		order.setOrderStatus("unusual");
		orderService.saveORupdate(order);
		orderType.setStatus("unusual");
		orderTypeService.saveORupdate(orderType);

		// 推送订单信息
		List<String> sss = new ArrayList<String>();
		List<Staff> adminList = staffService.getList(order.getCompanyId().getId(), 1);
		for (Staff admin : adminList) {
			sss.add(admin.getUserName());
		}
		List<Staff> cashierList = staffService.getList(order.getCompanyId().getId(), 2);
		for (Staff cashier : cashierList) {
			sss.add(cashier.getUserName());
		}
		new PushUtil();
		PushUtil.sendAlias("订单:" + order.getOrderNo() + "异常,已送回商家店内", sss, JiguangConfig.companyKey,
				JiguangConfig.companySecret);
		if (order.getUserId() != null) {
			sss.add(order.getUserId().getUserName());
		}

		PushUtil.sendAlias("订单:" + order.getOrderNo() + "异常,已送回商家店内", sss, JiguangConfig.userKey,
				JiguangConfig.userSecret);

		message.addProperty("success", true);
		message.addProperty("message", "操作成功");
		new PushJson().outString(message.toJSonString(), response);
	}

	// 催单
	@RequestMapping(params = "reminder", method = RequestMethod.POST)
	public void reminder(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId)) {
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

		OrderType orderType = orderTypeService.getOrderType(Integer.valueOf(orderId));
		if (orderType == null) {
			message.addProperty("message", "订单信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		orderType.setIsReminder(true);
		orderTypeService.saveORupdate(orderType);

		// 推送订单信息
		List<String> sss = new ArrayList<String>();
		List<Staff> adminList = staffService.getList(order.getCompanyId().getId(), 1);
		for (Staff admin : adminList) {
			sss.add(admin.getUserName());
		}
		List<Staff> cashierList = staffService.getList(order.getCompanyId().getId(), 2);
		for (Staff cashier : cashierList) {
			sss.add(cashier.getUserName());
		}
		new PushUtil();
		PushUtil.sendAlias("订单:" + order.getOrderNo() + ",客户催单,请及时处理", sss, JiguangConfig.companyKey,
				JiguangConfig.companySecret);
		if (orderType.getUserId() != null) {
			sss.add(orderType.getUserId().getUserName());
		}

		PushUtil.sendAlias("订单:" + order.getOrderNo() + ",客户催单,请及时处理", sss, JiguangConfig.runManKey,
				JiguangConfig.runManSecret);

		message.addProperty("success", true);
		message.addProperty("message", "催单成功");
		new PushJson().outString(message.toJSonString(), response);
	}

	// 商家新订单
	@RequestMapping(params = "newOrder", method = RequestMethod.POST)
	public void newOrder(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		DecimalFormat dFormat = new DecimalFormat("######0.00");

		List<Order> orders = orderService.getStatus(Integer.valueOf(companyId), "paysuccess");

		JSonTree json = new JSonTree();
		for (Order order : orders) {
			JSonTreeNode node = new JSonTreeNode();

			if (order.getOrderType().equals("1")) {

				OrderType orderType = orderTypeService.getOrderType(order.getId());

				if (orderType != null) {
					if (!orderType.getStatus().equals("paysuccess"))
						continue;

					if (orderType.getAddressId() != null) {
						int distance = MapUtil.getdistance(orderType.getAddressId().getLocation(),
								order.getCompanyId().getCoordinates());
						String range = distance + "m";

						if (distance >= 1000) {
							range = Double.valueOf(distance) / 1000 + "km";
						}

						node.addProperty("distance", range);
						node.addProperty("userAddress", orderType.getAddressId().getAddress());
						node.addProperty("room", orderType.getAddressId().getRoom());

					} else {
						node.addProperty("userAddress", "自取");
						node.addProperty("distance", "自取");
						node.addProperty("room", "!");
					}

					if (orderType.getAddressId() == null) {
						node.addProperty("userPhone", "");
						node.addProperty("userName", "");
						node.addProperty("quantity", "");
						node.addProperty("cost", "0");
					} else {
						node.addProperty("userPhone", orderType.getAddressId().getPhone());
						node.addProperty("userName", orderType.getAddressId().getName());
						node.addProperty("quantity", orderType.getQuantity());
						node.addProperty("cost", orderType.getPrice());
					}

					node.addProperty("status", orderType.getStatus());
					if (orderType.getUserId() != null) {
						node.addProperty("runUserName", orderType.getUserId().getName());
						node.addProperty("runStatus", orderType.getStatus());
						node.addProperty("runUserPhone", orderType.getUserId().getPhone());
						node.addProperty("shipTime", orderType.getShippingTime());
					}

					if (!orderType.getType().equals("self")) {
						Distribution dis = distributionService.getDistribution(Integer.valueOf(companyId));
						String tempDis = dis.getDistributionPrice(); // 配送费
						double total = Double.valueOf(order.getTotal()) - Double.valueOf(tempDis);
						double pay = Double.valueOf(order.getPay()) - Double.valueOf(tempDis);

						if (pay < 0)
							pay = 0;

						node.addProperty("activity", dFormat.format(total - pay));
						node.addProperty("total_new", dFormat.format(total));
						node.addProperty("pay", dFormat.format(pay));
					} else {
						double total = Double.valueOf(order.getTotal());
						double pay = Double.valueOf(order.getPay());

						if (pay < 0)
							pay = 0;

						node.addProperty("activity", dFormat.format(total - pay));
						node.addProperty("total_new", dFormat.format(total));
						node.addProperty("pay", dFormat.format(pay));
					}
				}
			} else if (order.getOrderType().equals("2")) {
				Cate cate = cateService.getCate(order.getId());
				if (cate != null) {
					node.addProperty("userPhone", cate.getPhone());
					node.addProperty("userName", order.getUserId().getName());
					if (cate.getSeat().equals("dt")) {
						node.addProperty("tableNo", cate.getTableNo());
					} else {
						node.addProperty("tableNo", cate.getName() + " " + cate.getTableNo());
					}
					node.addProperty("reachTime", cate.getEndTime());
					node.addProperty("seat", cate.getSeat());
					node.addProperty("deposit", cate.getPrice());
					node.addProperty("meals", cate.getMeals());
				}
				node.addProperty("total_new", order.getTotal());
				node.addProperty("pay", order.getPay());

				double total = Double.valueOf(order.getTotal());
				double pay = Double.valueOf(order.getPay());

				node.addProperty("activity", dFormat.format(total - pay));
				Delicacy delicacy = delicacyService.getDelicacy(order.getCompanyId().getId());

				double meal = 0;
				// if (cate.getMealFee() == null) {
				// meal = 0;
				// } else {
				// meal = Double.valueOf(cate.getMealFee());
				// }
				if (cate.getMeals() == null) {// 2018-10-19 @Tyy 餐位费显示不正确
					meal = 0;
				} else {
					meal = Double.valueOf(cate.getMeals());
				}
				Double tempMeal = meal * Double.valueOf(delicacy.getMealFee());

				node.addProperty("position_fee", dFormat.format(tempMeal));
				node.addProperty("fee", delicacy.getMealFee());
			}
			node.addProperty("time", order.getAddTime());
			node.addProperty("discount", order.getDiscount());
			node.addProperty("isDiscount", order.getIsDiscount());
			node.addProperty("remarks", order.getRemarks() == null ? "" : order.getRemarks());
			node.addProperty("type", order.getOrderType());
			node.addProperty("orderNo", order.getOrderNo());
			node.addProperty("orderId", order.getId());
			if (!StringUtil.isEmpty(order.getPay()) && !StringUtil.isEmpty(order.getTotal())) {
				node.addProperty("reduce", Double.valueOf(
						dFormat.format(Double.parseDouble(order.getTotal()) - Double.parseDouble(order.getPay()))));
			}

			List<Relating> relatingList = relatingService.getrelating(Integer.valueOf(order.getId()),
					Integer.valueOf(order.getCompanyId().getId()));

			double boxPrice = 0d;
			for (Relating relating : relatingList) {
				Goods good = goodsService.getGoods(relating.getGoodsId());
				if (good == null) {
					message.addProperty("message", "商品已下架或不存在");
					message.addProperty("success", false);
					new PushJson().outString(message.toJSonString(), response);
					return;
				}

				if (order.getOrderType().equals("1")) {
					boxPrice = boxPrice + relating.getNumb() * Double.valueOf(good.getBoxPrice());
				}
				JSonTreeNode node1 = new JSonTreeNode();
				node1.addProperty("name", good.getName());
				node1.addProperty("logo", good.getZoomUrl());
				node1.addProperty("price", good.getPrice());
				node1.addProperty("number", relating.getNumb());
				String price = good.getPrice();
				if (!StringUtil.isEmpty(good.getSvgPrice())) {
					if (good.getSvgPrice().equals("0")) {
						price = good.getSvgPrice();
					}
				}
				node1.addProperty("subtotal", relating.getNumb() * Double.parseDouble(price));
				node.addProperty("leaf", true);
				node.addChild(node1);
			}

			if (order.getOrderType().equals("1")) {
				node.addProperty("packing_fee", boxPrice);
			}

			json.addNode(node);
		}

		new PushJson().outString(json.toJSonString(), response);
	}

	// 商家所有订单
	@RequestMapping(params = "allOrder", method = RequestMethod.POST)
	public void allOrder(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String status = request.getParameter("status");
		String start = request.getParameter("start");// 起始页
		String limit = request.getParameter("limit");// 容量

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)) {
			message.addProperty("message", "start和limit不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(status)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Order> orders = orderService.getAllOrder(Integer.valueOf(companyId), status, Integer.valueOf(start),
				Integer.valueOf(limit));

		DecimalFormat dFormat = new DecimalFormat("######0.00");

		JSonTree json = new JSonTree();
		for (Order order : orders) {
			JSonTreeNode node = new JSonTreeNode();

			if (order.getOrderType().equals("1")) {
				OrderType orderType = orderTypeService.getOrderType(order.getId());
				if (!orderType.getType().equals("self")) {
					if (!StringUtil.isEmpty(orderType.getStatus())) {
						if (orderType.getStatus().equals("paysuccess")) {
							continue;
						}
					}

					int distance = 0;
					if (orderType.getAddressId() != null) {
						distance = MapUtil.getdistance(orderType.getAddressId().getLocation(),
								order.getCompanyId().getCoordinates());
					}

					String range = distance + "m";

					if (distance >= 1000) {
						range = Double.valueOf(distance) / 1000 + "km";
					}

					node.addProperty("distance", range);
					node.addProperty("type", "1");
					node.addProperty("quantity", orderType.getQuantity());
					node.addProperty("cost", orderType.getPrice());
					node.addProperty("code", orderType.getCode());

					if (orderType.getStatus().equals("paysuccess")) {
						node.addProperty("runStatus", "支付成功");
					} else if (orderType.getStatus().equals("unreceiption")) {
						node.addProperty("runStatus", "订单取消");
					} else {
						node.addProperty("runStatus", orderType.getStatus());
					}

					if (StringUtil.isEmpty(orderType.getCode())) {
						node.addProperty("code", "暂未生成取餐码");
					}
					if (StringUtil.isEmpty(orderType.getStatus())) {
						node.addProperty("code", "暂时无人接单");
					}

					if (orderType.getUserId() != null) {
						if (!StringUtil.isEmpty(orderType.getUserId().getId() + "")) {
							node.addProperty("runUserName", orderType.getUserId().getName());
							node.addProperty("runUserPhone", orderType.getUserId().getPhone());
							node.addProperty("shipTime", orderType.getShippingTime());
						}
					} else {
						node.addProperty("runUserName", "暂时无人接单");
						node.addProperty("runUserPhone", "暂时无人接单");
						node.addProperty("shipTime", "暂时无人接单");
					}

					if (orderType.getAddressId() != null) {
						node.addProperty("userPhone", orderType.getAddressId().getPhone());
						node.addProperty("userName", orderType.getAddressId().getName());
						node.addProperty("userAddress", orderType.getAddressId().getAddress());
						node.addProperty("room", orderType.getAddressId().getRoom());
					} else {
						node.addProperty("userPhone", "");
						node.addProperty("userName", "用户已删除");
						node.addProperty("userAddress", "用户已删除");
						node.addProperty("room", "!");
					}
				} else {
					if (!StringUtil.isEmpty(orderType.getStatus())) {
						if (orderType.getStatus().equals("paysuccess")) {
							continue;
						}
					}

					node.addProperty("distance", "自取");
					node.addProperty("type", "1");
					node.addProperty("quantity", orderType.getQuantity());
					node.addProperty("cost", orderType.getPrice());
					node.addProperty("code", orderType.getCode());

					if (orderType.getStatus().equals("paysuccess")) {
						node.addProperty("runStatus", "支付成功");
					} else if (orderType.getStatus().equals("unreceiption")) {
						node.addProperty("runStatus", "订单取消");
					} else {
						node.addProperty("runStatus", orderType.getStatus());
					}

					if (StringUtil.isEmpty(orderType.getCode())) {
						node.addProperty("code", "暂未生成取餐码");
					}
					if (StringUtil.isEmpty(orderType.getStatus())) {
						node.addProperty("code", "暂时无人接单");
					}

					node.addProperty("runUserName", "到店自取");
					node.addProperty("runUserPhone", "");
					node.addProperty("shipTime", "");
					node.addProperty("runstatus", "self");
					node.addProperty("userPhone", "");
					node.addProperty("userName", "");
					node.addProperty("userAddress", "自取");
					node.addProperty("room", "!");
				}

				double total = Double.valueOf(order.getTotal());
				double pay = Double.valueOf(order.getPay());
				double price = 0d;
				if (orderType.getPrice() != null) {
					price = Double.valueOf(orderType.getPrice());
				}

				node.addProperty("activity", dFormat.format(total - pay));
				node.addProperty("total_new", dFormat.format(total - price));
				node.addProperty("pay", dFormat.format(pay - price));

			}

			if (order.getOrderType().equals("2")) {
				node.addProperty("type", "2");
				Cate cate = cateService.getCate(order.getId());
				if (cate != null) {

					if (cate.getSeat().equals("dt")) {
						node.addProperty("tableNo", cate.getTableNo());
					} else {
						node.addProperty("tableNo", cate.getName() + " " + cate.getTableNo());
					}

					node.addProperty("seat", cate.getSeat());
					node.addProperty("meals", cate.getMeals());
					node.addProperty("shipTime", cate.getEndTime());
					node.addProperty("phone", cate.getPhone());
					node.addProperty("quantity", cate.getMeals());
					node.addProperty("cost", cate.getPrice());
					if (!StringUtil.isEmpty(cate.getUserId().getId() + "")) {
						node.addProperty("userPhone", cate.getUserId().getUserName());
					}

					if (cate.getMealFee() != null) {
						double postFee = Double.valueOf(cate.getMealFee()) / Integer.valueOf(cate.getMeals());
						node.addProperty("fee", postFee);
						node.addProperty("position_fee", cate.getMealFee());
					}
				}

				double total = Double.valueOf(order.getTotal());
				double pay = Double.valueOf(order.getPay());

				node.addProperty("total_new", order.getTotal());
				node.addProperty("pay", order.getPay());
				node.addProperty("activity", dFormat.format(total - pay));
			}

			String newStatus = "";
			if (order.getOrderStatus().equals("unreceiption")) {
				newStatus = "4";
			} else if (order.getOrderStatus().equals("finish")) {
				newStatus = "1";
			} else if (order.getOrderStatus().equals("backBalance")) {
				newStatus = "3";
			} else if (order.getOrderStatus().equals("unusual")) {
				newStatus = "2";
			} else if (order.getOrderStatus().equals("doing")) {
				newStatus = "0";
			} else if (order.getOrderStatus().equals("paysuccess")) {
				newStatus = "6";
			}

			node.addProperty("status", newStatus);
			node.addProperty("orderId", order.getId());
			node.addProperty("time", order.getAddTime());
			node.addProperty("discount", order.getDiscount());
			node.addProperty("isDiscount", order.getIsDiscount());
			node.addProperty("remarks", order.getRemarks() == null ? "" : order.getRemarks());
			node.addProperty("orderNo", order.getOrderNo());
			if (!StringUtil.isEmpty(order.getPay()) && !StringUtil.isEmpty(order.getTotal())) {
				node.addProperty("reduce", Double.valueOf(
						dFormat.format(Double.parseDouble(order.getTotal()) - Double.parseDouble(order.getPay()))));
			}
			List<Relating> relatingList = relatingService.getrelating(Integer.valueOf(order.getId()),
					Integer.valueOf(order.getCompanyId().getId()));
			double packing = 0;
			for (Relating relating : relatingList) {
				Goods good = goodsService.getGoods(relating.getGoodsId());
				if (good == null) {
					message.addProperty("message", "商品已下架或不存在");
					message.addProperty("success", false);
					new PushJson().outString(message.toJSonString(), response);
					return;
				}
				packing = packing + relating.getNumb() * Double.parseDouble(good.getBoxPrice());
				JSonTreeNode node1 = new JSonTreeNode();
				node1.addProperty("name", good.getName());
				node1.addProperty("logo", good.getZoomUrl());
				node1.addProperty("price", good.getPrice());
				node1.addProperty("number", relating.getNumb());

				if (StringUtil.isEmpty(good.getSvgPrice())) {
					node1.addProperty("subtotal", relating.getNumb() * Double.parseDouble(good.getPrice()));
				} else {
					node1.addProperty("subtotal", relating.getNumb() * Double.parseDouble(good.getSvgPrice()));
				}

				node.addProperty("leaf", true);
				node.addChild(node1);
			}

			if (order.getOrderType().equals("1")) {
				node.addProperty("packing_fee", packing);
			}

			json.addNode(node);
		}

		new PushJson().outString(json.toJSonString(), response);
	}

	// 退款订单
	@RequestMapping(params = "backBalance", method = RequestMethod.POST)
	public void backBalance(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Order> orders = orderService.getStatus(Integer.valueOf(companyId), "backBalance");

		JSonTree json = new JSonTree();
		for (Order order : orders) {
			JSonTreeNode node = new JSonTreeNode();
			if (order.getOrderType().equals("1")) {
				OrderType orderType = orderTypeService.getOrderType(order.getId());
				if (orderType == null)
					continue;

				if (orderType.getAddressId() != null) {
					node.addProperty("userPhone", orderType.getAddressId().getPhone());
					node.addProperty("userName", orderType.getAddressId().getName());
					node.addProperty("quantity", orderType.getQuantity());
					node.addProperty("cost", orderType.getPrice());
				} else {
					node.addProperty("userPhone", "");
					node.addProperty("userName", "");
					node.addProperty("quantity", "");
					node.addProperty("cost", 0);
				}

				if (orderType.getUserId() != null) {
					node.addProperty("runUserName", orderType.getUserId().getName());
					node.addProperty("runStatus", orderType.getStatus());
					node.addProperty("runUserPhone", orderType.getUserId().getPhone());
					node.addProperty("shipTime", orderType.getShippingTime());
				}
			}

			node.addProperty("type", order.getOrderType());

			if (order.getOrderType().equals("2")) {
				Cate cate = cateService.getCate(order.getId());
				if (cate != null) {
					node.addProperty("userPhone", cate.getPhone());
					node.addProperty("userName", order.getUserId().getName());

					if (cate.getSeat().equals("dt")) {
						node.addProperty("tableNo", cate.getTableNo());
					} else {
						node.addProperty("tableNo", cate.getName() + " " + cate.getTableNo());
					}

					node.addProperty("reachTime", cate.getEndTime());
					node.addProperty("seat", cate.getSeat());
					node.addProperty("deposit", cate.getPrice());
					node.addProperty("meals", cate.getMeals());
				}
			}
			node.addProperty("orderId", order.getId());
			node.addProperty("time", order.getAddTime());
			node.addProperty("discount", order.getDiscount());
			node.addProperty("isDiscount", order.getIsDiscount());
			node.addProperty("remarks", order.getRemarks());
			node.addProperty("total", order.getTotal());
			node.addProperty("pay", order.getPay());
			node.addProperty("status", order.getOrderStatus());
			node.addProperty("orderNo", order.getOrderNo());
			if (!StringUtil.isEmpty(order.getPay()) && !StringUtil.isEmpty(order.getTotal())) {
				DecimalFormat dFormat = new DecimalFormat("######0.00");
				node.addProperty("reduce", Double.valueOf(
						dFormat.format(Double.parseDouble(order.getTotal()) - Double.parseDouble(order.getPay()))));
			}
			List<Relating> relatingList = relatingService.getrelating(Integer.valueOf(order.getId()),
					Integer.valueOf(order.getCompanyId().getId()));
			double total = 0;
			for (Relating relating : relatingList) {
				Goods good = goodsService.getGoods(relating.getGoodsId());
				if (good == null) {
					message.addProperty("message", "商品已下架或不存在");
					message.addProperty("success", false);
					new PushJson().outString(message.toJSonString(), response);
					return;
				}
				total = total + relating.getNumb() * Double.parseDouble(good.getPrice());
				JSonTreeNode node1 = new JSonTreeNode();
				node1.addProperty("name", good.getName());
				node1.addProperty("logo", good.getZoomUrl());
				node1.addProperty("price", good.getPrice());
				node1.addProperty("number", relating.getNumb());
				node1.addProperty("subtotal", relating.getNumb() * Double.parseDouble(good.getPrice()));

				node.addProperty("leaf", true);
				node.addChild(node1);
			}
			json.addNode(node);
		}
		new PushJson().outString(json.toJSonString(), response);
	}

	// 修改备注
	@RequestMapping(params = "updateRemark", method = RequestMethod.POST)
	public void updateRemark(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		String remarks = request.getParameter("remarks");
		String activityId = request.getParameter("activit");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(activityId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		DecimalFormat dFormat = new DecimalFormat("######0.00");

		Order order = orderService.getOrder(Integer.valueOf(orderId));
		if (order != null) {

			CompanyActivity company = companyActivityService.getCompanyActivity(Integer.valueOf(activityId));

			double total = Double.valueOf(order.getTotal());

			switch (company.getActivityId().getId()) {
			case 1:
				total = total - Double.valueOf(company.getBenefit());
				break;
			case 2:
				total = total - Double.valueOf(company.getNewUser());
				break;
			case 3:
				total = (total * Double.valueOf(company.getSvg())) / 10;
				break;
			case 4:
				total = total - Double.valueOf(company.getCoupon());
				break;
			}

			order.setPay(dFormat.format(total) + "");

			if (!StringUtil.isEmpty(remarks)) {
				order.setRemarks(remarks);
			}

			orderService.saveORupdate(order);
		} else {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		message.addProperty("success", true);
		message.addProperty("message", "备注信息修改成功");
		new PushJson().outString(message.toJSonString(), response);
	}

	// 支付信息
	@RequestMapping(params = "getPay", method = RequestMethod.POST)
	public void getPay(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		String token = request.getParameter("token");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(token)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);
		if (user == null) {
			message.addProperty("message", "token失效");
			message.addProperty("isout", true);
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
		message.addProperty("success", true);
		message.addProperty("companyName", order.getCompanyId().getName());
		message.addProperty("orderNo", order.getOrderNo());
		message.addProperty("pay", order.getPay() == null ? "0" : order.getPay());
		message.addProperty("orderId", order.getId());

		new PushJson().outString(message.toJSonString(), response);
	}

}
