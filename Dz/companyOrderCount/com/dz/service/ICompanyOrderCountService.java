package com.dz.service;

import java.util.List;

import com.dz.entity.CompanyOrderCount;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface ICompanyOrderCountService {

	public void delete(final String id);//删除

	public void saveORupdate(final CompanyOrderCount companyOrderCount);//添加或修改信息
	
	Object[] companyOrderCount(int companyId);//商家总交易统计
	
	Object[] getCompanyOrderCount(int companyId, String date);  //商家当日交易统计
	
	CompanyOrderCount find(int id);  //单个地址查询

}
