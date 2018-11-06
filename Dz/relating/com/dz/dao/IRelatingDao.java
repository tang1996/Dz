package com.dz.dao;

import java.util.List;

import com.dz.entity.Relating;

/**
 * 用户DAO
 * 
 * @author GJ
 * @date 2015年4月15日
 */
public interface IRelatingDao {

	List<Relating> relatingList(String sql); // 查询

	List<Relating> getrelating(int orderId, int companyId);

	List<Relating> getrelating(int orderId);

	Relating getRelating(int orderId, int goodsId);
	
	List<Relating> getinsertrelating(int insideOrder);
	
	List<Relating> getGoods(int goodid, int companyId, int orderId);
	
	public void delete(final String id); // 删除

	public void saveORupdate(final Relating relating); // 添加或修改信息

	public List<Relating> getMenus(int orderId, int goodsId, int cid);

	// 线下订单加减菜品 ynw
	Relating getinsideRelating(int orderId, int goodsId);	//ynw
	
	public List<Relating> getinsideMenus(int insideorderId, int goodsId, int cid);	//ynw
	
	List<Relating> getrelatingbyinsideID(int orderId);	//ynw
	
	public List<Relating> getrelatingbyinsideID(int orderId, int companyId);	//ynw

}
