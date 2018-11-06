package com.dz.dao;

import java.util.List;

import com.dz.entity.RiderInfo;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IRiderInfoDao {
 
	List<RiderInfo> riderInfoList(String sql);		//管理员列表
	
	public void delete(final String id) ;		//删除管理员
	
	public void saveORupdate(final RiderInfo riderInfo);		//添加或修改管理员信息
	
	RiderInfo getid(final int id);		//按id查询管理员
	
	RiderInfo getuserId(int userId);
	
}

 
