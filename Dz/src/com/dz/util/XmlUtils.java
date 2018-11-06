package com.dz.util;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.dz.entity.Order;

public class XmlUtils {

	// 微信支付统一下单结果xml解析
	public static Map<String, String> orderXmlOut(String xml)
			throws DocumentException, FileNotFoundException, UnsupportedEncodingException {
		Map<String, String> map = new HashMap<String, String>();
		// 将字符串转为XML
		Document doc = DocumentHelper.parseText(xml);
		// 获取根节点
		Element rootElt = doc.getRootElement();
		// 拿到根节点的名称
		if (rootElt.getName().equals("xml")) {
			String return_code = rootElt.elementTextTrim("return_code");

			if (return_code.equals("SUCCESS")) {
				String return_msg = rootElt.elementTextTrim("return_msg");
				String appid = rootElt.elementTextTrim("appid");
				String mch_id = rootElt.elementTextTrim("mch_id");
				String nonce_str = rootElt.elementTextTrim("nonce_str");
				String sign = rootElt.elementTextTrim("sign");
				String result_code = rootElt.elementTextTrim("result_code");

				map.put("return_code", return_code == null ? "" : return_code);
				map.put("return_msg", return_msg == null ? "" : return_msg);
				map.put("appid", appid == null ? "" : appid);
				map.put("mch_id", mch_id == null ? "" : mch_id);
				map.put("nonce_str", nonce_str == null ? "" : nonce_str);
				map.put("sign", sign == null ? "" : sign);
				map.put("result_code", result_code == null ? "" : result_code);
				if (result_code.equals("SUCCESS")) {
					String prepay_id = rootElt.elementTextTrim("prepay_id");
					String trade_type = rootElt.elementTextTrim("trade_type");
					map.put("prepay_id", prepay_id == null ? "" : prepay_id);
					map.put("trade_type", trade_type == null ? "" : trade_type);
				} else {
					String err_code = rootElt.elementTextTrim("err_code");
					String err_code_des = rootElt.elementTextTrim("err_code_des");
					map.put("err_code", err_code == null ? "" : err_code);
					map.put("err_code_des", err_code_des == null ? "" : err_code_des);
				}
			} else {
				String return_msg = rootElt.elementTextTrim("return_msg");
				map.put("return_msg", return_msg == null ? "" : return_msg);
			}
		}
		return map;
	}

	// 微信支付订单查询结果xml解析
	public static Map<String, String> selectXmlOut(String xml)
			throws DocumentException, FileNotFoundException, UnsupportedEncodingException {
		Map<String, String> map = new HashMap<String, String>();
		// 将字符串转为XML
		Document doc = DocumentHelper.parseText(xml);
		// 获取根节点
		Element rootElt = doc.getRootElement();
		// 拿到根节点的名称
		if (rootElt.getName().equals("xml")) {
			String return_code = rootElt.elementTextTrim("return_code");

			if (return_code.equals("SUCCESS")) {
				String return_msg = rootElt.elementTextTrim("return_msg");
				String appid = rootElt.elementTextTrim("appid");
				String mch_id = rootElt.elementTextTrim("mch_id");
				String nonce_str = rootElt.elementTextTrim("nonce_str");
				String sign = rootElt.elementTextTrim("sign");
				String result_code = rootElt.elementTextTrim("result_code");

				map.put("return_code", return_code == null ? "" : return_code);
				map.put("return_msg", return_msg == null ? "" : return_msg);
				map.put("appid", appid == null ? "" : appid);
				map.put("mch_id", mch_id == null ? "" : mch_id);
				map.put("nonce_str", nonce_str == null ? "" : nonce_str);
				map.put("sign", sign == null ? "" : sign);
				map.put("result_code", result_code == null ? "" : result_code);
				if (result_code.equals("SUCCESS")) {
					String openid = rootElt.elementTextTrim("openid");
					String trade_type = rootElt.elementTextTrim("trade_type");
					String trade_state = rootElt.elementTextTrim("trade_state");
					String bank_type = rootElt.elementTextTrim("bank_type");
					String total_fee = rootElt.elementTextTrim("total_fee");
					String cash_fee = rootElt.elementTextTrim("cash_fee");
					String transaction_id = rootElt.elementTextTrim("transaction_id");
					String out_trade_no = rootElt.elementTextTrim("out_trade_no");
					String time_end = rootElt.elementTextTrim("time_end");
					String trade_state_desc = rootElt.elementTextTrim("trade_state_desc");
					map.put("openid", openid == null ? "" : openid);
					map.put("trade_type", trade_type == null ? "" : trade_type);
					map.put("trade_state", trade_state == null ? "" : trade_state);
					map.put("bank_type", bank_type == null ? "" : bank_type);
					map.put("total_fee", total_fee == null ? "" : total_fee);
					map.put("cash_fee", cash_fee == null ? "" : cash_fee);
					map.put("transaction_id", transaction_id == null ? "" : transaction_id);
					map.put("out_trade_no", out_trade_no == null ? "" : out_trade_no);
					map.put("time_end", time_end == null ? "" : time_end);
					map.put("trade_state_desc", trade_state_desc == null ? "" : trade_state_desc);
				} else {
					String err_code = rootElt.elementTextTrim("err_code");
					String err_code_des = rootElt.elementTextTrim("err_code_des");
					map.put("err_code", err_code == null ? "" : err_code);
					map.put("err_code_des", err_code_des == null ? "" : err_code_des);
				}
			} else {
				String return_msg = rootElt.elementTextTrim("return_msg");
				map.put("return_msg", return_msg == null ? "" : return_msg);
			}
		}
		return map;
	}

