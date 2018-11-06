package com.dz.service;


import java.util.List;

import com.dz.entity.SalerCompany;


/**
 * 通用dao，包括基本的CRUD方法
 * @author Ljh
 *
 */

public interface ISalerCompanyService {
 
	
	List<SalerCompany> salerCompanyList(SalerCompany salerCompany);
	
	public void delete(final String id);
	
	public void saveORupdate(final SalerCompany salerCompany);
	
	SalerCompany getid(final int id);
	
	Object[] count(SalerCompany salerCompany);
	
	Object[] count(SalerCompany salerCompany, String startTime, String endTime);
	
}
 
