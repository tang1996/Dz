package com.dz.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 用户表
 */
@Entity
@Table(name = "dz_companyDetailed")
public class CompanyDetailed implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	// 主键id
	private int id;

	@Column(name = "expire_time")
	// 到期时间
	private String expireTime;

	@Column(name = "cost")
	// 年费费用
	private String cost;

	@OneToOne
    @JoinColumn(name = "saler_id")  
    private SalerInfo salerId; 
	
	@OneToOne
    @JoinColumn(name = "company_id")  
    private Company companyId; 
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public SalerInfo getSalerId() {
		return salerId;
	}

	public void setSalerId(SalerInfo salerId) {
		this.salerId = salerId;
	}

	public Company getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Company companyId) {
		this.companyId = companyId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
