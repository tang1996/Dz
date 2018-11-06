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

//美食桌位预定
@Entity
@Table(name = "dz_bookTime")
public class BookTime implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)		//主键id
	private int id;
	
	@Column(name = "reserve_time")		//预定时间
	private String reserveTime;
	
	@Column(name = "people")		//就餐人数
	private String people;
	
	@Column(name = "is_pay")		//是否交了预付金
	private boolean isPay;
	
	@Column(name = "creat_time")		//创建时间
	private String creatTime;
	
	@Column(name = "user_name")		//预订人
	private String userName;
	
	@Column(name = "user_phone")		//预订人联系方式
	private String userPhone;

	@ManyToOne
    @JoinColumn(name = "reserve_id")  //桌位id
    private Reserve reserveId;
	
	@Column(name = "order_id")		//订单id
	private int orderId;

	@Column(name = "company_id")		//商家id
	private int companyId;

	public int getCompanyId() {
		return companyId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	public String getReserveTime() {
		return reserveTime;
	}

	public void setReserveTime(String reserveTime) {
		this.reserveTime = reserveTime;
	}

	public String getPeople() {
		return people;
	}

	public void setPeople(String people) {
		this.people = people;
	}

	public boolean getIsPay() {
		return isPay;
	}

	public void setIsPay(boolean isPay) {
		this.isPay = isPay;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Reserve getReserveId() {
		return reserveId;
	}

	public void setReserveId(Reserve reserveId) {
		this.reserveId = reserveId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	} 
	
}
