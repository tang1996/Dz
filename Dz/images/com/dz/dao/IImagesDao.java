package com.dz.dao;

import java.util.List;

import com.dz.entity.Images;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IImagesDao {
 
	List<Images> imagesList(String sql);		//查询
	
	Images getImage(String customId, String scene);		//查询
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final Images images);		//添加或修改信息
	
}

 
