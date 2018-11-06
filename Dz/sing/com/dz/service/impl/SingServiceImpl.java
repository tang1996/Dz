package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.ISingDao;
import com.dz.entity.Sing;
import com.dz.service.ISingService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("singService")
public class SingServiceImpl implements ISingService{

	@Autowired
	ISingDao singDao;
	
	@Override
	public List<Sing> singList(Sing sing) {
		String sql = "SELECT o FROM Sing o WHERE 1=1";
		return singDao.singList(sql);
	}
	
	@Override
	public Sing getSing(int companyId) {
		return singDao.getSing(companyId);
	}
	
	@Override
	public void delete(final String id) {
		singDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Sing sing) {
		singDao.saveORupdate(sing);
	}
	
}