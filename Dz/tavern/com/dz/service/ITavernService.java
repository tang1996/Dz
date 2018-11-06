package com.dz.service;

import java.util.List;

import com.dz.entity.Tavern;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface ITavernService {

	List<Tavern> tavernList(Tavern tavern);
	
	List<Tavern> getTavern(int stayId);

	public void delete(final String id);

	public void saveORupdate(final Tavern tavern);

}
