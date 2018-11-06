package com.dz.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//总活动实体类
@Entity
@Table(name = "dz_activity")
public class Activity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)		//主键id
	private int id;
	
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "activity_id")
	private List<GoodsActivity> goodsActivity; //商品id一对多关联
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  	@JoinColumn(name = "activity_id")
  	private List<CompanyActivity> companyActivity; //商品id一对多关联
	
	@Column(name = "name")		//活动名称  
	private String name;
	
	@Column(name = "bewrite")		//活动描述
	private String bewrite;
	
	@Column(name = "substance")		//活动内容
	private String substance;
	
	@Column(name = "scope")		//活动范围
	private String scope;
	
	@Column(name = "logo")		//图标
	private String logo;
	
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public List<GoodsActivity> getGoodsActivity() {
		return goodsActivity;
	}

	public void setGoodsActivity(List<GoodsActivity> goodsActivity) {
		this.goodsActivity = goodsActivity;
	}

	public List<CompanyActivity> getCompanyActivity() {
		return companyActivity;
	}

	public void setCompanyActivity(List<CompanyActivity> companyActivity) {
		this.companyActivity = companyActivity;
	}

	public String getBewrite() {
		return bewrite;
	}

	public void setBewrite(String bewrite) {
		this.bewrite = bewrite;
	}

	public String getSubstance() {
		return substance;
	}

	public void setSubstance(String substance) {
		this.substance = substance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
