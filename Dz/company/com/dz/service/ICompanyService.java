package com.dz.service;

import java.util.List;

import com.dz.entity.Company;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 * 
 */

public interface ICompanyService {

	Company getCompany(int id);// 查询单个商家

	List<Company> companyList(Company company, String sort, int start, int limit);
	
	List<Company> searchCompany(String keyword, String sort, int start, int limit);

	List<Company> classifyCompany(Company company, String sort, int classify,
			int start, int limit);

	Long count(Company company);

	Long classifycount(Company company, int classify);

	public void saveORupdate(final Company company);

	List<Company> basecompanyList(Company company);

	List<Company> auditList();
	
	List<Company> getIsBusiness(String status);
	
	List<Company> getList(int start, int limit);
	
	List<Company> typeCompany(Company company, String sort, int classify,
			int start, int limit);//2018-10-27 @Tyy
	
	//管理后台
	public void delete(int id);//删除

	public void save(final Company company);

	public void auditUpdate(final String id);
	
	List<Company> basecompanyList(Company company, int start, int limit);	/*ynw*/
	
	Company getCompany(String phone);// 查询单个商家

}
