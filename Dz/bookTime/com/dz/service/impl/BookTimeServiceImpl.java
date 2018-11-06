package com.dz.service.impl;


import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IBookTimeDao;
import com.dz.entity.BookTime;
import com.dz.service.IBookTimeService;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("bookTimeService")
public class BookTimeServiceImpl implements IBookTimeService{

	@Autowired
	IBookTimeDao bookTimeDao;
	
	@Override
	public List<BookTime> bookTimeList(BookTime bookTime) {
		String sql = "SELECT o FROM BookTime o WHERE 1=1";
		return bookTimeDao.bookTimeList(sql);
	}
	
	@Override
	public List<BookTime> getBookTime(int reserevId, String date) {
		return bookTimeDao.getBookTime(reserevId, date);
	}
	
	@Override
	public List<BookTime> getBookTime(int order, String startTime, String endTime){
		return bookTimeDao.getBookTime(order, startTime, endTime);
	}
	
	@Override
	public Long getReserve(int companyId, String date) {
		return bookTimeDao.getReserve(companyId, date);
	}
	
	@Override
	public List<BookTime> getCompanyBookTime(int companyId, String date) {
		return bookTimeDao.getCompanyBookTime(companyId, date);
	}
	
	@Override
	public void delete(final String id) {
		bookTimeDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final BookTime bookTime) {
		bookTimeDao.saveORupdate(bookTime);
	}
	


	@Override	//lhy
	public List<BookTime> getCompanyBook(int companyId, String datestart,
			String dateend) {
		return bookTimeDao.getCompanyBook(companyId, datestart, dateend);
	}

	@Override	//调用存储过程取分页 ynw
	public List<Object[]> getCompanyBookByCall(int companyId, String datestart,
			String dateend, int Page,String sql) {
		return bookTimeDao.getCompanyBookByCall(companyId, datestart, dateend, Page, sql);
	}

	@Override	//调用存储过程取分页 ynw
	public BigInteger getCompanyBookCountByCall(int companyId, String datestart,
			String dateend, int Page,String sql) {
		return bookTimeDao.getCompanyBookCountByCall(companyId, datestart, dateend, Page, sql);
	}

	@Override	//调用存储过程取未订菜分页 ynw
	public List<Object[]> getNOCompanyBookByCall(int companyId,
			String datestart, String dateend, int Page,String sql) {
		return bookTimeDao.getNOCompanyBookByCall(companyId, datestart, dateend, Page, sql);
	}

	@Override	//调用存储过程取未订菜分页总数 ynw
	public BigInteger getNOCompanyBookCountByCall(int companyId,
			String datestart, String dateend, int Page,String sql) {
		return bookTimeDao.getNOCompanyBookCountByCall(companyId, datestart, dateend, Page, sql);
	}
	
	public BookTime getBookTime(int orderId) {//2018-10-20 @Tyy
		return bookTimeDao.getBookTime(orderId);
	}
	
}