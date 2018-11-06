package com.dz.service;

import java.util.List;

import com.dz.entity.User;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IUserService {

	User login(String userName);

	List<User> userList(User user);

	public void delete(final String id);

	public void saveORupdate(final User user);

	User getid(final int id);

	List<User> getuserName(final String userName);

	User gettoken(String token);

	List<User> getCompanyId(int companyId, int start, int limit);

	List<User> getCompanyId(int companyId);

	// 管理后台
	User getUserName(String userName);

	User getPhone(String phone);

	List<User> getBoss(int bossId);

	List<User> userList(User user, int start, int limit); // ynw

}
