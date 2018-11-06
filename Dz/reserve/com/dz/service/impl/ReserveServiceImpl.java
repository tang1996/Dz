package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IReserveDao;
import com.dz.entity.Reserve;
import com.dz.service.IReserveService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("reserveService")
public class ReserveServiceImpl implements IReserveService{

	@Autowired
	IReserveDao reserveDao;
	
	@Override
	public List<Reserve> reserveList(Reserve reserve) {
		String sql = "SELECT o FROM Reserve o WHERE 1=1";
		return reserveDao.reserveList(sql);
	}
	
	@Override
	public List<Reserve> getReserve(int companyId,String seat) {
		return reserveDao.getReserve(companyId,seat);
	}
	
	@Override
	public List<Reserve> getAllReserve(int companyId,String seat) {
		return reserveDao.getAllReserve(companyId,seat);
	}
	
	@Override
	public Reserve getTable(String tableNo) {
		return reserveDao.getTable(tableNo);
	}

	@Override
	public Reserve find(int id) {
		return reserveDao.find(id);
	}
	
	@Override
	public Reserve getTable(String tableNo, int companyId) {
		return reserveDao.getTable(tableNo, companyId);
	}
	
	@Override
	public Reserve getTable(String tableNo, int companyId, String seat) {
		return reserveDao.getTable(tableNo, companyId, seat);
	}
	
	@Override
	public Reserve getTable(String tableNo, int companyId, String seat, String name) {
		return reserveDao.getTable(tableNo, companyId, seat, name);
	}
	
	@Override
	public void delete(final String id) {
		reserveDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Reserve reserve) {
		reserveDao.saveORupdate(reserve);
	}
	
	@Override	//YNW
	public List<Reserve> getReserve(int companyId, String seat, String status) {
		return reserveDao.getReserve(companyId, seat, status);
	}

	@Override	//YNW
	public List<Reserve> getReserveForTwo(int companyId,String seat) {
		return reserveDao.getReserveForTwo(companyId,seat);
	}
	
}