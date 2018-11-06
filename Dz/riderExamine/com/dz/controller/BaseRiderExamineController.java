package com.dz.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.RiderExamine;
import com.dz.entity.RiderInfo;
import com.dz.entity.Shop;
import com.dz.entity.User;
import com.dz.entity.Wallet;
import com.dz.service.IRiderExamineService;
import com.dz.service.IRiderInfoService;
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

@Controller
@RequestMapping("/base/riderExamine")
public class BaseRiderExamineController {

	@Autowired
	private IRiderExamineService riderExamineService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IShopService shopService;

	@Autowired
	private IWalletService walletService;
	
	@Autowired
	private IRiderInfoService riderInfoService;

	private static String sign(StringBuilder builder) {
		return MD5Util.MD5(builder.toString());
	}

	// 用户列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response, RiderExamine riderExamine) {

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		List<RiderExamine> riderExamineList = riderExamineService.riderExamineList(riderExamine);

		for (RiderExamine riderExamines : riderExamineList) {
			JSonGridRecord record = new JSonGridRecord();
			record.addColumn("id", riderExamines.getId());
			record.addColumn("name", riderExamines.getName());
			record.addColumn("phone", riderExamines.getPhone());
			record.addColumn("cardNo", riderExamines.getCardNo());
			record.addColumn("create_time", riderExamines.getCreateTime());
			record.addColumn("age", riderExamines.getAge());
			record.addColumn("city", riderExamines.getCity());
			record.addColumn("education", riderExamines.getEducation());
			record.addColumn("sex", riderExamines.getSex());

			grid.addRecord(record);
		}
		grid.addProperties("totalCount", riderExamineList.size());
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 删除用户
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, RiderExamine riderExamine, HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(riderExamine.getId() + "") || riderExamine.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		riderExamineService.delete(riderExamine.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加用户
	@RequestMapping(params = "save", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, HttpServletResponse response) {
		String sex = request.getParameter("sex");// 性别
		String city = request.getParameter("city");// 城市
		String phone = request.getParameter("phone");// 手机
		String cardNo = request.getParameter("cardNo");// 身份证号
		String name = request.getParameter("name");// 姓名
		String education = request.getParameter("education");// 学历
		String accountOpening = request.getParameter("accountOpening");
		String bankCard = request.getParameter("bankCard");
		String bankType = request.getParameter("bankType");
		String accountBank = request.getParameter("accountBank");
		String age = request.getParameter("age");
		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(sex) || StringUtil.isEmpty(education) || StringUtil.isEmpty(city)
				|| StringUtil.isEmpty(phone) || StringUtil.isEmpty(cardNo) || StringUtil.isEmpty(name)
				|| StringUtil.isEmpty(accountOpening) || StringUtil.isEmpty(bankCard) || StringUtil.isEmpty(bankType)
				|| StringUtil.isEmpty(accountBank)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if (!NumberUtils.isNumber(phone) || phone.length() != 11) {
			message.addProperty("message", "请输入正确的手机号");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		if (cardNo.length() != 18) {
			message.addProperty("message", "请输入正确的身份证号");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		RiderExamine riderExamine = new RiderExamine();
		riderExamine.setSex(sex);
		riderExamine.setCity(city);
		riderExamine.setName(name);
		riderExamine.setEducation(education);
		riderExamine.setPhone(phone);
		riderExamine.setCardNo(cardNo);
		riderExamine.setAccountOpening(accountOpening);
		riderExamine.setBankCard(bankCard);
		riderExamine.setBankType(bankType);
		riderExamine.setAccountBank(accountBank);
		riderExamine.setAge(age);
		riderExamine.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		riderExamineService.saveORupdate(riderExamine);
		message.addProperty("message", "申请成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 个人信息
	@RequestMapping(params = "info", method = RequestMethod.POST)
	public void info(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "暂未登录,请重新登录哦");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		RiderExamine riderExamines = riderExamineService.getid(Integer.valueOf(id));
		if (riderExamines == null) {
			message.addProperty("message", "token验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		JSonGridRecord record = new JSonGridRecord();
		record.addColumn("id", riderExamines.getId());
		record.addColumn("name", riderExamines.getName());
		record.addColumn("phone", riderExamines.getPhone());
		record.addColumn("cardNo", riderExamines.getCardNo());
		record.addColumn("create_time", riderExamines.getCreateTime());
		record.addColumn("age", riderExamines.getAge());

		grid.addRecord(record);

		grid.addProperties("totalCount", 1);
		new PushJson().outString(grid.toJSonString("list"), response);

	}

	// 审核用户
	@RequestMapping(params = "apply", method = RequestMethod.POST)
	public void apply(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");// 性别
		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		RiderExamine riderExamine = riderExamineService.getid(Integer.valueOf(id));
		if (riderExamine == null) {
			message.addProperty("message", "申请记录不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		RiderInfo riderInfo = new RiderInfo();
		riderInfo.setAccountBank(riderExamine.getAccountBank());
		riderInfo.setAccountOpening(riderExamine.getAccountOpening());
		riderInfo.setBankCard(riderExamine.getBankCard());
		riderInfo.setBankType(riderExamine.getBankType());
		riderInfo.setCardNo(riderExamine.getCardNo());
		riderInfo.setCity(riderExamine.getCity());
		riderInfo.setEducation(riderExamine.getEducation());
		riderInfo.setSex(riderExamine.getSex());

		User user = userService.getUserName(riderExamine.getPhone());
		if (user == null) {
			String random_numb = TokenUtil.getRandomChar(8);
			StringBuilder builder = new StringBuilder();
			builder.append(random_numb + "123456");
			String localSign = sign(builder);

			User newUser = new User();
			newUser.setAge(riderExamine.getAge());
			newUser.setUserName(riderExamine.getPhone());
			newUser.setName(riderExamine.getName());
			newUser.setIsBusiness(false);
			newUser.setAuditor("1");
			newUser.setIsDistribution(true);
			newUser.setIsNewuser(true);
			newUser.setCreateTime(riderExamine.getCreateTime());
			newUser.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			newUser.setRandomCode(random_numb);
			newUser.setPassword(localSign);
			newUser.setPhone(riderExamine.getPhone());
			userService.saveORupdate(newUser);

			Shop shop = new Shop();
			shop.setUserId(newUser);
			shopService.saveORupdate(shop);

			Wallet wallet = new Wallet();
			wallet.setBalance("0");
			wallet.setErrorTimes("0");
			wallet.setFreeTimes("0");
			wallet.setLockingTime("0");
			wallet.setStatus("1");
			wallet.setRedBalance("0");
			wallet.setUserId(newUser);
			walletService.saveORupdate(wallet);
			
			riderInfo.setUserId(newUser);
			riderInfoService.saveORupdate(riderInfo);
			
			User theUser = userService.getUserName(riderExamine.getPhone());
			theUser.setShop(shop);
			userService.saveORupdate(theUser);
		} else {
			user.setAge(riderExamine.getAge());
			user.setUserName(riderExamine.getPhone());
			user.setName(riderExamine.getName());
			user.setIsBusiness(false);
			user.setAuditor("1");
			user.setIsDistribution(true);
			user.setPhone(riderExamine.getPhone());
			user.setCreateTime(riderExamine.getCreateTime());
			user.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			
			riderInfo.setUserId(user);
			riderInfoService.saveORupdate(riderInfo);
			
			userService.saveORupdate(user);
		}

		riderExamineService.delete(id);
		message.addProperty("message", "操作成功");
		message.addProperty("success", true);
		new SmessageUtils().sendToRider(user.getPhone(), user.getUserName(), user.getPassword());	//将审核通过的账号密码发送到对应骑手手机 ynw
		new PushJson().outString(message.toJSonString(), response);
	}

}
