package com.dz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IEvaluateDao;
import com.dz.entity.Evaluate;
import com.dz.service.IEvaluateService;

/**
 * 登陆业务实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Transactional(readOnly = false)
@Service("evaluateService")
public class EvaluateServiceImpl implements IEvaluateService {

	@Autowired
	IEvaluateDao evaluateDao;

	@Override
	public List<Evaluate> evaluateList(Evaluate evaluate) {
		String sql = "SELECT o FROM Evaluate o WHERE 1=1";
		return evaluateDao.evaluateList(sql);
	}

	@Override
	public List<Evaluate> userevaluate(int userid, int start, int limit) {
		return evaluateDao.userevaluate(userid, start, limit);
	}

	@Override
	public  void delete(Evaluate evaluate) {
		evaluateDao.delete(evaluate);
	}

	@Override
	public void saveORupdate(final Evaluate evaluate) {
		evaluateDao.saveORupdate(evaluate);
	}

	@Override
	public List<Evaluate> getevaluate(String type, int customId) {
		return evaluateDao.getevaluate(type, customId);
	}
	
	@Override
	public List<Evaluate> getevaluate(String type, int customId,int start, int limit) {
		return evaluateDao.getevaluate(type, customId, start, limit);
	}

	@Override
	public List<Evaluate> getevaluate(String type, int customId,
			String typeClass, String isReply) {
		return evaluateDao.getevaluate(type, customId, typeClass, isReply);
	}

	@Override
	public  List<Evaluate> getIsReply(String type, int customId) {
		return evaluateDao.getIsReply(type, customId);
	}

	@Override
	public List<Evaluate> getTypeClass(String type, int customId) {
		return evaluateDao.getTypeClass(type, customId);
	}
	
	@Override
	public List<Evaluate> getTypeClass(String type, int customId,
			String typeClass, int start, int limit) {
		return evaluateDao.getTypeClass(type, customId, typeClass, start, limit);
	}

	@Override
	public List<Evaluate> getevaluate(int orderId) {
		return evaluateDao.getevaluate(orderId);
	}

	@Override
	public Evaluate get(int id) {
		return evaluateDao.get(id);
	}
}