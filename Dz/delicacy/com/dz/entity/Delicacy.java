package com.dz.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//美食商家属性

@Entity
@Table(name = "dz_delicacy")
public class Delicacy implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)		//主键id
	private int id;
	
	@Column(name = "companyId")		//自定义id
	private String companyId;
	
	@Column(name = "GDP")		//人均消费
	private String gdp;
	
	@Column(name = "meal_fee") // 餐位费
	private String mealFee;

	public String getMealFee() {
		return mealFee;
	}

	public void setMealFee(String mealFee) {
		this.mealFee = mealFee;
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

	public String getGdp() {
		return gdp;
	}

	public void setGdp(String gdp) {
		this.gdp = gdp;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
