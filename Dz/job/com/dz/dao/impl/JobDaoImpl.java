package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IJobDao;
import com.dz.entity.Job;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("jobDao")
@SuppressWarnings("unchecked")
public class JobDaoImpl implements IJobDao {

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
	public List<Job> jobList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Job> jobList = query.list();
		return jobList;
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM Job o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Job job) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(job);
	}

	@Override
	public List<Job> getjobList(String orderId) {
		String sql = "SELECT o FROM Job o WHERE o.orderId=:v_orderId and o.isPrint = '0'";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_orderId", orderId);
		List<Job> jobList = query.list();
		return jobList;
	}
	
	@Override
	public List<Job> getjobListBase(String orderId) {
		String sql = "SELECT o FROM Job o WHERE o.orderId=:v_orderId and o.isPrintBase = '0'";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_orderId", orderId);
		List<Job> jobList = query.list();
		return jobList;
	}
	
	@Override
	public List<Job> getOrderId(String companyId) {
		String sql = "SELECT o FROM Job o WHERE o.companyId=:v_companyId and o.isPrint = '0' GROUP BY o.orderId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_companyId", companyId);
		List<Job> jobList = query.list();
		return jobList;
	}
	
	@Override
	public Job job(int id) {
		String sql = "SELECT o FROM Job o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<Job> jobList = query.list();
		if (jobList.size() > 0) {
			return jobList.get(0);
		} else {
			return null;
		}
	}
	
	@Override		//ynw
	public List<Job> getInsidejobList(String orderId) {
		String sql = "SELECT o FROM Job o WHERE o.InsideOrderId=:v_InsideOrderId and o.isPrint = '0'";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_InsideOrderId", orderId);
		List<Job> jobList = query.list();
		return jobList;
	}
	
	@Override
	public List<Job> getPrinte(int orderId, String printName) {
		String sql = "SELECT o FROM Job o WHERE o.orderId=:v_orderId and o.printName=:v_printName and o.isPrintBase = '0'";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_orderId", orderId);
		query.setString("v_printName", printName);
		List<Job> jobList = query.list();
		return jobList;
	}

	@Override
	public List<Job> getInsidePrinte(int orderId, String printName) {
		String sql = "SELECT o FROM Job o WHERE  o.InsideOrderId=:v_InsideOrderId and o.printName=:v_printName and o.isPrintBase = '0'";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_InsideOrderId", orderId);
		query.setString("v_printName", printName);
		List<Job> jobList = query.list();
		return jobList;
	}

	@Override
	public List<String> getPrinteName(int companyId) {
		String sql = "SELECT o.printName FROM Job o WHERE  o.companyId=:v_companyId and o.isPrintBase = '0' GROUP BY o.printName";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		List<String> jobList = query.list();
		return jobList;
	}
	
	@Override
	public List<Job> getList(int companyId) {
		String sql = "SELECT o FROM Job o WHERE  o.companyId=:v_companyId and o.isPrintBase = '0'";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		List<Job> jobList = query.list();
		return jobList;
	}

}