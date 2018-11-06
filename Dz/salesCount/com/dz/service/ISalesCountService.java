package com.dz.service;

import java.util.List;

import com.dz.entity.SalesCount;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface ISalesCountService {

	SalesCount login(String userName);

	List<SalesCount> salesCountList(SalesCount salesCount);

	public void delete(final String id);

	public void saveORupdate(final SalesCount salesCount);

	SalesCount getid(final int id);

	List<SalesCount> getuserName(final String userName);

}
