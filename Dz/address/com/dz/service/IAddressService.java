package com.dz.service;

import java.util.List;

import com.dz.entity.Address;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IAddressService {

	public void delete(final String id);//删除

	public void saveORupdate(final Address address);//添加或修改信息
	
	List<Address> useraddress(int userid);//用户地址列表
	
	Address getAddress(int userid);  //用户默认地址
	
	 Address get(int userid) ;
	
	Address find(int id);  //单个地址查询

}
