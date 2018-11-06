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

//商品属性子属性
@Entity
@Table(name = "dz_nature")
public class Nature implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)		//主键id
	private int id;
	
	@Column(name = "content")		//属性内容
	private String content;
	
	@ManyToOne
	@JoinColumn(name = "attribute_id")		//属性id
	private Attribute attributeId;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Attribute getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(Attribute attributeId) {
		this.attributeId = attributeId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
