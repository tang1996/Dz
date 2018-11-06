package com.dz.dao;

import java.util.List;

import com.dz.entity.Nature;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface INatureDao {
 
	List<Nature> natureList(String sql);		//查询
	
	List<Nature> getnature(int goodsid);
	
	Nature nature(int id);
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final Nature nature);		//添加或修改信息
	
}

 
