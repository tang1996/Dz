package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IStaffDao;
import com.dz.entity.Staff;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("staffDao")
@SuppressWarnings("unchecked")
public class StaffDaoImpl implements IStaffDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	// 用户登录
	@Override
	public Staff login(String userName) {
		String sql ="SELECT o FROM Staff o WHERE o.userName =:v_username";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_username", userName);
		List<Staff> staffs = query.list();

		if (staffs != null && staffs.size() > 0) {
			return staffs.get(0);
		}
		return null;
	}

	// 用户列表
	@Override
	public List<Staff> staffList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Staff> staffList = query.list();
		return staffList;
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM Staff o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改用户信息
	@Override
	public void saveORupdate(Staff staff) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(staff);
	}

	// 按id查找用户
	@Override
	public Staff getid(final int id) {
		String sql = "SELECT o FROM Staff o WHERE id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<Staff> _li = query.list();

		if (_li.size() > 0)
			return _li.get(0);
		else
			return null;
	}

	// 按账号查找用户
	@Override
	public List<Staff> getuserName(final String userName) {
		String sql = "SELECT o FROM Staff o WHERE o.userName=:v_userName";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_userName", userName);
		List<Staff> _li = query.list();
		return _li;
	}
	
	// 按账公司查询
	@Override
	public List<Staff> getCompany(final String companyId) {
		String sql = "SELECT o FROM Staff o WHERE o.companyId=:v_companyId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_companyId", companyId);
		List<Staff> _li = query.list();
		return _li;
	}

	// 按账号查找用户
	@Override
	public Staff gettoken(final String token) {
		String sql = "SELECT o FROM Staff o WHERE o.token=:v_token";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_token", token);
		List<Staff> _li = query.list();
		if (_li.size() > 0)
			return _li.get(0);
		else
			return null;
	}
	
	// 按账号查找用户
	@Override
	public List<Staff> getList(int companyId, int powerSortId) {
		String sql = "SELECT o FROM Staff o WHERE o.companyId=:v_companyId and o.powerSortId=:v_powerSortId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_companyId", companyId);
		query.setInteger("v_powerSortId", powerSortId);
		List<Staff> _li = query.list();
		return _li;
	}

}