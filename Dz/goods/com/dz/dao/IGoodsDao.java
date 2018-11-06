package com.dz.dao;

import java.util.List;

import com.dz.entity.Goods;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IGoodsDao {
 
	List<Goods> goodsList(String sql);		//查询
	
	List<Goods> goodsList(int companyId, int ificationId);//按商家和商家分类查询
	
	List<Goods> companyGoods(int companyId, int ificationId);//商家app   按商家和商家分类查询
	
	List<Goods> companyGoodsWm(int companyId, int ificationId, int type);//商家app   按商家和商家分类查询
	
	public void down(final int id, final int shelves);		//商品上下架
	
	public void saveORupdate(final Goods goods);		//添加或修改信息
	
	public void update(final Goods goods);		//添加或修改信息
	
	Goods getGoods(int id);//查询单个商品

	List<Goods> getCGoods(int companyid ,int type);//按商家查询商品
	
	List<Goods> backGoods(int companyid ,int type);//按商家查询商品
	
	List<Goods> computerGoodsWm(int companyId, int ificationId, int type);//   按商家和商家分类查询已上架的商品 ynw
	
	//管理后台
	List<Goods> bastGoods(String sql);//后台商品列表
	
}

 
