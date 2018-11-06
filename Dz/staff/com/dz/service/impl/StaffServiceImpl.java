package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IStaffDao;
import com.dz.entity.Staff;
import com.dz.service.IStaffService;
import com.dz.util.StringUtil;

@Transactional(readOnly=false)
@Service("staffService")
public class StaffServiceImpl implements IStaffService{

	@Autowired
	IStaffDao staffDao;
	
	@Override
	public Staff login(String userName) {
		return staffDao.login(userName);
	}

	@Override
	public List<Staff> staffList(Staff staff) {
		String sql = "SELECT o FROM Staff o WHERE 1=1";
		if(!StringUtil.isEmpty(staff.getName())){
			sql = sql + " and o.userName = '" + staff.getUserName() + "'";
		}
		return staffDao.staffList(sql);
	}
	
	@Override
	public void delete(final String id) {
		staffDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Staff staff) {
		staffDao.saveORupdate(staff);
	}
	
	@Override
	public Staff getid(final int id) {
		return staffDao.getid(id);
	}
	
	@Override
	public List<Staff> getuserName(final String userName) {
		return staffDao.getuserName(userName);
	}
	
	@Override
	public List<Staff> getCompany(final String companyId) {
		return staffDao.getCompany(companyId);
	}

	@Override
	public Staff gettoken(String token) {
		return staffDao.gettoken(token);
	}
	
	@Override
	public List<Staff> getList(int companyId, int powerSortId) {
		return staffDao.getList(companyId, powerSortId);
	}
	
}
