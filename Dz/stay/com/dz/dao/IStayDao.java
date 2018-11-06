package com.dz.dao;

import java.util.List;

import com.dz.entity.Stay;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IStayDao {
 
	List<Stay> stayList(String sql);		//查询
	
	List<Stay> getStay(int companyId,int haveRoom);
	
	Stay Stay(int goodsId);
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final Stay stay);		//添加或修改信息
	
}

 
