package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IIficationDao;
import com.dz.entity.Ification;
import com.dz.service.IIficationService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("ificationService")
public class IficationServiceImpl implements IIficationService{

	@Autowired
	IIficationDao ificationDao;
	
	@Override
	public List<Ification> getIfication(int companyId){
		return ificationDao.getification(companyId);
	}
	
	@Override
	public Ification find(int id){
		return ificationDao.find(id);
	}
	
	
	@Override
	public void delete(final String id) {
		ificationDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Ification ification) {
		ificationDao.saveORupdate(ification);
	}
	
}