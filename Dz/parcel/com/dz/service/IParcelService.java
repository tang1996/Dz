package com.dz.service;

import java.util.List;

import com.dz.entity.Parcel;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IParcelService {

	List<Parcel> parcelList(Parcel parcel);
	
	Parcel getparcel(int goodsId);

	public void delete(final String id);

	public void saveORupdate(final Parcel parcel);

}
