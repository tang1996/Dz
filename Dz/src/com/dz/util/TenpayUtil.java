package com.dz.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TenpayUtil {
	
	private static Object Server;
	private static String QRfromGoogle;

	/**
	 * 鎶婂璞¤浆鎹㈡垚瀛楃涓�
	 * @param obj
	 * @return String 杞崲鎴愬瓧绗︿覆,鑻ュ璞′负null,鍒欒繑鍥炵┖瀛楃涓�
	 */
	public static String toString(Object obj) {
		if(obj == null)
			return "";
		
		return obj.toString();
	}
	
	/**
	 * 鎶婂璞¤浆鎹负int鏁板�.
	 * 
	 * @param obj
	 *            鍖呭惈鏁板瓧鐨勫璞�
	 * @return int 杞崲鍚庣殑鏁板�,瀵逛笉鑳借浆鎹㈢殑瀵硅薄杩斿洖0銆�
	 */
	public static int toInt(Object obj) {
		int a = 0;
		try {
			if (obj != null)
				a = Integer.parseInt(obj.toString());
		} catch (Exception e) {

		}
		return a;
	}
	
	/**
	 * 鑾峰彇褰撳墠鏃堕棿 yyyyMMddHHmmss
	 * @return String
	 */ 
	public static String getCurrTime() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String s = outFormat.format(now);
		return s;
	}
	
	/**
	 * 鑾峰彇褰撳墠鏃ユ湡 yyyyMMdd
	 * @param date
	 * @return String
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String strDate = formatter.format(date);
		return strDate;
	}
	
	/**
	 * 鍙栧嚭涓�釜鎸囧畾闀垮害澶у皬鐨勯殢鏈烘鏁存暟.
	 * 
	 * @param length
	 *            int 璁惧畾鎵�彇鍑洪殢鏈烘暟鐨勯暱搴︺�length灏忎簬11
	 * @return int 杩斿洖鐢熸垚鐨勯殢鏈烘暟銆�
	 */
	public static int buildRandom(int length) {
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ((random * num));
	}
	
	/**
	 * 鑾峰彇缂栫爜瀛楃闆�
	 * @param request
	 * @param response
	 * @return String
	 */

	public static String getCharacterEncoding(HttpServletRequest request,
			HttpServletResponse response) {
		
		if(null == request || null == response) {
			return "gbk";
		}
		
		String enc = request.getCharacterEncoding();
		if(null == enc || "".equals(enc)) {
			enc = response.getCharacterEncoding();
		}
		
		if(null == enc || "".equals(enc)) {
			enc = "gbk";
		}
		
		return enc;
	}
	
	public  static String URLencode(String content){
		
		String URLencode;
		
		URLencode= replace(Server.equals(content), "+", "%20");
		
		return URLencode;
	}
	private static String replace(boolean equals, String string, String string2) {
		
		return null;
	}

	/**
	 * 鑾峰彇unix鏃堕棿锛屼粠1970-01-01 00:00:00寮�鐨勭鏁�
	 * @param date
	 * @return long
	 */
	public static long getUnixTime(Date date) {
		if( null == date ) {
			return 0;
		}
		
		return date.getTime()/1000;
	}
	
	 public static String QRfromGoogle(String chl)
	    {
	        int widhtHeight = 300;
	        String EC_level = "L";
	        int margin = 0;
	        String QRfromGoogle;
	        chl = URLencode(chl);
	        
	        QRfromGoogle = "http://chart.apis.google.com/chart?chs=" + widhtHeight + "x" + widhtHeight + "&cht=qr&chld=" + EC_level + "|" + margin + "&chl=" + chl;
	       
	        return QRfromGoogle;
	    }

	/**
	 * 鏃堕棿杞崲鎴愬瓧绗︿覆
	 * @param date 鏃堕棿
	 * @param formatType 鏍煎紡鍖栫被鍨�
	 * @return String
	 */
	public static String date2String(Date date, String formatType) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatType);
		return sdf.format(date);
	}
	
}