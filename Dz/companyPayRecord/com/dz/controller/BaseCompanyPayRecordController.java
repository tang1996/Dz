package com.dz.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Company;
import com.dz.entity.CompanyDetailed;
import com.dz.entity.CompanyPayRecord;
import com.dz.service.ICompanyDetailedService;
import com.dz.service.ICompanyPayRecordService;
import com.dz.service.ICompanyService;
import com.dz.util.ExcelUtils;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/base/companyPayRecord")
public class BaseCompanyPayRecordController {

	@Autowired
	private ICompanyPayRecordService companyPayRecordService;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private ICompanyDetailedService companyDetailedService;
	
	// 导出缴费明细	ynw
	@RequestMapping(params = "export", method = RequestMethod.POST)
	public void export(HttpServletRequest request, HttpServletResponse response) {
		
		String companyId = request.getParameter("companyId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		List<CompanyPayRecord> list = companyPayRecordService.companyPayRecordTotal(Integer.valueOf(companyId));
		
		String[][] datas = new String[list.size()][6];
		int i = 0;
		String CompanyName = "";
		
		for (CompanyPayRecord payRecord : list) {
			datas[i][0] = payRecord.getCompanyId().getName();
			CompanyName = payRecord.getCompanyId().getName();
			datas[i][1] = payRecord.getOrderNo();
			datas[i][2] = payRecord.getBalance();
			datas[i][3] = payRecord.getDate();
			datas[i][4] = payRecord.getRenewTime();
			if(payRecord.getIsAccount()){
				datas[i][5] = "是";
			}else{
				datas[i][5] = "否";
			}
			i++;
		}
		
		String Path = request.getRealPath("/");	//获得根路径 ynw
		int dzbase = Path.indexOf("\\DzBase\\");	//截取到给定字符串位	ynw
		
		String[] arr = ExcelUtils.companyPayDetailed(datas, Path.substring(0, dzbase), CompanyName);
		
		int dzplfrom = arr[2].indexOf("\\DzPlfrom\\");	//截取到给定字符串位	ynw
		
		message.addProperty("message", arr[1]);
		message.addProperty("success", arr[0]);
		
		message.addProperty("path", arr[2].substring(dzplfrom, arr[2].length()));	//将路径发送给前端	ynw
		new PushJson().outString(message.toJSonString(), response);
	}
	
	// 用户列表		ynw
	@RequestMapping(params = "companyPayView", method = RequestMethod.POST)
	public void companyPayView(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<CompanyPayRecord> list = companyPayRecordService.companyPayRecordTotal(Integer.valueOf(companyId));
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (CompanyPayRecord payRecord : list) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", payRecord.getId());
			record.addColumn("date", payRecord.getDate());
			if (payRecord.getCompanyId() != null) {
				Company company = payRecord.getCompanyId();
				record.addColumn("name", company.getName());
				record.addColumn("phone", company.getPhone());
				record.addColumn("address", company.getPosition());
			}
			record.addColumn("balance", payRecord.getBalance());
			record.addColumn("orderNo", payRecord.getOrderNo());
			record.addColumn("renewTime", payRecord.getRenewTime());
			record.addColumn("updateTime", payRecord.getUpdateTime());
			if(payRecord.getIsAccount()){
				record.addColumn("isAccount", "是");
			}else{
				record.addColumn("isAccount", "否");
			}
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", list.size());
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 用户列表
	@RequestMapping(params = "companyView", method = RequestMethod.POST)
	public void companyView(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<CompanyPayRecord> list = companyPayRecordService.companyPayRecord(Integer.valueOf(companyId));
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (CompanyPayRecord payRecord : list) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", payRecord.getId());
			record.addColumn("date", payRecord.getDate());
			if (payRecord.getCompanyId() != null) {
				Company company = payRecord.getCompanyId();
				record.addColumn("name", company.getName());
				record.addColumn("phone", company.getPhone());
				record.addColumn("address", company.getPosition());
			}
			record.addColumn("balance", payRecord.getBalance());
			record.addColumn("orderNo", payRecord.getOrderNo());
			record.addColumn("renewTime", payRecord.getRenewTime());
			record.addColumn("updateTime", payRecord.getUpdateTime());
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", list.size());
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 续费
	@RequestMapping(params = "save", method = RequestMethod.POST)
	public void save(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String balance = request.getParameter("balance");
		String renewTime = request.getParameter("renewTime");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(balance) || StringUtil.isEmpty(renewTime)) {
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

		CompanyPayRecord companyPayRecord = new CompanyPayRecord();
		companyPayRecord.setBalance(balance);
		companyPayRecord.setRenewTime(renewTime);
		companyPayRecord.setCompanyId(company);
		companyPayRecord.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		companyPayRecord.setIsAccount(false);
		companyPayRecordService.saveORupdate(companyPayRecord);

		message.addProperty("success", true);
		message.addProperty("message", "缴费成功");
		new PushJson().outString(message.toJSonString(), response);
	}

	// 缴费记录查询
	@RequestMapping(params = "payRecord", method = RequestMethod.POST)
	public void payRecord(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(startDate) || StringUtil.isEmpty(endDate)
				|| StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		List<CompanyPayRecord> list = companyPayRecordService.getList(Integer.valueOf(companyId), startDate, endDate,
				Integer.valueOf(start), Integer.valueOf(limit));
		Long count = companyPayRecordService.getList(Integer.valueOf(companyId), startDate, endDate);
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (CompanyPayRecord payRecord : list) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", payRecord.getId());
			record.addColumn("date", payRecord.getDate());
			if (payRecord.getCompanyId() != null) {
				Company company = payRecord.getCompanyId();
				record.addColumn("name", company.getName());
				record.addColumn("phone", company.getPhone());
				record.addColumn("address", company.getPosition());
			}
			record.addColumn("balance", payRecord.getBalance());
			record.addColumn("orderNo", payRecord.getOrderNo());
			record.addColumn("renewTime", payRecord.getRenewTime());
			record.addColumn("updateTime", payRecord.getUpdateTime());
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", count);
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 人工审核
	@RequestMapping(params = "update", method = RequestMethod.POST)
	public void update(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		CompanyPayRecord companyPayRecord = companyPayRecordService.find(Integer.valueOf(id));
		if (companyPayRecord == null) {
			message.addProperty("message", "记录不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (companyPayRecord.getCompanyId() == null) {
			message.addProperty("message", "商家有误");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		Company company = companyPayRecord.getCompanyId();
		companyPayRecord.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		companyPayRecord.setOrderNo("JF" + company.getId() + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		companyPayRecord.setIsAccount(true);

		CompanyDetailed companyDetailed = companyDetailedService.getCompany(company.getId());
		if (companyDetailed == null) {
			message.addProperty("message", "商家id有误");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String date = "";
			if (!StringUtil.isEmpty(companyDetailed.getExpireTime())) {
				date = companyDetailed.getExpireTime();
			} else {
				date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			}
			Date beginDate = format.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(beginDate);
			calendar.add(Calendar.DAY_OF_YEAR, Integer.valueOf(companyPayRecord.getRenewTime()));
			String endDate = format.format(calendar.getTime());
			companyDetailed.setExpireTime(endDate);
			companyDetailedService.saveORupdate(companyDetailed);
		} catch (ParseException e) {
			e.printStackTrace();
			message.addProperty("message", "时间计算失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		companyPayRecordService.saveORupdate(companyPayRecord);

		message.addProperty("success", true);
		message.addProperty("message", "审核成功");
		new PushJson().outString(message.toJSonString(), response);
	}

	// 待审核列表
	@RequestMapping(params = "payView", method = RequestMethod.POST)
	public void payView(HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)) {
			message.addProperty("message", "缺少分页信息");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<CompanyPayRecord> list = companyPayRecordService.getPayView();
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (CompanyPayRecord payRecord : list) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", payRecord.getId());
			record.addColumn("date", payRecord.getDate());
			if (payRecord.getCompanyId() != null) {
				Company company = payRecord.getCompanyId();
				record.addColumn("name", company.getName());
				record.addColumn("phone", company.getPhone());
				record.addColumn("address", company.getPosition());
			}
			record.addColumn("balance", payRecord.getBalance());
			record.addColumn("renewTime", payRecord.getRenewTime());
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", list.size());
		new PushJson().outString(grid.toJSonString("list"), response);
	}

}