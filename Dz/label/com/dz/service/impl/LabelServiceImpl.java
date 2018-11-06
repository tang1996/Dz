package com.dz.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.ILabelDao;
import com.dz.entity.Company;
import com.dz.entity.Label;
import com.dz.service.ILabelService;
import com.dz.util.StringUtil;


/**
 * 登陆业务实现类
 * @author GJ
 * @date   2015年4月15日
 */
@Transactional(readOnly=false)
@Service("labelService")
public class LabelServiceImpl implements ILabelService{

	@Autowired
	ILabelDao labelDao;
	
	@Override
	public List<Label> labelList(Label label) {
		String sql = "SELECT o FROM Label o WHERE 1=1";
		return labelDao.labelList(sql);
	}
	
	@Override
	public List<Label> getLabel(String type,String customId){
		return labelDao.getLabel(type,customId);
	}
	
	@Override
	public void delete(final String id) {
		labelDao.delete(id);
	}
	
	@Override
	public void saveORupdate(final Label label) {
		labelDao.saveORupdate(label);
	}
	
	// 分类商家总数
	@Override
	public Long count(String label, String type) {
		String sql = "SELECT COUNT(o) FROM Label o WHERE o.type=:v_type";
		if (!StringUtil.isEmpty(label)) {
			sql = sql + " and o.content like '%" + label + "%'";
		}
		return labelDao.count(sql,type);
	}
}