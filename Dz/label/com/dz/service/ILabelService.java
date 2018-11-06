package com.dz.service;

import java.util.List;

import com.dz.entity.Label;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface ILabelService {

	List<Label> labelList(Label label);
	
	List<Label> getLabel(String type,String customId);

	public void delete(final String id);

	public void saveORupdate(final Label label);
	
	Long count(String label, String type);

}
