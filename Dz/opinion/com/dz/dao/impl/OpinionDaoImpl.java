package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IOpinionDao;
import com.dz.entity.Opinion;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("opinionDao")
@SuppressWarnings("unchecked")
public class OpinionDaoImpl implements IOpinionDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	// 查询列表
	@Override
	public List<Opinion> opinionList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Opinion> opinionList = query.list();
		return opinionList;
	}

	// 按id查询
	@Override
	public Opinion opinion(int id) {
		String sql = "SELECT o FROM Opinion o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<Opinion> opinionList = query.list();
		if (opinionList.size() > 0) {
			return opinionList.get(0);
		} else {
			return null;
		}
	}

	// 删除
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM Opinion o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Opinion opinion) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(opinion);
	}

	// 管理后台
	@Override
	public List<Opinion> opinionList(String sql, int start, int limit) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql).setFirstResult(start).setMaxResults(limit);
		List<Opinion> opinionList = query.list();
		return opinionList;
	}
}