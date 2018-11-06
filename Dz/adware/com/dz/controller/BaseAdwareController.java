package com.dz.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dz.entity.Adware;
import com.dz.service.IAdwareService;
import com.dz.util.DateUtils;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.MD5Util;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/base/adware")
public class BaseAdwareController {

	@Autowired
	private IAdwareService adwareService;

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Adware adware, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(adware.getId() + "") || adware.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
		} else {
			adwareService.delete(adware.getId() + "");
			message.addProperty("message", "删除成功");
			message.addProperty("success", true);
			new PushJson().outString(message.toJSonString(), response);
		}
	}
	
	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, Adware adware, HttpServletResponse response) {
		boolean resutl = false;
		JSonMessage message = new JSonMessage();
		if(!StringUtil.isEmpty(adware.getStartTime())){
			resutl = new DateUtils().isDate(adware.getStartTime());
			if(!resutl){
				message.addProperty("message", "开始时间格式不正确");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
		}
		if(!StringUtil.isEmpty(adware.getEndTime())){
			resutl = new DateUtils().isDate(adware.getEndTime());
			if(!resutl){
				message.addProperty("message", "结束时间格式不正确");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
		}
		
		adware.setType("index");
		adware.setClickRate("0");
		adware.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		adwareService.saveORupdate(adware);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 上传
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public void upload(HttpServletRequest request, @RequestParam("file") MultipartFile file,
			HttpServletResponse response) {
		String seat = request.getParameter("seat");
		String id = request.getParameter("id");
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(seat)) {
			message.addProperty("message", "广告位置不能为空");
			message.addProperty("success", false);
			new PushJson().bastUpload(message.toJSonString(), response);
			return;
		}
		// 如果文件为空，返回失败
		if (file.isEmpty()) {
			message.addProperty("message", "请选择要上传的文件");
			message.addProperty("success", false);
			new PushJson().bastUpload(message.toJSonString(), response);
			return;
		}

		// 上传文件路径
		String path = request.getSession().getServletContext().getRealPath("");

		path = path.substring(0, path.length() - 2) + "DzClient\\WebRoot\\common\\images\\";
		// 上传文件名
		String uploadName = file.getOriginalFilename();
		String prefix = uploadName.substring(uploadName.lastIndexOf("."));
		String filename = MD5Util.MD5(System.currentTimeMillis() + "") + prefix;

		// String newPath =
		// "http://39.108.6.102:8080/DzClient/WebRoot/common/images/" +
		// filename;
		// File filepath = new File(path, filename);
		String newPath = "C:\\Users\\Administrator\\Desktop\\2";
		File filepath = new File(newPath, filename);

		// 判断路径是否存在，如果不存在就创建一个
		if (!filepath.getParentFile().exists()) {
			filepath.getParentFile().mkdirs();
		}

		// System.out.println("path====>" + path);
		System.out.println("newPath====>" + newPath);

		Adware adware = adwareService.getAdware(Integer.valueOf(id));
		adware.setUrl(newPath);
		adware.setSource(newPath);
		// adware.setUrl(path);
		// adware.setSource(path);
		adware.setSeat(seat);

		// 将上传文件保存到一个目标文件当中
		try {
			// file.transferTo(new File(path + File.separator + filename));
			file.transferTo(new File(newPath + File.separator + filename));
			adwareService.saveORupdate(adware);
			message.addProperty("message", "上传成功");
			message.addProperty("success", true);
			System.out.println("上传成功");
			System.out.println(message.toJSonString());
			new PushJson().bastUpload(message.toJSonString(), response);
			return;
		} catch (IOException e) {
			message.addProperty("message", "上传失败");
			message.addProperty("success", false);
			System.out.println("上传失败");
			e.printStackTrace();
			new PushJson().bastUpload(message.toJSonString(), response);
			return;
		}

	}

	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response) {
		String start = request.getParameter("start");	/*ynw*/
		String limit = request.getParameter("limit");	/*ynw*/
		String id = request.getParameter("id");	/*ynw*/
		
		JSonMessage message = new JSonMessage();	/*ynw*/
		if (StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		Adware adware = new Adware();
		if(!StringUtil.isEmpty(id)){	/*ynw start*/
			adware.setId(Integer.valueOf(id));
		}	/*ynw end*/
		
		List<Adware> count = adwareService.getAdwareList(adware);	/*ynw*/
		List<Adware> adwareList = adwareService.getAdwareList(adware, Integer.valueOf(start), Integer.valueOf(limit));	/*ynw*/

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		for (Adware adwares : adwareList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", adwares.getId());
			record.addColumn("name", adwares.getName());
			record.addColumn("depict", adwares.getDepict());
			record.addColumn("type", adwares.getType());
			record.addColumn("region", adwares.getRegion());
			record.addColumn("source", adwares.getSource());
			record.addColumn("seat", adwares.getSeat());
			record.addColumn("click_rate", adwares.getClickRate());
			record.addColumn("url", adwares.getUrl());
			record.addColumn("create_time", adwares.getCreateTime());
			record.addColumn("start_time", adwares.getStartTime());
			record.addColumn("end_time", adwares.getEndTime());
			record.addColumn("contacts", adwares.getContacts());
			record.addColumn("e_mail", adwares.getEmail());
			record.addColumn("phone", adwares.getPhone());
			record.addColumn("is_close", adwares.getIsClose());

			grid.addRecord(record);
		}
		grid.addProperties("totalCount", count.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

}