package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.ICompanyOrderCountDao;
import com.dz.entity.CompanyOrderCount;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("companyOrderCountDao")
@SuppressWarnings("unchecked")
public class CompanyOrderCountDaoImpl implements ICompanyOrderCountDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	// 获取商家总交易统计
	@Override
	public Object[] companyOrderCount(int companyId) {
		String sql = "SELECT o FROM CompanyOrderCount o WHERE o.companyId=:v_companyId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		List<Object[]> companyOrderCountList = query.list();
		if(companyOrderCountList.size() > 0){
			Object[] count = companyOrderCountList.get(0);
			return count;
		}else{
			return null;
		}
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM CompanyOrderCount o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(CompanyOrderCount companyOrderCount) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(companyOrderCount);
	}

	// 获取商家当天交易统计
	@Override
	public Object[] getCompanyOrderCount(int companyId, String date) {
		String sql = "SELECT o FROM CompanyOrderCount o WHERE o.companyId=:v_companyId and o.date>=:v_staetDate and o.date<=:v_endDate";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setString("v_staetDate", date + " 00:00:00");
		query.setString("v_endDate", date + " 23:59:59");
		List<Object[]> companyOrderCountList = query.list();
		if (companyOrderCountList.size() > 0) {
			Object[] count = companyOrderCountList.get(0);
			return count;
		} else {
			return null;
		}
	}
	
	// 获取用户默认地址
	@Override
	public CompanyOrderCount find(int id) {
		String sql = "SELECT o FROM CompanyOrderCount o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<CompanyOrderCount> companyOrderCountList = query.list();
		if (companyOrderCountList.size() > 0) {
			return companyOrderCountList.get(0);
		} else {
			return null;
		}
	}

}