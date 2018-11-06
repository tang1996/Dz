package com.dz.service;

import java.util.List;

import com.dz.entity.Opinion;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IOpinionService {

	Opinion opinion(int id);//按id查询
	
	public void delete(final String id);//删除

	public void saveORupdate(final Opinion opinion);//添加或修改信息
	
	//管理后台
	List<Opinion> opinionList(Opinion opinion, int start, int limit);

}
