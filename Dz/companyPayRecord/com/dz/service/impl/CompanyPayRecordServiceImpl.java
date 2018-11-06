package com.dz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.ICompanyPayRecordDao;
import com.dz.entity.CompanyPayRecord;
import com.dz.service.ICompanyPayRecordService;
import com.dz.util.StringUtil;

/**
 * 登陆业务实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Transactional(readOnly = false)
@Service("companyPayRecordService")
public class CompanyPayRecordServiceImpl implements ICompanyPayRecordService {

	@Autowired
	ICompanyPayRecordDao companyPayRecordDao;

	// 删除
	@Override
	public void delete(final String id) {
		companyPayRecordDao.delete(id);
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(final CompanyPayRecord companyPayRecord) {
		companyPayRecordDao.saveORupdate(companyPayRecord);
	}

	// 用户地址列表
	@Override
	public List<CompanyPayRecord> companyPayRecord(int companyId) {
		return companyPayRecordDao.companyPayRecord(companyId);
	}

	// 用户默认地址
	@Override
	public List<CompanyPayRecord> getCompanyPayRecord(int companyId, String date) {
		return companyPayRecordDao.getCompanyPayRecord(companyId, date);
	}

	// 单个地址查询
	@Override
	public CompanyPayRecord find(int id) {
		return companyPayRecordDao.find(id);
	}

	// 用户地址列表
	@Override
	public List<CompanyPayRecord> getList(Integer companyId, String startDate, String endDate, int start, int limit) {
		String sql = "SELECT o FROM CompanyPayRecord o WHERE o.date>=:v_startDate and o.date<=:v_endDate and o.isAccount=1";
		if (!StringUtil.isEmpty(companyId.toString())) {
			sql = sql + " and o.companyId=" + companyId;
		}
		return companyPayRecordDao.getList(sql, startDate, endDate, start, limit);
	}

	// 用户地址列表
	@Override
	public Long getList(Integer companyId, String startDate, String endDate) {
		String sql = "SELECT count(o.id) FROM CompanyPayRecord o WHERE o.date>=:v_startDate and o.date<=:v_endDate and o.isAccount=1";
		if (!StringUtil.isEmpty(companyId.toString())) {
			sql = sql + " and o.companyId=" + companyId;
		}
		return companyPayRecordDao.getList(sql, startDate, endDate);
	}

	@Override
	public List<CompanyPayRecord> getPayView() {
		return companyPayRecordDao.getPayView();
	}
	
	// 		ynw
	@Override
	public List<CompanyPayRecord> companyPayRecordTotal(int companyId) {
		return companyPayRecordDao.companyPayRecordTotal(companyId);
	}
}