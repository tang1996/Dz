package com.dz.service;

import java.util.List;

import com.dz.entity.Adware;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IAdwareService {

	List<Adware> getAdware(String type);// 按类型查询

	public void delete(final String id);// 删除

	public void saveORupdate(final Adware adware);// 添加或修改信息

	// 管理后台
	List<Adware> getAdwareList(Adware adware);// 查询

	List<Adware> getAdwareList(Adware adware, int start, int limit); /* ynw */

	Adware getAdware(int id);// 查询

}
