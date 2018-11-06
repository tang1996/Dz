package com.dz.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Address;
import com.dz.entity.Cate;
import com.dz.entity.Company;
import com.dz.entity.CompanyOrderCount;
import com.dz.entity.ComputerCate;
import com.dz.entity.Delicacy;
import com.dz.entity.Distribution;
import com.dz.entity.Goods;
import com.dz.entity.InsideOrder;
import com.dz.entity.Nature;
import com.dz.entity.Order;
import com.dz.entity.OrderType;
import com.dz.entity.Relating;
import com.dz.entity.Reserve;
import com.dz.service.ICateService;
import com.dz.service.ICompanyOrderCountService;
import com.dz.service.ICompanyService;
import com.dz.service.IComputerCateService;
import com.dz.service.IDelicacyService;
import com.dz.service.IDistributionService;
import com.dz.service.IGoodsService;
import com.dz.service.IInsideOrderService;
import com.dz.service.INatureService;
import com.dz.service.IOrderService;
import com.dz.service.IOrderTypeService;
import com.dz.service.IRelatingService;
import com.dz.service.IReserveService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/companyOrderCount")
public class CompanyOrderCountController {

	@Autowired
	private ICompanyOrderCountService companyOrderCountService;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private IOrderService orderService;

	@Autowired
	private IGoodsService goodsService;

	@Autowired
	private IRelatingService relatingService;

	@Autowired
	private IReserveService reserveService;

	@Autowired
	private IInsideOrderService insideOrderService;

	@Autowired
	private IOrderTypeService orderTypeService;

	@Autowired
	private INatureService natureService;

	@Autowired
	private ICateService cateService;

	@Autowired
	private IComputerCateService computerCateService;

	@Autowired
	private IDistributionService distributionService;

	@Autowired
	private IDelicacyService delicacyService;

