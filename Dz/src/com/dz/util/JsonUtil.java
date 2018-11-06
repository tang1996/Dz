package com.dz.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil {
	private static JSONObject sendGetUrl(String url) {
		JSONObject jsonObject = null;
		String result = "";
		result = HttpRequest.sendGet(url);
		jsonObject = JSONObject.fromObject(result);

		return jsonObject;
	}

	public static JSONObject position(String address) {
	/*	String params = "address=" + address + "&key=56YBZ-Q4XCJ-DRQF5-KHC62-WGREV-PJFA3";
		JSONObject jsonObject = sendGetUrl("http://apis.map.qq.com/ws/geocoder/v1/?"
				+ params);*/
		
		String address1 = "广东省深圳市龙华区宝能科技园9栋C座6楼609L";
		try {
			System.out.println(HttpRequest.sendPost("http://restapi.amap.com/v3/geocode/geo","key=d64814d634b8469a9fef1e500892e7cf&address=" + address1));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static JSONObject coordinate() {
		String params = "location=25.256093,110.218728&key=56YBZ-Q4XCJ-DRQF5-KHC62-WGREV-PJFA3";
		JSONObject jsonObject = sendGetUrl("http://apis.map.qq.com/ws/geocoder/v1/?"
				+ params);
		return jsonObject;
	}

	public static JSONObject distance(String from, String to) {
		String params = "from=" + from + "&to=" + to
				+ "&key=56YBZ-Q4XCJ-DRQF5-KHC62-WGREV-PJFA3";
		JSONObject jsonObject = sendGetUrl("http://apis.map.qq.com/ws/distance/v1/?"
				+ params);
		return jsonObject;
	}

	public static String getPosition(String address) {
		JSONObject result = JSONObject.fromObject(position(address));
	//	if(result.get("status")){
			
	//	}
		System.out.println("result====>" + result);
		JSONObject elements = JSONObject.fromObject((JSONObject) result
				.get("result"));
		System.out.println("elements====>" + elements);
		if(elements == null){
			System.out.println("nullnullnullnull");
		}
		String location = elements.get("location").toString();
		
		JSONObject coordinate = JSONObject.fromObject(location);
		String lat = coordinate.get("lat").toString();
		String lng = coordinate.get("lng").toString();
		
		String position = lat + "," + lng;

		return position;
	}

	public static List<String> getCoordinate() {
		JSONObject result = JSONObject.fromObject(coordinate());
		JSONObject elements = JSONObject.fromObject((JSONObject) result
				.get("result"));
		List<String> list = new ArrayList<String>();

		String address = elements.get("address").toString();

		list.add(address);// 地理位置

		return list;
	}

	public static String getDistance(String from, String to) {
		String range = "";
		JSONObject result = JSONObject.fromObject(distance(from, to));
		String status = result.get("status").toString();
		if (status.equals("0")) {
			JSONObject elements = JSONObject.fromObject((JSONObject) result
					.get("result"));
			List<String> list = new ArrayList<String>();
			JSONArray jsarr = JSONArray.fromObject(elements.get("elements")
					.toString());
			if (jsarr.size() > 0) {
				for (int i = 0; i < jsarr.size(); i++) {
					JSONObject content = (JSONObject) jsarr.get(i);
					String distance = content.get("distance").toString();
					list.add(distance);// 距离 单位米
				}
				range = list.get(0);
			}
		} else {
			range = result.get("message").toString();
		}
		return range;
	}
	
	//2018-10-15 @Tyy
	public static boolean getCard(String card) {
		boolean result = false;
		String url = "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardBinCheck=true&cardNo=";
		JSONObject jsonObject = sendGetUrl(url + card);
		String cardType = jsonObject.get("cardType").toString();
		if(!StringUtil.isEmpty(cardType) && !cardType.equals("null")){
			if(cardType.equals("DC")){
				result = true;
			}
		}
		return result;
		//DC	储蓄卡
		//CC	信用卡
		//SCC	准贷记卡
		//PC	预付费卡
	}

	public static void main(String[] args) throws IOException {
		String address = "广东省深圳市龙华区宝能科技园9栋C座6楼609L";
		System.out.println( HttpRequest.sendPost("http://restapi.amap.com/v3/geocode/geo","key=d64814d634b8469a9fef1e500892e7cf&output=XML&address=" + address));
		
	}
	
	//2018-10-29 @Tyy
	public static JSONObject coordinate(String location) {
		String params = "location=" + location + "&key=56YBZ-Q4XCJ-DRQF5-KHC62-WGREV-PJFA3";
		JSONObject jsonObject = sendGetUrl("http://apis.map.qq.com/ws/geocoder/v1/?"
				+ params);
		return jsonObject;
	}
	
	//2018-10-29 @Tyy
	public static String getDistrict(String location) {
		String district = null;
		JSONObject result = JSONObject.fromObject(coordinate(location));
		JSONObject elements = JSONObject.fromObject((JSONObject) result.get("result"));
		if(elements.equals(null)){
			if (elements.get("address_component").toString() != null) {
				JSONObject component = JSONObject.fromObject((JSONObject) elements.get("address_component"));
				district = component.get("district").toString();
			}
		}
		return district;
	}
}