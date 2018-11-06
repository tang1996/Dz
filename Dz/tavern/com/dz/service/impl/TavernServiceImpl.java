package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.ITavernDao;
import com.dz.entity.Tavern;
import com.dz.service.ITavernService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("tavernService")
public class TavernServiceImpl implements ITavernService{

	@Autowired
	ITavernDao tavernDao;
	
	@Override
	public List<Tavern> tavernList(Tavern tavern) {
		String sql = "SELECT o FROM Tavern o WHERE 1=1";
		return tavernDao.tavernList(sql);
	}
	
	@Override
	public List<Tavern> getTavern(int stayId) {
		return tavernDao.getTavern(stayId);
	}
	
	@Override
	public void delete(final String id) {
		tavernDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Tavern tavern) {
		tavernDao.saveORupdate(tavern);
	}
	
}