package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.ITrackDao;
import com.dz.entity.Track;
import com.dz.service.ITrackService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("trackService")
public class TrackServiceImpl implements ITrackService{

	@Autowired
	ITrackDao trackDao;
	
	@Override
	public List<Track> trackList(Track track) {
		String sql = "SELECT o FROM Track o WHERE 1=1";
		return trackDao.trackList(sql);
	}
	
	@Override
	public List<Track> getTrack(int orderId) {
		return trackDao.getTrack(orderId);
	}
	
	@Override
	public List<Track> getTracks(int orderId) {
		return trackDao.getTracks(orderId);
	}
	
	@Override
	public void delete(final String id) {
		trackDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Track track) {
		trackDao.saveORupdate(track);
	}
	
}