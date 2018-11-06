package com.dz.dao;

import java.util.List;

import com.dz.entity.PowerSort;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IPowerSortDao {
 
	PowerSort getPowerSort(String position, int companyId);	//管理员登录
	
	List<PowerSort> powerSortList(String sql);		//管理员列表
	
	public void delete(final String id) ;		//删除管理员
	
	public void saveORupdate(final PowerSort powerSort);		//添加或修改管理员信息
	
	PowerSort getid(final int id);		//按id查询管理员
	
	List<PowerSort> getuserName(final String userName);		//按账号查询管理员
	
	PowerSort gettoken(String token);		//按用户token查询
	
}

 
