package com.dz.service;


import java.util.List;

import com.dz.entity.RiderInfo;


/**
 * 通用dao，包括基本的CRUD方法
 * @author Ljh
 *
 */

public interface IRiderInfoService {
 
	
	List<RiderInfo> riderInfoList(RiderInfo riderInfo);
	
	public void delete(final String id);
	
	public void saveORupdate(final RiderInfo riderInfo);
	
	RiderInfo getid(final int id);
	
	RiderInfo getuserId(int userId);
	
}
 
