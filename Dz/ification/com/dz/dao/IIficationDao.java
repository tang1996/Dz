package com.dz.dao;

import java.util.List;

import com.dz.entity.Ification;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IIficationDao {
 
	List<Ification> getification(int companyId);		//查询
	
	Ification find(int id);		//查询
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final Ification ification);		//添加或修改信息
	
}

 
