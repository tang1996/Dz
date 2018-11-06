package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.ISalerPowerDao;
import com.dz.entity.SalerPower;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("salerPowerDao")
@SuppressWarnings("unchecked")
public class SalerPowerDaoImpl implements ISalerPowerDao {

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
	public SalerPower getSalerPower(String position, int companyId) {
		String sql ="SELECT o FROM SalerPower o WHERE o.position =:v_position and o.companyId=:v_companyId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_position", position);
		query.setInteger("v_companyId", companyId);
		List<SalerPower> salerPowers = query.list();

		if (salerPowers != null && salerPowers.size() > 0) {
			return salerPowers.get(0);
		}
		return null;
	}

	// 用户列表
	@Override
	public List<SalerPower> salerPowerList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<SalerPower> salerPowerList = query.list();
		return salerPowerList;
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM SalerPower o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改用户信息
	@Override
	public void saveORupdate(SalerPower salerPower) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(salerPower);
	}

	// 按id查找用户
	@Override
	public SalerPower getid(final int id) {
		String sql = "SELECT o FROM SalerPower o WHERE id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<SalerPower> _li = query.list();

		if (_li.size() > 0)
			return _li.get(0);
		else
			return null;
	}

	// 按账号查找用户
	@Override
	public List<SalerPower> getuserName(final String userName) {
		String sql = "SELECT o FROM SalerPower o WHERE o.userName=:v_userName";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_userName", userName);
		List<SalerPower> _li = query.list();
		return _li;
	}

	// 按账号查找用户
	@Override
	public SalerPower gettoken(final String token) {
		String sql = "SELECT o FROM SalerPower o WHERE o.token=:v_token";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_token", token);
		List<SalerPower> _li = query.list();
		if (_li.size() > 0)
			return _li.get(0);
		else
			return null;
	}

}