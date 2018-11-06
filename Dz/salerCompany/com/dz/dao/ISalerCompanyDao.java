package com.dz.dao;

import java.util.List;

import com.dz.entity.SalerCompany;

/**
 * 用户DAO
 * 
 * @author GJ
 * @date 2015年4月15日
 */
public interface ISalerCompanyDao {

	List<SalerCompany> salerCompanyList(String sql); // 管理员列表

	public void delete(final String id); // 删除管理员

	public void saveORupdate(final SalerCompany salerCompany); // 添加或修改管理员信息

	SalerCompany getid(final int id); // 按id查询管理员

	Object[] count(String sql);

}
