package com.dz.dao;

import java.util.List;

import com.dz.entity.CompanyPayRecord;

/**
 * 用户DAO
 * 
 * @author GJ
 * @date 2015年4月15日
 */
public interface ICompanyPayRecordDao {

	public void delete(final String id); // 删除

	public void saveORupdate(final CompanyPayRecord companyPayRecord); // 添加或修改信息

	List<CompanyPayRecord> companyPayRecord(int CompanyId);// 商家总交易统计

	List<CompanyPayRecord> getCompanyPayRecord(int CompanyId, String date);// 商家当日交易统计

	CompanyPayRecord find(int companyId);// 单个地址查询

	List<CompanyPayRecord> getList(String sql, String startDate, String endDate, int start, int limit);//

	Long getList(String sql, String startDate, String endDate);//

	List<CompanyPayRecord> getPayView();
	
	List<CompanyPayRecord> companyPayRecordTotal(int CompanyId);// 商家总交易统计	ynw
	
}
