package com.dz.service;

import java.util.List;

import com.dz.entity.Job;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IJobService {

	List<Job> jobList(Job job);

	List<Job> getjobList(String orderId);
	
	List<Job> getjobListBase(String orderId);

	List<Job> getOrderId(String companyId);

	Job job(int id);

	public void delete(final String id);

	public void saveORupdate(final Job job);

	List<Job> getInsidejobList(String orderId); // 线下订单打印 ynw

	List<Job> getPrinte(int orderId, String printName);

	List<Job> getInsidePrinte(int orderId, String printName);

	List<String> getPrinteName(int companyId);
	
	List<Job> getList(int companyId);
}
