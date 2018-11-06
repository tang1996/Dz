package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IImagesDao;
import com.dz.entity.Images;
import com.dz.service.IImagesService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("imagesService")
public class ImagesServiceImpl implements IImagesService{

	@Autowired
	IImagesDao imagesDao;
	
	@Override
	public List<Images> imagesList(Images images) {
		String sql = "SELECT o FROM Images o WHERE 1=1";
		return imagesDao.imagesList(sql);
	}
	
	@Override
	public List<Images> getImages(String customId, String scene) {
		String sql = "SELECT o FROM Images o WHERE 1=1";
		return imagesDao.imagesList(sql);
	}
	
	@Override
	public Images getImage(String customId, String scene) {
		return imagesDao.getImage(customId,scene);
	}
	
	@Override
	public void delete(final String id) {
		imagesDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Images images) {
		imagesDao.saveORupdate(images);
	}
	
}