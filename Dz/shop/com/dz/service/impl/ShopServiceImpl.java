package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IShopDao;
import com.dz.entity.Goods;
import com.dz.entity.Shop;
import com.dz.service.IShopService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("shopService")
public class ShopServiceImpl implements IShopService{

	@Autowired
	IShopDao shopDao;

	@Override
	public List<Shop> shopList(Shop shop) {
		String sql = "SELECT o FROM Shop o";
		return shopDao.shopList(sql);
	}
	
	@Override
	public void delete(final String id) {
		shopDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Shop shop) {
		shopDao.saveORupdate(shop);
	}
	
	@Override
	public Shop getByShop(int userid) {
		return shopDao.getByShop(userid);
	}

	@Override
	public Shop getShop(int id) {
		return shopDao.getshop(id);
	}

}