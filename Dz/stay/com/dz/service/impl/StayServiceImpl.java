package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IStayDao;
import com.dz.entity.Stay;
import com.dz.service.IStayService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("stayService")
public class StayServiceImpl implements IStayService{

	@Autowired
	IStayDao stayDao;
	
	@Override
	public List<Stay> stayList(Stay stay) {
		String sql = "SELECT o FROM Stay o WHERE 1=1";
		return stayDao.stayList(sql);
	}
	
	@Override
	public List<Stay> getStay(int companyId,int haveRoom) {
		return stayDao.getStay(companyId,haveRoom);
	}
	
	@Override
	public Stay Stay(int goodsId) {
		return stayDao.Stay(goodsId);
	}
	
	@Override
	public void delete(final String id) {
		stayDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Stay stay) {
		stayDao.saveORupdate(stay);
	}
	
}