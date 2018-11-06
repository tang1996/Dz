package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IAdminDao;
import com.dz.entity.Admin;
import com.dz.service.IAdminService;
import com.dz.util.StringUtil;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("adminService")
public class AdminServiceImpl implements IAdminService{

	@Autowired
	IAdminDao adminDao;
	
	@Override
	public Admin login(String userName) {
		String sql ="SELECT o FROM Admin o WHERE o.userName =:v_username";
		return adminDao.login(sql, userName);
	}

	@Override
	public List<Admin> adminList(Admin admin) {
		String sql = "SELECT o FROM Admin o WHERE 1=1";
		if(!StringUtil.isEmpty(admin.getName())){
			sql = sql + " and name = '" + admin.getName() + "'";
		}
		return adminDao.adminList(sql);
	}
	
	@Override
	public void delete(final String id) {
		String sql = "DELETE FROM Admin o WHERE o.id=:v_id";
		adminDao.delete(sql, id);
	}
	
	@Override
	public void saveORupdate(final Admin admin) {
		adminDao.saveORupdate(admin);
	}
	
	@Override
	public Admin getid(final int id) {
		String sql = "SELECT o FROM Admin o WHERE id=:v_id";
		return adminDao.getid(sql, id);
	}
	
	@Override
	public List<Admin> getuserName(final String userName) {
		String sql = "SELECT o FROM Admin o WHERE o.userName=:v_userName";
		return adminDao.getuserName(sql, userName);
	}
	
}