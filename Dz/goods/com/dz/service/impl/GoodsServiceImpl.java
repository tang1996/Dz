package com.dz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dz.dao.IGoodsDao;
import com.dz.entity.Goods;
import com.dz.service.IGoodsService;
import com.dz.util.StringUtil;

/**
 * 登陆业务实现类
 * 
 * @author GJ
 * @date 2015年4月15日
 */
@Transactional(readOnly = false)
@Service("goodsService")
public class GoodsServiceImpl implements IGoodsService {

	@Autowired
	IGoodsDao goodsDao;

	// 添加或修改信息
	@Override
	public void saveORupdate(final Goods goods) {
		goodsDao.saveORupdate(goods);
	}

	// 添加或修改信息
	@Override
	public void update(final Goods goods) {
		goodsDao.update(goods);
	}

	// 下架
	@Override
	public void down(final int id, final int shelves) {
		goodsDao.down(id, shelves);
	}

	// 获取单个商品
	@Override
	public Goods getGoods(int id) {
		return goodsDao.getGoods(id);
	}

	// 按商家和商品分类获取商品
	@Override
	public List<Goods> goodsList(int companyId, int ificationId) {
		return goodsDao.goodsList(companyId, ificationId);
	}

	// 商家 App 按商家和商品分类获取商品
	@Override
	public List<Goods> companyGoods(int companyId, int ificationId) {
		return goodsDao.companyGoods(companyId, ificationId);
	}

	// 商家 App 按商家和商品分类获取商品
	@Override
	public List<Goods> companyGoodsWm(int companyId, int ificationId, int type) {
		return goodsDao.companyGoodsWm(companyId, ificationId, type);
	}

	// 按商家获取商品
	@Override
	public List<Goods> getCGoods(int companyid, int type) {
		return goodsDao.getCGoods(companyid, type);
	}

	// 按商家获取商品
	@Override
	public List<Goods> backGoods(int companyid, int type) {
		return goodsDao.backGoods(companyid, type);
	}

	@Override // 按商家和商家分类查询已上架的商品 ynw
	public List<Goods> computerGoodsWm(int companyId, int ificationId, int type) {
		return goodsDao.computerGoodsWm(companyId, ificationId, type);
	}

	// 管理后台
	// 后台获取商品
	@Override
	public List<Goods> bastGoods(Goods goods) {
		String sql = "SELECT o FROM Goods o WHERE 1=1";
		if (!StringUtil.isEmpty(goods.getName())) {
			sql = sql + " and o.name like '%" + goods.getName() + "%'";
		}
		if (goods.getCompanyId() != null) {
			sql = sql + " and o.companyId=" + goods.getCompanyId().getId();
		}
		return goodsDao.bastGoods(sql);
	}

}