package com.dz.dao;

import java.util.List;

import com.dz.entity.CompanyExamine;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface ICompanyExamineDao {
 
	CompanyExamine login(String userName);	//管理员登录
	
	List<CompanyExamine> companyExamineList(String sql);		//管理员列表
	
	public void delete(final String id) ;		//删除管理员
	
	public void saveORupdate(final CompanyExamine companyExamine);		//添加或修改管理员信息
	
	CompanyExamine getid(final int id);		//按id查询管理员
	
	List<CompanyExamine> getuserName(final String userName);		//按账号查询管理员
	
}

 
