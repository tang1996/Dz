package com.dz.dao;

import java.util.List;

import com.dz.entity.Meun;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IMeunDao {
 
	List<Meun> meunList(String sql);		//查询
	
	List<Meun> getmeun(int userId);
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final Meun meun);		//添加或修改信息
	
}

 
