package com.dz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IIncludeDao;
import com.dz.entity.Include;
import com.dz.service.IIncludeService;

/**
 * 登陆业务实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Transactional(readOnly = false)
@Service("includeService")
public class IncludeServiceImpl implements IIncludeService {

	@Autowired
	IIncludeDao includeDao;

	@Override
	public List<Include> getInclude(int userId) {
		return includeDao.getInclude(userId);
	}

	@Override
	public Include include(int id) {
		return includeDao.include(id);
	}

	@Override
	public Include getInclude(int userId, int companyId) {
		return includeDao.getInclude(userId, companyId);
	}

	@Override
	public void delete(final String id) {
		includeDao.delete(id);
	}

	@Override
	public void saveORupdate(final Include include) {
		includeDao.saveORupdate(include);
	}

	// 后台管理
	@Override
	public List<Include> includeList(Include include) {
		String sql = "SELECT o FROM Include o WHERE 1=1";
		if (include.getCompanyId() != null) {
			sql = sql + " and o.companyId=" + include.getCompanyId().getId();
		}
		if (include.getUserId() != null) {
			sql = sql + " and o.userId=" + include.getUserId().getId();
		}
		return includeDao.includeList(sql);
	}

	@Override /* ynw */
	public List<Include> includeList(Include include, int start, int limit) {
		String sql = "SELECT o FROM Include o WHERE 1=1";
		if (include.getCompanyId() != null) {
			sql = sql + " and o.companyId=" + include.getCompanyId().getId();
		}
		if (include.getUserId() != null) {
			sql = sql + " and o.userId=" + include.getUserId().getId();
		}
		return includeDao.includeList(sql);
	}

}