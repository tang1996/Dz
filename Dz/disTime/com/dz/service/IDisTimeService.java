package com.dz.service;

import java.util.List;

import com.dz.entity.DisTime;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IDisTimeService {

	List<DisTime> disTimeList(DisTime disTime);
	
	DisTime getDisTime(int companyId);

	public void delete(final String id);

	public void saveORupdate(final DisTime disTime);

}
