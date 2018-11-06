package com.dz.dao;

import java.util.List;

import com.dz.entity.Include;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IIncludeDao {
 
	List<Include> includeList(String sql);		//查询
	
	Include include(int id);		//查询
	
	Include getInclude(int id, int companyId);		//查询
	
	List<Include> getInclude(int userId);
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final Include include);		//添加或修改信息
	
}

 
