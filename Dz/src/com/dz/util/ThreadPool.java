package com.dz.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.dz.entity.Company;
import com.dz.entity.CompanyActivity;
import com.dz.entity.Order;
import com.dz.entity.OrderType;
import com.dz.entity.Staff;
import com.dz.entity.Track;
import com.dz.service.ICompanyActivityService;
import com.dz.service.ICompanyService;
import com.dz.service.IOrderService;
import com.dz.service.IOrderTypeService;
import com.dz.service.IStaffService;
import com.dz.service.ITrackService;

/**
 * 回调接口定时器
 * 
 * @author Administrator
 * 
 */
public class ThreadPool implements ServletContextListener {

	@Autowired
	private IOrderTypeService orderTypeService;

	@Autowired
	private IOrderService orderService;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private ICompanyActivityService companyActivityService;

	@Autowired
	private ITrackService trackService;

	@Autowired
	private IStaffService staffService;

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

		ScheduledExecutorService schedulePool = Executors.newScheduledThreadPool(5);
		schedulePool.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					synchronized (this) {

						// 商家接单定时器
						if (orderService != null) {
							List<Order> orderList = orderService.getStatus("paysuccess");
							for (Order order : orderList) {
								SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化时间
								Calendar calendar = Calendar.getInstance();
								calendar.setTimeInMillis(d.parse(order.getPayTime()).getTime());
								calendar.add(Calendar.SECOND, 180);

								long result = (System.currentTimeMillis() - calendar.getTime().getTime()) / 1000;

								if (result > 0) {
									order.setOrderStatus("unreceiption");
									if (order.getOrderType().equals("1")) {
										OrderType orderType = orderTypeService.getOrderType(order.getId());
										if (orderType != null) {
											orderType.setStatus("unreceiption");
											orderTypeService.saveORupdate(orderType);
										}
									}
									String out_refund_no = "B" + order.getId()
											+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

									String total_fee = order.getPay();
									if (!StringUtil.isEmpty(total_fee)) {
										double newfee = Double.valueOf(total_fee) * 100;
										total_fee = ((int) newfee) + "";
									} else {
										total_fee = "0";
									}

									boolean fig = false;
									if (!fig) {
										for (int i = 0; i <= 3; i++) {
											boolean wxRestul = wxRefund.doRefund(out_refund_no, total_fee + "",
													order.getOrderNo(), total_fee + "");

											if (wxRestul) {
												order.setOrderStatus("unreceiption");
												order.setFinishTime(
														new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
												order.setBack_no(out_refund_no);
												order.setIsAccount("4");
												orderService.saveORupdate(order);
												fig = true;
												Track track = new Track();
												if (order.getOrderType().equals("2")) {

													track.setOrderId(order);
													track.setStatus("unreceiption");
													track.setBewrite("接单超时已退款");
													track.setCreateTime(
															new SimpleDateFormat("MM-dd HH:mm").format(new Date()));

												}

												if (order.getOrderType().equals("1")) {
													track.setOrderId(order);
													track.setStatus("backBalance");
													track.setBewrite("接单超时已退款");
													track.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
															.format(new Date()));
												}
												trackService.saveORupdate(track);

												List<String> templist = new ArrayList<String>();
												templist.add(order.getUserId().getUserName());
												PushUtil.sendAlias("商家接单超时自动退款", templist, JiguangConfig.userKey,
														JiguangConfig.userSecret);

												break;
											}
											orderService.saveORupdate(order);

										}
									}
								}
							}
						}

						// 商家自动接单定时器
						if (orderService != null) {
							List<Order> orderList = orderService.getStatus("paysuccess");
							for (Order order : orderList) {
								if (order.getCompanyId() != null) {
									if (order.getCompanyId().getReceipt().equals("1")) {
										if (order.getOrderType().equals("1")) {
											order.setOrderStatus("doing");
											orderService.saveORupdate(order);
											OrderType ordertype = orderTypeService.getOrderType(order
													.getId());
											if (ordertype != null) {
												Random random = new Random();
												int code = random.nextInt(9999) % (9999 - 1001) + 1000;
												ordertype.setCode(code + "");
												ordertype.setStatus("untaking");
												ordertype.setReceiptTime(new SimpleDateFormat(
														"yyyy-MM-dd HH:mm:ss").format(new Date()));
												orderTypeService.saveORupdate(ordertype);
											}
											
											Track track = new Track();
											track.setOrderId(order);
											track.setStatus("companyTaking");
											track.setBewrite("商家已接单");
											track.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
											trackService.saveORupdate(track);
											
											List<String> templist = new ArrayList<String>();
											templist.add(order.getUserId().getUserName());
											PushUtil.sendAlias("商家已接单", templist, JiguangConfig.userKey, JiguangConfig.userSecret);
										}
									}
								}
							}

						}

						// 商家休息
						if (companyService != null) {
							List<Company> companyList = companyService.getIsBusiness("1");
							for (Company company : companyList) {
								String date = new SimpleDateFormat("HH:mm:ss").format(new Date());// 格式化时间
								if (date.equals(company.getBusinessTimeEnd() + ":00")
										|| date.equals(company.getBusinessTimeEnd() + ":01")
										|| date.equals(company.getBusinessTimeEnd() + ":02")) {
									company.setBusiness(false);
									companyService.saveORupdate(company);
								}
							}
						}

						// 商家营业
						if (companyService != null) {
							List<Company> companyList = companyService.getIsBusiness("0");
							for (Company company : companyList) {
								String date = new SimpleDateFormat("HH:mm:ss").format(new Date());// 格式化时间
								if (date.equals(company.getBusinessTimeStart() + ":00")//2018-11-05 @Tyy
										|| date.equals(company.getBusinessTimeStart() + ":01")
										|| date.equals(company.getBusinessTimeStart() + ":02")) {
									company.setBusiness(true);
									companyService.saveORupdate(company);
								}
							}
						}

						// 商家活动开启
						if (companyActivityService != null) {
							List<CompanyActivity> companyActivityList = companyActivityService.getList("0");
							for (CompanyActivity companyActivity : companyActivityList) {
								String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());// 格式化时间
								if (date.equals(companyActivity.getStartTime() + "00:00:00")
										|| date.equals(companyActivity.getStartTime() + "00:00:01")
										|| date.equals(companyActivity.getStartTime() + "00:00:02")) {
									companyActivity.setIsOpen(true);
									companyActivityService.saveORupdate(companyActivity);
								}
							}
						}

