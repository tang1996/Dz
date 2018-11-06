package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IMeunDao;
import com.dz.entity.Meun;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("meunDao")
@SuppressWarnings("unchecked")
public class MeunDaoImpl implements IMeunDao {

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
	public List<Meun> meunList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Meun> meunList = query.list();
		return meunList;
	}

	// 用户列表
	@Override
	public List<Meun> getmeun(int userId) {
		String sql = "SELECT o FROM Meun o WHERE o.userId=:v_userId and o.isShow = 1";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userId);
		List<Meun> meunList = query.list();
		if (meunList.size() > 0) {
			return meunList;
		} else {
			return null;
		}
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM Meun o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Meun meun) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(meun);
	}

}