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

//酒店预订
@Entity
@Table(name = "dz_tavern")
public class Tavern implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false) // 主键id
	private int id;

	@Column(name = "start_time") // 入住时间
	private String startTime;

	@Column(name = "end_time") // 离店时间
	private String endTime;

	@ManyToOne
	@JoinColumn(name = "user_id") // 用户id
	private User userId;

	@ManyToOne
	@JoinColumn(name = "stay_id") // 商家id
	private Stay stayId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public Stay getStayId() {
		return stayId;
	}

	public void setStayId(Stay stayId) {
		this.stayId = stayId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
