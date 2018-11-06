package com.dz.service;

import java.util.List;

import com.dz.entity.Distribution;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IDistributionService {

	List<Distribution> distributionList(Distribution distribution);
	
	Distribution getDistribution(int companyId);
	
	Distribution find(int id);

	public void delete(final String id);

	public void saveORupdate(final Distribution distribution);

}
