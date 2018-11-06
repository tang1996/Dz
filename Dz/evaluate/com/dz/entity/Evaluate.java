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
import javax.persistence.OneToOne;
import javax.persistence.Table;

//评价实体类
@Entity
@Table(name = "dz_evaluate")
public class Evaluate implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	// 主键id
	private int id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	// 用户id
	private User userId;

	@ManyToOne
	@JoinColumn(name = "goods_id")
	// 用户id
	private Goods goodsId;

	@Column(name = "content")
	// 评价内容
	private String content;

	@Column(name = "type")
	// 场景
	private String type;

	@Column(name = "custom_id")
	// 自定义id
	private int customId;

	@Column(name = "title")
	// 标题
	private String title;

	@Column(name = "reply")
	// 回复内容
	private String reply;

	@Column(name = "is_reply")
	// 是否回复
	private boolean isReply;

	@Column(name = "create_time")
	// 评价时间
	private String createTime;

	@Column(name = "update_time")
	// 回复时间
	private String updateTime;

	@Column(name = "label")
	// 标签内容
	private String label;

	// 是否显示
	@Column(name = "is_display")
	private boolean isDisplay;

	// 评价图片
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "images_id")
	private List<Images> img;
	
	@OneToOne
	@JoinColumn(name = "order_id")
	// 订单id
	private Order orderId;

	public Goods getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Goods goodsId) {
		this.goodsId = goodsId;
	}

	public boolean getIsReply() {
		return isReply;
	}

	public void setIsReply(boolean isReply) {
		this.isReply = isReply;
	}

	public List<Images> getImg() {
		return img;
	}

	public void setImg(List<Images> img) {
		this.img = img;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCustomId() {
		return customId;
	}

	public void setCustomId(int customId) {
		this.customId = customId;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Order getOrderId() {
		return orderId;
	}

	public void setOrderId(Order orderId) {
		this.orderId = orderId;
	}

	public boolean getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(boolean isDisplay) {
		this.isDisplay = isDisplay;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
