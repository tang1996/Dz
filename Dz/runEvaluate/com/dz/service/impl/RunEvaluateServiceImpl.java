package com.dz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IRunEvaluateDao;
import com.dz.entity.RunEvaluate;
import com.dz.service.IRunEvaluateService;

/**
 * 登陆业务实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Transactional(readOnly = false)
@Service("runEvaluateService")
public class RunEvaluateServiceImpl implements IRunEvaluateService {

	@Autowired
	IRunEvaluateDao runEvaluateDao;

	@Override
	public List<RunEvaluate> runEvaluateList(RunEvaluate runEvaluate) {
		String sql = "SELECT o FROM RunEvaluate o WHERE 1=1";
		return runEvaluateDao.runEvaluateList(sql);
	}

	@Override
	public RunEvaluate getrunEvaluate(int goodsId) {
		return runEvaluateDao.getrunEvaluate(goodsId);
	}

	@Override
	public void delete(final String id) {
		runEvaluateDao.delete(id);
	}

	@Override
	public void saveORupdate(final RunEvaluate runEvaluate) {
		runEvaluateDao.saveORupdate(runEvaluate);
	}

	//管理后台
	@Override // ynw xm
	public List<RunEvaluate> getrunEvaluate(RunEvaluate runEvaluate, int start, int limit) {
		String sql = "SELECT o FROM RunEvaluate o WHERE 1=1";
		if (Integer.valueOf(runEvaluate.getRunId()) != null) {
			sql += "and o.runId=" + runEvaluate.getRunId();
		}

		return runEvaluateDao.getrunEvaluate(sql, start, limit);
	}
	
	@Override
	public Object[] getScore(int runId) {
		return runEvaluateDao.getScore(runId);
	}

}