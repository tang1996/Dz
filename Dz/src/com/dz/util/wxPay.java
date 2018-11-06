package com.dz.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.dom4j.DocumentException;

public class wxPay {

	// 统一下单
	public String order(String body, String attach, String out_trade_no,
			String total_fee) throws IOException, DocumentException {
		body = new String(body.getBytes(), "UTF-8");
		attach = new String(attach.getBytes(), "UTF-8");
		
		//total_fee = total_fee.replace(".", "");
		
		String nonce_str = "dzzb"
				+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String stringSignTemp = "appid=wx932fb18d44e00508" + "&attach="
				+ attach + "&body=" + body + "&mch_id=1507027841"
				+ "&nonce_str=" + nonce_str
				+ "&notify_url=" + URL.WX_PAY_URL
				+ "&out_trade_no=" + out_trade_no
				+ "&spbill_create_ip="+ URL.IP + "&total_fee="
				+ total_fee + "&trade_type=APP" + "&key="
				+ "guangdongshengshenzhengshidongzi";
		String sign = MD5Util.MD5(stringSignTemp).toUpperCase();

		String xml = "<xml>" + "<appid>wx932fb18d44e00508</appid>" + "<attach>"
				+ attach
				+ "</attach>"
				+ "<body>"
				+ body
				+ "</body>"
				+ "<mch_id>1507027841</mch_id>"
				+ "<nonce_str>"
				+ nonce_str
				+ "</nonce_str>"
				+ "<notify_url>"+ URL.WX_PAY_URL +"</notify_url>"
				+ "<out_trade_no>" + out_trade_no + "</out_trade_no>"
				+ "<spbill_create_ip>" + URL.IP + "</spbill_create_ip>"
				+ "<total_fee>"+total_fee+"</total_fee>"
				+ "<trade_type>APP</trade_type>" + "<sign>" + sign + "</sign>"
				+ "</xml>";
		String order = HttpRequest.sendXml("https://api.mch.weixin.qq.com/pay/unifiedorder", xml);
		return order;
	}

	// 订单查询
	public String select(String out_trade_no) throws IOException {

		String nonce_str = "5K8264ILTKCH16CQ2502SI8ZNMTM67VS";
		String stringSignTemp = "appid=" + "wx932fb18d44e00508" + "&mch_id=1507027841"
				+ "&nonce_str=" + nonce_str + "&out_trade_no="
				+ out_trade_no + "&key=" + "guangdongshengshenzhengshidongzi";
		String sign = MD5Util.MD5(stringSignTemp).toUpperCase();

		String xml = "<xml>" + "<appid>" + "wx932fb18d44e00508" + "</appid>"
				+ "<mch_id>1507027841</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>" + "<sign>" + sign + "</sign>" + "</xml>";
		String select = HttpRequest.sendXml("https://api.mch.weixin.qq.com/pay/orderquery", xml);
		return select;
	}

	// 关闭订单
	public String close(String out_trade_no) throws IOException {
		String nonce_str = "5K8264ILTKCH16CQ2502SI8ZNMTM67VS";
		String stringSignTemp = "appid=" + "wx932fb18d44e00508" + "&mch_id=1507027841"
				+ "&nonce_str=" + nonce_str + "&out_trade_no="
				+ out_trade_no + "&key=" + "guangdongshengshenzhengshidongzi";
		String sign = MD5Util.MD5(stringSignTemp).toUpperCase();

		String xml = "<xml>" + "<appid>" + "wx932fb18d44e00508" + "</appid>"
				+ "<mch_id>1507027841</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>" + "<sign>" + sign + "</sign>" + "</xml>";
		String close = HttpRequest.sendXml("https://api.mch.weixin.qq.com/pay/closeorder", xml);
		return close;
	}

	public static void main(String[] args) throws IOException,
			DocumentException {
		String body = "动致充值测试";
		String attach = "深圳分店";
		String out_trade_no = "20150806125347";
		String total_fee = "888";
//		new wxPay().order(body, attach, out_trade_no, total_fee);
		String xml = new wxPay().select(out_trade_no);
		System.out.println(xml);
		Map<String, String> map = XmlUtils.selectXmlOut(xml);
		System.out.println(map);
		String aaa = map.get("mch_id");
		System.out.println(aaa);
		// close();
	}
}
