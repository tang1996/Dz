package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.ISalesmanDao;
import com.dz.entity.Salesman;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("salesmanDao")
@SuppressWarnings("unchecked")
public class SalesmanDaoImpl implements ISalesmanDao {

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
	public Salesman login(String sql, String userName) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_username", userName);
		List<Salesman> salesmans = query.list();

		if (salesmans != null && salesmans.size() > 0) {
			return salesmans.get(0);
		}
		return null;
	}

	// 管理员列表
	@Override
	public List<Salesman> salesmanList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Salesman> salesmanList = query.list();
		if (salesmanList.size() > 0) {
			return salesmanList;
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
	public void saveORupdate(Salesman salesman) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(salesman);
	}
	
	// 按id查找管理员
	@Override
	public Salesman getid(final String sql, final int id) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<Salesman> _li = query.list();

		if (_li.size() > 0)
			return _li.get(0);
		else
			return null;
	}

	// 按账号查找管理员
	@Override
	public List<Salesman> getuserName(final String sql, final String userName) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_userName", userName);
		List<Salesman> _li = query.list();
		return _li;
	}

}