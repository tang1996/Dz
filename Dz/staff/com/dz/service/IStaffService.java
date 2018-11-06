package com.dz.service;


import java.util.List;

import com.dz.entity.Staff;


/**
 * 通用dao，包括基本的CRUD方法
 * @author Ljh
 *
 */

public interface IStaffService {
 
	Staff login(String userName);
	
	List<Staff> staffList(Staff staff);
	
	public void delete(final String id);
	
	public void saveORupdate(final Staff staff);
	
	Staff getid(final int id);
	
	List<Staff> getuserName(final String userName);
	
	List<Staff> getCompany(final String companyId);
	
	Staff gettoken(String token);
	
	List<Staff> getList(int companyId, int powerSortId);
	
}
 
