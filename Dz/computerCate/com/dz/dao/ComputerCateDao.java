package com.dz.dao;

import com.dz.entity.Cate;
import com.dz.entity.ComputerCate;

public interface ComputerCateDao {
	
	public void saveORupdate(final ComputerCate computerCate);
	
	public ComputerCate getCate(String tableNo, int cid, String name, String seat);
	
	ComputerCate getCate(int orderId);
	
	public void delete(final String id); // 删除
	
	ComputerCate getReserv(int reservId);
}
