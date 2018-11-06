package com.dz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IOrderDao;
import com.dz.entity.Order;
import com.dz.service.IOrderService;
import com.dz.util.StringUtil;

@Transactional(readOnly = false)
@Service("orderService")
public class OrderServiceImpl implements IOrderService {

	@Autowired
	IOrderDao orderDao;

	@Override
	public List<Order> orderList(Order order) {
		String sql = "SELECT o FROM Order o WHERE 1=1";
		return orderDao.orderList(sql);
	}

	@Override
	public List<Order> getStatus(String status) {
		return orderDao.getStatus(status);
	}

	@Override
	public List<Order> getStatus(int companyId, String status) {
		return orderDao.getStatus(companyId, status);
	}

	@Override
	public List<Order> getDoing(int userId, int start, int limit) {
		return orderDao.getDoing(userId, start, limit);
	}

	@Override
	public Order getOrder(int id) {
		return orderDao.getOrder(id);
	}

	public Order getOrderNo(String orderNo) {
		return orderDao.getOrderNo(orderNo);
	}

	@Override
	public void delete(final String id) {
		orderDao.delete(id);
	}

	@Override
	public void saveORupdate(final Order user) {
		orderDao.saveORupdate(user);
	}

	@Override
	public Order get(int userId, int companyId) {
		return orderDao.get(userId, companyId);
	}

	@Override
	public List<Order> getAll(int userId, String status, int start, int limit) {
		return orderDao.getAll(userId, status, start, limit);
	}

	@Override
	public List<Order> getAll(int userId, int start, int limit) {
		return orderDao.getAll(userId, start, limit);
	}

	@Override
	public List<Order> getAllOrder(int companyId, String status, int start, int limit) {
		return orderDao.getAllOrder(companyId, status, start, limit);
	}

	@Override
	public Order track(String id) {
		return orderDao.track(id);
	}

	@Override
	public Long count(int companyId) {
		return orderDao.count(companyId);
	}

	@Override
	public Object[] getCount(int companyId, String date) {
		return orderDao.getCount(companyId, date);
	}

	@Override
	public Object[] getCount(int companyId, String startTime, String endTime, String type) {
		return orderDao.getCount(companyId, startTime, endTime, type);
	}

	@Override
	public Long getTypeOrder(int companyId, String startTime, String endTime) {
		return orderDao.getTypeOrder(companyId, startTime, endTime);
	}

	@Override
	public List<Order> getList(int companyId, String startTime, String endTime, String type, String classify) {
		String sql = "SELECT o FROM Order o WHERE o.companyId=:v_companyId and o.addTime>=:v_staetDate and o.addTime<=:v_endDate and o.orderType=:v_orderType";
		if (classify.equals("finish")) {
			sql = sql + " and o.orderStatus='finish'";// and o.isAccount=1";
		}
		if (classify.equals("doing")) {
			sql = sql + " and o.orderStatus='doing'";
		}
		if (classify.equals("cancel")) {
			sql = sql + " and o.orderStatus='finish' and o.isAccount=4";
		}
		sql = sql + " order by o.addTime desc";
		return orderDao.getList(companyId, startTime, endTime, type, sql);
	}

	@Override
	public List<Order> getList(int companyId, String type, String classify) {
		String sql = "SELECT o FROM Order o WHERE o.companyId=:v_companyId and o.orderType=:v_orderType";
		if (classify.equals("finish")) {
			sql = sql + " and o.orderStatus='finish' and o.isAccount=1";
		}
		if (classify.equals("doing")) {
			sql = sql + " and o.orderStatus='doing'";
		}
		if (classify.equals("cancel")) {
			sql = sql + " and o.orderStatus='finish' and o.isAccount=4";
		}
		sql = sql + " order by o.addTime desc";
		return orderDao.getList(companyId, type, sql);
	}

	@Override
	public List<Order> getIsSend(int userId, int start, int limit) {
		return orderDao.getIsSend(userId, start, limit);
	}

	@Override
	public Object[] getUserCount(int userId, int companyId) {
		return orderDao.getUserCount(userId, companyId);
	}
	
	//后台管理
	@Override
	public List<Order> orderList(Order order, int start, int limit) {
		String sql = "SELECT o FROM Order o WHERE 1=1";
		if(!StringUtil.isEmpty(order.getOrderType())){
			sql = sql + " and o.orderType = " + order.getOrderType();
		}
		if(order.getCompanyId() != null){
			sql = sql + " and o.companyId = " + order.getCompanyId().getId();
		}
		if(!StringUtil.isEmpty(order.getOrderNo())){
			sql = sql + " and o.orderNo = '" + order.getOrderNo() + "'";
		}
		if(order.getUserId() != null){
			sql = sql + " and o.userId = " + order.getUserId().getId();
		}
		sql = sql + " order by o.addTime DESC";
		return orderDao.orderList(sql, start, limit);
	}
	
	@Override	//ynw
	public List<Object[]> activeUserOrder(String startTime, String endTime,
			int start, int limit) {
		return orderDao.activeUserOrder(startTime, endTime, start, limit);
	}
	
	@Override	/*ynw*/
	public List<Object[]> activeUserOrder(String startTime, String endTime){
		return orderDao.activeUserOrder(startTime, endTime);
	}

	@Override	//ynw
	public List<Order> getStatus(int comgpanyId, String startTime,
			String endTime, String status) {
		return orderDao.getStatus(comgpanyId, startTime, endTime, status);
	}
	
	public List<Order> searchOrderNo(String orderNo) {//2018-11-03 @Tyy
		return orderDao.searchOrderNo(orderNo);
	}
	
}
