package com.dz.service;

import com.dz.entity.ComputerCate;

/**
 * 通用dao，包括基本的CRUD方法
 * @author Ljh
 *
 */
public interface IComputerCateService {
	
	public void saveORupdate(final ComputerCate computerCate);
	
	ComputerCate getCate(String tableNo, int cid, String name, String seat);
	
	ComputerCate getCate(int orderId);

	ComputerCate getReserv(int reservId);
	
	public void delete(final String id); // 删除
	
}	
