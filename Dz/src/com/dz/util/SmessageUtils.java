package com.dz.util;

import java.io.IOException;
import java.util.Random;

public class SmessageUtils {

	// 固定参数
	final private static String parameter = "userid=11286&account=dzkj&password=dzkj1234&action=send&sendTime=&extno=";
	final private static String url = "http://139.224.60.78:8888/smsGBK.aspx";
//	final private static String url = "http://118.190.149.109:8081/Test123/servlet/Recevice";

	public static void main(String[] args) {
		String mobile = "13737365150";
		try {
			String str = new SmessageUtils().getCode(mobile);
			System.out.println(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getCode(String mobile) throws IOException {		//发送验证码 dtbb
		Random random = new Random();
		int code = random.nextInt(999999) % (999999 - 100000) + 100000;
		String str = "【啄呗】您的短信验证码是："+ code +"。若非本人发送，请忽略此短信。";
		String param = parameter + "&mobile=" + mobile + "&content=" +  str;
		String result = HttpRe.sendPost(url, param);
		System.out.println(result);
		return String.valueOf(code);
	}
	
	//ynw 
	public boolean sendToRider(String mobile, String riderUserName, String riderPassword){		//骑手审核通过
		String str = "【啄呗】您申请的骑手资格已通过审核，用户名:"+ riderUserName +"  密码：123456  (请保管好账号和密码！)";
		String param = parameter + "&mobile=" + mobile + "&content=" +  str;
		HttpRe.sendPost(url, param);
		System.out.println(str);
		return true;
	}
	
	//ynw
	public String sendToSaler(String mobile, String salerCode, String name){		//业务员审核通过
		String str = "【啄呗】您申请的 "+ name +" 资格已通过审核，推广码为:"+ salerCode;
		String param = parameter + "&mobile=" + mobile + "&content=" +  str;
		HttpRe.sendPost(url, param);
		System.out.println(str);
		return salerCode;
	}
	
	//ynw
	public boolean sendToCompany(String mobile, String companyUserName, String companyPassword){	//商家审核通过
		String str = "【啄呗】您申请的店铺资格已通过审核，商家账号为:"+ companyUserName +"  密码: 123456  (商家版APP下载链接:Http://www.baidu.com ; 用户版APP下载链接:Http://www.baidu.com)";
		String param = parameter + "&mobile=" + mobile + "&content=" +  str;
		HttpRe.sendPost(url, param);
		System.out.println(str);
		return true;
	}
	
	public void sendToCompanyExamine(String mobile){	//商家入驻申请
		String str = "【啄呗】您的入驻申请已提交成功，请耐心等待审核。如需帮助请咨询客服热线:400-833-3520";
		String param = parameter + "&mobile=" + mobile + "&content=" +  str;
		HttpRe.sendPost(url, param);
	}
}
