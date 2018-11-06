package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IActivityDao;
import com.dz.entity.Activity;
import com.dz.service.IActivityService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("activityService")
public class ActivityServiceImpl implements IActivityService{

	@Autowired
	IActivityDao activityDao;
	
	@Override
	public List<Activity> activityList(Activity activity) {
		String sql = "SELECT o FROM Activity o WHERE 1=1";
		return activityDao.activityList(sql);
	}
	
	@Override
	public Activity getActivity(int id) {
		return activityDao.getActivity(id);
	}
	
	@Override
	public void delete(final String id) {
		activityDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Activity activity) {
		activityDao.saveORupdate(activity);
	}
	
}