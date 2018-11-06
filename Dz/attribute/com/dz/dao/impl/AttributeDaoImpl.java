package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IAttributeDao;
import com.dz.entity.Attribute;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("attributeDao")
@SuppressWarnings("unchecked")
public class AttributeDaoImpl implements IAttributeDao {

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
	public List<Attribute> attributeList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Attribute> attributeList = query.list();
		return attributeList;
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM Attribute o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Attribute attribute) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(attribute);
	}

	@Override
	public List<Attribute> getattribute(int goodsid) {
		String sql = "SELECT o FROM Attribute o WHERE o.goodsId=:v_goodsId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_goodsId", goodsid);
		List<Attribute> attributeList = query.list();
		return attributeList;
	}
	
	@Override
	public List<Attribute> attributelist(int id) {
		String sql = "SELECT o FROM Attribute o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<Attribute> attributeList = query.list();
		return attributeList;
	}
	
	public List<Attribute> getList(int orderId,int goodsId){
		String sql = "SELECT o FROM Attribute o WHERE o.orderId=:v_orderId and o.goodsId=:v_goodsId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_orderId", orderId);
		query.setInteger("v_goodsId", goodsId);
		List<Attribute> attributeList = query.list();
		return attributeList;
	}

	@Override
	public Attribute attribute(int goodsid) {
		String sql = "SELECT o FROM Attribute o WHERE o.goodsId=:v_goodsId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_goodsId", goodsid);
		List<Attribute> attributeList = query.list();
		if (attributeList.size() > 0) {
			return attributeList.get(0);
		} else {
			return null;
		}
	}
	
	@Override
	public Attribute getAttribute(int id) {
		String sql = "SELECT o FROM Attribute o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<Attribute> attributeList = query.list();
		if (attributeList.size() > 0) {
			return attributeList.get(0);
		} else {
			return null;
		}
	}

}