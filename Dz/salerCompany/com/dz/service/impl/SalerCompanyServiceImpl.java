package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.ISalerCompanyDao;
import com.dz.entity.SalerCompany;
import com.dz.service.ISalerCompanyService;
import com.dz.util.StringUtil;

@Transactional(readOnly=false)
@Service("salerCompanyService")
public class SalerCompanyServiceImpl implements ISalerCompanyService{

	@Autowired
	ISalerCompanyDao salerCompanyDao;
	
	@Override
	public List<SalerCompany> salerCompanyList(SalerCompany salerCompany) {
		String sql = "SELECT o FROM SalerCompany o WHERE 1=1";
		if(salerCompany.getCompanyId() != 0){
			sql = sql + " and o.companyId=" + salerCompany.getCompanyId();
		}
		if(salerCompany.getSalerId() != 0){
			sql = sql + " and o.salerId=" + salerCompany.getSalerId();
		}
		return salerCompanyDao.salerCompanyList(sql);
	}
	
	@Override
	public void delete(final String id) {
		salerCompanyDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final SalerCompany salerCompany) {
		salerCompanyDao.saveORupdate(salerCompany);
	}
	
	@Override
	public SalerCompany getid(final int id) {
		return salerCompanyDao.getid(id);
	}
	
	@Override
	public Object[] count(SalerCompany salerCompany) {
		String sql = "SELECT o.salerId,count(o.id),count(o.main),count(o.deputy) FROM SalerCompany o WHERE 1=1";
		if(!StringUtil.isEmpty(salerCompany.getCreateTime())){
			sql = sql + " and o.createTime >='" + salerCompany.getCreateTime() + "'";
		}
		if(salerCompany.getSalerId() != null){
			sql = sql + " and o.salerId=" + salerCompany.getSalerId();
		}
		sql = sql + " group by o.salerId";
		return salerCompanyDao.count(sql);
	}
	
	@Override
	public Object[] count(SalerCompany salerCompany, String startTime, String endTime) {
		String sql = "SELECT o.salerId,count(o.id),count(o.main),count(o.deputy) FROM SalerCompany o WHERE 1=1";
		if(!StringUtil.isEmpty(startTime) || !StringUtil.isEmpty(endTime)){
			sql = sql + " and o.createTime >='" + startTime + "' and o.createTime <='" + endTime + "'";
		}
		if(salerCompany.getSalerId() != null){
			sql = sql + " and o.salerId=" + salerCompany.getSalerId();
		}
		sql = sql + " group by o.salerId";
		System.out.println(sql);
		return salerCompanyDao.count(sql);
	}
}
