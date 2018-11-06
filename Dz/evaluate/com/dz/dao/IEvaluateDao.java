package com.dz.dao;

import java.util.List;

import com.dz.entity.Evaluate;

/**
 * 用户DAO
 * 
 * @author GJ
 * @date 2015年4月15日
 */
public interface IEvaluateDao {

	List<Evaluate> evaluateList(String sql); // 查询

	List<Evaluate> userevaluate(int userid, int start, int limit);

	public  void delete(Evaluate evaluate); // 删除

	public void saveORupdate(final Evaluate evaluate); // 添加或修改信息

	List<Evaluate> getevaluate(String type, int customId); //
	
	Evaluate get(int id);
	
	List<Evaluate> getevaluate(int orderId);
	
	List<Evaluate> getevaluate(String type, int customId, int start, int limit); //

	List<Evaluate> getevaluate(String type, int customId, String typeClass,
			String isReply); //

	 List<Evaluate> getIsReply(String type, int customId); //
	
	 List<Evaluate> getTypeClass(String type, int customId);
	
	List<Evaluate> getTypeClass(String type, int customId, String typeClass, int start, int limit); //

}
