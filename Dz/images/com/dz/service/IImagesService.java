package com.dz.service;

import java.util.List;

import com.dz.entity.Images;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IImagesService {

	List<Images> imagesList(Images images);//图片查询
	
	List<Images> getImages(String customId, String scene);//安场景查询
	
	Images getImage(String customId, String scene);//安场景查询单个图片

	public void delete(final String id);//删除

	public void saveORupdate(final Images images);//添加或修改信息

}
