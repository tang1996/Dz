package com.dz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IUserDao;
import com.dz.entity.User;
import com.dz.service.IUserService;
import com.dz.util.StringUtil;

@Transactional(readOnly = false)
@Service("userService")
public class UserServiceImpl implements IUserService {

	@Autowired
	IUserDao userDao;

	@Override
	public User login(String userName) {
		return userDao.login(userName);
	}

	@Override
	public List<User> userList(User user) {
		String sql = "SELECT o FROM User o WHERE 1=1";
		if (!StringUtil.isEmpty(user.getName())) {
			sql = sql + " and o.userName = '" + user.getUserName() + "'";
		}
		return userDao.userList(sql);
	}

	@Override
	public void delete(final String id) {
		userDao.delete(id);
	}

	@Override
	public void saveORupdate(final User user) {
		userDao.saveORupdate(user);
	}

	@Override
	public User getid(final int id) {
		return userDao.getid(id);
	}

	@Override
	public List<User> getuserName(final String userName) {
		return userDao.getuserName(userName);
	}

	@Override
	public User gettoken(String token) {
		return userDao.gettoken(token);
	}

	@Override
	public List<User> getCompanyId(int companyId, int start, int limit) {
		return userDao.getCompanyId(companyId, start, limit);
	}

	@Override
	public List<User> getCompanyId(int companyId) {
		return userDao.getCompanyId(companyId);
	}

	// 管理后台
	@Override
	public User getUserName(String userName) {
		return userDao.getUserName(userName);
	}

	@Override
	public User getPhone(String phone) {
		return userDao.getPhone(phone);
	}

	@Override
	public List<User> getBoss(int bossId) {
		return userDao.getBoss(bossId);
	}
	
	@Override	//ynw xm
	public List<User> userList(User user, int start, int limit) {
		String sql = "SELECT o FROM User o WHERE 1=1";
		if(!StringUtil.isEmpty(user.getUserName())){
			sql = sql + " and o.userName = '" + user.getUserName() + "'";
		}
		
		if(!StringUtil.isEmpty(user.getName())){
			sql = sql + "and o.name = '" + user.getName() + "'";
		}
		
		if(!StringUtil.isEmpty(user.getPhone())){
			sql = sql + "and o.phone = '" + user.getPhone() + "'";
		}
		
		if(!StringUtil.isEmpty(user.getIsDistribution() + "")){
			if(user.getIsDistribution() == true){
				sql = sql + " and o.isDistribution=1";
			}
		}
		return userDao.userList(sql,start,limit);
	}

}
