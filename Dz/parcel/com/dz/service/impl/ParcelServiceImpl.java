package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IParcelDao;
import com.dz.entity.Parcel;
import com.dz.service.IParcelService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("parcelService")
public class ParcelServiceImpl implements IParcelService{

	@Autowired
	IParcelDao parcelDao;
	
	@Override
	public List<Parcel> parcelList(Parcel parcel) {
		String sql = "SELECT o FROM Parcel o WHERE 1=1";
		return parcelDao.parcelList(sql);
	}
	
	@Override
	public Parcel getparcel(int goodsId) {
		return parcelDao.getparcel(goodsId);
	}
	
	@Override
	public void delete(final String id) {
		parcelDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Parcel parcel) {
		parcelDao.saveORupdate(parcel);
	}
	
}