package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.SalesCount;
import com.dz.service.ISalesCountService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/SalesCount")
public class SalesCountController {

	@Autowired
	private ISalesCountService SalesCountService;

	// 业务员统计表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response, SalesCount SalesCount) {

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<SalesCount> SalesCountList = SalesCountService.salesCountList(SalesCount);

		if (SalesCountList != null) {
			for (SalesCount SalesCounts : SalesCountList) {
				JSonGridRecord record = new JSonGridRecord();
				record.addColumn("id", SalesCounts.getId());
				record.addColumn("companyName", SalesCounts.getCompanyId().getName());
				record.addColumn("createTime", SalesCounts.getCreatTime());
				record.addColumn("salesmanName", SalesCounts.getSalesmanId().getName());
				record.addColumn("place", SalesCounts.getSalesmanId().getPlace());

				grid.addRecord(record);
				grid.addProperties("totalCount", SalesCountList.size());
				grid.addProperties("totalSum", SalesCountList.size());
			}
		} else {
			JSonGridRecord record = new JSonGridRecord();
			grid.addRecord(record);
			grid.addProperties("totalCount", 0);
			grid.addProperties("totalSum", 0);
		}
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除管理员
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, SalesCount SalesCount, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(SalesCount.getId() + "") || SalesCount.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
		} else {
			SalesCountService.delete(SalesCount.getId() + "");
			message.addProperty("message", "删除成功");
			message.addProperty("success", true);
			new PushJson().outString(message.toJSonString(), response);
		}
	}

	// 添加或修改管理员 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, SalesCount SalesCount, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		SalesCountService.saveORupdate(SalesCount);
		
		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}