package com.dz.service;

import java.util.List;

import com.dz.entity.GoodsActivity;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IGoodsActivityService {

	List<GoodsActivity> goodsActivityList(GoodsActivity goodsActivity);
	
	List<GoodsActivity> getList(Integer id);

	public void delete(final String id);

	public void saveORupdate(final GoodsActivity goodsActivity);

}
