package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IRiderOrderCountDao;
import com.dz.entity.RiderOrderCount;


@Repository("riderOrderCountDao")
@SuppressWarnings("unchecked")
public class RiderOrderCountDaoImpl implements IRiderOrderCountDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	@Override	//删除
	public void delete(String id) {
		String sql = "DELETE FROM RiderOrderCount o WHERE o.id=:v_id";	
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	@Override	//按ID查找对应数据
	public RiderOrderCount getid(int id) {
		String sql = "SELECT o FROM RiderOrderCount o WHERE id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<RiderOrderCount> _li = query.list();

		if (_li.size() > 0)
			return _li.get(0);
		else
			return null;
	}

	@Override	//保存或更新
	public void saveORupdate(RiderOrderCount riderOrderCount) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(riderOrderCount);
	}
	
}
