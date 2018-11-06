package com.dz.service;

import java.util.List;

import com.dz.entity.Box;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IBoxService {

	List<Box> boxList(Box box);
	
	List<Box> getBox(int goodId);

	public void delete(final String id);

	public void saveORupdate(final Box box);

}
