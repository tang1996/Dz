package com.dz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.ComputerCateDao;
import com.dz.dao.ICateDao;
import com.dz.entity.ComputerCate;
import com.dz.service.IComputerCateService;

@Transactional(readOnly = false)
@Service("computercateService")
public class ComputerCateServiceImpl implements IComputerCateService{

	@Autowired
	ComputerCateDao computerCateDao;
	
	@Override
	public void saveORupdate(ComputerCate computerCate) {
		computerCateDao.saveORupdate(computerCate);
	}

	@Override
	public ComputerCate getCate(String tableNo, int cid, String name,
			String seat) {
		return computerCateDao.getCate(tableNo, cid, name, seat);
	}

	@Override
	public ComputerCate getCate(int orderId) {
		return computerCateDao.getCate(orderId);
	}
	
	@Override
	public void delete(String id) {
		computerCateDao.delete(id);
	}

	@Override
	public ComputerCate getReserv(int reservId) {
		return computerCateDao.getReserv(reservId);
	}
	
}
