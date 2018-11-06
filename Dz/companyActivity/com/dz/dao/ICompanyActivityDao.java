package com.dz.dao;

import java.util.List;

import com.dz.entity.CompanyActivity;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface ICompanyActivityDao {
 
	List<CompanyActivity> companyActivityList(String sql);		//查询
	
	List<CompanyActivity> getList(int id, String type);		//通过活动id查询商家列表
	
	List<CompanyActivity> companyActivity(int companyId, int activityId, String type);
	
	List<CompanyActivity> companyActivity(int companyId, String type, String date);
	
	List<CompanyActivity> getTime(int companyId, String date, String type);
	
	List<CompanyActivity> getTimeIsNot(int companyId, int activityId, String date, String type);
	
	List<CompanyActivity> getList(int companyId);
	
	CompanyActivity getCompanyActivity(int id);
	
	Long count(int id);
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final CompanyActivity companyActivity);		//添加或修改信息
	
	List<CompanyActivity> getList(String isOpen);
	
	List<CompanyActivity> getCompany(int activityId, int start, int limit);
	
	List<CompanyActivity> ngetCompany(int activityId, int start, int limit);
	
}

 
