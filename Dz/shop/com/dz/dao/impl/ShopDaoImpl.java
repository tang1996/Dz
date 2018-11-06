package com.dz.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IShopDao;
import com.dz.entity.Goods;
import com.dz.entity.Shop;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("shopDao")
@SuppressWarnings("unchecked")
public class ShopDaoImpl implements IShopDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	// 列表
	@Override
	public List<Shop> shopList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Shop> meunList = query.list();
		return meunList;
	}

	// 删除
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM Shop o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Shop meun) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(meun);
	}

	@Override
	public Shop getshop(int id) {
		String sql = "SELECT o FROM Shop o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<Shop> meunList = query.list();
		if (meunList.size() > 0) {
			return meunList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Shop getByShop(int userid) {
		String sql = "SELECT o FROM Shop o WHERE o.userid=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", userid);
		List<Shop> meunList = query.list();
		if (meunList.size() > 0) {
			return meunList.get(0);
		} else {
			return null;
		}
	}
}