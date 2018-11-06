package com.dz.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.BookTime;
import com.dz.entity.Company;
import com.dz.entity.Reserve;
import com.dz.service.IBookTimeService;
import com.dz.service.ICompanyService;
import com.dz.service.IOrderService;
import com.dz.service.IReserveService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/reserve")
public class ReserveController {

	@Autowired
	private IReserveService reserveService;
	
	@Autowired
	private IOrderService orderService;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private IBookTimeService bookTimeService;
	
	
	/* ynw start 电脑端根据桌子状态查数据 */
	@RequestMapping(params = "viewTable", method = RequestMethod.POST)
	public void viewTable(HttpServletRequest request,
			HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String seat = request.getParameter("seat");
		String status = request.getParameter("status");

		if (StringUtil.isEmpty(companyId)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "comId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (StringUtil.isEmpty(status)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "status不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<Reserve> reserveList = reserveService.getReserve(Integer
				.valueOf(companyId), seat, status);

		for (Reserve reserves : reserveList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", reserves.getId());
			record.addColumn("name", reserves.getName());
			record.addColumn("seat", reserves.getSeat());
			record.addColumn("status", reserves.getStatus());
			record.addColumn("meals", reserves.getMeals());	//最高容纳人数
			record.addColumn("img", reserves.getImg());
			record.addColumn("tableNo", reserves.getTableNo());
			record.addColumn("type", reserves.getTyoe());
			record.addColumn("isOpen", reserves.getIsOpen());
			record.addColumn("reserveid", reserves.getId());
			
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", reserveList.size());

		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String seat = request.getParameter("seat");

		if (StringUtil.isEmpty(companyId)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "comId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<Reserve> reserveList = reserveService.getReserve(Integer
				.valueOf(companyId), seat);

		for (Reserve reserves : reserveList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", reserves.getId());
			record.addColumn("name", reserves.getName());
			record.addColumn("seat", reserves.getSeat());
			record.addColumn("status", reserves.getStatus());
			record.addColumn("meals", reserves.getMeals());
			record.addColumn("img", reserves.getImg());
			record.addColumn("tableNo", reserves.getTableNo());
			record.addColumn("type", reserves.getTyoe());
			record.addColumn("isOpen", reserves.getIsOpen());
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", reserveList.size());

		new PushJson().outString(grid.toJSonString("list"), response);

	}
	
	// 更改餐桌状态
	@RequestMapping(params = "changeStatus", method = RequestMethod.POST)
	public void changeStatus(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		String status = request.getParameter("status");
		
		if (StringUtil.isEmpty(orderId)||StringUtil.isEmpty(status)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "id 或 status 不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		BookTime book = bookTimeService.getBookTime(Integer.valueOf(orderId));
		
		Reserve reserve = book.getReserveId();
		JSonMessage message = new JSonMessage();
		
		if(reserve != null){
			reserve.setStatus(status);
			reserveService.saveORupdate(reserve);
			
			message.addProperty("message", "开台成功");
			message.addProperty("success", true);
		}else{
			message.addProperty("message", "桌子不存在");
			message.addProperty("success", false);
		}

		new PushJson().outString(message.toJSonString(), response);
	}

	// 查询所有餐桌
	@RequestMapping(params = "allView", method = RequestMethod.POST)
	public void allView(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String seat = request.getParameter("seat");

		if (StringUtil.isEmpty(companyId)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "comId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<Reserve> reserveList = reserveService.getAllReserve(Integer
				.valueOf(companyId), seat);

		for (Reserve reserves : reserveList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", reserves.getId());
			record.addColumn("name", reserves.getName());
			record.addColumn("seat", reserves.getSeat());
			record.addColumn("status", reserves.getStatus());
			record.addColumn("meals", reserves.getMeals());
			record.addColumn("img", reserves.getImg());
			record.addColumn("tableNo", reserves.getTableNo());
			record.addColumn("type", reserves.getTyoe());
			String isOpen = "";
			if (reserves.getIsOpen()) {
				isOpen = "1";
			} else {
				isOpen = "0";
			}
			record.addColumn("isOpen", isOpen);
			record.addColumn("cancelTime", reserves.getCancelTime());
			record.addColumn("deposit", reserves.getDeposit());
			grid.addRecord(record);
		}
		
		grid.addProperties("totalCount", reserveList.size());

		new PushJson().outString(grid.toJSonString("list"), response);

	}
	
	// 查找餐桌
	@RequestMapping(params = "findid", method = RequestMethod.POST)
	public void findid(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "tableNo,companyId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		Reserve reserve =reserveService.find(Integer.valueOf(id));

		if (reserve == null) {
			message.addProperty("message", "餐桌不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		message.addProperty("success", true);
		message.addProperty("id", reserve.getId());
		message.addProperty("name", reserve.getName());
		message.addProperty("seat", reserve.getSeat());
		message.addProperty("meals", reserve.getMeals());
		message.addProperty("img", reserve.getImg());
		message.addProperty("tableNo", reserve.getTableNo());
		message.addProperty("price", reserve.getDeposit());
		message.addProperty("type", reserve.getTyoe());
		message.addProperty("logo", reserve.getCompanyId().getLogo());

		new PushJson().outString(message.toJSonString(), response);

	}

	// 查找餐桌
	@RequestMapping(params = "find", method = RequestMethod.POST)
	public void find(HttpServletRequest request, HttpServletResponse response) {
		String tableNo = request.getParameter("tableNo");
		String companyId = request.getParameter("cid");

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(tableNo) || StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "tableNo,companyId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		Reserve reserve = reserveService.getTable(tableNo, Integer
				.valueOf(companyId));

		if (reserve == null) {
			message.addProperty("message", "餐桌不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		message.addProperty("success", true);
		message.addProperty("id", reserve.getId());
		message.addProperty("name", reserve.getName());
		message.addProperty("seat", reserve.getSeat());
		message.addProperty("meals", reserve.getMeals());
		message.addProperty("img", reserve.getImg());
		message.addProperty("tableNo", reserve.getTableNo());
		message.addProperty("price", reserve.getDeposit());
		message.addProperty("type", reserve.getTyoe());
		message.addProperty("logo", reserve.getCompanyId().getLogo());
		

		new PushJson().outString(message.toJSonString(), response);

	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			message.addProperty("status", "false");
			System.out.println("id不能为空");
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		Reserve reserve = reserveService.find(Integer.valueOf(id));
		if (reserve == null) {
			message.addProperty("message", "餐桌不存在");
			message.addProperty("success", false);
			message.addProperty("status", "false");
			System.out.println("餐桌不存在");
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (!reserve.getStatus().equals("0")) {
			message.addProperty("message", "餐桌被预订或正在使用");
			message.addProperty("success", false);
			message.addProperty("status", "false");
			System.out.println("餐桌被预订或正在使用");
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());
		List<BookTime> bookTime = bookTimeService.getBookTime(Integer
				.valueOf(id), date);
		if (bookTime != null) {
			message.addProperty("message", "餐桌已被预订");
			message.addProperty("success", false);
			message.addProperty("status", "false");
			System.out.println("餐桌已被预订");
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		reserve.setIsDelete("1");
		reserveService.saveORupdate(reserve);
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		message.addProperty("status", "true");
		System.out.println("删除成功");
		new PushJson().outString(message.toJSonString(), response);
	}

	// 查看餐桌是否存在
	@RequestMapping(params = "getTableNo", method = RequestMethod.POST)
	public void getTableNo(HttpServletRequest request,
			HttpServletResponse response) {
		String tableNo = request.getParameter("tableNo");
		String companyId = request.getParameter("cid");
		String seat = request.getParameter("seat");
		String name = request.getParameter("name");

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(tableNo) || StringUtil.isEmpty(companyId)
				|| StringUtil.isEmpty(seat) || StringUtil.isEmpty(name)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (name.equals(tableNo + "号桌")) {
			Reserve reserve = reserveService.getTable(tableNo, Integer
					.valueOf(companyId), seat);
			if (reserve == null) {
				message.addProperty("message", "餐桌不存在");
				message.addProperty("status", "0");
			} else {
				message.addProperty("message", "餐桌已存在");
				message.addProperty("status", "1");
			}
		} else {
			Reserve reserve = reserveService.getTable(tableNo, Integer
					.valueOf(companyId), seat, name);
			if (reserve == null) {
				message.addProperty("message", "餐桌不存在");
				message.addProperty("status", "0");
			} else {
				message.addProperty("message", "餐桌已存在");
				message.addProperty("status", "1");
			}
		}

		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);

	}

	// 添加信息
	@RequestMapping(params = "save", method = RequestMethod.POST)
	public void save(HttpServletRequest request, HttpServletResponse response) {
		String tableNo = request.getParameter("tableNo");// 桌号
		String companyId = request.getParameter("cid");// 商家id
		String name = request.getParameter("name");// 餐桌名
		String seat = request.getParameter("seat");// 餐桌位置
		String meals = request.getParameter("meals");// 可容纳人数
		String deposit = request.getParameter("deposit");// 预定金额
		JSonMessage message = new JSonMessage();

		
		if (StringUtil.isEmpty(tableNo) || StringUtil.isEmpty(companyId)
				|| StringUtil.isEmpty(seat) || StringUtil.isEmpty(meals)
				|| StringUtil.isEmpty(deposit)
				) {
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
		
		if (name.equals(tableNo + "号桌")) {
			Reserve reserve = reserveService.getTable(tableNo, Integer
					.valueOf(companyId), seat);
			if (reserve != null) {
				message.addProperty("message", "餐桌已存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
		} else {
			Reserve reserve = reserveService.getTable(tableNo, Integer
					.valueOf(companyId), seat, name);
			if (reserve != null) {
				message.addProperty("message", "餐桌已存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
		}
		
		Reserve reserve = new Reserve();
		reserve.setCompanyId(company);
		reserve.setIsOpen(true);
		reserve.setStatus("0");
		reserve.setIsDelete("0");
		reserve.setTableNo(tableNo);
		if (!StringUtil.isEmpty(name)) {
			reserve.setName(name);
		} else {
			reserve.setName(tableNo + "号桌");
		}
		reserve.setSeat(seat);
		reserve.setTyoe("1");
		reserve.setMeals(meals);
		reserve.setDeposit(deposit);
		reserve.setImg("../common/img/tableware01.png");
		reserveService.saveORupdate(reserve);

		message.addProperty("message", "添加信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 餐桌详情
	@RequestMapping(params = "getReserve", method = RequestMethod.POST)
	public void getReserve(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");// 餐桌id
		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Reserve reserve = reserveService.find(Integer.valueOf(id));
		if (reserve == null) {
			message.addProperty("message", "餐桌不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		message.addProperty("id", reserve.getId());
		message.addProperty("name", reserve.getName());
		message.addProperty("seat", reserve.getSeat());
		message.addProperty("meals", reserve.getMeals());
		message.addProperty("img", reserve.getImg());
		message.addProperty("tableNo", reserve.getTableNo());
		message.addProperty("deposit", reserve.getDeposit());
		message.addProperty("type", reserve.getTyoe());
		message.addProperty("isOpen", reserve.getIsOpen());
		message.addProperty("status", reserve.getStatus());
		message.addProperty("cancelTime", reserve.getCancelTime());

		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 修改 信息
	@RequestMapping(params = "update", method = RequestMethod.POST)
	public void update(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");// 餐桌id
		// 选填项
		String seat = request.getParameter("seat");// 餐桌位置
		String tyoe = request.getParameter("tyoe");// 餐桌类型
		String meals = request.getParameter("meals");// 可容纳人数
		String deposit = request.getParameter("deposit");// 预定金额
		JSonMessage message = new JSonMessage();

		

		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Reserve reserve = reserveService.find(Integer.valueOf(id));
		if (reserve == null) {
			message.addProperty("message", "餐桌不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if (!StringUtil.isEmpty(seat)) {
			reserve.setSeat(seat);
		}
		if (!StringUtil.isEmpty(tyoe)) {
			reserve.setTyoe(tyoe);
		}
		if (!StringUtil.isEmpty(meals)) {
			reserve.setMeals(meals);
		}
		if (!StringUtil.isEmpty(deposit)) {
			reserve.setDeposit(deposit);
		}
	
		reserveService.saveORupdate(reserve);

		message.addProperty("message", "修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 是否开桌
	@RequestMapping(params = "updateIsOpen", method = RequestMethod.POST)
	public void updateIsOpen(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");// 餐桌id
		String isOpen = request.getParameter("isOpen");// 是否开桌
		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(isOpen)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Reserve reserve = reserveService.find(Integer.valueOf(id));
		if (reserve == null) {
			message.addProperty("message", "餐桌不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if(reserve.getStatus().equals("2")){
			message.addProperty("message", "餐桌正在就餐中,请结算后再操作");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if (isOpen.equals("0")) {
			reserve.setIsOpen(false);
		} else if (isOpen.equals("1")) {
			reserve.setIsOpen(true);
		} else {
			message.addProperty("message", "isOpen状态不正确");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		reserveService.saveORupdate(reserve);

		message.addProperty("message", "餐桌操作成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 修改餐桌状态
	@RequestMapping(params = "updateStatus", method = RequestMethod.POST)
	public void updateStatus(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");// 餐桌id
		String status = request.getParameter("status").trim();// 是否开桌
		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(status)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Reserve reserve = reserveService.find(Integer.valueOf(id));
		if (reserve == null) {
			message.addProperty("message", "餐桌不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if (status.equals("0") || status.equals("1") || status.equals("2")) {
			reserve.setStatus(status);
			reserveService.saveORupdate(reserve);
		} else {
			message.addProperty("message", "状态不正确");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		message.addProperty("message", "状态修改成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}