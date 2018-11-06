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
import com.dz.entity.Track;
import com.dz.service.IOrderService;
import com.dz.service.IOrderTypeService;
import com.dz.service.ITrackService;

@Controller
@RequestMapping("/recevice")
public class wxRefundBack extends HttpServlet {

	@Autowired
	private IOrderService orderService;

	@Autowired
	private ITrackService trackService;

	private static final long serialVersionUID = 960048716931700982L;

	@RequestMapping(value = "/wxRefundBack", method = RequestMethod.POST)
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
				map = XmlUtils.wxRefundBackXmlOut(result);
				System.out.println("map====>" + map);
				if (!StringUtil.isEmpty(map.get("return_code"))) {
					if (map.get("return_code").equals("SUCCESS")) {
						message.addProperty("success", true);
						message.addProperty("openid", map.get("openid"));
						message.addProperty("out_trade_no", map
								.get("out_trade_no"));
						message.addProperty("total_fee", map.get("total_fee"));
						message.addProperty("time_end", map.get("time_end"));
						message.addProperty("transaction_id", map
								.get("transaction_id"));
						message.addProperty("message", "退款成功");
						message.addProperty("result_code", map
								.get("result_code"));
						Order order = orderService.getOrderNo(map
								.get("out_trade_no"));
					//	order.setOrderStatus("finish");
						order.setIsAccount("4");
						orderService.saveORupdate(order);
						if (order.getOrderType().equals("2")) {
							Track track = new Track();
							track.setOrderId(order);
							track.setStatus("backBalance");
							track.setBewrite("订单已退款");
							track.setCreateTime(new SimpleDateFormat(
									"MM-dd HH:mm").format(new Date()));
							trackService.saveORupdate(track);
						}

						if (order.getOrderType().equals("1")) {
							Track track = new Track();
							track.setOrderId(order);
							track.setStatus("backBalance");
							track.setBewrite("订单已退款");
							track.setCreateTime(new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss").format(new Date()));
							trackService.saveORupdate(track);
						}

						List<String> sss = new ArrayList<String>();
						sss.add(order.getUserId().getUserName());
						new PushUtil();
						PushUtil.sendAlias("退款已到帐", sss, JiguangConfig.userKey,
								JiguangConfig.userSecret);
					}
				}
			} catch (DocumentException e) {
				e.printStackTrace();
				message.addProperty("message", "解析错误");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			String resXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml> ";

			response.setContentType("text/xml");
			response.getWriter().write(resXml);
			response.getWriter().flush();
			response.getWriter().close();
		}

	}
}
