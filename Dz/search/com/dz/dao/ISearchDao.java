package com.dz.dao;

import java.util.List;

import com.dz.entity.Search;


/**
 * 用户钱包DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface ISearchDao {
 
	List<Search> searchList(String sql);
	
	List<Object[]>  countSearch();
	
	List<Search> userSearch(int userId);
	
	List<Search> getSearch(String keyword, String location);
	
	public void delete(String id) ;
	
	public void saveORupdate(final Search search);
	
	public void userDelete(int id) ;
	
}

 
