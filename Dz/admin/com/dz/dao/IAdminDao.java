package com.dz.dao;
 
import java.util.List;

import com.dz.entity.Admin;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IAdminDao {
 
	Admin login(String sql, String userName);	//管理员登录
	
	List<Admin> adminList(String sql);		//管理员列表
	
	public void delete(final String sql, final String id) ;		//删除管理员
	
	public void saveORupdate(final Admin admin);		//添加或修改管理员信息
	
	Admin getid(final String sql, final int id);		//按id查询管理员
	
	List<Admin> getuserName(final String sql, final String userName);		//按账号查询管理员
	
}

 
