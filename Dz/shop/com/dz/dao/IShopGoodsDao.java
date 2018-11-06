package com.dz.dao;

import java.util.List;

import com.dz.entity.ShopGoods;

/**
 * 用户DAO
 * 
 * @author GJ
 * @date 2015年4月15日
 */
public interface IShopGoodsDao {

	void delete(final String id); // 删除

	List<ShopGoods> getshop(int id);
	
	void delete(int cid, int shopid) ;
	
	List<ShopGoods> getshop(int cid, int shopid);
	
}
