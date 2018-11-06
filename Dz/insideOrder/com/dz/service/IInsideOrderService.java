package com.dz.service;


import java.util.List;

import com.dz.entity.InsideOrder;


/**
 * 通用dao，包括基本的CRUD方法
 * @author Ljh
 *
 */

public interface IInsideOrderService {
 
	List<InsideOrder> orderList(InsideOrder user);
	
	InsideOrder getInsideOrder(int id);
	
	public InsideOrder getInsideOrderNo(String id) ;
	
	public void delete(final String id);
	
	public void saveORupdate(final InsideOrder user);
	
	InsideOrder track(String orderNo);
	
	InsideOrder get(int userId,int companyId);
	
	List<InsideOrder> getAll(int userId, String status, int start, int limit);
	
	 List<InsideOrder> getIsSend(int userId, int start, int limit) ;
	
	List<InsideOrder> getAll(int userId, int start, int limit);
	
	List<InsideOrder> getDoing(int userId, int start, int limit);
	
	List<InsideOrder> getAllInsideOrder(int companyId, String status, int start, int limit);

	List<InsideOrder> getStatus(String status);
	
	List<InsideOrder> getStatus(int comgpanyId, String status);
	
	Long count(int companyId);
	
	Long getTypeInsideOrder(int companyId, String startTime, String endTime);
	
	Object[] getCount(int comgpanyId, String date);

	Object[] getCount(int comgpanyId, String startTime, String endTime, String type);

	List<InsideOrder> getList(int companyId, String startTime, String endTime, String classify);

	List<InsideOrder> getList(int companyId, String type, String classify);

	public List<InsideOrder> getInsideOrderByThree(int companyId, String seat);

	List<InsideOrder> getBaseList(int companyId, String startTime, String endTime);//线下订单列表

	Object[] getCount(int companyId, String startTime, String endTime);//线下订单统计
	
}
 
