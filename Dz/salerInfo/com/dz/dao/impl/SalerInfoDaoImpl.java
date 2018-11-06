package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.ISalerInfoDao;
import com.dz.entity.SalerInfo;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("salerInfoDao")
@SuppressWarnings("unchecked")
public class SalerInfoDaoImpl implements ISalerInfoDao {

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
	public List<SalerInfo> salerInfoList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<SalerInfo> salerInfoList = query.list();
		return salerInfoList;
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM SalerInfo o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改用户信息
	@Override
	public void saveORupdate(SalerInfo salerInfo) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(salerInfo);
	}

	// 按id查找用户
	@Override
	public SalerInfo getid(final int id) {
		String sql = "SELECT o FROM SalerInfo o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<SalerInfo> _li = query.list();

		if (_li.size() > 0)
			return _li.get(0);
		else
			return null;
	}

	// 按id查找用户
	@Override
	public SalerInfo getPhone(final String phone) {
		String sql = "SELECT o FROM SalerInfo o WHERE o.phone=:v_phone";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_phone", phone);
		List<SalerInfo> _li = query.list();

		if (_li.size() > 0)
			return _li.get(0);
		else
			return null;
	}

	// 用户列表
	@Override
	public List<SalerInfo> countList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<SalerInfo> salerCompanyList = query.list();
		return salerCompanyList;
	}

	// 用户列表
	@Override
	public List<SalerInfo> countList(String sql, int start, int limit) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setFirstResult(start).setMaxResults(limit);
		List<SalerInfo> salerCompanyList = query.list();
		return salerCompanyList;
	}

	// 获取业务员上司
	@Override
	public List<SalerInfo> getBossId(int bossId) {
		String sql = "SELECT o FROM SalerInfo o WHERE o.bossId=:v_bossId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_bossId", bossId);
		List<SalerInfo> _li = query.list();
		return _li;
	}

	// 按查找用户
	@Override
	public SalerInfo getCode(String code) {
		String sql = "SELECT o FROM SalerInfo o WHERE o.code=:v_code";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_code", code);
		List<SalerInfo> _li = query.list();
		if (_li.size() > 0)
			return _li.get(0);
		else
			return null;
	}

}