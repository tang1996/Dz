package com.dz.dao;

import java.util.List;

import com.dz.entity.Opinion;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IOpinionDao {
 
	List<Opinion> opinionList(String sql);		//查询
	
	Opinion opinion(int id);		//查询
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final Opinion opinion);		//添加或修改信息
	
	//管理后台
	List<Opinion> opinionList(String sql, int start, int limit);		//查询
	
}

 
