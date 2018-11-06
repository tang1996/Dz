package com.dz.service;

import java.util.List;

import com.dz.entity.Admin;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IAdminService {

	Admin login(String userName);

	List<Admin> adminList(Admin admin);

	public void delete(final String id);

	public void saveORupdate(final Admin admin);

	Admin getid(final int id);

	List<Admin> getuserName(final String userName);

}
