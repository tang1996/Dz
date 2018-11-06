package com.dz.dao;

import java.util.List;

import com.dz.entity.SaleExamine;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface ISaleExamineDao {
 
	SaleExamine login(String userName);	//管理员登录
	
	List<SaleExamine> saleExamineList(String sql);		//管理员列表
	
	public void delete(final String id) ;		//删除管理员
	
	public void saveORupdate(final SaleExamine saleExamine);		//添加或修改管理员信息
	
	SaleExamine getid(final int id);		//按id查询管理员
	
	List<SaleExamine> getuserName(final String userName);		//按账号查询管理员
	
	SaleExamine gettoken(String token);		//按用户token查询
	
}

 
