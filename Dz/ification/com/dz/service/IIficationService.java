package com.dz.service;

import java.util.List;

import com.dz.entity.Ification;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IIficationService {

	List<Ification> getIfication(int companyId);//按商家查询
	
	Ification find(int id);//按id查询
	
	public void delete(final String id);//删除

	public void saveORupdate(final Ification ification);//添加或修改信息

}
