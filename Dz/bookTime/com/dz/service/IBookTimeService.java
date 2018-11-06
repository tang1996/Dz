package com.dz.service;

import java.math.BigInteger;
import java.util.List;

import com.dz.entity.BookTime;

/**
 * 通用dao，包括基本的CRUD方法
 * 
 * @author Ljh
 *
 */

public interface IBookTimeService {

	List<BookTime> bookTimeList(BookTime bookTime);
	
	List<BookTime> getBookTime(int reserevId, String date);
	
	List<BookTime> getBookTime(int order, String startTime, String endTime);

	public void delete(final String id);

	public void saveORupdate(BookTime bookTime);

	List<BookTime> getCompanyBookTime(int companyId, String date);
	
	Long getReserve(int companyId, String date) ;
	
	List<BookTime> getCompanyBook(int companyId, String datestart,String dateend);	//李海洋
	
	List<Object[]> getCompanyBookByCall(int companyId, String datestart,String dateend,int Page,String sql);	//调用存储过程取分页 ynw
	
	BigInteger getCompanyBookCountByCall(int companyId, String datestart,String dateend,int Page,String sql);	//调用存储过程取分页 ynw
	
	List<Object[]> getNOCompanyBookByCall(int companyId, String datestart,String dateend,int Page,String sql);	//调用存储过程取未订菜分页 ynw
	
	BigInteger getNOCompanyBookCountByCall(int companyId, String datestart,String dateend,int Page,String sql);	//调用存储过程取未订菜分页总数 ynw
	
	BookTime getBookTime(int orderId);//2018-10-20 @Tyy
	
}
