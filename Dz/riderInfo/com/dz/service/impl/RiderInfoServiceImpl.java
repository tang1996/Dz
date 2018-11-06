package com.dz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IRiderInfoDao;
import com.dz.entity.RiderInfo;
import com.dz.service.IRiderInfoService;

@Transactional(readOnly = false)
@Service("riderInfoService")
public class RiderInfoServiceImpl implements IRiderInfoService {

	@Autowired
	IRiderInfoDao riderInfoDao;

	@Override
	public List<RiderInfo> riderInfoList(RiderInfo riderInfo) {
		String sql = "SELECT o FROM RiderInfo o WHERE 1=1";
		return riderInfoDao.riderInfoList(sql);
	}

	@Override
	public void delete(final String id) {
		riderInfoDao.delete(id);
	}

	@Override
	public void saveORupdate(final RiderInfo riderInfo) {
		riderInfoDao.saveORupdate(riderInfo);
	}

	@Override
	public RiderInfo getid(final int id) {
		return riderInfoDao.getid(id);
	}

	@Override
	public RiderInfo getuserId(int userId) {
		return riderInfoDao.getuserId(userId);
	}

}
