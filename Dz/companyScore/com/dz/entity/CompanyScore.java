package com.dz.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//商家评价属性实体类
@Entity
@Table(name = "dz_companyScore")
public class CompanyScore implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	// 主键id
	private int id;

	@Column(name = "taste")
	// 菜品口味
	private String taste;

	@Column(name = "quality")
	// 服务质量评分
	private String quality;

	@ManyToOne
	@JoinColumn(name = "evaluate_id")
	// 商家id
	private Evaluate evaluateId;

	public Evaluate getEvaluateId() {
		return evaluateId;
	}

	public void setEvaluateId(Evaluate evaluateId) {
		this.evaluateId = evaluateId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTaste() {
		return taste;
	}

	public void setTaste(String taste) {
		this.taste = taste;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
