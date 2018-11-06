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
@Table(name = "dz_job")
public class Job implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)		//主键id
	private int id;
	
	@Column(name = "num")		//商品数量
	private String num;
	
	@Column(name = "goods_id")		//商品id
	private String goodsId;
	
	@Column(name = "is_print")		//是否已打印
	private String isPrint;
	
	@Column(name = "is_printBase")		//厨房是否已打印
	private String isPrintBase;
	
	@Column(name = "companyId")		//商家id
	private String companyId;
	
	@ManyToOne
	@JoinColumn(name="order_id")//订单id
	private Order orderId;
	
	@Column(name = "print_name")// 打印机名称
	private String printName;
	
	@Column(name = "goods_name")// 商品名称
	private String goodsName;
	
	@Column(name = "price")// 商品价格
	private String price;

	@Column(name = "remark")// 备注信息
	private String remark;
	
	@ManyToOne
	@JoinColumn(name="insideOrder_id")//订单id
	private InsideOrder InsideOrderId;
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getIsPrintBase() {
		return isPrintBase;
	}

	public void setIsPrintBase(String isPrintBase) {
		this.isPrintBase = isPrintBase;
	}

	public InsideOrder getInsideOrderId() {
		return InsideOrderId;
	}

	public void setInsideOrderId(InsideOrder insideOrderId) {
		InsideOrderId = insideOrderId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPrintName() {
		return printName;
	}

	public void setPrintName(String printName) {
		this.printName = printName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getIsPrint() {
		return isPrint;
	}

	public void setIsPrint(String isPrint) {
		this.isPrint = isPrint;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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
