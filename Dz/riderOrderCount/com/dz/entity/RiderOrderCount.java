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

//骑手订单统计实体类
@Entity
@Table(name = "dz_riderOrderCount")
public class RiderOrderCount implements Serializable{

	private static final long serialVersionUID = 1L;	
	
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)		//主键id
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "run_id")		//骑手id
	private User userId;
	
	@Column(name = "date")		//日期
	private String date;
	
	@Column(name = "balance")		//交易金额
	private String balance;
	
	@Column(name = "order_total")		//订单配送总量
	private String order_total;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getOrder_total() {
		return order_total;
	}

	public void setOrder_total(String orderTotal) {
		order_total = orderTotal;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
