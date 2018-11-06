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

//KTV商品属性
@Entity
@Table(name = "dz_box")
public class Box implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false) // 主键id
	private int id;

	@Column(name = "consumption") // 消费类型
	private String consumption;
	
	@Column(name = "theme") // 包厢主题
	private String theme;
	
	@Column(name = "start_time") // 套餐开始时间
	private String startTime;
	
	@Column(name = "continued_time") // 持续时间
	private String continuedTime;
	
	@Column(name = "is_full") // 是否满员
	private boolean isFull;
	
	@Column(name = "price") // 套餐价格
	private String price;

	@ManyToOne
	@JoinColumn(name = "goods_id") // 商品id
	private Goods goodsId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getConsumption() {
		return consumption;
	}

	public void setConsumption(String consumption) {
		this.consumption = consumption;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getContinuedTime() {
		return continuedTime;
	}

	public void setContinuedTime(String continuedTime) {
		this.continuedTime = continuedTime;
	}

	public boolean getIsFull() {
		return isFull;
	}

	public void setIsFull(boolean isFull) {
		this.isFull = isFull;
	}

	public Goods getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Goods goodsId) {
		this.goodsId = goodsId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
