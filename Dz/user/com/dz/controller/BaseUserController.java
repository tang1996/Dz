package com.dz.controller;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.RiderInfo;
import com.dz.entity.Shop;
import com.dz.entity.User;
import com.dz.entity.Wallet;
import com.dz.service.IOrderService;
import com.dz.service.IOrderTypeService;
import com.dz.service.IRiderInfoService;
import com.dz.service.IRunEvaluateService;
import com.dz.service.IShopService;
import com.dz.service.IUserService;
import com.dz.service.IWalletService;
import com.dz.util.ExcelUtils;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.MD5Util;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;
import com.dz.util.TokenUtil;

@Controller
@RequestMapping("/base/user")
public class BaseUserController {

	@Autowired
	private IUserService userService;

	@Autowired
	private IShopService shopService;

	@Autowired
	private IWalletService walletService;

	@Autowired
	private IOrderService orderService;

	@Autowired
	private IRiderInfoService riderService;

	@Autowired
	private IOrderTypeService orderTypeService;

	@Autowired
	private IRunEvaluateService runEvaluateService;

	private static String sign(StringBuilder builder) {
		return MD5Util.MD5(builder.toString());
	}

	// 活跃用户列表 ynw
	@RequestMapping(params = "avtiveUser", method = RequestMethod.POST)
	public void avtiveUser(HttpServletRequest request, HttpServletResponse response) {
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime)) {
			startTime = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(new Date());
			endTime = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(new Date());
		} else {
			try {
				Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startTime);
				Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endTime);

				startTime = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(startDate);
				endTime = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		List<Object[]> count = orderService.activeUserOrder(startTime, endTime);	/*ynw*/

		List<Object[]> acticeOrderList = orderService.activeUserOrder(startTime, endTime, Integer.valueOf(start),
				Integer.valueOf(limit));

		JSonGrid grid = new JSonGrid();
		for (Object[] obj : acticeOrderList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("orderCount", obj[0]);
			//record.addColumn("payCount", obj[1].toString().substring(0, obj[1].toString().indexOf(".") + 3)); // 截取至小数点后两位
			record.addColumn("payCount",new DecimalFormat("#0.00").format(Double.valueOf(obj[1].toString()))); // 截取至小数点后两位
			record.addColumn("username", obj[2]);
			record.addColumn("name", obj[3]);
			record.addColumn("nickname", obj[4]);
			record.addColumn("phone", obj[5]);
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", count.size());	/*ynw*/
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 用户列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response) {
		String userName = request.getParameter("userName");
		
		/* ynw start xm */
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		JSonMessage message = new JSonMessage();
		if(StringUtil.isEmpty(start) || StringUtil.isEmpty(limit)){
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		/* ynw end xm */

		User user = new User();
		if (!StringUtil.isEmpty(userName)) {
			user.setUserName(userName);
		}

		List<User> count = userService.userList(user);	/*ynw xm*/
		List<User> userList = userService.userList(user, Integer.valueOf(start), Integer.valueOf(limit));		/*ynw start*/
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (User users : userList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", users.getId());
			record.addColumn("user_name", users.getUserName());
			record.addColumn("password", users.getPassword());
			record.addColumn("random_code", users.getRandomCode());
			record.addColumn("img_url", users.getImgUrl());
			record.addColumn("name", users.getName());
			record.addColumn("nickname", users.getNickname());
			record.addColumn("token", users.getToken());
			record.addColumn("phone", users.getPhone());
			record.addColumn("card_front", users.getCardFront());
			record.addColumn("card_back", users.getCardBack());
			record.addColumn("create_time", users.getCreateTime());
			record.addColumn("update_time", users.getUpdateTime());
			record.addColumn("auditor", users.getAuditor());
			record.addColumn("status", users.getStatus());
			record.addColumn("age", users.getAge());
			record.addColumn("company", users.getCompany());
			record.addColumn("position", users.getPosition());
			record.addColumn("hobby", users.getHobby());
			record.addColumn("sign", users.getSign());
			record.addColumn("career", users.getCareer());
			record.addColumn("is_distribution", users.getIsDistribution());
			record.addColumn("is_newuser", users.getIsNewuser());

			grid.addRecord(record);
		}
		grid.addProperties("totalCount", count.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 用户列表
	@RequestMapping(params = "riderView", method = RequestMethod.POST)
	public void riderView(HttpServletRequest request, HttpServletResponse response) {
		String userName = request.getParameter("userName");

		User user = new User();
		user.setIsDistribution(true);
		if (!StringUtil.isEmpty(userName)) {
			user.setUserName(userName);
		}
		List<User> userList = userService.userList(user);
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (User users : userList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", users.getId());
			record.addColumn("user_name", users.getUserName());
			record.addColumn("password", users.getPassword());
			record.addColumn("random_code", users.getRandomCode());
			record.addColumn("img_url", users.getImgUrl());
			record.addColumn("name", users.getName());
			record.addColumn("nickname", users.getNickname());
			record.addColumn("token", users.getToken());
			record.addColumn("phone", users.getPhone());
			record.addColumn("card_front", users.getCardFront());
			record.addColumn("card_back", users.getCardBack());
			record.addColumn("create_time", users.getCreateTime());
			record.addColumn("update_time", users.getUpdateTime());
			record.addColumn("auditor", users.getAuditor());
			record.addColumn("status", users.getStatus());
			record.addColumn("age", users.getAge());
			record.addColumn("company", users.getCompany());
			record.addColumn("position", users.getPosition());
			record.addColumn("hobby", users.getHobby());
			record.addColumn("sign", users.getSign());
			record.addColumn("career", users.getCareer());
			record.addColumn("is_distribution", users.getIsDistribution());
			record.addColumn("is_newuser", users.getIsNewuser());

			grid.addRecord(record);
		}
		grid.addProperties("totalCount", userList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除用户
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, User user, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(user.getId() + "") || user.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		userService.delete(user.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 删除骑手
	@RequestMapping(params = "riderDelete", method = RequestMethod.POST)
	public void riderDelete(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.getid(Integer.valueOf(id));
		if (user == null) {
			message.addProperty("message", "用户不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		user.setIsDistribution(false);
		userService.saveORupdate(user);

		message.addProperty("message", "骑手删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加用户
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, User user, HttpServletResponse response) {
		int id = user.getId();
		JSonMessage message = new JSonMessage();
		String name = user.getName();
		String username = user.getUserName();
		String password = "";
		String key = "";
		String random_numb = TokenUtil.getRandomChar(8);

		StringBuilder builder = new StringBuilder();
		if (id == 0) {
			List<User> user_name = userService.getuserName(user.getUserName());
			if (user_name.size() > 0) {
				message.addProperty("message", "该账号已存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			builder.append(random_numb + user.getPassword());
			String localSign = sign(builder);
			password = localSign;
			key = random_numb;
			User users = new User();
			users.setUserName(user.getUserName());
			users.setPassword(localSign);
			users.setRandomCode(random_numb);
			users.setImgUrl(user.getImgUrl());
			users.setName(user.getName());
			users.setNickname(user.getNickname());
			users.setToken(user.getToken());
			users.setPhone(user.getPhone());
			users.setCardFront(user.getCardFront());
			users.setCardBack(user.getCardBack());
			users.setCreateTime(user.getCreateTime());
			users.setUpdateTime(user.getUpdateTime());
			users.setAuditor(user.getAuditor());
			users.setStatus(user.getStatus());
			users.setAge(user.getAge());
			users.setCompany(user.getCompany());
			users.setPosition(user.getPosition());
			users.setHobby(user.getHobby());
			users.setSign(user.getSign());
			users.setCareer(user.getCareer());
			users.setIsDistribution(user.getIsDistribution());
			users.setIsNewuser(user.getIsNewuser());
			users.setBossId(0);
			userService.saveORupdate(users);

			Shop shop = new Shop();
			shop.setUserId(users);
			shopService.saveORupdate(shop);

			Wallet wallet = new Wallet();
			wallet.setBalance("0");
			wallet.setErrorTimes("0");
			wallet.setFreeTimes("0");
			wallet.setLockingTime("0");
			wallet.setStatus("1");
			wallet.setRedBalance("0");
			wallet.setUserId(users);
			walletService.saveORupdate(wallet);
			users.setShop(shop);
			userService.saveORupdate(users);
		} else {
			User usern = userService.getid(Integer.valueOf(id));
			if (!usern.getUserName().equals(user.getUserName())) {
				message.addProperty("message", "不能修改用户账号");
				message.addProperty("success", true);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			if (builder.append(usern.getRandomCode() + user.getPassword()).equals(usern.getPassword())) {
				user.setPassword(usern.getPassword());
				password = usern.getPassword();
				key = usern.getRandomCode();
			} else {
				builder.append(usern.getRandomCode() + user.getPassword());
				String localSign = sign(builder);
				usern.setPassword(localSign);
				password = localSign;
			}

			key = usern.getRandomCode();
			usern.setName(user.getName());
			usern.setImgUrl(user.getImgUrl());
			usern.setName(user.getName());
			usern.setNickname(user.getNickname());
			usern.setToken(user.getToken());
			usern.setPhone(user.getPhone());
			usern.setCardFront(user.getCardFront());
			usern.setCardBack(user.getCardBack());
			usern.setCreateTime(user.getCreateTime());
			usern.setUpdateTime(user.getUpdateTime());
			usern.setAuditor(user.getAuditor());
			usern.setStatus(user.getStatus());
			usern.setAge(user.getAge());
			usern.setCompany(user.getCompany());
			usern.setPosition(user.getPosition());
			usern.setHobby(user.getHobby());
			usern.setSign(user.getSign());
			usern.setCareer(user.getCareer());
			usern.setIsDistribution(user.getIsDistribution());
			usern.setIsNewuser(user.getIsNewuser());
			userService.saveORupdate(usern);
		}

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("name", name);
		message.addProperty("userName", username);
		message.addProperty("password", password);
		message.addProperty("token", key);
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 修改密码
	@RequestMapping(params = "updatePassword", method = RequestMethod.POST)
	public void updatePassword(HttpServletRequest request, HttpServletResponse response) {
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(password)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.login(userName);
		if (user == null) {
			message.addProperty("message", "token验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		StringBuilder builder = new StringBuilder();
		builder.append(user.getRandomCode() + password);
		String localSign = sign(builder);
		user.setPassword(localSign);
		userService.saveORupdate(user);

		message.addProperty("message", "修改密码成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 修改信息
	@RequestMapping(params = "update", method = RequestMethod.POST)
	public void update(HttpServletRequest request, User user, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(user.getId() + "") || user.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		int id = user.getId();

		User usern = userService.getid(Integer.valueOf(id));
		if (!usern.getUserName().equals(user.getUserName())) {
			message.addProperty("message", "不能修改用户账号");
			message.addProperty("success", true);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		usern.setName(user.getName());
		usern.setImgUrl(user.getImgUrl());
		usern.setName(user.getName());
		usern.setNickname(user.getNickname());
		usern.setPhone(user.getPhone());
		usern.setCardFront(user.getCardFront());
		usern.setCardBack(user.getCardBack());
		usern.setCreateTime(user.getCreateTime());
		usern.setUpdateTime(user.getUpdateTime());
		usern.setAuditor(user.getAuditor());
		usern.setStatus(user.getStatus());
		usern.setAge(user.getAge());
		usern.setCompany(user.getCompany());
		usern.setPosition(user.getPosition());
		usern.setHobby(user.getHobby());
		usern.setSign(user.getSign());
		usern.setCareer(user.getCareer());
		userService.saveORupdate(usern);

		message.addProperty("message", "修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 个人信息
	@RequestMapping(params = "info", method = RequestMethod.POST)
	public void info(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token)) {
			message.addProperty("message", "暂未登录,请重新登录哦");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User users = userService.gettoken(token);
		if (users == null) {
			message.addProperty("message", "token验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		// List<User> userList = userService.userList(user);

		// for (User users : userList) {
		JSonGridRecord record = new JSonGridRecord();
		record.addColumn("id", users.getId());
		record.addColumn("user_name", users.getUserName());
		record.addColumn("password", users.getPassword());
		record.addColumn("random_code", users.getRandomCode());
		record.addColumn("img_url", users.getImgUrl());
		record.addColumn("name", users.getName());
		record.addColumn("nickname", users.getNickname());
		record.addColumn("token", users.getToken());
		record.addColumn("phone", users.getPhone());
		record.addColumn("card_front", users.getCardFront());
		record.addColumn("card_back", users.getCardBack());
		record.addColumn("create_time", users.getCreateTime());
		record.addColumn("update_time", users.getUpdateTime());
		record.addColumn("auditor", users.getAuditor());
		record.addColumn("status", users.getStatus());
		record.addColumn("age", users.getAge());
		record.addColumn("company", users.getCompany());
		record.addColumn("position", users.getPosition());
		record.addColumn("hobby", users.getHobby());
		record.addColumn("sign", users.getSign());
		record.addColumn("career", users.getCareer());
		record.addColumn("is_distribution", users.getIsDistribution());
		record.addColumn("is_newuser", users.getIsNewuser());

		grid.addRecord(record);

		grid.addProperties("totalCount", 1);
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 区域经理骑手
	@RequestMapping(params = "salerView", method = RequestMethod.POST)
	public void salerView(HttpServletRequest request, HttpServletResponse response) {
		String bossId = request.getParameter("bossId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(bossId)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<User> list = userService.getBoss(Integer.valueOf(bossId));
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		for (User user : list) {
			JSonGridRecord record = new JSonGridRecord();

			record.addColumn("id", user.getId());
			record.addColumn("name", user.getName());
			record.addColumn("phone", user.getPhone());

			RiderInfo rider = riderService.getuserId(user.getId());
			record.addColumn("cardNo", rider.getCardNo());
			record.addColumn("city", rider.getCity());

			Object[] obj = orderTypeService.getCount(user.getId());
			DecimalFormat dFormat = new DecimalFormat("######.00");
			if (obj != null) {
				record.addColumn("num", obj[0]);
				if (obj[1] == null) {
					record.addColumn("balance", 0);
				} else {
					String balance = dFormat.format(Double.valueOf(obj[1].toString()));
					record.addColumn("balance", balance);
				}
			} else {
				record.addColumn("num", 0);
				record.addColumn("balance", 0);
			}

			Object[] score = runEvaluateService.getScore(user.getId());
			double total = 5;
			if (score != null) {
				if (score[0] != null && score[1] != null) {
					double subtotal = (Double.valueOf(score[0].toString()) + Double.valueOf(score[1].toString())) / 2;
					total = subtotal;
				}
			}
			record.addColumn("score", total);
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", list.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 用户列表
	@RequestMapping(params = "export", method = RequestMethod.POST)
	public void export(HttpServletRequest request, HttpServletResponse response) {
		String city = request.getParameter("city");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime)/* || StringUtil.isEmpty(city)*/) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		RiderInfo riderInfo = new RiderInfo();
		riderInfo.setCity(city);
		List<RiderInfo> list = riderService.riderInfoList(riderInfo);
		String[][] datas = new String[list.size()][9];
		for (int i = 0; i < list.size(); i++) {
			RiderInfo rider = list.get(i);
			User user = rider.getUserId();
			String balance = "0";
			String num = "0";
			Object[] obj = orderTypeService.getCount(user.getId(), startTime, endTime);
			if (obj != null) {
				num = obj[0].toString();
				if (obj[1] != null) {
					DecimalFormat dFormat = new DecimalFormat("######.00");
					balance = dFormat.format(Double.valueOf(obj[1].toString()));
				}
			}

			Object[] score = runEvaluateService.getScore(user.getId());
			double subtotal = 5;
			if (score != null) {
				if (score[0] != null && score[1] != null) {
					subtotal = (Double.valueOf(score[0].toString()) + Double.valueOf(score[1].toString())) / 2;
				}
			}
			DecimalFormat assess = new DecimalFormat("######.0");
			String total = assess.format(subtotal);
			datas[i][0] = user.getName();
			datas[i][1] = user.getPhone();
			datas[i][2] = num;
			datas[i][3] = balance;
			datas[i][4] = total;
			datas[i][5] = rider.getBankCard();
			datas[i][6] = rider.getCardNo();
			datas[i][7] = rider.getBankType();
			datas[i][8] = rider.getAccountBank();
		}
		
		String Path = request.getRealPath("/");	//获得根路径 ynw
		int dzbase = Path.indexOf("\\DzBase\\");	//截取到给定字符串位	ynw
		
		String[] arr = ExcelUtils.riderDetailed(datas, Path.substring(0, dzbase), city);
		
		int dzplfrom = arr[2].indexOf("\\DzPlfrom\\");	//截取到给定字符串位	ynw
		
		message.addProperty("message", arr[1]);
		message.addProperty("success", arr[0]);
		
		message.addProperty("path", arr[2].substring(dzplfrom, arr[2].length()));	//将路径发送给前端	ynw
		new PushJson().outString(message.toJSonString(), response);
	}
	
}
