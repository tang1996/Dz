package com.dz.dao;

import java.util.List;

import com.dz.entity.Distribution;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IDistributionDao {
 
	List<Distribution> distributionList(String sql);		//查询
	
	Distribution getDistribution(int userId);
	
	Distribution find(int id);
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final Distribution distribution);		//添加或修改信息
	
}

 
