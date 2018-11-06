package com.dz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Address;
import com.dz.entity.Label;
import com.dz.entity.Order;
import com.dz.entity.User;
import com.dz.service.IAddressService;
import com.dz.service.ILabelService;
import com.dz.service.IOrderService;
import com.dz.service.IUserService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/address")
public class AddressController {

	@Autowired
	private IAddressService addressService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IOrderService orderService;

	@Autowired
	private ILabelService labelService;

	// ===============================手机端============================== //
	@RequestMapping(params = "getAddress", method = RequestMethod.POST)
	public void getAddress(HttpServletRequest request,
			HttpServletResponse response) {

		String token = request.getParameter("token");
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token)) {
			message.addProperty("message", "token不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);
		if (user == null) {
			message.addProperty("success", false);
			message.addProperty("message", "token验证失败");
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Address addr = addressService.getAddress(user.getId());

		if (addr == null) {
			message.addProperty("message", "addr不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		message.addProperty("success", true);
		message.addProperty("username", addr.getName());
		message.addProperty("phone", addr.getPhone());
		message.addProperty("id", addr.getId());
		message.addProperty("address", addr.getAddress());
		message.addProperty("room", addr.getRoom());
		new PushJson().outString(message.toJSonString(), response);
	}

	// 外卖配送地址
	@RequestMapping(params = "receiveAddress", method = RequestMethod.POST)
	public void receiveAddress(HttpServletRequest request,
			HttpServletResponse response, Address address) {
		String token = request.getParameter("token");
		if (StringUtil.isEmpty(token)) {
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

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		List<Address> addressList = addressService.useraddress(user.getId());
		for (Address addresss : addressList) {
			JSonGridRecord record = new JSonGridRecord();
			StringBuilder label = new StringBuilder();
			List<Label> labellist = labelService.getLabel("address", addresss
					.getId()
					+ "");
			for (Label labels : labellist) {
				label.append(labels.getContent());
			}
			record.addColumn("label", label.toString());
			record.addColumn("id", addresss.getId());
			record.addColumn("address", addresss.getAddress());
			record.addColumn("is_default", addresss.getIsDefault());
			record.addColumn("name", addresss.getName());
			record.addColumn("phone", addresss.getPhone());
			record.addColumn("room", addresss.getRoom());
			grid.addRecord(record);
		}
		grid.addProperties("totalCount", addressList.size());
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 到店自取地址
	@RequestMapping(params = "Topickup", method = RequestMethod.POST)
	public void Topickup(HttpServletRequest request,
			HttpServletResponse response, Address address) {
		String orderId = request.getParameter("orderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(orderId)) {
			message.addProperty("message", "orderId不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Order order = orderService.getOrder(Integer.valueOf(orderId));
		if (order == null) {
			message.addProperty("message", "该订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		message.addProperty("success", true);
		message.addProperty("address", order.getCompanyId().getPosition());
		new PushJson().outString(message.toJSonString(), response);
	}

	// 删除   2018-10-20 @Tyy 地址伪删除
	@RequestMapping(params = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String token = request.getParameter("token");
		
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)||StringUtil.isEmpty(token)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		Address address = addressService.find(Integer.valueOf(id));
		if(address == null){
			message.addProperty("message", "找不到对应地址");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		address.setIsDelete(true);
		if(address.getIsDefault()){
			User user = userService.gettoken(token);
			if (user == null) {
				message.addProperty("message", "token过期");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			Address uaddre = addressService.get(user.getId());
			if(uaddre == null){
				message.addProperty("message", "地址不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			uaddre.setIsDefault(true);
			addressService.saveORupdate(uaddre);
		}
		address.setIsDefault(false);
		addressService.saveORupdate(address);
		
		message.addProperty("message", "删除成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}
	
	// 添加信息
	@RequestMapping(params = "saveORupdate", method = RequestMethod.POST)
	public void saveORupdate(HttpServletRequest request,
			HttpServletResponse response) {
		String token = request.getParameter("token");
		String addr = request.getParameter("address").replaceAll(" ", "");
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String isDefault = request.getParameter("isDefault");
		String room = request.getParameter("room");
		String label = request.getParameter("label");
		String lng = request.getParameter("lng");
		
		JSonMessage message = new JSonMessage();

		if (StringUtil.isEmpty(addr) || StringUtil.isEmpty(name)
				|| StringUtil.isEmpty(token) || StringUtil.isEmpty(phone)) {
			message.addProperty("message", "缺少必要参数");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (StringUtil.isEmpty(token)) {
			message.addProperty("message", "token验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);

		if (user == null) {
			message.addProperty("message", "user不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (isDefault.equals("true")) {
			List<Address> addrList = addressService.useraddress(user.getId());
			for (Address address : addrList) {
				address.setIsDefault(false);
				addressService.saveORupdate(address);
			}
		}

		Address address = new Address();
		address.setUserId(user);
		address.setAddress(addr);
		address.setName(name);
		address.setPhone(phone);
		address.setRoom(room);
		address.setLocation(null);
		address.setIsDelete(false);
		if (isDefault.equals("true")) {
			address.setIsDefault(true);
		} else {
			address.setIsDefault(false);
		}
		address.setLocation(lng);
		addressService.saveORupdate(address);

		if (!StringUtil.isEmpty(label)) {
			Label labels = new Label();
			List<Label> labellist = labelService.getLabel("address", address
					.getId()
					+ "");
			if (labellist.size() > 0) {
				for (Label labeln : labellist) {
					labels.setId(labeln.getId());
				}
			}
			labels.setType("address");
			labels.setContent(label);
			labels.setCustomId(address.getId() + "");
			labelService.saveORupdate(labels);
		}
		message.addProperty("message", "添加信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 单个地址
	@RequestMapping(params = "update", method = RequestMethod.POST)
	public void updateAddr(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		String token = request.getParameter("token");
		// 选填项
		String addr = request.getParameter("address");
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String isDefault = request.getParameter("isDefault");
		String room = request.getParameter("room");
		String label = request.getParameter("label");
		String location = request.getParameter("location");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);

		if (user == null) {
			message.addProperty("message", "user不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<Address> addrList = addressService.useraddress(user.getId());
		for (Address address : addrList) {
			address.setIsDefault(false);
			addressService.saveORupdate(address);
		}

		Address address = addressService.find(Integer.valueOf(id));

		if (address == null) {
			message.addProperty("message", "地址不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		address.setIsDefault(true);
		if (!StringUtil.isEmpty(isDefault)) {
			if (isDefault.equals("true")) {
				address.setIsDefault(true);
			} else {
				address.setIsDefault(false);
			}
		}
		if (!StringUtil.isEmpty(location)) {
			address.setLocation(location);
		}
		if (!StringUtil.isEmpty(addr)) {
			address.setAddress(addr);
		}
		if (!StringUtil.isEmpty(name)) {
			address.setName(name);
		}
		if (!StringUtil.isEmpty(phone)) {
			address.setPhone(phone);
		}
		if (!StringUtil.isEmpty(room)) {
			address.setRoom(room);
		}
		if (!StringUtil.isEmpty(label)) {
			Label labels = new Label();
			List<Label> labellist = labelService.getLabel("address", address
					.getId()
					+ "");
			if (labellist.size() > 0) {
				labels = labellist.get(0);
			}
			labels.setContent(label);
			labelService.saveORupdate(labels);
		}
		addressService.saveORupdate(address);

		message.addProperty("message", "修改信息成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);

	}

	// 单个地址
	@RequestMapping(params = "find", method = RequestMethod.POST)
	public void find(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(id)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Address address = addressService.find(Integer.valueOf(id));

		if (address == null) {
			message.addProperty("message", "地址不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		message.addProperty("success", true);

		StringBuilder label = new StringBuilder();
		List<Label> labellist = labelService.getLabel("address", address
				.getId()
				+ "");
		for (Label labels : labellist) {
			label.append(labels.getContent());
		}
		message.addProperty("label", label.toString());
		message.addProperty("id", address.getId());
		message.addProperty("address", address.getAddress());
		message.addProperty("is_default", address.getIsDefault());
		message.addProperty("name", address.getName());
		message.addProperty("phone", address.getPhone());
		message.addProperty("room", address.getRoom());
		
		if(address.getLocation() != null){
			String locat = address.getLocation();
			message.addProperty("lng", locat.substring(0, locat.indexOf(",")));
			message.addProperty("lat", locat.substring(locat.indexOf(",") + 1, locat.length()));
		}
		
		new PushJson().outString(message.toJSonString(), response);

	}

	public IAddressService getAddressService() {
		return addressService;
	}

	public void setAddressService(IAddressService addressService) {
		this.addressService = addressService;
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public IOrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(IOrderService orderService) {
		this.orderService = orderService;
	}

	public ILabelService getLabelService() {
		return labelService;
	}

	public void setLabelService(ILabelService labelService) {
		this.labelService = labelService;
	}
	
}