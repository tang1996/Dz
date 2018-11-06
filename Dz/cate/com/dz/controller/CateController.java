package com.dz.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Cate;
import com.dz.entity.Company;
import com.dz.entity.ComputerCate;
import com.dz.entity.Delicacy;
import com.dz.entity.InsideOrder;
import com.dz.entity.Order;
import com.dz.entity.Reserve;
import com.dz.entity.Track;
import com.dz.entity.User;
import com.dz.service.ICateService;
import com.dz.service.ICompanyService;
import com.dz.service.IComputerCateService;
import com.dz.service.IDelicacyService;
import com.dz.service.IInsideOrderService;
import com.dz.service.IOrderService;
import com.dz.service.IReserveService;
import com.dz.service.ITrackService;
import com.dz.service.IUserService;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/cate")
public class CateController {

	@Autowired
	private ICateService cateService;

	@Autowired
	private IUserService userService;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private IReserveService reserveService;
	
	@Autowired
	private IOrderService orderService;

	@Autowired
	private ITrackService trackService;
	
	@Autowired
	private IInsideOrderService insideOrderService;
	
	@Autowired
	private IDelicacyService delicacyService;
	
	@Autowired
	private IComputerCateService computerCateService;
	
	//线上得线下(新建线下订单)	ynw
	@RequestMapping(params = "OnsideAndInside", method = RequestMethod.POST)
	public void OnsideAndInside(HttpServletRequest request, HttpServletResponse response) {
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
		if(cate == null){
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
			
		if(cate.getInsideOrderId() == null){
			InsideOrder insideOrder = new InsideOrder();	//生成线下订单
			insideOrder.setCompanyId(company);		
			insideOrder.setPayStatus("0");
			String orderNo = "TS";
			insideOrder.setOrderNo(orderNo + company.getId() + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			insideOrder.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			insideOrder.setCustomId("1");
			insideOrder.setOrderStatus("unpay");	//订单初始状态
			insideOrder.setIsAccount("0");		
			insideOrderService.saveORupdate(insideOrder);
			
			cate.setInsideOrderId(insideOrder);	//将线上与线下关联
			cateService.saveORupdate(cate);
			
			ComputerCate insidecate = new ComputerCate();
			insidecate.setName(reserve.getName());
			insidecate.setCompanyId(company);
			insidecate.setSeat(reserve.getSeat());
			insidecate.setTableNo(reserve.getTableNo());
			insidecate.setReserveId(reserve.getId() + "");
			insidecate.setInsideorderId(insideOrder);
			insidecate.setMeals(cate.getMeals());
			Delicacy delicacy = delicacyService.getDelicacy(Integer.valueOf(order.getCompanyId().getId()));	//获得对应商家属性
			insidecate.setMealFee(delicacy.getMealFee());		//获得对应商家的茶位费 
			computerCateService.saveORupdate(insidecate);
			
			
			message.addProperty("insideOrderId",insideOrder.getId());	//ynw
		}else{
			message.addProperty("insideOrderId",cate.getInsideOrderId().getId());	//ynw
		}
	
		message.addProperty("doingMeals",cate.getMeals());	//就餐人数
		message.addProperty("success", true);
		message.addProperty("message", "备注信息修改成功");
		new PushJson().outString(message.toJSonString(), response);
	}
	

	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "token不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Cate cate = cateService.getCate(Integer.valueOf(orderId));
		if (cate != null) {
			message.addProperty("success", true);
			message.addProperty("endTime", cate.getEndTime());
			message.addProperty("tableNo", cate.getTableNo());		//ynw
			message.addProperty("companyid",  cate.getCompanyId().getId());	//ynw
			message.addProperty("meals", cate.getMeals());	//ynw
			Reserve reserve = reserveService.getTable(cate.getTableNo(), cate.getCompanyId().getId(), cate.getSeat());
			message.addProperty("seat", cate.getSeat());
			if (reserve != null) {
				message.addProperty("cancelTime", reserve.getCancelTime());
				message.addProperty("companyName", reserve.getCompanyId()
						.getName());
				message.addProperty("companyPhone", reserve.getCompanyId()
						.getPhone());
				message.addProperty("logo", reserve.getCompanyId().getLogo());
				message.addProperty("price", reserve.getDeposit());
				message.addProperty("tableNo", reserve.getName());
				message.addProperty("status", reserve.getStatus());
				message.addProperty("reserveId", reserve.getId());
			}else{
				message.addProperty("success", false);
				message.addProperty("message", "餐桌信息有误");
			}
		}else{
			message.addProperty("success", false);
			message.addProperty("message", "订单信息有误");
		}

		new PushJson().outString(message.toJSonString(), response);
	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Cate cate,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(cate.getId() + "") || cate.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		cateService.delete(cate.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}
	

	//开台
	@RequestMapping(params = "openTable", method = RequestMethod.POST)
	public void openTable(HttpServletRequest request,
			HttpServletResponse response) {
		String token = request.getParameter("token");
		String companyId = request.getParameter("companyId");
		String tableNo = request.getParameter("tableNo");
		String seat = request.getParameter("seat");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token)) {
			message.addProperty("message", "token验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(tableNo)
					|| StringUtil.isEmpty(seat)) {
			message.addProperty("message",
					"companyId,tableNo,createTime,seat和endTime不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);

		if (user == null) {
			message.addProperty("message", "user不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = companyService.getCompany(Integer.valueOf(companyId));

		if (company == null) {
			message.addProperty("message", "company不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
	

		Order order = new Order();
		order.setCompanyId(company);
		order.setPayStatus("0");
		order.setUserId(user);
		
		order.setOrderNo("MS" + company.getId()
				+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		order.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date()));
		order.setCustomId("1");
		order.setOrderType("2"); //2 代表美食
		order.setOrderStatus("unpay");
		String remarks = request.getParameter("remarks");
		order.setRemarks(remarks);
		orderService.saveORupdate(order);
		
		
		Cate cate = new Cate();
		cate.setUserId(user);
		cate.setCompanyId(company);
		cate.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date()));
		cate.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
		.format(new Date()));
		cate.setSeat(seat);
		cate.setTableNo(tableNo);
		cate.setOrderId(order);

		cateService.saveORupdate(cate);

		Track track = new Track();
		track.setOrderId(order);
		track.setStatus("submit");
		track.setBewrite("订单提交成功");
		track.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date()));
		trackService.saveORupdate(track);
		
		Reserve reserve = reserveService.getTable(tableNo, Integer.valueOf(companyId));
		reserve.setStatus("2");
		reserveService.saveORupdate(reserve);

		message.addProperty("message", "订单添加成功");
		message.addProperty("orderId", order.getId());
		message.addProperty("orderNo", order.getOrderNo());
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
		
		
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request,
			HttpServletResponse response) {
		String token = request.getParameter("token");
		String companyId = request.getParameter("companyId");
		String tableNo = request.getParameter("tableNo");
		String endTime = request.getParameter("endTime");
		String seat = request.getParameter("seat");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token)) {
			message.addProperty("message", "token验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(tableNo)
				|| StringUtil.isEmpty(endTime) || StringUtil.isEmpty(seat)) {
			message.addProperty("message",
					"companyId,tableNo,createTime,seat和endTime不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);

		if (user == null) {
			message.addProperty("message", "user不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = companyService.getCompany(Integer.valueOf(companyId));

		if (company == null) {
			message.addProperty("message", "company不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Cate cate = new Cate();
		cate.setUserId(user);
		cate.setCompanyId(company);
		cate.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date()));
		cate.setEndTime(endTime);
		cate.setSeat(seat);
		cate.setTableNo(tableNo);

		cateService.saveORupdate(cate);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}