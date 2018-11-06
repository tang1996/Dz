package com.dz.dao;

import java.util.List;

import com.dz.entity.User;

/**
 * 用户DAO
 * 
 * @author GJ
 * @date 2015年4月15日
 */
public interface IUserDao {

	User login(String userName); // 管理员登录

	List<User> userList(String sql); // 管理员列表

	public void delete(final String id); // 删除管理员

	public void saveORupdate(final User user); // 添加或修改管理员信息

	User getid(final int id); // 按id查询管理员

	List<User> getuserName(final String userName); // 按账号查询管理员

	User gettoken(String token); // 按用户token查询

	List<User> getCompanyId(int companyId, int start, int limit);

	List<User> getCompanyId(int companyId);

	// 管理后台
	User getUserName(String userName);

	User getPhone(String phone);

	List<User> getBoss(int bossId);

	List<User> userList(String sql, int start, int limit); // ynw xm

}
