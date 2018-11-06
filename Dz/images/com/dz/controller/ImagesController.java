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

import com.dz.entity.Company;
import com.dz.entity.Goods;
import com.dz.entity.Images;
import com.dz.service.ICompanyService;
import com.dz.service.IGoodsService;
import com.dz.service.IImagesService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.MD5Util;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;
import com.dz.util.URL;

@Controller
@RequestMapping("/images")
public class ImagesController {

	@Autowired
	private IImagesService imagesService;

	@Autowired
	private IGoodsService goodsService;

	@Autowired
	private ICompanyService companyService;

	// =========================手机端==========================//
	// 查询列表
	@RequestMapping(params = "getview", method = RequestMethod.POST)
	public void getview(HttpServletRequest request, HttpServletResponse response, Images images) {
		String customId = request.getParameter("customId");
		String scene = request.getParameter("scene");

		if (StringUtil.isEmpty(scene) || StringUtil.isEmpty(customId)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "scene或customId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<Images> imagesList = imagesService.imagesList(images);

		for (Images imagess : imagesList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", imagess.getId());
			record.addColumn("custom_id", imagess.getCustomId());
			record.addColumn("scene", imagess.getScene());
			record.addColumn("original_url", imagess.getOriginalUrl());
			record.addColumn("zoom_url", imagess.getZoomUrl());
			record.addColumn("info", imagess.getInfo());

			grid.addRecord(record);
		}
		grid.addProperties("totalCount", imagesList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response, Images images) {

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<Images> imagesList = imagesService.imagesList(images);

		for (Images imagess : imagesList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", imagess.getId());
			record.addColumn("custom_id", imagess.getCustomId());
			record.addColumn("scene", imagess.getScene());
			record.addColumn("original_url", imagess.getOriginalUrl());
			record.addColumn("zoom_url", imagess.getZoomUrl());
			record.addColumn("info", imagess.getInfo());

			grid.addRecord(record);
		}
		grid.addProperties("totalCount", imagesList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Images images, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(images.getId() + "") || images.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		imagesService.delete(images.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, Images images, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		imagesService.saveORupdate(images);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 上传店铺头像
	@RequestMapping(params = "logoUpload", method = RequestMethod.POST)
	public void logoUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file,
			HttpServletResponse response) {

		String companyId = request.getParameter("companyId");
		String type = request.getParameter("type");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(type)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		// 如果文件为空，返回失败
		if (file.isEmpty()) {
			message.addProperty("message", "请选择要上传的文件");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		// 上传文件路径
		String path = request.getSession().getServletContext().getRealPath("");
		path = path.substring(0, path.indexOf("Dz\\")) + "DzClient\\common\\images\\";

		// 上传文件名
		String name = file.getOriginalFilename();
		String prefix = name.substring(name.lastIndexOf("."));

		if (prefix.equals(".jpg") || prefix.equals(".png") || prefix.equals(".jpeg") || prefix.equals(".bmp")
				|| prefix.equals(".PNG") || prefix.equals(".JPEG") || prefix.equals(".BMP") || prefix.equals(".JPG")) {

			String filename = MD5Util.MD5(System.currentTimeMillis() + "") + prefix;

			File filepath = new File(path, filename);
			// 判断路径是否存在，如果不存在就创建一个
			if (!filepath.getParentFile().exists()) {
				filepath.getParentFile().mkdirs();
			}

			String newPath = URL.FIND_URL + filename;
			Company company = companyService.getCompany(Integer.valueOf(companyId));
			if (company == null) {
				message.addProperty("message", "商家不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			if (type.equals("logo")) {
				company.setLogo(newPath);
			}
			if (type.equals("honor")) {
				company.setHonor(newPath);
			}
			if (type.equals("img")) {
				company.setImg(newPath);
			}
			// 将上传文件保存到一个目标文件当中
			try {
				file.transferTo(new File(path + File.separator + filename));
				companyService.saveORupdate(company);
			} catch (IOException e) {
				message.addProperty("message", "上传失败");
				message.addProperty("success", false);
				e.printStackTrace();
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			message.addProperty("message", "上传成功");
			message.addProperty("success", true);
			new PushJson().outString(message.toJSonString(), response);
		} else {
			message.addProperty("message", "目前只支持jpg,png,jpeg,bmp 几种格式且不超过1M的图片.");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
		}

	}

	// 上传商品图片
	@RequestMapping(params = "upload", method = RequestMethod.POST)
	public void upload(HttpServletRequest request, @RequestParam("file") MultipartFile file,
			HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String customId = request.getParameter("customId");
		String type = request.getParameter("type");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(customId) || StringUtil.isEmpty(type)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			return;
		}

		// 如果文件为空，返回失败
		if (file.isEmpty()) {
			message.addProperty("message", "请选择要上传的文件");
			message.addProperty("success", false);
			return;
		}
		// 上传文件路径
		String path = request.getSession().getServletContext().getRealPath("");

		path = path.substring(0, path.indexOf("Dz\\")) + "DzClient\\common\\images\\";
		// 上传文件名
		String name = file.getOriginalFilename();
		String prefix = name.substring(name.lastIndexOf("."));
		String filename = MD5Util.MD5(System.currentTimeMillis() + "") + prefix;
		File filepath = new File(path, filename);

		// 判断路径是否存在，如果不存在就创建一个
		if (!filepath.getParentFile().exists()) {
			filepath.getParentFile().mkdirs();
		}

		String newPath = URL.FIND_URL + filename;

		Goods goods = goodsService.getGoods(Integer.valueOf(customId));
		if (goods == null) {
			message.addProperty("message", "商品不存在");
			message.addProperty("success", false);
			new PushJson().upload(message.toJSonString(), response);
			return;
		}
		goods.setZoomUrl(newPath);
		goods.setOriginalUrl(newPath);

		// 将上传文件保存到一个目标文件当中
		try {
			file.transferTo(new File(path + File.separator + filename));
			goodsService.saveORupdate(goods);
			message.addProperty("message", "上传成功");
			message.addProperty("success", true);
			new PushJson().upload(message.toJSonString(), response);
			return;
		} catch (IOException e) {
			message.addProperty("message", "上传失败");
			message.addProperty("success", false);
			e.printStackTrace();
			new PushJson().upload(message.toJSonString(), response);
			return;
		}
	}

}