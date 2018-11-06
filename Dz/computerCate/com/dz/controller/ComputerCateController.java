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
import com.dz.entity.InsideOrder;
import com.dz.entity.Order;
import com.dz.entity.Reserve;
import com.dz.service.ICateService;
import com.dz.service.ICompanyService;
import com.dz.service.IComputerCateService;
import com.dz.service.IInsideOrderService;
import com.dz.service.IOrderService;
import com.dz.service.IReserveService;
import com.dz.service.ITrackService;
import com.dz.service.IUserService;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/computerCate")
public class ComputerCateController {

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
	private IInsideOrderService insideOrderService;

	@Autowired
	private ITrackService trackService;
	
	@Autowired
	private IComputerCateService computercateService;
	
	//线下餐桌调整人数
	@RequestMapping(params = "keepPeople", method = RequestMethod.POST)
	public void keepPeople(HttpServletRequest request,
			HttpServletResponse response) {
		
			String insideOrderId = request.getParameter("insideOrderId");
			String doingMeals = request.getParameter("meals");
			
			JSonMessage message = new JSonMessage();
			
			if (StringUtil.isEmpty(insideOrderId) || StringUtil.isEmpty(doingMeals)) {
				message.addProperty("message", "找不到线下订单号");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			
			if (StringUtil.isEmpty(doingMeals) || Integer.valueOf(doingMeals)==0 ) {
				message.addProperty("message", "调整人数参数有误");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			
			ComputerCate cate = computercateService.getCate(Integer.valueOf(insideOrderId));
			if(cate == null){
				message.addProperty("message", "找不到对应的线下餐桌");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			
			cate.setMeals(doingMeals);	//保存人数
			computercateService.saveORupdate(cate);
			
			message.addProperty("doingPeople", cate.getMeals());	//返回最新人数
			new PushJson().outString(message.toJSonString(), response);
		}

	// 不可用
	@RequestMapping(params = "openTable", method = RequestMethod.POST)
	public void openTable(HttpServletRequest request,
			HttpServletResponse response) {
		String companyId = request.getParameter("companyId");	//商家ID
		String tableNo = request.getParameter("tableNo");	//桌台号
		String seat = request.getParameter("seat");	//包厢还是大厅
		String meals = request.getParameter("meals");	//就餐人数

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(tableNo)
				|| StringUtil.isEmpty(seat) || StringUtil.isEmpty(meals)) {
			message.addProperty("message", "companyId,tableNo,,seat和meals不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = companyService.getCompany(Integer.valueOf(companyId)); // 找到对应ID的商家
		if (company == null) {
			message.addProperty("message", "company不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		InsideOrder order = new InsideOrder(); // 新建一个订单表
		order.setCompanyId(company); // 填入商家信息
		order.setPayStatus("0"); // 设置支付情况

		order.setOrderNo("MS" + company.getId() // 设置订单的订单号
				+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		order.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date()));
		order.setCustomId("1");
		order.setOrderStatus("unpay"); // 设置订单支付描述
		String remarks = request.getParameter("remarks"); // 获取订单备注
		order.setRemarks(remarks); // 设置订单描述
		insideOrderService.saveORupdate(order); // 生成订单

		
		ComputerCate cate = new ComputerCate(); //线下桌台信息
		cate.setCompanyId(company);
		cate.setSeat(seat);
		cate.setTableNo(tableNo);
		cate.setMeals(meals);
		cate.setInsideorderId(order);
		/* ynw start 增加表内的name值 */
		Reserve reserveForname = reserveService.getTable(tableNo, Integer
				.valueOf(companyId), seat);
		if (reserveForname != null) {
			cate.setName(reserveForname.getName());
		} else {
			String seatType = "bx";
			String name = null;
			if (StringUtil.isEmpty(name)) {
				name = tableNo + "号桌";
				seat = "dt";
			}
			cate.setName(name);
		}
	}
	/* end */
}
