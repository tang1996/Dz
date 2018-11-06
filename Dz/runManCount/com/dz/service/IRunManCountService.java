package com.dz.service;

import java.util.List;

import com.dz.entity.RunManCount;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IRunManCountService {

	List<RunManCount> runManCountList(RunManCount runManCount);
	
	List<RunManCount> getrunManCount(int userId);

	public void delete(final String id);

	public void saveORupdate(final RunManCount runManCount);
	
	Object[] runManCount(int userId);//配送员总交易统计
	
	Object[] getRunManCount(int userId, String date);  //配送员当日交易统计

}
