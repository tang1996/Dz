package com.dz.service;

import java.util.List;

import com.dz.entity.Delicacy;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IDelicacyService {

	List<Delicacy> delicacyList(Delicacy delicacy);
	
	Delicacy getDelicacy(int companyId);
	
	Delicacy find(int id);

	public void delete(final String id);

	public void saveORupdate(final Delicacy delicacy);

}
