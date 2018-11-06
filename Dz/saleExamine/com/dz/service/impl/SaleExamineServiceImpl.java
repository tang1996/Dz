package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.ISaleExamineDao;
import com.dz.entity.SaleExamine;
import com.dz.service.ISaleExamineService;
import com.dz.util.StringUtil;

@Transactional(readOnly=false)
@Service("saleExamineService")
public class SaleExamineServiceImpl implements ISaleExamineService{

	@Autowired
	ISaleExamineDao saleExamineDao;
	
	@Override
	public List<SaleExamine> saleExamineList(SaleExamine saleExamine) {
		String sql = "SELECT o FROM SaleExamine o WHERE 1=1";
		if(!StringUtil.isEmpty(saleExamine.getName())){
			sql = sql + " and o.Name = '" + saleExamine.getName() + "'";
		}
		return saleExamineDao.saleExamineList(sql);
	}
	
	@Override
	public void delete(final String id) {
		saleExamineDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final SaleExamine saleExamine) {
		saleExamineDao.saveORupdate(saleExamine);
	}
	
	@Override
	public SaleExamine getid(final int id) {
		return saleExamineDao.getid(id);
	}
	
	@Override
	public List<SaleExamine> getuserName(final String userName) {
		return saleExamineDao.getuserName(userName);
	}

	@Override
	public SaleExamine gettoken(String token) {
		return saleExamineDao.gettoken(token);
	}
	
}
