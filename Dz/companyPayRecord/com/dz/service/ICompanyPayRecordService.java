package com.dz.service;

import java.util.List;

import com.dz.entity.CompanyPayRecord;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface ICompanyPayRecordService {

	public void delete(final String id);// 删除

	public void saveORupdate(final CompanyPayRecord companyPayRecord);// 添加或修改信息

	List<CompanyPayRecord> companyPayRecord(int companyId);// 商家总交易统计

	List<CompanyPayRecord> getCompanyPayRecord(int companyId, String date); // 商家当日交易统计

	CompanyPayRecord find(int id); // 单个地址查询

	List<CompanyPayRecord> getList(Integer companyId, String startDate, String endDate, int start, int limit);//
	
	Long getList(Integer companyId, String startDate, String endDate);//
	
	List<CompanyPayRecord> getPayView();
	
	List<CompanyPayRecord> companyPayRecordTotal(int companyId);// 商家总交易统计  ynw

}
