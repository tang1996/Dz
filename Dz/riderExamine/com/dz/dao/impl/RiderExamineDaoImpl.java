package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IRiderExamineDao;
import com.dz.entity.RiderExamine;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("riderExamineDao")
@SuppressWarnings("unchecked")
public class RiderExamineDaoImpl implements IRiderExamineDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	// 用户登录
	@Override
	public RiderExamine login(String userName) {
		String sql ="SELECT o FROM RiderExamine o WHERE o.userName =:v_username";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_username", userName);
		List<RiderExamine> riderExamines = query.list();
		if (riderExamines != null && riderExamines.size() > 0) {
			return riderExamines.get(0);
		}
		return null;
	}

	// 用户列表
	@Override
	public List<RiderExamine> riderExamineList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<RiderExamine> riderExamineList = query.list();
		return riderExamineList;
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM RiderExamine o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改用户信息
	@Override
	public void saveORupdate(RiderExamine riderExamine) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(riderExamine);
	}

	// 按id查找用户
	@Override
	public RiderExamine getid(final int id) {
		String sql = "SELECT o FROM RiderExamine o WHERE id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<RiderExamine> _li = query.list();

		if (_li.size() > 0)
			return _li.get(0);
		else
			return null;
	}

	// 按账号查找用户
	@Override
	public List<RiderExamine> getuserName(final String userName) {
		String sql = "SELECT o FROM RiderExamine o WHERE o.userName=:v_userName";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_userName", userName);
		List<RiderExamine> _li = query.list();
		return _li;
	}

	// 按账号查找用户
	@Override
	public RiderExamine gettoken(final String token) {
		String sql = "SELECT o FROM RiderExamine o WHERE o.token=:v_token";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_token", token);
		List<RiderExamine> _li = query.list();
		if (_li.size() > 0)
			return _li.get(0);
		else
			return null;
	}

}