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

//用户订单实体类
@Entity
@Table(name = "dz_insideOrder")
public class InsideOrder implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)		//主键id
	private int id;
	
	@Column(name = "order_no")		//订单编号
	private String orderNo;
	
	@Column(name = "pay_status")		//支付状态（0未付款;1付款中;2已付款.3退款 4.取消）
	private String payStatus;
	
	@Column(name = "custom_id")		//自定义id
	private String customId;
	
	@Column(name = "back_no")		//退款订单号
	private String back_no;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "order_id")
	private List<Relating> relating; // 订单子表id
	
	@Column(name = "is_discount")		//是否优惠
	private boolean isDiscount;
	
	@Column(name = "remarks")		//备注
	private String remarks;
	
	@Column(name = "total")		//总金额
	private String total;
	
	@Column(name = "discount")		//优惠金额
	private String discount;
	
	@Column(name = "sub_sale")		//subfee status    SUBFREE 减免               允许  IS NULL
	private String subSale;
	
	@Column(name = "disacount")		//disacount status    DISACOUNT 折扣          允许  IS NULL
	private String disAcount;
	
	@Column(name = "pay")		//实付金额
	private String pay;
	
	@Column(name = "add_time")		//创建时间
	private String addTime;
	
	@Column(name = "pay_time")		//支付时间
	private String payTime;
	
	@Column(name = "finish_time")		//完成时间
	private String finishTime;
	
	@Column(name = "order_status")		//订单状态
	private String orderStatus;
	
	@Column(name = "order_fail")		//订单失败原因
	private String orderFail;
	
	@Column(name = "is_account")		//金额去向               0  动致科技(为完成)    1商家帐户(已完成)    4用户帐户(退款到帐)
	private String isAccount;
	
	@Column(name = "is_send")		//金额去向               0  动致科技(为完成)    1商家帐户(已完成)    4用户帐户(退款到帐)
	private String isSend;
	
	@ManyToOne
	@JoinColumn(name="company_id")//商家id
	private Company companyId;
	
	public String getIsSend() {
		return isSend;
	}

	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}

	public String getIsAccount() {
		return isAccount;
	}

	public void setIsAccount(String isAccount) {
		this.isAccount = isAccount;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getBack_no() {
		return back_no;
	}

	public void setBack_no(String backNo) {
		back_no = backNo;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getPay() {
		return pay;
	}

	public void setPay(String pay) {
		this.pay = pay;
	}

	public Company getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Company companyId) {
		this.companyId = companyId;
	}

	public String getCustomId() {
		return customId;
	}

	public void setCustomId(String customId) {
		this.customId = customId;
	}

	public List<Relating> getRelating() {
		return relating;
	}

	public void setRelating(List<Relating> relating) {
		this.relating = relating;
	}

	public void setId(int id) {
		this.id = id;
	}


	public int getId() {
		return id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public boolean getIsDiscount() {
		return isDiscount;
	}

	public void setIsDiscount(boolean isDiscount) {
		this.isDiscount = isDiscount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderFail() {
		return orderFail;
	}

	public void setOrderFail(String orderFail) {
		this.orderFail = orderFail;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSubSale() {
		return subSale;
	}

	public void setSubSale(String subSale) {
		this.subSale = subSale;
	}

	public String getDisAcount() {
		return disAcount;
	}

	public void setDisAcount(String disAcount) {
		this.disAcount = disAcount;
	}

	public void setDiscount(boolean isDiscount) {
		this.isDiscount = isDiscount;
	}

}