	// 微信支付关闭订单结果xml解析
	public static Map<String, String> closeXmlOut(String xml)
			throws DocumentException, FileNotFoundException, UnsupportedEncodingException {
		Map<String, String> map = new HashMap<String, String>();
		// 将字符串转为XML
		Document doc = DocumentHelper.parseText(xml);
		// 获取根节点
		Element rootElt = doc.getRootElement();
		// 拿到根节点的名称
		if (rootElt.getName().equals("xml")) {
			String return_code = rootElt.elementTextTrim("return_code");

			if (return_code.equals("SUCCESS")) {
				String return_msg = rootElt.elementTextTrim("return_msg");
				String appid = rootElt.elementTextTrim("appid");
				String mch_id = rootElt.elementTextTrim("mch_id");
				String nonce_str = rootElt.elementTextTrim("nonce_str");
				String sign = rootElt.elementTextTrim("sign");
				String result_code = rootElt.elementTextTrim("result_code");

				map.put("return_code", return_code == null ? "" : return_code);
				map.put("return_msg", return_msg == null ? "" : return_msg);
				map.put("appid", appid == null ? "" : appid);
				map.put("mch_id", mch_id == null ? "" : mch_id);
				map.put("nonce_str", nonce_str == null ? "" : nonce_str);
				map.put("sign", sign == null ? "" : sign);
				map.put("result_code", result_code == null ? "" : result_code);
				if (result_code.equals("SUCCESS")) {
					String prepay_id = rootElt.elementTextTrim("prepay_id");
					String trade_type = rootElt.elementTextTrim("trade_type");
					map.put("prepay_id", prepay_id == null ? "" : prepay_id);
					map.put("trade_type", trade_type == null ? "" : trade_type);
				} else {
					String err_code = rootElt.elementTextTrim("err_code");
					String err_code_des = rootElt.elementTextTrim("err_code_des");
					map.put("err_code", err_code == null ? "" : err_code);
					map.put("err_code_des", err_code_des == null ? "" : err_code_des);
				}
			} else {
				String return_msg = rootElt.elementTextTrim("return_msg");
				map.put("return_msg", return_msg == null ? "" : return_msg);
			}
		}
		return map;
	}

