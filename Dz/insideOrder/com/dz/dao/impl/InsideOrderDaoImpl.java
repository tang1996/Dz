package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IInsideOrderDao;
import com.dz.entity.InsideOrder;
import com.dz.entity.Reserve;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("insideOrderDao")
@SuppressWarnings("unchecked")
public class InsideOrderDaoImpl implements IInsideOrderDao {

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
	public List<InsideOrder> orderList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<InsideOrder> orderList = query.list();
		return orderList;
	}

	@Override
	public List<InsideOrder> getStatus(String status) {
		String sql = "SELECT o FROM InsideOrder o WHERE o.orderStatus=:v_orderStatus";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_orderStatus", status);
		List<InsideOrder> orderList = query.list();
		return orderList;
	}

	@Override
	public List<InsideOrder> getStatus(int companyId, String status) {
		String sql = "SELECT o FROM InsideOrder o WHERE o.orderStatus=:v_orderStatus and o.companyId=:v_companyId order by o.addTime desc";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_orderStatus", status);
		query.setInteger("v_companyId", companyId);
		List<InsideOrder> orderList = query.list();
		return orderList;
	}

	@Override
	public List<InsideOrder> getDoing(int userId, int start, int limit) {
		String sql = "SELECT o FROM InsideOrder o where o.userId=:v_userId and o.orderStatus in ('paysuccess','doing','unusual','backBalance','finish','unreceiption') order by o.addTime desc";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userId);
		query.setFirstResult(start).setMaxResults(limit);
		List<InsideOrder> orderList = query.list();
		return orderList;
	}

	@Override
	public InsideOrder getInsideOrder(int id) {
		String sql = "SELECT o FROM InsideOrder o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<InsideOrder> orderList = query.list();
		if (orderList.size() > 0) {
			return orderList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public InsideOrder get(int userId, int companyId) {
		String sql = "SELECT o FROM InsideOrder o where o.companyId.id=:v_companyId and o.userId=:v_userId and o.payStatus = 0 order by o.addTime";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setInteger("v_userId", userId);
		List<InsideOrder> orderList = query.list();
		if (orderList.size() > 0) {
			return orderList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<InsideOrder> getAll(int userId, String status, int start, int limit) {
		String sql = "SELECT o FROM InsideOrder o where o.userId=:v_userId and o.orderStatus=:v_status order by o.addTime desc";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userId);
		query.setString("v_status", status);
		query.setFirstResult(start).setMaxResults(limit);
		List<InsideOrder> orderList = query.list();
		return orderList;
	}

	@Override
	public List<InsideOrder> getAll(int userId, int start, int limit) {
		String sql = "SELECT o FROM InsideOrder o where o.userId=:v_userId order by o.addTime desc";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userId);
		query.setFirstResult(start).setMaxResults(limit);
		List<InsideOrder> orderList = query.list();
		return orderList;
	}

	@Override
	public List<InsideOrder> getAllInsideOrder(int companyId, String status, int start, int limit) {
		String sql = "SELECT o FROM InsideOrder o where o.companyId=:v_companyId and o.orderStatus=:v_orderStatus order by o.addTime DESC";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setString("v_orderStatus", status);
		query.setMaxResults(limit);
		query.setFirstResult(start);
		List<InsideOrder> orderList = query.list();
		return orderList;
	}

	// 订单总数
	@Override
	public Long count(int companyId) {
		String sql = "SELECT COUNT(o) FROM InsideOrder o WHERE o.companyId=:v_companyId";
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
		String sql = "DELETE FROM InsideOrder o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(InsideOrder order) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(order);
	}

	// 订单追踪
	@Override
	public InsideOrder track(String id) {
		String sql = "SELECT o FROM InsideOrder o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		List<InsideOrder> list = query.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

	// 订单号查询
	@Override
	public InsideOrder getInsideOrderNo(String id) {
		String sql = "SELECT o FROM InsideOrder o WHERE o.orderNo=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		List<InsideOrder> list = query.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

	@Override
	public Long getTypeInsideOrder(int companyId, String startTime, String endTime) {
		String sql = "SELECT count(t.id) FROM InsideOrderType t WHERE  t.orderId  in (SELECT id FROM InsideOrder o WHERE o.companyId=:v_companyId and o.orderStatus='finish' and o.orderType=1 and o.finishTime>=:v_staetDate and o.finishTime<=:v_endDate) and t.type != 'self' ";
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
		String sql = "SELECT count(o.id),sum(o.pay),sum(o.total) FROM InsideOrder o where o.companyId=:v_companyId and o.orderStatus='finish' and o.finishTime>=:v_staetDate and o.finishTime<=:v_endDate";
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
		String sql = "SELECT sum(o.pay),count(o.id),sum(o.total) FROM InsideOrder as o where o.companyId=:v_company and o.orderType=:v_type and o.orderStatus='finish' and o.finishTime >=:v_startTime and o.finishTime <=:v_endTime";
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
	public List<InsideOrder> getList(int companyId, String startTime, String endTime, String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setString("v_staetDate", startTime + " 00:00:00");
		query.setString("v_endDate", endTime + " 23:59:59");
		List<InsideOrder> orderList = query.list();
		return orderList;
	}

	@Override
	public List<InsideOrder> getList(int companyId, String type, String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setString("v_orderType", type);
		List<InsideOrder> orderList = query.list();
		return orderList;
	}

	@Override
	public List<InsideOrder> getIsSend(int userId, int start, int limit) {
		String sql = "SELECT o FROM InsideOrder o where o.userId=:v_userId and o.isSend is null and o.orderStatus='finish' order by o.addTime desc";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userId);
		query.setFirstResult(start).setMaxResults(limit);
		List<InsideOrder> orderList = query.list();
		return orderList;
	}

	@Override
	public List<InsideOrder> getInsideOrderByThree(int companyId, String seat) {
		String sql = "select a.id as ad,b.id as bd,c.id as cd from Reserve a,ComputerCate b,InsideOrder c where a.id=b.reserveId and b.insideorderId=c.id and a.status='2' and a.companyId=:v_companyId and a.seat=:v_seat and a.isOpen=1 and a.isDelete = 0";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setString("v_seat", seat);
		List<InsideOrder> list = query.list();
		return list;
	}

	@Override
	public List<InsideOrder> getBaseList(int companyId, String startTime, String endTime) {// 线下订单列表
		String sql = "SELECT o FROM InsideOrder o where o.companyId=:v_companyId and o.addTime>=:v_startTime and o.addTime<=:v_endTime and o.orderStatus='finish' order by o.addTime desc";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setString("v_startTime", startTime + " 00:00:00");
		query.setString("v_endTime", endTime + " 23:59:59");
		List<InsideOrder> orderList = query.list();
		return orderList;
	}

	@Override
	public Object[] getCount(int companyId, String startTime, String endTime) {// 线下订单统计
		String sql = "SELECT o FROM InsideOrder o where o.companyId=:v_companyId and o.addTime>=:v_startTime and o.addTime<=:v_endTime and o.orderStatus='finish' order by o.addTime desc";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setString("v_startTime", startTime + " 00:00:00");
		query.setString("v_endTime", endTime + " 23:59:59");
		List<Object[]> list = query.list();
		if (list.size() > 0) {
			Object[] obj = list.get(0);
			return obj;
		} else {
			return null;
		}
	}

}