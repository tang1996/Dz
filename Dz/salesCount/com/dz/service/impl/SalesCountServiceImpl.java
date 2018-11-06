package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.ISalesCountDao;
import com.dz.entity.SalesCount;
import com.dz.service.ISalesCountService;
import com.dz.util.StringUtil;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("salesCountService")
public class SalesCountServiceImpl implements ISalesCountService{

	@Autowired
	ISalesCountDao salesCountDao;
	
	@Override
	public SalesCount login(String userName) {
		String sql ="SELECT o FROM SalesCount o WHERE o.userName =:v_username";
		return salesCountDao.login(sql, userName);
	}

	@Override
	public List<SalesCount> salesCountList(SalesCount salesCount) {
		String sql = "SELECT o FROM SalesCount o WHERE 1=1";
		return salesCountDao.salesCountList(sql);
	}
	
	@Override
	public void delete(final String id) {
		String sql = "DELETE FROM SalesCount o WHERE o.id=:v_id";
		salesCountDao.delete(sql, id);
	}
	
	@Override
	public void saveORupdate(final SalesCount salesCount) {
		salesCountDao.saveORupdate(salesCount);
	}
	
	@Override
	public SalesCount getid(final int id) {
		String sql = "SELECT o FROM SalesCount o WHERE id=:v_id";
		return salesCountDao.getid(sql, id);
	}
	
	@Override
	public List<SalesCount> getuserName(final String userName) {
		String sql = "SELECT o FROM SalesCount o WHERE o.userName=:v_userName";
		return salesCountDao.getuserName(sql, userName);
	}
	
}