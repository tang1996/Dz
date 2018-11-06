package com.dz.service;

import java.util.List;

import com.dz.entity.Nature;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface INatureService {

	List<Nature> natureList(Nature nature);
	
	List<Nature> getnature(int nature);
	
	Nature nature(int id);

	public void delete(final String id);

	public void saveORupdate(final Nature nature);

}
