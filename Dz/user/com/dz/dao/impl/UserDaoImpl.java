package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IUserDao;
import com.dz.entity.User;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("userDao")
@SuppressWarnings("unchecked")
public class UserDaoImpl implements IUserDao {

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
	public User login(String userName) {
		String sql = "SELECT o FROM User o WHERE o.userName =:v_username";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_username", userName);
		List<User> users = query.list();
		if (users != null && users.size() > 0) {
			return users.get(0);
		}
		return null;
	}

	// 用户列表
	@Override
	public List<User> userList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<User> userList = query.list();
		return userList;
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM User o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改用户信息
	@Override
	public void saveORupdate(User user) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(user);
	}

	// 按id查找用户
	@Override
	public User getid(final int id) {
		String sql = "SELECT o FROM User o WHERE id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<User> _li = query.list();

		if (_li.size() > 0)
			return _li.get(0);
		else
			return null;
	}

	// 按账号查找用户
	@Override
	public List<User> getuserName(final String userName) {
		String sql = "SELECT o FROM User o WHERE o.userName=:v_userName";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_userName", userName);
		List<User> _li = query.list();
		return _li;
	}

	// 按账号查找用户
	@Override
	public User gettoken(final String token) {
		String sql = "SELECT o FROM User o WHERE o.token=:v_token";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_token", token);
		List<User> _li = query.list();
		if (_li.size() > 0)
			return _li.get(0);
		else
			return null;
	}

	// 按商家id查询分页
	@Override
	public List<User> getCompanyId(int companyId, int start, int limit) {
		String sql = "SELECT o FROM User o WHERE o.companyId=:v_companyId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setFirstResult(start).setMaxResults(limit);
		List<User> _li = query.list();
		return _li;
	}

	// 按商家id查询总计
	@Override
	public List<User> getCompanyId(int companyId) {
		String sql = "SELECT o FROM User o WHERE o.companyId=:v_companyId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		List<User> _li = query.list();
		return _li;
	}

	//后台管理
	// 按手机查询
	@Override
	public User getUserName(String userName) {
		String sql = "SELECT o FROM User o WHERE o.userName =:v_username";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_username", userName);
		List<User> users = query.list();
		if (users != null && users.size() > 0) {
			return users.get(0);
		}
		return null;
	}

	// 按手机查询
	@Override
	public User getPhone(String phone) {
		String sql = "SELECT o FROM User o WHERE o.phone =:v_phone";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_phone", phone);
		List<User> users = query.list();
		if (users != null && users.size() > 0) {
			return users.get(0);
		}
		return null;
	}

	// 按上司查询
	@Override
	public List<User> getBoss(int bossId) {
		String sql = "SELECT o FROM User o WHERE o.bossId =:v_bossId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_bossId", bossId);
		List<User> users = query.list();
		return users;
	}
	
	@Override	//ynw
	public List<User> userList(String sql, int start, int limit) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql).setFirstResult(start).setMaxResults(limit);
		List<User> userList = query.list();
		return userList;
	}

}