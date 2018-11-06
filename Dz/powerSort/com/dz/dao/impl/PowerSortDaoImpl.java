package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IPowerSortDao;
import com.dz.entity.PowerSort;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("powerSortDao")
@SuppressWarnings("unchecked")
public class PowerSortDaoImpl implements IPowerSortDao {

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
	public PowerSort getPowerSort(String position, int companyId) {
		String sql ="SELECT o FROM PowerSort o WHERE o.position =:v_position and o.companyId=:v_companyId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_position", position);
		query.setInteger("v_companyId", companyId);
		List<PowerSort> powerSorts = query.list();

		if (powerSorts != null && powerSorts.size() > 0) {
			return powerSorts.get(0);
		}
		return null;
	}

	// 用户列表
	@Override
	public List<PowerSort> powerSortList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<PowerSort> powerSortList = query.list();
		return powerSortList;
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM PowerSort o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改用户信息
	@Override
	public void saveORupdate(PowerSort powerSort) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(powerSort);
	}

	// 按id查找用户
	@Override
	public PowerSort getid(final int id) {
		String sql = "SELECT o FROM PowerSort o WHERE id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<PowerSort> _li = query.list();

		if (_li.size() > 0)
			return _li.get(0);
		else
			return null;
	}

	// 按账号查找用户
	@Override
	public List<PowerSort> getuserName(final String userName) {
		String sql = "SELECT o FROM PowerSort o WHERE o.userName=:v_userName";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_userName", userName);
		List<PowerSort> _li = query.list();
		return _li;
	}

	// 按账号查找用户
	@Override
	public PowerSort gettoken(final String token) {
		String sql = "SELECT o FROM PowerSort o WHERE o.token=:v_token";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_token", token);
		List<PowerSort> _li = query.list();
		if (_li.size() > 0)
			return _li.get(0);
		else
			return null;
	}

}