package com.dz.service;

import java.util.List;

import com.dz.entity.Track;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface ITrackService {

	List<Track> trackList(Track track);
	
	List<Track> getTrack(int orderId);
	
	List<Track> getTracks(int orderId);

	public void delete(final String id);

	public void saveORupdate(final Track track);

}
