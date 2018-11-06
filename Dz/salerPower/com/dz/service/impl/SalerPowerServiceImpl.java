package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.ISalerPowerDao;
import com.dz.entity.SalerPower;
import com.dz.service.ISalerPowerService;
import com.dz.util.StringUtil;

@Transactional(readOnly=false)
@Service("salerPowerService")
public class SalerPowerServiceImpl implements ISalerPowerService{

	@Autowired
	ISalerPowerDao salerPowerDao;
	
	@Override
	public SalerPower getSalerPower(String position, int companyId) {
		return salerPowerDao.getSalerPower(position,companyId);
	}

	@Override
	public List<SalerPower> salerPowerList(SalerPower salerPower) {
		String sql = "SELECT o FROM SalerPower o WHERE 1=1";
		if(!StringUtil.isEmpty(salerPower.getName())){
			
		}
		return salerPowerDao.salerPowerList(sql);
	}
	
	@Override
	public void delete(final String id) {
		salerPowerDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final SalerPower salerPower) {
		salerPowerDao.saveORupdate(salerPower);
	}
	
	@Override
	public SalerPower getid(final int id) {
		return salerPowerDao.getid(id);
	}
	
	@Override
	public List<SalerPower> getuserName(final String userName) {
		return salerPowerDao.getuserName(userName);
	}

	@Override
	public SalerPower gettoken(String token) {
		return salerPowerDao.gettoken(token);
	}
	
}
