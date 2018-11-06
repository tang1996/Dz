package com.dz.dao;

import java.util.List;

import com.dz.entity.Hotel;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IHotelDao {
 
	List<Hotel> hotelList(String sql);		//查询
	
	Hotel getHotel(int companyId);
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final Hotel hotel);		//添加或修改信息
	
}

 
