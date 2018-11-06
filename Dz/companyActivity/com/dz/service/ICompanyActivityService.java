package com.dz.service;

import java.util.List;

import com.dz.entity.CompanyActivity;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface ICompanyActivityService {

	List<CompanyActivity> companyActivityList(CompanyActivity companyActivity);
	
	List<CompanyActivity> getList(int companyId, String type);

	List<CompanyActivity> getList(int companyId);
	
	List<CompanyActivity> getList(String isOpen);
	
	List<CompanyActivity> companyActivity(int companyId, int activityId, String type);
	
	List<CompanyActivity> companyActivity(int companyId, String type, String date);
	
	List<CompanyActivity> getTime(int companyId, String date, String type);
	
	List<CompanyActivity> getTimeIsNot(int companyId, int activityId, String date, String type);
	
	CompanyActivity getCompanyActivity(int id);
	
	Long count(int id);

	public void delete(final String id);

	public void saveORupdate(final CompanyActivity companyActivity);
	
	List<CompanyActivity> getCompany(int activityId, int start, int limit);
	
	List<CompanyActivity> ngetCompany(int activityId, int start, int limit);

}
