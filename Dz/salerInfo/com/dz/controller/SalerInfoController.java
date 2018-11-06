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

import com.dz.entity.SaleExamine;
import com.dz.entity.SalerCompany;
import com.dz.entity.SalerInfo;
import com.dz.entity.SalerPower;
import com.dz.service.ISaleExamineService;
import com.dz.service.ISalerCompanyService;
import com.dz.service.ISalerInfoService;
import com.dz.service.ISalerPowerService;
import com.dz.util.ExcelUtils;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PerformanceAppraisalUtil;
import com.dz.util.PushJson;
import com.dz.util.SmessageUtils;
import com.dz.util.StringUtil;
import com.dz.util.TokenUtil;

@Controller
@RequestMapping("/salerInfo")
public class SalerInfoController {

	@Autowired
	private ISalerInfoService salerInfoService;

	@Autowired
	private ISaleExamineService saleExamineService;

	@Autowired
	private ISalerCompanyService salerCompanyService;

	@Autowired
	private ISalerPowerService salerPowerService;

	// 用户列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response) {
		String phone = request.getParameter("phone");

		SalerInfo salerInfo = new SalerInfo();
		if (!StringUtil.isEmpty(phone)) {
			salerInfo.setPhone(phone);
		}
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		List<SalerInfo> salerInfoList = salerInfoService.salerInfoList(salerInfo);
		for (SalerInfo salerInfos : salerInfoList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", salerInfos.getId());
			record.addColumn("code", salerInfos.getCode());
			record.addColumn("age", salerInfos.getAge());
			record.addColumn("createTime", salerInfos.getCreateTime());
			record.addColumn("education", salerInfos.getEducation());
			record.addColumn("cardNo", salerInfos.getCardNo());
			record.addColumn("city", salerInfos.getCity());
			record.addColumn("sex", salerInfos.getSex());
			record.addColumn("phone", salerInfos.getPhone());
			record.addColumn("name", salerInfos.getName());

			grid.addRecord(record);
		}
		grid.addProperties("totalCount", salerInfoList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除用户
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, SalerInfo saler, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(saler.getId() + "") || saler.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		salerInfoService.delete(saler.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 保存
	@RequestMapping(params = "save", method = RequestMethod.POST)
	public void save(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String salerPowerId = request.getParameter("salerPowerId");
		System.out.println(salerPowerId);
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		SaleExamine saleExamine = saleExamineService.getid(Integer.valueOf(id));
		if (saleExamine == null) {
			message.addProperty("message", "推广员信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		SalerPower salerPower = salerPowerService.getid(Integer.valueOf(salerPowerId));
		if (salerPower == null) {
			message.addProperty("message", "职务信息有误");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		String randomCode = TokenUtil.getRandomChar(8);
		SalerInfo salerInfo = new SalerInfo();
		salerInfo.setCardNo(saleExamine.getCardNo());
		salerInfo.setCity(saleExamine.getCity());
		salerInfo.setCode(randomCode);
		salerInfo.setCreateTime(saleExamine.getCreateTime());
		salerInfo.setEducation(saleExamine.getEducation());
		salerInfo.setName(saleExamine.getName());
		salerInfo.setPhone(saleExamine.getPhone());
		salerInfo.setAge(saleExamine.getAge());
		salerInfo.setSex(saleExamine.getSex());
		salerInfo.setSalerPowerId(salerPower);
		salerInfoService.saveORupdate(salerInfo);

		saleExamineService.delete(id);
		message.addProperty("message", "修改信息成功");
		message.addProperty("success", true);
		new SmessageUtils().sendToSaler(salerInfo.getPhone(), salerInfo.getCode(), salerPower.getName());	//将审核通过的业务员推广码发送到对应业务员手机号码上 ynw
		new PushJson().outString(message.toJSonString(), response);
	}

	// 修改信息
	@RequestMapping(params = "update", method = RequestMethod.POST)
	public void update(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		// 选填
		String city = request.getParameter("city");
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		SalerInfo salerInfo = salerInfoService.getid(Integer.valueOf(id));
		if (salerInfo == null) {
			message.addProperty("message", "骑手信息不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if (!StringUtil.isEmpty(city)) {
			salerInfo.setCity(city);
		}
		salerInfoService.saveORupdate(salerInfo);

		message.addProperty("message", "修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 个人信息
	@RequestMapping(params = "info", method = RequestMethod.POST)
	public void info(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		SalerInfo salerInfo = salerInfoService.getid(Integer.valueOf(id));
		if (salerInfo == null) {
			message.addProperty("message", "token验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		JSonGridRecord record = new JSonGridRecord();
		record.addColumn("id", salerInfo.getId());
		record.addColumn("code", salerInfo.getCode());
		record.addColumn("age", salerInfo.getAge());
		record.addColumn("createTime", salerInfo.getCreateTime());
		record.addColumn("education", salerInfo.getEducation());
		record.addColumn("cardNo", salerInfo.getCardNo());
		record.addColumn("city", salerInfo.getCity());
		record.addColumn("sex", salerInfo.getSex());
		record.addColumn("phone", salerInfo.getPhone());
		record.addColumn("name", salerInfo.getName());

		grid.addRecord(record);

		grid.addProperties("totalCount", 1);
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 团队统计方法
	public int[] teamCount(String date, String code, int id) {
		int otherNum = 0;
		int mainNum = 0;
		int deputyNum = 0;
		int[] arr = new int[]{otherNum,mainNum,deputyNum};
		SalerCompany salerCompany = new SalerCompany();
		if (!StringUtil.isEmpty(date)) {
			salerCompany.setCreateTime(date);
		}
		if ("city".equals(code)) {
			List<SalerInfo> salerList = salerInfoService.getBossId(id);
			for (SalerInfo city : salerList) {
				salerCompany.setSalerId(city.getId());
				Object[] num = salerCompanyService.count(salerCompany);
				if (num != null) {
					otherNum = otherNum + Integer.valueOf(num[1].toString());
					mainNum = Integer.valueOf(num[2].toString());
					deputyNum = Integer.valueOf(num[3].toString());
				}
				List<SalerInfo> salerLists = salerInfoService.getBossId(city.getId());
				for (SalerInfo region : salerLists) {
					salerCompany.setSalerId(region.getId());
					Object[] nums = salerCompanyService.count(salerCompany);
					if (nums != null) {
						otherNum = otherNum + Integer.valueOf(nums[1].toString());
						mainNum = Integer.valueOf(nums[2].toString());
						deputyNum = Integer.valueOf(nums[3].toString());
					}
				}
			}
		}
		if ("region".equals(code)) {
			List<SalerInfo> salerList = salerInfoService.getBossId(id);
			for (SalerInfo region : salerList) {
				salerCompany.setSalerId(region.getId());
				Object[] num = salerCompanyService.count(salerCompany);
				if (num != null) {
					otherNum = otherNum + Integer.valueOf(num[1].toString());
					mainNum = Integer.valueOf(num[2].toString());
					deputyNum = Integer.valueOf(num[3].toString());
				}
			}
		}
		
		arr[0] = otherNum;
		arr[1] = mainNum;
		arr[2] = deputyNum;
		return arr;
	}
	
	// 导出 团队统计方法
	public int[] totailCount(String startTime, String endTime, String code, int id) {
		int mainNum = 0;
		int deputyNum = 0;
		int[] arr = new int[] { mainNum, deputyNum };
		SalerCompany salerCompany = new SalerCompany();
		if ("city".equals(code)) {
			List<SalerInfo> salerList = salerInfoService.getBossId(id);
			for (SalerInfo city : salerList) {
				salerCompany.setSalerId(city.getId());
				Object[] num = salerCompanyService.count(salerCompany, startTime, endTime);
				if (num != null) {
					mainNum = mainNum + Integer.valueOf(num[2].toString());
					deputyNum = deputyNum + Integer.valueOf(num[3].toString());
				}
				List<SalerInfo> salerLists = salerInfoService.getBossId(city.getId());
				for (SalerInfo region : salerLists) {
					salerCompany.setSalerId(region.getId());
					Object[] nums = salerCompanyService.count(salerCompany, startTime, endTime);
					if (nums != null) {
						mainNum = mainNum + Integer.valueOf(nums[2].toString());
						deputyNum = deputyNum + Integer.valueOf(nums[3].toString());
					}
				}
			}
		}
		if ("region".equals(code)) {
			List<SalerInfo> salerList = salerInfoService.getBossId(id);
			for (SalerInfo region : salerList) {
				salerCompany.setSalerId(region.getId());
				Object[] num = salerCompanyService.count(salerCompany);
				if (num != null) {
					mainNum = mainNum + Integer.valueOf(num[2].toString());
					deputyNum = deputyNum + Integer.valueOf(num[3].toString());
				}
			}
		}

		arr[0] = mainNum;
		arr[1] = deputyNum;
		return arr;
	}

	// 月绩效统计
	@RequestMapping(params = "monCount", method = RequestMethod.POST)
	public void monCount(HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		String score = request.getParameter("score");
		String power = request.getParameter("power");
		String city = request.getParameter("city");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(start) || StringUtil.isEmpty(limit) || StringUtil.isEmpty(power)
				|| StringUtil.isEmpty(city)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		SalerInfo salerInfo = new SalerInfo();
		if (!"0".equals(power)) {
			SalerPower salerpower = salerPowerService.getid(Integer.valueOf(power));
			if (salerpower == null) {
				message.addProperty("message", "没有对应职务");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			salerInfo.setSalerPowerId(salerpower);
		}
		salerInfo.setCity(city);
		List<SalerInfo> count = salerInfoService.countList(salerInfo);
		List<SalerInfo> list = salerInfoService.countList(salerInfo, Integer.valueOf(start), Integer.valueOf(limit));
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (SalerInfo saler : list) {
			int selfNum = 0;
			int otherNum = 0;
			int mainNum = 0;
			int deputyNum = 0;
			int otherMainNum = 0;
			int otherDeputyNum = 0;
			SalerCompany salerCompany = new SalerCompany();
			String date = new SimpleDateFormat("yyyy-MM").format(new Date()) + "-01 00:00:00";
			salerCompany.setCreateTime(date);
			salerCompany.setSalerId(saler.getId());
			Object[] obj = salerCompanyService.count(salerCompany);
			if (obj != null) {
				selfNum = Integer.valueOf(obj[1].toString());
				mainNum = Integer.valueOf(obj[2].toString());
				deputyNum = Integer.valueOf(obj[3].toString());
			}
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("power", saler.getSalerPowerId().getName());
			int[] arr = teamCount(date, saler.getSalerPowerId().getCode(), saler.getId());
			otherNum = arr[0];
			otherMainNum = arr[1];
			otherDeputyNum = arr[2];

			record.addColumn("selfNum", selfNum);
			record.addColumn("otherNum", otherNum);
			record.addColumn("totalNum", selfNum + otherNum);
			record.addColumn("mainNum", mainNum);
			record.addColumn("deputyNum", deputyNum);
			record.addColumn("otherMainNum", otherMainNum);
			record.addColumn("otherDeputyNum", otherDeputyNum);
			record.addColumn("salerName", saler.getName());
			record.addColumn("salerPhone", saler.getPhone());
			record.addColumn("city", saler.getCity());
			record.addColumn("id", saler.getId());
			String assess = new PerformanceAppraisalUtil().getMark(selfNum + otherNum);
			record.addColumn("assess", assess);
			if (!StringUtil.isEmpty(score)) {
				boolean flg = new PerformanceAppraisalUtil().isView(score, mainNum + otherMainNum);
				if (flg) {
					grid.addRecord(record);
				}
			} else {
				grid.addRecord(record);
			}
		}
		grid.addProperties("totalCount", count.size());
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 总用户统计
	@RequestMapping(params = "totalCount", method = RequestMethod.POST)
	public void totalCount(HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)) {
			message.addProperty("message", "缺少分页信息");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		SalerInfo salerInfo = new SalerInfo();
		List<SalerInfo> count = salerInfoService.countList(salerInfo);
		List<SalerInfo> list = salerInfoService.countList(salerInfo, Integer.valueOf(start), Integer.valueOf(limit));
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (SalerInfo saler : list) {
			int selfNum = 0;
			int otherNum = 0;
			int mainNum = 0;
			int deputyNum = 0;
			int otherMainNum = 0;
			int otherDeputyNum = 0;
			SalerCompany salerCompany = new SalerCompany();
			salerCompany.setSalerId(saler.getId());
			Object[] obj = salerCompanyService.count(salerCompany);
			if (obj != null) {
				selfNum = Integer.valueOf(obj[1].toString());
				mainNum = Integer.valueOf(obj[2].toString());
				deputyNum = Integer.valueOf(obj[3].toString());
			}
			if (saler.getSalerPowerId() != null) {
				int[] arr = teamCount("", saler.getSalerPowerId().getCode(), saler.getId());
				otherNum = arr[0];
				otherMainNum = arr[1];
				otherDeputyNum = arr[2];
			}

			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("selfNum", selfNum);
			record.addColumn("otherNum", otherNum);
			record.addColumn("totalNum", selfNum + otherNum);
			record.addColumn("mainNum", mainNum);
			record.addColumn("deputyNum", deputyNum);
			record.addColumn("otherMainNum", otherMainNum);
			record.addColumn("otherDeputyNum", otherDeputyNum);
			record.addColumn("salerName", saler.getName());
			record.addColumn("salerPhone", saler.getPhone());
			record.addColumn("city", saler.getCity());
			record.addColumn("id", saler.getId());
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", count.size());
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 团队明细
	@RequestMapping(params = "teamCount", method = RequestMethod.POST)
	public void teamCount(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		SalerCompany salerCompany = new SalerCompany();
		String date = new SimpleDateFormat("yyyy-MM").format(new Date()) + "-01 00:00:00";
		salerCompany.setCreateTime(date);

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		SalerInfo saler = salerInfoService.getid(Integer.valueOf(id));
		if (saler.getSalerPowerId() != null) {
			if ("city".equals(saler.getSalerPowerId().getCode())) {
				List<SalerInfo> salerList = salerInfoService.getBossId(Integer.valueOf(id));
				for (SalerInfo city : salerList) {
					JSonGridRecord record = new JSonGridRecord();
					int otherNum = 0;
					int selfNum = 0;
					int mainNum = 0;
					int deputyNum = 0;
					int otherMainNum = 0;
					int otherDeputyNum = 0;
					salerCompany.setSalerId(city.getId());
					Object[] num = salerCompanyService.count(salerCompany);
					if (num != null) {
						selfNum = Integer.valueOf(num[1].toString());
						mainNum = Integer.valueOf(num[2].toString());
						deputyNum = Integer.valueOf(num[3].toString());
					}
					List<SalerInfo> salerLists = salerInfoService.getBossId(city.getId());
					for (SalerInfo region : salerLists) {
						salerCompany.setSalerId(region.getId());
						Object[] nums = salerCompanyService.count(salerCompany);
						if (nums != null) {
							otherNum = otherNum + Integer.valueOf(nums[1].toString());
							otherMainNum = otherMainNum + Integer.valueOf(nums[2].toString());
							otherDeputyNum = otherDeputyNum + Integer.valueOf(nums[3].toString());
						}
					}
					record.addColumn("id", city.getId());
					record.addColumn("num", otherNum);
					record.addColumn("name", city.getName());
					record.addColumn("selfNum", selfNum);
					record.addColumn("mainNum", mainNum);
					record.addColumn("deputyNum", deputyNum);
					record.addColumn("otherMainNum", otherMainNum);
					record.addColumn("otherDeputyNum", otherDeputyNum);
					if (city.getSalerPowerId() != null) {
						record.addColumn("power", city.getSalerPowerId().getName());
					}
					record.addColumn("cardNo", city.getCardNo());
					record.addColumn("phone", city.getPhone());
					grid.addRecord(record);
				}
			} else if ("region".equals(saler.getSalerPowerId().getCode())) {
				List<SalerInfo> salerList = salerInfoService.getBossId(Integer.valueOf(id));
				for (SalerInfo region : salerList) {
					JSonGridRecord record = new JSonGridRecord();
					salerCompany.setSalerId(region.getId());
					Object[] num = salerCompanyService.count(salerCompany);
					int selfNum = 0;
					int mainNum = 0;
					int deputyNum = 0;
					if (num != null) {
						selfNum = Integer.valueOf(num[1].toString());
						mainNum = Integer.valueOf(num[2].toString());
						deputyNum = Integer.valueOf(num[3].toString());
					}
					record.addColumn("id", region.getId());
					record.addColumn("num", selfNum);
					record.addColumn("mainNum", mainNum);
					record.addColumn("deputyNum", deputyNum);
					if (region.getSalerPowerId() != null) {
						record.addColumn("power", region.getSalerPowerId().getName());
					}
					record.addColumn("name", region.getName());
					record.addColumn("cardNo", region.getCardNo());
					record.addColumn("phone", region.getPhone());
					grid.addRecord(record);
				}
			} else {
				JSonGridRecord record = new JSonGridRecord();
				record.addColumn("num", 0);
				record.addColumn("name", "业务员没有下级");
				record.addColumn("phone", "业务员没有下级");
				grid.addRecord(record);
			}
		}
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 导出绩效
	@RequestMapping(params = "export", method = RequestMethod.POST)
	public void export(HttpServletRequest request, HttpServletResponse response) {
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String city = request.getParameter("city");
		
		System.out.println("startTime====>" + startTime);
		System.out.println("endTime====>" + endTime);
		System.out.println("city====>" + city);

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime) /*|| StringUtil.isEmpty(city)*/) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		startTime = startTime.substring(0, 10) + " 00:00:00";
		endTime = endTime.substring(0, 10) + " 23:59:59";

		SalerInfo salerInfo = new SalerInfo();
		salerInfo.setCity(city);
		List<SalerInfo> list = salerInfoService.countList(salerInfo);

		String[][] datas = new String[list.size()][12];
		for (int i = 0; i < list.size(); i++) {
			int mainNum = 0;
			int deputyNum = 0;
			int otherMainNum = 0;
			int otherDeputyNum = 0;
			SalerInfo saler = list.get(i);
			SalerCompany salerCompany = new SalerCompany();
			salerCompany.setSalerId(saler.getId());
			Object[] obj = salerCompanyService.count(salerCompany, startTime, endTime);
			if (obj != null) {
				mainNum = Integer.valueOf(obj[2].toString());
				deputyNum = Integer.valueOf(obj[3].toString());
			}
			int[] arr = totailCount(startTime, endTime, saler.getSalerPowerId().getCode(), saler.getId());
			otherMainNum = arr[0];
			otherDeputyNum = arr[1];

			String assess = new PerformanceAppraisalUtil().getMark(mainNum + otherMainNum);

			datas[i][0] = saler.getName();
			datas[i][1] = saler.getPhone();
			datas[i][2] = mainNum + "";
			datas[i][3] = deputyNum + "";
			datas[i][4] = otherMainNum + "";
			datas[i][5] = otherDeputyNum + "";
			datas[i][6] = assess;
			datas[i][7] = saler.getBankCard();
			datas[i][8] = saler.getCardNo();
			datas[i][9] = saler.getBankType();
			datas[i][10] = saler.getAccountBank();
			datas[i][11] = saler.getSalerPowerId().getName();
		}
		
		String Path = request.getRealPath("/");	//获得根路径 ynw
		int dzbase = Path.indexOf("\\DzBase\\");	//截取到给定字符串位	ynw
		
		String[] arr = ExcelUtils.salerDetailed(datas, Path.substring(0, dzbase), city);
		
		int dzplfrom = arr[2].indexOf("\\DzPlfrom\\");	//截取到给定字符串位	ynw
		
		message.addProperty("message", arr[1]);
		message.addProperty("success", arr[0]);
		
		message.addProperty("path", arr[2].substring(dzplfrom, arr[2].length()));	//将路径发送给前端	ynw
		new PushJson().outString(message.toJSonString(), response);
	}
	
}