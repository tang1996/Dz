package com.dz.dao;

import java.util.List;

import com.dz.entity.Company;

/**
 * 用户DAO
 * 
 * @author GJ
 * @date 2015年4月15日
 */
public interface ICompanyDao {

	List<Company> companyList(String sql, int start, int limit); // 商家列表

	List<Company> searchCompany(String sql, int start, int limit); // 商家列表

	List<Company> classifyCompany(String sql, int start, int limit);// 按分类查询商家

	Long count(String sql);// 商家总数

	Long classifycount(String sql);// 分类商家总数

	Company findCompany(int id);// 查找单个商家

	public void saveORupdate(final Company company); // 添加或修改信息

	List<Company> companyList(String sql); // 查询

	List<Company> auditList();

	List<Company> getIsBusiness(String status);

	List<Company> getList(int start, int limit);

	// 管理后台
	public void delete(int id);// 删除

	public void save(final Company company);

	public void auditUpdate(final String id);

	Company getCompany(String phone);// 查询单个商家
}
