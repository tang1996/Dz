package com.dz.service;

import java.util.List;

import com.dz.entity.Attribute;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IAttributeService {

	List<Attribute> attributeList(Attribute attribute);
	
	List<Attribute> getattribute(int goodsid);
	
	List<Attribute> attributelist(int id);
	
	List<Attribute> getList(int orderId,int goodsId);
	
	Attribute attribute(int goodsid);
	
	Attribute getAttribute(int id);

	public void delete(final String id);

	public void saveORupdate(final Attribute attribute);

}
