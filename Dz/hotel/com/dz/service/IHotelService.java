package com.dz.service;

import java.util.List;

import com.dz.entity.Hotel;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IHotelService {

	List<Hotel> hotelList(Hotel hotel);
	
	Hotel getHotel(int companyId);

	public void delete(final String id);

	public void saveORupdate(final Hotel hotel);

}
