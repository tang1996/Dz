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

import com.dz.entity.BookTime;
import com.dz.entity.Cate;
import com.dz.entity.Company;
import com.dz.entity.CompanyActivity;
import com.dz.entity.ComputerCate;
import com.dz.entity.Delicacy;
import com.dz.entity.Goods;
import com.dz.entity.InsideOrder;
import com.dz.entity.Job;
import com.dz.entity.Order;
import com.dz.entity.Relating;
import com.dz.entity.Reserve;
import com.dz.entity.Staff;
import com.dz.service.IBookTimeService;
import com.dz.service.ICateService;
import com.dz.service.ICompanyActivityService;
import com.dz.service.ICompanyService;
import com.dz.service.IComputerCateService;
import com.dz.service.IDelicacyService;
import com.dz.service.IGoodsService;
import com.dz.service.IInsideOrderService;
import com.dz.service.IJobService;
import com.dz.service.IOrderService;
import com.dz.service.IRelatingService;
import com.dz.service.IReserveService;
import com.dz.service.IStaffService;
import com.dz.util.CollectMoneyUtils;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/insideOrder")
public class InsideOrderController {

	@Autowired
	private IInsideOrderService insideOrderService;

	@Autowired
	private IOrderService orderService;

	@Autowired
	private ICompanyActivityService companyActivityService;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private IReserveService reserveService;

	@Autowired
	private ICateService cateService;

	@Autowired
	private IComputerCateService computerCateService;

	@Autowired
	private IRelatingService relatingService;

	@Autowired
	private IGoodsService goodsService;

	@Autowired
	private IStaffService staffService;

	@Autowired
	private IDelicacyService delicacyService;

	@Autowired
	private IJobService jobService;
	
	@Autowired
	private IBookTimeService bookTimeService;

