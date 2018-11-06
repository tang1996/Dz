package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IRelatingDao;
import com.dz.entity.Relating;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("relatingDao")
@SuppressWarnings("unchecked")
public class RelatingDaoImpl implements IRelatingDao {

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
	public List<Relating> relatingList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Relating> relatingList = query.list();
		return relatingList;
	}

	// 用户列表
	@Override
	public List<Relating> getrelating(int orderId, int companyId) {
		String sql = "SELECT o FROM Relating o WHERE o.orderId=:v_orderId and o.companyId=:v_companyId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_orderId", orderId);
		query.setInteger("v_companyId", companyId);
		List<Relating> relatingList = query.list();
		return relatingList;
	}

	// 用户列表
	@Override
	public List<Relating> getrelating(int orderId) {
		String sql = "SELECT o FROM Relating o WHERE o.orderId=:v_orderId";
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_orderId", orderId);
		List<Relating> relatingList = query.list();
		return relatingList;
	}

	// 用户列表
	@Override
	public Relating getRelating(int orderId, int goodsId) {
		String sql = "SELECT o FROM Relating o WHERE o.orderId=:v_orderId and o.goodsId=:v_goodsId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_orderId", orderId);
		query.setInteger("v_goodsId", goodsId);
		List<Relating> relatingList = query.list();
		if (relatingList.size() > 0) {
			return relatingList.get(0);
		} else {
			return null;
		}
	}

	// 获取用户点菜记录
	@Override
	public List<Relating> getMenus(int orderId, int goodsId, int cid) {
		String sql = "SELECT o FROM Relating o WHERE o.orderId=:v_orderId and o.companyId=:v_companyId and o.goodsId=:v_goodsId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_orderId", orderId);
		query.setInteger("v_companyId", cid);
		query.setInteger("v_goodsId", goodsId);
		List<Relating> relatingList = query.list();
		return relatingList;
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM Relating o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Relating relating) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(relating);
		session.flush();
	}

	// 用户列表
	@Override
	public Relating getinsideRelating(int orderId, int goodsId) {
		String sql = "SELECT o FROM Relating o WHERE o.insideOrderId=:v_orderId and o.goodsId=:v_goodsId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_orderId", orderId);
		query.setInteger("v_goodsId", goodsId);
		List<Relating> relatingList = query.list();
		if (relatingList.size() > 0) {
			return relatingList.get(0);
		} else {
			return null;
		}
	}

	@Override	//ynw
	public List<Relating> getinsideMenus(int insideorderId, int goodsId, int cid) {	
		String sql = "SELECT o FROM Relating o WHERE o.insideOrderId=:v_insideorderId and o.companyId=:v_companyId and o.goodsId=:v_goodsId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_insideorderId", insideorderId);
		query.setInteger("v_companyId", cid);
		query.setInteger("v_goodsId", goodsId);
		List<Relating> relatingList = query.list();
		return relatingList;
	}

	@Override	//ynw
	public List<Relating> getrelatingbyinsideID(int orderId) {
		String sql = "SELECT o FROM Relating o WHERE o.insideOrderId=:v_insideorderId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_insideorderId", orderId);
		List<Relating> relatingList = query.list();
		return relatingList;
	}

	@Override	//ynw
	public List<Relating> getrelatingbyinsideID(int orderId, int companyId) {
		String sql = "SELECT o FROM Relating o WHERE o.insideOrderId=:v_orderId and o.companyId=:v_companyId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_orderId", orderId);
		query.setInteger("v_companyId", companyId);
		List<Relating> relatingList = query.list();
		return relatingList;
	}

	@Override
	public List<Relating> getinsertrelating(int insideOrder) {
		String sql = "SELECT o FROM Relating o WHERE o.insideOrderId=:v_insideOrderId";
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_insideOrderId", insideOrder);
		List<Relating> relatingList = query.list();
		return relatingList;
	}

	@Override
	public List<Relating> getGoods(int goodid, int companyId, int orderId) {
		String sql = "SELECT o FROM Relating o WHERE o.goodsId=:v_goodsId and o.companyId=:v_companyId and o.orderId =:v_orderId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_goodsId", goodid);
		query.setInteger("v_companyId", companyId);
		query.setInteger("v_orderId", orderId);
		List<Relating> relatingList = query.list();
		return relatingList;
	}
}