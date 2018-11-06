package com.dz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.ICompanyDao;
import com.dz.entity.Company;
import com.dz.service.ICompanyService;
import com.dz.util.StringUtil;

/**
 * 登陆业务实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Transactional(readOnly = false)
@Service("companyService")
public class CompanyServiceImpl implements ICompanyService {

	@Autowired
	ICompanyDao companyDao;

	// 商家列表
	@Override
	public List<Company> companyList(Company company, String sort, int start, int limit) {

		String sql = "SELECT o FROM Company o WHERE o.isOpen = 1";
		if (!StringUtil.isEmpty(sort)) {
			String type = "";
			if (sort.equals("volume")) {
				type = "o.monSales";
			}

			if (sort.equals("score")) {
				type = "o.assess";
			}
			if (!StringUtil.isEmpty(type)) {
				sql = sql + " ORDER BY " + type + " DESC";
			}
		} else {
			sql = "SELECT o FROM Company o WHERE o.isOpen = 1";
		}
		return companyDao.companyList(sql, start, limit);
	}

	// 商家列表
	@Override
	public List<Company> searchCompany(String keyword, String sort, int start, int limit) {

		String sql = "SELECT o FROM Company o WHERE o.isOpen = 1 and o.name like '%" + keyword + "%'";
		if (!StringUtil.isEmpty(sort)) {
			String type = "";
			if (sort.equals("volume")) {
				type = "o.monSales";
			}

			if (sort.equals("score")) {
				type = "o.assess";
			}
			if (!StringUtil.isEmpty(type)) {
				sql = sql + " ORDER BY " + type + " DESC";
			}
		} else {
			sql = "SELECT o FROM Company o WHERE o.isOpen = 1 and o.name like '%" + keyword + "%'";
		}
		return companyDao.searchCompany(sql, start, limit);
	}

	// 按分类查询商家
	@Override
	public List<Company> classifyCompany(Company company, String sort, int classify, int start, int limit) {

		String sql = "SELECT o FROM Company o WHERE o.isOpen = 1 and o.classifyId like '%" + classify + "%'";
		if (!StringUtil.isEmpty(company.getName())) {
			sql = sql + " and o.name like '%" + company.getName() + "%'";
		}

		if (!StringUtil.isEmpty(sort)) {
			String type = "";
			if (sort.equals("volume")) {
				type = "o.monSales";
			}

			if (sort.equals("score")) {
				type = "o.assess";
			}
			if (!StringUtil.isEmpty(type)) {
				sql = sql + " ORDER BY " + type + " ASC";
			}
		}
		return companyDao.classifyCompany(sql, start, limit);
	}

	// 商家总数
	@Override
	public Long count(Company company) {
		String sql = "SELECT COUNT(o) FROM Company o WHERE o.isOpen = 1";
		return companyDao.count(sql);
	}

	// 分类商家总数
	@Override
	public Long classifycount(Company company, int classify) {
		String sql = "SELECT COUNT(o) FROM Company o WHERE o.isOpen = 1 and o.classifyId = " + classify;
		if (!StringUtil.isEmpty(company.getName())) {
			sql = sql + " and o.name like '%" + company.getName() + "%'";
		}
		return companyDao.classifycount(sql);
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(final Company company) {
		companyDao.saveORupdate(company);
	}

	// 查询单个商家
	@Override
	public Company getCompany(int id) {
		return companyDao.findCompany(id);
	}

	public List<Company> getList(int start, int limit) {
		return companyDao.getList(start, limit);
	}

	@Override
	public List<Company> basecompanyList(Company company) {

		String sql = "SELECT o FROM Company o WHERE 1=1";
		if (!StringUtil.isEmpty(company.getName())) {
			sql = sql + " and o.name like '%" + company.getName() + "%'";
		}
		if (!StringUtil.isEmpty(company.getPosition())) {
			sql = sql + " and o.position like '%" + company.getPosition() + "%'";
		}
		return companyDao.companyList(sql);
	}

	@Override
	public List<Company> auditList() {
		return companyDao.auditList();
	}

	@Override
	public List<Company> getIsBusiness(String status) {
		return companyDao.getIsBusiness(status);
	}

	// 按子分类查询商家	2018-10-27 @Tyy
	@Override
	public List<Company> typeCompany(Company company, String sort, int classify, int start, int limit) {

		String sql = "SELECT o FROM Company o WHERE o.isOpen = 1 and o.type =" + classify;
		if (!StringUtil.isEmpty(company.getName())) {
			sql = sql + " and o.name like '%" + company.getName() + "%'";
		}

		if (!StringUtil.isEmpty(sort)) {
			String type = "";
			if (sort.equals("volume")) {
				type = "o.monSales";
			}

			if (sort.equals("score")) {
				type = "o.assess";
			}
			if (!StringUtil.isEmpty(type)) {
				sql = sql + " ORDER BY " + type + " ASC";
			}
		}
		return companyDao.classifyCompany(sql, start, limit);
	}
	
	//管理后台
	// 删除
		@Override
		public void delete(int id) {
			companyDao.delete(id);
		}
		
		@Override	/*ynw*/
		public List<Company> basecompanyList(Company company, int start, int limit) {

			String sql = "SELECT o FROM Company o WHERE audit=1";
			if (!StringUtil.isEmpty(company.getName())) {
				sql = sql + " and o.name like '%" + company.getName() + "%'";
			}
			if (!StringUtil.isEmpty(company.getInfo())) {
				sql = sql + "and o.info like '%" + company.getInfo() + "%'"; // 根据区域名得到商家列表
																				
			}
			return companyDao.companyList(sql,start,limit);
		}

		// 添加或修改信息
		@Override
		public void save(final Company company) {
			companyDao.save(company);
		}

		@Override
		public void auditUpdate(final String id) {
			companyDao.auditUpdate(id);
		}
		
		@Override
		public Company getCompany(String phone) {
			return companyDao.getCompany(phone);
		}
}