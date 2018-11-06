package com.dz.dao;

import java.util.List;

import com.dz.entity.Attribute;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IAttributeDao {
 
	List<Attribute> attributeList(String sql);		//查询
	
	List<Attribute> getattribute(int goodsid);
	
	List<Attribute> getList(int orderId,int goodsId);
	
	Attribute attribute(int goodsid);
	
	Attribute getAttribute(int id);
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final Attribute attribute);		//添加或修改信息
	
	List<Attribute> attributelist(int id);
	
}

 
