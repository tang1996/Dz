package com.dz.dao;

import com.dz.entity.RiderOrderCount;

public interface IRiderOrderCountDao {
	
	public void delete(final String id);	//删除
		
	public void saveORupdate(final RiderOrderCount riderOrderCount);	//保存或更新
	
	RiderOrderCount getid(final int id);	//根据Id查找对应骑手信息
}
