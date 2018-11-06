package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IAddressDao;
import com.dz.entity.Address;
import com.dz.service.IAddressService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("addressService")
public class AddressServiceImpl implements IAddressService{

	@Autowired
	IAddressDao addressDao;
	
	//删除
	@Override
	public void delete(final String id) {
		addressDao.delete(id);
	}
	
	//添加或修改信息
	@Override
	public void saveORupdate(final Address address) {
		addressDao.saveORupdate(address);
	}
	
	//用户地址列表
	@Override
	public List<Address> useraddress(int userid) {
		return addressDao.useraddress(userid);
	}
	
	//用户默认地址
	@Override
	public Address getAddress(int userid) {
		return addressDao.getAddress(userid);
	}
	
	@Override
	public  Address get(int userid) {
		return addressDao.get(userid);
	}
	
	//单个地址查询
	@Override
	public Address find(int id) {
		return addressDao.find(id);
	}
}