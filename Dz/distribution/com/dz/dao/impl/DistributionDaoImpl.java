package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IDistributionDao;
import com.dz.entity.Distribution;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("distributionDao")
@SuppressWarnings("unchecked")
public class DistributionDaoImpl implements IDistributionDao {

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
	public List<Distribution> distributionList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Distribution> distributionList = query.list();
		return distributionList;
	}

	// 查询单个属性
	@Override
	public Distribution getDistribution(int companyId) {
		String sql = "SELECT o FROM Distribution o WHERE o.companyId=:v_companyId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		List<Distribution> distributionList = query.list();
		if (distributionList.size() > 0) {
			return distributionList.get(0);
		} else {
			return null;
		}
	}
	
	// 查询单个属性
	@Override
	public Distribution find(int id) {
		String sql = "SELECT o FROM Distribution o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<Distribution> distributionList = query.list();
		if (distributionList.size() > 0) {
			return distributionList.get(0);
		} else {
			return null;
		}
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM Distribution o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Distribution distribution) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(distribution);
	}

}