package com.dz.service;


import java.util.List;

import com.dz.entity.CompanyExamine;


/**
 * 通用dao，包括基本的CRUD方法
 * @author Ljh
 *
 */

public interface ICompanyExamineService {
 
	List<CompanyExamine> companyExamineList(CompanyExamine companyExamine);
	
	public void delete(final String id);
	
	public void saveORupdate(final CompanyExamine companyExamine);
	
	CompanyExamine getid(final int id);
	
	List<CompanyExamine> getuserName(final String userName);
	
}
 
