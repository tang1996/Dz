package com.dz.dao;

import java.util.List;

import com.dz.entity.Adware;

/**
 * 用户DAO
 * 
 * @author GJ
 * @date 2015年4月15日
 */
public interface IAdwareDao {

	List<Adware> getAdware(String type);// 按类型查询

	public void delete(final String id); // 删除

	public void saveORupdate(final Adware adware); // 添加或修改信息

	// 管理后台
	List<Adware> getAdwareList(String sql);

	List<Adware> getAdwareList(String sql, int start, int limit); /* ynw */

	Adware getAdware(int id);// 按类型查询

}
