package com.dz.dao;

import java.util.List;

import com.dz.entity.Activity;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IActivityDao {
 
	List<Activity> activityList(String sql);		//查询
	
	Activity getActivity(int id);
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final Activity activity);		//添加或修改信息
	
}

 
