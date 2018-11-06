package com.dz.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class ExceptionHandler implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception ex) {

		ModelAndView mv = new ModelAndView();
		// 判断异常类型，来跳转不同页面
		if (ex instanceof MaxUploadSizeExceededException) {
			// 指定错误信息
			mv.addObject("message", "上传文件过大");
			mv.addObject("success", false);
			
			// 设置跳转视图
			mv.setViewName("upload");
			return mv;
		}
		
		// 其他异常
		return null;
	}

}
