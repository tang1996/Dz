package com.dz.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

//用户钱包实体类
@Entity
@Table(name = "dz_wallet")
public class Wallet implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)		//主键id
	private int id;
	
	@Column(name = "balance")		//余额
	private String balance;
	
	@Column(name = "red_balance")		//红包余额
	private String redBalance;
	
	@Column(name = "free_times")		//剩余免费跑腿次数
	private String freeTimes;
	
	@Column(name = "password")		//交易密码
	private String password;
	
	@Column(name = "locking_time")		//锁定时间
	private String lockingTime;
	
	@Column(name = "error_times")		//密码错误次数
	private String errorTimes;
	
	@Column(name = "wstatus")		//钱包状态
	private String status;

	@OneToOne
    @JoinColumn(name = "user_id")  
    private User userId; 
	
	public String getRedBalance() {
		return redBalance;
	}

	public void setRedBalance(String redBalance) {
		this.redBalance = redBalance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getFreeTimes() {
		return freeTimes;
	}

	public void setFreeTimes(String freeTimes) {
		this.freeTimes = freeTimes;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLockingTime() {
		return lockingTime;
	}

	public void setLockingTime(String lockingTime) {
		this.lockingTime = lockingTime;
	}

	public String getErrorTimes() {
		return errorTimes;
	}

	public void setErrorTimes(String errorTimes) {
		this.errorTimes = errorTimes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
