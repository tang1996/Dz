package com.dz.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dz.entity.Company;
import com.dz.entity.Goods;
import com.dz.entity.Nature;
import com.dz.entity.Order;
import com.dz.entity.Relating;
import com.dz.entity.Shop;
import com.dz.entity.ShopGoods;
import com.dz.entity.User;
import com.dz.service.ICompanyService;
import com.dz.service.IGoodsService;
import com.dz.service.INatureService;
import com.dz.service.IOrderService;
import com.dz.service.IRelatingService;
import com.dz.service.IShopGoodsService;
import com.dz.service.IShopService;
import com.dz.service.IUserService;
import com.dz.util.JSonGrid;
import com.dz.util.JSonGridRecord;
import com.dz.util.JSonMessage;
import com.dz.util.JSonTree;
import com.dz.util.JSonTreeNode;
import com.dz.util.PushJson;
import com.dz.util.StringUtil;

@Controller
@RequestMapping("/shop")
public class ShopController {

	@Autowired
	private IShopService shopService;

	@Autowired
	private IShopGoodsService shopgoodsService;

	@Autowired
	private IGoodsService goodsService;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IRelatingService relatingService;

	@Autowired
	private INatureService natureService;

	@Autowired
	private IOrderService orderService;

	@RequestMapping(params = "getShopGoods", method = RequestMethod.POST)
	public void getShopGoods(HttpServletRequest request,
			HttpServletResponse response, Shop shop) {

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		Shop newShop = shopService.getByShop(shop.getUserId().getId());
		if (newShop == null) {
			JSonMessage message = new JSonMessage();
			message.addProperty("message", "userid不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		List<ShopGoods> listGoods = newShop.getShopGoods();

		for (ShopGoods goods : listGoods) {
			JSonGridRecord record = new JSonGridRecord();
			Goods ngoods = goodsService.getGoods(goods.getNgoodsId());
			record.addColumn("name", ngoods.getName());
			record.addColumn("id", ngoods.getId());
			record.addColumn("img", ngoods.getZoomUrl());
			record.addColumn("companyName", ngoods.getCompanyId().getName());
			record.addColumn("icon", ngoods.getCompanyId().getLogo());
		}

		grid.addProperties("totalCount", listGoods.size());
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	@RequestMapping(params = "save", method = RequestMethod.POST)
	public void save(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("comId");
		String token = request.getParameter("token");
		String goodsId = request.getParameter("goodId");
		String natrue = request.getParameter("natrue");
		String orderId  = request.getParameter("orderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(token)
				|| StringUtil.isEmpty(goodsId) || StringUtil.isEmpty(natrue)) {
			message.addProperty("message", "comId,token,goodId,natrue不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = companyService.getCompany(Integer.valueOf(companyId));

		if (company == null) {
			message.addProperty("message", "商家不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		User user = userService.gettoken(token);

		if (user == null) {
			message.addProperty("message", "token验证失败");
			message.addProperty("success", false);
			message.addProperty("isout", true);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Goods good = goodsService.getGoods(Integer.valueOf(goodsId));
		if (good == null) {
			message.addProperty("message", "商品不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		synchronized (this) {
			Shop shop = user.getShop();

			if (shop != null) {
				boolean flg = true;
				List<ShopGoods> list = shopgoodsService.getShop(shop.getId());
				if (list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						
						if (list.get(i).getNgoodsId() == good.getId()) {
							int anumb = list.get(i).getNumb() + 1;
							list.get(i).setNumb(anumb);
							list.set(i, list.get(i));
							shop.setShopGoods(list);
							
							shopService.saveORupdate(shop);
							flg = false;
						}
					}

					if (flg) {
						ShopGoods shopgoods = new ShopGoods();
						shopgoods.setNgoodsId(Integer.valueOf(goodsId));
						shopgoods.setNumb(Integer.valueOf(1));
						shopgoods.setNatrue(natrue);
						shopgoods.setcId(Integer.valueOf(companyId));
						shopgoods.setShop(shop);
						list.add(shopgoods);

						shop.setShopGoods(list);
						
						shopService.saveORupdate(shop);
					}
				} else {
					List<ShopGoods> newShopList = new ArrayList<ShopGoods>();
					ShopGoods shopgoods = new ShopGoods();
					shopgoods.setNgoodsId(Integer.valueOf(goodsId));
					shopgoods.setNumb(Integer.valueOf(1));
					shopgoods.setNatrue(natrue);
					shopgoods.setcId(Integer.valueOf(companyId));
					shopgoods.setShop(shop);
					newShopList.add(shopgoods);

					shop.setShopGoods(newShopList);

					shopService.saveORupdate(shop);
				}
			} else {
				Shop newShop = new Shop();
				newShop.setUserId(user);
				shopService.saveORupdate(newShop);

				List<ShopGoods> newShopList = new ArrayList<ShopGoods>();
				ShopGoods shopgoods = new ShopGoods();
				shopgoods.setNgoodsId(Integer.valueOf(goodsId));
				shopgoods.setNumb(Integer.valueOf(1));
				shopgoods.setNatrue(natrue);
				shopgoods.setcId(Integer.valueOf(companyId));
				shopgoods.setShop(newShop);
				newShopList.add(shopgoods);
				newShop.setShopGoods(newShopList);
				shopService.saveORupdate(newShop);

				user.setShop(newShop);
				userService.saveORupdate(user);
			}
			
			if(!StringUtil.isEmpty(orderId)){
				List<Relating> listRelating = relatingService.getGoods(Integer.valueOf(goodsId), Integer.valueOf(companyId), Integer.valueOf(orderId));
				if(listRelating.size() > 0){
					Relating re = listRelating.get(0);
					int count = re.getNumb() + 1;
					re.setNumb(count);
					relatingService.saveORupdate(re);
				}
			}
			
		}

		message.addProperty("message", "添加购物车成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	@RequestMapping(params = "sub", method = RequestMethod.POST)
	public void sub(HttpServletRequest request, HttpServletResponse response) {

		String companyId = request.getParameter("comId");
		String token = request.getParameter("token");
		String goodsId = request.getParameter("goodId");
		String natrue = request.getParameter("natrue");
		String orderId = request.getParameter("orderId");
		
		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(token)
				|| StringUtil.isEmpty(goodsId) || StringUtil.isEmpty(natrue)) {
			message.addProperty("message", "comId,token,goodId,natrue不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		synchronized (this) {
			
			Company company = companyService.getCompany(Integer.valueOf(companyId));
	
			if (company == null) {
				message.addProperty("message", "商家不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
	
			User user = userService.gettoken(token);
	
			if (user == null) {
				message.addProperty("message", "token验证失败");
				message.addProperty("isout", true);
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
	
			Goods good = goodsService.getGoods(Integer.valueOf(goodsId));
			if (good == null) {
				message.addProperty("message", "商品不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
	
			Shop shop = user.getShop();
			if (shop == null) {
				message.addProperty("message", "购物车为空");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
	
			List<ShopGoods> shopList = shop.getShopGoods();
			if (shopList.size() <= 0) {
				message.addProperty("message", "购物车里找不到商品");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			
			boolean flg = false;
			
			List<ShopGoods> list = shop.getShopGoods();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getNgoodsId() == good.getId()) {
					flg = true;
					list.get(i).setNumb(list.get(i).getNumb() - 1);
					list.set(i, list.get(i));
					shop.setShopGoods(list);
					shopService.saveORupdate(shop);
					if (list.get(i).getNumb() == 0) {
						shopgoodsService.delete(list.get(i).getId() + "");
					}
					
					if(!StringUtil.isEmpty(orderId)){
						List<Relating> listRelating = relatingService.getGoods(Integer.valueOf(goodsId), Integer.valueOf(companyId), Integer.valueOf(orderId));
						if(listRelating.size() > 0){
							Relating re = listRelating.get(0);
							if((re.getNumb() - 1) == 0){
								String relatid = listRelating.get(0).getId()+"";
								relatingService.delete(relatid);
							}else{
								Relating relatid =listRelating.get(0);
								int count = relatid.getNumb() - 1;
								relatid.setNumb(count);
								relatingService.saveORupdate(relatid);
							}
						}
					}
					
					break;
				}
			}
			
			if(!flg&& !StringUtil.isEmpty(orderId)){
				List<Relating> listRelating = relatingService.getGoods(Integer.valueOf(goodsId), Integer.valueOf(companyId), Integer.valueOf(orderId));
				if(listRelating.size() > 0){
					Relating re = listRelating.get(0);
					if((re.getNumb() - 1) == 0){
						String relatid = listRelating.get(0).getId()+"";
						relatingService.delete(relatid);
					}else{
						Relating relatid =listRelating.get(0);
						int count = relatid.getNumb() - 1;
						relatid.setNumb(count);
						relatingService.saveORupdate(relatid);
					}
				}
			}
		}
	
		message.addProperty("message", "减少购物车成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	@RequestMapping(params = "companyShop", method = RequestMethod.POST)
	public void companyShop(HttpServletRequest request,
			HttpServletResponse response) {

		String companyId = request.getParameter("comId");
		String token = request.getParameter("token");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(token)) {
			message.addProperty("message", "comId,token不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = companyService.getCompany(Integer.valueOf(companyId));

		if (company == null) {
			message.addProperty("message", "商家不存在");
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

		Shop shop = user.getShop();
		JSonGrid grid = new JSonGrid();
		if (shop != null) {
			List<ShopGoods> shopList = shopgoodsService.getshop(Integer
					.valueOf(companyId), user.getShop().getId());
			if (shopList.size() > 0) {
				grid.addProperties("success", true);
				for (int i = 0; i < shopList.size(); i++) {
					JSonGridRecord record = new JSonGridRecord();
					Goods goods = goodsService.getGoods(shopList.get(i)
							.getNgoodsId());
					record.addColumn("name", goods.getName());
					record.addColumn("num", shopList.get(i).getNumb());
					grid.addRecord(record);
				}

			} else {
				message.addProperty("message", "购物车空荡荡的快去添加商品吧");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
		} else {
			message.addProperty("message", "暂未创建购物车");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		message.addProperty("message", "添加购物车成功");
		message.addProperty("success", true);
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	@RequestMapping(params = "view", method = RequestMethod.POST)
	public void view(HttpServletRequest request, HttpServletResponse response) {

		String token = request.getParameter("token");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(token)) {
			message.addProperty("message", "id不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);

		User user = userService.gettoken(token);

		Shop shop = user.getShop();

		JSonTree ftree = new JSonTree();

		Map<String, Integer> map = new HashMap<String, Integer>();

		if (shop != null) {
			List<ShopGoods> shopGoodsList = shopgoodsService.getShop(shop
					.getId());
			JSonTreeNode node = null;
			for (ShopGoods ngoods : shopGoodsList) {
				Company company = companyService.getCompany(ngoods.getcId());
				if (map.get("id") != company.getId()) {
					node = new JSonTreeNode();

					node.addProperty("companyName", company.getName());
					node.addProperty("logo", company.getLogo());
					node.addProperty("id", company.getId());

					Goods goods = goodsService.getGoods(ngoods.getNgoodsId());

					JSonTreeNode cnode = new JSonTreeNode();
					cnode.addProperty("goodsName", goods.getName());
					cnode.addProperty("goodsId", goods.getId());
					cnode.addProperty("img", goods.getZoomUrl());
					cnode.addProperty("num", ngoods.getNumb());
					cnode.addProperty("price", goods.getPrice());
					cnode.addProperty("leaf", true);
					// cnode.addProperty("id", goods.getId());//TODO 规格

					node.addChild(cnode);

					ftree.addNode(node);

					map.put("id", company.getId());
				} else {
					Goods goods = goodsService.getGoods(ngoods.getNgoodsId());

					JSonTreeNode cnode = new JSonTreeNode();
					cnode.addProperty("goodsName", goods.getName());
					cnode.addProperty("goodsId", goods.getId());
					cnode.addProperty("img", goods.getZoomUrl());
					cnode.addProperty("num", ngoods.getNumb());
					cnode.addProperty("price", goods.getPrice());
					cnode.addProperty("leaf", true);
					// cnode.addProperty("id", goods.getId());//TODO 规格

					node.addChild(cnode);

					map.put("id", company.getId());
				}
			}

			new PushJson().outString(ftree.toJSonString(), response);
		}

	}

	@RequestMapping(params = "companyShopGoodView", method = RequestMethod.POST)
	public void companyShopGoodView(HttpServletRequest request,
			HttpServletResponse response) {
		String token = request.getParameter("token");
		String orderId = request.getParameter("orderId");
		JSonMessage message = new JSonMessage();
		JSonGrid grid = new JSonGrid();
		grid.addProperties("success", true);
		User user = userService.gettoken(token);
		if (user == null) {
			message.addProperty("message", "token验证失败");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}
		Order order = orderService.getOrder(Integer.valueOf(orderId));
		if (order == null) {
			message.addProperty("message", "订单不存在");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		int i = 0;
		List<Relating> relatingList = relatingService.getrelating(Integer
				.valueOf(orderId), Integer
				.valueOf(order.getCompanyId().getId()));
		for (Relating relating : relatingList) {
			Goods good = goodsService.getGoods(relating.getGoodsId());
			if (good == null) {
				message.addProperty("message", "商品已下架或不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}
			JSonGridRecord record = new JSonGridRecord();
			i++;
			record.addColumn("name", good.getName());
			record.addColumn("logo", good.getZoomUrl());
			record.addColumn("price", good.getPrice());
			record.addColumn("number", relating.getNumb());
			record.addColumn("subtotal", relating.getNumb()
					* Double.parseDouble(good.getPrice()));
			String natureIds = relating.getNatureId();

			if (natureIds != null) {
				String[] natureid = natureIds.split(",");
				StringBuilder natureinfo = new StringBuilder();
				for (String natures : natureid) {
					Nature nature = natureService.nature(Integer
							.valueOf(natures.toString()));
					if (nature == null) {
						message.addProperty("message", "商品属性已修改或不存在");
						message.addProperty("success", false);
						new PushJson().outString(message.toJSonString(),
								response);
						return;
					}
					natureinfo.append(nature.getAttributeId().getName() + ":"
							+ nature.getContent() + ";");
				}
				record.addColumn("natureinfo", natureinfo.toString());
				grid.addRecord(record);
			} else {
				grid.addRecord(record);
				continue;
			}
		}
		grid.addProperties("totalCount", i);
		new PushJson().outString(grid.toJSonString("list"), response);
	}

	// 批量添加购物车
	@RequestMapping(params = "batchSave", method = RequestMethod.POST)
	public void batchSave(HttpServletRequest request,
			HttpServletResponse response) {

		String companyId = request.getParameter("comId");
		String token = request.getParameter("token");
		String goodIdList = request.getParameter("goodIdList");
		String natrue = request.getParameter("natrue");
		String num = request.getParameter("num");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(token)
				|| StringUtil.isEmpty(goodIdList) || StringUtil.isEmpty(natrue)
				|| StringUtil.isEmpty(num)) {
			message.addProperty("message", "comId,token,goodId,natrue不能为空");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		if (!NumberUtils.isNumber(num)) {
			message.addProperty("message", "数量值不正确");
			message.addProperty("success", false);
			new PushJson().outString(message.toJSonString(), response);
			return;
		}

		Company company = companyService.getCompany(Integer.valueOf(companyId));

		if (company == null) {
			message.addProperty("message", "商家不存在");
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

		String[] goodsIds = goodIdList.split(",");
		for (String goodsId : goodsIds) {
			Goods good = goodsService.getGoods(Integer.valueOf(goodsId));
			if (good == null) {
				message.addProperty("message", "商品不存在");
				message.addProperty("success", false);
				new PushJson().outString(message.toJSonString(), response);
				return;
			}

			Shop shop = user.getShop();
			if (shop != null) {
				boolean flg = true;
				List<ShopGoods> shopList = shop.getShopGoods();
				if (shopList.size() > 0) {
					List<ShopGoods> list = shop.getShopGoods();
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).getNgoodsId() == good.getId()
								&& list.get(i).getNatrue().equals(natrue)) {
							list.get(i).setNumb(list.get(i).getNumb() + 1);
							list.set(i, list.get(i));
							shop.setShopGoods(list);
							shopService.saveORupdate(shop);
							flg = false;
						}
					}

					if (flg) {
						ShopGoods shopgoods = new ShopGoods();
						shopgoods.setNgoodsId(Integer.valueOf(goodsId));
						shopgoods.setNumb(Integer.valueOf(1));
						shopgoods.setNatrue(natrue);
						shopgoods.setcId(Integer.valueOf(companyId));
						shopgoods.setShop(shop);
						shopList.add(shopgoods);

						shop.setShopGoods(shopList);

						shopService.saveORupdate(shop);
					}
				} else {
					List<ShopGoods> newShopList = new ArrayList<ShopGoods>();
					ShopGoods shopgoods = new ShopGoods();
					shopgoods.setNgoodsId(Integer.valueOf(goodsId));
					shopgoods.setNumb(Integer.valueOf(1));
					shopgoods.setNatrue(natrue);
					shopgoods.setcId(Integer.valueOf(companyId));
					shopgoods.setShop(shop);
					newShopList.add(shopgoods);

					shop.setShopGoods(newShopList);

					shopService.saveORupdate(shop);
				}
			} else {
				Shop newShop = new Shop();
				newShop.setUserId(user);
				shopService.saveORupdate(newShop);

				List<ShopGoods> newShopList = new ArrayList<ShopGoods>();
				ShopGoods shopgoods = new ShopGoods();
				shopgoods.setNgoodsId(Integer.valueOf(goodsId));
				shopgoods.setNumb(Integer.valueOf(1));
				shopgoods.setNatrue(natrue);
				shopgoods.setcId(Integer.valueOf(companyId));
				shopgoods.setShop(newShop);
				newShopList.add(shopgoods);
				newShop.setShopGoods(newShopList);
				shopService.saveORupdate(newShop);

				user.setShop(newShop);
				userService.saveORupdate(user);
			}
		}

		message.addProperty("message", "添加购物车成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

	// 清空购物车
	@RequestMapping(params = "emptyShop", method = RequestMethod.POST)
	public void emptyShop(HttpServletRequest request,
			HttpServletResponse response) {

		String companyId = request.getParameter("cid");
		String token = request.getParameter("token");
		String orderId = request.getParameter("orderId");

		JSonMessage message = new JSonMessage();
		if (StringUtil.isEmpty(companyId) || StringUtil.isEmpty(token)) {
			message.addProperty("message", "缺少必要参数");
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
		
		shopgoodsService.delete(Integer.valueOf(companyId), user.getShop().getId());
		
		if(!StringUtil.isEmpty(orderId)){
			List<Relating> relist = relatingService.getrelating(Integer.valueOf(orderId));
			for(Relating re : relist){
				relatingService.delete(re.getId()+"");
			}
		}
	
		message.addProperty("message", "清空购物车成功");
		message.addProperty("success", true);
		new PushJson().outString(message.toJSonString(), response);
	}

}