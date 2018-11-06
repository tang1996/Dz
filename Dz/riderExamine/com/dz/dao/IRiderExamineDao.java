package com.dz.dao;

import java.util.List;

import com.dz.entity.RiderExamine;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IRiderExamineDao {
 
	RiderExamine login(String userName);	//管理员登录
	
	List<RiderExamine> riderExamineList(String sql);		//管理员列表
	
	public void delete(final String id) ;		//删除管理员
	
	public void saveORupdate(final RiderExamine riderExamine);		//添加或修改管理员信息
	
	RiderExamine getid(final int id);		//按id查询管理员
	
	List<RiderExamine> getuserName(final String userName);		//按账号查询管理员
	
	RiderExamine gettoken(String token);		//按用户token查询
	
}

 
