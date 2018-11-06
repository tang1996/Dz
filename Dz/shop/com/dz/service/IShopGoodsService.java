package com.dz.service;

import java.util.List;

import com.dz.entity.ShopGoods;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 * 
 */

public interface IShopGoodsService {

	public void delete(final String id);
	
	public List<ShopGoods> getShop(int id);
	
	public List<ShopGoods> getshop(int cid, int shopid);
	
	public void delete(int cid, int shopid) ;
}
