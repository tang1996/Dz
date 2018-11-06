package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.ICompanyExamineDao;
import com.dz.entity.CompanyExamine;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("companyExamineDao")
@SuppressWarnings("unchecked")
public class CompanyExamineDaoImpl implements ICompanyExamineDao {

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
	public CompanyExamine login(String userName) {
		String sql ="SELECT o FROM CompanyExamine o WHERE o.userName =:v_username";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_username", userName);
		List<CompanyExamine> companyExamines = query.list();
		if (companyExamines != null && companyExamines.size() > 0) {
			return companyExamines.get(0);
		}
		return null;
	}

	// 用户列表
	@Override
	public List<CompanyExamine> companyExamineList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<CompanyExamine> companyExamineList = query.list();
		return companyExamineList;
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM CompanyExamine o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改用户信息
	@Override
	public void saveORupdate(CompanyExamine companyExamine) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(companyExamine);
	}

	// 按id查找用户
	@Override
	public CompanyExamine getid(final int id) {
		String sql = "SELECT o FROM CompanyExamine o WHERE id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<CompanyExamine> _li = query.list();

		if (_li.size() > 0)
			return _li.get(0);
		else
			return null;
	}

	// 按账号查找用户
	@Override
	public List<CompanyExamine> getuserName(final String userName) {
		String sql = "SELECT o FROM CompanyExamine o WHERE o.userName=:v_userName";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_userName", userName);
		List<CompanyExamine> _li = query.list();
		return _li;
	}

}