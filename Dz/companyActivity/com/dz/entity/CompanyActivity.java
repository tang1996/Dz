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

//商家活动
@Entity
@Table(name = "dz_companyActivity")
public class CompanyActivity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)		//主键id
	private int id;
	
	@ManyToOne
	@JoinColumn(name="company_id")		//商家id
	private Company companyId;
	
	@ManyToOne
	@JoinColumn(name="activity_id")		//活动id
	private Activity activityId;
	
	@Column(name = "depict")		//活动描述
	private String depict;
	
	@Column(name = "is_open")		//是否开启
	private boolean isOpen;
	
	@Column(name = "newUser")		//新人优惠
	private String newUser;
	
	@Column(name = "coupon")		//优惠券
	private String coupon;
	
	@Column(name = "content")		//活动内容
	private String content;
	
	@Column(name = "balance")		//活动起步金额,满减金额
	private String balance;
	
	@Column(name = "benefit")		//优惠活动金额
	private String benefit;
	
	@Column(name = "svg")		//折扣
	private String svg;
	
	@Column(name = "start_time")		//活动的开始时间
	private String startTime;
	
	@Column(name = "end_time")		//活动的结束时间
	private String endTime;
	
	@Column(name = "type")		//活动类型
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public String getNewUser() {
		return newUser;
	}

	public void setNewUser(String newUser) {
		this.newUser = newUser;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Activity getActivityId() {
		return activityId;
	}

	public void setActivityId(Activity activityId) {
		this.activityId = activityId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	public String getDepict() {
		return depict;
	}

	public void setDepict(String depict) {
		this.depict = depict;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getBenefit() {
		return benefit;
	}

	public void setBenefit(String benefit) {
		this.benefit = benefit;
	}

	public String getSvg() {
		return svg;
	}

	public void setSvg(String svg) {
		this.svg = svg;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Company getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Company companyId) {
		this.companyId = companyId;
	}

}
