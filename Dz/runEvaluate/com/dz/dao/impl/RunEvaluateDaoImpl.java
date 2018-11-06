package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IRunEvaluateDao;
import com.dz.entity.RunEvaluate;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("runEvaluateDao")
@SuppressWarnings("unchecked")
public class RunEvaluateDaoImpl implements IRunEvaluateDao {

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
	public List<RunEvaluate> runEvaluateList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<RunEvaluate> runEvaluateList = query.list();
		return runEvaluateList;
	}

	// 用户列表
	@Override
	public RunEvaluate getrunEvaluate(int goodsId) {
		String sql = "SELECT o FROM RunEvaluate o WHERE o.goodsId=:v_goodsId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_goodsId", goodsId);
		List<RunEvaluate> runEvaluateList = query.list();
		if (runEvaluateList.size() > 0) {
			return runEvaluateList.get(0);
		} else {
			return null;
		}
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM RunEvaluate o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(RunEvaluate runEvaluate) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(runEvaluate);
	}

	// ==========================================管理后台
	@Override // ynw xm
	public List<RunEvaluate> getrunEvaluate(String sql, int start, int limit) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(
				sql);/* .setFirstResult(start).setMaxResults(limit); */
		List<RunEvaluate> runEvaluateList = query.list();
		return runEvaluateList;
	}

	@Override
	public Object[] getScore(int runId) {
		String sql = "SELECT (AVG(o.speen)+AVG(o.manner))/2,count(o.id) FROM RunEvaluate o WHERE o.runId=:v_runId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_runId", runId);
		List<Object[]> list = query.list();
		if (list.size() > 0) {
			Object[] obj = list.get(0);
			return obj;
		} else {
			return null;
		}
	}

}