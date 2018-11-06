package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IImagesDao;
import com.dz.entity.Images;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("imagesDao")
@SuppressWarnings("unchecked")
public class ImagesDaoImpl implements IImagesDao {

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
	public List<Images> imagesList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Images> imagesList = query.list();
		return imagesList;
	}
	
	@Override
	public Images getImage(String customId, String scene) {
		String sql = "SELECT o FROM Images o WHERE o.customId=:v_customId and o.scene=:v_scene";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_customId", customId);
		query.setString("v_scene", scene);
		List<Images> imagesList = query.list();
		if(imagesList.size()>0){
		return imagesList.get(0);
		}else{
			return null;
		}
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM Images o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Images images) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(images);
	}

}