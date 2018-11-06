package com.dz.service;


import java.util.List;

import com.dz.entity.Search;


/**
 * 通用dao，包括基本的CRUD方法
 * @author Ljh
 *
 */

public interface ISearchService {
 
	List<Search> searchList(Search search);
	
	List<Object[]>  countSearch();
	
	List<Search> userSearch(int userId);
	
	List<Search> getSearch(String keyword, String location);
	
	public void delete(final String id);
	
	public void saveORupdate(final Search search);
	
	public void userDelete(int id);
	
}
 
