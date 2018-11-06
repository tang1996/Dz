package com.dz.dao;

import java.util.List;

import com.dz.entity.CompanyOrderCount;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface ICompanyOrderCountDao {
 
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final CompanyOrderCount companyOrderCount);		//添加或修改信息
	
	Object[] companyOrderCount(int CompanyId);//商家总交易统计
	
	Object[] getCompanyOrderCount(int CompanyId, String date);//商家当日交易统计
	
	CompanyOrderCount find(int companyId);//单个地址查询
}

 
