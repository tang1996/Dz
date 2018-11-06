package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IAttributeDao;
import com.dz.entity.Attribute;
import com.dz.service.IAttributeService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("attributeService")
public class AttributeServiceImpl implements IAttributeService{

	@Autowired
	IAttributeDao attributeDao;
	
	@Override
	public List<Attribute> attributeList(Attribute attribute) {
		String sql = "SELECT o FROM Attribute o WHERE 1=1";
		return attributeDao.attributeList(sql);
	}
	
	
	@Override
	public List<Attribute> attributelist(int id) {
		return attributeDao.attributelist(id);
	}
	
	@Override
	public void delete(final String id) {
		attributeDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Attribute attribute) {
		attributeDao.saveORupdate(attribute);
	}

	@Override
	public List<Attribute> getattribute(int goodsid) {
		return attributeDao.getattribute(goodsid);
	}
	
	public List<Attribute> getList(int orderId,int goodsId){
		return attributeDao.getList(orderId, goodsId);
	}
	
	@Override
	public Attribute attribute(int goodsid) {
		return attributeDao.attribute(goodsid);
	}
	
	@Override
	public Attribute getAttribute(int id) {
		return attributeDao.getAttribute(id);
	}
	
}