package com.dz.dao;

import java.util.List;

import com.dz.entity.SalerInfo;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface ISalerInfoDao {
 
	List<SalerInfo> salerInfoList(String sql);		//管理员列表
	
	public void delete(final String id) ;		//删除管理员
	
	public void saveORupdate(final SalerInfo salerInfo);		//添加或修改管理员信息
	
	SalerInfo getid(final int id);		//按id查询管理员
	
	SalerInfo getPhone(final String phone);		//按id查询管理员
	
	List<SalerInfo> countList(String sql);

	List<SalerInfo> countList(String sql, int start, int limit);
	
	List<SalerInfo> getBossId(int bossId);		//获取业务员上司
	
	SalerInfo getCode(String code);
	
}

 
