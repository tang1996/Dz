package com.dz.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IOrderTypeDao;
import com.dz.entity.OrderType;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("OrderTypeDao")
@SuppressWarnings("unchecked")
public class OrderTypeDaoImpl implements IOrderTypeDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	// 用户列表
	@Override
	public List<OrderType> orderTypeList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<OrderType> orderTypeList = query.list();
		return orderTypeList;
	}

	@Override
	public List<OrderType> getorderTypeList(String userId, String status) {
		String sql = "SELECT o FROM OrderType o WHERE o.userId=:v_userId and o.status=:v_status";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_userId", userId);
		query.setString("v_status", status);
		List<OrderType> orderTypeList = query.list();
		return orderTypeList;
	}

	@Override
	public List<OrderType> getorderTypeList(String status) {
		String sql = "SELECT o FROM OrderType o WHERE o.status=:v_status";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_status", status);
		List<OrderType> orderTypeList = query.list();
		return orderTypeList;
	}

	// 为接单查询
	@Override
	public List<OrderType> getStatus() {
		String sql = "SELECT o FROM OrderType o WHERE o.status = 'unanswered'";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<OrderType> orderTypeList = query.list();
		return orderTypeList;
	}

	@Override
	public List<OrderType> getList(String status) {
		String sql = "SELECT o FROM OrderType o WHERE o.type='company' and o.status=:v_status";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_status", status);
		List<OrderType> orderTypeList = query.list();
		return orderTypeList;
	}

	// 为接单查询
	@Override
	public List<OrderType> getStatus(int orderId, String status) {
		String sql = "SELECT o FROM OrderType o WHERE o.orderId=:v_orderId and o.status=:v_status";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_orderId", orderId);
		query.setString("v_status", status);
		List<OrderType> orderTypeList = query.list();
		return orderTypeList;
	}

	public OrderType getOrderType(int orderId) {
		String sql = "SELECT o FROM OrderType o WHERE o.orderId=:v_orderId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_orderId", orderId);
		List<OrderType> orderTypeList = query.list();
		if (orderTypeList.size() > 0) {
			return orderTypeList.get(0);
		} else {
			return null;
		}
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM OrderType o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(OrderType orderType) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(orderType);
	}

	@Override
	public OrderType getOrderType(int orderId, String payStatus) {
		String sql = "SELECT o FROM OrderType o WHERE o.orderId=:v_orderId and o.status=:v_payStatus";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_orderId", orderId);
		query.setString("v_payStatus", payStatus);
		List<OrderType> orderTypeList = query.list();
		if (orderTypeList.size() > 0) {
			return orderTypeList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Object[] getMonCount(int userId, String date) {
		String sql = "SELECT count(o.id),sum(o.price) FROM OrderType o WHERE o.userId=:v_userId and o.status='finish' and o.shippingTime>=:v_staetDate and o.shippingTime<=:v_endDate";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userId);
		query.setString("v_staetDate", date + "-01 00:00:00");
		query.setString("v_endDate", date + "-31 23:59:59");
		List<Object[]> orderTypeList = query.list();
		if (orderTypeList.size() > 0) {
			Object[] count = orderTypeList.get(0);
			return count;
		} else {
			return null;
		}
	}

	@Override
	public Object[] getDayCount(int userId, String date) {
		String sql = "SELECT count(o.id),sum(o.price) FROM OrderType o WHERE o.userId=:v_userId and o.status='finish' and o.shippingTime>=:v_staetDate and o.shippingTime<=:v_endDate";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userId);
		query.setString("v_staetDate", date + " 00:00:00");
		query.setString("v_endDate", date + " 23:59:59");
		List<Object[]> orderTypeList = query.list();
		if (orderTypeList.size() > 0) {
			Object[] count = orderTypeList.get(0);
			return count;
		} else {
			return null;
		}
	}

	// 管理后台
	@Override
	public List<OrderType> getBastList(String sql) {

		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<OrderType> orderTypeList = query.list();
		return orderTypeList;
	}

	@Override
	public Object[] getBastCount(String startTime, String endTime, int userId) {
		String sql = "SELECT count(o.id),sum(o.price) FROM OrderType o WHERE o.userId=:v_userId and o.status='finish' and o.shippingTime>=:v_staetDate and o.shippingTime<=:v_endDate";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_staetDate", startTime + " 00:00:00");
		query.setString("v_endDate", endTime + " 23:59:59");
		query.setInteger("v_userId", userId);
		List<Object[]> orderTypeList = query.list();
		if (orderTypeList.size() > 0) {
			Object[] count = orderTypeList.get(0);
			return count;
		} else {
			return null;
		}
	}

	@Override
	public List<OrderType> getBastCountList(String startTime, String endTime, String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_staetDate", startTime + " 00:00:00");
		query.setString("v_endDate", endTime + " 23:59:59");
		List<OrderType> orderTypeList = query.list();
		return orderTypeList;
	}

	@Override
	public List<OrderType> getBastCountList(String startTime, String endTime, String sql, int start, int limit) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_staetDate", startTime + " 00:00:00");
		query.setString("v_endDate", endTime + " 23:59:59");
		query.setFirstResult(start).setMaxResults(limit);
		List<OrderType> orderTypeList = query.list();
		return orderTypeList;
	}

	@Override
	public Object[] getCount(int userId) {
		String sql = "SELECT count(o.id),sum(o.price) FROM OrderType o WHERE o.userId=:v_userId and o.status='finish' and o.shippingTime>=:v_date";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		String date = new SimpleDateFormat("yyyy-MM").format(new Date()) + "-01 00:00:00";
		query.setString("v_date", date);
		query.setInteger("v_userId", userId);
		List<Object[]> orderTypeList = query.list();
		if (orderTypeList.size() > 0) {
			Object[] obj = orderTypeList.get(0);
			return obj;
		} else {
			return null;
		}
	}

	@Override
	public Object[] getCount(int userId, String startTime, String endTime) {
		String sql = "SELECT count(o.id),sum(o.price) FROM OrderType o WHERE o.userId=:v_userId and o.status='finish' and o.shippingTime>=:v_startTime and o.shippingTime<=:v_endTime";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_startTime", startTime);
		query.setString("v_endTime", endTime);
		query.setInteger("v_userId", userId);
		List<Object[]> orderTypeList = query.list();
		if (orderTypeList.size() > 0) {
			Object[] obj = orderTypeList.get(0);
			return obj;
		} else {
			return null;
		}
	}

	@Override /* ynw */
	public List<OrderType> getBastList(String sql, int start, int limit) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql).setFirstResult(start).setMaxResults(limit);
		List<OrderType> orderTypeList = query.list();
		return orderTypeList;
	}

	@Override // ynw
	public List<OrderType> getBastList(String startTime, String endTime, String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_staetDate", startTime);
		query.setString("v_endDate", endTime);
		List<OrderType> orderTypeList = query.list();
		return orderTypeList;
	}

}