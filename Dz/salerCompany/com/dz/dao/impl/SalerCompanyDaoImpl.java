package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.ISalerCompanyDao;
import com.dz.entity.SalerCompany;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("salerCompanyDao")
@SuppressWarnings("unchecked")
public class SalerCompanyDaoImpl implements ISalerCompanyDao {

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
	public List<SalerCompany> salerCompanyList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<SalerCompany> salerCompanyList = query.list();
		return salerCompanyList;
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM SalerCompany o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改用户信息
	@Override
	public void saveORupdate(SalerCompany salerCompany) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(salerCompany);
	}

	// 按id查找用户
	@Override
	public SalerCompany getid(final int id) {
		String sql = "SELECT o FROM SalerCompany o WHERE id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<SalerCompany> _li = query.list();

		if (_li.size() > 0)
			return _li.get(0);
		else
			return null;
	}

	// 用户列表
	@Override
	public Object[] count(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Object[]> salerCompanyList = query.list();
		if(salerCompanyList.size()>0){
			Object[] obj = salerCompanyList.get(0);
			return obj;
		}else{
			return null;
		}
	}

}