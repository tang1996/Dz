package com.dz.dao;

import java.util.List;

import com.dz.entity.RunEvaluate;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IRunEvaluateDao {
 
	List<RunEvaluate> runEvaluateList(String sql);		//查询
	
	RunEvaluate getrunEvaluate(int goodsId);
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final RunEvaluate runEvaluate);		//添加或修改信息
	
	//管理后台
	List<RunEvaluate> getrunEvaluate(String sql, int start, int limit);	
	
	Object[] getScore(int runId);
	
}

 
