package com.dz.dao;

import java.util.List;

import com.dz.entity.Delicacy;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IDelicacyDao {
 
	List<Delicacy> delicacyList(String sql);		//查询
	
	Delicacy getDelicacy(int companyId);
	
	Delicacy find(int id);
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final Delicacy delicacy);		//添加或修改信息
	
}

 
