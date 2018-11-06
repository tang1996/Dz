package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.INatureDao;
import com.dz.entity.Nature;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("natureDao")
@SuppressWarnings("unchecked")
public class NatureDaoImpl implements INatureDao {

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
	public List<Nature> natureList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Nature> natureList = query.list();
		return natureList;
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM Nature o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Nature nature) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(nature);
	}

	@Override
	public List<Nature> getnature(int attributeId) {
		String sql = "SELECT o FROM Nature o WHERE o.attributeId=:v_attributeId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_attributeId", attributeId);
		List<Nature> natureList = query.list();
		return natureList;
	}

	@Override
	public Nature nature(int id) {
		String sql = "SELECT o FROM Nature o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<Nature> natureList = query.list();
		if (natureList.size() > 0) {
			return natureList.get(0);
		} else {
			return null;
		}
	}

}