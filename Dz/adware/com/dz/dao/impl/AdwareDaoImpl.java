package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IAdwareDao;
import com.dz.entity.Adware;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("adwareDao")
@SuppressWarnings("unchecked")
public class AdwareDaoImpl implements IAdwareDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	// 按类型查询
	@Override
	public List<Adware> getAdware(String type) {
		String sql = "SELECT o FROM Adware o WHERE o.type=:v_type";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_type", type);
		List<Adware> adwareList = query.list();
		return adwareList;
	}

	// 删除
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM Adware o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Adware adware) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(adware);
	}

	// 管理后台
	// 按类型查询
	@Override
	public List<Adware> getAdwareList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Adware> adwareList = query.list();
		return adwareList;
	}

	@Override /* ynw */
	public List<Adware> getAdwareList(String sql, int start, int limit) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql).setFirstResult(start).setMaxResults(limit);
		List<Adware> adwareList = query.list();
		return adwareList;
	}

	// 按类型查询
	@Override
	public Adware getAdware(int id) {
		String sql = "SELECT o FROM Adware o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<Adware> adwareList = query.list();
		if (adwareList.size() > 0) {
			return adwareList.get(0);
		}
		return null;
	}

}