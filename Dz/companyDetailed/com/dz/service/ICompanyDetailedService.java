package com.dz.service;


import java.util.List;

import com.dz.entity.CompanyDetailed;


/**
 * 通用dao，包括基本的CRUD方法
 * @author Ljh
 *
 */

public interface ICompanyDetailedService {
 
	List<CompanyDetailed> companyDetailedList(CompanyDetailed companyDetailed);
	
	public void delete(final String id);
	
	public void saveORupdate(final CompanyDetailed companyDetailed);
	
	CompanyDetailed getid(final int id);
	
	CompanyDetailed getCompany(int companyId);
	
	List<CompanyDetailed> companyDetailedList(CompanyDetailed companyDetailed, String startTime, String endTime);	/*ynw*/
	
}
 
