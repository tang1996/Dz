package com.dz.dao;

import java.util.List;

import com.dz.entity.InsideOrder;

/**
 * 用户钱包DAO
 * 
 * @author GJ
 * @date 2015年4月15日
 */
public interface IInsideOrderDao {

	List<InsideOrder> orderList(String sql);

	InsideOrder getInsideOrder(int id);

	public void delete(final String id);

	public void saveORupdate(final InsideOrder order);

	public InsideOrder track(String id);

	InsideOrder get(int userId, int companyId);

	List<InsideOrder> getAll(int userId, String status, int start, int limit);

	List<InsideOrder> getIsSend(int userId, int start, int limit);

	List<InsideOrder> getAll(int userId, int start, int limit);

	List<InsideOrder> getDoing(int userId, int start, int limit);

	List<InsideOrder> getAllInsideOrder(int companyId, String status, int start, int limit);

	List<InsideOrder> getStatus(String status);

	List<InsideOrder> getStatus(int companyId, String status);

	public Long count(int companyId);

	public InsideOrder getInsideOrderNo(String id);

	Long getTypeInsideOrder(int companyId, String startTime, String endTime);

	Object[] getCount(int companyId, String date);

	Object[] getCount(int companyId, String startTime, String endTime, String type);

	List<InsideOrder> getList(int companyId, String startTime, String endTime, String sql);

	List<InsideOrder> getList(int companyId, String type, String sql);

	public List<InsideOrder> getInsideOrderByThree(int companyId, String seat); // ynw

	List<InsideOrder> getBaseList(int companyId, String startTime, String endTime);// 线下订单列表

	Object[] getCount(int companyId, String startTime, String endTime);// 线下订单统计

}
