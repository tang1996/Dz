package com.dz.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//外卖商家属性
@Entity
@Table(name = "dz_distribution")
public class Distribution implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)		//主键id
	private int id;
	
	@Column(name = "companyId")		//自定义id
	private String companyId;
	
	@Column(name = "distribution_time")		//配送时间
	private String time;
	
	@Column(name = "mode")		//配送方式
	private String mode;
	
	@Column(name = "distribution_price")		//配送价格
	private String distributionPrice;
	
	@Column(name = "GDP")		//人均消费
	private String GDP;
	
	@Column(name = "mini_price")		//起送价格
	private String miniPrice;

	public String getDistributionPrice() {
		return distributionPrice;
	}

	public void setDistributionPrice(String distributionPrice) {
		this.distributionPrice = distributionPrice;
	}

	public String getMiniPrice() {
		return miniPrice;
	}

	public void setMiniPrice(String miniPrice) {
		this.miniPrice = miniPrice;
	}

	public String getGDP() {
		return GDP;
	}

	public void setGDP(String gDP) {
		GDP = gDP;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