	// 删除线下订单 就餐变空桌 ynw
	@RequestMapping(params = "deleteInsideOrder", method = RequestMethod.POST)
	public void deleteInsideOrder(HttpServletRequest request, HttpServletResponse response) {

		String InsideOrderId = request.getParameter("insideOrderId");//
		JSonMessage message = new JSonMessage();

		if (InsideOrderId.isEmpty()) {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		ComputerCate cate = computerCateService.getCate(Integer.valueOf(InsideOrderId));

		if (cate != null) {

			Reserve reserve = reserveService.find(Integer.valueOf(cate.getReserveId()));
			if (reserve == null) {
				message.addProperty("message", "餐桌不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			long timeLose = System.currentTimeMillis(); // 当天时间

			SimpleDateFormat dateStart = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
			String newDateSTart = dateStart.format(new Date(timeLose));

			SimpleDateFormat dateEnd = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
			String newDateEnd = dateEnd.format(new Date(timeLose));
			List<Relating> relating = relatingService.getrelatingbyinsideID(Integer.valueOf(InsideOrderId));
			if (relating.size() > 0) {
				if (reserve.getStatus().equals("2")) { // 就餐中的桌子
					reserve.setStatus("0");
					//2018-10-17 @Tyy start  判断当日是否还有预定
					List<Order> list = orderService.getAllOrder(cate.getCompanyId().getId(), "doing", 0, 1000);
					for (Order order : list) {
						List<BookTime> listbook = bookTimeService.getBookTime(order.getId(), newDateSTart, newDateEnd);
						if(listbook.size() > 0){
							reserve.setStatus("1");
							break;
						}
					}
					//end
					reserveService.saveORupdate(reserve);

					message.addProperty("message", "状态修改成功");
					message.addProperty("success", true);
				}
			} else {
				if (reserve.getStatus().equals("2")) { // 就餐中的桌子
					reserve.setStatus("0");
					//2018-10-17 @Tyy start  判断当日是否还有预定
					List<Order> list = orderService.getAllOrder(cate.getCompanyId().getId(), "doing", 0, 1000);
					for (Order order : list) {
						List<BookTime> listbook = bookTimeService.getBookTime(order.getId(), newDateSTart, newDateEnd);
						if(listbook.size() > 0){
							reserve.setStatus("1");
							break;
						}
					}
					//end
					List<Job> job = jobService.getInsidejobList(InsideOrderId);

					for (Job j : job) {
						jobService.delete(j.getId() + "");
					}

					computerCateService.delete(cate.getId() + "");

					insideOrderService.delete(InsideOrderId);
					reserveService.saveORupdate(reserve);
					message.addProperty("message", "状态修改成功");
					message.addProperty("success", true);
				}
			}
		}
		new PushJson().outString(message.toJSonString(), response);
	}

	// 线下开桌 ynw
	@RequestMapping(params = "openTable", method = RequestMethod.POST)
	public void save(HttpServletRequest request, HttpServletResponse response) {
		String cid = request.getParameter("companyId");// 商家id
		String reserveId = request.getParameter("reserveId");// 餐桌 ID
		String meals = request.getParameter("meals");// 人数
		// 选填
		String remarks = request.getParameter("Remarks");// 备注信息

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(cid) || StringUtil.isEmpty(reserveId) || StringUtil.isEmpty(meals)) {
			message.addProperty("message", "缺少必要参数");
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

		Reserve reserve = reserveService.find(Integer.valueOf(reserveId));
		if (reserve == null) {
			message.addProperty("message", "餐桌不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (reserve.getStatus().equals("2")) { // 如果不为空桌或预定桌 ynw
			message.addProperty("message", "餐桌状态不可用");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Delicacy delicacy = delicacyService.getDelicacy(Integer.valueOf(cid)); // 得到商家对应的属性
		if (delicacy == null) {
			message.addProperty("message", "查询不到对应商家的属性");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		InsideOrder insideOrder = new InsideOrder(); // 生成线下订单
		insideOrder.setCompanyId(company);
		insideOrder.setPayStatus("0");
		String orderNo = "TS";
		insideOrder.setOrderNo(orderNo + company.getId() + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		insideOrder.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		insideOrder.setCustomId("1");
		insideOrder.setOrderStatus("unpay"); // 订单初始状态
		insideOrder.setIsAccount("0");
		if (StringUtil.isEmpty(remarks)) {
			insideOrder.setRemarks(remarks);
		}

		insideOrderService.saveORupdate(insideOrder);

		ComputerCate cate = new ComputerCate();
		cate.setName(reserve.getName());
		cate.setCompanyId(company);
		cate.setSeat(reserve.getSeat());
		cate.setTableNo(reserve.getTableNo());
		cate.setReserveId(reserve.getId() + "");
		cate.setInsideorderId(insideOrder);
		cate.setMeals(meals);
		// Delicacy delicacy =
		// delicacyService.getDelicacy(Integer.valueOf(cid)); //获得对应商家属性
		cate.setMealFee(delicacy.getMealFee()); // 获得对应商家的茶位费
		computerCateService.saveORupdate(cate);

		reserve.setStatus("2");
		reserveService.saveORupdate(reserve);

		message.addProperty("insideOrderId", insideOrder.getId()); // ynw
		message.addProperty("mealFee", delicacy.getMealFee()); // ynw
		message.addProperty("doingMeals", meals); // 就餐人数
		message.addProperty("success", true);
		message.addProperty("message", "备注信息修改成功");
		new PushJson().outString(message.toJSonString(), response);
	}

	// 点菜
	@RequestMapping(value = "/batchSave", method = RequestMethod.POST)
	public void batchSave(HttpServletRequest request, HttpServletResponse response) {

		String content = request.getParameter("content");
		String token = request.getParameter("token");// 员工token
		String orderId = request.getParameter("orderId");// 线下订单 ID

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(content) || StringUtil.isEmpty(token)) {
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

		InsideOrder insideOrder = insideOrderService.getInsideOrder(Integer.valueOf(orderId));

		if (insideOrder.getCompanyId() == null) {
			message.addProperty("message", "获取商家信息失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

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
			relating.setCompanyId(Integer.valueOf(insideOrder.getCompanyId().getId()));
			relating.setGoodsId(Integer.valueOf(goodId));
			relating.setInsideOrderId(insideOrder);
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
		insideOrder.setTotal(total + "");
		insideOrderService.saveORupdate(insideOrder);

		message.addProperty("message", "添加订单成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 减菜
	@RequestMapping(value = "/doDelete", method = RequestMethod.POST)
	public void doDelete(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");// 新订单id
		String goodsId = request.getParameter("goodsId");// 商品id
		String num = request.getParameter("num");// 商品数量
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(goodsId) || StringUtil.isEmpty(num)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Relating relating = relatingService.getinsideRelating(Integer.valueOf(orderId), Integer.valueOf(goodsId));

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
		String orderId = request.getParameter("orderId");// 新订单id
		String goodsId = request.getParameter("goodsId");// 商品id
		String num = request.getParameter("num");// 商品数量
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(goodsId) || StringUtil.isEmpty(num)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Relating relating = relatingService.getinsideRelating(Integer.valueOf(orderId), Integer.valueOf(goodsId));

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

	@RequestMapping(value = "/checkMoney", method = RequestMethod.POST)
	public void checkMoney(HttpServletRequest request, HttpServletResponse response) {

		String orderId = request.getParameter("orderId");// 新订单id
		String cid = request.getParameter("companyid");// 商家id

		//String personNum = request.getParameter("personNum");// 就餐人数

		String subFee = request.getParameter("subFee");// 是否有减免等

		String disacount = request.getParameter("disacount");// 是否有折扣等

		String total = request.getParameter("total"); // 总计
		String pay = request.getParameter("pay"); // 应收

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(cid)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		ComputerCate cate = computerCateService.getCate(Integer.valueOf(orderId));

		if (cate == null) {
			message.addProperty("message", "订单信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		//cate.setMeals(personNum);
		computerCateService.saveORupdate(cate);

		// 完成状态更改
		if (cate.getInsideorderId() != null) {
			InsideOrder insideOrder = cate.getInsideorderId();
			insideOrder.setPay(pay);
			insideOrder.setPayStatus("1");
			insideOrder.setPayTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			insideOrder.setTotal(total);
			insideOrder.setDisAcount(StringUtil.isEmpty(disacount) == true ? null : disacount);
			insideOrder.setSubSale(StringUtil.isEmpty(subFee) == true ? null : subFee);
			insideOrder.setOrderStatus("finish");
			insideOrder.setIsAccount("1");
			insideOrder.setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			insideOrderService.saveORupdate(insideOrder);
		}

		// 桌子状态调整
		Reserve reserve = reserveService.find(Integer.valueOf(cate.getReserveId()));

		// not found service data
//		List<Order> list = cateService.cateRes(cate.getReserveId());
//		if (list.size() > 1) {
//			reserve.setStatus("1");
//		} else {
//			reserve.setStatus("0");
//		}
		//2018-10-17 @Tyy 判断是否还有预定
		long timeLose = System.currentTimeMillis(); // 当天时间

		SimpleDateFormat dateStart = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String newDateSTart = dateStart.format(new Date(timeLose));

		SimpleDateFormat dateEnd = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
		String newDateEnd = dateEnd.format(new Date(timeLose));
		
		reserve.setStatus("0");
		List<Order> list = orderService.getAllOrder(cate.getCompanyId().getId(), "doing", 0, 1000);
		for (Order order : list) {
			List<BookTime> listbook = bookTimeService.getBookTime(order.getId(), newDateSTart, newDateEnd);
			if(listbook.size() > 0){
				reserve.setStatus("1");
				break;
			}
		}
		//end
		reserveService.saveORupdate(reserve);

		message.addProperty("message", "结算完成.");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);

	}

	// 结算 - 押金 - 收银
	@RequestMapping(value = "/newCheckMoney", method = RequestMethod.POST)
	public void newCheckMoney(HttpServletRequest request, HttpServletResponse response) {

		String orderId = request.getParameter("orderId");// 新订单id
		String cid = request.getParameter("companyid");// 商家id
		String recode = request.getParameter("recode");// 收银码

		//String personNum = request.getParameter("personNum");// 就餐人数

		String subFee = request.getParameter("subFee");// 是否有减免等

		String disacount = request.getParameter("disacount");// 是否有折扣等

		String total = request.getParameter("total"); // 总计
		String pay = request.getParameter("pay"); // 应收

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(cid) || StringUtil.isEmpty(recode)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Cate cate = cateService.getCateInside(Integer.valueOf(orderId));

		if (cate == null) {
			message.addProperty("message", "订单信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if (recode.equals(cate.getRecode())) {

			//cate.setMeals(personNum);
			cateService.saveORupdate(cate);

			ComputerCate ccate = computerCateService.getCate(Integer.valueOf(orderId));

			if (ccate == null) {
				message.addProperty("message", "订单信息不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}

		//	ccate.setMeals(personNum);
			computerCateService.saveORupdate(ccate);

			// 完成状态更改
			if (cate.getInsideOrderId() != null) {
				InsideOrder insideOrder = cate.getInsideOrderId();
				insideOrder.setPay(pay);
				insideOrder.setPayStatus("1");
				insideOrder.setPayTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				insideOrder.setTotal(total);
				insideOrder.setDisAcount(StringUtil.isEmpty(disacount) == true ? null : disacount);
				insideOrder.setSubSale(StringUtil.isEmpty(subFee) == true ? null : subFee);
				insideOrder.setOrderStatus("finish");
				insideOrder.setIsAccount("1");
				insideOrder.setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				insideOrderService.saveORupdate(insideOrder);

				Order order = cate.getOrderId();

				order.setOrderStatus("finish");
				order.setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				order.setPayTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				order.setPayStatus("1");
				
				//2018-10-27 @Tyy start  押金不到帐
				double npay = Double.valueOf(cate.getPrice());
				npay = npay*100;
				Company company = order.getCompanyId();
				if (!StringUtil.isEmpty(company.getOpenId())) {
					if (npay >= 1000) {
						npay = npay - (npay * 0.006);
					} else {
						npay = npay - 10;
					}

					String money = (int) npay + "";
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
				//end
				
				orderService.saveORupdate(order);
			}

			// 桌子状态调整
			Reserve reserve = reserveService.find(Integer.valueOf(cate.getReserveId()));

			// not found service data
//			List<Order> list = cateService.cateRes(cate.getReserveId());
//			if (list.size() > 1) {
//				reserve.setStatus("1");
//			} else {
//				reserve.setStatus("0");
//			}
			
			//2018-10-17 @Tyy 判断是否还有预定
			long timeLose = System.currentTimeMillis(); // 当天时间

			SimpleDateFormat dateStart = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
			String newDateSTart = dateStart.format(new Date(timeLose));

			SimpleDateFormat dateEnd = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
			String newDateEnd = dateEnd.format(new Date(timeLose));
			
			reserve.setStatus("0");
			List<Order> list = orderService.getAllOrder(cate.getCompanyId().getId(), "doing", 0, 1000);
			for (Order order : list) {
				List<BookTime> listbook = bookTimeService.getBookTime(order.getId(), newDateSTart, newDateEnd);
				if(listbook.size() > 0){
					reserve.setStatus("1");
					break;
				}
			}
			//end

			reserveService.saveORupdate(reserve);

			message.addProperty("message", "结算完成.");
			message.addProperty("success", true);
			new PushJson().outString(message.toJSonString(), response);
		} else {
			message.addProperty("message", "收银码不正确.");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
		}

	}

	// 结算 - 押金 线上 - 线下 - 收银
	@RequestMapping(value = "/sumCheckMoney", method = RequestMethod.POST)
	public void sumCheckMoney(HttpServletRequest request, HttpServletResponse response) {

		String orderId = request.getParameter("orderId");// 新订单id
		String cid = request.getParameter("companyid");// 商家id
		String recode = request.getParameter("recode");// 收银码

		String sale = request.getParameter("sale");

		//String personNum = request.getParameter("personNum");// 就餐人数

		String subFee = request.getParameter("subFee");// 是否有减免等

		String disacount = request.getParameter("disacount");// 是否有折扣等

		String total = request.getParameter("total"); // 总计
		String pay = request.getParameter("pay"); // 应收

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(cid) || StringUtil.isEmpty(recode)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Cate cate = cateService.getCateInside(Integer.valueOf(orderId));

		if (cate == null) {
			message.addProperty("message", "订单信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if (recode.equals(cate.getRecode())) {

		//	cate.setMeals(personNum);
			cateService.saveORupdate(cate);

			ComputerCate ccate = computerCateService.getCate(Integer.valueOf(orderId));

			if (ccate == null) {
				message.addProperty("message", "订单信息不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}

	//		ccate.setMeals(personNum);
			computerCateService.saveORupdate(ccate);

			// 完成状态更改
			if (cate.getInsideOrderId() != null) {
				InsideOrder insideOrder = cate.getInsideOrderId();
				insideOrder.setPay(pay);
				insideOrder.setPayStatus("1");
				insideOrder.setPayTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				insideOrder.setTotal(total);
				insideOrder.setDisAcount(StringUtil.isEmpty(disacount) == true ? null : disacount);
				insideOrder.setSubSale(StringUtil.isEmpty(subFee) == true ? null : subFee);
				insideOrder.setOrderStatus("finish");
				insideOrder.setIsAccount("1");

				if (!StringUtil.isEmpty(sale)) {
					insideOrder.setRemarks("用户退菜品总金额:" + sale);
				}

				insideOrder.setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				insideOrderService.saveORupdate(insideOrder);

				Order order = cate.getOrderId();

				order.setOrderStatus("finish");
				order.setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				order.setPayTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				order.setPayStatus("1");
				orderService.saveORupdate(order);
				
				double npay = Double.valueOf(order.getPay());//Double.valueOf(order.getPay()) * 100 - Double.valueOf(price) * 100;
				npay = npay*100;//2018-10-19 @Tyy
				Company company = order.getCompanyId();
				if (!StringUtil.isEmpty(company.getOpenId())) {
					if (npay >= 1000) {
						npay = npay - (npay * 0.006);
					} else {
						npay = npay - 10;
					}

					String money = (int) npay + "";
					String[] result = CollectMoneyUtils.CollectMoney(money, company.getOpenId());
					if (result != null) {
						order.setContent(result[1]);
						if (result[0].equals("true")) {
							order.setIsAccount("1");
						} else {
							order.setIsAccount("0");
						}
						
						orderService.saveORupdate(order);
					} else {
						order.setContent("发送请求失败");
					}
				} else {
					order.setContent("openId不存在");
				}
			}

			// 桌子状态调整
			Reserve reserve = reserveService.find(Integer.valueOf(cate.getReserveId()));

			// not found service data
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

	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, InsideOrder insideOrder, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(insideOrder.getId() + "") || insideOrder.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		insideOrderService.delete(insideOrder.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
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

		InsideOrder order = insideOrderService.getInsideOrder(Integer.valueOf(orderId));

		if (order == null) {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		order.setOrderStatus("finish");
		order.setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		insideOrderService.saveORupdate(order);

		message.addProperty("message", "确认完成");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
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

		InsideOrder order = insideOrderService.getInsideOrder(Integer.valueOf(orderId));
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
			// order.setTotal(dFormat.format(total) + "");

			if (!StringUtil.isEmpty(remarks)) {
				order.setRemarks(remarks);
			}

			insideOrderService.saveORupdate(order);
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

	// 线下订单经营数据
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

		List<Order> MSfinish = orderService.getList(Integer.valueOf(companyId), startTime, endTime);

		DecimalFormat dFormat = new DecimalFormat("######0.00");

		double MSpay = 0;

		// 美食支付统计
		for (Order order : MSfinish) {
			MSpay = Double.valueOf(dFormat.format(MSpay + Double.parseDouble(order.getPay())));
		}

		message.addProperty("success", true);
		message.addProperty("MSfinish", MSfinish.size());
		message.addProperty("MSpay", MSpay);
		new PushJson().outString(message.toJSonString(), response);

	}
}
