package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.ICateDao;
import com.dz.entity.Cate;
import com.dz.entity.Order;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("cateDao")
@SuppressWarnings("unchecked")
public class CateDaoImpl implements ICateDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public Cate getCate(String tableNo, int cid, String name, String seat) {
		String sql = "SELECT o FROM Cate o WHERE o.tableNo=:v_tableNo and o.companyId=:v_cid and o.name=:v_name and o.seat=:v_seat";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_tableNo", tableNo);
		query.setString("v_name", name);
		query.setString("v_seat", seat);
		query.setInteger("v_cid", cid);
		List<Cate> list = query.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	// 用户列表
	@Override
	public List<Cate> cateList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Cate> cateList = query.list();
		return cateList;
	}

	// 查询单个订单
	@Override
	public Cate getCate(int orderId) {
		String sql = "SELECT o FROM Cate o WHERE o.orderId=:v_orderId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_orderId", orderId);
		List<Cate> list = query.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM Cate o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Cate cate) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(cate);
	}

	@Override
	public List<Order> cateRes(String reserveId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(
				"SELECT t FROM Order t where t.orderStatus = 'doing' and t.id in ( SELECT o.orderId FROM Cate o WHERE o.reserveId=:v_reserveId )");
		query.setString("v_reserveId", reserveId);
		List<Order> cateList = query.list();
		return cateList;
	}

	@Override
	public Cate getCateInside(int insideId) {
		String sql = "SELECT o FROM Cate o WHERE o.insideOrderId=:v_orderId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_orderId", insideId);
		List<Cate> list = query.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	// 手机号查询
	@Override
	public List<Cate> searchPhone(String phone) {// 2018-11-03 @Tyy
		String sql = "SELECT o FROM Cate o WHERE o.phone=:v_phone";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_phone", phone);
		List<Cate> list = query.list();
		return list;
	}

}