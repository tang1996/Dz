package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IWalletDao;
import com.dz.entity.Wallet;
import com.dz.service.IWalletService;

@Transactional(readOnly=false)
@Service("walletService")
public class WalletServiceImpl implements IWalletService{

	@Autowired
	IWalletDao walletDao;
	
	@Override
	public void delete(final String id) {
		walletDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Wallet user) {
		walletDao.saveORupdate(user);
	}

	@Override
	public Wallet userwallet(int userId) {
		return walletDao.userwallet(userId);
	}
	
}
