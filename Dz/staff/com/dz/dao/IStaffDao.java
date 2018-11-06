package com.dz.dao;

import java.util.List;

import com.dz.entity.Staff;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IStaffDao {
 
	Staff login(String userName);	//管理员登录
	
	List<Staff> staffList(String sql);		//管理员列表
	
	public void delete(final String id) ;		//删除管理员
	
	public void saveORupdate(final Staff staff);		//添加或修改管理员信息
	
	Staff getid(final int id);		//按id查询管理员
	
	List<Staff> getuserName(final String userName);		//按账号查询管理员
	
	List<Staff> getCompany(final String companyId);		//按账公司查询
	
	Staff gettoken(String token);		//按用户token查询
	
	List<Staff> getList(int companyId, int powerSortId);
	
}

 
