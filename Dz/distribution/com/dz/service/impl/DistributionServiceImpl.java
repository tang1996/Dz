package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IDistributionDao;
import com.dz.entity.Distribution;
import com.dz.service.IDistributionService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("distributionService")
public class DistributionServiceImpl implements IDistributionService{

	@Autowired
	IDistributionDao distributionDao;
	
	@Override
	public List<Distribution> distributionList(Distribution distribution) {
		String sql = "SELECT o FROM Distribution o WHERE 1=1";
		return distributionDao.distributionList(sql);
	}
	
	@Override
	public Distribution getDistribution(int companyId) {
		return distributionDao.getDistribution(companyId);
	}
	
	@Override
	public Distribution find(int id) {
		return distributionDao.find(id);
	}
	
	@Override
	public void delete(final String id) {
		distributionDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Distribution distribution) {
		distributionDao.saveORupdate(distribution);
	}
	
}