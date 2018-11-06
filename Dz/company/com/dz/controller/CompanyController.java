package com.dz.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.dz.entity.CompanyActivity;
import com.dz.entity.Delicacy;
import com.dz.entity.Distribution;
import com.dz.entity.Hotel;
import com.dz.entity.Label;
import com.dz.entity.Sing;
import com.dz.entity.User;
import com.dz.service.IBookTimeService;
import com.dz.service.ICompanyActivityService;
import com.dz.service.ICompanyService;
import com.dz.service.IDelicacyService;
import com.dz.service.IDistributionService;
import com.dz.service.IHotelService;
import com.dz.service.ILabelService;
import com.dz.service.IOrderService;
import com.dz.service.ISingService;
import com.dz.service.IUserService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.MapUtil;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/company")
public class CompanyController {

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
	private ILabelService labelService;

	@Autowired
	private IBookTimeService bookTimeService;

	@Autowired
	private ICompanyActivityService companyActivityService;

	@Autowired
	private IOrderService orderService;

	@Autowired
	private IUserService userService;

	@RequestMapping(params = "findName", method = RequestMethod.POST)
	public void findName(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = companyService.getCompany(Integer.valueOf(id));

		if (company == null) {
			message.addProperty("message", "商家不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		message.addProperty("cname", company.getName());
		message.addProperty("logo", company.getLogo());
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// ======================================手机端======================================
	// 商家详情查询

	@RequestMapping(params = "find", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String location = request.getParameter("location");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(location)) {
			message.addProperty("message", "id和location不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = companyService.getCompany(Integer.valueOf(id));

		if (company == null) {
			message.addProperty("message", "商家不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		int distance = MapUtil.getdistance(location, company.getCoordinates());
		String range = distance + "m";
		if (distance >= 1000) {
			range = Double.valueOf(distance) / 1000 + "km";
		}
		grid.addProperties("id", company.getId());
		grid.addProperties("name", company.getName());
		grid.addProperties("notice", company.getNotice() == null ? "" : company.getNotice());
		grid.addProperties("assess", company.getAssess() == null ? "" : company.getAssess());
		grid.addProperties("range", range);
		grid.addProperties("monSales", company.getMonSales());
		grid.addProperties("coordinates", company.getCoordinates());
		grid.addProperties("img", company.getImg() == null ? "" : company.getImg());
		grid.addProperties("logo", company.getLogo() == null ? "" : company.getLogo());
		grid.addProperties("address", company.getPosition() == null ? "" : company.getPosition());
		grid.addProperties("place", company.getInfo() == null ? "" : company.getInfo());
		grid.addProperties("isBusiness", company.getIsBusiness());
		grid.addProperties("type", company.getClassifyId() == null ? "" : company.getClassifyId());
		grid.addProperties("unsubscribe_time",
				company.getUnsubscribe_time() == null ? "" : company.getUnsubscribe_time());
		grid.addProperties("business_time", company.getBusinessTimeStart() + "-" + company.getBusinessTimeEnd());
		grid.addProperties("phone", company.getPhone());
		String[] type = company.getClassifyId().split(",");
		if (type.length == 1) {
			if (type[0].equals("1")) {
				Distribution disteibution = distributionService.getDistribution(company.getId());
				if (disteibution != null) {
					grid.addProperties("mode", disteibution.getMode());
					grid.addProperties("time", disteibution.getTime());
					grid.addProperties("GDP", disteibution.getGDP());
					grid.addProperties("miniPrice", disteibution.getMiniPrice());
					grid.addProperties("DistributionPrice", disteibution.getDistributionPrice());
				}
			}
			if (type[0].equals("2")) {
				Delicacy delicacy = delicacyService.getDelicacy(company.getId());
				String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				List<BookTime> bookTimeList = bookTimeService.getCompanyBookTime(company.getId(), date);
				grid.addProperties("todayCount", bookTimeList.size());
				if (delicacy != null) {
					grid.addProperties("GDP", delicacy.getGdp());
				}
			}

			if (type[0].equals("3")) {
				Hotel hotel = hotelService.getHotel(company.getId());
				if (hotel != null) {
					grid.addProperties("miniConsume", hotel.getMiniConsume());
				}
			}

			if (type[0].equals("4")) {
				Sing sing = singService.getSing(company.getId());
				if (sing != null) {
					grid.addProperties("miniConsume", sing.getMiniConsume());
				}
			}
		} else if (type.length == 2) {
			if (type[0].equals("1")) {
				Distribution disteibution = distributionService.getDistribution(company.getId());
				if (disteibution != null) {
					grid.addProperties("mode", disteibution.getMode());
					grid.addProperties("time", disteibution.getTime());
					grid.addProperties("GDP", disteibution.getGDP());
					grid.addProperties("miniPrice", disteibution.getMiniPrice());
					grid.addProperties("DistributionPrice", disteibution.getDistributionPrice());
				}

			}

			if (type[1].equals("2")) {
				Delicacy delicacy = delicacyService.getDelicacy(company.getId());
				String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				List<BookTime> bookTimeList = bookTimeService.getCompanyBookTime(company.getId(), date);
				grid.addProperties("todayCount", bookTimeList.size());
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

		List<CompanyActivity> list = new ArrayList<CompanyActivity>();
		if (company.getClassifyId().contains("1")) {
			list = companyActivityService.getList(Integer.valueOf(company.getId()), "1");
		} else if (company.getClassifyId().contains("2")) {
			list = companyActivityService.getList(Integer.valueOf(company.getId()), "2");
		}
		int count = 0;
		StringBuilder subtraction = new StringBuilder();
		JSonGridRecord record = new JSonGridRecord();
		for (CompanyActivity companyActivity : list) {
			if (companyActivity.getActivityId().getId() == 1) {
				record.addColumn("activityName1", companyActivity.getActivityId().getName());

				subtraction.append("满" + companyActivity.getBalance() + "减" + companyActivity.getBenefit() + ";");

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
		grid.addProperties("count", count);
		grid.addRecord(record);
		if (count == 0) {
			grid.addProperties("totalCount", list.size());
		} else {
			grid.addProperties("totalCount", list.size() - count + 1);
		}

		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 为你推荐接口
	@RequestMapping(params = "companyTop", method = RequestMethod.POST)
	public void companyTop(HttpServletRequest request, HttpServletResponse response, Company company) {
		String location = request.getParameter("location");
		final String sort = request.getParameter("sort");
		// distance 距离
		// volume 销量
		// score 综合排序
		String start = request.getParameter("start");// 起始页
		String limit = request.getParameter("limit");// 容量

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)) {
			message.addProperty("message", "start和limit不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		// location = "25.2560978214,110.2188938856";
		if (StringUtil.isEmpty(location)) {
			message.addProperty("message", "location不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Company> companyList = companyService.companyList(company, sort, Integer.valueOf(start),
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
			int time = 30;
			if (Integer.valueOf(stu[1].toString()) > 2000) {
				time = time + (int) Math.ceil((Integer.valueOf(stu[1].toString()) - 2000) / 250);
			}
			Company com = companyService.getCompany((Integer) stu[0]);
			if (Integer.valueOf(stu[1].toString()) <= 10000000) {// 距离
				JSonGridRecord record = new JSonGridRecord();
				String[] type = com.getClassifyId().split(",");
				if (type.length == 1) {
					if (type[0].equals("1")) {
						Distribution disteibution = distributionService.getDistribution(com.getId());
						if (disteibution != null) {
							record.addColumn("mode", disteibution.getMode());
							record.addColumn("time", time + "分钟");
							record.addColumn("GDP", disteibution.getGDP());
							record.addColumn("miniPrice", disteibution.getMiniPrice());
							record.addColumn("DistributionPrice", disteibution.getDistributionPrice());
						}

					}
					if (type[0].equals("2")) {
						Delicacy delicacy = delicacyService.getDelicacy(com.getId());
						String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
						List<BookTime> bookTimeList = bookTimeService.getCompanyBookTime(com.getId(), date);
						record.addColumn("todayCount", bookTimeList.size());
						if (delicacy != null) {
							record.addColumn("GDP", delicacy.getGdp());
						}
					}

					if (type[0].equals("3")) {
						Hotel hotel = hotelService.getHotel(com.getId());
						if (hotel != null) {
							record.addColumn("miniConsume", hotel.getMiniConsume());
						}
					}

					if (type[0].equals("4")) {
						Sing sing = singService.getSing(com.getId());
						if (sing != null) {
							record.addColumn("miniConsume", sing.getMiniConsume());
						}
					}
				} else if (type.length == 2) {
					if (type[0].equals("1")) {
						Distribution disteibution = distributionService.getDistribution(com.getId());
						if (disteibution != null) {
							record.addColumn("mode", disteibution.getMode());
							record.addColumn("time", disteibution.getTime());
							record.addColumn("GDP", disteibution.getGDP());
							record.addColumn("miniPrice", disteibution.getMiniPrice());
							record.addColumn("DistributionPrice", disteibution.getDistributionPrice());
						}

					}

					if (type[1].equals("2")) {
						Delicacy delicacy = delicacyService.getDelicacy(com.getId());
						String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
						List<BookTime> bookTimeList = bookTimeService.getCompanyBookTime(com.getId(), date);
						record.addColumn("todayCount", bookTimeList.size());
						if (delicacy != null) {
							record.addColumn("GDP", delicacy.getGdp());
						}
					}
				} else {
					message.addProperty("message", "商家类型不正确");
					message.addProperty("success", false);
					new PushJson().outString(message.toJSonString(), response);
					return;
				}
				StringBuilder label = new StringBuilder();
				List<Label> labellist = labelService.getLabel("company", com.getId() + "");
				for (Label labels : labellist) {
					label.append(labels.getContent() + ",");
				}
				record.addColumn("label", label.toString());
				record.addColumn("monSales", com.getMonSales());
				record.addColumn("id", com.getId());
				record.addColumn("logo", com.getLogo());
				record.addColumn("name", com.getName());
				record.addColumn("phone", com.getPhone());
				record.addColumn("position", com.getPosition());
				record.addColumn("business_time", com.getBusinessTimeStart() + "-" + com.getBusinessTimeEnd());
				record.addColumn("honor", com.getHonor());
				record.addColumn("notice", com.getNotice());
				record.addColumn("type", com.getClassifyId());
				record.addColumn("info", com.getInfo());
				record.addColumn("distance", range);
				record.addColumn("assess", com.getAssess());
				record.addColumn("isBusiness", com.getIsBusiness());
				record.addColumn("distance", range);
				record.addColumn("img", com.getImg());
				List<CompanyActivity> list = new ArrayList<CompanyActivity>();
				if (com.getClassifyId().contains("1")) {
					list = companyActivityService.getList(Integer.valueOf(com.getId()), "1");
				} else if (com.getClassifyId().contains("2")) {
					list = companyActivityService.getList(Integer.valueOf(com.getId()), "2");
				}

				int count = 0;
				StringBuilder subtraction = new StringBuilder();
				for (CompanyActivity companyActivity : list) {
					if (companyActivity.getActivityId().getId() == 1) {
						record.addColumn("activityName1", companyActivity.getActivityId().getName());

						subtraction
								.append("满" + companyActivity.getBalance() + "减" + companyActivity.getBenefit() + ";");

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
				if (count == 0) {
					record.addColumn("count", list.size());
				} else {
					record.addColumn("count", list.size() - count + 1);
				}
				grid.addRecord(record);
			}
		}

		Long count = companyService.count(company);

		if (count != null) {
			grid.addProperties("totalCount", String.valueOf(count));
		} else {
			grid.addProperties("totalCount", 0);
		}
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 商家查询列表

	/**
	 * 参数说明 sort ：传递参数均为大写 SALE. 销售量排序 DISTANCE. 距离排序 PERSON. 人均消费排序 SCORE.
	 * 评价分排序 GENERAL. 综合排序 （这个规则根据我们自身原先定义的商家发红包活跃情况进行排序）
	 */
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response, Company company) {

		String location = request.getParameter("location");
		final String sort = request.getParameter("sort");// distance 距离 volume

		// 销量 //
		// score 综合排序
		String start = request.getParameter("start");// 起始页
		String limit = request.getParameter("limit");// 容量

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)) {
			message.addProperty("message", "start和limit不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		// location = "25.2560978214,110.2188938856";
		if (StringUtil.isEmpty(location) || StringUtil.isEmpty(sort)) {
			message.addProperty("message", "location和sort不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Company> companyList = companyService.companyList(company, sort, Integer.valueOf(start),
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
			int time = 30;
			if (Integer.valueOf(stu[1].toString()) > 2000) {
				time = time + (int) Math.ceil((Integer.valueOf(stu[1].toString()) - 2000) / 250);
			}
			Company com = companyService.getCompany((Integer) stu[0]);
			if (Integer.valueOf(stu[1].toString()) <= 5000000) {// 距离
				JSonGridRecord record = new JSonGridRecord();
				String[] type = company.getClassifyId().split(",");
				if (type.length == 1) {
					if (type[0].equals("1")) {
						Distribution disteibution = distributionService.getDistribution(company.getId());
						if (disteibution != null) {
							grid.addProperties("mode", disteibution.getMode());
							grid.addProperties("time", time);
							grid.addProperties("GDP", disteibution.getGDP());
							grid.addProperties("miniPrice", disteibution.getMiniPrice());
							grid.addProperties("DistributionPrice", disteibution.getDistributionPrice());
						}

					}
					if (type[0].equals("2")) {
						Delicacy delicacy = delicacyService.getDelicacy(company.getId());
						String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
						List<BookTime> bookTimeList = bookTimeService.getCompanyBookTime(com.getId(), date);
						record.addColumn("todayCount", bookTimeList.size());
						if (delicacy != null) {
							grid.addProperties("GDP", delicacy.getGdp());
						}
					}

					if (type[0].equals("3")) {
						Hotel hotel = hotelService.getHotel(company.getId());
						if (hotel != null) {
							grid.addProperties("miniConsume", hotel.getMiniConsume());
						}
					}

					if (type[0].equals("4")) {
						Sing sing = singService.getSing(company.getId());
						if (sing != null) {
							grid.addProperties("miniConsume", sing.getMiniConsume());
						}
					}
				} else if (type.length == 2) {
					if (type[0].equals("1")) {
						Distribution disteibution = distributionService.getDistribution(company.getId());
						if (disteibution != null) {
							grid.addProperties("mode", disteibution.getMode());
							grid.addProperties("time", disteibution.getTime());
							grid.addProperties("GDP", disteibution.getGDP());
							grid.addProperties("miniPrice", disteibution.getMiniPrice());
							grid.addProperties("DistributionPrice", disteibution.getDistributionPrice());
						}

					}

					if (type[1].equals("2")) {
						Delicacy delicacy = delicacyService.getDelicacy(company.getId());
						String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
						List<BookTime> bookTimeList = bookTimeService.getCompanyBookTime(com.getId(), date);
						record.addColumn("todayCount", bookTimeList.size());
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

				StringBuilder label = new StringBuilder();
				List<Label> labellist = labelService.getLabel("company", com.getId() + "");
				for (Label labels : labellist) {
					label.append(labels.getContent() + ",");
				}
				record.addColumn("label", label.toString());
				record.addColumn("monSales", com.getMonSales());
				record.addColumn("id", com.getId());
				record.addColumn("logo", com.getLogo());
				record.addColumn("name", com.getName());
				record.addColumn("phone", com.getPhone());
				record.addColumn("position", com.getPosition());
				record.addColumn("business_time", com.getBusinessTimeStart() + "-" + com.getBusinessTimeEnd());
				record.addColumn("honor", com.getHonor());
				record.addColumn("notice", com.getNotice());
				record.addColumn("type", com.getClassifyId());
				record.addColumn("info", com.getInfo());
				record.addColumn("distance", range);
				record.addColumn("assess", com.getAssess());
				record.addColumn("isBusiness", com.getIsBusiness());
				record.addColumn("distance", range);
				record.addColumn("img", com.getImg());
				List<CompanyActivity> list = new ArrayList<CompanyActivity>();
				if (com.getClassifyId().contains("1")) {
					list = companyActivityService.getList(Integer.valueOf(com.getId()), "1");
				} else if (com.getClassifyId().contains("2")) {
					list = companyActivityService.getList(Integer.valueOf(com.getId()), "2");
				}
				int count = 0;
				StringBuilder subtraction = new StringBuilder();
				for (CompanyActivity companyActivity : list) {
					if (companyActivity.getActivityId().getId() == 1) {
						record.addColumn("activityName1", companyActivity.getActivityId().getName());

						subtraction
								.append("满" + companyActivity.getBalance() + "减" + companyActivity.getBenefit() + ";");

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
				if (count == 0) {
					record.addColumn("count", list.size());
				} else {
					record.addColumn("count", list.size() - count + 1);
				}
				grid.addRecord(record);
			}
		}

		Long count = companyService.count(company);

		if (count != null) {
			grid.addProperties("totalCount", String.valueOf(count));
		} else {
			grid.addProperties("totalCount", 0);
		}
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 按分类查询商家

	/**
	 * 参数说明 sort ：传递参数均为大写 SALE. 销售量排序 DISTANCE. 距离排序 PERSON. 人均消费排序 SCORE.
	 * 评价分排序 GENERAL. 综合排序 （这个规则根据我们自身原先定义的商家发红包活跃情况进行排序）
	 */
	@RequestMapping(params = "getDisteibution", method = RequestMethod.POST)
	public void getDisteibution(HttpServletRequest request, HttpServletResponse response, Company company) {
		String location = request.getParameter("location");// location =
		// "25.2560978214,110.2188938856";
		final String sort = request.getParameter("sort");// distance 距离 volume
		// 销量 score 综合排序
		String start = request.getParameter("start");// 起始页
		String limit = request.getParameter("limit");// 容量

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)) {
			message.addProperty("message", "start和limit不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if (StringUtil.isEmpty(location) || StringUtil.isEmpty(sort)) {
			message.addProperty("message", "classify,location和sort不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Company> companyList = companyService.classifyCompany(company, sort, 2, Integer.valueOf(start),
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
			if (Integer.valueOf(stu[1].toString()) <= 20000000) {// 距离
				JSonGridRecord record = new JSonGridRecord();
				Delicacy delicacy = delicacyService.getDelicacy(com.getId());
				String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				List<BookTime> bookTimeList = bookTimeService.getCompanyBookTime(com.getId(), date);
				record.addColumn("todayCount", bookTimeList.size());
				if (delicacy != null) {
					record.addColumn("GDP", delicacy.getGdp());
				}

				StringBuilder label = new StringBuilder();
				List<Label> labellist = labelService.getLabel("company", com.getId() + "");
				for (Label labels : labellist) {
					label.append(labels.getContent() + ",");
				}
				record.addColumn("label", label.toString());
				record.addColumn("monSales", com.getMonSales());
				record.addColumn("id", com.getId());
				record.addColumn("logo", com.getLogo());
				record.addColumn("name", com.getName());
				record.addColumn("phone", com.getPhone());
				record.addColumn("position", com.getPosition());
				record.addColumn("business_time", com.getBusinessTimeStart() + "-" + com.getBusinessTimeEnd());
				record.addColumn("honor", com.getHonor());
				record.addColumn("monSales", com.getMonSales());
				record.addColumn("notice", com.getNotice());
				record.addColumn("type", com.getClassifyId());
				record.addColumn("info", com.getInfo());
				record.addColumn("distance", range);
				record.addColumn("assess", com.getAssess());
				record.addColumn("isBusiness", com.getIsBusiness());
				record.addColumn("distance", range);
				record.addColumn("img", com.getImg());
				/*
				 * List<CompanyActivity> list = companyActivityService.getList(
				 * Integer.valueOf(com.getId()), "2");
				 */

				String tdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

				List<CompanyActivity> list = companyActivityService.getTime(Integer.valueOf(com.getId()), tdate, "2");

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

		Long count = companyService.count(company);

		if (count != null) {
			grid.addProperties("totalCount", String.valueOf(count));
		} else {
			grid.addProperties("totalCount", 0);
		}
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 获取订单设置内容
	@RequestMapping(params = "getReceipt", method = RequestMethod.POST)
	public void getReceipt(HttpServletRequest request, HttpServletResponse response) {
		String cid = request.getParameter("cid");// 坐标

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(cid)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = companyService.getCompany(Integer.valueOf(cid));

		message.addProperty("message", company.getReceipt());
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);

	}

	// 获取订单设置内容
	@RequestMapping(params = "getXGtime", method = RequestMethod.POST)
	public void getXGtime(HttpServletRequest request, HttpServletResponse response) {
		String cid = request.getParameter("cid");// 坐标

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(cid)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = companyService.getCompany(Integer.valueOf(cid));

		message.addProperty("start", company.getBusinessTimeStart());
		message.addProperty("end", company.getBusinessTimeEnd());
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);

	}

	// 获取视频内容
	@RequestMapping(params = "getCamer", method = RequestMethod.POST)
	public void getCamer(HttpServletRequest request, HttpServletResponse response) {
		String cid = request.getParameter("cid");// 坐标

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(cid)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = companyService.getCompany(Integer.valueOf(cid));

		message.addProperty("isVideo", company.getIsVideo());
		message.addProperty("camerUser", company.getCamerUser());
		message.addProperty("camerPass", company.getCamerPass());
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);

	}

	// 设置视频内容
	@RequestMapping(params = "setCamer", method = RequestMethod.POST)
	public void setCamer(HttpServletRequest request, HttpServletResponse response) {
		String cid = request.getParameter("cid");// 坐标
		String video = request.getParameter("status");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(cid) || StringUtil.isEmpty(video)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = companyService.getCompany(Integer.valueOf(cid));
		if(StringUtil.isEmpty(company.getCamerUser()) || StringUtil.isEmpty(company.getCamerPass())){//2018-11-05 @Tyy
			message.addProperty("message", "请先购买并绑定摄像头");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		company.setIsVideo(video);
		companyService.saveORupdate(company);

		message.addProperty("message", "设置成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// ============================= 后台管理端 =======================

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, HttpServletResponse response) {
		String coordinates = request.getParameter("coordinates");// 坐标
		String info = request.getParameter("info");// 所属区域
		String name = request.getParameter("name");// 店名
		String phone = request.getParameter("phone");// 联系方式
		String position = request.getParameter("position");// 地理位置
		String classifyId = request.getParameter("classifyId");// 商家分类
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(coordinates) || StringUtil.isEmpty(name) || StringUtil.isEmpty(info)
				|| StringUtil.isEmpty(phone) || StringUtil.isEmpty(position) || StringUtil.isEmpty(classifyId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = new Company();
		company.setCoordinates(coordinates);
		company.setInfo(info);
		company.setName(name);
		company.setPhone(phone);
		company.setPosition(position);
		company.setClassifyId(classifyId);
		company.setReceipt("0");
		company.setAudit(0);
		company.setAssess("5");
		companyService.saveORupdate(company);
		Distribution distribution = new Distribution();
		distribution.setCompanyId(company.getId() + "");
		distribution.setGDP("0");
		distribution.setMiniPrice("0");
		distribution.setMode("商家配送");
		distribution.setTime("30");

		Delicacy delicacy = new Delicacy();
		delicacy.setCompanyId(company.getId() + "");
		delicacy.setGdp("0");
		delicacy.setMealFee("0");
		if (company.getClassifyId().equals("1")) {
			distributionService.saveORupdate(distribution);
		} else if (company.getClassifyId().equals("2")) {
			delicacyService.saveORupdate(delicacy);
		} else if (company.getClassifyId().equals("1,2")) {
			distributionService.saveORupdate(distribution);
			delicacyService.saveORupdate(delicacy);
		}

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 后台商家列表
	@RequestMapping(params = "baseview", method = RequestMethod.POST)
	public void baseview(HttpServletRequest request, HttpServletResponse response, Company company) {

		JSonGrid grid = new JSonGrid();

		grid.addProperties("success", true);

		List<Company> companyList = companyService.basecompanyList(company);

		for (Company companys : companyList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", companys.getId());
			record.addColumn("logo", companys.getLogo());
			record.addColumn("name", companys.getName());
			record.addColumn("phone", companys.getPhone());
			record.addColumn("position", companys.getPosition());
			record.addColumn("coordinates", companys.getCoordinates());
			record.addColumn("business_time", companys.getBusinessTimeStart() + "-" + companys.getBusinessTimeEnd());
			record.addColumn("honor", companys.getHonor());
			record.addColumn("notice", companys.getNotice());
			record.addColumn("info", companys.getInfo());
			record.addColumn("assess", companys.getAssess());
			record.addColumn("is_close", companys.getIsOpen());
			record.addColumn("is_business", companys.getIsBusiness());
			record.addColumn("audit", companys.getAudit());
			record.addColumn("img", companys.getImg());
			record.addColumn("classify_id", companys.getClassifyId());
			grid.addRecord(record);

		}
		grid.addProperties("totalCount", companyList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 后台商家详情
	@RequestMapping(params = "companyview", method = RequestMethod.POST)
	public void companyview(HttpServletRequest request, HttpServletResponse response, Company company) {
		String id = request.getParameter("id");// 容量

		JSonMessage message = new JSonMessage();
		message.addProperty("success", true);

		Company companys = companyService.getCompany(Integer.valueOf(id));
		message.addProperty("id", companys.getId());
		message.addProperty("logo", companys.getLogo());
		message.addProperty("name", companys.getName());
		message.addProperty("phone", companys.getPhone());
		message.addProperty("position", companys.getPosition());
		message.addProperty("coordinates", companys.getCoordinates());
		message.addProperty("business_time", companys.getBusinessTimeStart() + "-" + companys.getBusinessTimeEnd());
		message.addProperty("honor", companys.getHonor());
		message.addProperty("notice", companys.getNotice());
		message.addProperty("info", companys.getInfo());
		message.addProperty("assess", companys.getAssess());
		message.addProperty("is_close", companys.getIsOpen());
		message.addProperty("isBusiness", companys.getIsBusiness());
		message.addProperty("audit", companys.getAudit());
		message.addProperty("img", companys.getImg());
		message.addProperty("type", companys.getClassifyId());
		message.addProperty("monSales", companys.getMonSales());

		new PushJson().outString(message.toJSonString(), response);

	}

	// 审核列表
	@RequestMapping(params = "auditview", method = RequestMethod.POST)
	public void auditview(HttpServletRequest request, HttpServletResponse response, Company company) {

		JSonGrid grid = new JSonGrid();

		grid.addProperties("success", true);

		List<Company> companyList = companyService.auditList();

		for (Company companys : companyList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", companys.getId());
			record.addColumn("logo", companys.getLogo());
			record.addColumn("name", companys.getName());
			record.addColumn("phone", companys.getPhone());
			record.addColumn("position", companys.getPosition());
			record.addColumn("coordinates", companys.getCoordinates());
			record.addColumn("business_time", companys.getBusinessTimeStart() + "-" + companys.getBusinessTimeEnd());
			record.addColumn("honor", companys.getHonor());
			record.addColumn("notice", companys.getNotice());
			record.addColumn("info", companys.getInfo());
			record.addColumn("assess", companys.getAssess());
			record.addColumn("is_close", companys.getIsOpen());
			record.addColumn("isBusiness", companys.getIsBusiness());
			record.addColumn("audit", companys.getAudit());
			record.addColumn("img", companys.getImg());
			grid.addRecord(record);
		}

		grid.addProperties("totalCount", companyList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// // 通过审核
	// @RequestMapping(params = "auditUpdate", method = RequestMethod.POST)
	// public void auditUpdate(HttpServletRequest request, HttpServletResponse
	// response, Company company) {
	//
	// JSonMessage message = new JSonMessage();
	// if (StringUtil.isEmpty(company.getId() + "") || company.getId() == 0) {
	// message.addProperty("message", "id不能为空");
	// message.addProperty("success", false);
	// new PushJson().outString(message.toJSonString(), response);
	// return;
	// }
	// companyService.auditUpdate(company.getId() + "");
	// message.addProperty("message", "审核成功");
	// message.addProperty("success", true);
	// new PushJson().outString(message.toJSonString(), response);
	//
	// }

	// ======================================商家APP================================================

	// 修改信息
	@RequestMapping(params = "update", method = RequestMethod.POST)
	public void update(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id"); // id
		String notice = request.getParameter("notice"); // 公告
		String phone = request.getParameter("phone"); // 餐厅电话
		String position = request.getParameter("position"); // 地址
		String businessTimeStart = request.getParameter("businessTimeStart"); // 开始营业时间
		String businessTimeEnd = request.getParameter("businessTimeEnd"); // 结束营业时间
		String isBusiness = request.getParameter("isBusiness"); // 是否营业
		// String honor = request.getParameter("honor"); // 资质
		// String mode = request.getParameter("mode"); // 配送方式
		String print = request.getParameter("print"); // 收银打印机 2018-10-16 @Tyy

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = companyService.getCompany(Integer.valueOf(id));
		if (!StringUtil.isEmpty(notice)) {
			company.setNotice(notice);
		}
		if (!StringUtil.isEmpty(phone)) {
			company.setPhone(phone);
		}
		if (!StringUtil.isEmpty(position)) {
			company.setPosition(position);
		}
		if (!StringUtil.isEmpty(businessTimeStart)) {
			company.setBusinessTimeStart(businessTimeStart.substring(0, 5));
		}
		if (!StringUtil.isEmpty(businessTimeEnd)) {
			company.setBusinessTimeEnd(businessTimeEnd.substring(0, 5));
		}
		if (!StringUtil.isEmpty(isBusiness)) {
			boolean business = true;
			if (isBusiness.equals("0")) {
				business = false;
			} else if (isBusiness.equals("1")) {
				business = true;
			} else {
				message.addProperty("message", "isBusiness状态不正确");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			company.setBusiness(business);
		}
		if (!StringUtil.isEmpty(print)) {
			company.setPrint(print);
		}
		companyService.saveORupdate(company);

		message.addProperty("message", "修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加或修改退订时间
	@RequestMapping(params = "updateUnsubscribe", method = RequestMethod.POST)
	public void updateUnsubscribe(HttpServletRequest request, HttpServletResponse response) {

		String id = request.getParameter("id");
		String unsubscribeTime = request.getParameter("unsubscribeTime");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(unsubscribeTime)) {
			message.addProperty("message", "id或餐桌退订时间不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = companyService.getCompany(Integer.valueOf(id));

		if (company == null) {
			message.addProperty("message", "商家不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		company.setUnsubscribe_time(unsubscribeTime);
		companyService.saveORupdate(company);

		message.addProperty("message", "修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 商家信息
	@RequestMapping(params = "appinfo", method = RequestMethod.POST)
	public void appinfo(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id"); // 商家id

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = companyService.getCompany(Integer.valueOf(id));
		if (company == null) {
			message.addProperty("message", "商家不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		message.addProperty("success", true);

		message.addProperty("id", company.getId());
		message.addProperty("logo", company.getLogo());
		message.addProperty("name", company.getName());
		message.addProperty("phone", company.getPhone());
		message.addProperty("businessTimeStart", company.getBusinessTimeStart());
		message.addProperty("businessTimeEnd", company.getBusinessTimeEnd());
		message.addProperty("honor", company.getHonor());
		message.addProperty("notice", company.getNotice());
		message.addProperty("img", company.getImg());
		message.addProperty("audit", company.getAudit());
		message.addProperty("position", company.getPosition());
		message.addProperty("unsubscribeTime", company.getUnsubscribe_time());
		message.addProperty("isBusiness", company.getIsBusiness());
		message.addProperty("shopType", company.getClassifyId());
		message.addProperty("print", company.getPrint());
		String[] type = company.getClassifyId().split(",");
		if (type.length == 1) {
			if (type[0].equals("1")) {
				Distribution disteibution = distributionService.getDistribution(company.getId());
				if (disteibution != null) {
					message.addProperty("mode", disteibution.getMode());
					message.addProperty("time", disteibution.getTime());
					message.addProperty("GDP", disteibution.getGDP());
					message.addProperty("miniPrice", disteibution.getMiniPrice());
					message.addProperty("DistributionPrice", disteibution.getDistributionPrice());
				}
			}

			if (type[0].equals("2")) {
				Delicacy delicacy = delicacyService.getDelicacy(company.getId());
				if (delicacy != null) {
					message.addProperty("GDP", delicacy.getGdp());
				}
			}

			if (type[0].equals("3")) {
				Hotel hotel = hotelService.getHotel(company.getId());
				if (hotel != null) {
					message.addProperty("miniConsume", hotel.getMiniConsume());
				}
			}

			if (type[0].equals("4")) {
				Sing sing = singService.getSing(company.getId());
				if (sing != null) {
					message.addProperty("miniConsume", sing.getMiniConsume());
				}
			}
		} else if (type.length == 2) {
			if (type[0].equals("1")) {
				Distribution disteibution = distributionService.getDistribution(company.getId());
				if (disteibution != null) {
					message.addProperty("mode", disteibution.getMode());
					message.addProperty("time", disteibution.getTime());
					message.addProperty("GDP", disteibution.getGDP());
					message.addProperty("miniPrice", disteibution.getMiniPrice());
					message.addProperty("DistributionPrice", disteibution.getDistributionPrice());
				}
			}

			if (type[1].equals("2")) {
				Delicacy delicacy = delicacyService.getDelicacy(company.getId());
				if (delicacy != null) {
					message.addProperty("GDP", delicacy.getGdp());
				}
			}
		} else {
			message.addProperty("message", "商家类型不正确");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		new PushJson().outString(message.toJSonString(), response);

	}

	@RequestMapping(params = "getVerson", method = RequestMethod.POST)
	public void getVerson(HttpServletRequest request, HttpServletResponse response) {
		new PushJson().outString(
				"{'code':'1.4','update':'https://pro-bd.fir.im/e8e9ec9d32b432b9cea41ccb843d67aeb04dade9.apk?auth_key=1537834681-0-9b1583649b4a4026bdaa53857e31cd6e-cb45b4bf56295df85dfb64284a07f21a'}",
				response);
	}

	// 自动接单设置
	@RequestMapping(params = "setReceipt", method = RequestMethod.POST)
	public void setReceipt(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id"); // 商家id
		String status = request.getParameter("status"); // 接单状态

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(status)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = companyService.getCompany(Integer.valueOf(id));
		if (company == null) {
			message.addProperty("message", "商家不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		company.setReceipt(status);
		companyService.saveORupdate(company);
		message.addProperty("message", "接单设置成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);

	}

	// 商家属性展示
	@RequestMapping(params = "viewQuality", method = RequestMethod.POST)
	public void viewQuality(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId"); // 商家id

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Delicacy delicacy = delicacyService.getDelicacy(Integer.valueOf(companyId));
		if (delicacy != null) {
			message.addProperty("GDP", delicacy.getGdp());// 人均消费
			message.addProperty("mealFee", delicacy.getMealFee());// 餐位费
			message.addProperty("type", "2");// 美食商家
			message.addProperty("success", true);
		}

		Distribution distribution = distributionService.getDistribution(Integer.valueOf(companyId));
		if (distribution != null) {
			message.addProperty("distributionPrice", distribution.getDistributionPrice());// 配送费
			message.addProperty("DISGDP", distribution.getGDP());// 人均消费
			message.addProperty("miniPriceMS", distribution.getMiniPrice());// 起送价格
			message.addProperty("time", distribution.getTime());// 配送时间
			message.addProperty("type", "1");// 外卖商家
			message.addProperty("success", true);
		}

		if (distribution != null && delicacy != null) {
			message.addProperty("type", "1,2");// 外卖,美食商家
		}

		new PushJson().outString(message.toJSonString(), response);

	}

	// 用户管理
	@RequestMapping(params = "userCount", method = RequestMethod.POST)
	public void userCount(HttpServletRequest request, HttpServletResponse response) {
		String companyId = request.getParameter("companyId"); // 商家id
		String start = request.getParameter("start"); // 起始页
		String limit = request.getParameter("limit"); // 容量

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(start) || StringUtil.isEmpty(limit) || StringUtil.isEmpty(companyId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		List<User> users = userService.getCompanyId(Integer.valueOf(companyId), Integer.valueOf(start),
				Integer.valueOf(limit));
		List<User> count = userService.getCompanyId(Integer.valueOf(companyId));
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (User user : users) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("createTime", user.getCreateTime());
			record.addColumn("phone", user.getUserName());
			Object[] obj = orderService.getUserCount(user.getId(), Integer.valueOf(companyId));
			// 2018-10-13 @Tyy start
			String balance = "0";
			if (obj != null) {
				record.addColumn("num", obj[0]);
				if (obj[1] != null) {
					DecimalFormat dFormat = new DecimalFormat("######.00");
					balance = dFormat.format(Double.valueOf(obj[1].toString()));
					obj[1].toString();
				}
				record.addColumn("pay", balance);
			} // end
			else {
				record.addColumn("num", 0);
				record.addColumn("pay", 0);
			}
			grid.addRecord(record);
		}
		// List<Object[]> list =
		// orderService.getUserCount(Integer.valueOf(companyId),
		// Integer.valueOf(start), Integer.valueOf(limit));
		// for (Object[] obj : list) {
		// JSonGridRecord record = new JSonGridRecord();
		// record.addColumn("num", obj[0]);
		// record.addColumn("pay", obj[1]);
		// record.addColumn("userId", obj[2]);
		// User user = userService.getid(Integer.valueOf(obj[2].toString()));
		// if(user != null){
		// record.addColumn("createTime", user.getCreateTime());
		// record.addColumn("phone", user.getUserName());
		// }
		// grid.addRecord(record);
		// }
		grid.addProperties("totalCount", count.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 按子分类查询商家 2018-10-27 @Tyy

	/**
	 * 参数说明 sort ：传递参数均为大写 SALE. 销售量排序 DISTANCE. 距离排序 PERSON. 人均消费排序 SCORE.
	 * 评价分排序 GENERAL. 综合排序 （这个规则根据我们自身原先定义的商家发红包活跃情况进行排序）
	 */
	@RequestMapping(params = "getOrder", method = RequestMethod.POST)
	public void getOrder(HttpServletRequest request, HttpServletResponse response, Company company) {
		String location = request.getParameter("location");// location =
		// "25.2560978214,110.2188938856";
		final String sort = request.getParameter("sort");// distance 距离 volume
		// 销量 score 综合排序
		String start = request.getParameter("start");// 起始页
		String limit = request.getParameter("limit");// 容量

		String type = request.getParameter("type");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)) {
			message.addProperty("message", "start和limit不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if (StringUtil.isEmpty(location) || StringUtil.isEmpty(sort) || StringUtil.isEmpty(type)) {
			message.addProperty("message", "classify,location,type和sort不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Company> companyList = companyService.typeCompany(company, sort, Integer.valueOf(type),
				Integer.valueOf(start), Integer.valueOf(limit));
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
			if (Integer.valueOf(stu[1].toString()) <= 20000000) {// 距离
				JSonGridRecord record = new JSonGridRecord();
				Delicacy delicacy = delicacyService.getDelicacy(com.getId());
				String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				List<BookTime> bookTimeList = bookTimeService.getCompanyBookTime(com.getId(), date);
				record.addColumn("todayCount", bookTimeList.size());
				if (delicacy != null) {
					record.addColumn("GDP", delicacy.getGdp());
				}

				StringBuilder label = new StringBuilder();
				List<Label> labellist = labelService.getLabel("company", com.getId() + "");
				for (Label labels : labellist) {
					label.append(labels.getContent() + ",");
				}
				record.addColumn("label", label.toString());
				record.addColumn("monSales", com.getMonSales());
				record.addColumn("id", com.getId());
				record.addColumn("logo", com.getLogo());
				record.addColumn("name", com.getName());
				record.addColumn("phone", com.getPhone());
				record.addColumn("position", com.getPosition());
				record.addColumn("business_time", com.getBusinessTimeStart() + "-" + com.getBusinessTimeEnd());
				record.addColumn("honor", com.getHonor());
				record.addColumn("monSales", com.getMonSales());
				record.addColumn("notice", com.getNotice());
				record.addColumn("type", com.getClassifyId());
				record.addColumn("info", com.getInfo());
				record.addColumn("distance", range);
				record.addColumn("assess", com.getAssess());
				record.addColumn("isBusiness", com.getIsBusiness());
				record.addColumn("distance", range);
				record.addColumn("img", com.getImg());
				/*
				 * List<CompanyActivity> list = companyActivityService.getList(
				 * Integer.valueOf(com.getId()), "2");
				 */

				String tdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

				List<CompanyActivity> list = companyActivityService.getTime(Integer.valueOf(com.getId()), tdate, "2");

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

		Long count = companyService.count(company);

		if (count != null) {
			grid.addProperties("totalCount", String.valueOf(count));
		} else {
			grid.addProperties("totalCount", 0);
		}
		new PushJson().outString(grid.toJSonString("list"), response);
	}

}