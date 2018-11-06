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

//地址实体类
@Entity
@Table(name = "dz_companyPayRecord")
public class CompanyPayRecord implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)		//主键id
	private int id;
	
	@Column(name = "date")		//日期
	private String date;
	
	@ManyToOne
	@JoinColumn(name = "company_id")		//商家id
	private Company companyId;
	
	@Column(name = "balance")		//交易金额
	private String balance;
	
	@Column(name = "order_no")		//商家id
	private String orderNo;
	
	@Column(name = "update_time")		//支付完成时间
	private String updateTime;
	
	@Column(name = "renew_time")		//续费时长     单位：天
	private String renewTime;
	
	@Column(name = "is_account")		//是否到账
	private boolean isAccount;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public boolean getIsAccount() {
		return isAccount;
	}

	public void setIsAccount(boolean isAccount) {
		this.isAccount = isAccount;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getRenewTime() {
		return renewTime;
	}

	public void setRenewTime(String renewTime) {
		this.renewTime = renewTime;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Company getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Company companyId) {
		this.companyId = companyId;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
