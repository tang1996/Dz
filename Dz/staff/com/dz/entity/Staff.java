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

/**
 * 用户表
 */
@Entity
@Table(name = "dz_staff")
public class Staff implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false) // 主键id
	private int id;

	@Column(name = "user_name") // 帐号
	private String userName;

	@Column(name = "password") // 密码
	private String password;

	@Column(name = "random_code") // 随机号
	private String randomCode;

	@Column(name = "name") // 姓名
	private String name;

	@Column(name = "token") // 用户token
	private String token;

	@Column(name = "phone") // 联系方式
	private String phone;

	@ManyToOne
	@JoinColumn(name = "company_id") // 所属商家
	private Company companyId;

	@ManyToOne
	@JoinColumn(name = "powerSort_id") // 权限等级
	private PowerSort powerSortId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRandomCode() {
		return randomCode;
	}

	public void setRandomCode(String randomCode) {
		this.randomCode = randomCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Company getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Company companyId) {
		this.companyId = companyId;
	}

	public PowerSort getPowerSortId() {
		return powerSortId;
	}

	public void setPowerSortId(PowerSort powerSortId) {
		this.powerSortId = powerSortId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
