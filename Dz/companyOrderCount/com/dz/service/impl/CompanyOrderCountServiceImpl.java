package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.ICompanyOrderCountDao;
import com.dz.entity.CompanyOrderCount;
import com.dz.service.ICompanyOrderCountService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("companyOrderCountService")
public class CompanyOrderCountServiceImpl implements ICompanyOrderCountService{

	@Autowired
	ICompanyOrderCountDao companyOrderCountDao;
	
	//删除
	@Override
	public void delete(final String id) {
		companyOrderCountDao.delete(id);
	}
	
	//添加或修改信息
	@Override
	public void saveORupdate(final CompanyOrderCount companyOrderCount) {
		companyOrderCountDao.saveORupdate(companyOrderCount);
	}
	
	//用户地址列表
	@Override
	public Object[] companyOrderCount(int companyId) {
		return companyOrderCountDao.companyOrderCount(companyId);
	}
	
	//用户默认地址
	@Override
	public Object[] getCompanyOrderCount(int companyId, String date) {
		return companyOrderCountDao.getCompanyOrderCount(companyId,date);
	}
	
	//单个地址查询
	@Override
	public CompanyOrderCount find(int id) {
		return companyOrderCountDao.find(id);
	}
}