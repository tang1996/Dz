package com.dz.service;

import java.util.List;

import com.dz.entity.Relating;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IRelatingService {

	List<Relating> relatingList(Relating relating);
	
	List<Relating> getrelating(int orderId, int companyId);
	
	List<Relating> getGoods(int goodid, int companyId, int orderId);
	
	List<Relating> getrelating(int orderId);

	List<Relating> getinsertrelating(int insideOrder);
	
	Relating getRelating(int orderId, int goodsId);

	public void delete(final String id);

	public void saveORupdate(final Relating relating);
	
	public List<Relating> getMenus(int orderId,int goodsId, int cid);
	
	// 线下订单加减菜品
	Relating getinsideRelating(int orderId, int goodsId);	//ynw
	public List<Relating> getinsideMenus(int insideorderId, int goodsId, int cid);	//ynw
	List<Relating> getrelatingbyinsideID(int orderId);	//ynw
	public List<Relating> getrelatingbyinsideID(int orderId, int companyId);	//ynw

}
