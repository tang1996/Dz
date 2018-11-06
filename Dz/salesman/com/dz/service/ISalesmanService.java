package com.dz.service;

import java.util.List;

import com.dz.entity.Salesman;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface ISalesmanService {

	Salesman login(String userName);

	List<Salesman> salesmanList(Salesman salesman);

	public void delete(final String id);

	public void saveORupdate(final Salesman salesman);

	Salesman getid(final int id);

	List<Salesman> getuserName(final String userName);

}
