package com.dz.service;


import java.util.List;

import com.dz.entity.PowerSort;


/**
 * 通用dao，包括基本的CRUD方法
 * @author Ljh
 *
 */

public interface IPowerSortService {
 
	PowerSort getPowerSort(String position, int companyId);
	
	List<PowerSort> powerSortList(PowerSort powerSort);
	
	public void delete(final String id);
	
	public void saveORupdate(final PowerSort powerSort);
	
	PowerSort getid(final int id);
	
	List<PowerSort> getuserName(final String userName);
	
	PowerSort gettoken(String token);
	
}
 
