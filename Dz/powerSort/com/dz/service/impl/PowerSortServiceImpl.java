package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IPowerSortDao;
import com.dz.entity.PowerSort;
import com.dz.service.IPowerSortService;
import com.dz.util.StringUtil;

@Transactional(readOnly=false)
@Service("powerSortService")
public class PowerSortServiceImpl implements IPowerSortService{

	@Autowired
	IPowerSortDao powerSortDao;
	
	@Override
	public PowerSort getPowerSort(String position, int companyId) {
		return powerSortDao.getPowerSort(position,companyId);
	}

	@Override
	public List<PowerSort> powerSortList(PowerSort powerSort) {
		String sql = "SELECT o FROM PowerSort o WHERE 1=1";
		if(!StringUtil.isEmpty(powerSort.getName())){
			
		}
		return powerSortDao.powerSortList(sql);
	}
	
	@Override
	public void delete(final String id) {
		powerSortDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final PowerSort powerSort) {
		powerSortDao.saveORupdate(powerSort);
	}
	
	@Override
	public PowerSort getid(final int id) {
		return powerSortDao.getid(id);
	}
	
	@Override
	public List<PowerSort> getuserName(final String userName) {
		return powerSortDao.getuserName(userName);
	}

	@Override
	public PowerSort gettoken(String token) {
		return powerSortDao.gettoken(token);
	}
	
}
