package com.dz.service;

import java.util.List;

import com.dz.entity.Reserve;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IReserveService {

	List<Reserve> reserveList(Reserve reserve);
	
	List<Reserve> getReserve(int companyId,String seat);
	
	List<Reserve> getAllReserve(int companyId,String seat);
	
	Reserve getTable(String tableNo);
	
	Reserve find(int id);
	
	Reserve getTable(String tableNo, int companyId);
	
	Reserve getTable(String tableNo, int companyId, String seat);
	
	Reserve getTable(String tableNo, int companyId, String seat, String name);

	public void delete(final String id);

	public void saveORupdate(final Reserve reserve);
	
	List<Reserve> getReserveForTwo(int companyId,String seat);/////ynw
	
	List<Reserve> getReserve(int companyId, String seat, String status); // ynw

}
