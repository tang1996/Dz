package com.dz.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//美食预定订单属性
@Entity
@Table(name = "dz_reserve")
public class Reserve implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false) // 主键id
	private int id;

	@Column(name = "name") // 桌位名称
	private String name;

	@Column(name = "seat") // 桌位区域
	private String seat;
	
	@Column(name = "table_no") // 桌号
	private String tableNo;

	@Column(name = "status") // 桌位状态   0可预定   1 已预订   3 就餐中
	private String status;
	
	@Column(name = "tyoe")// 餐桌类型   1 圆桌   2 方桌   3 长方桌
	private String tyoe;

	@Column(name = "meals") // 可容纳人数
	private String meals;
	
	@Column(name = "img") // 餐桌图片
	private String img;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "reserve_id")
	private List<BookTime> bookTime; // 已定时间一对多关联

	@Column(name = "is_open") // 是否开桌
	private boolean isOpen;
	
	@Column(name = "deposit")		//预定定金
	private String deposit;
	
	@Column(name = "cancel_time")		//允许退订时间
	private String cancelTime;
	
	@Column(name = "is_delete")		//是否删除
	private String isDelete;
	
	@ManyToOne
	@JoinColumn(name = "company_id") // 商家id
	private Company companyId;

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getTableNo() {
		return tableNo;
	}

	public void setTableNo(String tableNo) {
		this.tableNo = tableNo;
	}

	public String getTyoe() {
		return tyoe;
	}

	public void setTyoe(String tyoe) {
		this.tyoe = tyoe;
	}

	public String getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(String cancelTime) {
		this.cancelTime = cancelTime;
	}

	public String getDeposit() {
		return deposit;
	}

	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public boolean getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public List<BookTime> getBookTime() {
		return bookTime;
	}

	public void setBookTime(List<BookTime> bookTime) {
		this.bookTime = bookTime;
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

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMeals() {
		return meals;
	}

	public void setMeals(String meals) {
		this.meals = meals;
	}

	public Company getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Company companyId) {
		this.companyId = companyId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
