package com.dz.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//KTV商家属性
@Entity
@Table(name = "dz_sing")
public class Sing implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)		//主键id
	private int id;
	
	@Column(name = "companyId")		//自定义id
	private String companyId;
	
	@Column(name = "mini_consume")		//最低消费
	private String miniConsume;

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

	public String getMiniConsume() {
		return miniConsume;
	}

	public void setMiniConsume(String miniConsume) {
		this.miniConsume = miniConsume;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
