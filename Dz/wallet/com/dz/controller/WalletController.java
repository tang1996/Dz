package com.dz.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.User;
import com.dz.entity.Wallet;
import com.dz.service.IUserService;
import com.dz.service.IWalletService;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/wallet")
public class WalletController {

	@Autowired
	private IWalletService walletService;

	@Autowired
	private IUserService userService;

	// 查询列表
	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");
		// token = "cOZ6cjmF9NF";
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token)) {
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

		message.addProperty("success", true);

		Wallet wallet = walletService.userwallet(user.getId());

		if (wallet == null) {
			message.addProperty("message", "钱包不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		
		message.addProperty("id", wallet.getId());
		message.addProperty("balance", wallet.getBalance());
		message.addProperty("redBalance", wallet.getRedBalance());
		message.addProperty("free_times", wallet.getFreeTimes());
		message.addProperty("password", wallet.getPassword());
		message.addProperty("locking_time", wallet.getLockingTime());
		message.addProperty("error_times", wallet.getErrorTimes());
		message.addProperty("wstatus", wallet.getStatus());
		message.addProperty("user_id", wallet.getUserId().getName());

		new PushJson().outString(message.toJSonString(), response);

	}

	// 删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, Wallet wallet,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(wallet.getId() + "") || wallet.getId() == 0) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		walletService.delete(wallet.getId() + "");
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 添加或修改 信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request, Wallet wallet,
			HttpServletResponse response) {
		JSonMessage message = new JSonMessage();

		walletService.saveORupdate(wallet);

		message.addProperty("message", "添加或修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}
