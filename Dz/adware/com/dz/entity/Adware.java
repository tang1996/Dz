package com.dz.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//广告实体类
@Entity
@Table(name = "dz_adware")
public class Adware implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)		//主键id
	private int id;
	
	@Column(name = "name")		//广告位名称
	private String name;
	
	@Column(name = "depict")		//广告位描述
	private String depict;
	
	@Column(name = "type")		//广告代号
	private String type;
	
	@Column(name = "region")		//广告地址区域
	private String region;
	
	@Column(name = "source")		//页面来源
	private String source;

	@Column(name = "seat")		//广告位置
	private String  seat;
	
	@Column(name = "click_rate")		//点击率
	private String clickRate;
	
	@Column(name = "url")		//url地址
	private String url;
	
	@Column(name = "create_time")		//添加广告时间
	private String createTime;
	
	@Column(name = "start_time")		//广告开始时间
	private String startTime;
	
	@Column(name = "end_time")		//广告结束时间
	private String endTime;
	
	@Column(name = "contacts")		//广告联系人
	private String contacts;
	
	@Column(name = "e_mail")		//联系人邮箱
	private String Email;
	
	@Column(name = "phone")		//联系人电话
	private String phone;
	
	@Column(name = "is_close")		//该广告是否关闭  1  开启   0  关闭
	private boolean isClose;

	public boolean getIsClose() {
		return isClose;
	}
	
	public void setIsClose(boolean isClose) {
		this.isClose = isClose;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDepict() {
		return depict;
	}

	public void setDepict(String depict) {
		this.depict = depict;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	public String getClickRate() {
		return clickRate;
	}

	public void setClickRate(String clickRate) {
		this.clickRate = clickRate;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
