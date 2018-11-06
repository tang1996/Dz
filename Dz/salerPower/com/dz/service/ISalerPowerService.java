package com.dz.service;


import java.util.List;

import com.dz.entity.SalerPower;


/**
 * 通用dao，包括基本的CRUD方法
 * @author Ljh
 *
 */

public interface ISalerPowerService {
 
	SalerPower getSalerPower(String position, int companyId);
	
	List<SalerPower> salerPowerList(SalerPower salerPower);
	
	public void delete(final String id);
	
	public void saveORupdate(final SalerPower salerPower);
	
	SalerPower getid(final int id);
	
	List<SalerPower> getuserName(final String userName);
	
	SalerPower gettoken(String token);
	
}
 
