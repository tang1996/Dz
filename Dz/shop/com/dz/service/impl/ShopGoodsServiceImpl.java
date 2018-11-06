package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IShopGoodsDao;
import com.dz.entity.ShopGoods;
import com.dz.service.IShopGoodsService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("shopgoodsService")
public class ShopGoodsServiceImpl implements IShopGoodsService{

	@Autowired
	IShopGoodsDao shopgoodsDao;

	@Override
	public void delete(final String id) {
		shopgoodsDao.delete(id);
	}

	@Override
	public List<ShopGoods> getShop(int id) {
		return shopgoodsDao.getshop(id);
	}
	@Override
	public List<ShopGoods> getshop(int cid, int shopid){
		return shopgoodsDao.getshop(cid, shopid);
	}

	@Override
	public void delete(int cid, int shopid) {
		shopgoodsDao.delete(cid, shopid);
	}

}