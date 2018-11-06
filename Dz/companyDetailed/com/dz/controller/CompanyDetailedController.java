package com.dz.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.dz.entity.SalerInfo;
import com.dz.service.ICompanyDetailedService;
import com.dz.service.ICompanyService;
import com.dz.service.ISalerInfoService;
import com.dz.util.ExcelUtils;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/companyDetailed")
public class CompanyDetailedController {

	@Autowired
	private ICompanyDetailedService companyDetailedService;

	@Autowired
	private ISalerInfoService salerService;

	@Autowired
	private ICompanyService companyService;
	
	// 导出缴费明细	ynw
	@RequestMapping(params = "export", method = RequestMethod.POST)
	public void export(HttpServletRequest request, HttpServletResponse response) {
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String city = request.getParameter("city");
		
		System.out.println("startTime====>" + startTime);
		System.out.println("endTime====>" + endTime);
		System.out.println("city====>" + city);

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime) || StringUtil.isEmpty(city)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		startTime = startTime.substring(0, 10) + " 00:00:00";
		endTime = endTime.substring(0, 10) + " 23:59:59";
		
		CompanyDetailed companyDetailed = new CompanyDetailed();
		SalerInfo salerInfo = new SalerInfo();
		salerInfo.setCity(city);
		companyDetailed.setSalerId(salerInfo);
		
		List<CompanyDetailed> companyDetailedList = companyDetailedService.companyDetailedList(companyDetailed,startTime,endTime);
		
		String[][] datas = new String[companyDetailedList.size()][9];
		int i = 0;

		
		String newDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		for (CompanyDetailed companyDetaileds : companyDetailedList) {
			
			Date beginDate;
			try {
				beginDate = format.parse(newDate);
				Date endDate = format.parse(companyDetaileds.getExpireTime());
				int day = (int) ((endDate.getTime() - beginDate.getTime()) / (1000 * 60 * 60 * 24));
				
				datas[i][0] = companyDetaileds.getCompanyId().getName();
				datas[i][1] = companyDetaileds.getCompanyId().getPhone();
				datas[i][2] = companyDetaileds.getSalerId().getName();
				datas[i][3] = companyDetaileds.getSalerId().getPhone();
				datas[i][4] = companyDetaileds.getSalerId().getCity();
				datas[i][5] = companyDetaileds.getExpireTime();
				datas[i][6] = day+"";
				datas[i][7] = companyDetaileds.getCompanyId().getPosition();
				datas[i][8] = companyDetaileds.getCost();
				i++;
				
			} catch (ParseException e) {
				e.printStackTrace();}
		}
		
		String Path = request.getRealPath("/");	//获得根路径 ynw
		int dzbase = Path.indexOf("\\DzBase\\");	//截取到给定字符串位	ynw

		String[] arr = ExcelUtils.companyDetailed(datas, Path.substring(0, dzbase), city);
		
		int dzplfrom = arr[2].indexOf("\\DzPlfrom\\");	//截取到给定字符串位	ynw
		
		message.addProperty("message", arr[1]);
		message.addProperty("success", arr[0]);
		
		message.addProperty("path", arr[2].substring(dzplfrom, arr[2].length()));	//将路径发送给前端	ynw
		new PushJson().outString(message.toJSonString(), response);
	}

	// 用户列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response) {
		String salerPhone = request.getParameter("salerPhone");
		
		String salerCity = request.getParameter("salerCity");	/*ynw xm*/
		CompanyDetailed companyDetailed = new CompanyDetailed();
		SalerInfo Csaler = new SalerInfo();	/*ynw*/
		if (!StringUtil.isEmpty(salerPhone)) {
			//SalerInfo saler = salerService.getPhone(salerPhone);
			Csaler.setPhone(salerPhone);		/*ynw*/
			companyDetailed.setSalerId(Csaler);
		}
		
		if (!StringUtil.isEmpty(salerCity)) {		/*ynw start*/
			//SalerInfo saler = salerService.countList(saler);
			Csaler.setCity(salerCity);
			companyDetailed.setSalerId(Csaler);
		}	/*ynw end*/
		
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<CompanyDetailed> companyDetailedList = companyDetailedService.companyDetailedList(companyDetailed);

		for (CompanyDetailed companyDetaileds : companyDetailedList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", companyDetaileds.getId());
			if (companyDetaileds.getCompanyId() != null) {
				Company company = companyDetaileds.getCompanyId();
				record.addColumn("companyId", company.getId());
				record.addColumn("companyName", company.getName());
				record.addColumn("companyPhone", company.getPhone());
				record.addColumn("address", company.getPosition());
			}
			if (companyDetaileds.getSalerId() != null) {
				SalerInfo saler = companyDetaileds.getSalerId();
				record.addColumn("salerId", saler.getId());
				record.addColumn("salerName", saler.getName());
				record.addColumn("salerPhone", saler.getPhone());
				record.addColumn("city", saler.getCity());
			}
			record.addColumn("expireTime", companyDetaileds.getExpireTime());
			record.addColumn("cost", companyDetaileds.getCost());
			String newDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date beginDate = format.parse(newDate);
				Date endDate = format.parse(companyDetaileds.getExpireTime());
				int day = (int) ((endDate.getTime() - beginDate.getTime()) / (1000 * 60 * 60 * 24));
				String status = "运行中";
				if (day < 0) {
					status = "已欠费";
				}
				record.addColumn("RemainTime", day);
				record.addColumn("status", status);
			} catch (ParseException e) {
				e.printStackTrace();
				record.addColumn("RemainTime", "时间计算错误");
				record.addColumn("status", "未知状态");
			}
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", companyDetailedList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除用户
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, CompanyDetailed companyDetailed, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyDetailed.getId() + "") || companyDetailed.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		companyDetailedService.delete(companyDetailed.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加商家
	@RequestMapping(params = "save", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");// 位置坐标
		String salerId = request.getParameter("salerId");// 推广员id
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(salerId) || StringUtil.isEmpty(companyId)) {
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

		SalerInfo saler = salerService.getid(Integer.valueOf(salerId));
		if (saler == null) {
			message.addProperty("message", "业务员不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		CompanyDetailed companyDetailed = new CompanyDetailed();
		companyDetailed.setSalerId(saler);
		companyDetailed.setCompanyId(company);
		// companyDetailed.setExpireTime(new SimpleDateFormat("yyyy-MM-dd
		// HH:mm:ss").format(new Date()));

		companyDetailedService.saveORupdate(companyDetailed);
		message.addProperty("message", "保存成功");
		message.addProperty("id", companyDetailed.getId());
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}


}
