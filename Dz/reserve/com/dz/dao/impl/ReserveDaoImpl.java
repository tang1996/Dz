package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IReserveDao;
import com.dz.entity.Reserve;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("reserveDao")
@SuppressWarnings("unchecked")
public class ReserveDaoImpl implements IReserveDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override	//YNW
	public List<Reserve> getReserve(int companyId, String seat, String status) {
		String sql = "select o from Reserve o join o.companyId c where c.id=:v_companyId and o.seat=:v_seat and o.isOpen=1 and o.isDelete = 0 and o.status=:v_status";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setString("v_seat", seat);
		query.setString("v_status", status);
		List<Reserve> reserveList = query.list();
		return reserveList;
	}

	@Override	//YNW
	public List<Reserve> getReserveForTwo(int companyId,String seat){
		String sql = "from Reserve a,ComputerCate b,InsideOrder c where a.id=b.reserveId and b.insideorderId=c.id and a.status='2' and a.companyId=:v_companyId and a.seat=:v_seat and a.isOpen=1 and a.isDelete = 0";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setString("v_seat", seat);
		List<Reserve> reserveList = query.list();
		return reserveList;
	}
	
	// 用户列表
	@Override
	public List<Reserve> reserveList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Reserve> reserveList = query.list();
		return reserveList;
	}

	// 按商家查询
	@Override
	public List<Reserve> getReserve(int companyId, String seat) {
		String sql = "select o from Reserve o join o.companyId c where c.id=:v_companyId and o.seat=:v_seat and o.isOpen=1 and o.isDelete = 0";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setString("v_seat", seat);
		List<Reserve> reserveList = query.list();
		return reserveList;
	}
	
	// 按商家查询
	@Override
	public List<Reserve> getAllReserve(int companyId, String seat) {
		String sql = "SELECT o FROM Reserve o join o.companyId c where c.id=:v_companyId and o.seat=:v_seat and o.isDelete = 0";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setString("v_seat", seat);
		List<Reserve> reserveList = query.list();
		return reserveList;
	}

	// 按桌号查询
	@Override
	public Reserve getTable(String tableNo) {
		String sql = "SELECT o FROM Reserve o WHERE o.tableNo=:v_tableNo";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_tableNo", tableNo);
		List<Reserve> reserveList = query.list();
		if (reserveList.size() > 0) {
			return reserveList.get(0);
		} else {
			return null;
		}
	}
	
	// 按桌号查询
	@Override
	public Reserve find(int id) {
		String sql = "SELECT o FROM Reserve o WHERE o.id=:v_id and o.isDelete = 0";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<Reserve> reserveList = query.list();
		if (reserveList.size() > 0) {
			return reserveList.get(0);
		} else {
			return null;
		}
	}
	
	// 按桌号查询
	@Override
	public Reserve getTable(String tableNo, int companyId) {
		String sql = "SELECT o FROM Reserve o WHERE o.tableNo=:v_tableNo and o.companyId=:v_companyId and o.isDelete = 0";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_tableNo", tableNo);
		query.setInteger("v_companyId", companyId);
		List<Reserve> reserveList = query.list();
		if (reserveList.size() > 0) {
			return reserveList.get(0);
		} else {
			return null;
		}
	}
	
	// 按桌号查询
	@Override
	public Reserve getTable(String tableNo, int companyId, String seat) {
		String sql = "SELECT o FROM Reserve o WHERE o.tableNo=:v_tableNo and o.companyId=:v_companyId and o.seat=:v_seat and o.isDelete = 0";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_tableNo", tableNo);
		query.setString("v_seat", seat);
		query.setInteger("v_companyId", companyId);
		List<Reserve> reserveList = query.list();
		if (reserveList.size() > 0) {
			return reserveList.get(0);
		} else {
			return null;
		}
	}
	
	// 按桌号查询
	@Override
	public Reserve getTable(String tableNo, int companyId, String seat, String name) {
		String sql = "SELECT o FROM Reserve o WHERE o.tableNo=:v_tableNo and o.companyId=:v_companyId and o.seat=:v_seat and o.name like '%" + name + "%' and o.isDelete = 0";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_tableNo", tableNo);
		query.setString("v_tableNo", tableNo);
		query.setString("v_seat", seat);
		query.setInteger("v_companyId", companyId);
		List<Reserve> reserveList = query.list();
		if (reserveList.size() > 0) {
			return reserveList.get(0);
		} else {
			return null;
		}
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM Reserve o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Reserve reserve) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(reserve);
	}

}