package com.dz.controller;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.dz.entity.Company;
import com.dz.entity.CompanyScore;
import com.dz.entity.Distribution;
import com.dz.entity.Evaluate;
import com.dz.entity.Images;
import com.dz.entity.Label;
import com.dz.entity.Order;
import com.dz.entity.OrderType;
import com.dz.entity.Parcel;
import com.dz.entity.RunEvaluate;
import com.dz.entity.User;
import com.dz.service.ICompanyScoreService;
import com.dz.service.ICompanyService;
import com.dz.service.IDistributionService;
import com.dz.service.IEvaluateService;
import com.dz.service.ILabelService;
import com.dz.service.IOrderService;
import com.dz.service.IOrderTypeService;
import com.dz.service.IParcelService;
import com.dz.service.IRunEvaluateService;
import com.dz.service.IUserService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.MD5Util;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;
import com.dz.util.URL;

@Controller
@RequestMapping("/evaluate")
public class EvaluateController {

	@Autowired
	private IEvaluateService evaluateService;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private IUserService userService;
	
	@Autowired
	private IDistributionService distribution;
	
	@Autowired
	private IOrderService orderService;

	@Autowired
	private IOrderTypeService ordertypeService;

	@Autowired
	private ICompanyScoreService companyScoreService;

	@Autowired
	private IParcelService parcelService;

	@Autowired
	private ILabelService labelService;
	
	@Autowired
	private IRunEvaluateService runEvaluateService;
	
