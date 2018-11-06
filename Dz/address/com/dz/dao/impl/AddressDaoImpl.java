package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IAddressDao;
import com.dz.entity.Address;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("addressDao")
@SuppressWarnings("unchecked")
public class AddressDaoImpl implements IAddressDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	// 手机端地址列表
	@Override
	public List<Address> useraddress(int userid) {
		String sql = "SELECT o FROM Address o WHERE o.userId=:v_userId and o.isDelete=0";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userid);
		List<Address> addressList = query.list();

		return addressList;
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM Address o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Address address) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(address);
	}

	// 获取用户地址
	@Override
	public Address getAddress(int userid) {
		String sql = "SELECT o FROM Address o WHERE o.userId=:v_userId and o.isDefault=1 and o.isDelete=0";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userid);
		List<Address> addressList = query.list();
		if (addressList.size() > 0) {
			return addressList.get(0);
		} else {
			return null;
		}
	}
	
	// 获取用户地址
	@Override
	public Address get(int userid) {
		String sql = "SELECT o FROM Address o WHERE o.userId=:v_userId and o.isDelete=0";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userid);
		List<Address> addressList = query.list();
		if (addressList.size() > 0) {
			return addressList.get(0);
		} else {
			return null;
		}
	}
	
	// 获取用户默认地址
	@Override
	public Address find(int id) {
		String sql = "SELECT o FROM Address o WHERE o.id=:v_id and o.isDelete=0";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<Address> addressList = query.list();
		if (addressList.size() > 0) {
			return addressList.get(0);
		} else {
			return null;
		}
	}

}