package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IIncludeDao;
import com.dz.entity.Include;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("includeDao")
@SuppressWarnings("unchecked")
public class IncludeDaoImpl implements IIncludeDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	// 查询列表
	@Override
	public List<Include> includeList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Include> includeList = query.list();
		return includeList;
	}

	// 按用户查询
	@Override
	public List<Include> getInclude(int userId) {
		String sql = "SELECT o FROM Include o WHERE o.userId=:v_userId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userId);
		List<Include> includeList = query.list();
		return includeList;
		
	}
	
	// 按id查询
	@Override
	public Include include(int id) {
		String sql = "SELECT o FROM Include o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<Include> includeList = query.list();
		if (includeList.size() > 0) {
			return includeList.get(0);
		} else {
			return null;
		}
	}
	
	// 按id查询
	@Override
	public Include getInclude(int userId, int companyId) {
		String sql = "SELECT o FROM Include o WHERE o.userId=:v_userId and o.companyId=:v_companyId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userId);
		query.setInteger("v_companyId", companyId);
		List<Include> includeList = query.list();
		if (includeList.size() > 0) {
			return includeList.get(0);
		} else {
			return null;
		}
	}

	// 删除
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM Include o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Include include) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(include);
	}

}