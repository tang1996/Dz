package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.ICompanyActivityDao;
import com.dz.entity.CompanyActivity;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("companyActivityDao")
@SuppressWarnings("unchecked")
public class CompanyActivityDaoImpl implements ICompanyActivityDao {

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
	public List<CompanyActivity> companyActivityList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<CompanyActivity> companyActivityList = query.list();
		return companyActivityList;
	}
	
	@Override
	public List<CompanyActivity> getList(int companyId){
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createQuery("SELECT o FROM CompanyActivity o WHERE o.companyId = :v_companyId and o.isOpen = 1 ORDER BY o.activityId");
		query.setInteger("v_companyId", companyId);
		List<CompanyActivity> list = query.list();
		return list;
	}

	// 商家列表
	@Override
	public List<CompanyActivity> getList(int companyId, String type) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createQuery("SELECT o FROM CompanyActivity o WHERE o.companyId = :v_companyId and o.type = :v_type and o.isOpen = 1 ORDER BY o.activityId");
		query.setInteger("v_companyId", companyId);
		query.setString("v_type", type);
		List<CompanyActivity> list = query.list();
		return list;
	}

	// 商家列表
	@Override
	public List<CompanyActivity> getTime(int companyId, String date, String type) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(
				"SELECT o FROM CompanyActivity o WHERE o.companyId = :v_companyId and o.type=:v_type and o.startTime<=:v_date and o.endTime>=:v_date ORDER BY o.id DESC");
		query.setInteger("v_companyId", companyId);
		query.setString("v_type", type);
		query.setString("v_date", date);
		List<CompanyActivity> list = query.list();
		return list;
	}
	
	@Override
	public List<CompanyActivity> getTimeIsNot(int companyId, int activityId, String date, String type){
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(
				"SELECT o FROM CompanyActivity o WHERE o.companyId = :v_companyId and o.activityId!=:v_activityId and o.type=:v_type and o.startTime<=:v_date and o.endTime>=:v_date ORDER BY o.id DESC");
		query.setInteger("v_companyId", companyId);
		query.setInteger("v_activityId", activityId);
		query.setString("v_type", type);
		query.setString("v_date", date);
		List<CompanyActivity> list = query.list();
		
		return list;
	}
	

	// 商家列表
	@Override
	public List<CompanyActivity> companyActivity(int companyId, int activityId, String type) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("SELECT o FROM CompanyActivity o WHERE o.companyId = :v_companyId and o.activityId=:v_activityId and o.type=:v_type ORDER BY o.id DESC");
		query.setInteger("v_companyId", companyId);
		query.setInteger("v_activityId", activityId);
		query.setString("v_type", type);
		List<CompanyActivity> list = query.list();
		return list;
	}
	
	// 商家列表
	@Override
	public List<CompanyActivity> companyActivity(int companyId, String type, String date) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("SELECT o FROM CompanyActivity o WHERE o.companyId = :v_companyId and o.type=:v_type and o.isOpen=1 and o.startTime<=:v_date and o.endTime>=:v_date order by o.balance DESC");
		query.setInteger("v_companyId", companyId);
		query.setString("v_type", type);
		query.setString("v_date", date);
		List<CompanyActivity> list = query.list();
		return list;
	}

	// 商家列表
	@Override
	public CompanyActivity getCompanyActivity(int id) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createQuery("SELECT o FROM CompanyActivity o WHERE o.id = :v_id");
		query.setInteger("v_id", id);
		List<CompanyActivity> list = query.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	// 商家列表
	@Override
	public Long count(int companyId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createQuery("SELECT o FROM CompanyActivity o WHERE o.companyId = :v_companyId ORDER BY o.activityId");
		query.setInteger("v_companyId", companyId);
		List<CompanyActivity> list = query.list();
		Long a = 0L;
		return a;
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM CompanyActivity o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(CompanyActivity companyActivity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(companyActivity);
	}

	// 查询所有活动
	@Override
	public List<CompanyActivity> getList(String isOpen) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createQuery("SELECT o FROM CompanyActivity o WHERE o.isOpen = :v_isOpen");
		query.setString("v_isOpen", isOpen);
		List<CompanyActivity> list = query.list();
		return list;
	}

	// 商家列表
	@Override
	public List<CompanyActivity> getCompany(int activityId, int start, int limit) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createQuery("SELECT o FROM CompanyActivity o WHERE o.activityId = :v_activityId and o.isOpen = 1 Group By o.companyId");
		query.setInteger("v_activityId", activityId);
		query.setFirstResult(start).setMaxResults(limit);
		List<CompanyActivity> list = query.list();
		return list;
	}
	
	// 商家列表
	@Override
	public List<CompanyActivity> ngetCompany(int activityId, int start, int limit) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createQuery("SELECT o FROM CompanyActivity o WHERE o.activityId = :v_activityId and o.isOpen = 1");
		query.setInteger("v_activityId", activityId);
		query.setFirstResult(start).setMaxResults(limit);
		List<CompanyActivity> list = query.list();
		return list;
	}

}