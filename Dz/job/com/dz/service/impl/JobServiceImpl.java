package com.dz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IJobDao;
import com.dz.entity.Job;
import com.dz.service.IJobService;

/**
 * 登陆业务实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Transactional(readOnly = false)
@Service("jobService")
public class JobServiceImpl implements IJobService {

	@Autowired
	IJobDao jobDao;

	@Override
	public List<Job> jobList(Job job) {
		String sql = "SELECT o FROM Job o WHERE 1=1";
		return jobDao.jobList(sql);
	}

	@Override
	public void delete(final String id) {
		jobDao.delete(id);
	}

	@Override
	public void saveORupdate(final Job job) {
		jobDao.saveORupdate(job);
	}

	@Override
	public List<Job> getjobList(String orderId) {
		return jobDao.getjobList(orderId);
	}
	
	@Override
	public List<Job> getjobListBase(String orderId) {
		return jobDao.getjobListBase(orderId);
	}

	@Override
	public List<Job> getOrderId(String companyId) {
		return jobDao.getOrderId(companyId);
	}

	@Override
	public Job job(int id) {
		return jobDao.job(id);
	}

	@Override // ynw
	public List<Job> getInsidejobList(String orderId) {
		return jobDao.getInsidejobList(orderId);
	}

	@Override
	public List<Job> getPrinte(int orderId, String printName) {
		return jobDao.getPrinte(orderId, printName);
	}

	@Override
	public List<Job> getInsidePrinte(int orderId, String printName) {
		return jobDao.getInsidePrinte(orderId, printName);
	}

	@Override
	public List<String> getPrinteName(int companyId) {
		return jobDao.getPrinteName(companyId);
	}

	@Override
	public List<Job> getList(int companyId) {
		return jobDao.getList(companyId);
	}

}