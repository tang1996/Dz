package com.dz.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.dz.service.IStaffService;

public class Interceptor implements HandlerInterceptor {

	@Autowired
	private IStaffService staffService;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String token = request.getParameter("token");
		String userName = request.getParameter("userName");
		JSonMessage message = new JSonMessage();
		System.out.println("request.getRequestURI()====>"
				+ request.getRequestURI());
		
		if(request.getRequestURI().equals("/Dz/staff/login")){
			com.dz.entity.Staff staff = staffService.login(userName);
			if (staff == null) {
				message.addProperty("message", "用户不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return false;
			}

			if (staff.getPowerSortId().getId() != 1 && staff.getPowerSortId().getId() != 2) {
				message.addProperty("message", "没有对应操作权限");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return false;
			}
		}

		if (request.getRequestURI().equals("/Dz/relating/batchSave")
				|| request.getRequestURI().equals("/Dz/relating/doDelete")
				|| request.getRequestURI().equals("/Dz/relating/doSave")) {
			com.dz.entity.Staff staff = staffService.gettoken(token);
			if (staff == null) {
				message.addProperty("message", "token验证失败");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return false;
			}

			if (staff.getPowerSortId().getId() != 3) {
				message.addProperty("message", "没有对应操作权限");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return false;
			}
		}

		if (request.getRequestURI().equals("/Dz/relating/checkMoney")) {
			com.dz.entity.Staff staff = staffService.gettoken(token);
			if (staff == null) {
				message.addProperty("message", "token验证失败");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return false;
			}

			if (staff.getPowerSortId().getId() == 3) {
				message.addProperty("message", "没有对应操作权限");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return false;
			}
		}

		return true;
	}
	
	

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
