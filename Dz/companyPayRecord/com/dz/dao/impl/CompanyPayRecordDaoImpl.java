package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.ICompanyPayRecordDao;
import com.dz.entity.CompanyPayRecord;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("companyPayRecordDao")
@SuppressWarnings("unchecked")
public class CompanyPayRecordDaoImpl implements ICompanyPayRecordDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	// 获取商家总交易统计			ynw
	@Override
	public List<CompanyPayRecord> companyPayRecordTotal(int companyId) {
		String sql = "SELECT o FROM CompanyPayRecord o WHERE o.companyId=:v_companyId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		List<CompanyPayRecord> companyPayRecordList = query.list();
		return companyPayRecordList;
	}

	// 获取商家总交易统计
	@Override
	public List<CompanyPayRecord> companyPayRecord(int companyId) {
		String sql = "SELECT o FROM CompanyPayRecord o WHERE o.companyId=:v_companyId and o.isAccount=1";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		List<CompanyPayRecord> companyPayRecordList = query.list();
		return companyPayRecordList;
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM CompanyPayRecord o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(CompanyPayRecord companyPayRecord) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(companyPayRecord);
	}

	// 获取商家当天交易统计
	@Override
	public List<CompanyPayRecord> getCompanyPayRecord(int companyId, String date) {
		String sql = "SELECT o FROM CompanyPayRecord o WHERE o.companyId=:v_companyId and o.date>=:v_staetDate and o.date<=:v_endDate";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setString("v_staetDate", date + " 00:00:00");
		query.setString("v_endDate", date + " 23:59:59");
		List<CompanyPayRecord> companyPayRecordList = query.list();
		return companyPayRecordList;
	}

	// 获取用户默认地址
	@Override
	public CompanyPayRecord find(int id) {
		String sql = "SELECT o FROM CompanyPayRecord o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<CompanyPayRecord> companyPayRecordList = query.list();
		if (companyPayRecordList.size() > 0) {
			return companyPayRecordList.get(0);
		} else {
			return null;
		}
	}

	// 获取商家当天交易统计
	@Override
	public List<CompanyPayRecord> getList(String sql, String startDate, String endDate, int start, int limit) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_startDate", startDate + " 00:00:00");
		query.setString("v_endDate", endDate + " 23:59:59");
		query.setFirstResult(start).setMaxResults(limit);
		List<CompanyPayRecord> companyPayRecordList = query.list();
		return companyPayRecordList;
	}

	// 获取商家当天交易统计
	@Override
	public Long getList(String sql, String startDate, String endDate) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_startDate", startDate + " 00:00:00");
		query.setString("v_endDate", endDate + " 23:59:59");
		List<Long> companyPayRecordList = query.list();
		if (companyPayRecordList.size() > 0) {
			Long count = companyPayRecordList.get(0);
			return count;
		} else {
			return 0L;
		}
	}

	@Override
	public List<CompanyPayRecord> getPayView() {
		String sql = "SELECT o FROM CompanyPayRecord o WHERE o.isAccount=0";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<CompanyPayRecord> list = query.list();
		return list;
	}

}