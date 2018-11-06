package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IDisTimeDao;
import com.dz.entity.DisTime;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("disTimeDao")
@SuppressWarnings("unchecked")
public class DisTimeDaoImpl implements IDisTimeDao {

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
	public List<DisTime> disTimeList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<DisTime> disTimeList = query.list();
		return disTimeList;
	}

	// 查询单个属性
	@Override
	public DisTime getDisTime(int companyId) {
		String sql = "SELECT o FROM DisTime o WHERE o.companyId=:v_companyId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		List<DisTime> disTimeList = query.list();
		if (disTimeList.size() > 0) {
			return disTimeList.get(0);
		} else {
			return null;
		}
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM DisTime o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(DisTime disTime) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(disTime);
	}

}