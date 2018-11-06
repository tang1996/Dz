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

@Entity
@Table(name = "dz_shop_goods")
public class ShopGoods  implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)		//主键id
	private int id;
	
	@ManyToOne
	@JoinColumn(name="shop_id")//商家id
	private Shop shop;
	
	@Column(name = "c_id")	
	private Integer cId;
	
	@Column(name = "ngoods_id")	
	private Integer ngoodsId;

	@Column(name = "numb")	
	private Integer numb;
	
	@Column(name = "natrue")	
	private String natrue;
	
	public String getNatrue() {
		return natrue;
	}

	public void setNatrue(String natrue) {
		this.natrue = natrue;
	}

	public Integer getcId() {
		return cId;
	}

	public void setcId(Integer cId) {
		this.cId = cId;
	}

	public Integer getNgoodsId() {
		return ngoodsId;
	}

	public void setNgoodsId(Integer ngoodsId) {
		this.ngoodsId = ngoodsId;
	}

	public Integer getNumb() {
		return numb;
	}

	public void setNumb(Integer numb) {
		this.numb = numb;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
