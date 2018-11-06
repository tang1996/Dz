package com.dz.dao;

import java.util.List;

import com.dz.entity.Reserve;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IReserveDao {
 
	List<Reserve> reserveList(String sql);		//查询
	
	List<Reserve> getReserve(int companyId,String seat);
	
	List<Reserve> getAllReserve(int companyId,String seat);
	
	Reserve getTable(String tableNo);
	
	Reserve find(int id);
	
	Reserve getTable(String tableNo, int companyId);
	
	Reserve getTable(String tableNo, int companyId, String seat);
	
	Reserve getTable(String tableNo, int companyId, String seat, String name);
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final Reserve reserve);		//添加或修改信息
	
	List<Reserve> getReserveForTwo(int companyId,String seat);	//ynw
	
	List<Reserve> getReserve(int companyId, String seat, String status); // ynw

	
}

 
