package com.dz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.ISalerInfoDao;
import com.dz.entity.SalerInfo;
import com.dz.service.ISalerInfoService;
import com.dz.util.StringUtil;

@Transactional(readOnly = false)
@Service("salerInfoService")
public class SalerInfoServiceImpl implements ISalerInfoService {

	@Autowired
	ISalerInfoDao salerInfoDao;

	@Override
	public List<SalerInfo> salerInfoList(SalerInfo salerInfo) {
		String sql = "SELECT o FROM SalerInfo o WHERE 1=1";
		if (!StringUtil.isEmpty(salerInfo.getPhone())) {
			sql = sql + " and o.phone=" + salerInfo.getPhone();
		}
		return salerInfoDao.salerInfoList(sql);
	}

	@Override
	public void delete(final String id) {
		salerInfoDao.delete(id);
	}

	@Override
	public void saveORupdate(final SalerInfo salerInfo) {
		salerInfoDao.saveORupdate(salerInfo);
	}

	@Override
	public SalerInfo getid(final int id) {
		return salerInfoDao.getid(id);
	}

	@Override
	public SalerInfo getPhone(final String phone) {
		return salerInfoDao.getPhone(phone);
	}

	@Override
	public List<SalerInfo> countList(SalerInfo saler) {
		String sql = "SELECT o FROM SalerInfo o WHERE 1=1";
		if (!StringUtil.isEmpty(saler.getPhone())) {
			sql = sql + " and o.phone =" + saler.getPhone();
		}
		if (!StringUtil.isEmpty(saler.getCity())) {
			sql = sql + " and o.city = '" + saler.getCity() + "'";
		}
		if (saler.getSalerPowerId() != null) {
			sql = sql + " and o.salerPowerId ="
					+ saler.getSalerPowerId().getId();
		}
		sql = sql + " order by o.createTime DESC";
		return salerInfoDao.countList(sql);
	}

	@Override
	public List<SalerInfo> countList(SalerInfo saler, int start, int limit) {
		String sql = "SELECT o FROM SalerInfo o WHERE 1=1";
		if (!StringUtil.isEmpty(saler.getPhone())) {
			sql = sql + " and o.phone =" + saler.getPhone();
		}
		if (!StringUtil.isEmpty(saler.getCity())) {
			sql = sql + " and o.city = '" + saler.getCity() + "'";
		}
		if (saler.getSalerPowerId() != null) {
			sql = sql + " and o.salerPowerId ="
					+ saler.getSalerPowerId().getId();
		}
		sql = sql + " order by o.createTime DESC";
		return salerInfoDao.countList(sql, start, limit);
	}

	@Override
	public List<SalerInfo> getBossId(int bossId) {
		return salerInfoDao.getBossId(bossId);
	}

	@Override
	public SalerInfo getCode(String code) {
		return salerInfoDao.getCode(code);
	}

}
