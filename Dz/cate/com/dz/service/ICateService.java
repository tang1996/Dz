package com.dz.service;

import java.util.List;

import com.dz.entity.Cate;
import com.dz.entity.Order;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface ICateService {

	List<Cate> cateList(Cate cate);
	
	List<Order> cateRes(String reserveId);
	
	Cate getCate(int orderId);
	
	Cate getCateInside(int insideId);
	
	Cate getCate(String tableNo, int cid, String name, String seat);
	
	public void delete(final String id);

	public void saveORupdate(final Cate cate);
	
	List<Cate> searchPhone(String phone);//2018-11-03 @Tyy
	
}
