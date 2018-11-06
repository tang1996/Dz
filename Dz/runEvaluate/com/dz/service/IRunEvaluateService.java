package com.dz.service;

import java.util.List;

import com.dz.entity.RunEvaluate;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IRunEvaluateService {

	List<RunEvaluate> runEvaluateList(RunEvaluate runEvaluate);
	
	RunEvaluate getrunEvaluate(int goodsId);

	public void delete(final String id);

	public void saveORupdate(final RunEvaluate runEvaluate);
	
	//管理后台
	List<RunEvaluate> getrunEvaluate(RunEvaluate runEvaluate, int start, int limit);	//ynw xm
	
	Object[] getScore(int runId);
	
}
