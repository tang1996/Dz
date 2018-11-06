package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.ILabelDao;
import com.dz.entity.Label;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("labelDao")
@SuppressWarnings("unchecked")
public class LabelDaoImpl implements ILabelDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	// 标签列表
	@Override
	public List<Label> labelList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Label> labelList = query.list();
		return labelList;
	}
	
	// 按类型获取标签
	@Override
	public List<Label> getLabel(String type,String customId) {
		String sql = "SELECT o FROM Label o WHERE o.type=:v_type and o.customId=:v_customId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_type", type);
		query.setString("v_customId", customId);
		List<Label> labelList = query.list();
		return labelList;
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM Label o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Label label) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(label);
	}
	
	// 标签总数
	@Override
	public Long count(String sql, String type) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_type", type);
		List<Long> list = query.list();
		if(list.size() > 0){
			Long count = list.get( 0 );
			return count;
		}else{
			return null;
		}
	}

}