	// 微信支付回调xml解析
	public static Map<String, String> wxBackXmlOut(String xml)
			throws DocumentException, FileNotFoundException, UnsupportedEncodingException {
		Map<String, String> map = new HashMap<String, String>();
		try {//2018-11-05 @Tyy
			Document doc = null;
			// 将字符串转为XML
			doc = DocumentHelper.parseText(xml);
			// 获取根节点
			Element rootElt = doc.getRootElement();
			// 拿到根节点的名称
			if (rootElt.getName().equals("xml")) {
				String return_code = rootElt.elementTextTrim("return_code");
				if (return_code.equals("SUCCESS")) {
					String appid = rootElt.elementTextTrim("appid");
					String mch_id = rootElt.elementTextTrim("mch_id");
					String nonce_str = rootElt.elementTextTrim("nonce_str");
					String sign = rootElt.elementTextTrim("sign");
					String result_code = rootElt.elementTextTrim("result_code");

					map.put("return_code", return_code == null ? "" : return_code);
					map.put("appid", appid == null ? "" : appid);
					map.put("mch_id", mch_id == null ? "" : mch_id);
					map.put("nonce_str", nonce_str == null ? "" : nonce_str);
					map.put("sign", sign == null ? "" : sign);
					map.put("result_code", result_code == null ? "" : result_code);
					if (result_code.equals("SUCCESS")) {
						String openid = rootElt.elementTextTrim("openid");
						String trade_type = rootElt.elementTextTrim("trade_type");
						String bank_type = rootElt.elementTextTrim("bank_type");
						String total_fee = rootElt.elementTextTrim("total_fee");
						String cash_fee = rootElt.elementTextTrim("cash_fee");
						String transaction_id = rootElt.elementTextTrim("transaction_id");
						String out_trade_no = rootElt.elementTextTrim("out_trade_no");
						String time_end = rootElt.elementTextTrim("time_end");
						map.put("openid", openid == null ? "" : openid);
						map.put("trade_type", trade_type == null ? "" : trade_type);
						map.put("bank_type", bank_type == null ? "" : bank_type);
						map.put("total_fee", total_fee == null ? "" : total_fee);
						map.put("cash_fee", cash_fee == null ? "" : cash_fee);
						map.put("transaction_id", transaction_id == null ? "" : transaction_id);
						map.put("out_trade_no", out_trade_no == null ? "" : out_trade_no);
						map.put("time_end", time_end == null ? "" : time_end);
					} else {
						String err_code = rootElt.elementTextTrim("err_code");
						String err_code_des = rootElt.elementTextTrim("err_code_des");
						map.put("err_code", err_code == null ? "" : err_code);
						map.put("err_code_des", err_code_des == null ? "" : err_code_des);
					}
				} else {
					String return_msg = rootElt.elementTextTrim("return_msg");
					map.put("return_msg", return_msg == null ? "" : return_msg);
				}
			}
			return map;
		} catch (Exception e) {
			map.put("return_msg", "xml解析失败");
			return map;
		}
	}

	// 微信退款回调xml解析
	public static Map<String, String> wxRefundBackXmlOut(String xml)
			throws DocumentException, FileNotFoundException, UnsupportedEncodingException {

		Map<String, String> map = new HashMap<String, String>();
		Document doc = null;
		// 将字符串转为XML
		doc = DocumentHelper.parseText(xml);
		// 获取根节点
		Element rootElt = doc.getRootElement();
		// 拿到根节点的名称
		if (rootElt.getName().equals("xml")) {
			String return_code = rootElt.elementTextTrim("return_code");

			if (return_code.equals("SUCCESS")) {
				String appid = rootElt.elementTextTrim("appid");
				String mch_id = rootElt.elementTextTrim("mch_id");
				String nonce_str = rootElt.elementTextTrim("nonce_str");
				String req_info = rootElt.elementTextTrim("req_info");
				// String result_code = rootElt.elementTextTrim("result_code");

				map.put("return_code", return_code == null ? "" : return_code);
				map.put("appid", appid == null ? "" : appid);
				map.put("mch_id", mch_id == null ? "" : mch_id);
				map.put("nonce_str", nonce_str == null ? "" : nonce_str);
				// map.put("req_info", req_info == null ? "" : req_info);
				// map.put("result_code", result_code == null ? "" :
				// result_code);
				if (!StringUtil.isEmpty(req_info)) {
					String decrypt = MyWXPayUtil.getRefundDecrypt(req_info);
					System.out.println("decrypt====>" + decrypt);
					Map<String, String> result = XmlUtils.wxBackXmlInfo(decrypt);
					System.out.println("result====>" + result);
					String openid = result.get("openid");
					String trade_type = result.get("trade_type");
					String bank_type = result.get("bank_type");
					String total_fee = result.get("total_fee");
					String cash_fee = result.get("cash_fee");
					String transaction_id = result.get("transaction_id");
					String out_trade_no = result.get("out_trade_no");
					String time_end = rootElt.elementTextTrim("time_end");
					map.put("openid", openid == null ? "" : openid);
					map.put("trade_type", trade_type == null ? "" : trade_type);
					map.put("bank_type", bank_type == null ? "" : bank_type);
					map.put("total_fee", total_fee == null ? "" : total_fee);
					map.put("cash_fee", cash_fee == null ? "" : cash_fee);
					map.put("transaction_id", transaction_id == null ? "" : transaction_id);
					map.put("out_trade_no", out_trade_no == null ? "" : out_trade_no);
					map.put("time_end", time_end == null ? "" : time_end);
				}
			} else {
				String return_msg = rootElt.elementTextTrim("return_msg");
				map.put("return_msg", return_msg == null ? "" : return_msg);
			}
		}
		return map;
	}

