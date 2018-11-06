package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.ICompanyScoreDao;
import com.dz.entity.CompanyScore;
import com.dz.service.ICompanyScoreService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("companyScoreService")
public class CompanyScoreServiceImpl implements ICompanyScoreService{

	@Autowired
	ICompanyScoreDao companyScoreDao;
	
	@Override
	public List<CompanyScore> companyScoreList(CompanyScore companyScore) {
		String sql = "SELECT o FROM CompanyScore o WHERE 1=1";
		return companyScoreDao.companyScoreList(sql);
	}
	
	@Override
	public CompanyScore getCompanyScore(int evaluateid) {
		return companyScoreDao.getcompanyScore(evaluateid);
	}
	
	@Override
	public void delete(final String id) {
		companyScoreDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final CompanyScore companyScore) {
		companyScoreDao.saveORupdate(companyScore);
	}

}