package com.dz.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	public boolean isDate(String theDate) {
		boolean isDate = true;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String tishi = "";
		try {
			format.setLenient(false);
			String[] dateTime = theDate.split(" ");
			String[] date = dateTime[0].split("-");
			if (date[0].length() != 4) {
				isDate = false;
				tishi = "年份请写全。";
			}
			if (date[1].length() != 2) {
				isDate = false;
				tishi = "月份请写全。";
			}
			if (date[2].length() != 2) {
				isDate = false;
				tishi = "日期请写全。";
			}
			String[] time = dateTime[1].split(":");
			if (time[0].length() != 2) {
				isDate = false;
				tishi = "小时请写全。";
			}
			if (time[1].length() != 2) {
				isDate = false;
				tishi = "分钟请写全。";
			}
			if (time[2].length() != 2) {
				isDate = false;
				tishi = "秒请写全。";
			}
		} catch (Exception e) {
			isDate = false;
		}
		return isDate;
	}

	public boolean isFirstDay(Date date) { // ynw
		SimpleDateFormat sf = new SimpleDateFormat("dd");
		if (sf.format(date).equals("01")) {
			return true;
		}
		return false;
	}

	public String lastMonthLastDay(Date date) { // 得到上个月的最后一天 YNW
		Calendar calendar = Calendar.getInstance(); // 实例化日历对象 0为1月
		calendar.setTime(date); // 设置参数时间
		if (calendar.get(Calendar.MONTH) == 0) { // 如果是1月份
			calendar.add(Calendar.YEAR, -1); // 年份-1
			calendar.set(Calendar.MONTH, 11); // 月份设置为12
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); // 根据每个月的不同来设置天数
		} else {
			calendar.add(Calendar.MONTH, -1); // 不是1月份，则直接月份-1
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); // 根据每个月的不同来设置天数
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd 23:59:59"); // 初始化时间格式对象(以
																			// 年-月-日
																			// 为格式)
		return sf.format(calendar.getTime()); // 返回时间字符串
	}

	public String lastMonthFirstDay(Date date) { // 得到上个月的第一天 YNW
		Calendar calendar = Calendar.getInstance(); // 实例化日历对象 0为1月
		calendar.setTime(date); // 设置参数时间
		if (calendar.get(Calendar.MONTH) == 0) { // 如果是1月份
			calendar.add(Calendar.YEAR, -1); // 年份-1
			calendar.set(Calendar.MONTH, 11); // 月份设置为12
		} else {
			calendar.add(Calendar.MONTH, -1); // 不是1月份，则直接月份-1
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-01 00:00:00"); // 初始化时间格式对象(以
																			// 年-月-01
																			// 为格式)
		return sf.format(calendar.getTime()); // 返回时间字符串
	}

	public static int compare_date(String DATE1, String DATE2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

}
