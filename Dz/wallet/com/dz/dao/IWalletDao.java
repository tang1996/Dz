package com.dz.dao;

import java.util.List;

import com.dz.entity.Wallet;


/**
 * 用户钱包DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IWalletDao {
 
	Wallet userwallet(int userId);
	
	public void delete(final String id) ;
	
	public void saveORupdate(final Wallet wallet);
	
}

 
