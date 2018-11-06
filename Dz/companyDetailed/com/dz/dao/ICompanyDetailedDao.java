package com.dz.dao;

import java.util.List;

import com.dz.entity.CompanyDetailed;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface ICompanyDetailedDao {
 
	CompanyDetailed login(String userName);	//管理员登录
	
	List<CompanyDetailed> companyDetailedList(String sql);		//管理员列表
	
	public void delete(final String id) ;		//删除管理员
	
	public void saveORupdate(final CompanyDetailed companyDetailed);		//添加或修改管理员信息
	
	CompanyDetailed getid(final int id);		//按id查询管理员
	
	CompanyDetailed getCompany(int companyId);
	
}

 
