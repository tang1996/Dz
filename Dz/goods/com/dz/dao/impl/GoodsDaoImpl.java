package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IGoodsDao;
import com.dz.entity.Goods;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("goodsDao")
@SuppressWarnings("unchecked")
public class GoodsDaoImpl implements IGoodsDao {

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
	public List<Goods> goodsList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Goods> goodsList = query.list();
		return goodsList;
	}

	// 用户列表
	@Override
	public List<Goods> goodsList(int companyId, int ificationId) {
		String sql = "SELECT o FROM Goods o WHERE o.companyId=:v_companyId and o.ificationId=:v_ificationId and o.shelves=1 and o.isDelete=0";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setInteger("v_ificationId", ificationId);
		List<Goods> list = query.list();
		return list;
	}

	// 用户列表
	@Override
	public List<Goods> companyGoods(int companyId, int ificationId) {
		String sql = "SELECT o FROM Goods o WHERE o.companyId=:v_companyId and o.ificationId=:v_ificationId and o.isDelete=0";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setInteger("v_ificationId", ificationId);
		List<Goods> list = query.list();
		return list;
	}

	// 用户列表
	@Override
	public List<Goods> companyGoodsWm(int companyId, int ificationId, int type) {
		String sql = "SELECT o FROM Goods o WHERE o.companyId=:v_companyId and o.ificationId=:v_ificationId and o.classIf=:v_type and o.isDelete=0";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setInteger("v_ificationId", ificationId);
		query.setInteger("v_type", type);
		List<Goods> list = query.list();
		return list;
	}

	// 下架
	@Override
	public void down(int id, int shelves) {
		String sql = "UPDATE Goods o SET o.shelves=:v_shelves WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		query.setInteger("v_shelves", shelves);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Goods goods) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(goods);
	}

	// 添加或修改信息
	@Override
	public void update(Goods goods) {
		Session session = sessionFactory.getCurrentSession();
		session.update(goods);
		session.flush();
	}

	@Override
	public Goods getGoods(int id) {
		String sql = "SELECT o FROM Goods o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<Goods> list = query.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<Goods> getCGoods(int companyid, int type) {
		String sql = "SELECT o FROM Goods o WHERE o.companyId=:v_id and o.classIf=:v_classIf  and o.shelves = 1 and o.isDelete=0  ORDER BY o.ificationId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", companyid);
		query.setInteger("v_classIf", type);
		List<Goods> list = query.list();
		return list;
	}

	@Override
	public List<Goods> backGoods(int companyid, int type) {
		String sql = "SELECT o FROM Goods o WHERE o.companyId=:v_id and o.classIf=:v_classIf and o.isDelete=0 ORDER BY o.ificationId ";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", companyid);
		query.setInteger("v_classIf", type);
		List<Goods> list = query.list();
		return list;
	}

	@Override // 按商家和商家分类查询已上架的商品 ynw
	public List<Goods> computerGoodsWm(int companyId, int ificationId, int type) {
		String sql = "SELECT o FROM Goods o WHERE o.companyId=:v_companyId and o.ificationId=:v_ificationId and o.classIf=:v_type and o.shelves = 1 and o.isDelete=0";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setInteger("v_ificationId", ificationId);
		query.setInteger("v_type", type);
		List<Goods> list = query.list();
		return list;
	}

	// 管理后台
	public List<Goods> bastGoods(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Goods> goodsList = query.list();
		return goodsList;
	}

}