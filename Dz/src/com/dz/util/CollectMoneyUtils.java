package com.dz.util;

import java.util.HashMap;
import java.util.Map;

public class CollectMoneyUtils {

	public static String[] CollectMoney(String money, String openId) {
		String[] flg = new String[2];
		String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

		String orderNNo = CollectMoney.getOrderNo();
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("nonce_str", CollectMoney.buildRandom());
		map.put("partner_trade_no", orderNNo);
		map.put("mchid", "1507027841");
		map.put("mch_appid", "wx68ff76cbd77a0056");
		map.put("openid", openId);
		map.put("amount", money);
		map.put("desc", "提现");
		map.put("check_name", "NO_CHECK");
		map.put("spbill_create_ip", "127.0.0.1");
		map.put("sign", CollectMoney.createSign((Map) map));

		String result = "";
		try {
			result = CollectMoney.doSendMoney(url, CollectMoney.createXML(map));
			Map<String, String> xml = XmlUtils.collectMoney(result);
			String return_code = xml.get("return_code");
			if (return_code.equals("SUCCESS")) {
				String result_code = xml.get("result_code");
				if (result_code.equals("SUCCESS")) {
					flg[0] = "true";
					flg[1] = xml.get("payment_no");
				} else {
					flg[0] = "false";
					flg[1] = xml.get("return_msg");
				}
			} else {
				flg[0] = "false";
				flg[1] = xml.get("return_msg");
			}
		} catch (Exception e) {
			e.printStackTrace();
			flg[0] = "false";
			flg[1] = "发送请求失败";
		}

		return flg;
	}
}
