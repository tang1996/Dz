package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IStayDao;
import com.dz.entity.Stay;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("stayDao")
@SuppressWarnings("unchecked")
public class StayDaoImpl implements IStayDao {

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
	public List<Stay> stayList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Stay> stayList = query.list();
		return stayList;
	}

	// 按商家查询
	@Override
	public List<Stay> getStay(int companyId, int haveRoom) {
		String sql = "SELECT o FROM Stay o WHERE o.companyId=:v_companyId and o.haveRoom=:v_haveRoom";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setInteger("v_haveRoom", haveRoom);
		List<Stay> stayList = query.list();
		return stayList;
	}

	// 按商品查询
	@Override
	public Stay Stay(int goodsId) {
		String sql = "SELECT o FROM Stay o WHERE o.goodsId=:v_goodsId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_goodsId", goodsId);
		List<Stay> stayList = query.list();
		if (stayList.size() > 0) {
			return stayList.get(0);
		} else {
			return null;
		}
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM Stay o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Stay stay) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(stay);
	}

}