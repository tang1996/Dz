package com.dz.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Activity;
import com.dz.entity.Company;
import com.dz.entity.CompanyActivity;
import com.dz.service.IActivityService;
import com.dz.service.ICompanyActivityService;
import com.dz.service.ICompanyService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.JSonTree;
import com.dz.util.JSonTreeNode;
import com.dz.util.JsonUtil;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/companyActivity")
public class CompanyActivityController {

	@Autowired
	private ICompanyActivityService companyActivityService;

	@Autowired
	private IActivityService activityService;

	@Autowired
	private ICompanyService companyService;

	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response,
			CompanyActivity companyActivity) {

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<CompanyActivity> companyActivityList = companyActivityService
				.companyActivityList(companyActivity);

		for (CompanyActivity companyActivitys : companyActivityList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", companyActivitys.getId());
			record.addColumn("activity_id", companyActivitys.getActivityId());

			grid.addRecord(record);
		}
		grid.addProperties("totalCount", companyActivityList.size());
		grid.addProperties("totalSum", companyActivityList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 按类型查询
	@RequestMapping(params = "getCompanyActivity", method = RequestMethod.POST)
	public void getCompanyActivity(HttpServletRequest request,
			HttpServletResponse response) {
		String companyId = request.getParameter("companyId");// 商家ID
		String activityId = request.getParameter("activityId");// 总活动ID

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(activityId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<CompanyActivity> companyActivityList = companyActivityService
				.companyActivity(Integer.valueOf(companyId), Integer
						.valueOf(activityId), "1");

		for (CompanyActivity companyActivitys : companyActivityList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", companyActivitys.getId());
			if (companyActivitys.getActivityId().getId() == 1) {
				record.addColumn("name", "满" + companyActivitys.getBalance()
						+ "减" + companyActivitys.getBenefit());
			}
			if (companyActivitys.getActivityId().getId() == 2) {
				record.addColumn("name", "新用户立减"
						+ companyActivitys.getNewUser());
			}
			if (companyActivitys.getActivityId().getId() == 3) {
				record
						.addColumn("name", "全场" + companyActivitys.getSvg()
								+ "折");
			}
			if (companyActivitys.getActivityId().getId() == 4) {
				record.addColumn("name", "优惠券" + companyActivitys.getCoupon()
						+ "元");
			}
			if (!StringUtil.isEmpty(companyActivitys.getDepict())) {
				record.addColumn("depict", companyActivitys.getDepict());
			}
			if (!StringUtil.isEmpty(companyActivitys.getContent())) {
				record.addColumn("content", companyActivitys.getContent());
			}
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			if (companyActivitys.getEndTime().compareTo(date) >= 0) {
				record.addColumn("status", "2");// 未结束
			} else {
				record.addColumn("status", "1");// 已结束
			}
			record.addColumn("startTime", companyActivitys.getStartTime());
			record.addColumn("endTime", companyActivitys.getEndTime());
			record.addColumn("isOpen", companyActivitys.getIsOpen());

			grid.addRecord(record);
		}

		grid.addProperties("totalCount", companyActivityList.size());
		grid.addProperties("totalSum", companyActivityList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 按类型查询美食
	@RequestMapping(params = "getCompanyActivityMS", method = RequestMethod.POST)
	public void getCompanyActivityMS(HttpServletRequest request,
			HttpServletResponse response) {
		String companyId = request.getParameter("companyId");// 商家ID
		String activityId = request.getParameter("activityId");// 总活动ID

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(activityId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<CompanyActivity> companyActivityList = companyActivityService
				.companyActivity(Integer.valueOf(companyId), Integer
						.valueOf(activityId), "2");

		for (CompanyActivity companyActivitys : companyActivityList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", companyActivitys.getId());
			if (companyActivitys.getActivityId().getId() == 1) {
				record.addColumn("name", "满" + companyActivitys.getBalance()
						+ "减" + companyActivitys.getBenefit());
			}
			if (companyActivitys.getActivityId().getId() == 2) {
				record.addColumn("name", "新用户立减"
						+ companyActivitys.getNewUser());
			}
			if (companyActivitys.getActivityId().getId() == 3) {
				record
						.addColumn("name", "全场" + companyActivitys.getSvg()
								+ "折");
			}
			if (companyActivitys.getActivityId().getId() == 4) {
				record.addColumn("name", "优惠券" + companyActivitys.getCoupon()
						+ "元");
			}
			if (!StringUtil.isEmpty(companyActivitys.getDepict())) {
				record.addColumn("depict", companyActivitys.getDepict());
			}
			if (!StringUtil.isEmpty(companyActivitys.getContent())) {
				record.addColumn("content", companyActivitys.getContent());
			}
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			if (companyActivitys.getEndTime().compareTo(date) >= 0) {
				record.addColumn("status", "2");// 未结束
			} else {
				record.addColumn("status", "1");// 已结束
			}
			record.addColumn("startTime", companyActivitys.getStartTime());
			record.addColumn("endTime", companyActivitys.getEndTime());
			record.addColumn("isOpen", companyActivitys.getIsOpen());

			grid.addRecord(record);
		}

		grid.addProperties("totalCount", companyActivityList.size());
		grid.addProperties("totalSum", companyActivityList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 查询商家外卖活动
	@RequestMapping(params = "findWM", method = RequestMethod.POST)
	public void findWM(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");// 商家ID

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		List<CompanyActivity> companyActivityList = companyActivityService.companyActivity(Integer.valueOf(companyId), "1", date);
		
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for(CompanyActivity ca : companyActivityList){
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", ca.getId());
			switch(ca.getActivityId().getId()){
			case 1:
				record.addColumn("name", "满" + ca.getBalance()
						+ "减" + ca.getBenefit());
				record.addColumn("type", "1");
				break;
			case 2:
				record.addColumn("name", "新用户立减"
						+ ca.getNewUser());
				record.addColumn("type", "2");
				break;
			case 3:
				record
				.addColumn("name", "全场" + ca.getSvg()
						+ "折");
				record.addColumn("type", "3");
				break;
			case 4:
				record.addColumn("name", "优惠券" + ca.getCoupon()
						+ "元");
				record.addColumn("type", "4");
				break;
			}
			
			grid.addRecord(record);
		}
		
		grid.addProperties("totalCount", companyActivityList.size());
		new PushJson().outString(grid.toJSonString("list"), response);
		
	}
	
	
	// 查询商家外卖活动
	/*@RequestMapping(params = "findWM", method = RequestMethod.POST)
	public void findWM(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");// 商家ID

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		List<CompanyActivity> companyActivityList = companyActivityService
				.companyActivity(Integer.valueOf(companyId), "1", date);
		StringBuilder subtraction = new StringBuilder();
		message.addProperty("isOpen1", false);
		message.addProperty("isOpen2", false);
		message.addProperty("isOpen3", false);
		message.addProperty("isOpen4", false);
		int count = 0;
		for (CompanyActivity companyActivitys : companyActivityList) {
			if (companyActivitys.getActivityId().getId() == 1) {
				subtraction.append("满" + companyActivitys.getBalance() + "减"
						+ companyActivitys.getBenefit() + ";");
				message.addProperty("subtraction", subtraction.toString());
				message.addProperty("isOpen1", companyActivitys.getIsOpen());
				count++;
			}
			if (companyActivitys.getActivityId().getId() == 2) {
				message.addProperty("newUser", "新用户立减"
						+ companyActivitys.getNewUser() + "元");
				message.addProperty("isOpen2", companyActivitys.getIsOpen());
			}
			if (companyActivitys.getActivityId().getId() == 3) {
				message.addProperty("svg", "折扣商品" + companyActivitys.getSvg()
						+ "折起");
				message.addProperty("isOpen3", companyActivitys.getIsOpen());
			}
			if (companyActivitys.getActivityId().getId() == 4) {
				message.addProperty("coupon", "优惠券"
						+ companyActivitys.getCoupon() + "元");
				message.addProperty("isOpen4", companyActivitys.getIsOpen());
			}
			if (!StringUtil.isEmpty(companyActivitys.getDepict())) {
				message.addProperty("depict", companyActivitys.getDepict());
			}
			if (!StringUtil.isEmpty(companyActivitys.getContent())) {
				message.addProperty("content", companyActivitys.getContent());
			}

		}
		if (count == 0) {
			message.addProperty("count", companyActivityList.size());
		} else {
			message.addProperty("count", companyActivityList.size()
					- count + 1);
		}
		if (companyActivityList.size() > 0) {
			message.addProperty("success", true);
		} else {
			message.addProperty("success", false);
		}
		new PushJson().outString(message.toJSonString(), response);
	}*/

	// 查询商家美食活动
	@RequestMapping(params = "findMS", method = RequestMethod.POST)
	public void findMS(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");// 商家ID

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		List<CompanyActivity> companyActivityList = companyActivityService
				.companyActivity(Integer.valueOf(companyId), "2", date);
		StringBuilder subtraction = new StringBuilder();
		message.addProperty("isOpen1", false);
		message.addProperty("isOpen2", false);
		message.addProperty("isOpen3", false);
		message.addProperty("isOpen4", false);
		
		int count = 0;
		for (CompanyActivity companyActivitys : companyActivityList) {
			if (companyActivitys.getActivityId().getId() == 1) {
				subtraction.append("满" + companyActivitys.getBalance() + "减"
						+ companyActivitys.getBenefit() + ";");

				message.addProperty("subtraction", subtraction.toString());
				message.addProperty("isOpen1", companyActivitys.getIsOpen());
				count++;
			}
			if (companyActivitys.getActivityId().getId() == 2) {
				message.addProperty("newUser", "新用户立减"
						+ companyActivitys.getNewUser() + "元");
				message.addProperty("isOpen2", companyActivitys.getIsOpen());
			}
			if (companyActivitys.getActivityId().getId() == 3) {
				message.addProperty("svg", "折扣商品" + companyActivitys.getSvg()
						+ "折起");
				message.addProperty("isOpen3", companyActivitys.getIsOpen());
			}
			if (companyActivitys.getActivityId().getId() == 4) {
				message.addProperty("coupon", "优惠券"
						+ companyActivitys.getCoupon() + "元");
				message.addProperty("isOpen4", companyActivitys.getIsOpen());
			}
			if (!StringUtil.isEmpty(companyActivitys.getDepict())) {
				message.addProperty("depict", companyActivitys.getDepict());
			}
			if (!StringUtil.isEmpty(companyActivitys.getContent())) {
				message.addProperty("content", companyActivitys.getContent());
			}
		}
		if (count == 0) {
			message.addProperty("count", companyActivityList.size());
		} else {
			message.addProperty("count", companyActivityList.size()
					- count + 1);
		}
		if (companyActivityList.size() > 0) {
			message.addProperty("success", true);
		} else {
			message.addProperty("success", false);
		}
		new PushJson().outString(message.toJSonString(), response);
	}

	// // 通过首页活动查询搞活动商品
	// @RequestMapping(params = "getGoods", method = RequestMethod.POST)
	// public void getGoods(HttpServletRequest request,
	// HttpServletResponse response, CompanyActivity companyActivity) {
	// String id = request.getParameter("id");// 总活动ID
	// JSonMessage message = new JSonMessage();
	// if (StringUtil.isEmpty(id)) {
	// message.addProperty("message", "id不能为空");
	// message.addProperty("success", false);
	// new PushJson().outString(message.toJSonString(), response);
	// return;
	// }
	// JSonGrid grid = new JSonGrid();
	//		
	// List<CompanyActivity> companyActivityList = companyActivityService
	// .getList(Integer.valueOf(id), "1");
	//
	// if (companyActivityList != null) {
	// for (CompanyActivity companyActivitys : companyActivityList) {
	// JSonGridRecord record = new JSonGridRecord();
	// record.addColumn("id", companyActivitys.getId());
	// record.addColumn("benefit", companyActivitys.getBenefit());
	// record.addColumn("balance", companyActivitys.getBalance());
	// record.addColumn("content", companyActivitys.getContent());
	// record.addColumn("depict", companyActivitys.getDepict());
	// record.addColumn("startTime", companyActivitys.getStartTime());
	// record.addColumn("endTime", companyActivitys.getEndTime());
	// record.addColumn("svg", companyActivitys.getSvg());
	// record.addColumn("companyName", companyActivitys.getCompanyId()
	// .getName());
	// record.addColumn("companyLogo", companyActivitys.getCompanyId()
	// .getLogo());
	// record.addColumn("companyId", companyActivitys.getCompanyId()
	// .getId());
	//
	// grid.addRecord(record);
	//
	// }
	// }
	//
	// grid.addProperties("totalCount", companyActivityList.size());
	//
	// new PushJson().outString(grid.toJSonString("list"), response);
	//
	// }

	// 活动详情
	@RequestMapping(params = "find", method = RequestMethod.POST)
	public void find(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");// 商家ID

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		CompanyActivity companyActivity = companyActivityService
				.getCompanyActivity(Integer.valueOf(id));

		message.addProperty("id", companyActivity.getId());
		if (companyActivity.getActivityId().getId() == 1) {
			message.addProperty("balance", companyActivity.getBalance());
			message.addProperty("benefit", companyActivity.getBenefit());
		}
		if (companyActivity.getActivityId().getId() == 2) {
			message.addProperty("newUser", companyActivity.getNewUser());
		}
		if (companyActivity.getActivityId().getId() == 3) {
			message.addProperty("svg", companyActivity.getSvg());
		}
		if (companyActivity.getActivityId().getId() == 4) {
			message.addProperty("coupon", companyActivity.getCoupon());
		}
		if (!StringUtil.isEmpty(companyActivity.getDepict())) {
			message.addProperty("depict", companyActivity.getDepict());
		}
		if (!StringUtil.isEmpty(companyActivity.getContent())) {
			message.addProperty("content", companyActivity.getContent());
		}
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		if (companyActivity.getEndTime().compareTo(date) >= 0) {
			message.addProperty("status", "2");// 未结束
		} else {
			message.addProperty("status", "1");// 已结束
		}
		message.addProperty("startTime", companyActivity.getStartTime());
		message.addProperty("endTime", companyActivity.getEndTime());

		new PushJson().outString(message.toJSonString(), response);

	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request,
			CompanyActivity companyActivity, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyActivity.getId() + "")
				|| companyActivity.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		companyActivityService.delete(companyActivity.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加活动
	@RequestMapping(params = "save", method = RequestMethod.POST)
	public void save(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");// 商家id
		String activityId = request.getParameter("activityId");// 总活动id
		String startTime = request.getParameter("startTime");// 活动开始时间
		String endTime = request.getParameter("endTime");// 活动结束时间
		String type = request.getParameter("type");// 商家类型
		// 选填项
		String depict = request.getParameter("depict");// 活动描述
		String content = request.getParameter("content");// 活动内容
		String newUser = request.getParameter("newUser");// 新人优惠
		String coupon = request.getParameter("coupon");// 优惠券
		String balance = request.getParameter("balance");// 满减起步金额
		String benefit = request.getParameter("benefit");// 满减优惠金额
		String svg = request.getParameter("svg");// 折扣

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(activityId)
				|| StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime)
				|| StringUtil.isEmpty(type)) {
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

		Activity activity = activityService.getActivity(Integer
				.valueOf(activityId));
		if (activity == null) {
			message.addProperty("message", "活动不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<CompanyActivity> clist = companyActivityService.companyActivity(
				Integer.valueOf(companyId), Integer.valueOf(activityId), type);
		switch (Integer.valueOf(activityId)) {
		case 1:
			if (type.equals("1")) {
				if (clist.size() == 2) {
					message.addProperty("message", "外卖分类下只能添加两个满减活动");
					message.addProperty("success", false);
					new PushJson().outString(message.toJSonString(), response);
					return;
				}
			} else if (type.equals("2")) {
				if (clist.size() == 2) {
					message.addProperty("message", "美食分类下只能添加两个满减活动");
					message.addProperty("success", false);
					new PushJson().outString(message.toJSonString(), response);
					return;
				}
			}
			break;
		case 2:
			if (type.equals("1")) {
				if (clist.size() == 1) {
					message.addProperty("message", "外卖分类下只能添加一个新人立减活动 ");
					message.addProperty("success", false);
					new PushJson().outString(message.toJSonString(), response);
					return;
				}
			} else if (type.equals("2")) {
				if (clist.size() == 1) {
					message.addProperty("message", "美食分类下只能添加一个新人立减活动 ");
					message.addProperty("success", false);
					new PushJson().outString(message.toJSonString(), response);
					return;
				}
			}
			break;
		case 3:
			if (type.equals("1")) {
				if (clist.size() == 1) {
					message.addProperty("message", "外卖分类下只能添加一个折扣活动");
					message.addProperty("success", false);
					new PushJson().outString(message.toJSonString(), response);
					return;
				}
			} else if (type.equals("2")) {
				if (clist.size() == 1) {
					message.addProperty("message", "美食分类下只能添加一个折扣活动");
					message.addProperty("success", false);
					new PushJson().outString(message.toJSonString(), response);
					return;
				}
			}
			break;
		case 4:
			if (type.equals("1")) {
				if (clist.size() == 1) {
					message.addProperty("message", "外卖分类下只能添加一个优惠券活动");
					message.addProperty("success", false);
					new PushJson().outString(message.toJSonString(), response);
					return;
				}
			} else if (type.equals("2")) {
				if (clist.size() == 1) {
					message.addProperty("message", "美食分类下只能添加一个优惠券活动");
					message.addProperty("success", false);
					new PushJson().outString(message.toJSonString(), response);
					return;
				}
			}
			break;
		}

		CompanyActivity companyActivity = new CompanyActivity();
		companyActivity.setCompanyId(company);
		companyActivity.setActivityId(activity);
		companyActivity.setStartTime(startTime);
		companyActivity.setEndTime(endTime);
		companyActivity.setType(type);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		if (startTime.compareTo(date) >= 0) {
			companyActivity.setIsOpen(true);
		} else {
			companyActivity.setIsOpen(false);
		}
		if (endTime.compareTo(date) < 0) {
			message.addProperty("message", "活动结束时间不正确");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		/*
		 * List<CompanyActivity> start =
		 * companyActivityService.getTime(Integer.valueOf(companyId),
		 * Integer.valueOf(activityId), startTime, type); List<CompanyActivity>
		 * end = companyActivityService.getTime(Integer.valueOf(companyId),
		 * Integer.valueOf(activityId), endTime, type);
		 * 
		 * if(start == null || end == null){ message.addProperty("message",
		 * "同时间段已有同类型活动，请勿重复添加"); message.addProperty("success", false); new
		 * PushJson().outString(message.toJSonString(), response); return; }
		 */

		if (!StringUtil.isEmpty(newUser)) {
			if (!NumberUtils.isNumber(newUser)) {
				message.addProperty("message", "newUser值不正确");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			companyActivity.setNewUser(newUser);
		}
		if (!StringUtil.isEmpty(coupon)) {
			if (!NumberUtils.isNumber(coupon)) {
				message.addProperty("message", "coupon值不正确");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			companyActivity.setCoupon(coupon);
		}
		if (!StringUtil.isEmpty(svg)) {
			if (!NumberUtils.isNumber(svg)) {
				message.addProperty("message", "svg值不正确");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			companyActivity.setSvg(svg);
		}
		if (!StringUtil.isEmpty(balance) && !StringUtil.isEmpty(benefit)) {
			if (!NumberUtils.isNumber(balance)
					&& !NumberUtils.isNumber(benefit)) {
				message.addProperty("message", "balance或benefit值不正确");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			companyActivity.setBalance(balance);
			companyActivity.setBenefit(benefit);
		}
		if (!StringUtil.isEmpty(depict)) {
			companyActivity.setDepict(depict);
		}
		if (!StringUtil.isEmpty(content)) {
			companyActivity.setContent(content);
		}
		companyActivityService.saveORupdate(companyActivity);

		message.addProperty("message", "添加活动成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 修改活动
	@RequestMapping(params = "update", method = RequestMethod.POST)
	public void update(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");// 活动id
		// 选填项
		String depict = request.getParameter("depict");// 活动描述
		String content = request.getParameter("content");// 活动内容
		String startTime = request.getParameter("startTime");// 活动开始时间
		String endTime = request.getParameter("endTime");// 活动结束时间
		String newUser = request.getParameter("newUser");// 新人优惠
		String coupon = request.getParameter("coupon");// 优惠券
		String balance = request.getParameter("balance");// 满减起步金额
		String benefit = request.getParameter("benefit");// 满减优惠金额
		String svg = request.getParameter("svg");// 折扣

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		CompanyActivity companyActivity = companyActivityService
				.getCompanyActivity(Integer.valueOf(id));
		if (companyActivity == null) {
			message.addProperty("message", "活动不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if (!StringUtil.isEmpty(depict)) {
			companyActivity.setDepict(depict);
		}
		if (!StringUtil.isEmpty(content)) {
			companyActivity.setContent(content);
		}
		if (!StringUtil.isEmpty(startTime)) {
			companyActivity.setStartTime(startTime);
		}
		if (!StringUtil.isEmpty(endTime)) {
			companyActivity.setEndTime(endTime);
		}
		if (!StringUtil.isEmpty(newUser)) {
			if (!NumberUtils.isNumber(newUser)) {
				message.addProperty("message", "newUser值不正确");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			companyActivity.setNewUser(newUser);
		}
		if (!StringUtil.isEmpty(coupon)) {
			if (!NumberUtils.isNumber(coupon)) {
				message.addProperty("message", "coupon值不正确");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			companyActivity.setCoupon(coupon);
		}
		if (!StringUtil.isEmpty(svg)) {
			if (!NumberUtils.isNumber(svg)) {
				message.addProperty("message", "svg值不正确");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			companyActivity.setSvg(svg);
		}
		if (!StringUtil.isEmpty(balance) && !StringUtil.isEmpty(benefit)) {
			if (!NumberUtils.isNumber(balance)
					&& !NumberUtils.isNumber(benefit)) {
				message.addProperty("message", "balance或benefit值不正确");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			companyActivity.setBalance(balance);
			companyActivity.setBenefit(benefit);
		}
		companyActivityService.saveORupdate(companyActivity);

		message.addProperty("message", "修改活动成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 修改活动状态
	@RequestMapping(params = "updateStatus", method = RequestMethod.POST)
	public void updateStatus(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");// 活动id
		String status = request.getParameter("status");// 状态 0关闭 1 修改
		System.out.println("id====>" + id);
		System.out.println("status====>" + status);
		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(status)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		CompanyActivity companyActivity = companyActivityService
				.getCompanyActivity(Integer.valueOf(id));
		if (companyActivity == null) {
			message.addProperty("message", "商家活动不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if (status.equals("0")) {
			companyActivity.setIsOpen(false);
		} else if (status.equals("1")) {
			companyActivity.setIsOpen(true);
		} else {
			message.addProperty("message", "状态值不正确");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		companyActivityService.saveORupdate(companyActivity);

		message.addProperty("message", "修改活动成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}
	
	
	// 首页 按活动搜索
	@RequestMapping(params = "doSearch", method = RequestMethod.POST)
	public void doSearch(HttpServletRequest request,
			HttpServletResponse response) {
		String location = request.getParameter("location");
		String start = request.getParameter("start");// 起始页
		String limit = request.getParameter("limit");// 容量
		String activityId = request.getParameter("activityId");// 活动id

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(activityId) || StringUtil.isEmpty(location)
				|| StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonTree json = new JSonTree();
		
		List<Company> list = companyService.getList(Integer.valueOf(start), Integer.valueOf(limit));
		for(Company company : list){
			
			String distance = JsonUtil.getDistance(location, company
					.getCoordinates());
			if (StringUtils.isNumeric(distance)) {
				String range = distance + "m";
				if (Integer.valueOf(distance) > 1000) {
					range = Double.valueOf(distance.toString()) / 1000 + "km";
				}
				if (Integer.valueOf(distance) <= 1000000) {// 距离
					
					for(int i = 1; i< 3; i++){
						StringBuilder builder = new StringBuilder();
						List<CompanyActivity> companyActivityList = companyActivityService.companyActivity(company.getId(), Integer.valueOf(activityId),  i + "");
						JSonTreeNode node = new JSonTreeNode();
						node.addProperty("distance", range);
						node.addProperty("assess", company.getAssess());
						node.addProperty("position", company.getPosition());
						node.addProperty("id", company.getId());
						node.addProperty("name", company.getName());
						node.addProperty("isBusiness", company.getIsBusiness());
						node.addProperty("logo", company.getLogo());
						node.addProperty("isOpen", company.getIsOpen());
						node.addProperty("distance", range);
						node.addProperty("type", i);
						for(CompanyActivity activity : companyActivityList){
							switch(Integer.valueOf(activityId)){
							case 1:
								//满减
								builder.append("满"+ activity.getBalance() +"元 立减    " + activity.getBenefit() + " 元;");
								break;
							case 2:
								//新人
								builder.append("新人立减     " + activity.getNewUser() + " 元");
								break;
							case 3:
								//折扣
								builder.append("优惠    " + activity.getSvg() + " 折起");
								break;
							case 4:
								//优惠券
								builder.append("立即使用代金券    " + activity.getCoupon() + " 元");
								break;
							}
						}

						node.addProperty("activity", builder.toString());
						if(builder.length() > 0){
							json.addNode(node);
						}
					}
				}
			}
		}
		
		new PushJson().outString(json.toJSonString(), response);
		
	}

}