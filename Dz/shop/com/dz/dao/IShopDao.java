package com.dz.dao;

import java.util.List;

import com.dz.entity.Goods;
import com.dz.entity.Shop;

/**
 * 用户DAO
 * 
 * @author GJ
 * @date 2015年4月15日
 */
public interface IShopDao {

	List<Shop> shopList(String sql); // 查询

	public void delete(final String id); // 删除

	public void saveORupdate(final Shop meun); // 添加或修改信息

	Shop getshop(int id);
	
	Shop getByShop(int userid);
	
}
