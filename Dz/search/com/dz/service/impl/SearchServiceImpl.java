package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.ISearchDao;
import com.dz.entity.Search;
import com.dz.service.ISearchService;

@Transactional(readOnly=false)
@Service("searchService")
public class SearchServiceImpl implements ISearchService{

	@Autowired
	ISearchDao searchDao;
	
	@Override
	public List<Search> searchList(Search search) {
		String sql = "SELECT o FROM Search o WHERE 1=1";
		return searchDao.searchList(sql);
	}
	
	@Override
	public List<Object[]>  countSearch() {
		return searchDao.countSearch();
	}
	
	@Override
	public List<Search> userSearch(int userId) {
		return searchDao.userSearch(userId);
	}
	
	@Override
	public List<Search> getSearch(String keyword, String location){
		return searchDao.getSearch(keyword, location);
	}
	
	@Override
	public void delete(final String id) {
		searchDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Search user) {
		searchDao.saveORupdate(user);
	}
	
	@Override
	public void userDelete(int id) {
		searchDao.userDelete(id);
	}
	
}
