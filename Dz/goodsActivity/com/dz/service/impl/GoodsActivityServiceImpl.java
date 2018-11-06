package com.dz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IGoodsActivityDao;
import com.dz.entity.GoodsActivity;
import com.dz.service.IGoodsActivityService;

/**
 * 登陆业务实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Transactional(readOnly = false)
@Service("goodsActivityService")
public class GoodsActivityServiceImpl implements IGoodsActivityService {

	@Autowired
	IGoodsActivityDao goodsActivityDao;

	@Override
	public List<GoodsActivity> goodsActivityList(GoodsActivity goodsActivity) {
		String sql = "SELECT o FROM Relation o WHERE 1=1";
		return goodsActivityDao.goodsActivityList(sql);
	}

	@Override
	public List<GoodsActivity> getList(Integer id) {
		return goodsActivityDao.getList(id);
	}

	@Override
	public void delete(final String id) {
		goodsActivityDao.delete(id);
	}

	@Override
	public void saveORupdate(final GoodsActivity goodsActivity) {
		goodsActivityDao.saveORupdate(goodsActivity);
	}

}