	// ===============================手机端============================== //
	// 商家当日交易统计
	@RequestMapping(params = "dayCount", method = RequestMethod.POST)
	public void dayCount(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("companyId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "token不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		Object[] orderCount = orderService.getCount(Integer.valueOf(companyId), date);
		if (orderCount == null) {
			message.addProperty("success", true);
			message.addProperty("balance", 0);
			message.addProperty("total", 0);
			message.addProperty("discount", 0);
			message.addProperty("num", 0);
		} else {
			String balance = orderCount[1] == null ? "0" : orderCount[1].toString();
			String total = orderCount[2] == null ? "0" : orderCount[2].toString();
			String num = orderCount[0] == null ? "0" : orderCount[0].toString();

			Distribution dis = distributionService.getDistribution(Integer.valueOf(companyId));

			String dissum = "0";
			if (!StringUtil.isEmpty(dis.getDistributionPrice())) {
				dissum = dis.getDistributionPrice();
			}

			long sumTypeCount = orderService.getTypeOrder(Integer.valueOf(companyId), date, date);

			double sumDis = Double.valueOf(dissum) * sumTypeCount;

			double sumTotal = Double.valueOf(total) - sumDis;
			double sumbalance = Double.valueOf(balance) - sumDis;

			double discount = Double.valueOf(Double.parseDouble(total) - Double.parseDouble(balance));
			DecimalFormat dFormat = new DecimalFormat("######0.00");
			message.addProperty("success", true);
			message.addProperty("balance", dFormat.format(sumbalance));
			message.addProperty("total", dFormat.format(sumTotal));
			message.addProperty("discount", discount);
			message.addProperty("num", num);
		}
		new PushJson().outString(message.toJSonString(), response);
	}

	// 商家外卖交易统计
	@RequestMapping(params = "WMCount", method = RequestMethod.POST)
	public void WMCount(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("companyId");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Object[] orderCount = orderService.getCount(Integer.valueOf(companyId), startTime, endTime, "1");
		DecimalFormat dFormat = new DecimalFormat("######0.00");
		if (orderCount == null) {
			message.addProperty("success", true);
			message.addProperty("balance", 0);
			message.addProperty("total", 0);
			message.addProperty("discount", 0);
			message.addProperty("num", 0);
		} else {

			Distribution dis = distributionService.getDistribution(Integer.valueOf(companyId));

			String count = orderCount[1] == null ? "0" : orderCount[1].toString();// 单数
			String total = orderCount[2] == null ? "0" : orderCount[2].toString(); // 总金额
			String pay = orderCount[0] == null ? "0" : orderCount[0].toString(); // 实收

			String dissum = dis.getDistributionPrice() == null ? "0" : dis.getDistributionPrice();

			long sumTypeCount = orderService.getTypeOrder(Integer.valueOf(companyId), startTime, endTime);

			double sumDis = Double.valueOf(dissum) * sumTypeCount;

			double discount = Double.valueOf(dFormat.format(Double.parseDouble(total) - Double.parseDouble(pay)));
			message.addProperty("success", true);
			message.addProperty("balance", dFormat.format(Double.parseDouble(pay) - sumDis));
			message.addProperty("total", dFormat.format(Double.parseDouble(total) - sumDis));
			message.addProperty("discount", discount);
			message.addProperty("num", count);
		}
		new PushJson().outString(message.toJSonString(), response);

	}

	// 商家美食交易统计
	@RequestMapping(params = "MSCount", method = RequestMethod.POST)
	public void MSCount(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("companyId");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Object[] orderCount = orderService.getCount(Integer.valueOf(companyId), startTime, endTime, "2");

		DecimalFormat dFormat = new DecimalFormat("######0.00");
		if (orderCount == null) {
			message.addProperty("success", true);
			message.addProperty("balance", 0);
			message.addProperty("total", 0);
			message.addProperty("discount", 0);
			message.addProperty("num", 0);
		} else {
			String count = orderCount[1] == null ? "0" : orderCount[1].toString();// 单数
			String total = orderCount[2] == null ? "0" : orderCount[2].toString(); // 总金额
			String pay = orderCount[0] == null ? "0" : orderCount[0].toString(); // 实收
			double discount = Double.valueOf(dFormat.format(Double.parseDouble(total) - Double.parseDouble(pay)));
			message.addProperty("success", true);
			message.addProperty("balance", dFormat.format(Double.parseDouble(pay)));
			message.addProperty("total", dFormat.format(Double.parseDouble(total)));
			message.addProperty("discount", discount);
			message.addProperty("num", count);
		}

		new PushJson().outString(message.toJSonString(), response);

	}

	// 商家已完成订单统计
	@RequestMapping(params = "getCount", method = RequestMethod.POST)
	public void getCount(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("companyId");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		// 外卖已完成
		List<Order> WMfinish = orderService.getList(Integer.valueOf(companyId), startTime, endTime, "1", "finish");
		// 美食已完成
		List<Order> MSfinish = orderService.getList(Integer.valueOf(companyId), startTime, endTime, "2", "finish");
		// 堂食已完成
		List<InsideOrder> insideFinish = insideOrderService.getList(Integer.valueOf(companyId), startTime, endTime,
				"finish");

		DecimalFormat dFormat = new DecimalFormat("######0.00");

		double WMpay = 0;
		double MSpay = 0;
		double YSpay = 0;

		Distribution dis = distributionService.getDistribution(Integer.valueOf(companyId));

		String dissum = dis.getDistributionPrice() == null ? "0" : dis.getDistributionPrice();

		long sumTypeCount = orderService.getTypeOrder(Integer.valueOf(companyId), startTime, endTime);

		double sumDis = Double.valueOf(dissum) * sumTypeCount;

		// 外卖支付统计
		for (Order order : WMfinish) {
			WMpay = Double.valueOf(dFormat.format(WMpay + Double.parseDouble(order.getPay())));
		}
		// 美食支付统计
		for (Order order : MSfinish) {
			MSpay = Double.valueOf(dFormat.format(MSpay + Double.parseDouble(order.getPay())));
		}

		for (InsideOrder inside : insideFinish) {
			if (NumberUtils.isNumber(inside.getPay())) {// 新加判断
				YSpay = Double.valueOf(dFormat.format(YSpay + Double.parseDouble(inside.getPay())));
			}
		}

		message.addProperty("success", true);

		message.addProperty("TSfinish", insideFinish.size());
		message.addProperty("TSpay", dFormat.format(YSpay));
		message.addProperty("WMfinish", WMfinish.size());
		// message.addProperty("WMcancel", WMcancel.size());
		message.addProperty("MSfinish", MSfinish.size());
		// message.addProperty("MScancel", MScancel.size());
		// message.addProperty("MSdoing", MSdoing.size());
		message.addProperty("WMpay", dFormat.format(WMpay - sumDis));
		message.addProperty("MSpay", MSpay);
		new PushJson().outString(message.toJSonString(), response);

	}

	// 商家未完成订单统计
	@RequestMapping(params = "getDoingCount", method = RequestMethod.POST)
	public void getDoingCount(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("companyId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		// 外卖未完成
		List<Order> WMorder = orderService.getList(Integer.valueOf(companyId), "1", "doing");
		// 美食未完成
		List<Order> MSorder = orderService.getList(Integer.valueOf(companyId), "2", "doing");
		DecimalFormat dFormat = new DecimalFormat("#.00");
		double WMpay = 0;
		double MSpay = 0;
		// 外卖未收款统计
		for (Order order : WMorder) {
			WMpay = Double.valueOf(dFormat.format(WMpay + Double.parseDouble(order.getPay())));
		}
		// 美食未收款统计
		for (Order order : MSorder) {
			double temp = order.getPay() == null ? 0d : Double.valueOf(order.getPay());
			MSpay = Double.valueOf(dFormat.format(MSpay + temp));
		}

		message.addProperty("success", true);
		message.addProperty("WMorder", WMorder.size());
		message.addProperty("MSorder", MSorder.size());
		message.addProperty("WMpay", WMpay);
		message.addProperty("MSpay", MSpay);
		new PushJson().outString(message.toJSonString(), response);

	}

	// 商家订单列表
	@RequestMapping(params = "getList", method = RequestMethod.POST)
	public void getList(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("companyId");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String type = request.getParameter("type");
		String status = request.getParameter("status");// finish 已完成,doing
														// 进行中,cancel 已取消

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime)
				|| StringUtil.isEmpty(type) || StringUtil.isEmpty(status)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Order> list = orderService.getList(Integer.valueOf(companyId), startTime, endTime, type, status);
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		DecimalFormat dFormat = new DecimalFormat("######0.00");

		for (Order order : list) {
			JSonGridRecord record = new JSonGridRecord();
			OrderType ordertype = orderTypeService.getOrderType(order.getId());
			if (ordertype != null) {
				if (ordertype.getType().equals("self")) {
					record.addColumn("total", order.getPay());
				} else {
					Distribution dis = distributionService.getDistribution(order.getCompanyId().getId());

					if (dis != null) {
						double sum = Double.valueOf(order.getPay()) - Double.valueOf(dis.getDistributionPrice());
						record.addColumn("total", dFormat.format(sum));
					} else {
						record.addColumn("total", order.getPay());
					}
				}
			} else {
				record.addColumn("total", order.getPay());
			}

			record.addColumn("orderNo", order.getOrderNo());
			record.addColumn("addTime", order.getAddTime());
			record.addColumn("id", order.getId());
			record.addColumn("type", order.getOrderType());
			if (order.getOrderType().equals("2")) {
				Cate cate = cateService.getCate(order.getId());
				if (cate != null) {
					if (cate.getSeat().equals("dt")) {
						record.addColumn("tableNo", "大厅:" + cate.getName());
					} else if (cate.getSeat().equals("bx")) {
						record.addColumn("tableNo", "包厢:" + cate.getName() + cate.getTableNo() + "号桌");
					}

					record.addColumn("repastTime", cate.getEndTime());
				}

			}
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", list.size());
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 商家为完成订单列表
	@RequestMapping(params = "getDoingList", method = RequestMethod.POST)
	public void getDoingList(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String type = request.getParameter("type");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(type)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Order> list = orderService.getList(Integer.valueOf(companyId), type, "doing");
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (Order order : list) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", order.getId());
			record.addColumn("orderNo", order.getOrderNo());
			record.addColumn("addTime", order.getAddTime());
			record.addColumn("total", order.getPay());
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", list.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 商家订单详情
	@RequestMapping(params = "getOrder", method = RequestMethod.POST)
	public void getOrder(HttpServletRequest request, HttpServletResponse response) {
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

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		DecimalFormat dFormat = new DecimalFormat("######0.00");
		double sTotal = Double.valueOf(order.getTotal());
		double sPay = Double.valueOf(order.getPay());
		grid.addProperties("orderNo", order.getOrderNo());
		grid.addProperties("companyPhone", order.getCompanyId().getPhone());
		grid.addProperties("addTime", order.getAddTime());
		grid.addProperties("finishTime", order.getFinishTime());
		grid.addProperties("remarks", order.getRemarks() == null ? "" : order.getRemarks());
		grid.addProperties("total", dFormat.format(sTotal));
		grid.addProperties("pay", dFormat.format(sPay));
		grid.addProperties("discount", dFormat.format(sTotal - sPay));
		grid.addProperties("payTime", order.getPayTime());

		List<Relating> relatingList = relatingService.getrelating(Integer.valueOf(order.getId()),
				Integer.valueOf(order.getCompanyId().getId()));
		Double boxPrice = 0d;
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
			if (StringUtil.isEmpty(good.getSvgPrice())) {
				record.addColumn("price", good.getPrice());
			} else {
				record.addColumn("price", good.getSvgPrice());
			}
			record.addColumn("number", relating.getNumb());

			if (StringUtil.isEmpty(good.getSvgPrice())) {
				record.addColumn("subtotal", relating.getNumb() * Double.parseDouble(good.getPrice()));
			} else {
				record.addColumn("subtotal", relating.getNumb() * Double.parseDouble(good.getSvgPrice()));
			}

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

		if (order.getOrderType().equals("1")) {
			OrderType orderType = orderTypeService.getOrderType(Integer.valueOf(orderId));
			Distribution disteibution = distributionService.getDistribution(order.getCompanyId().getId());
			if (orderType != null) {
				grid.addProperties("type", orderType.getType());
				if (!StringUtil.isEmpty(orderType.getPrice())) {
					grid.addProperties("shipTime",
							orderType.getShippingTime() == null ? "" : orderType.getShippingTime());
					grid.addProperties("boxPrice", boxPrice);
					grid.addProperties("distributionPrice", orderType.getPrice());
				} else {
					grid.addProperties("boxPrice", boxPrice);
				}
				if (!StringUtil.isEmpty(orderType.getWay())) {
					grid.addProperties("way", orderType.getWay());
				} else {
					grid.addProperties("way", "商家配送");
				}
				if (orderType.getAddressId() != null) {
					Address address = orderType.getAddressId();
					grid.addProperties("userAddress", address.getAddress() + address.getRoom());
					grid.addProperties("userName", address.getName());
					grid.addProperties("userPhone", address.getPhone());

					String ntotal = order.getTotal() == null ? "" : order.getTotal();
					String npay = order.getPay() == null ? "" : order.getPay();

					double subTotal = Double.valueOf(ntotal) - Double.valueOf(disteibution.getDistributionPrice());
					double subPay = Double.valueOf(npay) - Double.valueOf(disteibution.getDistributionPrice());

					grid.addProperties("total", dFormat.format(subTotal));
					grid.addProperties("pay", dFormat.format(subPay));

				} else {
					grid.addProperties("userAddress", "用户自取");
					grid.addProperties("userName", "用户自取");
					grid.addProperties("userPhone", "用户自取");
				}
			} else if (disteibution != null) {
				grid.addProperties("distributionPrice",
						disteibution.getDistributionPrice() == null ? "0" : disteibution.getDistributionPrice());
				grid.addProperties("boxPrice", boxPrice);
			}
		}

		if (order.getOrderType().equals("2")) {
			Cate cate = cateService.getCate(Integer.valueOf(orderId));
			Delicacy delicacy = delicacyService.getDelicacy(order.getCompanyId().getId());
			if (cate != null) {
				if (delicacy != null) {
					grid.addProperties("mealFee", dFormat
							.format(Double.valueOf(cate.getMeals()) * Double.valueOf(delicacy.getMealFee())));
					grid.addProperties("fee", delicacy.getMealFee());
				}
				if (relatingList.size() == 0) {
					if (!StringUtil.isEmpty(cate.getPrice())) {
						grid.addProperties("deposit", cate.getPrice());
					}
				} else {
					grid.addProperties("deposit", 0);
				}

				Reserve reserve = reserveService.find(Integer.valueOf(cate.getReserveId()));

				if (reserve == null) {
					message.addProperty("message", "餐桌信息不存在");
					message.addProperty("success", false);
					new PushJson().outString(message.toJSonString(), response);
					return;
				}
				if (StringUtil.isEmpty(reserve.getCancelTime())) {
					grid.addProperties("shipTime", "");
				} else {
					grid.addProperties("shipTime", reserve.getCancelTime());
				}

				if (reserve.getSeat().equals("dt")) {
					grid.addProperties("tableNo", "大厅:" + reserve.getName());
				} else if (reserve.getSeat().equals("bx")) {
					grid.addProperties("tableNo", "包厢:" + reserve.getName() + "(" + reserve.getTableNo() + "号桌");
				}
				grid.addProperties("phone", cate.getPhone());
				grid.addProperties("createTime", cate.getCreateTime());
				grid.addProperties("repastTime", cate.getEndTime());
				grid.addProperties("meals", cate.getMeals());

				double subTotal = Double.valueOf(order.getTotal());
				double subPay = Double.valueOf(order.getPay());

				grid.addProperties("discount", dFormat.format(subTotal - subPay));
				grid.addProperties("remarks", order.getRemarks());

			}
		}
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 商家总交易统计
	@RequestMapping(params = "totalCount", method = RequestMethod.POST)
	public void receiveCompanyOrderCount(HttpServletRequest request, HttpServletResponse response,
			CompanyOrderCount companyOrderCount) {
		String companyId = request.getParameter("companyId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Object[] companyOrderCountList = companyOrderCountService.companyOrderCount(Integer.valueOf(companyId));

		if (companyOrderCountList == null) {
			message.addProperty("success", true);
			message.addProperty("balance", 0);
			message.addProperty("num", 0);
		} else {
			message.addProperty("success", true);
			message.addProperty("balance", companyOrderCountList[1]);
			message.addProperty("num", companyOrderCountList[0]);
		}

		new PushJson().outString(message.toJSonString(), response);
	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, CompanyOrderCount companyOrderCount, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyOrderCount.getId() + "") || companyOrderCount.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		companyOrderCountService.delete(companyOrderCount.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String orderId = request.getParameter("orderId");
		String balance = request.getParameter("balance");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(balance) || StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (!NumberUtils.isNumber(balance)) {
			message.addProperty("message", "金额不正确");
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

		Order order = orderService.getOrder(Integer.valueOf(orderId));
		if (order == null) {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		CompanyOrderCount orderCount = new CompanyOrderCount();
		orderCount.setBalance(balance);
		orderCount.setDate(date);
		orderCount.setCompanyId(company);
		orderCount.setOrderId(orderId);
		companyOrderCountService.saveORupdate(orderCount);
		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 商家线下订单列表 2018-11-02 @Tyy
	@RequestMapping(params = "getInList", method = RequestMethod.POST)
	public void getInList(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("companyId");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<InsideOrder> list = insideOrderService.getBaseList(Integer.valueOf(companyId), startTime, endTime);
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		for (InsideOrder order : list) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("total", order.getPay());
			record.addColumn("orderNo", order.getOrderNo());
			record.addColumn("addTime", order.getAddTime());
			record.addColumn("id", order.getId());
			ComputerCate cate = computerCateService.getCate(order.getId());
			if (cate != null) {
				if (cate.getSeat().equals("dt")) {
					record.addColumn("tableNo", "大厅:" + cate.getName());
				} else if (cate.getSeat().equals("bx")) {
					record.addColumn("tableNo", "包厢:" + cate.getName() + cate.getTableNo() + "号桌");
				}
			}
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", list.size());
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 商家线下订单详情
	@RequestMapping(params = "getInOrder", method = RequestMethod.POST)
	public void getInOrder(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		InsideOrder order = insideOrderService.getInsideOrder(Integer.valueOf(orderId));
		if (order == null) {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		DecimalFormat dFormat = new DecimalFormat("######0.00");
		double sTotal = Double.valueOf(order.getTotal());
		double sPay = Double.valueOf(order.getPay());
		grid.addProperties("orderNo", order.getOrderNo());
		grid.addProperties("companyPhone", order.getCompanyId().getPhone());
		grid.addProperties("finishTime", order.getFinishTime());
		grid.addProperties("remarks", order.getRemarks() == null ? "" : order.getRemarks());
		grid.addProperties("total", dFormat.format(sTotal));
		grid.addProperties("pay", dFormat.format(sPay));
		grid.addProperties("discount", dFormat.format(sTotal - sPay));

		List<Relating> relatingList = relatingService.getinsertrelating(Integer.valueOf(order.getId()));
		Double boxPrice = 0d;
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
			if (StringUtil.isEmpty(good.getSvgPrice())) {
				record.addColumn("price", good.getPrice());
			} else {
				record.addColumn("price", good.getSvgPrice());
			}
			record.addColumn("number", relating.getNumb());

			if (StringUtil.isEmpty(good.getSvgPrice())) {
				record.addColumn("subtotal", relating.getNumb() * Double.parseDouble(good.getPrice()));
			} else {
				record.addColumn("subtotal", relating.getNumb() * Double.parseDouble(good.getSvgPrice()));
			}

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

		ComputerCate cate = computerCateService.getCate(Integer.valueOf(orderId));
		Delicacy delicacy = delicacyService.getDelicacy(order.getCompanyId().getId());
		if (cate != null) {
			grid.addProperties("deposit", 0);
			grid.addProperties("meals", cate.getMeals());
			if (delicacy != null) {
				grid.addProperties("mealFee", dFormat
						.format(Double.valueOf(cate.getMeals()) * Double.valueOf(delicacy.getMealFee())));
				grid.addProperties("fee", delicacy.getMealFee());
			}

			Reserve reserve = reserveService.find(Integer.valueOf(cate.getReserveId()));

			if (reserve == null) {
				message.addProperty("message", "餐桌信息不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}

			if (reserve.getSeat().equals("dt")) {
				grid.addProperties("tableNo", "大厅:" + reserve.getName());
			} else if (reserve.getSeat().equals("bx")) {
				grid.addProperties("tableNo", "包厢:" + reserve.getName() + "(" + reserve.getTableNo() + "号桌");
			}
			double subTotal = Double.valueOf(order.getTotal());
			double subPay = Double.valueOf(order.getPay());

			grid.addProperties("discount", dFormat.format(subTotal - subPay));
		}
		new PushJson().outString(grid.toJSonString("list"), response);

	}

}