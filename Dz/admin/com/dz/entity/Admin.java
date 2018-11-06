package com.dz.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//管理员实体类
@Entity
@Table(name = "dz_admin")
public class Admin implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)		//主键id
	private int id;
	
	@Column(name = "user_name")		//帐号
	private String userName;
	
	@Column(name = "password")		//密码
	private String password;
	
	@Column(name = "random_code")		//随机号
	private String randomCode;
	
	@Column(name = "e_mail")		//邮箱
	private String Email;
	
	@Column(name = "name")		//姓名
	private String name;
	
	@Column(name = "create_time")		//添加时间
	private String createTime;
	
	@Column(name = "last_login")		//最后登录时间
	private String lastLogin;
	
	@Column(name = "last_ip")		//最后登录ip
	private String lastIp;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getLastIp() {
		return lastIp;
	}

	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Admin [id=" + id + ", userName=" + userName + ", password=" + password + ", randomCode=" + randomCode
				+ ", Email=" + Email + ", name=" + name + ", createTime=" + createTime + ", lastLogin=" + lastLogin
				+ ", lastIp=" + lastIp + "]";
	}
	
	
}
