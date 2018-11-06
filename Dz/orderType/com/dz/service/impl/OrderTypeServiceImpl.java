package com.dz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IOrderTypeDao;
import com.dz.entity.OrderType;
import com.dz.service.IOrderTypeService;
import com.dz.util.StringUtil;

/**
 * 登陆业务实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Transactional(readOnly = false)
@Service("orderTypeService")
public class OrderTypeServiceImpl implements IOrderTypeService {

	@Autowired
	IOrderTypeDao orderTypeDao;

	@Override
	public List<OrderType> orderTypeList(OrderType orderType) {
		String sql = "SELECT o FROM OrderType o WHERE 1=1";
		return orderTypeDao.orderTypeList(sql);
	}

	@Override
	public List<OrderType> getorderTypeList(String userId, String status) {
		return orderTypeDao.getorderTypeList(userId, status);
	}

	@Override
	public List<OrderType> getorderTypeList(String status) {
		return orderTypeDao.getorderTypeList(status);
	}

	@Override
	public List<OrderType> getList(String status) {
		return orderTypeDao.getList(status);
	}

	@Override
	public OrderType getOrderType(int orderId) {
		return orderTypeDao.getOrderType(orderId);
	}

	@Override
	public void delete(final String id) {
		orderTypeDao.delete(id);
	}

	@Override
	public void saveORupdate(final OrderType orderType) {
		orderTypeDao.saveORupdate(orderType);
	}

	@Override
	public List<OrderType> getStatus() {
		return orderTypeDao.getStatus();
	}

	@Override
	public List<OrderType> getStatus(int companyId, String status) {
		return orderTypeDao.getStatus(companyId, status);
	}

	@Override
	public OrderType getOrderType(int orderId, String payStatus) {
		return orderTypeDao.getOrderType(orderId, payStatus);
	}

	@Override
	public Object[] getMonCount(int userId, String date) {
		return orderTypeDao.getMonCount(userId, date);
	}

	@Override
	public Object[] getDayCount(int userId, String date) {
		return orderTypeDao.getDayCount(userId, date);
	}

	// 管理后台
	@Override
	public List<OrderType> getBastList(OrderType orderType) {
		String sql = "SELECT o FROM OrderType o WHERE o.type='company'";
		String newSql = " and o.userId IS NOT NULL";
		if (orderType.getUserId() != null) {
			if (!StringUtil.isEmpty(orderType.getUserId().getId() + "")) {
				newSql = " and o.userId=" + orderType.getUserId().getId();
			}
		}
		sql = sql + newSql + " order by o.id DESC";
		return orderTypeDao.getBastList(sql);
	}

	@Override
	public Object[] getBastCount(String startTime, String endTime, int userId) {
		return orderTypeDao.getBastCount(startTime, endTime, userId);
	}

	@Override
	public List<OrderType> getBastCountList(String startTime, String endTime, OrderType orderType) {
		String sql = "SELECT o FROM OrderType o WHERE o.type='company' and o.status='finish' "
				+ "and o.shippingTime>=:v_staetDate and o.shippingTime<=:v_endDate";
		String newSql = " and o.userId IS NOT NULL";
		if (orderType.getUserId() != null) {
			if (!StringUtil.isEmpty(orderType.getUserId().getId() + "")) {
				newSql = " and o.userId=" + orderType.getUserId().getId();
			}
		}
		sql = sql + newSql + " group by o.userId";
		return orderTypeDao.getBastCountList(startTime, endTime, sql);
	}

	@Override
	public List<OrderType> getBastCountList(String startTime, String endTime, OrderType orderType, int start,
			int limit) {
		String sql = "SELECT o FROM OrderType o WHERE o.type='company' and o.status='finish' "
				+ "and o.shippingTime>=:v_staetDate and o.shippingTime<=:v_endDate";
		String newSql = " and o.userId IS NOT NULL";
		if (orderType.getUserId() != null) {
			if (!StringUtil.isEmpty(orderType.getUserId().getId() + "")) {
				newSql = " and o.userId=" + orderType.getUserId().getId();
			}
		}
		sql = sql + newSql + " group by o.userId";
		return orderTypeDao.getBastCountList(startTime, endTime, sql, start, limit);
	}

	@Override
	public Object[] getCount(int userId) {
		return orderTypeDao.getCount(userId);
	}

	@Override
	public Object[] getCount(int userId, String startTime, String endTime) {
		return orderTypeDao.getCount(userId, startTime, endTime);
	}
	
	@Override	/*ynw*/
	public List<OrderType> getBastList(OrderType orderType, int start, int limit) {
		String sql = "SELECT o FROM OrderType o WHERE o.type='company'";
		String newSql = " and o.userId IS NOT NULL";
		if (orderType.getUserId() != null) {
			if (!StringUtil.isEmpty(orderType.getUserId().getId() + "")) {
				newSql = " and o.userId=" + orderType.getUserId().getId();
			}
		}
		sql = sql + newSql + " order by o.id DESC";
		return orderTypeDao.getBastList(sql, start, limit);
	}

	@Override	//ynw xm
	public List<OrderType> getBastList(String startTime, String endTime,
			OrderType orderType) {
		String sql = "SELECT o FROM OrderType o WHERE o.type='company' and o.status='finish' "
			+ "and o.shippingTime>=:v_staetDate and o.shippingTime<=:v_endDate";
		String newSql = "";
		if (orderType.getUserId() != null) {
			if (!StringUtil.isEmpty(orderType.getUserId().getId() + "")) {
				newSql = " and o.userId=" + orderType.getUserId().getId();
			}
		}
		sql = sql + newSql;
		return orderTypeDao.getBastList(startTime, endTime, sql);
	}

}