package com.dz.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.BookTime;
import com.dz.entity.Cate;
import com.dz.entity.ComputerCate;
import com.dz.entity.Delicacy;
import com.dz.entity.Order;
import com.dz.entity.Relating;
import com.dz.entity.Reserve;
import com.dz.service.IBookTimeService;
import com.dz.service.ICateService;
import com.dz.service.IComputerCateService;
import com.dz.service.IDelicacyService;
import com.dz.service.IOrderService;
import com.dz.service.IRelatingService;
import com.dz.service.IReserveService;
import com.dz.util.DateUtils;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/bookTime")
public class BookTimeController {

	@Autowired
	private IBookTimeService bookTimeService;

	@Autowired
	private ICateService cateService;

	@Autowired
	private IDelicacyService delicacyService;

	@Autowired
	private IReserveService reserveService;

	@Autowired
	private IOrderService orderService;

	@Autowired
	private IRelatingService relatingService;

	@Autowired
	private IComputerCateService computercateService;

	// 线下餐桌调整人数 ynw
	@RequestMapping(params = "keepPeople", method = RequestMethod.POST)
	public void keepPeople(HttpServletRequest request, HttpServletResponse response) {

		String OrderId = request.getParameter("OrderId");
		String doingMeals = request.getParameter("meals");

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(OrderId) || StringUtil.isEmpty(doingMeals)) {
			message.addProperty("message", "找不到线下订单号");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (StringUtil.isEmpty(doingMeals) || Integer.valueOf(doingMeals) == 0) {
			message.addProperty("message", "调整人数参数有误");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		long timeLose = System.currentTimeMillis(); // 当天时间

		SimpleDateFormat dateStart = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String newDateSTart = dateStart.format(new Date(timeLose));

		SimpleDateFormat dateEnd = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
		String newDateEnd = dateEnd.format(new Date(timeLose));

		List<BookTime> booktime = bookTimeService.getBookTime(Integer.valueOf(OrderId), newDateSTart, newDateEnd);
		BookTime bookTime = booktime.get(0);
		// bookTime.setPeople(doingMeals); //预约单保存预约人数
		// bookTimeService.saveORupdate(bookTime); ynw

		Cate cate = cateService.getCate(Integer.valueOf(OrderId));
		cate.setMeals(doingMeals); // 线上餐桌保存预约人数
		cateService.saveORupdate(cate);

		ComputerCate computerCate = computercateService.getCate(cate.getInsideOrderId().getId());
		if (computerCate != null) {
			computerCate.setMeals(doingMeals); // 线下餐桌保存预约人数
			computercateService.saveORupdate(computerCate);
		}

		message.addProperty("doingPeople", doingMeals); // 返回最新人数
		new PushJson().outString(message.toJSonString(), response);
	}

	@RequestMapping(params = "InsideReserveview", method = RequestMethod.POST)
	public void InsideReserveview(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId"); // 商家ID
		String pageIndex = request.getParameter("pageIndex");// 当前页面

		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(pageIndex)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "companyId或pageIndex不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		long timeLose = System.currentTimeMillis(); // 当天时间

		SimpleDateFormat dateStart = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String newDateSTart = dateStart.format(new Date(timeLose));

		SimpleDateFormat dateEnd = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
		String newDateEnd = dateEnd.format(new Date(timeLose));

		List<Order> list = orderService.getAllOrder(Integer.valueOf(companyId), "doing", Integer.valueOf(pageIndex),
				1000);
		// 2018-10-20 @Tyy 按预约时间排序 start
		List<Object[]> distancelist = new ArrayList<Object[]>();
		for (Order orders : list) {
			BookTime bookTime = bookTimeService.getBookTime(orders.getId());
			if (bookTime != null) {
				Object[] obj = { orders.getId(), bookTime.getReserveTime() };
				distancelist.add(obj);
			}
		}
		Collections.sort(distancelist, new Comparator<Object[]>() {

			@Override
			public int compare(Object[] o1, Object[] o2) {
				int i = DateUtils.compare_date(o1[1].toString(), o2[1].toString());
				return i;
			}

		});
		for (Object[] stu : distancelist) {
			Order order = orderService.getOrder(Integer.valueOf(stu[0].toString()));
			// end
			// for (order order : list) {
			Cate cateList = cateService.getCate(order.getId());

			List<Relating> tempList = relatingService.getrelating(order.getId());

			List<Relating> newList = null;
			if (cateList != null) {
				if (cateList.getInsideOrderId() == null) {
					newList = new ArrayList<Relating>();
				} else {
					newList = relatingService.getinsertrelating(cateList.getInsideOrderId().getId());
				}
			}

			if (tempList.size() > 0 || newList.size() > 0) {
				List<BookTime> listbook = bookTimeService.getBookTime(order.getId(), newDateSTart, newDateEnd);
				if (listbook.size() > 0) {
					JSonGridRecord record = new JSonGridRecord();
					BookTime temp = listbook.get(0);

					if (temp.getReserveId().getSeat().equals("dt")) {
						record.addColumn("tableNo", "大厅" + temp.getReserveId().getName());
					} else {

						record.addColumn("tableNo", "包厢" + temp.getReserveId().getName());
					}

					record.addColumn("reserveTime", temp.getReserveTime());
					record.addColumn("people", temp.getPeople()); // 预约人数

					record.addColumn("orderNo", order.getOrderNo());
					record.addColumn("creatTime", order.getAddTime());

					if (tempList.size() == 0 && newList.size() > 0) {
						// 押金
						record.addColumn("isDost", 1);
					}

					record.addColumn("userPhone", order.getUserId().getUserName());

					record.addColumn("orderId", order.getId());
					record.addColumn("order_status", order.getOrderStatus()); // 得到订单状态

					Delicacy delicacy = delicacyService.getDelicacy(Integer.valueOf(companyId)); // 得到对应商家的属性表
					record.addColumn("mealFee", delicacy.getMealFee()); // 得到茶位费

					record.addColumn("total", order.getTotal()); // 得到总金额

					double tempTotal = Double.valueOf(order.getTotal());
					double tempPay = Double.valueOf(order.getPay());

					DecimalFormat dFormat = new DecimalFormat("######0.00");

					double stemp = tempTotal - tempPay;

					record.addColumn("discount", dFormat.format(stemp)); // 得到优惠折扣
					record.addColumn("pay", order.getPay()); // 得到实际支付金额

					grid.addRecord(record);
				}
			}
		}

		grid.addProperties("status", "0");

		SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd");
		String dates = fdate.format(new Date(timeLose));
		grid.addProperties("totalCount", bookTimeService.getReserve(Integer.valueOf(companyId), dates));

		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 查询当天未订菜的订单 电脑端ynw
	@RequestMapping(params = "InsideNoReserveview", method = RequestMethod.POST)
	public void InsideNoReserveview(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId"); // 商家ID

		if (StringUtil.isEmpty(companyId)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "comId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		long timeLose = System.currentTimeMillis(); // 当天时间

		SimpleDateFormat dateStart = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String newDateSTart = dateStart.format(new Date(timeLose));

		SimpleDateFormat dateEnd = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
		String newDateEnd = dateEnd.format(new Date(timeLose));

		List<Order> list = orderService.getAllOrder(Integer.valueOf(companyId), "doing", 0, 1000);
		// 2018-10-20 @Tyy 按预约时间排序 start
		List<Object[]> distancelist = new ArrayList<Object[]>();
		for (Order orders : list) {
			BookTime bookTime = bookTimeService.getBookTime(orders.getId());
			if (bookTime != null) {
				Object[] obj = { orders.getId(), bookTime.getReserveTime() };
				distancelist.add(obj);
			}
		}
		Collections.sort(distancelist, new Comparator<Object[]>() {

			@Override
			public int compare(Object[] o1, Object[] o2) {
				int i = DateUtils.compare_date(o1[1].toString(), o2[1].toString());
				return i;
			}

		});
		for (Object[] stu : distancelist) {
			Order order = orderService.getOrder(Integer.valueOf(stu[0].toString()));
			// end
			// for (order order : list) {
			Cate cateList = cateService.getCate(order.getId());

			List<Relating> tempList = relatingService.getrelating(order.getId());

			List<Relating> newList = null;
			if (cateList == null) {
				newList = new ArrayList<Relating>();
			} else {
				if (cateList.getInsideOrderId() == null) {
					newList = new ArrayList<Relating>();
				} else {
					newList = relatingService.getinsertrelating(cateList.getInsideOrderId().getId());
				}
			}

			if (tempList.size() == 0 && newList.size() == 0) {
				List<BookTime> listbook = bookTimeService.getBookTime(order.getId(), newDateSTart, newDateEnd);
				if (listbook.size() > 0) {
					JSonGridRecord record = new JSonGridRecord();
					BookTime temp = listbook.get(0);

					if (temp.getReserveId().getSeat().equals("dt")) {
						record.addColumn("tableNo", "大厅" + temp.getReserveId().getName());
					} else {

						record.addColumn("tableNo", "包厢" + temp.getReserveId().getName());
					}

					record.addColumn("reserveTime", temp.getReserveTime());
					record.addColumn("people", temp.getPeople()); // 预约人数

					record.addColumn("orderNo", order.getOrderNo());
					record.addColumn("creatTime", order.getAddTime());

					record.addColumn("userPhone", order.getUserId().getUserName());

					record.addColumn("orderid", order.getId());
					record.addColumn("order_status", order.getOrderStatus()); // 得到订单状态

					Delicacy delicacy = delicacyService.getDelicacy(Integer.valueOf(companyId)); // 得到对应商家的属性表
					record.addColumn("mealFee", delicacy.getMealFee()); // 得到茶位费

					record.addColumn("deposit", order.getPay()); // 得到实际支付金额

					grid.addRecord(record);
				}
			}

		}
		/*
		 * List<Object[]> booklist =
		 * bookTimeService.getNOCompanyBookByCall(Integer .valueOf(companyId),
		 * newDateSTart,newDateEnd,pageCurrentCount,appanedSql.trim());
		 * 
		 * BigInteger bookcount =
		 * bookTimeService.getNOCompanyBookCountByCall(Integer
		 * .valueOf(companyId), newDateSTart,newDateEnd,1,appanedSql.trim());
		 * 
		 * for(Object[] b : booklist){ JSonGridRecord record = new
		 * JSonGridRecord(); record.addColumn("id", b[0]);
		 * record.addColumn("orderNo", b[11]); record.addColumn("people", b[2]);
		 * record.addColumn("isPay", b[1]); record.addColumn("time", b[8]); if(
		 * String.valueOf(b[13]).equals("dt")){ record.addColumn("tableNo",
		 * "大厅"+ b[12]); }else{
		 * 
		 * record.addColumn("tableNo", "包厢"+ b[12]); }
		 * 
		 * record.addColumn("userName", b[6]);
		 * record.addColumn("creatTime",b[4]);
		 * record.addColumn("deposit",b[14]); record.addColumn("orderid",b[10]);
		 * record.addColumn("userPhone", b[7]);
		 * record.addColumn("reserveTime",b[8]); grid.addRecord(record); } int
		 * pagetotal = 0; //总页面数量 pagetotal = bookcount.intValue()%9 == 0 ?(
		 * bookcount.intValue()/9):( bookcount.intValue()/9+1);//总页面数量判断
		 * grid.addProperties("status", "0"); grid.addProperties("pagetotal",
		 * pagetotal);
		 */

		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response) {
		String reservesId = request.getParameter("reservesId");

		if (StringUtil.isEmpty(reservesId)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "reservesId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long timeLose = System.currentTimeMillis() - 2 * 60 * 60 * 1000;
		String newDate = date.format(new Date(timeLose));
		List<BookTime> bookTimeList = bookTimeService.getBookTime(Integer.valueOf(reservesId), newDate);

		if (bookTimeList == null) {
			grid.addProperties("success", true);
			grid.addProperties("status", "1");
			grid.addProperties("totalCount", "0");
			new PushJson().outString(grid.toJSonString("list"), response);
			return;
		}

		int count = 0;
		for (BookTime bookTimes : bookTimeList) {

			Order order = orderService.getOrder(bookTimes.getOrderId());

			if (order.getOrderStatus().equals("doing")) {
				JSonGridRecord record = new JSonGridRecord();
				record.addColumn("id", bookTimes.getId());
				record.addColumn("people", bookTimes.getPeople());
				record.addColumn("isPay", bookTimes.getIsPay());
				record.addColumn("time", bookTimes.getReserveTime());
				record.addColumn("userName", bookTimes.getUserName());
				record.addColumn("userPhone", bookTimes.getUserPhone());
				record.addColumn("reserveTime", bookTimes.getReserveTime());
				grid.addRecord(record);
				count++;
			}
		}

		grid.addProperties("status", "0");
		grid.addProperties("totalCount", count);

		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 后台查询列表
	@RequestMapping(params = "backView", method = RequestMethod.POST)
	public void backView(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "comId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		Order order = orderService.getOrder(Integer.valueOf(orderId));
		if (order == null) {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (!order.getOrderStatus().equals("finish")) {
			Cate cate = cateService.getCate(Integer.valueOf(orderId));
			if (cate == null) {
				message.addProperty("message", "订餐信息不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}

			Reserve reserve = reserveService.find(Integer.valueOf(cate.getReserveId()));

			if (reserve == null) {
				message.addProperty("message", "餐桌信息不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}

			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			long timeLose = System.currentTimeMillis() - 2 * 60 * 60 * 1000;
			String newDate = date.format(new Date(timeLose));
			List<BookTime> bookTimeList = bookTimeService.getBookTime(reserve.getId(), newDate);

			if (bookTimeList == null) {
				grid.addProperties("success", true);
				grid.addProperties("status", "1");
				grid.addProperties("totalCount", "0");
				new PushJson().outString(grid.toJSonString("list"), response);
				return;
			}

			int count = 0;
			for (BookTime bookTimes : bookTimeList) {

				Order torder = orderService.getOrder(bookTimes.getOrderId());

				if (torder.getOrderStatus().equals("doing")) {
					JSonGridRecord record = new JSonGridRecord();
					record.addColumn("id", bookTimes.getId());
					record.addColumn("people", bookTimes.getPeople());
					record.addColumn("isPay", bookTimes.getIsPay());
					record.addColumn("time", bookTimes.getReserveTime());
					record.addColumn("userName", bookTimes.getUserName());
					record.addColumn("userPhone", bookTimes.getUserPhone());
					record.addColumn("reserveTime", bookTimes.getReserveTime());
					grid.addRecord(record);
					count++;
				}
			}
			grid.addProperties("status", "0");
			grid.addProperties("totalCount", count);

			new PushJson().outString(grid.toJSonString("list"), response);
		}

	}

	// 后台查询列表
	/*
	 * @RequestMapping(params = "backView", method = RequestMethod.POST) public
	 * void backView(HttpServletRequest request, HttpServletResponse response) {
	 * String orderId = request.getParameter("orderId");
	 * 
	 * JSonMessage message = new JSonMessage(); if (StringUtil.isEmpty(orderId))
	 * { message.addProperty("message", "comId不能为空");
	 * message.addProperty("success", false); new
	 * PushJson().outString(message.toJSonString(), response); return; }
	 * 
	 * JSonGrid grid = new JSonGrid(); grid.addProperties("success", true);
	 * 
	 * Cate cate = cateService.getCate(Integer.valueOf(orderId)); if (cate ==
	 * null) { message.addProperty("message", "订餐信息不存在");
	 * message.addProperty("success", false); new
	 * PushJson().outString(message.toJSonString(), response); return; }
	 * 
	 * Reserve reserve =
	 * reserveService.find(Integer.valueOf(cate.getReserveId()));
	 * 
	 * if (reserve == null) { message.addProperty("message", "餐桌信息不存在");
	 * message.addProperty("success", false); new
	 * PushJson().outString(message.toJSonString(), response); return; }
	 * 
	 * SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm"); long
	 * timeLose = System.currentTimeMillis() - 2 * 60 * 60 * 1000; String
	 * newDate = date.format(new Date(timeLose)); List<BookTime> bookTimeList =
	 * bookTimeService.getBookTime(reserve .getId(), newDate);
	 * 
	 * if (bookTimeList == null) { grid.addProperties("success", true);
	 * grid.addProperties("status", "1"); grid.addProperties("totalCount", "0");
	 * new PushJson().outString(grid.toJSonString("list"), response); return; }
	 * 
	 * for (BookTime bookTimes : bookTimeList) { JSonGridRecord record = new
	 * JSonGridRecord(); record.addColumn("id", bookTimes.getId());
	 * record.addColumn("people", bookTimes.getPeople());
	 * record.addColumn("isPay", bookTimes.getIsPay()); record.addColumn("time",
	 * bookTimes.getReserveTime()); record.addColumn("userName",
	 * bookTimes.getUserName()); record.addColumn("userPhone",
	 * bookTimes.getUserPhone()); record.addColumn("reserveTime",
	 * bookTimes.getReserveTime()); grid.addRecord(record); }
	 * grid.addProperties("status", "0"); grid.addProperties("totalCount",
	 * bookTimeList.size());
	 * 
	 * new PushJson().outString(grid.toJSonString("list"), response);
	 * 
	 * }
	 */

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, BookTime bookTime, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(bookTime.getId() + "") || bookTime.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		bookTimeService.delete(bookTime.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, BookTime bookTime, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		bookTimeService.saveORupdate(bookTime);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	//网络订单搜索
	@RequestMapping(params = "searchOrder", method = RequestMethod.POST)
	public void searchOrder(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId"); // 商家ID
		String keyWord = request.getParameter("keyWord"); // 搜索内容

		if (StringUtil.isEmpty(companyId)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "companyId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		long timeLose = System.currentTimeMillis(); // 当天时间

		SimpleDateFormat dateStart = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String newDateSTart = dateStart.format(new Date(timeLose));

		SimpleDateFormat dateEnd = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
		String newDateEnd = dateEnd.format(new Date(timeLose));
		List<Order> list = new ArrayList<Order>();
		if (!StringUtil.isEmpty(keyWord)) {
			list = orderService.searchOrderNo(keyWord);
			List<Cate> cateLists = cateService.searchPhone(keyWord);
			for (Cate cates : cateLists) {
				list.add(cates.getOrderId());
			}
		}

		// 2018-10-20 @Tyy 按预约时间排序 start
		// List<Object[]> distancelist = new ArrayList<Object[]>();
		// for (Order orders : list) {
		// BookTime bookTime = bookTimeService.getBookTime(orders.getId());
		// if (bookTime != null) {
		// Object[] obj = { orders.getId(), bookTime.getReserveTime() };
		// distancelist.add(obj);
		// }
		// }
		// Collections.sort(distancelist, new Comparator<Object[]>() {
		//
		// @Override
		// public int compare(Object[] o1, Object[] o2) {
		// int i = DateUtils.compare_date(o1[1].toString(), o2[1].toString());
		// return i;
		// }
		//
		// });
		// for (Object[] stu : distancelist) {
		// Order order =
		// orderService.getOrder(Integer.valueOf(stu[0].toString()));
		for (Order order : list) {
			List<Relating> tempList = relatingService.getrelating(order.getId());
			JSonGridRecord record = new JSonGridRecord();
			if (tempList.size() > 0) {
				record.addColumn("status", "1");
			} else {
				// 押金
				record.addColumn("status", "0");
				Cate cate = cateService.getCate(order.getId());
				if (cate != null) {
					Reserve reserve = reserveService.find(Integer.valueOf(cate.getReserveId()));
					if (reserve != null) {
						record.addColumn("dost", reserve.getDeposit());
					}
				}
			}
			List<BookTime> listbook = bookTimeService.getBookTime(order.getId(), newDateSTart, newDateEnd);
			if (listbook.size() > 0) {
				BookTime temp = listbook.get(0);
				if (temp.getReserveId().getSeat().equals("dt")) {
					record.addColumn("tableNo", "大厅" + temp.getReserveId().getName());
				} else {
					record.addColumn("tableNo", "包厢" + temp.getReserveId().getName());
				}
				record.addColumn("reserveTime", temp.getReserveTime());
				record.addColumn("people", temp.getPeople()); // 预约人数
				record.addColumn("orderNo", order.getOrderNo());
				record.addColumn("creatTime", order.getAddTime());
				record.addColumn("userPhone", order.getUserId().getUserName());
				record.addColumn("orderId", order.getId());
				record.addColumn("order_status", order.getOrderStatus()); // 得到订单状态

				grid.addRecord(record);
			}
		}

		SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd");
		String dates = fdate.format(new Date(timeLose));
		grid.addProperties("totalCount", bookTimeService.getReserve(Integer.valueOf(companyId), dates));

		new PushJson().outString(grid.toJSonString("list"), response);

	}

}