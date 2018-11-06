package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.ICompanyDetailedDao;
import com.dz.entity.CompanyDetailed;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("companyDetailedDao")
@SuppressWarnings("unchecked")
public class CompanyDetailedDaoImpl implements ICompanyDetailedDao {

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
	public CompanyDetailed login(String userName) {
		String sql ="SELECT o FROM CompanyDetailed o WHERE o.userName =:v_username";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_username", userName);
		List<CompanyDetailed> companyDetaileds = query.list();
		if (companyDetaileds != null && companyDetaileds.size() > 0) {
			return companyDetaileds.get(0);
		}
		return null;
	}

	// 用户列表
	@Override
	public List<CompanyDetailed> companyDetailedList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<CompanyDetailed> companyDetailedList = query.list();
		return companyDetailedList;
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM CompanyDetailed o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改用户信息
	@Override
	public void saveORupdate(CompanyDetailed companyDetailed) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(companyDetailed);
	}

	// 按id查找用户
	@Override
	public CompanyDetailed getid(final int id) {
		String sql = "SELECT o FROM CompanyDetailed o WHERE id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<CompanyDetailed> _li = query.list();

		if (_li.size() > 0)
			return _li.get(0);
		else
			return null;
	}

	// 按账号查找用户
	@Override
	public CompanyDetailed getCompany(int companyId) {
		String sql = "SELECT o FROM CompanyDetailed o WHERE o.companyId=:v_companyId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		List<CompanyDetailed> _li = query.list();
		if (_li.size() > 0)
			return _li.get(0);
		else
			return null;
	}

}