package com.dz.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IBookTimeDao;
import com.dz.entity.BookTime;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("bookTimeDao")
@SuppressWarnings("unchecked")
public class BookTimeDaoImpl implements IBookTimeDao {

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
	public List<BookTime> bookTimeList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<BookTime> bookTimeList = query.list();
		return bookTimeList;
	}

	// 按桌位查询
	@Override
	public List<BookTime> getBookTime(int reserveId, String date) {
		String sql = "SELECT o FROM BookTime o WHERE o.reserveId=:v_reserveId and o.reserveTime>=:v_date order by o.reserveTime";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_reserveId", reserveId);
		query.setString("v_date", date);
		List<BookTime> bookTimeList = query.list();
		return bookTimeList;
	}

	// 按订单号加时间查询
	@Override
	public List<BookTime> getBookTime(int order, String startTime, String endTime) {
		String sql = "SELECT o FROM BookTime o WHERE o.orderId=:v_orderId and o.reserveTime>=:v_startDate and o.reserveTime<=:v_endDate order by o.reserveTime";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_orderId", order);
		query.setString("v_startDate", startTime);
		query.setString("v_endDate", endTime);
		List<BookTime> bookTimeList = query.list();
		return bookTimeList;
	}

	// 按桌位查询
	@Override
	public List<BookTime> getCompanyBookTime(int companyId, String date) {
		String sql = "SELECT o FROM BookTime o WHERE o.companyId=:v_companyId and o.creatTime>=:v_date";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setString("v_date", date + " 00:00:00");
		List<BookTime> bookTimeList = query.list();
		return bookTimeList;
	}

	// 按桌位查询
	@Override
	public Long getReserve(int companyId, String date) {
		String sql = "SELECT count(o) FROM BookTime o WHERE o.companyId=:v_companyId and o.reserveTime>=:v_startTime and o.reserveTime <=:v_endTime";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setString("v_startTime", date + " 00:00:00");
		query.setString("v_endTime", date + " 23:59:59");
		List bookTimeList = query.list();

		if (bookTimeList.size() > 0) {
			return (Long) bookTimeList.get(0);
		} else {
			return 0l;
		}
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM BookTime o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(BookTime bookTime) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(bookTime);
	}

	@Override // 李海洋
	public List<BookTime> getCompanyBook(int companyId, String date) {
		String sql = "select o from BookTime o where o.companyId=:v_companyId and o.reserveTime>=:v_date";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setString("v_date", date);
		List<BookTime> bookTimeList = query.list();

		if (bookTimeList.size() > 0) {
			return bookTimeList;
		} else {
			return null;
		}
	}

	@Override // 李海洋
	public List<BookTime> getCompanyBook(int companyId, String datestart, String dateend) {
		String sql = "select o from BookTime o where o.companyId=:v_companyId and o.reserveTime>=:v_datestart and o.reserveTime < :v_dateend";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setString("v_datestart", datestart);
		query.setString("v_dateend", dateend);
		List<BookTime> bookTimeList = query.list();
		if (bookTimeList.size() > 0) {
			return bookTimeList;
		} else {
			return null;
		}
	}

	@Override // 调用存储过程取已订菜分页 ynw
	public List<Object[]> getCompanyBookByCall(int companyId, String datestart, // 调用存储过程
																				// ynw
			String dateend, int Page, String sql) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("{CALL getOrderbooktime(?,?,?,?,?)}");
		query.setString(0, datestart);
		query.setString(1, dateend);
		query.setString(2, companyId + "");
		query.setInteger(3, Page);
		query.setString(4, sql);
		// query.setFirstResult(0).setMaxResults(16);
		List<Object[]> list = query.list();
		return list;
	}

	@Override // 调用存储过程取已订菜分页总数 ynw
	public BigInteger getCompanyBookCountByCall(int companyId, String datestart, String dateend, int Page, String sql) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("{CALL getOrderbooktimeCount(?,?,?,?,?)}");
		query.setString(0, datestart);
		query.setString(1, dateend);
		query.setString(2, companyId + "");
		query.setInteger(3, 0);
		query.setString(4, sql);
		List<BigInteger> list = query.list();
		return list.get(0);
	}

	@Override // 调用存储过程取未订菜分页 ynw
	public List<Object[]> getNOCompanyBookByCall(int companyId, String datestart, String dateend, int Page,
			String sql) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("{CALL getNoOrderbooktime(?,?,?,?,?)}");
		query.setString(0, datestart);
		query.setString(1, dateend);
		query.setString(2, companyId + "");
		query.setInteger(3, Page);
		query.setString(4, sql);
		// query.setFirstResult(0).setMaxResults(16);
		List<Object[]> list = query.list();
		return list;
	}

	@Override // 调用存储过程取未订菜分页总数 ynw
	public BigInteger getNOCompanyBookCountByCall(int companyId, String datestart, String dateend, int Page,
			String sql) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("{CALL getNoOrderbooktimeCount(?,?,?,?,?)}");
		query.setString(0, datestart);
		query.setString(1, dateend);
		query.setString(2, companyId + "");
		query.setInteger(3, 0);
		query.setString(4, sql);
		List<BigInteger> list = query.list();
		return list.get(0);
	}

	// 按桌位查询
	@Override
	public BookTime getBookTime(int orderId) {
		String sql = "SELECT o FROM BookTime o WHERE o.orderId=:v_orderId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_orderId", orderId);
		List<BookTime> bookTimeList = query.list();
		if (bookTimeList.size() > 0) {
			return bookTimeList.get(0);
		} else {
			return null;
		}
	}

}