						// 商家活动关闭
						if (companyActivityService != null) {
							List<CompanyActivity> companyActivityList = companyActivityService.getList("1");
							for (CompanyActivity companyActivity : companyActivityList) {
								String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());// 格式化时间
								if (date.equals(companyActivity.getEndTime() + "23:59:57")
										|| date.equals(companyActivity.getEndTime() + "23:59:58")
										|| date.equals(companyActivity.getEndTime() + "23:59:59")) {
									companyActivity.setIsOpen(false);
									companyActivityService.saveORupdate(companyActivity);
								}
							}
						}

						// 骑手接单定时器
						if (orderService != null) {
							List<OrderType> orderTypeList = orderTypeService.getList("untaking");
							for (OrderType orderType : orderTypeList) {
								SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化时间
								Calendar calendar = Calendar.getInstance();
								calendar.setTimeInMillis(d.parse(orderType.getReceiptTime()).getTime());
								calendar.add(Calendar.SECOND, 900);

								long result = (System.currentTimeMillis() - calendar.getTime().getTime()) / 1000;

								if (result > 0) {
									if (orderType.getOrderId() != null) {
										Order order = orderType.getOrderId();
										order.setOrderStatus("unreceiption");
										String out_refund_no = "B" + order.getId()
												+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

										String total_fee = order.getPay();
										if (!StringUtil.isEmpty(total_fee)) {
											double newfee = Double.valueOf(total_fee) * 100;
											total_fee = ((int) newfee) + "";
										} else {
											total_fee = "0";
										}

										boolean fig = false;
										if (!fig) {
											for (int i = 0; i <= 3; i++) {
												boolean wxRestul = wxRefund.doRefund(out_refund_no, total_fee + "",
														order.getOrderNo(), total_fee + "");

												if (wxRestul) {
													order.setOrderStatus("unreceiption");
													order.setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
															.format(new Date()));
													order.setBack_no(out_refund_no);
													orderService.saveORupdate(order);
													fig = true;
													Track track = new Track();
													if (order.getOrderType().equals("1")) {
														track.setOrderId(order);
														track.setStatus("backBalance");
														track.setBewrite("骑手接单超时已退款");
														track.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
																.format(new Date()));
													}
													trackService.saveORupdate(track);
													List<String> user = new ArrayList<String>();
													user.add(order.getUserId().getUserName());
													PushUtil.sendAlias("骑手接单超时自动退款", user, JiguangConfig.userKey, JiguangConfig.userSecret);

													// 推送商家新订单
													List<String> sss = new ArrayList<String>();
													List<Staff> adminList = staffService
															.getList(order.getCompanyId().getId(), 1);
													for (Staff admin : adminList) {
														sss.add(admin.getUserName());
													}
													List<Staff> cashierList = staffService
															.getList(order.getCompanyId().getId(), 2);
													for (Staff cashier : cashierList) {
														sss.add(cashier.getUserName());
													}
													PushUtil.sendAlias("骑手接单超时自动退款", sss, JiguangConfig.companyKey, JiguangConfig.companySecret);
													orderType.setStatus("unreceiption");
													orderTypeService.saveORupdate(orderType);

													break;
												}
												orderService.saveORupdate(order);

											}
										}
									}
								}
							}
						}
						
						
					}
				} catch (

				Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 2, TimeUnit.SECONDS);

	}

	public ICompanyService getCompanyService() {
		return companyService;
	}

	public void setCompanyService(ICompanyService companyService) {
		this.companyService = companyService;
	}

}
