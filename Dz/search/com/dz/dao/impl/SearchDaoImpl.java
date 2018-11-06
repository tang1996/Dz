package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.ISearchDao;
import com.dz.entity.Search;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("searchDao")
@SuppressWarnings("unchecked")
public class SearchDaoImpl implements ISearchDao {

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
	public List<Search> searchList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Search> searchList = query.list();
		return searchList;
	}

	// 用户搜索
	@Override
	public List<Search> userSearch(int userId) {
		String sql = "SELECT o FROM Search o WHERE o.userId=:v_userId ORDER BY o.id DESC";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userId);
		query.setFirstResult(0).setMaxResults(6);
		List<Search> searchList = query.list();
		return searchList;
	}

	// 用户搜索
	@Override
	public List<Object[]> countSearch() {
		String sql = "SELECT count(o.id),o.keyword,o.id FROM Search o GROUP BY o.keyword";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setFirstResult(0).setMaxResults(6);
		List<Object[]> searchList = query.list();
		return searchList;
	}

	@Override
	public List<Search> getSearch(String keyword, String location) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "SELECT o FROM Search o WHERE o.keyword=:v_keyword and o.";
		Query query = session.createQuery(sql);
		query.setString("v_keyword", keyword);
		List<Search> searchList = query.list();
		if (searchList.size() > 0) {
			return searchList;
		} else {
			return null;
		}
	}

	// 删除用户
	@Override
	public void delete(String id) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "DELETE FROM Search o WHERE o.id=:v_id";
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Search search) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(search);
	}

	// 删除用户
	@Override
	public void userDelete(int id) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "DELETE FROM Search o WHERE o.userId=:v_id";
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		query.executeUpdate();
	}

}