	private String upload(String prefix, String path, MultipartFile file){
		if(prefix.equals(".jpg")||prefix.equals(".png")||prefix.equals(".jpeg")||prefix.equals(".bmp")
				|| prefix.equals(".JPG") || prefix.equals(".PNG") || prefix.equals(".JPEG") || prefix.equals(".BMP")){//2018-10-13  @tyy
			String filename = MD5Util.MD5(System.currentTimeMillis()+"") + prefix;
			
			File filepath = new File(path, filename);
			// 判断路径是否存在，如果不存在就创建一个
			if (!filepath.getParentFile().exists()) {
				filepath.getParentFile().mkdirs();
			}
			
			String newPath = URL.FIND_URL + filename;

			// 将上传文件保存到一个目标文件当中
			try {
				file.transferTo(new File(path + File.separator + filename));
				return newPath; 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	// 商家发表评价
	@RequestMapping(params = "sendEvaluate", method = RequestMethod.POST)
	public void sendEvaluate(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("file1") MultipartFile file1, 
			@RequestParam("file2") MultipartFile file2, @RequestParam("file3") MultipartFile file3) {
	
		String token = request.getParameter("token");//用户token
		String companyId = request.getParameter("cid");//商家ID
		
		String orderId = request.getParameter("orderId");//订单ID
		
		// 商家评分
		String taste = request.getParameter("taste");// 菜品口味
		String quality = request.getParameter("quality");// 服务质量
		// 配送员评分
		String speen = request.getParameter("speen");// 送餐速度
		String manner = request.getParameter("manner");// 服务态度

		String content = request.getParameter("content");// 评价内容
		
		JSonMessage message = new JSonMessage();
		
		if (StringUtil.isEmpty(token)||StringUtil.isEmpty(companyId)||StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "token不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		User user = userService.gettoken(token);
		
		if (user == null) {
			message.addProperty("message", "token验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		Order order = orderService.getOrder(Integer.valueOf(orderId));
		
		if (order == null) {
			message.addProperty("message", "order订单不存在无法评价.");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		Evaluate evaluate = new Evaluate();
		evaluate.setContent(content);
		evaluate.setUserId(user);
		evaluate.setOrderId(order);
		evaluate.setIsReply(false);
		evaluate.setType("COMPANY");
		evaluate.setCreateTime(new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
		evaluate.setIsDisplay(true);
		evaluate.setCustomId(Integer.valueOf(companyId));
		
		
		// 上传文件路径
		String path = request.getSession().getServletContext().getRealPath("");
		path = path.substring(0,path.indexOf("Dz\\")) + "DzClient\\common\\images\\";
		
		List<Images> imgList = new ArrayList<Images>();
		
		if (!file1.isEmpty()) {
			// 上传文件名
			String name = file1.getOriginalFilename();
			String prefix = name.substring(name.lastIndexOf("."));
			
			String newPath = upload(prefix, path, file1);
			Images images = new Images();
			images.setCustomId(companyId);
			images.setInfo("evaluate");
			images.setOriginalUrl(newPath);
			images.setScene("EVALUATE");
			images.setZoomUrl(newPath);
			imgList.add(images);
		}
		
		if (!file2.isEmpty()) {
			// 上传文件名
			String name = file2.getOriginalFilename();
			String prefix = name.substring(name.lastIndexOf("."));
			
			String newPath = upload(prefix, path, file2);
			
			Images images = new Images();
			images.setCustomId(companyId);
			images.setInfo("evaluate");
			images.setOriginalUrl(newPath);
			images.setScene("EVALUATE");
			images.setZoomUrl(newPath);
			imgList.add(images);
		}
		
		if (!file3.isEmpty()) {
			// 上传文件名
			String name = file3.getOriginalFilename();
			String prefix = name.substring(name.lastIndexOf("."));
			
			String newPath = upload(prefix, path, file3);
			
			Images images = new Images();
			images.setCustomId(companyId);
			images.setInfo("evaluate");
			images.setOriginalUrl(newPath);
			images.setScene("EVALUATE");
			images.setZoomUrl(newPath);
			imgList.add(images);
		}

		evaluate.setImg(imgList);
		evaluateService.saveORupdate(evaluate);
		
		CompanyScore companyScore = new CompanyScore();
		companyScore.setQuality(quality);
		companyScore.setTaste(taste);
		companyScore.setEvaluateId(evaluate);
		companyScoreService.saveORupdate(companyScore);
		
		order.setIsSend("Y");
		orderService.saveORupdate(order);

		OrderType type = ordertypeService.getOrderType(Integer.valueOf(orderId));
		
		if(!StringUtil.isEmpty(manner) && !StringUtil.isEmpty(speen) && type != null){
			RunEvaluate runEvaluate = new RunEvaluate();
			runEvaluate.setManner(manner);
			runEvaluate.setSpeen(speen);
			runEvaluate.setUserId(user.getId());
			runEvaluate.setOrderId(order);
			if (type.getUserId() != null) {
				runEvaluate.setRunId(type.getUserId().getId());
			}
			runEvaluateService.saveORupdate(runEvaluate);
		}
		
		message.addProperty("message", "发表成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);

	}
	
	

	// ========================手机端=========================== //
	// 查询列表
	@RequestMapping(params = "userview", method = RequestMethod.POST)
	public void userview(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		// token = "cOZ6cjmF9NF";

		if (StringUtil.isEmpty(token)||StringUtil.isEmpty(limit)||StringUtil.isEmpty(start)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "token不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);

		if (user == null) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "token验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Evaluate> evaluateList = evaluateService
				.userevaluate(user.getId(), Integer.valueOf(start), Integer.valueOf(limit));

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (Evaluate evaluates : evaluateList) {
			JSonGridRecord record = new JSonGridRecord();
			Company company = companyService.getCompany(evaluates.getCustomId());
			if(company != null){
				record.addColumn("companyName", company.getName());
				record.addColumn("logo", company.getLogo());
				record.addColumn("classfy", company.getClassifyId());
				
				Distribution dis = distribution.getDistribution(company.getId());
				if(dis != null){
					record.addColumn("miniPrice", dis.getMiniPrice());
					record.addColumn("GDP", dis.getGDP());
					record.addColumn("distributionPrice", dis.getDistributionPrice());
				}
			}
			
			CompanyScore companyScore = companyScoreService.getCompanyScore(Integer.valueOf(evaluates.getId()));
			if (companyScore != null) {
				double tab = (Double.valueOf(companyScore.getTaste()) + Double.valueOf(companyScore.getQuality()))/ 2;
				record.addColumn("quality", tab);
			}
			
			record.addColumn("id", evaluates.getId());
			record.addColumn("content", evaluates.getContent());
			record.addColumn("reply", evaluates.getReply());
			record.addColumn("createTime", evaluates.getCreateTime());
			record.addColumn("updateTime", evaluates.getUpdateTime());
			record.addColumn("label", evaluates.getLabel());
			record.addColumn("isDisplay", evaluates.getIsDisplay());

			grid.addRecord(record);
		}

		grid.addProperties("totalCount", evaluateList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}
	
	// 商家评价
	@RequestMapping(params = "companyIsreply", method = RequestMethod.POST)
	public void companyIsreply(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		String content = request.getParameter("content");
		
		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(content)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "id或content不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		Evaluate evaluate = evaluateService.get(Integer.valueOf(id));
		
		evaluate.setIsReply(true);
		evaluate.setReply(content);
		evaluateService.saveORupdate(evaluate);
		
		JSonMessage message = new JSonMessage();
		message.addProperty("message", "发表成功.");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 商家评价
	@RequestMapping(params = "companyview", method = RequestMethod.POST)
	public void companyview(HttpServletRequest request,
			HttpServletResponse response) {
		String customId = request.getParameter("customId");
		String isReply = request.getParameter("isReply");
		
		if (StringUtil.isEmpty(customId)) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "customId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Evaluate> evaluateList = new ArrayList<Evaluate>();
		if (!StringUtil.isEmpty(isReply)) {
			if(isReply.equals("1")){
				evaluateList = evaluateService.getIsReply("COMPANY", Integer.valueOf(customId));
			}else{
				evaluateList = evaluateService.getevaluate("COMPANY", Integer.valueOf(customId));
			}
		} else {
			evaluateList = evaluateService.getevaluate("COMPANY", Integer.valueOf(customId),  0, 1000);
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		int count = 0;
		for (Evaluate evaluates : evaluateList) {
			JSonGridRecord record = new JSonGridRecord();
			CompanyScore companyScore = companyScoreService
					.getCompanyScore(evaluates.getId());
			Company company = companyService.getCompany(Integer
					.valueOf(customId));
			if (companyScore != null && company != null) {
				record.addColumn("taste", companyScore.getTaste());
				record.addColumn("quality", companyScore.getQuality());
				record.addColumn("companyName", company.getName());
				record.addColumn("logo", company.getLogo());
			}
			record.addColumn("id", evaluates.getId());
			record.addColumn("nickName", evaluates.getUserId().getUserName());
			record.addColumn("imgurl", evaluates.getUserId().getImgUrl());
			record.addColumn("content", evaluates.getContent());
			record.addColumn("title", evaluates.getTitle());
			record.addColumn("reply", evaluates.getReply());
			record.addColumn("createTime", evaluates.getCreateTime());
			record.addColumn("updateTime", evaluates.getUpdateTime());
			List<Images> list = evaluates.getImg();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) != null) {
					record.addColumn("images" + i, list.get(i).getZoomUrl());
				}
			}
			record.addColumn("imgtotal", list.size());
			count = list.size() == 0 ? count : (count = count + 1);
			StringBuilder label = new StringBuilder();
			List<Label> labellist = labelService.getLabel("evaluates",
					evaluates.getId() + "");
			for (Label labels : labellist) {
				label.append("<span>" + labels.getContent() + "</span>");
			}
			record.addColumn("label", label.toString());
			// record.addColumn("orderId", evaluates.getOrderId());
			record.addColumn("isDisplay", evaluates.getIsDisplay());

			grid.addRecord(record);
		}
		grid.addProperties("imgcount", count);
		grid.addProperties("totalCount", evaluateList.size());

		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 商家评价统计
	@RequestMapping(params = "companyCount", method = RequestMethod.POST)
	public void companyCount(HttpServletRequest request,
			HttpServletResponse response) {
		String customId = request.getParameter("customId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(customId) || customId.equals("null")) {
			message.addProperty("message", "customId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		// 所有评价
		List<Evaluate> total = evaluateService.getevaluate("COMPANY", Integer
				.valueOf(customId));
		// 好评
		List<Evaluate> praise = evaluateService.getTypeClass("COMPANY", Integer
				.valueOf(customId));
		// 差评
		List<Evaluate> negative = evaluateService.getTypeClass("COMPANY",
				Integer.valueOf(customId));

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		int imgCount = 0;
		int count = 0;
		double quality = 0;
		double taste = 0;
		for (Evaluate evaluates : total) {
			CompanyScore companyScore = companyScoreService
					.getCompanyScore(evaluates.getId());
			quality = quality + Double.valueOf(companyScore.getQuality());
			taste = taste + Double.valueOf(companyScore.getTaste());
			List<Images> list = evaluates.getImg();
			if (list.size() > 0) {
				imgCount++;
			}
			count++;
		}
		String newQuality = "0";
		String newTaste = "0";
		DecimalFormat dFormat = new DecimalFormat("#.0");
		if (count != 0) {
			newQuality = dFormat.format(quality / count);
			newTaste = dFormat.format(taste / count);
		}
		
		String totalCount = dFormat.format((Double.valueOf(newQuality) + Double
				.valueOf(newTaste)) / 2);
		if (quality == 0) {
			message.addProperty("quality", 5);// 服务质量评分
		} else {
			message.addProperty("quality", newQuality);// 服务质量评分
		}
		if (taste == 0) {
			message.addProperty("taste", 5);// 口味评分
		} else {
			message.addProperty("taste", newTaste);// 口味评分
		}
		if (quality == 0 && taste == 0) {
			message.addProperty("totalCount", 5);// 综合评分
		} else {
			message.addProperty("totalCount", totalCount);// 综合评分
		}
		message.addProperty("imgCount", imgCount);// 有图片的评价数量
		message.addProperty("total", total.size());// 评价总数
		message.addProperty("praiseCount", praise.size());// 好评数
		message.addProperty("negativeCount", negative.size());// 差评数
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);

	}
	

	// 商家评价分类
	@RequestMapping(params = "evaluateType", method = RequestMethod.POST)
	public void evaluateType(HttpServletRequest request,
			HttpServletResponse response) {
		String customId = request.getParameter("customId");
		String status = request.getParameter("status");// total
		// 所有评价，praise好评，negative差评，img有图
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(customId) || customId.equals("null")
				|| StringUtil.isEmpty(status) || StringUtil.isEmpty(start)
				|| StringUtil.isEmpty(limit)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Evaluate> evaluateList = new ArrayList<Evaluate>();
		List<Evaluate> totalCount = new ArrayList<Evaluate>();
		if (status.equals("total") || status.equals("img")) {// 全部评价
			evaluateList = evaluateService.getevaluate("COMPANY", Integer
					.valueOf(customId), Integer.valueOf(start), Integer
					.valueOf(limit));
			totalCount = evaluateService.getevaluate("COMPANY", Integer
					.valueOf(customId));
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		int count = 0;
		int imgCount = 0;
		for (Evaluate evaluates : evaluateList) {
			JSonGridRecord record = new JSonGridRecord();
			CompanyScore companyScore = companyScoreService
					.getCompanyScore(evaluates.getId());
			Company company = companyService.getCompany(Integer
					.valueOf(customId));
			if (companyScore != null && company != null) {
				record.addColumn("taste", companyScore.getTaste());
				record.addColumn("quality", companyScore.getQuality());
				record.addColumn("companyName", company.getName());
				record.addColumn("logo", company.getLogo());
			}
			record.addColumn("id", evaluates.getId());
			record.addColumn("nickName", evaluates.getUserId().getUserName());
			record.addColumn("imgurl", evaluates.getUserId().getImgUrl());
			record.addColumn("content", evaluates.getContent());
			record.addColumn("title", evaluates.getTitle());
			record.addColumn("reply", evaluates.getReply());
			record.addColumn("createTime", evaluates.getCreateTime());
			record.addColumn("updateTime", evaluates.getUpdateTime());
			List<Images> list = evaluates.getImg();
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i) != null) {
						record
								.addColumn("images" + i, list.get(i)
										.getZoomUrl());
					}
				}
				imgCount++;
			} else {
				if (status.equals("img")) {
					continue;
				}
			}

			record.addColumn("imgtotal", list.size());
			count = list.size() == 0 ? count : (count = count + 1);
			StringBuilder label = new StringBuilder();
			List<Label> labellist = labelService.getLabel("evaluates",
					evaluates.getId() + "");
			for (Label labels : labellist) {
				label.append("<span>" + labels.getContent() + "</span>");
			}
			record.addColumn("label", label.toString());
			// record.addColumn("orderId", evaluates.getOrderId());
			record.addColumn("isDisplay", evaluates.getIsDisplay());

			grid.addRecord(record);
		}
		grid.addProperties("imgcount", count);
		if (status.equals("img")) {
			grid.addProperties("totalCount", imgCount);
		} else {
			grid.addProperties("totalCount", totalCount.size());
		}
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 商品评价
	@RequestMapping(params = "goodsview", method = RequestMethod.POST)
	public void goodsview(HttpServletRequest request,
			HttpServletResponse response, Evaluate evaluate) {

		if (StringUtil.isEmpty(evaluate.getCustomId() + "")) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "自定义id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		List<Evaluate> evaluateList = evaluateService.getevaluate("parcel",
				evaluate.getCustomId());

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (Evaluate evaluates : evaluateList) {
			JSonGridRecord record = new JSonGridRecord();
			Parcel parcel = parcelService.getparcel(Integer.valueOf(evaluate
					.getCustomId()));
			if (parcel != null) {
				record.addColumn("speen", parcel.getSpeen());
				record.addColumn("taste", parcel.getTaste());
				record.addColumn("goodsName", parcel.getGoodsId().getName());
				record.addColumn("goodsImg", parcel.getGoodsId().getZoomUrl());
				record.addColumn("companyName", parcel.getGoodsId()
						.getCompanyId().getName());
			}
			record.addColumn("id", evaluates.getId());
			record.addColumn("nickName", evaluates.getUserId().getNickname());
			record.addColumn("userImg", evaluates.getUserId().getImgUrl());
			record.addColumn("content", evaluates.getContent());
			record.addColumn("reply", evaluates.getReply());
			record.addColumn("createTime", evaluates.getCreateTime());
			record.addColumn("updateTime", evaluates.getUpdateTime());
			record.addColumn("label", evaluates.getLabel());
			record.addColumn("orderId", evaluates.getOrderId());
			record.addColumn("img", evaluates.getImg());
			record.addColumn("isDisplay", evaluates.getIsDisplay());

			grid.addRecord(record);
		}

		grid.addProperties("totalCount", evaluateList.size());
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		
		String id = request.getParameter("id");
		
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		Evaluate evaluate = evaluateService.get(Integer.valueOf(id));
		
		CompanyScore soScore = companyScoreService.getCompanyScore(evaluate.getId());
		
		companyScoreService.delete(soScore.getId()+"");
		
		evaluateService.delete(evaluate);
		
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, Evaluate evaluate,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		evaluateService.saveORupdate(evaluate);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}