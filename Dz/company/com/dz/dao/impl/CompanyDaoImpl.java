package com.dz.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dz.dao.ICompanyDao;
import com.dz.entity.Company;

/**
 * 用户DAO实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Repository("companyDao")
@SuppressWarnings("unchecked")
public class CompanyDaoImpl implements ICompanyDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public List<Company> getList(int start, int limit){
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Company  o WHERE o.isOpen=1").setFirstResult(start)
				.setMaxResults(limit);
		List<Company> companyList = query.list();
		return companyList;
	}

	// 商家列表
	@Override
	public List<Company> companyList(String sql, int start, int limit) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql).setFirstResult(start)
				.setMaxResults(limit);
		List<Company> companyList = query.list();
		return companyList;
	}

	// 商家列表
	@Override
	public List<Company> searchCompany(String sql, int start, int limit) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql).setFirstResult(start)
				.setMaxResults(limit);
		List<Company> companyList = query.list();
		return companyList;
	}

	// 未审核商家列表
	@Override
	public List<Company> auditList() {
		String sql = "SELECT o FROM Company o WHERE audit = 0";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Company> companyList = query.list();
		if (companyList.size() > 0) {
			return companyList;
		} else {
			return null;
		}
	}

	// 用户列表
	@Override
	public List<Company> companyList(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Company> companyList = query.list();
		if (companyList.size() > 0) {
			return companyList;
		} else {
			return null;
		}
	}

	// 按分类查询商家
	@Override
	public List<Company> classifyCompany(String sql, int start, int limit) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setFirstResult(start).setMaxResults(limit);
		List<Company> companyList = query.list();
		return companyList;
	}

	// 商家总数
	@Override
	public Long count(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Long> list = query.list();
		if (list.size() > 0) {
			Long count = list.get(0);
			return count;
		} else {
			return null;
		}
	}

	// 分类商家总数
	@Override
	public Long classifycount(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		List<Long> list = query.list();
		if (list.size() > 0) {
			Long count = list.get(0);
			return count;
		} else {
			return null;
		}
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(Company company) {
		Session session = sessionFactory.getCurrentSession();
		session.update(company);
		session.flush();
	}

	@Override
	public Company findCompany(int id) {
		String sql = "SELECT o FROM Company o WHERE o.id=:v_id";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setInteger("v_id", id);
		List<Company> companyList = query.list();
		if (companyList.size() > 0) {
			return companyList.get(0);
		} else {
			return null;
		}
	}

	// 查询营业状态
	@Override
	public List<Company> getIsBusiness(String status) {
		String sql = "SELECT o FROM Company o WHERE o.isBusiness=:v_isBusiness";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		query.setString("v_isBusiness", status);
		List<Company> companyList = query.list();
		return companyList;
	}
	
	//管理后台
	// 删除商家
		@Override
		public void delete(int id) {
			String sql = "DELETE FROM Company o WHERE o.id=:v_id";
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(sql);
			query.setInteger("v_id", id);
			query.executeUpdate();
		}

		// 添加或修改信息
		@Override
		public void save(Company company) {
			Session session = sessionFactory.getCurrentSession();
			session.save(company);
		}

		// 通过审核
		@Override
		public void auditUpdate(final String id) {
			String sql = "UPDATE Company o SET o.audit=1,o.isOpen=1 WHERE o.id=:v_id";
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(sql);
			query.setString("v_id", id);
			query.executeUpdate();
		}

		@Override
		public Company getCompany(String phone) {
			String sql = "SELECT o FROM Company o WHERE o.phone=:v_phone";
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(sql);
			query.setString("v_phone", phone);
			List<Company> companyList = query.list();
			if (companyList.size() > 0) {
				return companyList.get(0);
			} else {
				return null;
			}
		}

}