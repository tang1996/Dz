package com.dz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IAdwareDao;
import com.dz.entity.Adware;
import com.dz.service.IAdwareService;

/**
 * 登陆业务实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Transactional(readOnly = false)
@Service("adwareService")
public class AdwareServiceImpl implements IAdwareService {

	@Autowired
	IAdwareDao adwareDao;

	// 按类型查询
	@Override
	public List<Adware> getAdware(String type) {
		return adwareDao.getAdware(type);
	}

	// 删除
	@Override
	public void delete(final String id) {
		adwareDao.delete(id);
	}

	// 添加或修改信息
	@Override
	public void saveORupdate(final Adware adware) {
		adwareDao.saveORupdate(adware);
	}

	// 管理后台
	// 查询
	@Override
	public List<Adware> getAdwareList(Adware adware) {
		String sql = "SELECT o FROM Adware o WHERE 1=1";
		return adwareDao.getAdwareList(sql);
	}

	@Override /* ynw */
	public List<Adware> getAdwareList(Adware adware, int start, int limit) {
		String sql = "SELECT o FROM Adware o WHERE 1=1";
		if (adware.getId() != 0) {
			sql += " and o.id=" + adware.getId();
		}
		return adwareDao.getAdwareList(sql);
	}

	@Override
	public Adware getAdware(int id) {
		return adwareDao.getAdware(id);
	}

}