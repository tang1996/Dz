package com.dz.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.BookTime;
import com.dz.entity.Cate;
import com.dz.entity.Company;
import com.dz.entity.ComputerCate;
import com.dz.entity.Delicacy;
import com.dz.entity.Goods;
import com.dz.entity.InsideOrder;
import com.dz.entity.Order;
import com.dz.entity.Relating;
import com.dz.entity.Reserve;
import com.dz.entity.Staff;
import com.dz.service.IBookTimeService;
import com.dz.service.ICateService;
import com.dz.service.ICompanyService;
import com.dz.service.IComputerCateService;
import com.dz.service.IDelicacyService;
import com.dz.service.IGoodsService;
import com.dz.service.IInsideOrderService;
import com.dz.service.IOrderService;
import com.dz.service.IRelatingService;
import com.dz.service.IReserveService;
import com.dz.service.IStaffService;
import com.dz.util.CollectMoneyUtils;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/relating")
public class RelatingController {

	@Autowired
	private IRelatingService relatingService;

	@Autowired
	private IGoodsService goodsService;

	@Autowired
	private ICateService cateService;

	@Autowired
	private IOrderService orderService;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private IReserveService reserveService;

	@Autowired
	private IStaffService staffService;

	@Autowired
	private IComputerCateService computerCateService;

	@Autowired
	private IInsideOrderService insideOrderService;

	@Autowired
	private IDelicacyService delicacyService;

	@Autowired
	private IBookTimeService bookTimeService;
	

