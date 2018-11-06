package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.ITavernDao;
import com.dz.entity.Tavern;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("tavernDao")
@SuppressWarnings("unchecked")
public class TavernDaoImpl implements ITavernDao {

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
	public List<Tavern> tavernList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Tavern> tavernList = query.list();
		return tavernList;
	}

	// 按商家查询
	@Override
	public List<Tavern> getTavern(int stayId) {
		String sql = "SELECT o FROM Tavern o WHERE o.stayId=:v_stayId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_stayId", stayId);
		List<Tavern> tavernList = query.list();
		return tavernList;
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM Tavern o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Tavern tavern) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(tavern);
	}

}