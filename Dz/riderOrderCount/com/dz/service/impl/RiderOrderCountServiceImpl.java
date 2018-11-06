package com.dz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IRiderOrderCountDao;
import com.dz.entity.RiderOrderCount;
import com.dz.service.IRiderOrderCountService;

@Transactional(readOnly=false)
@Service("riderOrderCountService")
public class RiderOrderCountServiceImpl implements IRiderOrderCountService{

	@Autowired
	private IRiderOrderCountDao riderOrderCountDao;
	
	@Override
	public void delete(String id) {
		riderOrderCountDao.delete(id);
	}

	@Override
	public RiderOrderCount getid(int id) {
		return riderOrderCountDao.getid(id);
	}

	@Override
	public void saveORupdate(RiderOrderCount riderOrderCount) {
		riderOrderCountDao.saveORupdate(riderOrderCount);
	}

}
