package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.ISalesmanDao;
import com.dz.entity.Salesman;
import com.dz.service.ISalesmanService;
import com.dz.util.StringUtil;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("salesmanService")
public class SalesmanServiceImpl implements ISalesmanService{

	@Autowired
	ISalesmanDao salesmanDao;
	
	@Override
	public Salesman login(String userName) {
		String sql ="SELECT o FROM Salesman o WHERE o.userName =:v_username";
		return salesmanDao.login(sql, userName);
	}

	@Override
	public List<Salesman> salesmanList(Salesman salesman) {
		String sql = "SELECT o FROM Salesman o WHERE 1=1";
		if(!StringUtil.isEmpty(salesman.getName())){
			sql = sql + " and name = '" + salesman.getName() + "'";
		}
		return salesmanDao.salesmanList(sql);
	}
	
	@Override
	public void delete(final String id) {
		String sql = "DELETE FROM Salesman o WHERE o.id=:v_id";
		salesmanDao.delete(sql, id);
	}
	
	@Override
	public void saveORupdate(final Salesman salesman) {
		salesmanDao.saveORupdate(salesman);
	}
	
	@Override
	public Salesman getid(final int id) {
		String sql = "SELECT o FROM Salesman o WHERE id=:v_id";
		return salesmanDao.getid(sql, id);
	}
	
	@Override
	public List<Salesman> getuserName(final String userName) {
		String sql = "SELECT o FROM Salesman o WHERE o.userName=:v_userName";
		return salesmanDao.getuserName(sql, userName);
	}
	
}