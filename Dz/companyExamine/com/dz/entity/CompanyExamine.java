package com.dz.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户表
 */
@Entity
@Table(name = "dz_companyExamine")
public class CompanyExamine implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	// 主键id
	private int id;

	@Column(name = "name")
	// 姓名
	private String name;

	@Column(name = "phone")
	// 联系方式
	private String phone;

	@Column(name = "address")
	// 身份证号
	private String address;

	@Column(name = "create_time")
	// 申请时间
	private String createTime;

	@Column(name = "card")
	// 身份证正反面url
	private String card;

	@Column(name = "take_card")
	// 手持身份证照片url
	private String takeCard;

	@Column(name = "health_card")
	// 卫生许可证url
	private String healthCard;

	@Column(name = "charter")
	// 营业执照url
	private String charter;

	@Column(name = "shop_phone")
	// 商家手机
	private String shopPhone;
	
	@Column(name = "location")
	// 商家坐标
	private String location;
	
	@Column(name = "salerId")
	// 推广员id
	private int salerId;
	
	@Column(name = "main")		//主营业务
	private String main;
	
	@Column(name = "deputy")		//副营业务
	private String deputy;
	
	public String getMain() {
		return main;
	}

	public void setMain(String main) {
		this.main = main;
	}

	public String getDeputy() {
		return deputy;
	}

	public void setDeputy(String deputy) {
		this.deputy = deputy;
	}

	public int getSalerId() {
		return salerId;
	}

	public void setSalerId(int salerId) {
		this.salerId = salerId;
	}

	public String getShopPhone() {
		return shopPhone;
	}

	public void setShopPhone(String shopPhone) {
		this.shopPhone = shopPhone;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getTakeCard() {
		return takeCard;
	}

	public void setTakeCard(String takeCard) {
		this.takeCard = takeCard;
	}

	public String getHealthCard() {
		return healthCard;
	}

	public void setHealthCard(String healthCard) {
		this.healthCard = healthCard;
	}

	public String getCharter() {
		return charter;
	}

	public void setCharter(String charter) {
		this.charter = charter;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
