package com.dz.service;

import java.util.List;

import com.dz.entity.Meun;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IMeunService {

	List<Meun> meunList(Meun meun);
	
	List<Meun> getmeun(int userId);

	public void delete(final String id);

	public void saveORupdate(final Meun meun);

}
