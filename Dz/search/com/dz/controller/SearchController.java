package com.dz.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Company;
import com.dz.entity.CompanyActivity;
import com.dz.entity.Delicacy;
import com.dz.entity.Distribution;
import com.dz.entity.Hotel;
import com.dz.entity.Label;
import com.dz.entity.Search;
import com.dz.entity.Sing;
import com.dz.entity.User;
import com.dz.service.ICompanyActivityService;
import com.dz.service.ICompanyService;
import com.dz.service.IDelicacyService;
import com.dz.service.IDistributionService;
import com.dz.service.IHotelService;
import com.dz.service.ILabelService;
import com.dz.service.ISearchService;
import com.dz.service.ISingService;
import com.dz.service.IUserService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.JsonUtil;
import com.dz.util.MapUtil;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/search")
public class SearchController {

	@Autowired
	private ISearchService searchService;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private IDistributionService distributionService;

	@Autowired
	private IDelicacyService delicacyService;

	@Autowired
	private IHotelService hotelService;

	@Autowired
	private ISingService singService;

	@Autowired
	private IUserService userService;

	@Autowired
	private ILabelService labelService;

	@Autowired
	private ICompanyActivityService companyActivityService;

	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response, Search search) {

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<Object[]> searchList = searchService.countSearch();
		Collections.sort(searchList, new Comparator<Object[]>() {

			@Override
			public int compare(Object[] o1, Object[] o2) {
				int i = Integer.valueOf(o1[0].toString()) - Integer.valueOf(o2[0].toString());
				if (i == 0) {
					return Integer.valueOf(o1[2].toString()) - Integer.valueOf(o2[2].toString());
				}
				return i;
			}

		});
		Collections.reverse(searchList);
		for (Object[] searchs : searchList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", searchs[2]);
			record.addColumn("keyword", searchs[1]);
			record.addColumn("time", searchs[0]);

			grid.addRecord(record);

		}
		grid.addProperties("totalCount", searchList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 用户搜索
	@RequestMapping(params = "userSearch", method = RequestMethod.POST)
	public void userSearch(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");
		// token = "cOZ6cjmF9NF";
		if (StringUtil.isEmpty(token)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "token不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);

		if (user == null) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "token验证失败");
			message.addProperty("isout", true);
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<Search> searchList = searchService.userSearch(user.getId());

		for (Search searchs : searchList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", searchs.getId());
			record.addColumn("keyword", searchs.getKeyword());

			grid.addRecord(record);

		}
		grid.addProperties("totalCount", searchList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 清空搜索记录
	@RequestMapping(params = "userDelete", method = RequestMethod.POST)
	public void userDelete(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");
		
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token)) {
			message.addProperty("message", "token不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);

		if (user == null) {
			message.addProperty("message", "账号已在另一台设备登录");
			message.addProperty("isout", true);
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		try{
			message.addProperty("success", true);
			searchService.userDelete(user.getId());
		}catch (Exception e) {
			message.addProperty("message", "清空失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		message.addProperty("message", "清空成功");
		new PushJson().outString(message.toJSonString(), response);
	}

	// 关键字搜索商家和商品(共同展示)
	@RequestMapping(params = "doSearch", method = RequestMethod.POST)
	public void doSearch(HttpServletRequest request, HttpServletResponse response) {
		String location = request.getParameter("location");
		String start = request.getParameter("start");// 起始页
		String limit = request.getParameter("limit");// 容量
		String sort = request.getParameter("sort");// distance 距离,volume
		// 销量,score 综合排序
		String keyword = request.getParameter("keyword");

		String token = request.getParameter("token");
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(keyword) || StringUtil.isEmpty(location)) {
			message.addProperty("message", "关键字不能为空");//2018-11-05 @Tyy
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (!StringUtil.isEmpty(token) && !StringUtil.isEmpty(keyword)) {
			User user = userService.gettoken(token);
			if (user == null) {
				message.addProperty("message", "token验证失败");
				message.addProperty("isout", true);
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}

			Search search = new Search();
			search.setKeyword(keyword);
			search.setUserId(user);
			searchService.saveORupdate(search);
		}
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		try {
			keyword = new String(keyword.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		List<Company> companyList = companyService.searchCompany(keyword, sort, Integer.valueOf(start),
				Integer.valueOf(limit));

		for (Company companys : companyList) {
			String distance = JsonUtil.getDistance(location, companys.getCoordinates());
			if (StringUtils.isNumeric(distance)) {
				String range = distance + "m";
				if (Integer.valueOf(distance) > 1000) {
					range = Double.valueOf(distance.toString()) / 1000 + "km";
				}
				if (Integer.valueOf(distance) <= 50000000) {// 距离
					JSonGridRecord record = new JSonGridRecord();
					String[] type = companys.getClassifyId().split(",");
					if (type.length == 1) {
						if (type[0].equals("1")) {
							Distribution disteibution = distributionService.getDistribution(companys.getId());
							if (disteibution != null) {
								grid.addProperties("mode", disteibution.getMode());
								grid.addProperties("time", disteibution.getTime());
								grid.addProperties("GDP", disteibution.getGDP());
								grid.addProperties("miniPrice", disteibution.getMiniPrice());
								grid.addProperties("DistributionPrice", disteibution.getDistributionPrice());
							}
						}
						if (type[0].equals("2")) {
							Delicacy delicacy = delicacyService.getDelicacy(companys.getId());
							if (delicacy != null) {
								grid.addProperties("GDP", delicacy.getGdp());
							}
						}

						if (type[0].equals("3")) {
							Hotel hotel = hotelService.getHotel(companys.getId());
							if (hotel != null) {
								grid.addProperties("miniConsume", hotel.getMiniConsume());
							}
						}

						if (type[0].equals("4")) {
							Sing sing = singService.getSing(companys.getId());
							if (sing != null) {
								grid.addProperties("miniConsume", sing.getMiniConsume());
							}
						}
					} else if (type.length == 2) {
						if (type[0].equals("1")) {
							Distribution disteibution = distributionService.getDistribution(companys.getId());
							if (disteibution != null) {
								grid.addProperties("mode", disteibution.getMode());
								grid.addProperties("time", disteibution.getTime());
								grid.addProperties("GDP", disteibution.getGDP());
								grid.addProperties("miniPrice", disteibution.getMiniPrice());
								grid.addProperties("DistributionPrice", disteibution.getDistributionPrice());
							}

						}

						if (type[1].equals("2")) {
							Delicacy delicacy = delicacyService.getDelicacy(companys.getId());
							if (delicacy != null) {
								grid.addProperties("GDP", delicacy.getGdp());
							}
						}
					} else {
						message.addProperty("message", "商家类型不正确");
						message.addProperty("success", false);
						new PushJson().outString(message.toJSonString(), response);
						return;
					}

					record.addColumn("id", companys.getId());
					record.addColumn("logo", companys.getLogo());
					record.addColumn("name", companys.getName());
					record.addColumn("phone", companys.getPhone());
					record.addColumn("position", companys.getPosition());
					record.addColumn("type", companys.getClassifyId());
					record.addColumn("business_time",
							companys.getBusinessTimeStart() + "-" + companys.getBusinessTimeEnd());
					record.addColumn("honor", companys.getHonor());
					record.addColumn("notice", companys.getNotice());
					record.addColumn("info", companys.getInfo());
					record.addColumn("assess", companys.getAssess());
					record.addColumn("is_business", companys.getIsBusiness());
					record.addColumn("distance", range);
					record.addColumn("img", companys.getImg());
					grid.addRecord(record);
				}
			}
		}
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 外卖商家搜索
	@RequestMapping(params = "searchTakeout", method = RequestMethod.POST)
	public void searchTakeout(HttpServletRequest request, HttpServletResponse response) {
		String location = request.getParameter("location");
		String start = request.getParameter("start");// 起始页
		String limit = request.getParameter("limit");// 容量
		final String sort = request.getParameter("sort");
		// distance 距离
		// volume 销量
		// score 综合排序
		String keyword = request.getParameter("keyword");
		String label = request.getParameter("label");
		if (StringUtil.isEmpty(location) || StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = new Company();
		if (!StringUtil.isEmpty(keyword)) {

			try {
				keyword = new String(keyword.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			company.setName(keyword);
		}

		List<Company> companyList = companyService.classifyCompany(company, sort, 1, Integer.valueOf(start),
				Integer.valueOf(limit));
		List<Object[]> distancelist = new ArrayList<Object[]>();
		for (Company companys : companyList) {
			int distance = MapUtil.getdistance(location, companys.getCoordinates());
			Object[] obj = { companys.getId(), distance, companys.getMonSales(), companys.getAssess() };
			distancelist.add(obj);
		}
		Collections.sort(distancelist, new Comparator<Object[]>() {

			@Override
			public int compare(Object[] o1, Object[] o2) {
				int i = 0;
				if (sort.equals("distance")) {
					i = Integer.valueOf(o1[1].toString()) - Integer.valueOf(o2[1].toString());
					if (i == 0) {
						return Integer.valueOf(o1[0].toString()) - Integer.valueOf(o2[0].toString());
					}
					return i;
				}

				if (sort.equals("volume")) {
					i = Integer.valueOf(o1[2].toString()) - Integer.valueOf(o2[2].toString());
					if (i == 0) {
						return Integer.valueOf(o1[0].toString()) - Integer.valueOf(o2[0].toString());
					}
					return i;
				}

				if (sort.equals("score")) {
					double j = Double.valueOf(o1[3].toString()) * 10 - Double.valueOf(o2[3].toString()) * 10;
					i = (new Double(j)).intValue();
					if (i == 0) {
						return Integer.valueOf(o1[0].toString()) - Integer.valueOf(o2[0].toString());
					}
					return i;
				}
				return i;
			}

		});
		if (!sort.equals("distance")) {
			Collections.reverse(distancelist);
		}
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (Object[] stu : distancelist) {
			String range = stu[1] + "m";
			if (Integer.valueOf(stu[1].toString()) > 1000) {
				range = Double.valueOf(stu[1].toString()) / 1000 + "km";
			}
			Company com = companyService.getCompany((Integer) stu[0]);
			if (Integer.valueOf(stu[1].toString()) <= 5000000) {// 距离
				String theLabel = "";
				boolean isView = true;
				StringBuilder labels = new StringBuilder();
				List<Label> labellist = labelService.getLabel("company", com.getId() + "");
				for (Label newlabel : labellist) {
					labels.append(newlabel.getContent() + ",");
				}
				if (!StringUtil.isEmpty(label)) {
					isView = false;
					try {
						label = new String(label.getBytes("ISO-8859-1"), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}

					for (Label newlabel : labellist) {
						theLabel = newlabel.getContent();

						if (!theLabel.equals(label)) {
							isView = false;
						} else {
							isView = true;
							break;
						}
					}
				}
				if (isView) {
					JSonGridRecord record = new JSonGridRecord();
					Distribution disteibution = distributionService.getDistribution(com.getId());
					if (disteibution != null) {
						record.addColumn("mode", disteibution.getMode());
						record.addColumn("time", disteibution.getTime());
						record.addColumn("GDP", disteibution.getGDP());
						record.addColumn("DistributionPrice", disteibution.getDistributionPrice());
					}
					record.addColumn("label", labels.toString());
					record.addColumn("monSales", com.getMonSales());
					record.addColumn("id", com.getId());
					record.addColumn("logo", com.getLogo());
					record.addColumn("name", com.getName());
					record.addColumn("monSales", com.getMonSales());
					record.addColumn("phone", com.getPhone());
					record.addColumn("position", com.getPosition());
					record.addColumn("business_time", com.getBusinessTimeStart() + "-" + com.getBusinessTimeEnd());
					record.addColumn("honor", com.getHonor());
					record.addColumn("notice", com.getNotice());
					record.addColumn("type", com.getClassifyId());
					record.addColumn("info", com.getInfo());
					record.addColumn("distance", range);
					record.addColumn("assess", com.getAssess());
					record.addColumn("is_business", com.getIsBusiness());
					record.addColumn("distance", range);
					record.addColumn("img", com.getImg());

					String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

					List<CompanyActivity> list = companyActivityService.getTime(Integer.valueOf(com.getId()), date,
							"1");

					int count = 0;
					StringBuilder subtraction = new StringBuilder();
					for (CompanyActivity companyActivity : list) {
						if (companyActivity.getIsOpen()) {
							if (companyActivity.getActivityId().getId() == 1) {
								record.addColumn("activityName1", companyActivity.getActivityId().getName());

								subtraction.append(
										"满" + companyActivity.getBalance() + "减" + companyActivity.getBenefit() + ";");

								record.addColumn("subtraction", subtraction.toString());
								record.addColumn("isOpen1", companyActivity.getIsOpen());
								count++;
							}

							if (companyActivity.getActivityId().getId() == 2) {
								record.addColumn("activityName2", companyActivity.getActivityId().getName());
								record.addColumn("newUser", "新用户立减" + companyActivity.getNewUser() + "元");
								record.addColumn("isOpen2", companyActivity.getIsOpen());
							}

							if (companyActivity.getActivityId().getId() == 3) {
								record.addColumn("activityName3", companyActivity.getActivityId().getName());
								record.addColumn("svg", "折扣商品" + companyActivity.getSvg() + "折起");
								record.addColumn("isOpen3", companyActivity.getIsOpen());
							}

							if (companyActivity.getActivityId().getId() == 4) {
								record.addColumn("activityName4", companyActivity.getActivityId().getName());
								record.addColumn("coupon", "可领取代金券" + companyActivity.getCoupon() + "元");
								record.addColumn("isOpen4", companyActivity.getIsOpen());
							}
						}
					}
					if (count == 0) {
						record.addColumn("count", list.size());
					} else {
						record.addColumn("count", list.size() - count + 1);
					}
					grid.addRecord(record);
				}
			}
		}

		Long count = 0L;
		// if (StringUtil.isEmpty(label)) {
		// count = labelService.count(label, "company");
		// } else {
		count = companyService.classifycount(company, 1);
		// }

		grid.addProperties("totalCount", String.valueOf(count));
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Search search, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(search.getId() + "") || search.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		searchService.delete(search.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, Search search, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		searchService.saveORupdate(search);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 外卖商家搜索
	@RequestMapping(params = "order", method = RequestMethod.POST)
	public void order(HttpServletRequest request, HttpServletResponse response) {
		String location = request.getParameter("location");
		String start = request.getParameter("start");// 起始页
		String limit = request.getParameter("limit");// 容量
		final String sort = request.getParameter("sort");
		// distance 距离
		// volume 销量
		// score 综合排序
		String keyword = request.getParameter("keyword");
		String label = request.getParameter("label");
		if (StringUtil.isEmpty(location) || StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = new Company();
		if (!StringUtil.isEmpty(keyword)) {

			try {
				keyword = new String(keyword.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			company.setName(keyword);
		}

		List<Company> companyList = companyService.classifyCompany(company, sort, 1, Integer.valueOf(start),
				Integer.valueOf(limit));
		List<Object[]> distancelist = new ArrayList<Object[]>();
		for (Company companys : companyList) {
			int distance = MapUtil.getdistance(location, companys.getCoordinates());
			Object[] obj = { companys.getId(), distance, companys.getMonSales(), companys.getAssess() };
			distancelist.add(obj);
		}
		Collections.sort(distancelist, new Comparator<Object[]>() {

			@Override
			public int compare(Object[] o1, Object[] o2) {
				int i = 0;
				if (sort.equals("distance")) {
					i = Integer.valueOf(o1[1].toString()) - Integer.valueOf(o2[1].toString());
					if (i == 0) {
						return Integer.valueOf(o1[0].toString()) - Integer.valueOf(o2[0].toString());
					}
					return i;
				}

				if (sort.equals("volume")) {
					i = Integer.valueOf(o1[2].toString()) - Integer.valueOf(o2[2].toString());
					if (i == 0) {
						return Integer.valueOf(o1[0].toString()) - Integer.valueOf(o2[0].toString());
					}
					return i;
				}

				if (sort.equals("score")) {
					double j = Double.valueOf(o1[3].toString()) * 10 - Double.valueOf(o2[3].toString()) * 10;
					i = (new Double(j)).intValue();
					if (i == 0) {
						return Integer.valueOf(o1[0].toString()) - Integer.valueOf(o2[0].toString());
					}
					return i;
				}
				return i;
			}

		});
		if (!sort.equals("distance")) {
			Collections.reverse(distancelist);
		}
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (Object[] stu : distancelist) {
			String range = stu[1] + "m";
			if (Integer.valueOf(stu[1].toString()) > 1000) {
				range = Double.valueOf(stu[1].toString()) / 1000 + "km";
			}
			Company com = companyService.getCompany((Integer) stu[0]);
			if (Integer.valueOf(stu[1].toString()) <= 5000000) {// 距离
				String theLabel = "";
				boolean isView = true;
				StringBuilder labels = new StringBuilder();
				List<Label> labellist = labelService.getLabel("company", com.getId() + "");
				for (Label newlabel : labellist) {
					labels.append(newlabel.getContent() + ",");
				}
				if (!StringUtil.isEmpty(label)) {
					isView = false;
					try {
						label = new String(label.getBytes("ISO-8859-1"), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}

					for (Label newlabel : labellist) {
						theLabel = newlabel.getContent();

						if (!theLabel.equals(label)) {
							isView = false;
						} else {
							isView = true;
							break;
						}
					}
				}
				if (isView) {
					JSonGridRecord record = new JSonGridRecord();
					Distribution disteibution = distributionService.getDistribution(com.getId());
					if (disteibution != null) {
						record.addColumn("mode", disteibution.getMode());
						record.addColumn("time", disteibution.getTime());
						record.addColumn("GDP", disteibution.getGDP());
						record.addColumn("DistributionPrice", disteibution.getDistributionPrice());
					}
					record.addColumn("label", labels.toString());
					record.addColumn("monSales", com.getMonSales());
					record.addColumn("id", com.getId());
					record.addColumn("logo", com.getLogo());
					record.addColumn("name", com.getName());
					record.addColumn("monSales", com.getMonSales());
					record.addColumn("phone", com.getPhone());
					record.addColumn("position", com.getPosition());
					record.addColumn("business_time", com.getBusinessTimeStart() + "-" + com.getBusinessTimeEnd());
					record.addColumn("honor", com.getHonor());
					record.addColumn("notice", com.getNotice());
					record.addColumn("type", com.getClassifyId());
					record.addColumn("info", com.getInfo());
					record.addColumn("distance", range);
					record.addColumn("assess", com.getAssess());
					record.addColumn("is_business", com.getIsBusiness());
					record.addColumn("distance", range);
					record.addColumn("img", com.getImg());

					String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

					List<CompanyActivity> list = companyActivityService.getTime(Integer.valueOf(com.getId()), date,
							"1");

					int count = 0;
					StringBuilder subtraction = new StringBuilder();
					for (CompanyActivity companyActivity : list) {
						if (companyActivity.getIsOpen()) {
							if (companyActivity.getActivityId().getId() == 1) {
								record.addColumn("activityName1", companyActivity.getActivityId().getName());

								subtraction.append(
										"满" + companyActivity.getBalance() + "减" + companyActivity.getBenefit() + ";");

								record.addColumn("subtraction", subtraction.toString());
								record.addColumn("isOpen1", companyActivity.getIsOpen());
								count++;
							}

							if (companyActivity.getActivityId().getId() == 2) {
								record.addColumn("activityName2", companyActivity.getActivityId().getName());
								record.addColumn("newUser", "新用户立减" + companyActivity.getNewUser() + "元");
								record.addColumn("isOpen2", companyActivity.getIsOpen());
							}

							if (companyActivity.getActivityId().getId() == 3) {
								record.addColumn("activityName3", companyActivity.getActivityId().getName());
								record.addColumn("svg", "折扣商品" + companyActivity.getSvg() + "折起");
								record.addColumn("isOpen3", companyActivity.getIsOpen());
							}

							if (companyActivity.getActivityId().getId() == 4) {
								record.addColumn("activityName4", companyActivity.getActivityId().getName());
								record.addColumn("coupon", "可领取代金券" + companyActivity.getCoupon() + "元");
								record.addColumn("isOpen4", companyActivity.getIsOpen());
							}
						}
					}
					if (count == 0) {
						record.addColumn("count", list.size());
					} else {
						record.addColumn("count", list.size() - count + 1);
					}
					grid.addRecord(record);
				}
			}
		}

		Long count = 0L;
		// if (StringUtil.isEmpty(label)) {
		// count = labelService.count(label, "company");
		// } else {
		count = companyService.classifycount(company, 1);
		// }

		grid.addProperties("totalCount", String.valueOf(count));
		new PushJson().outString(grid.toJSonString("list"), response);
	}

}
