package com.dz.dao;

import java.util.List;

import com.dz.entity.Label;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface ILabelDao {
 
	List<Label> labelList(String sql);		//查询
	
	List<Label> getLabel(String type,String customId);
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final Label label);		//添加或修改信息
	
	Long count(String sql, String type);
	
}

 
