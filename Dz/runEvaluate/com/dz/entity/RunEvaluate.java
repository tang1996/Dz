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

//商品评价实体类
@Entity
@Table(name = "dz_runEvaluate")
public class RunEvaluate implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	// 主键id
	private int id;

	@Column(name = "run_id")
	// 跑腿员id
	private int runId;
	
	@Column(name = "user_id")
	// 用户id
	private int userId;

	@Column(name = "speen")
	// 送餐速度评分
	private String speen;

	@Column(name = "manner")
	// 服务态度
	private String manner;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	// 评分订单
	private Order orderId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRunId() {
		return runId;
	}

	public void setRunId(int runId) {
		this.runId = runId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getManner() {
		return manner;
	}

	public void setManner(String manner) {
		this.manner = manner;
	}

	public Order getOrderId() {
		return orderId;
	}

	public void setOrderId(Order orderId) {
		this.orderId = orderId;
	}

	public String getSpeen() {
		return speen;
	}

	public void setSpeen(String speen) {
		this.speen = speen;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
