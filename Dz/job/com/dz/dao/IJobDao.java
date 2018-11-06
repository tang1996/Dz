package com.dz.dao;

import java.util.List;

import com.dz.entity.Job;

/**
 * 用户DAO
 * 
 * @author GJ
 * @date 2015年4月15日
 */
public interface IJobDao {

	List<Job> jobList(String sql); // 查询

	List<Job> getjobList(String orderId);
	
	List<Job> getjobListBase(String orderId);

	List<Job> getOrderId(String companyId);

	Job job(int id);

	public void delete(final String id); // 删除

	public void saveORupdate(final Job job); // 添加或修改信息

	List<Job> getInsidejobList(String orderId); // 线下订单打印 ynw

	List<Job> getPrinte(int orderId, String printName);

	List<Job> getInsidePrinte(int orderId, String printName);

	List<String> getPrinteName(int companyId);
	
	List<Job> getList(int companyId);

}
