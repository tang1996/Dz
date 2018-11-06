package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.ICateDao;
import com.dz.entity.Cate;
import com.dz.entity.Order;
import com.dz.service.ICateService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("cateService")
public class CateServiceImpl implements ICateService{

	@Autowired
	ICateDao cateDao;
	
	@Override
	public List<Cate> cateList(Cate cate) {
		String sql = "SELECT o FROM Cate o WHERE 1=1";
		return cateDao.cateList(sql);
	}
	
	@Override
	public Cate getCate(String tableNo, int cid, String name, String seat){
		return cateDao.getCate(tableNo, cid, name, seat);
	}
	
	@Override
	public Cate getCate(int id) {
		return cateDao.getCate(id);
	}
	
	@Override
	public void delete(final String id) {
		cateDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Cate cate) {
		cateDao.saveORupdate(cate);
	}

	@Override
	public List<Order> cateRes(String reserveId) {
		return cateDao.cateRes(reserveId);
	}

	@Override
	public Cate getCateInside(int insideId) {
		return cateDao.getCateInside(insideId);
	}
	
	public List<Cate> searchPhone(String phone) {//2018-11-03 @Tyy
		return cateDao.searchPhone(phone);
	}
	
}