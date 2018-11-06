package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IShopDao;
import com.dz.dao.IShopGoodsDao;
import com.dz.entity.ShopGoods;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("shopgoodsDao")
@SuppressWarnings("unchecked")
public class ShopGoodsDaoImpl implements IShopGoodsDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}


	// 删除
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM ShopGoods o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}
	
	// 删除
	@Override
	public void delete(int cid, int shopid) {
		String sql = "DELETE FROM ShopGoods o WHERE o.cId=:v_id and o.shop=:v_shopid";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", cid);
		query.setInteger("v_shopid", shopid);
		query.executeUpdate();
	}


	@Override
	public List<ShopGoods> getshop(int id) {
		String sql = "SELECT o FROM ShopGoods o WHERE o.shop=:v_id order by o.cId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		return (List<ShopGoods>)query.list();
	}

	@Override
	public List<ShopGoods> getshop(int cid, int shopid) {
		String sql = "SELECT o FROM ShopGoods o WHERE o.cId=:v_id and o.shop=:v_shopid";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", cid);
		query.setInteger("v_shopid", shopid);
		return (List<ShopGoods>)query.list();
	}
}