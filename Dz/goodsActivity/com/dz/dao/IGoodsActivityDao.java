package com.dz.dao;

import java.util.List;

import com.dz.entity.GoodsActivity;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IGoodsActivityDao {
 
	List<GoodsActivity> goodsActivityList(String sql);		//查询
	
	List<GoodsActivity> getList(Integer id);		//通过活动id查询商家列表
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final GoodsActivity goodsActivity);		//添加或修改信息
	
}

 
