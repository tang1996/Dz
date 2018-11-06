package com.dz.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IGoodsActivityDao;
import com.dz.entity.GoodsActivity;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("goodsActivityDao")
@SuppressWarnings("unchecked")
public class GoodsActivityDaoImpl implements IGoodsActivityDao {

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
	public List<GoodsActivity> goodsActivityList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<GoodsActivity> goodsActivityList = query.list();
		return goodsActivityList;
	}

	// 商家列表
	@Override
	public List<GoodsActivity> getList(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd :HH:mm:ss");
		Query query = session
				.createQuery("SELECT o FROM GoodsActivity o WHERE o.activityId = :v_activityId and o.startTime >= :v_startTime and o.endTime <= :v_endTime");
		query.setInteger("v_activityId", id);
		query.setString("v_startTime", dateFormat.format(new Date()));
		query.setString("v_endTime", dateFormat.format(new Date()));
		List<GoodsActivity> list = query.list();
		if (list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM GoodsActivity o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(GoodsActivity goodsActivity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(goodsActivity);
	}

}