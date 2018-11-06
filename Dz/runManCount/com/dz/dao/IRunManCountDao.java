package com.dz.dao;

import java.util.List;

import com.dz.entity.RunManCount;

/**
 * 用户DAO
 * 
 * @author GJ
 * @date 2015年4月15日
 */
public interface IRunManCountDao {

	List<RunManCount> runManCountList(String sql); // 查询

	List<RunManCount> getrunManCount(int userId);

	public void delete(final String id); // 删除

	public void saveORupdate(final RunManCount runManCount); // 添加或修改信息

	Object[] runManCount(int userId);// 商家总交易统计

	Object[] getRunManCount(int userId, String date);// 商家当日交易统计

}
