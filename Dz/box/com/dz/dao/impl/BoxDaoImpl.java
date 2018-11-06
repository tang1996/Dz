package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IBoxDao;
import com.dz.entity.Box;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("boxDao")
@SuppressWarnings("unchecked")
public class BoxDaoImpl implements IBoxDao {

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
	public List<Box> boxList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Box> boxList = query.list();
		return boxList;
	}

	// 按商家查询
	@Override
	public List<Box> getBox(int goodsId) {
		String sql = "SELECT o FROM Box o WHERE o.goodsId=:v_goodsId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_goodsId", goodsId);
		List<Box> boxList = query.list();
		return boxList;
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM Box o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Box box) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(box);
	}

}