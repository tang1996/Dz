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

//订单属性
@Entity
@Table(name = "dz_relating")
public class Relating implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	// 主键id
	private int id;

	@Column(name = "company_id")
	// 商家id
	private Integer companyId;

	@Column(name = "goods_id")
	// 商品id
	private Integer goodsId;
	
	//商品数量
	@Column(name = "numb")
	private int numb;
	
	//虚拟删除
	@Column(name = "is_del")
	private String isDel;
	
	@ManyToOne
	@JoinColumn(name="order_id")//订单id
	private Order orderId;
	
	@ManyToOne
	@JoinColumn(name="insideOrder_id")//线下订单id
	private InsideOrder insideOrderId;
	
	@Column(name = "nature_id")// 商品属性id(多个)
	private String natureId;
	
	@Column(name = "remark")// 商品备注
	private String remark;
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}

	public InsideOrder getInsideOrderId() {
		return insideOrderId;
	}

	public void setInsideOrderId(InsideOrder insideOrderId) {
		this.insideOrderId = insideOrderId;
	}

	public String getNatureId() {
		return natureId;
	}

	public void setNatureId(String natureId) {
		this.natureId = natureId;
	}

	public int getNumb() {
		return numb;
	}

	public void setNumb(int numb) {
		this.numb = numb;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Order getOrderId() {
		return orderId;
	}

	public void setOrderId(Order orderId) {
		this.orderId = orderId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
