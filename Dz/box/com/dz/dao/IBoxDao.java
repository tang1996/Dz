package com.dz.dao;

import java.util.List;

import com.dz.entity.Box;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IBoxDao {
 
	List<Box> boxList(String sql);		//查询
	
	List<Box> getBox(int goodsId);
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final Box box);		//添加或修改信息
	
}

 
