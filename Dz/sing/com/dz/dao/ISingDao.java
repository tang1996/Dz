package com.dz.dao;

import java.util.List;

import com.dz.entity.Sing;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface ISingDao {
 
	List<Sing> singList(String sql);		//查询
	
	Sing getSing(int companyId);
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final Sing sing);		//添加或修改信息
	
}

 
