package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.ICompanyScoreDao;
import com.dz.entity.CompanyScore;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("companyScoreDao")
@SuppressWarnings("unchecked")
public class CompanyScoreDaoImpl implements ICompanyScoreDao {

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
	public List<CompanyScore> companyScoreList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<CompanyScore> companyScoreList = query.list();
		return companyScoreList;
	}

	// 用户列表
	@Override
	public CompanyScore getcompanyScore(int evaluateid) {
		String sql = "SELECT o FROM CompanyScore o WHERE o.evaluateId=:v_companyScoreId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyScoreId", evaluateid);
		List<CompanyScore> companyScoreList = query.list();
		if (companyScoreList.size() > 0) {
			return companyScoreList.get(0);
		} else {
			return null;
		}
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM CompanyScore o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(CompanyScore companyScore) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(companyScore);
	}

}