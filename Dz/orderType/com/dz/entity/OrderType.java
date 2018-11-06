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
import javax.persistence.OneToOne;
import javax.persistence.Table;

//外卖订单类型
@Entity
@Table(name = "dz_orderType")
public class OrderType implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)		//主键id
	private int id;
	
	@OneToOne
	@JoinColumn(name = "order_id")		//订单id
	private Order orderId;
	
	@Column(name = "status")		//配送状态    paySuccess 支付成功
	private String status;
	
	@ManyToOne
	@JoinColumn(name="user_id")//配送员id
	private User userId;
	
	@ManyToOne
	@JoinColumn(name="address_id")//收货地址id
	private Address addressId;

	@Column(name = "shipping_time")		//送达时间
	private String shippingTime;
	
	@Column(name = "receipt_time")		//接单时间
	private String receiptTime;
	
	@Column(name = "way")		//配送方式
	private String way;
	
	@Column(name = "quantity")		//餐具数量
	private Integer quantity;
	
	@Column(name = "box_price")		//餐具单价
	private Double boxPrice;
	
	@Column(name = "price")		//配送价格
	private String price;
	
	@Column(name = "code")		//取货码
	private String code;
	
	@Column(name = "type")		//地址类型    company为配送上门     self为到店自取
	private String type;
	
	@Column(name = "is_reminder")		//是否催单
	private boolean isReminder;
	

	public String getReceiptTime() {
		return receiptTime;
	}

	public void setReceiptTime(String receiptTime) {
		this.receiptTime = receiptTime;
	}

	public Double getBoxPrice() {
		return boxPrice;
	}

	public void setBoxPrice(Double boxPrice) {
		this.boxPrice = boxPrice;
	}

	public boolean getIsReminder() {
		return isReminder;
	}

	public void setIsReminder(boolean isReminder) {
		this.isReminder = isReminder;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getShippingTime() {
		return shippingTime;
	}

	public void setShippingTime(String shippingTime) {
		this.shippingTime = shippingTime;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public Address getAddressId() {
		return addressId;
	}

	public void setAddressId(Address addressId) {
		this.addressId = addressId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
