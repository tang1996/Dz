package com.dz.service;

import java.util.List;

import com.dz.entity.Evaluate;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IEvaluateService {

	List<Evaluate> evaluateList(Evaluate evaluate);
	
	List<Evaluate> userevaluate(int userid, int start, int limit);

	public  void delete(Evaluate evaluate) ;

	public void saveORupdate(final Evaluate evaluate);
	
	List<Evaluate> getevaluate(String type, int customId);
	
	List<Evaluate> getevaluate(int orderId);
	
	Evaluate get(int id);
	
	List<Evaluate> getevaluate(String type, int customId, int start, int limit);
	
	List<Evaluate> getevaluate(String type, int customId, String typeClass, String isReply);
	
	List<Evaluate> getIsReply(String type, int customId);
	
	List<Evaluate> getTypeClass(String type, int customId);
	
	List<Evaluate> getTypeClass(String type, int customId, String typeClass, int start, int limit);

}
