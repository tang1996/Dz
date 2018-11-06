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

/**
 * 用户表
 */
@Entity
@Table(name = "dz_powerSort")
public class PowerSort implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false) // 主键id
	private int id;

	@Column(name = "sort") // 权限等级
	private String sort;
	
	@Column(name = "name") // 权限名称
	private String name;

	@Column(name = "info") // 权限描述
	private String info;
	
	@Column(name = "position") // 权限职位
	private String position;
	
//	@ManyToOne
//	@JoinColumn(name = "company_id") // 所属商家
//	private Company companyId;

//	public Company getCompanyId() {
//		return companyId;
//	}
//
//	public void setCompanyId(Company companyId) {
//		this.companyId = companyId;
//	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
