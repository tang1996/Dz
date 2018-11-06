package com.dz.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dz.entity.Order;
import com.dz.entity.OrderType;
import com.dz.entity.Staff;
import com.dz.entity.Track;
import com.dz.service.IOrderService;
import com.dz.service.IOrderTypeService;
import com.dz.service.IStaffService;
import com.dz.service.ITrackService;

@Controller
@RequestMapping("/recevice")
public class wxRecevice extends HttpServlet {

	@Autowired
	private IOrderService orderService;

	@Autowired
	private IOrderTypeService orderTypeService;

	@Autowired
	private ITrackService trackService;
	
	@Autowired
	private IStaffService staffService;

	private static final long serialVersionUID = 960048716931700982L;

	@RequestMapping(value = "/wxBack", method = RequestMethod.POST)
	@ResponseBody
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		synchronized (this) {
			JSonMessage message = new JSonMessage();
			InputStream inStream = request.getInputStream();
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			outSteam.close();
			inStream.close();
			String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
			Map<String, String> map = new HashMap<String, String>();
			try {
				map = XmlUtils.wxBackXmlOut(result);
				if (!StringUtil.isEmpty(map.get("return_code"))) {
					if (map.get("return_code").equals("SUCCESS")) {
						message.addProperty("success", true);
						message.addProperty("sign", map.get("sign"));
						if (!StringUtil.isEmpty(map.get("result_code"))) {
							if (map.get("result_code").equals("SUCCESS")) {
								message
										.addProperty("openid", map
												.get("openid"));
								message.addProperty("out_trade_no", map
										.get("out_trade_no"));
								message.addProperty("total_fee", map
										.get("total_fee"));
								message.addProperty("time_end", map
										.get("time_end"));
								message.addProperty("transaction_id", map
										.get("transaction_id"));
								message.addProperty("message", "支付成功");
								message.addProperty("result_code", map
										.get("result_code"));
								Order order = orderService.getOrderNo(map
										.get("out_trade_no"));
								order.setOrderStatus("paysuccess");
								String payTime = new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss")
										.format(new Date());
								order.setPayTime(payTime);
								orderService.saveORupdate(order);
								System.out.println("companyName====>"
										+ order.getCompanyId().getName());
								if (order.getOrderType().equals("2")) {
									Track track = new Track();
									track.setOrderId(order);
									track.setStatus("pay");
									track.setBewrite("订单已支付");
									track.setCreateTime(new SimpleDateFormat(
											"MM-dd HH:mm").format(new Date()));
									trackService.saveORupdate(track);
								}

								if (order.getOrderType().equals("1")) {
									OrderType orderType = orderTypeService
											.getOrderType(order.getId());
									if (orderType != null) {
										orderType.setStatus("paysuccess");
										orderTypeService
												.saveORupdate(orderType);
									}
									Track track = new Track();
									track.setOrderId(order);
									track.setStatus("pay");
									track.setBewrite("订单已支付");
									track.setCreateTime(new SimpleDateFormat(
											"yyyy-MM-dd HH:mm:ss")
											.format(new Date()));
									trackService.saveORupdate(track);
								}
								//推送商家新订单
								List<String> sss = new ArrayList<String>();
								List<Staff> adminList = staffService.getList(order.getCompanyId().getId(), 1);
								for(Staff admin : adminList){
									sss.add(admin.getUserName());
								}
								List<Staff> cashierList = staffService.getList(order.getCompanyId().getId(), 2);
								for(Staff cashier : cashierList){
									sss.add(cashier.getUserName());
								}
								new PushUtil();
								PushUtil.sendAlias("您有新的琢呗订单,请注意查收", sss, JiguangConfig.companyKey,
										JiguangConfig.companySecret);
							} else {
								message.addProperty("success", false);
								message.addProperty("err_code_des", map
										.get("err_code_des"));
								message.addProperty("message", "支付失败");
								message.addProperty("result_code", map
										.get("result_code"));
								new PushJson().outString(
										message.toJSonString(), response);
								return;
							}
						} else {
							message.addProperty("success", false);
							message.addProperty("message", "获取订单状态失败");
							new PushJson().outString(message.toJSonString(),
									response);
							return;
						}
					}
				}
			} catch (DocumentException e) {
				e.printStackTrace();
				message.addProperty("message", "解析错误");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}

			String resXml = "<xml>"
					+ "<return_code><![CDATA[SUCCESS]]></return_code>"
					+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

			response.setContentType("text/xml");
			response.getWriter().write(resXml);
			response.getWriter().flush();
			response.getWriter().close();
		}
	}
}
