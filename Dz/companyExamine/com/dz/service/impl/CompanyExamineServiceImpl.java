package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.ICompanyExamineDao;
import com.dz.entity.CompanyExamine;
import com.dz.service.ICompanyExamineService;
import com.dz.util.StringUtil;

@Transactional(readOnly=false)
@Service("companyExamineService")
public class CompanyExamineServiceImpl implements ICompanyExamineService{

	@Autowired
	ICompanyExamineDao companyExamineDao;
	
	@Override
	public List<CompanyExamine> companyExamineList(CompanyExamine companyExamine) {
		String sql = "SELECT o FROM CompanyExamine o WHERE 1=1";
		if(!StringUtil.isEmpty(companyExamine.getName())){
			sql = sql + " and o.Name = '" + companyExamine.getName() + "'";
		}
		return companyExamineDao.companyExamineList(sql);
	}
	
	@Override
	public void delete(final String id) {
		companyExamineDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final CompanyExamine companyExamine) {
		companyExamineDao.saveORupdate(companyExamine);
	}
	
	@Override
	public CompanyExamine getid(final int id) {
		return companyExamineDao.getid(id);
	}
	
	@Override
	public List<CompanyExamine> getuserName(final String userName) {
		return companyExamineDao.getuserName(userName);
	}

}
