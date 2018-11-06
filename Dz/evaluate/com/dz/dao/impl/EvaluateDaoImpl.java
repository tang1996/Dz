package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IEvaluateDao;
import com.dz.entity.Evaluate;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("evaluateDao")
@SuppressWarnings("unchecked")
public class EvaluateDaoImpl implements IEvaluateDao {

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
	public List<Evaluate> evaluateList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Evaluate> evaluateList = query.list();
		return evaluateList;
	}

	// 手机端评价列表
	@Override
	public List<Evaluate> userevaluate(int userid, int start, int limit) {
		String sql = "SELECT o FROM Evaluate o WHERE o.userId=:v_userId order by o.createTime DESC";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql).setFirstResult(start).setMaxResults(limit);
		query.setInteger("v_userId", userid);
		List<Evaluate> evaluateList = query.list();

		return evaluateList;

	}

	// 删除用户
	@Override
	public void delete(Evaluate evaluate) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(evaluate);
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Evaluate evaluate) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(evaluate);
	}

	@Override
	public List<Evaluate> getevaluate(String type, int customId) {
		String sql = "SELECT o FROM Evaluate o WHERE o.type=:v_type and o.customId=:v_customId order by createTime DESC";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_type", type);
		query.setInteger("v_customId", customId);
		List<Evaluate> evaluateList = query.list();
		return evaluateList;
	}
	
	@Override
	public List<Evaluate> getevaluate(String type, int customId, int start, int limit) {
		String sql = "SELECT o FROM Evaluate o WHERE o.type=:v_type and o.customId=:v_customId order by createTime DESC";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_type", type);
		query.setInteger("v_customId", customId);
		query.setFirstResult(start);
		query.setMaxResults(limit);
		List<Evaluate> evaluateList = query.list();
		return evaluateList;
	}

	@Override
	public List<Evaluate> getevaluate(String type, int customId,
			String typeClass, String isReply) {
		String sql = "SELECT o FROM Evaluate o WHERE o.type=:v_type and o.customId=:v_customId and o.isReply=:v_isReply";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_type", type);
		query.setString("v_typeClass", typeClass);
		query.setString("v_isReply", isReply);
		query.setInteger("v_customId", customId);
		List<Evaluate> evaluateList = query.list();
		return evaluateList;
	}

	@Override
	public List<Evaluate> getIsReply(String type, int customId) {
		String sql = "SELECT o FROM Evaluate o WHERE o.type=:v_type and o.customId=:v_customId and o.isReply=1 order by createTime DESC";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_type", type);
		query.setInteger("v_customId", customId);
		List<Evaluate> evaluateList = query.list();
		return evaluateList;
	}
	
	@Override
	public List<Evaluate> getTypeClass(String type, int customId) {
		String sql = "SELECT o FROM Evaluate o WHERE o.type=:v_type and o.customId=:v_customId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_type", type);
		query.setInteger("v_customId", customId);
		List<Evaluate> evaluateList = query.list();
		return evaluateList;
	}

	@Override
	public List<Evaluate> getTypeClass(String type, int customId, String typeClass, int start, int limit) {
		String sql = "SELECT o FROM Evaluate o WHERE o.type=:v_type and o.customId=:v_customId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_type", type);
		query.setInteger("v_customId", customId);
		query.setFirstResult(start);
		query.setMaxResults(limit);
		List<Evaluate> evaluateList = query.list();
		return evaluateList;
	}

	@Override
	public List<Evaluate> getevaluate(int orderId) {
		String sql = "SELECT o FROM Evaluate o WHERE o.orderId=:v_orderId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_orderId", orderId);
		List<Evaluate> evaluateList = query.list();
		return evaluateList;
	}

	@Override
	public Evaluate get(int id) {
		String sql = "SELECT o FROM Evaluate o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<Evaluate> evaluateList = query.list();
		
		if(evaluateList.size() > 0){
			return evaluateList.get(0);
		}else{
			return null;
		}
		
	}

}