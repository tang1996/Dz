package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IRiderExamineDao;
import com.dz.entity.RiderExamine;
import com.dz.service.IRiderExamineService;
import com.dz.util.StringUtil;

@Transactional(readOnly=false)
@Service("riderExamineService")
public class RiderExamineServiceImpl implements IRiderExamineService{

	@Autowired
	IRiderExamineDao riderExamineDao;
	
	@Override
	public List<RiderExamine> riderExamineList(RiderExamine riderExamine) {
		String sql = "SELECT o FROM RiderExamine o WHERE 1=1";
		if(!StringUtil.isEmpty(riderExamine.getName())){
			sql = sql + " and o.Name = '" + riderExamine.getName() + "'";
		}
		return riderExamineDao.riderExamineList(sql);
	}
	
	@Override
	public void delete(final String id) {
		riderExamineDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final RiderExamine riderExamine) {
		riderExamineDao.saveORupdate(riderExamine);
	}
	
	@Override
	public RiderExamine getid(final int id) {
		return riderExamineDao.getid(id);
	}
	
	@Override
	public List<RiderExamine> getuserName(final String userName) {
		return riderExamineDao.getuserName(userName);
	}

	@Override
	public RiderExamine gettoken(String token) {
		return riderExamineDao.gettoken(token);
	}
	
}
