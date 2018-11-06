package com.dz.service;


import java.util.List;

import com.dz.entity.SaleExamine;


/**
 * 通用dao，包括基本的CRUD方法
 * @author Ljh
 *
 */

public interface ISaleExamineService {
 
	List<SaleExamine> saleExamineList(SaleExamine saleExamine);
	
	public void delete(final String id);
	
	public void saveORupdate(final SaleExamine saleExamine);
	
	SaleExamine getid(final int id);
	
	List<SaleExamine> getuserName(final String userName);
	
	SaleExamine gettoken(String token);
	
}
 
