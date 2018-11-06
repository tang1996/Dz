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
import javax.persistence.OneToOne;
import javax.persistence.Table;

//美食大厅类型
@Entity
@Table(name = "dz_computerCate")
public class ComputerCate implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	// 主键id
	private int id;

	@ManyToOne
	@JoinColumn(name = "company_id")
	// 商家id
	private Company companyId;

	@Column(name = "tableNo")
	// 桌号
	private String tableNo;

	@Column(name = "seat")
	// 餐桌位置
	private String seat;

	@Column(name = "c_name")
	// 餐桌名称
	private String name;

	@OneToOne
	@JoinColumn(name = "insideOrder_id")
	// 订单id
	private InsideOrder insideorderId;

	@Column(name = "meals")
	// 用餐人数
	private String meals;

	@Column(name = "meal_fee")
	// 餐位费
	private String mealFee;

	@Column(name = "reserve_id")
	// 桌子id
	private String reserveId;

	public String getReserveId() {
		return reserveId;
	}

	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
	}

	public String getMealFee() {
		return mealFee;
	}

	public void setMealFee(String mealFee) {
		this.mealFee = mealFee;
	}

	public String getMeals() {
		return meals;
	}

	public void setMeals(String meals) {
		this.meals = meals;
	}


	public InsideOrder getInsideorderId() {
		return insideorderId;
	}

	public void setInsideorderId(InsideOrder insideorderId) {
		this.insideorderId = insideorderId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Company getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Company companyId) {
		this.companyId = companyId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTableNo() {
		return tableNo;
	}

	public void setTableNo(String tableNo) {
		this.tableNo = tableNo;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

}
