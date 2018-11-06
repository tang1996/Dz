package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IDisTimeDao;
import com.dz.entity.DisTime;
import com.dz.service.IDisTimeService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("disTimeService")
public class DisTimeServiceImpl implements IDisTimeService{

	@Autowired
	IDisTimeDao disTimeDao;
	
	@Override
	public List<DisTime> disTimeList(DisTime disTime) {
		String sql = "SELECT o FROM DisTime o WHERE 1=1";
		return disTimeDao.disTimeList(sql);
	}
	
	@Override
	public DisTime getDisTime(int companyId) {
		return disTimeDao.getDisTime(companyId);
	}
	
	@Override
	public void delete(final String id) {
		disTimeDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final DisTime disTime) {
		disTimeDao.saveORupdate(disTime);
	}
	
}