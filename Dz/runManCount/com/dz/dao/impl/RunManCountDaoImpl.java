package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IRunManCountDao;
import com.dz.entity.RunManCount;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("runManCountDao")
@SuppressWarnings("unchecked")
public class RunManCountDaoImpl implements IRunManCountDao {

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
	public List<RunManCount> runManCountList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<RunManCount> runManCountList = query.list();
		return runManCountList;
	}

	// 用户列表
	@Override
	public List<RunManCount> getrunManCount(int userId) {
		String sql = "SELECT o FROM RunManCount o WHERE o.userId=:v_userId and o.isShow = 1";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userId);
		List<RunManCount> runManCountList = query.list();
		if (runManCountList.size() > 0) {
			return runManCountList;
		} else {
			return null;
		}
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM RunManCount o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(RunManCount runManCount) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(runManCount);
	}

	// 获取商家总交易统计
	@Override
	public Object[] runManCount(int userId) {
		String sql = "SELECT count(o.id),sum(o.balance) FROM RunManCount o WHERE o.userId=:v_userId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userId);
		List<Object[]> runManCountList = query.list();
		if(runManCountList.size() > 0){
			Object[] count = runManCountList.get(0);
		return count;
		}else{
			return null;
		}
	}

	// 获取商家当天交易统计
	@Override
	public Object[] getRunManCount(int userId, String date) {
		String sql = "SELECT count(o.id),sum(o.balance) FROM RunManCount o WHERE o.userId=:v_userId and o.creatTime>=:v_staetDate and o.creatTime<=:v_endDate";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userId);
		query.setString("v_staetDate", date + " 00:00:00");
		query.setString("v_endDate", date + " 23:59:59");
		List<Object[]> runManCountList = query.list();
		if (runManCountList.size() > 0) {
			Object[] count = runManCountList.get(0);
			System.out.println(count[0]);
			System.out.println(count[1]);
			return count;
		} else {
			return null;
		}
	}

}