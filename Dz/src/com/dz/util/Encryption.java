package com.dz.util;
public class Encryption {
	// 将字符串转换为二进制
	public static String toBinary(String str) {
		char[] strChar = str.toCharArray();
		String result = "";
		for (int i = 0; i < strChar.length; i++) {
			result += Integer.toBinaryString(strChar[i]) + " ";
		}
		return result;
	}

	// 将二进制字符串转换成int数组
	public static int[] BinstrToIntArray(String binStr) {
		char[] temp = binStr.toCharArray();
		int[] result = new int[temp.length];
		for (int i = 0; i < temp.length; i++) {
			result[i] = temp[i] - 48;
		}
		return result;
	}
	
	// 将二进制转换成char
	public static char BinstrToChar(String binStr) {
		int[] temp = BinstrToIntArray(binStr);
		int sum = 0;
		for (int i = 0; i < temp.length; i++) {
			sum += temp[temp.length - 1 - i] << i;
		}
		return (char) sum;
	}

	// 将char转化成字符串
	public static String BinstrToStr(String binary) {
		String[] tempStr = binary.split(" ");
		char[] tempChar = new char[tempStr.length];
		for (int i = 0; i < tempStr.length; i++) {
			tempChar[i] = BinstrToChar(tempStr[i]);
		}
		return String.valueOf(tempChar);
	}

	// 将密码转化成字符�?
	private static String tostring(String pwd) {
		String string = "";
		String tostring = "";
		for (int i = 0; i < pwd.length(); i++) {
			char num = pwd.charAt(i);
			String str = String.valueOf(num);
			if (str.equals("0")) {
				string = "Z-";
			}
			if (str.equals("1")) {
				string = "*";
			}
			if (str.equals("2")) {
				string = "LY";
			}
			if (str.equals("3")) {
				string = "#";
			}
			if (str.equals("4")) {
				string = "(";
			}
			if (str.equals("5")) {
				string = "==";
			}
			if (str.equals("6")) {
				string = ".";
			}
			if (str.equals("7")) {
				string = ")^";
			}
			if (str.equals("8")) {
				string = "E+";
			}
			if (str.equals("9")) {
				string = "~";
			}
			tostring = tostring + string;
		}
		return tostring;
	}

	// 将字符串转化成密�?
	private static String toint(String pwd) {
		String string = "";
		String toint = "";
		String cursor = "";
		for (int i = 0; i < pwd.length(); i++) {
			char num = pwd.charAt(i);
			String str = String.valueOf(num);
			if (str.equals("Z")) {
				cursor = str;
			}
			if (str.equals("-")) {
				if (cursor.equals("Z"))
					string = "0";
				toint = toint + string;
			}
			if (str.equals("*")) {
				string = "1";
				toint = toint + string;
			}
			if (str.equals("L")) {
				cursor = str;
			}
			if (str.equals("Y")) {
				if (cursor.equals("L"))
					string = "2";
				toint = toint + string;
			}
			if (str.equals("#")) {
				string = "3";
				toint = toint + string;
			}
			if (str.equals("(")) {
				string = "4";
				toint = toint + string;
			}
			if (str.equals("=")) {
				if (str.equals(cursor)) {
					cursor = "";
				} else {
					string = "5";
					toint = toint + string;
					cursor = "=";
				}
			}
			if (str.equals(".")) {
				string = "6";
				toint = toint + string;
			}
			if (str.equals(")")) {
				cursor = str;
			}
			if (str.equals("^")) {
				if (cursor.equals(")"))
					string = "7";
				toint = toint + string;
			}
			if (str.equals("E")) {
				cursor = str;
			}
			if (str.equals("+")) {
				if (cursor.equals("E"))
					string = "8";
				toint = toint + string;
			}
			if (str.equals("~")) {
				string = "9";
				toint = toint + string;
			}
		}
		return toint;
	}

	//加密
	public static String encr(String pwd,String key){
		String encr = "";
		String str = tostring(pwd);
		try {
			encr = AES.getInstance().encrypt(str,"utf-8","pan*07735590021!","yykj" + key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encr;
	}
	
	//解密
	public static String decr(String pwd,String key){
		String str = "";
		try {
			str = AES.getInstance().decrypt(pwd,"utf-8","pan*07735590021!","yykj" + key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String decr = toint(str);
		return decr;
	}
	
	
	public static void main(String[] args) throws Exception {
//		String password = "45478126266287042634";
//		String key = "4457728144577281";
//		String encr = encr(password,key);
//		System.out.println(encr);
//		String decr = decr("1VOGRNjN1czqrkDmufa1cAALmjg43OFA8kbk87v/1tk=","6662975966629759");
//		System.out.println(decr);
//		System.out.println(decr.equals(password));
		
	}

}
