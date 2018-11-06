package com.dz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.ICompanyActivityDao;
import com.dz.entity.CompanyActivity;
import com.dz.service.ICompanyActivityService;

/**
 * 登陆业务实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Transactional(readOnly = false)
@Service("companyActivityService")
public class CompanyActivityServiceImpl implements ICompanyActivityService {

	@Autowired
	ICompanyActivityDao companyActivityDao;

	@Override
	public List<CompanyActivity> companyActivityList(CompanyActivity companyActivity) {
		String sql = "SELECT o FROM Relation o WHERE 1=1";
		return companyActivityDao.companyActivityList(sql);
	}
	
	@Override
	public List<CompanyActivity> getList(int companyId){
		return companyActivityDao.getList(companyId);
	}

	@Override
	public List<CompanyActivity> getList(int companyId, String type) {
		return companyActivityDao.getList(companyId, type);
	}
	
	@Override
	public  List<CompanyActivity> getTimeIsNot(int companyId, int activityId, String date, String type){
		return companyActivityDao.getTimeIsNot(companyId, activityId, date, type);
	}
	
	@Override
	public List<CompanyActivity>  getTime(int companyId, String date, String type) {
		return companyActivityDao.getTime(companyId, date, type);
	}
	
	@Override
	public List<CompanyActivity> getList(String isOpen) {
		return companyActivityDao.getList(isOpen);
	}
	
	@Override
	public List<CompanyActivity> companyActivity(int companyId, int activityId, String type) {
		return companyActivityDao.companyActivity(companyId, activityId, type);
	}
	
	@Override
	public List<CompanyActivity> companyActivity(int companyId, String type, String date) {
		return companyActivityDao.companyActivity(companyId, type, date);
	}
	
	@Override
	public CompanyActivity getCompanyActivity(int id) {
		return companyActivityDao.getCompanyActivity(id);
	}
	
	@Override
	public Long count(int id){
		return companyActivityDao.count(id);
	}
	
	@Override
	public void delete(final String id) {
		companyActivityDao.delete(id);
	}

	@Override
	public void saveORupdate(final CompanyActivity companyActivity) {
		companyActivityDao.saveORupdate(companyActivity);
	}
	
	@Override
	public List<CompanyActivity> getCompany(int activityId, int start, int limit) {
		return companyActivityDao.getCompany(activityId, start, limit);
	}
	
	@Override
	public List<CompanyActivity> ngetCompany(int activityId, int start, int limit){
		return companyActivityDao.ngetCompany(activityId, start, limit);
	}
}