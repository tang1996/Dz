package com.dz.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//商品评价实体类
@Entity
@Table(name = "dz_parcel")
public class Parcel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	// 主键id
	private int id;

	@ManyToOne
	@JoinColumn(name = "goods_id")
	// 商品id
	private Goods goodsId;

	@Column(name = "speen")
	// 送餐速度评分
	private String speen;

	@Column(name = "taste")
	// 口味评分
	private String taste;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	// 评分订单
	private Order orderId;

	public Goods getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Goods goodsId) {
		this.goodsId = goodsId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSpeen() {
		return speen;
	}

	public void setSpeen(String speen) {
		this.speen = speen;
	}

	public String getTaste() {
		return taste;
	}

	public void setTaste(String taste) {
		this.taste = taste;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
