package com.dz.service;

import java.util.List;

import com.dz.entity.Include;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IIncludeService {

	List<Include> getInclude(int userId);// 按用户查询

	Include include(int id);// 按id查询

	Include getInclude(int userId, int companyId);// 按id查询

	public void delete(final String id);// 删除

	public void saveORupdate(final Include include);// 添加或修改信息

	// 管理后台
	List<Include> includeList(Include include);

	List<Include> includeList(Include include, int start, int limit); /* ynw */

}
