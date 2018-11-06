package com.dz.service;

import java.util.List;

import com.dz.entity.OrderType;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IOrderTypeService {

	List<OrderType> orderTypeList(OrderType orderType);

	List<OrderType> getorderTypeList(String userId, String status);

	List<OrderType> getorderTypeList(String status);

	List<OrderType> getList(String status);

	OrderType getOrderType(int orderId);

	OrderType getOrderType(int orderId, String payStatus);

	public void delete(final String id);

	public void saveORupdate(final OrderType orderType);

	List<OrderType> getStatus();

	List<OrderType> getStatus(int companyId, String status);

	Object[] getMonCount(int userId, String date);

	Object[] getDayCount(int userId, String date);

	// 管理后台
	List<OrderType> getBastList(OrderType orderType);

	Object[] getBastCount(String startTime, String endTime, int userId);

	List<OrderType> getBastCountList(String startTime, String endTime, OrderType orderType);

	List<OrderType> getBastCountList(String startTime, String endTime, OrderType orderType, int start, int limit);

	List<OrderType> getBastList(String startTime, String endTime, OrderType orderType); // ynw

	Object[] getCount(int userId);

	Object[] getCount(int userId, String startTime, String endTime);

	List<OrderType> getBastList(OrderType orderType, int start, int limit); /* ynw */

}
