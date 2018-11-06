package com.dz.service;

import java.util.List;

import com.dz.entity.Activity;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IActivityService {

	List<Activity> activityList(Activity activity);
	
	Activity getActivity(int id);

	public void delete(final String id);

	public void saveORupdate(final Activity activity);

}
