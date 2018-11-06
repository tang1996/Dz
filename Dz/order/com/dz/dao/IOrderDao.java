package com.dz.dao;

import java.util.List;

import com.dz.entity.Order;


/**
 * 用户钱包DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IOrderDao {
 
	List<Order> orderList(String sql);
	
	Order getOrder(int id);
	
	public void delete(final String id) ;
	
	public void saveORupdate(final Order order);
	
	public Order track(String id);
	
	Order get(int userId, int companyId);
	
	List<Order> getAll(int userId, String status, int start, int limit);
	
	List<Order> getIsSend(int userId, int start, int limit) ;
	
	List<Order> getAll(int userId, int start, int limit);
	
	List<Order> getDoing(int userId, int start, int limit);
	
	List<Order> getAllOrder(int companyId, String status, int start, int limit);
	
	List<Order> getStatus(String status);
	
	List<Order> getStatus(int companyId, String status);
	
	public Long count(int companyId);
	
	public Order getOrderNo(String id) ;
	
	Long getTypeOrder(int companyId, String startTime, String endTime);
	
	Object[] getCount(int companyId, String date);
	
	Object[] getCount(int companyId, String startTime, String endTime, String type);
	
	List<Order> getList(int companyId, String startTime, String endTime, String type, String sql);
	
	List<Order> getList(int companyId, String type, String sql);
	
	Object[] getUserCount(int userId, int companyId);
	
	//管理后台
	List<Order> orderList(String sql, int start, int limit);
	
	List<Object[]> activeUserOrder(String startTime,String endTime,int start, int limit);		//ynw 活跃用户
	
	List<Object[]> activeUserOrder(String startTime,String endTime);		/*ynw*/
	
	List<Order> getStatus(int comgpanyId, String startTime, String endTime, String status);	//ynw
	
	List<Order> searchOrderNo(String orderNo);//2018-11-03 @Tyy
	
}

 
