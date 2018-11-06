package com.dz.util;

import java.math.BigDecimal;
import java.util.Scanner;
//距离计算
public class MapUtil {
	 private static double EARTH_RADIUS = 6371.393;

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 计算两个经纬度之间的距离
	 * 
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return
	 */

	private static double GetDistance(double lat1, double lng1, double lat2,
			double lng2) {

		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 1000);
		return s;
	}

	public static int getdistance(String from, String to) {
		String coordinate = from + "," + to;
		Scanner scan = new Scanner(coordinate);
		String str = scan.nextLine();

		String date[] = str.split(",");
		double result = MapUtil.GetDistance(Double.parseDouble(date[0]), Double
				.parseDouble(date[1]), Double.parseDouble(date[2]), Double
				.parseDouble(date[3]));
		BigDecimal bd = new BigDecimal(result).setScale(0,
				BigDecimal.ROUND_HALF_UP);
		int distance = Integer.valueOf(bd.toString());
		return distance;
	}

	// public static void main(String[] args) {
	// String from = "29.490295,106.486654";
	// String to = "29.615467,106.581515";
	// getdistance(from, to);
	// }
}