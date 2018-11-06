package com.dz.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dz.entity.Shop;
import com.dz.entity.User;
import com.dz.entity.Wallet;
import com.dz.service.IShopService;
import com.dz.service.IUserService;
import com.dz.service.IWalletService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.MD5Util;
import com.dz.util.PushJson;
import com.dz.util.SmessageUtils;
import com.dz.util.StringUtil;
import com.dz.util.TokenUtil;
import com.dz.util.Rcloud.RcloudParameter;
import com.dz.util.Rcloud.io.rong.RongCloud;
import com.dz.util.Rcloud.io.rong.models.response.TokenResult;
import com.dz.util.Rcloud.io.rong.models.user.UserModel;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IUserService userService;

	@Autowired
	private IShopService shopService;

	@Autowired
	private IWalletService walletService;

	private static final Map<String, String> temp = new ConcurrentHashMap<String, String>();

	private static String sign(StringBuilder builder) {
		return MD5Util.MD5(builder.toString());
	}

	// 登陆
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public void login(HttpServletRequest request, HttpServletResponse response) {
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(password)) {
			message.addProperty("message", "请输入帐号密码");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.login(userName);
		if (user == null) {
			message.addProperty("message", "该帐号不存在!");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		StringBuilder builder = new StringBuilder();
		builder.append(user.getRandomCode() + password);
		String localSign = sign(builder);
		if (!user.getUserName().equals(userName) || !user.getPassword().equals(localSign)) {
			message.addProperty("message", "账号或密码不正确!");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		// 融云token
		RongCloud rongCloud = RongCloud.getInstance(RcloudParameter.appKey, RcloudParameter.appSecret);
		com.dz.util.Rcloud.io.rong.methods.user.User RcloudUser = rongCloud.user;
		UserModel newUser = null;
		if (!StringUtil.isEmpty(user.getNickname()) && !StringUtil.isEmpty(user.getImgUrl())) {
			newUser = new UserModel().setId(user.getUserName()).setName(user.getNickname())
					.setPortrait(user.getImgUrl());
		} else {
			newUser = new UserModel().setId(user.getUserName()).setName("啄呗用户").setPortrait(RcloudParameter.logo);
		}
		try {
			TokenResult result = RcloudUser.register(newUser);
			TokenResult joinresult = RcloudUser.join(newUser);
			System.out.println(joinresult);
			String[] arr = result.toString().split("\",\"");
			String RcloudToken = arr[0].toString();
			user.setRcloudToken(RcloudToken.substring(10, RcloudToken.length()));
		} catch (Exception e) {
			e.printStackTrace();
			message.addProperty("message", "融云端获取token失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		String token = TokenUtil.getRandomChar(11);
		user.setToken(token);
		userService.saveORupdate(user);
		message.addProperty("token", token);
		message.addProperty("RcloudToken", user.getRcloudToken());
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 跑腿用户登陆
	@RequestMapping(value = "/runLogin", method = RequestMethod.POST)
	@ResponseBody
	public void runLogin(HttpServletRequest request, HttpServletResponse response) {
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(password)) {
			message.addProperty("message", "请输入帐号密码");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.login(userName);
		if (user == null) {
			message.addProperty("message", "该帐号不存在!");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		StringBuilder builder = new StringBuilder();
		builder.append(user.getRandomCode() + password);
		String localSign = sign(builder);
		if (!user.getUserName().equals(userName) || !user.getPassword().equals(localSign)) {
			message.addProperty("message", "账号或密码不正确!");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		boolean flg = Boolean.parseBoolean(user.getIsDistribution() + "");
		if (!flg) {
			if (!user.getIsDistribution()) {
				message.addProperty("message", "没有登陆权限");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			message.addProperty("message", "不是跑腿用户，不能登陆");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		// 融云token
		RongCloud rongCloud = RongCloud.getInstance(RcloudParameter.appKey, RcloudParameter.appSecret);
		com.dz.util.Rcloud.io.rong.methods.user.User RcloudUser = rongCloud.user;
		UserModel newUser = null;
		if (!StringUtil.isEmpty(user.getNickname()) && !StringUtil.isEmpty(user.getImgUrl())) {
			newUser = new UserModel().setId(user.getUserName()).setName(user.getNickname())
					.setPortrait(user.getImgUrl());
		} else {
			newUser = new UserModel().setId(user.getUserName()).setName("啄呗用户").setPortrait(RcloudParameter.logo);
		}
		try {
			TokenResult result = RcloudUser.register(newUser);
			String[] arr = result.toString().split("\",\"");
			String RcloudToken = arr[0].toString();
			user.setRcloudToken(RcloudToken.substring(10, RcloudToken.length()));
		} catch (Exception e) {
			e.printStackTrace();
			message.addProperty("message", "融云端获取token失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		String token = TokenUtil.getRandomChar(11);
		user.setToken(token);
		userService.saveORupdate(user);
		message.addProperty("token", token);
		message.addProperty("RcloudToken", user.getRcloudToken());
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 登陆
	@RequestMapping(value = "/passLogin", method = RequestMethod.POST)
	@ResponseBody
	public void passLogin(HttpServletRequest request, HttpServletResponse response) {
		String userName = request.getParameter("userName");
		String code = request.getParameter("code");
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(code)) {
			message.addProperty("message", "请输入帐号验证码");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.login(userName);
		if (user == null) {
			message.addProperty("message", "该帐号不存在!");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (!user.getUserName().equals(userName)) {
			message.addProperty("message", "账号或密码不正确!");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (code.equals(temp.get(userName))) {
			temp.remove(userName);
			// 融云token
			RongCloud rongCloud = RongCloud.getInstance(RcloudParameter.appKey, RcloudParameter.appSecret);
			com.dz.util.Rcloud.io.rong.methods.user.User RcloudUser = rongCloud.user;
			UserModel newUser = null;
			if (!StringUtil.isEmpty(user.getNickname()) && !StringUtil.isEmpty(user.getImgUrl())) {
				newUser = new UserModel().setId(user.getUserName()).setName(user.getNickname())
						.setPortrait(user.getImgUrl());
			} else {
				newUser = new UserModel().setId(user.getUserName()).setName("啄呗用户").setPortrait(RcloudParameter.logo);
			}
			try {
				TokenResult result = RcloudUser.register(newUser);
				String[] arr = result.toString().split("\",\"");
				String RcloudToken = arr[0].toString();
				user.setRcloudToken(RcloudToken.substring(10, RcloudToken.length()));
			} catch (Exception e) {
				e.printStackTrace();
				message.addProperty("message", "融云端获取token失败");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}

			String token = TokenUtil.getRandomChar(11);
			user.setToken(token);
			userService.saveORupdate(user);
			message.addProperty("token", token);
			message.addProperty("RcloudToken", user.getRcloudToken());
			message.addProperty("success", true);
		} else {
			message.addProperty("message", "验证码不正确!");
			message.addProperty("success", false);
		}

		new PushJson().outString(message.toJSonString(), response);
	}

	// 用户列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response, User user) {

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<User> userList = userService.userList(user);

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

	// 添加或修改用户 信息
	@RequestMapping(params = "register", method = RequestMethod.POST)
	public void register(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String code = request.getParameter("code");
		String nickname = request.getParameter("nickname");

		String imgUrl = request.getParameter("imgUrl");
		String companyId = request.getParameter("companyId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password) || StringUtil.isEmpty(code)) {
			message.addProperty("message", "用户名,密码和验证码不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User tuser = userService.login(username);
		if (tuser != null) {
			message.addProperty("message", "该用户已注册");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		String ncode = temp.get(username);
		if (code.equals(ncode)) {
			String randomCode = TokenUtil.getRandomChar(8);
			StringBuilder builder = new StringBuilder();

			temp.remove(username);

			User user = new User();
			builder.append(randomCode + password);
			String localSign = sign(builder);

			user.setUserName(username);
			user.setRandomCode(randomCode);
			user.setPassword(localSign);
			if (!StringUtil.isEmpty(nickname)) {
				user.setNickname(new String(nickname.getBytes("ISO-8859-1"), "UTF-8"));
			}

			user.setImgUrl(imgUrl);
			String token = TokenUtil.getRandomChar(11);
			user.setToken(token);
			user.setBossId(0);
			user.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			if (!StringUtil.isEmpty(companyId)) {
				user.setCompanyId(Integer.valueOf(companyId));
			}

			// 融云token
			RongCloud rongCloud = RongCloud.getInstance(RcloudParameter.appKey, RcloudParameter.appSecret);
			com.dz.util.Rcloud.io.rong.methods.user.User RcloudUser = rongCloud.user;
			UserModel newUser = null;
			if (!StringUtil.isEmpty(nickname) && !StringUtil.isEmpty(imgUrl)) {
				newUser = new UserModel().setId(user.getUserName()).setName(nickname).setPortrait(imgUrl);
			} else {
				newUser = new UserModel().setId(user.getUserName()).setName("啄呗用户").setPortrait(RcloudParameter.logo);
			}

			try {
				TokenResult result = RcloudUser.register(newUser);
				String[] arr = result.toString().split("\",\"");
				String RcloudToken = arr[0].toString();
				user.setRcloudToken(RcloudToken.substring(10, RcloudToken.length()));
			} catch (Exception e) {
				e.printStackTrace();
				message.addProperty("message", "融云端获取token失败");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}

			userService.saveORupdate(user);

			Shop shop = new Shop();
			shop.setUserId(user);
			shopService.saveORupdate(shop);

			Wallet wallet = new Wallet();
			wallet.setBalance("0");
			wallet.setErrorTimes("0");
			wallet.setFreeTimes("0");
			wallet.setLockingTime("0");
			wallet.setStatus("1");
			wallet.setRedBalance("0");
			wallet.setUserId(user);
			walletService.saveORupdate(wallet);
			user.setShop(shop);
			userService.saveORupdate(user);

			// 登陆
			message.addProperty("token", token);
			message.addProperty("RcloudToken", user.getRcloudToken());
			message.addProperty("success", true);
			message.addProperty("message", "注册成功");
		} else {
			message.addProperty("message", "验证码不正确");
			message.addProperty("success", false);
		}

		new PushJson().outString(message.toJSonString(), response);

	}

	// 验证token
	@RequestMapping(params = "checkToken", method = RequestMethod.POST)
	public void checkToken(HttpServletRequest request, User user, HttpServletResponse response) {
		String token = request.getParameter("token");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token)) {
			message.addProperty("message", "用户名,密码和验证码不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User tuser = userService.gettoken(token);
		if (tuser == null) {
//			message.addProperty("message", "token验证失败");
			message.addProperty("message", "账号已在另一台设备登录");//2018-10-31 @Tyy
			message.addProperty("isout", true);
			message.addProperty("success", false);
		} else {
			message.addProperty("message", "token正常");
			message.addProperty("success", true);
		}

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
			users.setImgUrl("http://39.108.6.102:8080/DzClient/common/img/defual.png");//2018-10-25 @Tyy
			users.setBossId(0);
			users.setCompany(user.getCompany());
			users.setPosition(user.getPosition());
			users.setHobby(user.getHobby());
			users.setSign(user.getSign());
			users.setCareer(user.getCareer());
			users.setIsDistribution(user.getIsDistribution());
			users.setIsNewuser(user.getIsNewuser());
			user.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			user.setCompanyId(user.getCompanyId());
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
		String code = request.getParameter("code");

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(password) || StringUtil.isEmpty(code)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		// 2018-10-24 @Tyy start 密码长度限制
		if (password.length() > 20 || password.length() < 6) {
			message.addProperty("message", "请输入6~20位密码");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		// end

		User user = userService.login(userName);
		if (user == null) {
			message.addProperty("message", "token验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (code.equals(temp.get(userName))) {
			temp.remove(userName);
			StringBuilder builder = new StringBuilder();
			builder.append(user.getRandomCode() + password);
			String localSign = sign(builder);
			user.setPassword(localSign);
			userService.saveORupdate(user);

			message.addProperty("message", "修改密码成功");
			message.addProperty("success", true);
		} else {
			message.addProperty("message", "验证码不正确.");
			message.addProperty("success", false);
		}

		new PushJson().outString(message.toJSonString(), response);
	}

	// 修改密码
	@RequestMapping(params = "changePass", method = RequestMethod.POST)
	public void changePass(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");
		String password = request.getParameter("password");

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(token) || StringUtil.isEmpty(password)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		// 2018-10-24 @Tyy start 密码长度限制
		if (password.length() > 20 || password.length() < 6) {
			message.addProperty("message", "请输入6~20位密码");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		// end

		User user = userService.gettoken(token);
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

	// 获取和验证短信验证玛(注册)
	@RequestMapping(params = "getCode", method = RequestMethod.POST)
	public void getCode(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String userName = request.getParameter("userName");

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(userName)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.login(userName);
		if (user != null) {
			message.addProperty("message", "该手机已注册");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		String sms = new SmessageUtils().getCode(userName);
		temp.put(userName, sms);

		message.addProperty("message", "验证码发送成功.");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 获取和验证短信验证玛(忘记密码)
	@RequestMapping(params = "getCodeForget", method = RequestMethod.POST)
	public void getCodeForget(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String userName = request.getParameter("userName");

		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(userName)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.login(userName);
		if (user == null) {
			message.addProperty("message", "该手机未注册");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		String sms = new SmessageUtils().getCode(userName);
		temp.put(userName, sms);

		message.addProperty("message", "验证码发送成功.");
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
//			message.addProperty("message", "token验证失败");
			message.addProperty("message", "账号已在另一台设备登录");//2018-10-31 @Tyy
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

}
