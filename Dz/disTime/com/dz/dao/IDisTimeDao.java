package com.dz.dao;

import java.util.List;

import com.dz.entity.DisTime;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IDisTimeDao {
 
	List<DisTime> disTimeList(String sql);		//查询
	
	DisTime getDisTime(int userId);
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final DisTime disTime);		//添加或修改信息
	
}

 
