package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.INatureDao;
import com.dz.entity.Nature;
import com.dz.service.INatureService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("natureService")
public class NatureServiceImpl implements INatureService{

	@Autowired
	INatureDao natureDao;
	
	@Override
	public List<Nature> natureList(Nature nature) {
		String sql = "SELECT o FROM Nature o WHERE 1=1";
		return natureDao.natureList(sql);
	}
	
	@Override
	public void delete(final String id) {
		natureDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Nature nature) {
		natureDao.saveORupdate(nature);
	}

	@Override
	public List<Nature> getnature(int attribute) {
		return natureDao.getnature(attribute);
	}
	
	@Override
	public Nature nature(int id) {
		return natureDao.nature(id);
	}
	
}