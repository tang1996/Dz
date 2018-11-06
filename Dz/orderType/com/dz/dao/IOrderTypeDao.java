package com.dz.dao;

import java.util.List;

import com.dz.entity.OrderType;

/**
 * 用户DAO
 * 
 * @author GJ
 * @date 2015年4月15日
 */
public interface IOrderTypeDao {

	List<OrderType> orderTypeList(String sql); // 查询

	List<OrderType> getorderTypeList(String userId, String status);

	List<OrderType> getorderTypeList(String status);

	List<OrderType> getStatus();

	List<OrderType> getStatus(int orderId, String status);

	List<OrderType> getList(String status);

	OrderType getOrderType(int orderId);

	public void delete(final String id); // 删除

	public void saveORupdate(final OrderType orderType); // 添加或修改信息

	OrderType getOrderType(int orderId, String payStatus);

	Object[] getMonCount(int userId, String date);

	Object[] getDayCount(int userId, String date);

	// 管理后台
	List<OrderType> getBastList(String sql);

	Object[] getBastCount(String startTime, String endTime, int userId);

	List<OrderType> getBastCountList(String startTime, String endTime, String sql);

	List<OrderType> getBastCountList(String startTime, String endTime, String sql, int start, int limit);

	List<OrderType> getBastList(String startTime, String endTime, String sql); // ynw

	Object[] getCount(int userId);

	List<OrderType> getBastList(String sql, int start, int limit); /* ynw */

	Object[] getCount(int userId, String startTime, String endTime);

}
