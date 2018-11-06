package com.dz.dao;

import java.math.BigInteger;
import java.util.List;

import com.dz.entity.BookTime;


/**
 * 用户DAO
 * @author GJ
 * @date   2015年4月15日
 */
public interface IBookTimeDao {
 
	List<BookTime> bookTimeList(String sql);		//查询
	
	List<BookTime> getBookTime(int reserevId, String date);
	
	List<BookTime> getBookTime(int order, String startTime, String endTime);
	
	List<BookTime> getCompanyBookTime(int companyId, String date);
	
	public void delete(final String id) ;		//删除
	
	public void saveORupdate(final BookTime bookTime);		//添加或修改信息
	
	Long getReserve(int companyId, String date) ;
	
	List<BookTime> getCompanyBook(int companyId, String date);	//李海洋
	
	List<BookTime> getCompanyBook(int companyId, String datestart,String dateend);	//李海洋
	
	List<Object[]> getCompanyBookByCall(int companyId, String datestart,String dateend,int Page,String sql);	//调用存储过程取已订菜分页 ynw	
	
	BigInteger getCompanyBookCountByCall(int companyId, String datestart,String dateend,int Page,String sql);	//调用存储过程取已订菜分页总数 ynw
	
	List<Object[]> getNOCompanyBookByCall(int companyId, String datestart,String dateend,int Page,String sql);	//调用存储过程取未订菜分页 ynw
	
	BigInteger getNOCompanyBookCountByCall(int companyId, String datestart,String dateend,int Page,String sql);	//调用存储过程取未订菜分页总数 ynw
	
	BookTime getBookTime(int orderId);//2018-10-20 @Tyy
	
}

 
