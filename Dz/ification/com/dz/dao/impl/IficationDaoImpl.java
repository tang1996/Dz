package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IIficationDao;
import com.dz.entity.Ification;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("ificationDao")
@SuppressWarnings("unchecked")
public class IficationDaoImpl implements IIficationDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	// 按商家查询
	@Override
	public List<Ification> getification(int companyId) {
		String sql = "SELECT o FROM Ification o WHERE o.isView=1 and o.companyId=:v_companyId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		List<Ification> ificationList = query.list();
		return ificationList;
	}

	// 按ID查询
	@Override
	public Ification find(int id) {
		String sql = "SELECT o FROM Ification o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<Ification> ificationList = query.list();
		if (ificationList.size() > 0) {
			return ificationList.get(0);
		} else {
			return null;
		}
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM Ification o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Ification ification) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(ification);
	}

}