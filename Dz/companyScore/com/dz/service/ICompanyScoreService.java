package com.dz.service;

import java.util.List;

import com.dz.entity.CompanyScore;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 * 
 */

public interface ICompanyScoreService {

	List<CompanyScore> companyScoreList(CompanyScore companyScore);

	CompanyScore getCompanyScore(int evaluateid);
	
	public void delete(final String id);

	public void saveORupdate(final CompanyScore companyScore);

}
