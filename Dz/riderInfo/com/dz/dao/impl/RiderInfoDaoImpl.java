package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IRiderInfoDao;
import com.dz.entity.RiderInfo;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("riderInfoDao")
@SuppressWarnings("unchecked")
public class RiderInfoDaoImpl implements IRiderInfoDao {

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
	public List<RiderInfo> riderInfoList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<RiderInfo> riderInfoList = query.list();
		return riderInfoList;
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM RiderInfo o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改用户信息
	@Override
	public void saveORupdate(RiderInfo riderInfo) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(riderInfo);
	}

	// 按id查找用户
	@Override
	public RiderInfo getid(final int id) {
		String sql = "SELECT o FROM RiderInfo o WHERE id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<RiderInfo> _li = query.list();

		if (_li.size() > 0)
			return _li.get(0);
		else
			return null;
	}

	// 按id查找用户
	@Override
	public RiderInfo getuserId(int userId) {
		String sql = "SELECT o FROM RiderInfo o WHERE o.userId=:v_userId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userId);
		List<RiderInfo> _li = query.list();

		if (_li.size() > 0)
			return _li.get(0);
		else
			return null;
	}
	
}