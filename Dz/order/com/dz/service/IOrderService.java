package com.dz.service;

import java.util.List;

import com.dz.entity.Order;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IOrderService {

	List<Order> orderList(Order user);

	Order getOrder(int id);

	public Order getOrderNo(String orderNo);

	public void delete(final String id);

	public void saveORupdate(final Order user);

	Order track(String orderNo);

	Order get(int userId, int companyId);

	List<Order> getAll(int userId, String status, int start, int limit);

	List<Order> getIsSend(int userId, int start, int limit);

	List<Order> getAll(int userId, int start, int limit);

	List<Order> getDoing(int userId, int start, int limit);

	List<Order> getAllOrder(int companyId, String status, int start, int limit);

	List<Order> getStatus(String status);

	List<Order> getStatus(int comgpanyId, String status);

	Long count(int companyId);

	Long getTypeOrder(int companyId, String startTime, String endTime);

	Object[] getCount(int comgpanyId, String date);

	Object[] getCount(int comgpanyId, String startTime, String endTime, String type);

	List<Order> getList(int companyId, String startTime, String endTime, String type, String classify);

	List<Order> getList(int companyId, String type, String classify);

	Object[] getUserCount(int userId, int companyId);

	// 管理后台
	List<Order> orderList(Order order, int start, int limit);

	List<Object[]> activeUserOrder(String startTime, String endTime, int start, int limit); // ynw 活跃用户

	List<Object[]> activeUserOrder(String startTime, String endTime); /* ynw */
	
	List<Order> getStatus(int comgpanyId, String startTime, String endTime, String status);	//ynw
	
	List<Order> searchOrderNo(String orderNo);//2018-11-03 @Tyy
	
}
