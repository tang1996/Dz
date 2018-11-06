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

/**
 * 用户表
 */
@Entity
@Table(name = "dz_riderInfo")
public class RiderInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)		//主键id
	private int id;
	
	@Column(name = "account_opening")		//开户人
	private String accountOpening;
	
	@Column(name = "bank_card")		//银行卡号
	private String bankCard;
	
	@Column(name = "bank_type")		//银行类型
	private String bankType;
	
	@Column(name = "account_bank")		//开户支行
	private String accountBank;
	
	@Column(name = "card_no")		//身份证号
	private String cardNo;
	
	@Column(name = "education")		//学历
	private String education;
	
	@Column(name = "city")		//城市
	private String city;
	
	@Column(name = "sex")		//性别
	private String sex;
	
	@OneToOne
    @JoinColumn(name = "user_id")  
    private User userId; 
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccountOpening() {
		return accountOpening;
	}

	public void setAccountOpening(String accountOpening) {
		this.accountOpening = accountOpening;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getAccountBank() {
		return accountBank;
	}

	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
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
