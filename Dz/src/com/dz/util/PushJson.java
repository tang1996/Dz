package com.dz.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class PushJson {

	public void outString(String str, HttpServletResponse response) {
		try {
			response.setContentType("text/json;charset=UTF-8");
			response.setHeader("Access-Control-Allow-Origin", "*"); 
			PrintWriter out = response.getWriter();
			out.print(str);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void outString(JSONObject json, HttpServletResponse response) {
		try {
			response.setContentType("text/json; charset=UTF-8");
			response.setHeader("Access-Control-Allow-Origin", "*"); 
			PrintWriter out = response.getWriter();
			out.print(json);
			out.flush();
			out.close();
		} catch (IOException error) {
			error.printStackTrace();
		}
	}

	public void outString(Object json, HttpServletResponse response) {
		try {
			response.setContentType("text/json; charset=UTF-8");
			response.setHeader("Access-Control-Allow-Origin", "*"); 
			PrintWriter out = response.getWriter();
			out.print(json);
			out.flush();
			out.close();
		} catch (IOException error) {
			error.printStackTrace();
		}
	}
	
	public void upload(String json, HttpServletResponse response) {
		try {
			response.setContentType("text/json; charset=UTF-8");
			response.setHeader("Access-Control-Allow-Origin", "*"); 
			PrintWriter out = response.getWriter();
			out.print(json);
			out.flush();
			out.close();
		} catch (IOException error) {
			error.printStackTrace();
		}
	}
	
	//2018-10-29 @Tyy
	public void bastUpload(String json, HttpServletResponse response) {
		try {
			response.setContentType("text/html; charset=UTF-8");
			response.setHeader("Access-Control-Allow-Origin", "*"); 
			PrintWriter out = response.getWriter();
			out.print(json);
			out.flush();
			out.close();
		} catch (IOException error) {
			error.printStackTrace();
		}
	}
}
