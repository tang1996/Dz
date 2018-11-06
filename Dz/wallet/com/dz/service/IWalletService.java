package com.dz.service;


import com.dz.entity.Wallet;


/**
 * 通用dao，包括基本的CRUD方法
 * @author Ljh
 *
 */

public interface IWalletService {
 
	Wallet userwallet(int userId);//按用户查询
	
	public void delete(final String id);//删除
	
	public void saveORupdate(final Wallet wallet);//添加或修改信息
	
}
 
