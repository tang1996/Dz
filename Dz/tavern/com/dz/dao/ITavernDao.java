package com.dz.dao;

import java.util.List;

import com.dz.entity.Tavern;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface ITavernDao {
 
	List<Tavern> tavernList(String sql);		//查询
	
	List<Tavern> getTavern(int stayId);
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final Tavern tavern);		//添加或修改信息
	
}

 
