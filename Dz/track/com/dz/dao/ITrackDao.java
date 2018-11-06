package com.dz.dao;

import java.util.List;

import com.dz.entity.Track;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface ITrackDao {
 
	List<Track> trackList(String sql);		//查询
	
	List<Track> getTrack(int orderId);	
	
	List<Track> getTracks(int orderId);	
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final Track track);		//添加或修改信息
	
}

 
