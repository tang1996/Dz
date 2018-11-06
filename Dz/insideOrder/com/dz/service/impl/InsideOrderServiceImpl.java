package com.dz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IInsideOrderDao;
import com.dz.entity.InsideOrder;
import com.dz.service.IInsideOrderService;

@Transactional(readOnly = false)
@Service("insideOrderService")
public class InsideOrderServiceImpl implements IInsideOrderService {

	@Autowired
	IInsideOrderDao insideOrderDao;

	@Override
	public List<InsideOrder> orderList(InsideOrder order) {
		String sql = "SELECT o FROM InsideOrder o WHERE 1=1";
		return insideOrderDao.orderList(sql);
	}

	@Override
	public List<InsideOrder> getStatus(String status) {
		return insideOrderDao.getStatus(status);
	}

	@Override
	public List<InsideOrder> getStatus(int companyId, String status) {
		return insideOrderDao.getStatus(companyId, status);
	}

	@Override
	public List<InsideOrder> getDoing(int userId, int start, int limit) {
		return insideOrderDao.getDoing(userId, start, limit);
	}

	@Override
	public InsideOrder getInsideOrder(int id) {
		return insideOrderDao.getInsideOrder(id);
	}

	public InsideOrder getInsideOrderNo(String id) {
		return insideOrderDao.getInsideOrderNo(id);
	}

	@Override
	public void delete(final String id) {
		insideOrderDao.delete(id);
	}

	@Override
	public void saveORupdate(final InsideOrder user) {
		insideOrderDao.saveORupdate(user);
	}

	@Override
	public InsideOrder get(int userId, int companyId) {
		return insideOrderDao.get(userId, companyId);
	}

	@Override
	public List<InsideOrder> getAll(int userId, String status, int start, int limit) {
		return insideOrderDao.getAll(userId, status, start, limit);
	}

	@Override
	public List<InsideOrder> getAll(int userId, int start, int limit) {
		return insideOrderDao.getAll(userId, start, limit);
	}

	@Override
	public List<InsideOrder> getAllInsideOrder(int companyId, String status, int start,
			int limit) {
		return insideOrderDao.getAllInsideOrder(companyId, status, start, limit);
	}

	@Override
	public InsideOrder track(String id) {
		return insideOrderDao.track(id);
	}

	@Override
	public Long count(int companyId) {
		return insideOrderDao.count(companyId);
	}

	@Override
	public Object[] getCount(int companyId, String date) {
		return insideOrderDao.getCount(companyId, date);
	}

	@Override
	public Object[] getCount(int companyId, String startTime, String endTime,
			String type) {
		return insideOrderDao.getCount(companyId, startTime, endTime, type);
	}
	
	@Override
	public Long getTypeInsideOrder(int companyId, String startTime, String endTime){
		return insideOrderDao.getTypeInsideOrder(companyId, startTime, endTime);
	}

	@Override
	public List<InsideOrder> getList(int companyId, String startTime, String endTime, String classify) {
		String sql = "SELECT o FROM InsideOrder o WHERE o.companyId=:v_companyId and o.addTime>=:v_staetDate and o.addTime<=:v_endDate";
		if(classify.equals("finish")){
			sql = sql + " and o.orderStatus='finish'";// and o.isAccount=1";
		}
		if(classify.equals("doing")){
			sql = sql + " and o.orderStatus='doing'";
		}
		if(classify.equals("cancel")){
			sql = sql + " and o.orderStatus='finish' and o.isAccount=4";
		}
		sql = sql + " order by o.addTime desc";
		return insideOrderDao.getList(companyId, startTime, endTime, sql);
	}
	
	@Override
	public List<InsideOrder> getList(int companyId, String type, String classify) {
		String sql = "SELECT o FROM InsideOrder o WHERE o.companyId=:v_companyId and o.orderType=:v_orderType";
		if(classify.equals("finish")){
			sql = sql + " and o.orderStatus='finish' and o.isAccount=1";
		}
		if(classify.equals("doing")){
			sql = sql + " and o.orderStatus='doing'";
		}
		if(classify.equals("cancel")){
			sql = sql + " and o.orderStatus='finish' and o.isAccount=4";
		}
		sql = sql + " order by o.addTime desc";
		return insideOrderDao.getList(companyId, type, sql);
	}

	@Override
	public List<InsideOrder> getIsSend(int userId, int start, int limit) {
		return insideOrderDao.getIsSend(userId, start, limit);
	}

	@Override
	public List<InsideOrder> getInsideOrderByThree(int companyId, String seat) {
		return insideOrderDao.getInsideOrderByThree(companyId, seat);
	}
	
	@Override
	public List<InsideOrder> getBaseList(int companyId, String startTime, String endTime) {// 线下订单列表
		return insideOrderDao.getBaseList(companyId, startTime, endTime);
	}

	@Override
	public Object[] getCount(int companyId, String startTime, String endTime) {// 线下订单统计
		return insideOrderDao.getCount(companyId, startTime, endTime);
	}
	
}
