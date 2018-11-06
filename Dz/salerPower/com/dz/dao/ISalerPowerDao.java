package com.dz.dao;

import java.util.List;

import com.dz.entity.SalerPower;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface ISalerPowerDao {
 
	SalerPower getSalerPower(String position, int companyId);	//管理员登录
	
	List<SalerPower> salerPowerList(String sql);		//管理员列表
	
	public void delete(final String id) ;		//删除管理员
	
	public void saveORupdate(final SalerPower salerPower);		//添加或修改管理员信息
	
	SalerPower getid(final int id);		//按id查询管理员
	
	List<SalerPower> getuserName(final String userName);		//按账号查询管理员
	
	SalerPower gettoken(String token);		//按用户token查询
	
}

 
