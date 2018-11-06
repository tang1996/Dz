package com.dz.dao;

import java.util.List;

import com.dz.entity.Parcel;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IParcelDao {
 
	List<Parcel> parcelList(String sql);		//查询
	
	Parcel getparcel(int goodsId);
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final Parcel parcel);		//添加或修改信息
	
}

 
