package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IHotelDao;
import com.dz.entity.Hotel;
import com.dz.service.IHotelService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("hotelService")
public class HotelServiceImpl implements IHotelService{

	@Autowired
	IHotelDao hotelDao;
	
	@Override
	public List<Hotel> hotelList(Hotel hotel) {
		String sql = "SELECT o FROM Hotel o WHERE 1=1";
		return hotelDao.hotelList(sql);
	}
	
	@Override
	public Hotel getHotel(int companyId) {
		return hotelDao.getHotel(companyId);
	}
	
	@Override
	public void delete(final String id) {
		hotelDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Hotel hotel) {
		hotelDao.saveORupdate(hotel);
	}
	
}