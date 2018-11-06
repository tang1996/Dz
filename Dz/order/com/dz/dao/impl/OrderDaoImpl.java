package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IOrderDao;
import com.dz.entity.Order;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("orderDao")
@SuppressWarnings("unchecked")
public class OrderDaoImpl implements IOrderDao {

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
	public List<Order> orderList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Order> orderList = query.list();
		return orderList;
	}

	@Override
	public List<Order> getStatus(String status) {
		String sql = "SELECT o FROM Order o WHERE o.orderStatus=:v_orderStatus";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_orderStatus", status);
		List<Order> orderList = query.list();
		return orderList;
	}

	@Override
	public List<Order> getStatus(int companyId, String status) {
		String sql = "SELECT o FROM Order o WHERE o.orderStatus=:v_orderStatus and o.companyId=:v_companyId order by o.addTime desc";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_orderStatus", status);
		query.setInteger("v_companyId", companyId);
		List<Order> orderList = query.list();
		return orderList;
	}

	@Override
	public List<Order> getDoing(int userId, int start, int limit) {
		String sql = "SELECT o FROM Order o where o.userId=:v_userId and o.orderStatus in ('paysuccess','doing','unusual','backBalance','finish','unreceiption') order by o.addTime desc";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userId);
		query.setFirstResult(start).setMaxResults(limit);
		List<Order> orderList = query.list();
		return orderList;
	}

	@Override
	public Order getOrder(int id) {
		String sql = "SELECT o FROM Order o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<Order> orderList = query.list();
		if (orderList.size() > 0) {
			return orderList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Order get(int userId, int companyId) {
		String sql = "SELECT o FROM Order o where o.companyId.id=:v_companyId and o.userId=:v_userId and o.payStatus = 0 order by o.addTime";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setInteger("v_userId", userId);
		List<Order> orderList = query.list();
		if (orderList.size() > 0) {
			return orderList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<Order> getAll(int userId, String status, int start, int limit) {
		String sql = "SELECT o FROM Order o where o.userId=:v_userId and o.orderStatus=:v_status order by o.addTime desc";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userId);
		query.setString("v_status", status);
		query.setFirstResult(start).setMaxResults(limit);
		List<Order> orderList = query.list();
		return orderList;
	}

	@Override
	public List<Order> getAll(int userId, int start, int limit) {
		String sql = "SELECT o FROM Order o where o.userId=:v_userId order by o.addTime desc";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userId);
		query.setFirstResult(start).setMaxResults(limit);
		List<Order> orderList = query.list();
		return orderList;
	}

	@Override
	public List<Order> getAllOrder(int companyId, String status, int start, int limit) {
		String sql = "SELECT o FROM Order o where o.companyId=:v_companyId and o.orderStatus=:v_orderStatus order by o.addTime DESC";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setString("v_orderStatus", status);
		query.setMaxResults(limit);
		query.setFirstResult(start);
		List<Order> orderList = query.list();
		return orderList;
	}

	// 订单总数
	@Override
	public Long count(int companyId) {
		String sql = "SELECT COUNT(o) FROM Order o WHERE o.companyId=:v_companyId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		List<Long> list = query.list();
		if (list.size() > 0) {
			Long count = list.get(0);
			return count;
		} else {
			return null;
		}
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM Order o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Order order) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(order);
	}

	// 订单追踪
	@Override
	public Order track(String id) {
		String sql = "SELECT o FROM Order o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		List<Order> list = query.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

	// 订单号查询
	@Override
	public Order getOrderNo(String orderNo) {
		String sql = "SELECT o FROM Order o WHERE o.orderNo=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", orderNo);
		List<Order> list = query.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

	@Override
	public Long getTypeOrder(int companyId, String startTime, String endTime) {
		String sql = "SELECT count(t.id) FROM OrderType t WHERE  t.orderId  in (SELECT id FROM Order o WHERE o.companyId=:v_companyId and o.orderStatus='finish' and o.orderType=1 and o.finishTime>=:v_staetDate and o.finishTime<=:v_endDate) and t.type != 'self' ";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setString("v_staetDate", startTime + " 00:00:00");
		query.setString("v_endDate", endTime + " 23:59:59");
		List<Long> orderList = query.list();
		if (orderList.size() > 0) {
			return (Long) orderList.get(0);
		} else {
			return 0l;
		}
	}

	@Override
	public Object[] getCount(int companyId, String date) {
		String sql = "SELECT count(o.id),sum(o.pay),sum(o.total) FROM Order o where o.companyId=:v_companyId and o.orderStatus='finish' and o.finishTime>=:v_staetDate and o.finishTime<=:v_endDate";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setString("v_staetDate", date + " 00:00:00");
		query.setString("v_endDate", date + " 23:59:59");
		List<Object[]> orderList = query.list();
		if (orderList.size() > 0) {
			Object[] count = orderList.get(0);
			return count;
		} else {
			return null;
		}
	}

	@Override
	public Object[] getCount(int companyId, String startTime, String endTime, String type) {
		String sql = "SELECT sum(o.pay),count(o.id),sum(o.total) FROM Order as o where o.companyId=:v_company and o.orderType=:v_type and o.orderStatus='finish' and o.finishTime >=:v_startTime and o.finishTime <=:v_endTime";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_company", companyId);
		query.setString("v_startTime", startTime + " 00:00:00");
		query.setString("v_endTime", endTime + " 23:59:59");
		query.setString("v_type", type);
		List<Object[]> orderList = query.list();
		if (orderList.size() > 0) {
			Object[] count = orderList.get(0);
			return count;
		} else {
			return null;
		}
	}

	@Override
	public List<Order> getList(int companyId, String startTime, String endTime, String type, String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setString("v_staetDate", startTime + " 00:00:00");
		query.setString("v_endDate", endTime + " 23:59:59");
		query.setString("v_orderType", type);
		List<Order> orderList = query.list();
		return orderList;
	}

	@Override
	public List<Order> getList(int companyId, String type, String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setString("v_orderType", type);
		List<Order> orderList = query.list();
		return orderList;
	}

	@Override
	public List<Order> getIsSend(int userId, int start, int limit) {
		String sql = "SELECT o FROM Order o where o.userId=:v_userId and o.isSend is null and o.orderStatus='finish' order by o.addTime desc";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userId);
		query.setFirstResult(start).setMaxResults(limit);
		List<Order> orderList = query.list();
		return orderList;
	}

	@Override
	public Object[] getUserCount(int userId, int companyId) {
		String sql = "SELECT count(id),sum(o.pay) FROM Order o where o.userId=:v_userId and o.companyId=:v_companyId and o.orderStatus='finish' Group By o.userId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userId);
		query.setInteger("v_companyId", companyId);
		List<Object[]> orderList = query.list();
		if (orderList.size() > 0) {
			Object[] obj = orderList.get(0);
			return obj;
		}
		return null;
	}

	// 后台管理
	// 用户列表
	@Override
	public List<Order> orderList(String sql, int start, int limit) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql).setFirstResult(start).setMaxResults(limit);
		List<Order> orderList = query.list();
		return orderList;
	}

	@Override // ynw
	public List<Order> getStatus(int comgpanyId, String startTime, String endTime, String status) {

		String sql = "";
		sql = "SELECT o FROM Order o  where o.companyId=:v_companyId and o.orderStatus=:v_orderStatus  and o.addTime between :v_startDate and :v_endDate";

		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", comgpanyId);
		query.setString("v_startDate", startTime);
		query.setString("v_endDate", endTime);
		query.setString("v_orderStatus", status);
		List<Order> orderList = query.list();
		return orderList;
	}

	@Override // 活跃用户订单 ynw
	public List<Object[]> activeUserOrder(String startTime, String endTime, int start, int limit) {
		String sql = "";
		sql = "SELECT count(u.id),sum(o.pay),u.userName,u.name,u.nickname,u.phone FROM User u,Order o where u.id=o.userId and u.isDistribution=0 and o.orderStatus='finish' and o.addTime>=:v_startDate and o.addTime<=:v_endDate GROUP BY u.id";

		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_startDate", startTime);
		query.setString("v_endDate", endTime);
		query.setFirstResult(start).setMaxResults(limit);
		List<Object[]> orderList = query.list();
		return orderList;
	}

	@Override /* ynw */
	public List<Object[]> activeUserOrder(String startTime, String endTime) {
		String sql = "";
		sql = "SELECT count(u.id),sum(o.pay),u.userName,u.name,u.nickname,u.phone FROM User u,Order o where u.id=o.userId and u.isDistribution=0 and o.orderStatus='finish' and o.addTime>=:v_startDate and o.addTime<=:v_endDate GROUP BY u.id";

		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_startDate", startTime);
		query.setString("v_endDate", endTime);
		List<Object[]> orderList = query.list();
		return orderList;
	}

	// 订单号查询
	@Override
	public List<Order> searchOrderNo(String orderNo) {// 2018-11-03 @Tyy
		String sql = "SELECT o FROM Order o WHERE o.orderNo like '%" + orderNo + "%'";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Order> list = query.list();
		return list;
	}

}