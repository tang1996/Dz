package com.dz.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class wxRefund {
	// 申请退款
	public String refund(String out_refund_no, String out_trade_no,
			String total_fee, String refund_fee) throws Exception {
		String nonce_str = "dzzb"
				+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String stringSignTemp = "appid=wx932fb18d44e00508"
				+ "&mch_id=1507027841"
				+ "&nonce_str="
				+ nonce_str
				+ "&notify_url=" + URL.WX_PAY_REFUND
				+ "&out_refund_no=" + out_refund_no + "&out_trade_no="
				+ out_trade_no + "&refund_fee=" + refund_fee + "&total_fee="
				+ total_fee + "&key=guangdongshengshenzhengshidongzi";
		String sign = MD5Util.MD5(stringSignTemp).toUpperCase();

		String xml = "<xml>"
				+ "<appid>wx932fb18d44e00508</appid>"
				+ "<mch_id>1507027841</mch_id>"
				+ "<nonce_str>"
				+ nonce_str
				+ "</nonce_str>"
				 +
				 "<notify_url>" + URL.WX_PAY_REFUND + "</notify_url>"
				+ "<out_refund_no>" + out_refund_no + "</out_refund_no>"
				+ "<out_trade_no>" + out_trade_no + "</out_trade_no>"
				+ "<refund_fee>" + refund_fee + "</refund_fee>" + "<total_fee>"
				+ total_fee + "</total_fee>" + "<sign>" + sign + "</sign>"
				+ "</xml>";
		String refund = Certificate.doRefund(
				"https://api.mch.weixin.qq.com/secapi/pay/refund", xml)
				.toString();
		return refund;
	}

	public static boolean doRefund(String out_refund_no, String refund_fee,
			String out_trade_no, String total_fee) throws Exception {
		boolean restul = false;
		if (StringUtil.isEmpty(out_refund_no) || StringUtil.isEmpty(refund_fee)
				|| StringUtil.isEmpty(out_trade_no)
				|| StringUtil.isEmpty(total_fee)) {
			restul = false;
		} else {
			String xml = new wxRefund().refund(out_refund_no, out_trade_no,
					total_fee, refund_fee);
			Map<String, String> map = XmlUtils.selectXmlOut(xml);
			if (!StringUtil.isEmpty(map.get("return_code"))) {
				if (map.get("return_code").equals("SUCCESS")) {
					if (!StringUtil.isEmpty(map.get("result_code"))) {
						if (map.get("result_code").equals("SUCCESS")) {
							restul = true;
						} else {
							restul = false;
						}
					} else {
						restul = false;
					}
				} else {
					restul = false;
				}
			} else {
				restul = false;
			}
		}
		return restul;
	}
}
