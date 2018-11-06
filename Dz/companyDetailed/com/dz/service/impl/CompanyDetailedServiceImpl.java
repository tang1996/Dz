package com.dz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.ICompanyDetailedDao;
import com.dz.entity.CompanyDetailed;
import com.dz.service.ICompanyDetailedService;
import com.dz.util.StringUtil;

@Transactional(readOnly = false)
@Service("companyDetailedService")
public class CompanyDetailedServiceImpl implements ICompanyDetailedService {

	@Autowired
	ICompanyDetailedDao companyDetailedDao;

	@Override
	public List<CompanyDetailed> companyDetailedList(CompanyDetailed companyDetailed) {
		String sql = "SELECT o FROM CompanyDetailed o WHERE 1=1";
		if(companyDetailed.getSalerId() != null){		/*ynw start*/
			if(!StringUtil.isEmpty(companyDetailed.getSalerId().getPhone())){
				sql += " and o.salerId.phone ="+ companyDetailed.getSalerId().getPhone();	
			}
		
			if(!StringUtil.isEmpty(companyDetailed.getSalerId().getCity())){
				sql += " and o.salerId.city like '%"+ companyDetailed.getSalerId().getCity()+"%'";	/*ynw end*/
			}
		}
		return companyDetailedDao.companyDetailedList(sql);
	}
	
	@Override	/*ynw*/
	public List<CompanyDetailed> companyDetailedList(CompanyDetailed companyDetailed, String startTime, String endTime) {
		String sql = "SELECT o FROM CompanyDetailed o WHERE 1=1";
		if(companyDetailed.getSalerId() != null){		/*ynw start*/
			if(!StringUtil.isEmpty(companyDetailed.getSalerId().getPhone())){
				sql += " and o.salerId.phone ="+ companyDetailed.getSalerId().getPhone();	
			}
		
			if(!StringUtil.isEmpty(companyDetailed.getSalerId().getCity())){
				sql += " and o.salerId.city like '%"+ companyDetailed.getSalerId().getCity()+"%'";	/*ynw end*/
			}
		}
		
		if(!StringUtil.isEmpty(startTime)){
			sql += " and o.expireTime >= '"+ startTime + "'";
		}
		
		if(!StringUtil.isEmpty(endTime)){
			sql += " and o.expireTime <= '"+ endTime + "'";
		}
		return companyDetailedDao.companyDetailedList(sql);
	}

	@Override
	public void delete(final String id) {
		companyDetailedDao.delete(id);
	}

	@Override
	public void saveORupdate(final CompanyDetailed companyDetailed) {
		companyDetailedDao.saveORupdate(companyDetailed);
	}

	@Override
	public CompanyDetailed getid(final int id) {
		return companyDetailedDao.getid(id);
	}

	@Override
	public CompanyDetailed getCompany(int companyId) {
		return companyDetailedDao.getCompany(companyId);
	}

}
