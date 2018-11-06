package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IMeunDao;
import com.dz.entity.Meun;
import com.dz.service.IMeunService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("meunService")
public class MeunServiceImpl implements IMeunService{

	@Autowired
	IMeunDao meunDao;
	
	@Override
	public List<Meun> meunList(Meun meun) {
		String sql = "SELECT o FROM Meun o WHERE 1=1";
		return meunDao.meunList(sql);
	}
	
	@Override
	public List<Meun> getmeun(int userId) {
		return meunDao.getmeun(userId);
	}
	
	@Override
	public void delete(final String id) {
		meunDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Meun meun) {
		meunDao.saveORupdate(meun);
	}
	
}