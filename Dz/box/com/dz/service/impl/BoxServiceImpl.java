package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IBoxDao;
import com.dz.entity.Box;
import com.dz.service.IBoxService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("boxService")
public class BoxServiceImpl implements IBoxService{

	@Autowired
	IBoxDao boxDao;
	
	@Override
	public List<Box> boxList(Box box) {
		String sql = "SELECT o FROM Box o WHERE 1=1";
		return boxDao.boxList(sql);
	}
	
	@Override
	public List<Box> getBox(int goodsId) {
		return boxDao.getBox(goodsId);
	}
	
	@Override
	public void delete(final String id) {
		boxDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Box box) {
		boxDao.saveORupdate(box);
	}
	
}