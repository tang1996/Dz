package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.IWalletDao;
import com.dz.entity.Wallet;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("walletDao")
@SuppressWarnings("unchecked")
public class WalletDaoImpl implements IWalletDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	// 删除用户
	@Override
	public void delete(String id) {
		String sql = "DELETE FROM Wallet o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_id", id);
		query.executeUpdate();
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Wallet wallet) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(wallet);
	}

	@Override
	public Wallet userwallet(int userId) {
		String sql = "SELECT o FROM Wallet o WHERE o.userId=:v_userId";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_userId", userId);
		List<Wallet> walletList = query.list();
		if (walletList.size() > 0) {
			return walletList.get(0);
		} else {
			return null;
		}
	}

}