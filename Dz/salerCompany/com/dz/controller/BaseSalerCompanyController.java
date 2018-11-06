package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Company;
import com.dz.entity.SalerCompany;
import com.dz.entity.SalerInfo;
import com.dz.service.ICompanyService;
import com.dz.service.ISalerCompanyService;
import com.dz.service.ISalerInfoService;
import com.dz.service.IUserService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/base/salerCompany")
public class BaseSalerCompanyController {

	@Autowired
	private IUserService userService;

	@Autowired
	private ISalerCompanyService salerCompanyService;

	@Autowired
	private ISalerInfoService salerInfoService;

	@Autowired
	private ICompanyService companyService;

	// 用户列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String phone = request.getParameter("phone");
		SalerCompany salerCompany = new SalerCompany();
		if (!StringUtil.isEmpty(companyId)) {
			salerCompany.setCompanyId(Integer.valueOf(companyId));
		}else{
			salerCompany.setCompanyId(0);
		}

		if(!StringUtil.isEmpty(phone)){
			SalerInfo salerInfo  = salerInfoService.getPhone(phone);
			if(salerInfo != null){
				salerCompany.setSalerId(salerInfo.getId());
			}else{
				salerCompany.setSalerId(-1);
			}
		}else{
			salerCompany.setSalerId(0);
		}
		List<SalerCompany> salerCompanyList = salerCompanyService.salerCompanyList(salerCompany);
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (SalerCompany salerCompanys : salerCompanyList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", salerCompanys.getId());
			record.addColumn("createTime", salerCompanys.getCreateTime());
			SalerInfo salerInfo = salerInfoService.getid(salerCompanys.getSalerId());
			if (salerInfo != null) {
				record.addColumn("salerName", salerInfo.getName());
				record.addColumn("salerPhone", salerInfo.getPhone());
				record.addColumn("city", salerInfo.getCity());
			}else{
				record.addColumn("salerName", "未填写");
				record.addColumn("salerPhone", "未填写");
				record.addColumn("city", "未填写");
			}

			Company company = companyService.getCompany(salerCompanys.getCompanyId());
			if (company != null) {
				record.addColumn("companyName", company.getName());
				record.addColumn("companyPhone", company.getPhone());
				record.addColumn("address", company.getPosition());
			}

			grid.addRecord(record);
		}
		grid.addProperties("totalSum", salerCompanyList.size());
		grid.addProperties("totalCount", salerCompanyList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除用户
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, SalerCompany user, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(user.getId() + "") || user.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		userService.delete(user.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}
