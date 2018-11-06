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

//酒店商品属性
@Entity
@Table(name = "dz_stay")
public class Stay implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false) // 主键id
	private int id;

	@Column(name = "is_reserve") // 是否预定
	private boolean isReserve;
	
	@Column(name = "level") // 豪华等级
	private String level;
	
	@Column(name = "type") // 房间类型
	private String type;
	
	@Column(name = "capacity") // 可容纳人数
	private String capacity;
	
	@Column(name = "cancel_time") // 可取消时间
	private String cancelTime;

	@ManyToOne
	@JoinColumn(name = "goods_id") // 商品id
	private Goods goodsId;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "stay_id")
	private List<Tavern> tavern; //地址一对多关联

	public String getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(String cancelTime) {
		this.cancelTime = cancelTime;
	}

	public List<Tavern> getTavern() {
		return tavern;
	}

	public void setTavern(List<Tavern> tavern) {
		this.tavern = tavern;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean getIsReserve() {
		return isReserve;
	}

	public void setIsReserve(boolean isReserve) {
		this.isReserve = isReserve;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public Goods getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Goods goodsId) {
		this.goodsId = goodsId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
