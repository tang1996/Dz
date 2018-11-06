package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IOpinionDao;
import com.dz.entity.Opinion;
import com.dz.service.IOpinionService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("opinionService")
public class OpinionServiceImpl implements IOpinionService{

	@Autowired
	IOpinionDao opinionDao;
	
	@Override
	public Opinion opinion(int id) {
		return opinionDao.opinion(id);
	}
	
	@Override
	public void delete(final String id) {
		opinionDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Opinion opinion) {
		opinionDao.saveORupdate(opinion);
	}
	
	//管理后台
	@Override
	public List<Opinion> opinionList(Opinion opinion, int start, int limit) {
		String sql = "SELECT o FROM Opinion o WHERE 1=1 order by o.createTime DESC";
		return opinionDao.opinionList(sql, start, limit);
	}
	
}