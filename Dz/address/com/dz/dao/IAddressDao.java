package com.dz.dao;

import java.util.List;

import com.dz.entity.Address;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IAddressDao {
 
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final Address address);		//添加或修改信息
	
	List<Address> useraddress(int userid);//用户地址列表
	
	Address getAddress(int userid);//用户默认地址
	
	Address get(int userid) ;
	
	Address find(int userid);//单个地址查询
}

 
