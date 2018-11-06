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

//美食订单类型

@Entity
@Table(name = "dz_cate")
public class Cate implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)		//主键id
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "company_id") // 商家id
	private Company companyId;
	
	@Column(name = "tableNo")		//桌号
	private String tableNo;
	
	@Column(name = "create_time")		//预定时间
	private String createTime;
	
	@Column(name = "end_time")		//到店消费时间
	private String endTime;
	
	@Column(name = "seat")		//餐桌位置
	private String seat;
	
	@Column(name = "name")		//餐桌名称
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "user_id") // 用户id
	private User userId;
	
	@OneToOne
	@JoinColumn(name = "order_id") // 订单id
	private Order orderId;
	
	@OneToOne
	@JoinColumn(name = "insideOrder_id") // 线下订单id
	private InsideOrder insideOrderId;
	
	@Column(name = "meals") // 用餐人数
	private String meals;
	
	@Column(name = "meal_fee") // 餐位费
	private String mealFee;
	
	@Column(name = "price") // 金额 
	private String price;
	
	@Column(name = "phone") // 联系方式
	private String phone;
	
	@Column(name = "reserve_id") // 桌子id 
	private String reserveId;
	
	@Column(name = "recode") // 收银随机码
	private String recode;
	
	public InsideOrder getInsideOrderId() {
		return insideOrderId;
	}

	public void setInsideOrderId(InsideOrder insideOrderId) {
		this.insideOrderId = insideOrderId;
	}

	public String getRecode() {
		return recode;
	}

	public void setRecode(String recode) {
		this.recode = recode;
	}

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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMeals() {
		return meals;
	}

	public void setMeals(String meals) {
		this.meals = meals;
	}

	public Order getOrderId() {
		return orderId;
	}

	public void setOrderId(Order orderId) {
		this.orderId = orderId;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
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
