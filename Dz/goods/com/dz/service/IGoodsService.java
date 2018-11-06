package com.dz.service;

import java.util.List;

import com.dz.entity.Goods;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IGoodsService {

	Goods getGoods(int id);//查询单个商品
	
	List<Goods> getCGoods(int companyid, int type);//按商家查询商品
	
	List<Goods> goodsList(int companyId, int ificationId);//按商家和商品分类查询商品 
	
	List<Goods> companyGoods(int companyId, int ificationId);//商家app	按商家和商品分类查询商品
	
	List<Goods> companyGoodsWm(int companyId, int ificationId,int type);//商家app	按商家和商品分类查询外卖下的查询商品

	public void saveORupdate(final Goods goods);//添加或修改信息
	
	public void update(final Goods goods);//添加或修改信息
	
	public void down(final int id, final int shelves);//商品上下架
	
	List<Goods> backGoods(int companyid, int type);//按商家查询商品
	
	List<Goods> computerGoodsWm(int companyId, int ificationId, int type);//   按商家和商家分类查询已上架的商品 ynw
	
	//管理后台
	List<Goods> bastGoods(Goods goods);//后台商品列表


}
