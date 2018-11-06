package com.dz.service;

import java.util.List;

import com.dz.entity.SalerInfo;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface ISalerInfoService {

	List<SalerInfo> salerInfoList(SalerInfo salerInfo);

	public void delete(final String id);

	public void saveORupdate(final SalerInfo salerInfo);

	SalerInfo getid(final int id);

	SalerInfo getPhone(final String phone);

	List<SalerInfo> countList(SalerInfo saler);

	List<SalerInfo> countList(SalerInfo saler, int start, int limit);
	
	List<SalerInfo> getBossId(int bossId);
	
	SalerInfo getCode(String code);

}