	// 微信退款回调内容解析
	public static Map<String, String> wxBackXmlInfo(String xml)
			throws DocumentException, FileNotFoundException, UnsupportedEncodingException {
		Map<String, String> map = new HashMap<String, String>();
		Document doc = null;
		// 将字符串转为XML
		doc = DocumentHelper.parseText(xml);
		// 获取根节点
		Element rootElt = doc.getRootElement();
		// 拿到根节点的名称
		if (rootElt.getName().equals("root")) {
			String refund_status = rootElt.elementTextTrim("refund_status");
			if (refund_status.equals("SUCCESS")) {
				String out_refund_no = rootElt.elementTextTrim("out_refund_no");
				String out_trade_no = rootElt.elementTextTrim("out_trade_no");
				String transaction_id = rootElt.elementTextTrim("transaction_id");
				String time_end = rootElt.elementTextTrim("time_end");
				String openid = rootElt.elementTextTrim("openid");
				String trade_type = rootElt.elementTextTrim("trade_type");
				String bank_type = rootElt.elementTextTrim("bank_type");
				String total_fee = rootElt.elementTextTrim("total_fee");
				String cash_fee = rootElt.elementTextTrim("cash_fee");

				map.put("openid", openid == null ? "" : openid);
				map.put("out_refund_no", out_refund_no == null ? "" : out_refund_no);
				map.put("trade_type", trade_type == null ? "" : trade_type);
				map.put("bank_type", bank_type == null ? "" : bank_type);
				map.put("total_fee", total_fee == null ? "" : total_fee);
				map.put("cash_fee", cash_fee == null ? "" : cash_fee);
				map.put("transaction_id", transaction_id == null ? "" : transaction_id);
				map.put("out_trade_no", out_trade_no == null ? "" : out_trade_no);
				map.put("time_end", time_end == null ? "" : time_end);

			} else {
				String return_msg = rootElt.elementTextTrim("return_msg");
				map.put("return_msg", return_msg == null ? "" : return_msg);
			}
		}
		return map;
	}

	// 微信支付给商家解析
	public static Map<String, String> collectMoney(String xml)
			throws DocumentException, FileNotFoundException, UnsupportedEncodingException {
		Map<String, String> map = new HashMap<String, String>();
		Document doc = null;
		// 将字符串转为XML
		doc = DocumentHelper.parseText(xml);
		// 获取根节点
		Element rootElt = doc.getRootElement();
		// 拿到根节点的名称
		if (rootElt.getName().equals("xml")) {
			String return_code = "";
			String return_msg = "";
			return_code = rootElt.elementTextTrim("return_code");
			return_msg = rootElt.elementTextTrim("return_msg");
			map.put("return_code", return_code);
			if (return_code.equals("SUCCESS")) {
				String result_code = "";
				String partner_trade_no = "";
				String payment_no = "";
				String payment_time = "";
				result_code = rootElt.elementTextTrim("result_code");
				partner_trade_no = rootElt.elementTextTrim("partner_trade_no");
				payment_no = rootElt.elementTextTrim("payment_no");
				payment_time = rootElt.elementTextTrim("payment_time");

				map.put("result_code", result_code);
				map.put("partner_trade_no", partner_trade_no);
				map.put("payment_no", payment_no);
				map.put("payment_time", payment_time);
				map.put("return_msg", return_msg);
			} else {
				map.put("return_msg", return_msg);
			}
		}
		return map;
	}

}