	// 网络订单未订菜 YNW
	@RequestMapping(params = "netAddInside", method = RequestMethod.POST)
	public void netAddInside(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");// 商家id

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Order order = orderService.getOrder(Integer.valueOf(orderId));

		Company company = companyService.getCompany(Integer.valueOf(order.getCompanyId().getId()));
		if (company == null) {
			message.addProperty("message", "company不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Cate cate = cateService.getCate(Integer.valueOf(orderId));
		if (cate == null) {
			message.addProperty("message", "cate不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Reserve reserve = reserveService.find(Integer.valueOf(cate.getReserveId()));
		if (reserve == null) {
			message.addProperty("message", "餐桌不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (cate.getInsideOrderId() == null) {
			InsideOrder insideOrder = new InsideOrder(); // 生成线下订单
			insideOrder.setCompanyId(company);
			insideOrder.setPayStatus("0");
			String orderNo = "TS";
			insideOrder
					.setOrderNo(orderNo + company.getId() + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			insideOrder.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			insideOrder.setCustomId("1");
			insideOrder.setOrderStatus("unpay"); // 订单初始状态
			insideOrder.setIsAccount("0");
			insideOrderService.saveORupdate(insideOrder);

			cate.setInsideOrderId(insideOrder); // 将线上与线下关联
			cateService.saveORupdate(cate);

			ComputerCate insidecate = new ComputerCate();
			insidecate.setName(reserve.getName());
			insidecate.setCompanyId(company);
			insidecate.setSeat(reserve.getSeat());
			insidecate.setTableNo(reserve.getTableNo());
			insidecate.setReserveId(reserve.getId() + "");
			insidecate.setInsideorderId(insideOrder);
			insidecate.setMeals(cate.getMeals());
			Delicacy delicacy = delicacyService.getDelicacy(Integer.valueOf(order.getCompanyId().getId())); // 获得对应商家属性
			insidecate.setMealFee(delicacy.getMealFee()); // 获得对应商家的茶位费
			computerCateService.saveORupdate(insidecate);

			message.addProperty("insideOrderId", insideOrder.getId()); // ynw
		} else {
			message.addProperty("insideOrderId", cate.getInsideOrderId().getId()); // ynw
		}

		message.addProperty("doingMeals", cate.getMeals()); // 就餐人数
		message.addProperty("success", true);
		message.addProperty("message", "备注信息修改成功");
		new PushJson().outString(message.toJSonString(), response);
	}

	// 查询该台号打印菜品 ynw(线下)
	@RequestMapping(params = "insideGet", method = RequestMethod.POST)
	public void insideGet(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("companyid");
		String tableNo = request.getParameter("tableNo");
		String insideOrderId = request.getParameter("insideOrderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(tableNo) || StringUtil.isEmpty(companyId) || StringUtil.isEmpty(insideOrderId)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		ComputerCate cate = computerCateService.getCate(Integer.valueOf(insideOrderId));

		if (cate == null) {
			message.addProperty("message", "餐桌信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (cate.getInsideorderId() != null) {
			List<Relating> relatingList = relatingService.getrelatingbyinsideID(
					Integer.valueOf(cate.getInsideorderId().getId()), Integer.valueOf(companyId));

			Double price = 0d;

			for (Relating relating : relatingList) {
				JSonGridRecord recordc = new JSonGridRecord();
				Goods goods = goodsService.getGoods(relating.getGoodsId());

				recordc.addColumn("numb", relating.getNumb());
				if (!StringUtil.isEmpty(relating.getIsDel())) { // xm
					if (Integer.valueOf(relating.getIsDel()) > 0) {
						recordc.addColumn("isDel", relating.getIsDel());
						// recordc.addColumn("numb", "0"); //xm
					}
				}
				recordc.addColumn("goodsId", relating.getGoodsId());

				recordc.addColumn("name", goods.getName());
				price = price + (Double.valueOf(goods.getPrice()) * Integer.valueOf(relating.getNumb()));
				recordc.addColumn("price", goods.getPrice());
				if (StringUtil.isEmpty(goods.getSvgPrice())) {
					recordc.addColumn("svg_price", "0");
				} else {
					recordc.addColumn("svg_price", goods.getSvgPrice());
				}
				grid.addRecord(recordc);
			}

			DecimalFormat df = new DecimalFormat("#.00");
			grid.addProperties("totalCount", df.format(price));

			Cate nCate = cateService.getCateInside(Integer.valueOf(insideOrderId));
			if (nCate != null) {
				Order order = nCate.getOrderId();
				grid.addProperties("nPay", order.getPay());
				grid.addProperties("orderNo", order.getOrderNo());

				List<Relating> relatings = relatingService.getrelating(order.getId());

				Double tprice = 0d;
				for (Relating nrelating : relatings) {
					if (!StringUtil.isEmpty(nrelating.getIsDel())) {
						if (Integer.valueOf(nrelating.getIsDel()) > 0) {
							Goods goods = goodsService.getGoods(nrelating.getGoodsId());
							tprice = tprice
									+ (Double.valueOf(goods.getPrice()) * Integer.valueOf(nrelating.getIsDel()));
						}
					}
				}

				grid.addProperties("tprice", tprice);
			} else {
				grid.addProperties("nPay", "0");
				grid.addProperties("orderNo", "0");
			}

			new PushJson().outString(grid.toJSonString("list"), response);
		}

	}

	// 查看该台已打印的菜品 ynw(线上)
	@RequestMapping(params = "onsideGet", method = RequestMethod.POST)
	public void onsideGet(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("companyid");
		String tableNo = request.getParameter("tableNo");
		String orderId = request.getParameter("orderId");
		String name = request.getParameter("name");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(tableNo) || StringUtil.isEmpty(companyId) || StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		String seat = "bx";
		if (StringUtil.isEmpty(name)) {
			name = tableNo + "号桌";
			seat = "dt";
		}

		Reserve reserveForseat = reserveService.getTable(tableNo, Integer.valueOf(companyId));
		if (reserveForseat != null) {
			seat = reserveForseat.getSeat();
		}

		Cate cate = cateService.getCate(Integer.valueOf(orderId));

		if (cate == null) {
			message.addProperty("message", "餐桌信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (cate.getOrderId() != null) {
			List<Relating> relatingList = relatingService.getrelating(Integer.valueOf(orderId));

			Double price = 0d;

			for (Relating relating : relatingList) {
				JSonGridRecord recordc = new JSonGridRecord();
				recordc.addColumn("numb", relating.getNumb());
				if (!StringUtil.isEmpty(relating.getIsDel())) {
					if (Integer.valueOf(relating.getIsDel()) > 0) {
						recordc.addColumn("isDel", relating.getIsDel());
						recordc.addColumn("numb", "0");
					}
				}

				recordc.addColumn("goodsId", relating.getGoodsId());
				Goods goods = goodsService.getGoods(relating.getGoodsId());
				recordc.addColumn("name", goods.getName());
				price = price + (Double.valueOf(goods.getPrice()) * Integer.valueOf(relating.getNumb()));
				recordc.addColumn("price", goods.getPrice());
				if (StringUtil.isEmpty(goods.getSvgPrice())) {
					recordc.addColumn("svg_price", "0");
				} else {
					recordc.addColumn("svg_price", goods.getSvgPrice());
				}
				grid.addRecord(recordc);
			}

			DecimalFormat df = new DecimalFormat("#.00");
			grid.addProperties("totalCount", df.format(price));
			new PushJson().outString(grid.toJSonString("list"), response);
		}

	}

	// 查询该台号菜品ynw
	@RequestMapping(params = "insideDoing", method = RequestMethod.POST)
	public void insideDoing(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("companyId");
		String reservId = request.getParameter("reservId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(reservId) || StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		ComputerCate cate = computerCateService.getReserv(Integer.valueOf(reservId));

		if (cate == null) {
			message.addProperty("message", "餐桌信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		Delicacy delicacy = delicacyService.getDelicacy(Integer.valueOf(companyId)); // 得到商家对应的属性
		if (delicacy == null) {
			message.addProperty("message", "查询不到对应商家的属性");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (cate.getInsideorderId() != null) {
			if(cate.getInsideorderId().getOrderStatus().equals("unpay")){
				List<Relating> relatingList = relatingService.getrelating(Integer.valueOf(cate.getInsideorderId().getId()),
						Integer.valueOf(companyId));

				Double price = 0d;

				for (Relating relating : relatingList) {
					JSonGridRecord recordc = new JSonGridRecord();
					recordc.addColumn("goodsId", relating.getGoodsId());
					recordc.addColumn("numb", relating.getNumb());
					Goods goods = goodsService.getGoods(relating.getGoodsId());
					recordc.addColumn("name", goods.getName());
					price = price + (Double.valueOf(goods.getPrice()) * Integer.valueOf(relating.getNumb()));
					recordc.addColumn("price", goods.getPrice());
					recordc.addColumn("stock", goods.getStock());
					grid.addRecord(recordc);
				}
				DecimalFormat df = new DecimalFormat("#.00");
				grid.addProperties("totalCount", df.format(price));
				
				grid.addProperties("doingMeals", cate.getMeals()); // 得到就餐人数
				grid.addProperties("insideOrderId", cate.getInsideorderId().getId());
				grid.addProperties("mealFee", delicacy.getMealFee()); // 对应商家的餐位费

				grid.addProperties("success", true);
				new PushJson().outString(grid.toJSonString("list"), response);
			}else{
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
				List<BookTime> listbook = bookTimeService.getBookTime(Integer.valueOf(reservId), df.format(new Date()));
				if(listbook.size() > 0){
					BookTime book = listbook.get(0);
					int orderId = book.getOrderId();
					Cate ncate = cateService.getCate(orderId);
					
					List<Relating> relatingList = relatingService.getrelating(Integer.valueOf(ncate.getInsideOrderId().getId()),
							Integer.valueOf(companyId));

					Double price = 0d;

					for (Relating relating : relatingList) {
						JSonGridRecord recordc = new JSonGridRecord();
						recordc.addColumn("goodsId", relating.getGoodsId());
						recordc.addColumn("numb", relating.getNumb());
						Goods goods = goodsService.getGoods(relating.getGoodsId());
						recordc.addColumn("name", goods.getName());
						price = price + (Double.valueOf(goods.getPrice()) * Integer.valueOf(relating.getNumb()));
						recordc.addColumn("price", goods.getPrice());
						recordc.addColumn("stock", goods.getStock());
						grid.addRecord(recordc);
					}
					DecimalFormat tdf = new DecimalFormat("#.00");
					grid.addProperties("totalCount", tdf.format(price));
					
					grid.addProperties("doingMeals", ncate.getMeals()); // 得到就餐人数
					grid.addProperties("insideOrderId", ncate.getInsideOrderId().getId());
					grid.addProperties("mealFee", delicacy.getMealFee()); // 对应商家的餐位费

					grid.addProperties("success", true);
					new PushJson().outString(grid.toJSonString("list"), response);
				}
			}
		}
	}
	
	/*
	 * // 查询该台号菜品ynw
	 * 
	 * @RequestMapping(params = "insideDoing", method = RequestMethod.POST)
	 * public void insideDoing(HttpServletRequest request, HttpServletResponse
	 * response) {
	 * 
	 * String companyId = request.getParameter("companyId"); String reservId =
	 * request.getParameter("reservId");
	 * 
	 * JSonMessage message = new JSonMessage(); if (StringUtil.isEmpty(reservId)
	 * || StringUtil.isEmpty(companyId)) { message.addProperty("message",
	 * "id不能为空"); message.addProperty("success", false); new
	 * PushJson().outString(message.toJSonString(), response); return; }
	 * 
	 * JSonGrid grid = new JSonGrid(); grid.addProperties("success", true);
	 * 
	 * ComputerCate cate =
	 * computerCateService.getReserv(Integer.valueOf(reservId));
	 * 
	 * if (cate == null) { message.addProperty("message", "餐桌信息不存在");
	 * message.addProperty("success", false); new
	 * PushJson().outString(message.toJSonString(), response); return; } if
	 * (cate.getInsideorderId() != null) { List<Relating> relatingList =
	 * relatingService.getrelating(Integer.valueOf(cate.getInsideorderId().getId
	 * ()), Integer.valueOf(companyId));
	 * 
	 * Double price = 0d;
	 * 
	 * for (Relating relating : relatingList) { JSonGridRecord recordc = new
	 * JSonGridRecord(); recordc.addColumn("goodsId", relating.getGoodsId());
	 * recordc.addColumn("numb", relating.getNumb()); Goods goods =
	 * goodsService.getGoods(relating.getGoodsId()); recordc.addColumn("name",
	 * goods.getName()); price = price + (Double.valueOf(goods.getPrice()) *
	 * Integer.valueOf(relating.getNumb())); recordc.addColumn("price",
	 * goods.getPrice()); recordc.addColumn("stock", goods.getStock());
	 * grid.addRecord(recordc); } grid.addProperties("doingMeals",
	 * cate.getMeals()); // 得到就餐人数 DecimalFormat df = new DecimalFormat("#.00");
	 * grid.addProperties("totalCount", df.format(price));
	 * grid.addProperties("insideOrderId", cate.getInsideorderId().getId());
	 * grid.addProperties("success", true); new
	 * PushJson().outString(grid.toJSonString("list"), response); }
	 * 
	 * }
	 */

	// 接收goodsId数组,加菜线上批量 ynw
	@RequestMapping(params = "arrSaveOnside", method = RequestMethod.POST)
	public void arrSaveOnside(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("companyId");
		String tableNo = request.getParameter("tableNo");
		String name = request.getParameter("name");
		String numbs = request.getParameter("numb");
		String goodsIds = request.getParameter("goodsId");
		String orderId = request.getParameter("orderId");

		String natures = request.getParameter("nature"); // ynw

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(tableNo) || StringUtil.isEmpty(goodsIds)
				|| StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		String seat = "bx";
		if (StringUtil.isEmpty(name)) {
			name = tableNo + "号桌";
			seat = "dt";
		}

		Reserve reserveForseat = reserveService.getTable(tableNo, Integer.valueOf(companyId));
		if (reserveForseat != null) {
			seat = reserveForseat.getSeat();
		}

		String nameForseat = "";
		Reserve reserveForname = reserveService.getTable(tableNo, Integer.valueOf(companyId), seat);
		if (reserveForname != null) {
			nameForseat = reserveForname.getName();
		} else {
			if (StringUtil.isEmpty(name)) {
				nameForseat = tableNo + "号桌";
			}
		}

		Cate cate = cateService.getCate(Integer.valueOf(orderId)); // ynw
		if (cate == null) {
			message.addProperty("message", "餐桌信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		// 得到每个id
		String[] idArr = goodsIds.split(",");
		// 得到每个数量
		String[] numbArr = numbs.split(",");
		// 得到每个的备注 ynw
		String[] natureArr = natures.split(";");

		Map<String, Integer> mapIDNUM = new HashMap<String, Integer>(); // 将ID和数量绑定的数组
																		// ynw
		Map<String, Integer> mapIDNUA = new HashMap<String, Integer>(); // 将ID和备注绑定的数组
																		// ynw
		for (int i = 0; i < idArr.length; i++) {
			mapIDNUM.put(idArr[i], Integer.valueOf(numbArr[i])); // 将ID和数量绑定 ynw
		}

		if (cate.getOrderId() != null) {
			for (String goodsId : mapIDNUM.keySet()) {

				Order order = cate.getOrderId();

				List<Relating> relatingList = relatingService.getMenus(order.getId(), Integer.valueOf(goodsId),
						Integer.valueOf(companyId));

				if (relatingList.size() > 0) {
					Relating relating = relatingList.get(0);

					int count = relating.getNumb() + Integer.valueOf(mapIDNUM.get(goodsId));

					relating.setNumb(count);

					relatingService.saveORupdate(relating);
				} else {
					Relating relating = new Relating();
					relating.setCompanyId(Integer.valueOf(companyId));
					relating.setGoodsId(Integer.valueOf(goodsId));
					relating.setOrderId(order);
					relating.setNumb(Integer.valueOf(mapIDNUM.get(goodsId)));
					relatingService.saveORupdate(relating);
				}

				// 加菜成功后,菜品库存-1
				Goods goods = goodsService.getGoods(Integer.valueOf(goodsId));
				if (goods != null) {
					if (Integer.valueOf(goods.getStock()) > 0) {
						goods.setStock(Integer.toString(
								(Integer.valueOf(goods.getStock()) - Integer.valueOf(mapIDNUM.get(goodsId)))));
						goodsService.update(goods);
					}
				}
				message.addProperty("orderId", order.getId());
				message.addProperty("message", "添加或修改信息成功");
				message.addProperty("success", true);
				new PushJson().outString(message.toJSonString(), response);
			}
		} else {
			message.addProperty("message", "开桌参数丢失");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
		}

	}

	// 线上批量退菜 ynw
	@RequestMapping(params = "arrDelOnside", method = RequestMethod.POST)
	public void arrDelOnside(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("companyId");
		String tableNo = request.getParameter("tableNo");
		String numbs = request.getParameter("numb");
		String goodsIds = request.getParameter("goodsId");
		String OrderId = request.getParameter("OrderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(tableNo) || StringUtil.isEmpty(goodsIds)
				|| StringUtil.isEmpty(OrderId)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Cate cate = cateService.getCate(Integer.valueOf(OrderId));

		if (cate == null) {
			message.addProperty("message", "餐桌信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		// 得到每个id
		String[] idArr = goodsIds.split(",");
		// 得到每个数量
		String[] numbArr = numbs.split(",");
		// 得到每个的备注 ynw

		Map<String, Integer> mapIDNUM = new HashMap<String, Integer>(); // 将ID和数量绑定的数组
																		// ynw
		for (int i = 0; i < idArr.length; i++) {
			mapIDNUM.put(idArr[i], Integer.valueOf(numbArr[i])); // 将ID和数量绑定 ynw
		}
		if (cate.getOrderId() != null) {
			for (String goodsId : mapIDNUM.keySet()) {

				Order order = cate.getOrderId();

				List<Relating> relatingList = relatingService.getMenus(order.getId(), Integer.valueOf(goodsId),
						Integer.valueOf(companyId)); // 用线下ID

				if (relatingList.size() > 0) { // 减去的数目
					Relating relating = relatingList.get(0);
					if ((Integer.valueOf(mapIDNUM.get(goodsId))) <= relating.getNumb()
							&& (Integer.valueOf(mapIDNUM.get(goodsId))) >= 0) {
						int count = relating.getNumb() - Integer.valueOf(mapIDNUM.get(goodsId));
						if (count == 0) {
							relating.setIsDel(relating.getNumb() + "");
							relatingService.saveORupdate(relating);
						} else {
							int scount = relating.getNumb() - count;
							relating.setIsDel(scount + "");
							relating.setNumb(count);
							relatingService.saveORupdate(relating);
						}
					} else {
						message.addProperty("message", "退菜数量参数异常");
						message.addProperty("success", false);
						new PushJson().outString(message.toJSonString(), response);
						return;
					}
				}

				// 退菜成功后,菜品库存+相应数量
				Goods goods = goodsService.getGoods(Integer.valueOf(goodsId));
				if (goods != null) {
					if (Integer.valueOf(goods.getStock()) >= 0) {
						goods.setStock(Integer.toString(
								(Integer.valueOf(goods.getStock()) + Integer.valueOf(mapIDNUM.get(goodsId)))));
						goodsService.update(goods);
					}
				}

				message.addProperty("message", "修改信息成功");
				message.addProperty("success", true);
				new PushJson().outString(message.toJSonString(), response);
			}
		}

	}

	// 线下批量退菜 ynw
	@RequestMapping(params = "arrDelInside", method = RequestMethod.POST)
	public void arrDelInside(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("companyId");
		String tableNo = request.getParameter("tableNo");
		String name = request.getParameter("name");
		String numbs = request.getParameter("numb");
		String goodsIds = request.getParameter("goodsId");
		String insideOrderId = request.getParameter("insideOrderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(tableNo) || StringUtil.isEmpty(goodsIds)
				|| StringUtil.isEmpty(insideOrderId)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		String seat = "bx";
		if (StringUtil.isEmpty(name)) {
			name = tableNo + "号桌";
			seat = "dt";
		}

		/* ynw */
		Reserve reserveForseat = reserveService.getTable(tableNo, Integer.valueOf(companyId));
		if (reserveForseat != null) {
			seat = reserveForseat.getSeat();
		}

		String nameForseat = "";
		Reserve reserveForname = reserveService.getTable(tableNo, Integer.valueOf(companyId), seat);
		if (reserveForname != null) {
			nameForseat = reserveForname.getName();
		} else {
			if (StringUtil.isEmpty(name)) {
				nameForseat = tableNo + "号桌";
			}
		}

		ComputerCate cate = computerCateService.getCate(Integer.valueOf(insideOrderId));

		if (cate == null) {
			message.addProperty("message", "餐桌信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		// 得到每个id
		String[] idArr = goodsIds.split(",");
		// 得到每个数量
		String[] numbArr = numbs.split(",");
		// 得到每个的备注 ynw

		Map<String, Integer> mapIDNUM = new HashMap<String, Integer>(); // 将ID和数量绑定的数组
																		// ynw
		for (int i = 0; i < idArr.length; i++) {
			mapIDNUM.put(idArr[i], Integer.valueOf(numbArr[i])); // 将ID和数量绑定 ynw
		}
		if (cate.getInsideorderId() != null) {
			for (String goodsId : mapIDNUM.keySet()) {

				InsideOrder order = cate.getInsideorderId();

				List<Relating> relatingList = relatingService.getinsideMenus(order.getId(), Integer.valueOf(goodsId),
						Integer.valueOf(companyId)); // 用线下ID

				if (relatingList.size() > 0) { // 减去的数目
					Relating relating = relatingList.get(0);
					if ((Integer.valueOf(mapIDNUM.get(goodsId))) <= relating.getNumb()
							&& (Integer.valueOf(mapIDNUM.get(goodsId))) >= 0) {
						int count = relating.getNumb() - Integer.valueOf(mapIDNUM.get(goodsId));
						if (count == 0) {
							if (!StringUtil.isEmpty(relating.getIsDel())) { // xm
								if (Integer.valueOf(relating.getIsDel()) >= 0) {
									relating.setIsDel(Integer.valueOf(relating.getNumb())
											+ Integer.valueOf(relating.getIsDel()) + "");
								}
							} else {
								relating.setIsDel(relating.getNumb() + "");
							}
							relating.setNumb(Integer.valueOf(0));
							relatingService.saveORupdate(relating);
						} else {
							int scount = relating.getNumb() - count;
							if (!StringUtil.isEmpty(relating.getIsDel())) { // xm
								if (Integer.valueOf(relating.getIsDel()) >= 0) {
									relating.setIsDel(
											Integer.valueOf(scount) + Integer.valueOf(relating.getIsDel()) + "");
								}
							} else {
								relating.setIsDel(scount + "");
							}

							relating.setNumb(count);
							relatingService.saveORupdate(relating);
						}
					} else {
						message.addProperty("message", "退菜数量参数异常");
						message.addProperty("success", false);
						new PushJson().outString(message.toJSonString(), response);
						return;
					}
				}

				// 退菜成功后,菜品库存+相应数量
				Goods goods = goodsService.getGoods(Integer.valueOf(goodsId));
				if (goods != null) {
					if (Integer.valueOf(goods.getStock()) >= 0) {
						goods.setStock(Integer.toString(
								(Integer.valueOf(goods.getStock()) + Integer.valueOf(mapIDNUM.get(goodsId)))));
						goodsService.update(goods);
					}
				}

				message.addProperty("message", "修改信息成功");
				message.addProperty("success", true);
				new PushJson().outString(message.toJSonString(), response);
			}
		}

	}

	/*
	 * // 线下批量退菜 ynw
	 * 
	 * @RequestMapping(params = "arrDelInside", method = RequestMethod.POST)
	 * public void arrDelInside(HttpServletRequest request, HttpServletResponse
	 * response) {
	 * 
	 * String companyId = request.getParameter("companyId"); String tableNo =
	 * request.getParameter("tableNo"); String name =
	 * request.getParameter("name"); String numbs =
	 * request.getParameter("numb"); String goodsIds =
	 * request.getParameter("goodsId"); String insideOrderId =
	 * request.getParameter("insideOrderId");
	 * 
	 * JSonMessage message = new JSonMessage(); if
	 * (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(tableNo) ||
	 * StringUtil.isEmpty(goodsIds) || StringUtil.isEmpty(insideOrderId)) {
	 * message.addProperty("message", "id不能为空"); message.addProperty("success",
	 * false); new PushJson().outString(message.toJSonString(), response);
	 * return; }
	 * 
	 * String seat = "bx"; if (StringUtil.isEmpty(name)) { name = tableNo +
	 * "号桌"; seat = "dt"; }
	 * 
	 * ynw Reserve reserveForseat = reserveService.getTable(tableNo,
	 * Integer.valueOf(companyId)); if (reserveForseat != null) { seat =
	 * reserveForseat.getSeat(); }
	 * 
	 * String nameForseat = ""; Reserve reserveForname =
	 * reserveService.getTable(tableNo, Integer.valueOf(companyId), seat); if
	 * (reserveForname != null) { nameForseat = reserveForname.getName(); } else
	 * { if (StringUtil.isEmpty(name)) { nameForseat = tableNo + "号桌"; } }
	 * 
	 * ComputerCate cate =
	 * computerCateService.getCate(Integer.valueOf(insideOrderId));
	 * 
	 * if (cate == null) { message.addProperty("message", "餐桌信息不存在");
	 * message.addProperty("success", false); new
	 * PushJson().outString(message.toJSonString(), response); return; }
	 * 
	 * // 得到每个id String[] idArr = goodsIds.split(","); // 得到每个数量 String[]
	 * numbArr = numbs.split(","); // 得到每个的备注 ynw
	 * 
	 * Map<String, Integer> mapIDNUM = new HashMap<String, Integer>(); //
	 * 将ID和数量绑定的数组 // ynw for (int i = 0; i < idArr.length; i++) {
	 * mapIDNUM.put(idArr[i], Integer.valueOf(numbArr[i])); // 将ID和数量绑定 ynw } if
	 * (cate.getInsideorderId() != null) { for (String goodsId :
	 * mapIDNUM.keySet()) {
	 * 
	 * InsideOrder order = cate.getInsideorderId();
	 * 
	 * List<Relating> relatingList =
	 * relatingService.getinsideMenus(order.getId(), Integer.valueOf(goodsId),
	 * Integer.valueOf(companyId)); // 用线下ID
	 * 
	 * if (relatingList.size() > 0) { // 减去的数目 Relating relating =
	 * relatingList.get(0); if ((Integer.valueOf(mapIDNUM.get(goodsId))) <=
	 * relating.getNumb() && (Integer.valueOf(mapIDNUM.get(goodsId))) >= 0) {
	 * int count = relating.getNumb() - Integer.valueOf(mapIDNUM.get(goodsId));
	 * if (count == 0) { relatingService.delete(relating.getId() + ""); } else {
	 * relating.setNumb(count); relatingService.saveORupdate(relating); } } else
	 * { message.addProperty("message", "退菜数量参数异常");
	 * message.addProperty("success", false); new
	 * PushJson().outString(message.toJSonString(), response); return; } }
	 * 
	 * // 退菜成功后,菜品库存+相应数量 Goods goods =
	 * goodsService.getGoods(Integer.valueOf(goodsId)); //
	 * if (goods != null) { if
	 * (Integer.valueOf(goods.getStock()) >= 0) {
	 * goods.setStock(Integer.toString( (Integer.valueOf(goods.getStock()) +
	 * Integer.valueOf(mapIDNUM.get(goodsId))))); goodsService.update(goods); }
	 * }
	 * 
	 * message.addProperty("message", "修改信息成功"); message.addProperty("success",
	 * true); new PushJson().outString(message.toJSonString(), response); } }
	 * 
	 * }
	 */

	// 接收goodsId数组,加菜 线下电脑端 ynw
	@RequestMapping(params = "arrSaveInside", method = RequestMethod.POST)
	public void arrSaveInside(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("companyId");
		String tableNo = request.getParameter("tableNo");
		String numbs = request.getParameter("numb");
		String goodsIds = request.getParameter("goodsId");
		String insideOrderId = request.getParameter("insideOrderId");
		String natureList = request.getParameter("nature");//2018-10-24 @Tyy

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(tableNo) || StringUtil.isEmpty(goodsIds)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		ComputerCate cate = computerCateService.getCate(Integer.valueOf(insideOrderId)); // ynw
		if (cate == null) {
			message.addProperty("message", "餐桌信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		// 得到每个id
		String[] idArr = goodsIds.split(",");
		// 得到每个数量
		String[] numbArr = numbs.split(",");
		//2018-10-24 @Tyy start
		String[] nature = null;
		if(!StringUtil.isEmpty(natureList)){
			if (natureList.length() > 0) {
				nature = natureList.split(";");
			}
		}
		//end

		Map<String, Integer> mapIDNUM = new HashMap<String, Integer>(); // 将ID和数量绑定的数组

		for (int i = 0; i < idArr.length; i++) {
			mapIDNUM.put(idArr[i], Integer.valueOf(numbArr[i])); // 将ID和数量绑定 ynw
		}

		if (cate.getInsideorderId() != null) {
			for (int i = 0; i < idArr.length; i++) {
				InsideOrder order = cate.getInsideorderId();
				List<Relating> relatingList = relatingService.getinsideMenus(order.getId(), Integer.valueOf(idArr[i].toString()),
						Integer.valueOf(companyId)); // 用线下ID
				if (relatingList.size() > 0) {
					Relating relating = relatingList.get(0);
					int count = relating.getNumb() + Integer.valueOf(mapIDNUM.get(idArr[i]));
					relating.setNumb(count);
					//2018-10-24 @Tyy start
					if (nature != null) {
						if (nature[i] != null) {
							relating.setRemark(nature[i]);
						}
					}
					//end
					relatingService.saveORupdate(relating);
				} else {
					// Order netOrder =
					// orderService.getOrder(Integer.valueOf(orderId));
					Relating relating = new Relating();
					relating.setCompanyId(Integer.valueOf(companyId));
					relating.setGoodsId(Integer.valueOf(idArr[i].toString()));
//					relating.setOrderId(netOrder);
//					if (nature != null) {
//						if (nature[i] != null) {
//							relating.setRemark(nature[i]);
//						}
//					}
					relating.setInsideOrderId(order);
					relating.setNumb(Integer.valueOf(mapIDNUM.get(idArr[i].toString())));
					relatingService.saveORupdate(relating);
				}

				// 加菜成功后,菜品库存-1
				Goods goods = goodsService.getGoods(Integer.valueOf(idArr[i].toString()));
				if (goods != null) {
					if (Integer.valueOf(goods.getStock()) > 0) {
						goods.setStock(Integer.toString(
								(Integer.valueOf(goods.getStock()) - Integer.valueOf(mapIDNUM.get(idArr[i].toString())))));
						goodsService.update(goods);
					}
				}
				message.addProperty("insideorderId", order.getId());
				message.addProperty("message", "添加或修改信息成功");
				message.addProperty("success", true);
				new PushJson().outString(message.toJSonString(), response);
			}
		} else {
			message.addProperty("message", "开桌参数丢失");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
		}
	}

	// 结算 - 收银
	@RequestMapping(value = "/checkMoney", method = RequestMethod.POST)
	public void checkMoney(HttpServletRequest request, HttpServletResponse response) {

		String orderId = request.getParameter("orderId");
		String cid = request.getParameter("companyid");
		String token = request.getParameter("token");
		String recode = request.getParameter("recode");

		synchronized (this) {
			JSonMessage message = new JSonMessage();
			if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(cid) || StringUtil.isEmpty(token)
					|| StringUtil.isEmpty(recode)) {
				message.addProperty("message", "缺少必要参数");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			try {
				Cate cate = cateService.getCate(Integer.valueOf(orderId));
				if (cate == null) {
					message.addProperty("message", "订单信息不存在");
					message.addProperty("success", false);
					new PushJson().outString(message.toJSonString(), response);
					return;
				}

				if (recode.equals(cate.getRecode())) {
					Company company = companyService.getCompany(Integer.valueOf(cid));
					if (company != null) {
						String mon = company.getMonSales();
						if (mon != null) {
							company.setMonSales(String.valueOf(Integer.valueOf(mon) + 1));
						} else {
							company.setMonSales(String.valueOf(1));
						}
						companyService.saveORupdate(company);
					}

					// 完成状态更改
					if (cate.getOrderId() != null) {
						Order order = cate.getOrderId();
						if (order.getOrderStatus().equals("doing")) {
							order.setOrderStatus("finish");
							order.setIsAccount("1");
							order.setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

							// 清除点菜记录
							/*
							 * List<Relating> relatingList =
							 * relatingService.getrelating(order .getId(),
							 * Integer.valueOf(cid)); for (Relating relating :
							 * relatingList) {
							 * relatingService.delete(String.valueOf(relating.
							 * getId())); }
							 */

							// 支付给商家
							if (!StringUtil.isEmpty(company.getOpenId())) {
								double total = Double.valueOf(order.getPay()) * 100;
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
						} else {
							message.addProperty("message", "状态已变更");
							message.addProperty("success", false);
							new PushJson().outString(message.toJSonString(), response);
							return;
						}
					} else {
						message.addProperty("message", "订单不存在");
						message.addProperty("success", false);
						new PushJson().outString(message.toJSonString(), response);
						return;
					}

					// 桌子状态调整
					Reserve reserve = reserveService.find(Integer.valueOf(cate.getReserveId()));
					List<Order> list = cateService.cateRes(cate.getReserveId());
					if (list.size() > 1) {
						reserve.setStatus("1");
					} else {
						reserve.setStatus("0");
					}

					reserveService.saveORupdate(reserve);

					message.addProperty("message", "结算完成.");
					message.addProperty("success", true);
					new PushJson().outString(message.toJSonString(), response);
				} else {
					message.addProperty("message", "收银码不正确.");
					message.addProperty("success", false);
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

	// 查询该台号菜品
	@RequestMapping(params = "get", method = RequestMethod.POST)
	public void get(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("companyid");
		String tableNo = request.getParameter("tableNo");
		String name = request.getParameter("name");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(tableNo) || StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		String seat = "bx";
		if (StringUtil.isEmpty(name)) {
			name = tableNo + "号桌";
			seat = "dt";
		}

		Cate cate = cateService.getCate(tableNo, Integer.valueOf(companyId), name, seat);

		if (cate == null) {
			message.addProperty("message", "餐桌信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (cate.getOrderId() != null) {
			List<Relating> relatingList = relatingService.getrelating(Integer.valueOf(cate.getOrderId().getId()),
					Integer.valueOf(companyId));

			Double price = 0d;

			for (Relating relating : relatingList) {
				JSonGridRecord recordc = new JSonGridRecord();
				recordc.addColumn("numb", relating.getNumb());
				Goods goods = goodsService.getGoods(relating.getGoodsId());
				recordc.addColumn("name", goods.getName());
				price = price + (Double.valueOf(goods.getPrice()) * Integer.valueOf(relating.getNumb()));
				recordc.addColumn("price", goods.getPrice());
				grid.addRecord(recordc);
			}

			DecimalFormat df = new DecimalFormat("#.00");
			grid.addProperties("totalCount", df.format(price));
			new PushJson().outString(grid.toJSonString("list"), response);
		}

	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Relating relating, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(relating.getId() + "") || relating.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		relatingService.delete(relating.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, Relating relating, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		relatingService.saveORupdate(relating);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 单个添加
	@RequestMapping(params = "newSave", method = RequestMethod.POST)
	public void newSave(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("companyId");
		String tableNo = request.getParameter("tableNo");
		String name = request.getParameter("name");
		String goodsId = request.getParameter("goodsId");
		String numb = request.getParameter("numb");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(tableNo) || StringUtil.isEmpty(goodsId)
				|| StringUtil.isEmpty(numb)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		String seat = "bx";
		if (StringUtil.isEmpty(name)) {
			name = tableNo + "号桌";
			seat = "dt";
		}
		Cate cate = cateService.getCate(tableNo, Integer.valueOf(companyId), name, seat);
		if (cate == null) {
			message.addProperty("message", "餐桌信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (cate.getOrderId() != null) {

			Order order = cate.getOrderId();

			List<Relating> relatingList = relatingService.getMenus(order.getId(), Integer.valueOf(goodsId),
					Integer.valueOf(companyId));

			if (relatingList.size() > 0) {
				Relating relating = relatingList.get(0);
				int count = relating.getNumb() + Integer.valueOf(numb);
				relating.setNumb(count);

				relatingService.saveORupdate(relating);
			} else {
				Relating relating = new Relating();

				relating.setCompanyId(Integer.valueOf(companyId));
				relating.setGoodsId(Integer.valueOf(goodsId));
				relating.setOrderId(order);
				relating.setNumb(Integer.valueOf(numb));

				relatingService.saveORupdate(relating);
			}

			message.addProperty("message", "添加或修改信息成功");
			message.addProperty("success", true);
			new PushJson().outString(message.toJSonString(), response);
		}
	}

	// 批量添加
	@RequestMapping(value = "/batchSave", method = RequestMethod.POST)
	public void batchSave(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("companyId");
		String tableNo = request.getParameter("tableNo");
		String seat = request.getParameter("seat");
		String name = request.getParameter("name");
		String content = request.getParameter("content");
		String token = request.getParameter("token");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(tableNo) || StringUtil.isEmpty(content)
				|| StringUtil.isEmpty(seat) || StringUtil.isEmpty(name) || StringUtil.isEmpty(token)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Staff staff = staffService.gettoken(token);
		if (staff == null) {
			message.addProperty("message", "服务员不存在");
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

		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		Order order = new Order();
		order.setCompanyId(company);
		order.setPayStatus("0");
		String orderNo = "MD";
		order.setOrderNo(orderNo + company.getId() + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		order.setAddTime(date);
		order.setCustomId("1");
		order.setOrderType("3");
		order.setOrderStatus("unpay");
		order.setRemarks("服务员人工点餐");
		orderService.saveORupdate(order);

		Cate cate = new Cate();
		cate.setCompanyId(company);
		cate.setCreateTime(date);
		cate.setName(name);
		cate.setSeat(seat);
		cate.setOrderId(order);
		cate.setTableNo(tableNo);
		cateService.saveORupdate(cate);

		double total = 0;
		String[] arr = content.split(";");
		for (int i = 0; i < arr.length; i++) {
			String[] goodInfo = arr[i].toString().split(",");
			String goodId = goodInfo[0];
			String num = goodInfo[1];
			Goods goods = goodsService.getGoods(Integer.valueOf(goodId));
			if (goods == null) {
				message.addProperty("message", "商品不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			Relating relating = new Relating();
			relating.setCompanyId(Integer.valueOf(companyId));
			relating.setGoodsId(Integer.valueOf(goodId));
			relating.setOrderId(order);
			relating.setNumb(Integer.valueOf(num));
			relatingService.saveORupdate(relating);
			double price = 0;
			if (!StringUtil.isEmpty(goods.getSvgPrice())) {
				price = Double.valueOf(goods.getSvgPrice());
			} else {
				price = Double.valueOf(goods.getPrice());
			}
			total = total + price * Double.valueOf(num);
		}
		Order theOrder = orderService.getOrder(order.getId());
		theOrder.setTotal(total + "");
		orderService.saveORupdate(theOrder);

		// 桌子状态调整
		Reserve reserve = reserveService.getTable(tableNo, Integer.valueOf(companyId), seat, name);
		if (!reserve.getStatus().equals("0")) {
			message.addProperty("message", "餐桌状态不可用");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		reserve.setStatus("2");
		reserveService.saveORupdate(reserve);

		message.addProperty("message", "添加订单成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 减菜
	@RequestMapping(value = "/doDelete", method = RequestMethod.POST)
	public void doDelete(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		String goodsId = request.getParameter("goodsId");
		String num = request.getParameter("num");
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(goodsId) || StringUtil.isEmpty(num)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Relating relating = relatingService.getRelating(Integer.valueOf(orderId), Integer.valueOf(goodsId));

		if (relating == null) {
			message.addProperty("message", "订单信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (!NumberUtils.isNumber(num)) {
			message.addProperty("message", "要减少的数量不正确");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (Integer.valueOf(num) == relating.getNumb()) {
			relatingService.delete(relating.getId() + "");
		} else if (Integer.valueOf(num) < relating.getNumb()) {
			relating.setNumb(relating.getNumb() - Integer.valueOf(num));
			relatingService.saveORupdate(relating);
		} else {
			message.addProperty("message", "减少数量大于预定份量");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		message.addProperty("message", "减菜成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 加菜
	@RequestMapping(value = "/doSave", method = RequestMethod.POST)
	public void doSave(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		String goodsId = request.getParameter("goodsId");
		String num = request.getParameter("num");
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(goodsId) || StringUtil.isEmpty(num)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Relating relating = relatingService.getRelating(Integer.valueOf(orderId), Integer.valueOf(goodsId));

		if (relating == null) {
			message.addProperty("message", "订单信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (!NumberUtils.isNumber(num)) {
			message.addProperty("message", "要增加的数量不正确");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		relating.setNumb(relating.getNumb() + Integer.valueOf(num));
		relatingService.saveORupdate(relating);

		message.addProperty("message", "加菜成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}
}