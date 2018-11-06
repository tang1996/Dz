package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IRelatingDao;
import com.dz.entity.Relating;
import com.dz.service.IRelatingService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("relatingService")
public class RelatingServiceImpl implements IRelatingService{

	@Autowired
	IRelatingDao relatingDao;
	
	@Override
	public List<Relating> relatingList(Relating relating) {
		String sql = "SELECT o FROM Relating o WHERE 1=1";
		return relatingDao.relatingList(sql);
	}
	
	@Override
	public List<Relating> getrelating(int orderId, int companyId) {
		return relatingDao.getrelating(orderId,companyId);
	}
	
	@Override
	public List<Relating> getrelating(int orderId){
		return relatingDao.getrelating(orderId);
	}
	
	@Override
	public Relating getRelating(int orderId, int goodsId) {
		return relatingDao.getRelating(orderId, goodsId);
	}
	
	@Override
	public List<Relating> getMenus(int orderId,int goodsId, int cid){
		return relatingDao.getMenus(orderId, goodsId, cid);
	}
	
	@Override
	public void delete(final String id) {
		relatingDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Relating relating) {
		relatingDao.saveORupdate(relating);
	}
	
	// 线下订单加减菜品
	@Override		//ynw
	public Relating getinsideRelating(int orderId, int goodsId) {
		return relatingDao.getinsideRelating(orderId, goodsId);
	}

	@Override	//ynw
	public List<Relating> getinsideMenus(int insideorderId, int goodsId, int cid) {
		return relatingDao.getinsideMenus(insideorderId, goodsId, cid);
	}

	
	@Override	//ynw
	public List<Relating> getrelatingbyinsideID(int orderId) {
		return relatingDao.getrelatingbyinsideID(orderId);
	}

	@Override	//ynw
	public List<Relating> getrelatingbyinsideID(int orderId, int companyId) {
		return relatingDao.getrelatingbyinsideID(orderId, companyId);
	}

	@Override
	public List<Relating> getinsertrelating(int insideOrder) {
		return relatingDao.getinsertrelating(insideOrder);
	}

	@Override
	public List<Relating> getGoods(int goodid, int companyId, int orderId) {
		return relatingDao.getGoods(goodid, companyId, orderId);
	}
	
	
	
}