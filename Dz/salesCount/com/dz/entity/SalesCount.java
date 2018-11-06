package com.dz.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

//业务员统计表实体类
@Entity
@Table(name = "dz_salesCount")
public class SalesCount implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)		//主键id
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "salesman_id") // 业务员id
	private Salesman salesmanId;
	
	@Column(name = "create_time") // 添加时间id
	private String creatTime;
	
	@OneToOne
	@JoinColumn(name = "company_id") // 商家id
	private  Company companyId;
	
	

	public Salesman getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(Salesman salesmanId) {
		this.salesmanId = salesmanId;
	}

	public String getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	public Company getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Company companyId) {
		this.companyId = companyId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}