package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IRunManCountDao;
import com.dz.entity.RunManCount;
import com.dz.entity.RunManCount;
import com.dz.service.IRunManCountService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("runManCountService")
public class RunManCountServiceImpl implements IRunManCountService{

	@Autowired
	IRunManCountDao runManCountDao;
	
	@Override
	public List<RunManCount> runManCountList(RunManCount runManCount) {
		String sql = "SELECT o FROM RunManCount o WHERE 1=1";
		return runManCountDao.runManCountList(sql);
	}
	
	@Override
	public List<RunManCount> getrunManCount(int userId) {
		return runManCountDao.getrunManCount(userId);
	}
	
	@Override
	public void delete(final String id) {
		runManCountDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final RunManCount runManCount) {
		runManCountDao.saveORupdate(runManCount);
	}
	
	//用户地址列表
	@Override
	public Object[] runManCount(int userId) {
		return runManCountDao.runManCount(userId);
	}
	
	//用户默认地址
	@Override
	public Object[] getRunManCount(int companyId, String date) {
		return runManCountDao.getRunManCount(companyId,date);
	}
	
}