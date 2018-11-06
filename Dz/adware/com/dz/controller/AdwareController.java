package com.dz.controller;

import java.io.File;
import java.io.IOException;
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
import com.dz.entity.Images;
import com.dz.service.IAdwareService;
import com.dz.service.IImagesService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/adware")
public class AdwareController {

	@Autowired
	private IAdwareService adwareService;
	
	@Autowired
	private IImagesService imagesService;

	// =====================手机端=====================//
	// 查询列表
	@RequestMapping(params = "show", method = RequestMethod.POST)
	public void show(HttpServletRequest request, HttpServletResponse response,
			Adware adware) {
		String type = request.getParameter("type");

		if (StringUtil.isEmpty(type)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "region不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Adware> adwareList = adwareService.getAdware(type);

		if (adwareList == null) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "该区域暂无广告");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
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
		grid.addProperties("totalCount", adwareList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 首页广告
	@RequestMapping(params = "indexShow", method = RequestMethod.POST)
	public void indexShow(HttpServletRequest request, HttpServletResponse response) {

		List<Adware> adwareList = adwareService.getAdware("index");

		if (adwareList == null) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "该区域暂无广告");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		for (Adware adwares : adwareList) {
			Images images = imagesService.getImage(adwares.getId()+"", "ADWARE");
			if(images == null){
				JSonMessage message = new JSonMessage();
				message.addProperty("message", "该广告暂未上传图片");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
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
			record.addColumn("img", images.getZoomUrl());

			grid.addRecord(record);
		}
		grid.addProperties("totalCount", adwareList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	
	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Adware adware,
			HttpServletResponse response) {
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
	public void saveORupdate(HttpServletRequest request, Adware adware,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		adwareService.saveORupdate(adware);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 上传
	@RequestMapping(params = "upload", method = RequestMethod.POST)
	public void upload(HttpServletRequest request, Adware adware,
			@RequestParam("file") MultipartFile file,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		// 如果文件为空，返回失败
		if (file.isEmpty()) {
			message.addProperty("message", "请选择要上传的文件");
			message.addProperty("success", false);
			return;
		}
		// 上传文件路径
		// String path = request.getServletContext().getRealPath("/images/");
		String path = "C:\\Users\\Administrator\\Desktop\\2";
		// 上传文件名
		String filename = file.getOriginalFilename();
		File filepath = new File(path, filename);

		// 判断路径是否存在，如果不存在就创建一个
		if (!filepath.getParentFile().exists()) {
			filepath.getParentFile().mkdirs();
		}
		// 将上传文件保存到一个目标文件当中
		try {
			file.transferTo(new File(path + File.separator + filename));
			message.addProperty("message", "上传成功");
			message.addProperty("success", true);
		} catch (IOException e) {
			message.addProperty("message", "上传失败");
			message.addProperty("success", false);
			e.printStackTrace();
		}
		new PushJson().upload(message.toJSonString(), response);
	}

}