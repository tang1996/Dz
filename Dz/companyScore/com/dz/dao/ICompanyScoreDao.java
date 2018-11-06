package com.dz.dao;

import java.util.List;

import com.dz.entity.CompanyScore;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface ICompanyScoreDao {
 
	List<CompanyScore> companyScoreList(String sql);		//查询
	
	CompanyScore getcompanyScore(int evaluateid);
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final CompanyScore companyScore);		//添加或修改信息

}

 
