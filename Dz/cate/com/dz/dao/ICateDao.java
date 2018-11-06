package com.dz.dao;

import java.util.List;

import com.dz.entity.Cate;
import com.dz.entity.Order;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface ICateDao {
 
	List<Cate> cateList(String sql);		//查询
	
	List<Order> cateRes(String reserveId);		//查询
	
	Cate getCate(int orderId);
	
	public Cate getCate(String tableNo, int cid, String name, String seat);
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final Cate cate);		//添加或修改信息
	
	Cate getCateInside(int insideId);
	
	List<Cate> searchPhone(String phone);//2018-11-03 @Tyy
}

 
