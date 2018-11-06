package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.ComputerCateDao;
import com.dz.entity.Cate;
import com.dz.entity.ComputerCate;
import com.dz.service.IComputerCateService;


@Repository("computerCateDao")
@SuppressWarnings("unchecked")
public class ComputerCateDaoImpl implements ComputerCateDao{

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	@Override
	public void saveORupdate(ComputerCate computerCate) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(computerCate);
	}

	@Override
	public ComputerCate getCate(String tableNo, int cid, String name,
			String seat) {
		String sql = "SELECT o FROM ComputerCate o WHERE o.tableNo=:v_tableNo and o.companyId=:v_cid and o.name=:v_name and o.seat=:v_seat order by o.id desc";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_tableNo", tableNo);
		query.setString("v_name", name);
		query.setString("v_seat", seat);
		query.setInteger("v_cid", cid);
		List<ComputerCate> list = query.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public ComputerCate getCate(int orderId) {
		String sql = "SELECT o FROM ComputerCate o WHERE o.insideorderId=:v_orderId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_orderId", orderId);
		List<ComputerCate> list = query.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM ComputerCate o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	@Override
	public ComputerCate getReserv(int reservId) {
		String sql = "SELECT o FROM ComputerCate o WHERE o.reserveId=:v_reservId order by o.id desc";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_reservId", reservId);
		List<ComputerCate> list = query.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	
}
