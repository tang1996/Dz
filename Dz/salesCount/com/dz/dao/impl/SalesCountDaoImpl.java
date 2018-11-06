package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.ISalesCountDao;
import com.dz.entity.SalesCount;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("salesCountDao")
@SuppressWarnings("unchecked")
public class SalesCountDaoImpl implements ISalesCountDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	// 管理员登录
	@Override
	public SalesCount login(String sql, String userName) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_username", userName);
		List<SalesCount> salesCounts = query.list();

		if (salesCounts != null && salesCounts.size() > 0) {
			return salesCounts.get(0);
		}
		return null;
	}

	// 管理员列表
	@Override
	public List<SalesCount> salesCountList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<SalesCount> salesCountList = query.list();
		if (salesCountList.size() > 0) {
			return salesCountList;
		} else {
			return null;
		}
	}

	// 删除管理员
	@Override
	public void delete(final String sql, String id) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改管理员信息
	@Override
	public void saveORupdate(SalesCount salesCount) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(salesCount);
	}
	
	// 按id查找管理员
	@Override
	public SalesCount getid(final String sql, final int id) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<SalesCount> _li = query.list();

		if (_li.size() > 0)
			return _li.get(0);
		else
			return null;
	}

	// 按账号查找管理员
	@Override
	public List<SalesCount> getuserName(final String sql, final String userName) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_userName", userName);
		List<SalesCount> _li = query.list();
		return _li;
	}

}