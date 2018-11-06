package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IDelicacyDao;
import com.dz.entity.Delicacy;
import com.dz.service.IDelicacyService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("delicacyService")
public class DelicacyServiceImpl implements IDelicacyService{

	@Autowired
	IDelicacyDao delicacyDao;
	
	@Override
	public List<Delicacy> delicacyList(Delicacy delicacy) {
		String sql = "SELECT o FROM Delicacy o WHERE 1=1";
		return delicacyDao.delicacyList(sql);
	}
	
	@Override
	public Delicacy getDelicacy(int companyId) {
		return delicacyDao.getDelicacy(companyId);
	}
	
	@Override
	public Delicacy find(int id) {
		return delicacyDao.find(id);
	}
	
	@Override
	public void delete(final String id) {
		delicacyDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Delicacy delicacy) {
		delicacyDao.saveORupdate(delicacy);
	}
	
}