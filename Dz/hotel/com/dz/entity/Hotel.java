package com.dz.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//酒店商家属性
@Entity
@Table(name = "dz_hotel")
public class Hotel implements Serializable {
	
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
	
	@Column(name = "have_room") // 是否有房
	private boolean haveRoom;
	
	@Column(name = "star") // 星级
	private String star;
	
	@Column(name = "keyword") // 关键词
	private String keyword;

	public boolean getHaveRoom() {
		return haveRoom;
	}

	public void setHaveRoom(boolean haveRoom) {
		this.haveRoom = haveRoom;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
