package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Company;
import com.dz.entity.Delicacy;
import com.dz.entity.Distribution;
import com.dz.entity.Hotel;
import com.dz.entity.Include;
import com.dz.entity.Sing;
import com.dz.entity.User;
import com.dz.service.ICompanyService;
import com.dz.service.IDelicacyService;
import com.dz.service.IDistributionService;
import com.dz.service.IHotelService;
import com.dz.service.IIncludeService;
import com.dz.service.ISingService;
import com.dz.service.IUserService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/include")
public class IncludeController {

	@Autowired
	private IIncludeService includeService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IDistributionService distributionService;

	@Autowired
	private IDelicacyService delicacyService;

	@Autowired
	private IHotelService hotelService;

	@Autowired
	private ISingService singService;
	
	@Autowired
	private ICompanyService companyService;

	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response,
			Include include) {
		String token = request.getParameter("token");

		JSonMessage message = new JSonMessage();
		if(StringUtil.isEmpty(token)){
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		User user = userService.gettoken(token);
		if (user == null) {
			message.addProperty("message", "token验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<Include> includeList = includeService.getInclude(user.getId());

		for (Include includes : includeList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", includes.getId());
			record.addColumn("name", includes.getCompanyId().getName());
			record.addColumn("assess", includes.getCompanyId().getAssess());
			record.addColumn("logo", includes.getCompanyId().getLogo());
			record.addColumn("companyId", includes.getCompanyId().getId());
			record.addColumn("classifyName", includes.getCompanyId()
					.getClassifyId());

			String[] type = includes.getCompanyId().getClassifyId().split(",");
			if(type.length == 1){
				if (type[0].equals("1")) {
					Distribution disteibution = distributionService
							.getDistribution(includes.getCompanyId().getId());
					if(disteibution != null){
						record.addColumn("mode", disteibution.getMode());
						record.addColumn("time", disteibution.getTime());
						record.addColumn("GDP", disteibution.getGDP());
						record.addColumn("miniPrice", disteibution.getMiniPrice());
						record.addColumn("DistributionPrice", disteibution
								.getDistributionPrice());
						record.addColumn("monSales", includes.getCompanyId()
								.getMonSales());
						record.addColumn("classifyName", "外卖");
					}
					
				}
				if (type[0].equals("2")) {
					Delicacy delicacy = delicacyService.getDelicacy(includes.getCompanyId().getId());
					if(delicacy != null){
						record.addColumn("GDP", delicacy.getGdp());
						record.addColumn("classifyName", "美食");
					}
				}

				if (type[0].equals("3")) {
					Hotel hotel = hotelService.getHotel(includes.getCompanyId().getId());
					if(hotel != null){
						record.addColumn("miniConsume", hotel.getMiniConsume());
						record.addColumn("classifyName", "酒店");
					}
				}

				if (type[0].equals("4")) {
					Sing sing = singService.getSing(includes.getCompanyId().getId());
					if(sing != null){
						record.addColumn("miniConsume", sing.getMiniConsume());
						record.addColumn("classifyName", "KTV");
					}
				}
			}else if(type.length == 2){
				if (type[0].equals("1")) {
					Distribution disteibution = distributionService
							.getDistribution(includes.getCompanyId().getId());
					if(disteibution != null){
						record.addColumn("mode", disteibution.getMode());
						record.addColumn("time", disteibution.getTime());
						record.addColumn("GDP", disteibution.getGDP());
						record.addColumn("miniPrice", disteibution.getMiniPrice());
						record.addColumn("DistributionPrice", disteibution
								.getDistributionPrice());
						record.addColumn("monSales", includes.getCompanyId()
								.getMonSales());
					}
					
				}

				if (type[1].equals("2")) {
					Delicacy delicacy = delicacyService.getDelicacy(includes.getCompanyId().getId());
					if(delicacy != null){
						record.addColumn("GDP", delicacy.getGdp());
					}
				}
				record.addColumn("classifyName", "外卖,美食");
			}else{
				message.addProperty("message", "商家类型不正确");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			grid.addRecord(record);

		}
		grid.addProperties("totalCount", includeList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		String ids = request.getParameter("ids");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(ids)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		String[] id = ids.split(",");
		for (int i = 0; i < id.length; i++) {
			Include include = includeService.include(Integer.valueOf(id[i]));

			includeService.delete(include.getId() + "");
			message.addProperty("message", "删除成功");
			message.addProperty("success", true);
		}
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		String token = request.getParameter("token");
		String cid = request.getParameter("cid");
		
		if(StringUtil.isEmpty(token) || StringUtil.isEmpty(cid)){
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);
		if(user == null){
			message.addProperty("message", "token验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		Company company = companyService.getCompany(Integer.valueOf(cid));
		if(company == null){
			message.addProperty("message", "商家不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		Include include = includeService.getInclude(user.getId(), Integer.valueOf(cid));
		if(include != null){
			message.addProperty("message", "商家已被收藏，请勿重复操作");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		Include in = new Include();
		
		in.setUserId(user);
		in.setCompanyId(company);
		
		includeService.saveORupdate(in);

		message.addProperty("message", "商家收藏成功");//2018-10-24 @Tyy
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}