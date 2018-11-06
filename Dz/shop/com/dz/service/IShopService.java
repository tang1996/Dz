package com.dz.service;

import java.util.List;

import com.dz.entity.Goods;
import com.dz.entity.Shop;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 * 
 */

public interface IShopService {
	List<Shop> shopList(Shop shop);

	public void delete(final String id);

	public void saveORupdate(final Shop shop);
	
	public Shop getShop(int id);
	
	public Shop getByShop(int userid);
}
