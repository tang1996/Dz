package com.dz.service;

import java.util.List;

import com.dz.entity.Stay;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IStayService {

	List<Stay> stayList(Stay stay);
	
	List<Stay> getStay(int companyId,int haveRoom);
	
	Stay Stay(int goodsId);

	public void delete(final String id);

	public void saveORupdate(final Stay stay);

}
