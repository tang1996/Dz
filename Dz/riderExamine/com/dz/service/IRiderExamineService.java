package com.dz.service;


import java.util.List;

import com.dz.entity.RiderExamine;


/**
 * 通用dao，包括基本的CRUD方法
 * @author Ljh
 *
 */

public interface IRiderExamineService {
 
	List<RiderExamine> riderExamineList(RiderExamine riderExamine);
	
	public void delete(final String id);
	
	public void saveORupdate(final RiderExamine riderExamine);
	
	RiderExamine getid(final int id);
	
	List<RiderExamine> getuserName(final String userName);
	
	RiderExamine gettoken(String token);
	
}
 
