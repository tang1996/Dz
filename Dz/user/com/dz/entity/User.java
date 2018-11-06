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

/**
 * 用户表
 */
@Entity
@Table(name = "dz_user")
public class User implements Serializable {
	
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
	
	@Column(name = "img_url")		//头像url
	private String imgUrl;
	
	@Column(name = "name")		//姓名
	private String name;
	
	@Column(name = "nickname")		//昵称
	private String nickname;
	
	@Column(name = "token")		//用户token
	private String token;
	
	@Column(name = "phone")		//联系方式
	private String phone;
	
	@Column(name = "card_front")		//身份证正面url
	private String cardFront;
	
	@Column(name = "card_back")		//身份证反面url
	private String cardBack;
	
	@Column(name = "create_time")		//申请时间
	private String createTime;
	
	@Column(name = "update_time")		//审核时间
	private String updateTime;
	
	@Column(name = "auditor")		//审核人
	private String auditor;
	
	@Column(name = "ustatus")		//账户状态
	private String status;
	
	@Column(name = "age")		//年龄
	private String age;
	
	@Column(name = "company")		//工作单位
	private String company;
	
	@Column(name = "position")		//职务
	private String position;
	
	@Column(name = "hobby")		//爱好
	private String hobby;
	
	@Column(name = "sign")		//个性签名
	private String sign;
	
	@Column(name = "career")		//从事职业
	private String career;
	
	@Column(name = "Rcloud_token")		//融云Token
	private String RcloudToken;
	
	@Column(name = "is_business")		//是否接单
	private boolean isBusiness;
	
	@Column(name = "companyId")		//商家id
	private int companyId;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private List<Address> address; //地址一对多关联
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private List<Tavern> tavern; //地址一对多关联
	
	@OneToOne
	@JoinColumn(name = "shop_id")		//订单id
	private Shop shop;//购物车一对一关联
	
	@Column(name = "is_distribution")		//是否是跑腿员
	private boolean isDistribution;
	
	@Column(name = "is_newuser")		//是否是新用户
	private boolean isNewuser;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "evaluate_id")
	private List<Evaluate> evaluate; //评价一对多关联
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private List<Order> order; //订单一对多关联
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "include_id")
	private List<Include> include; //收藏一对多关联
	
	@Column(name = "boss_id")		//上级id
	private int bossId;
	
	public int getBossId() {
		return bossId;
	}

	public void setBossId(int bossId) {
		this.bossId = bossId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public boolean getIsBusiness() {
		return isBusiness;
	}

	public void setIsBusiness(boolean isBusiness) {
		this.isBusiness = isBusiness;
	}
	
	public String getRcloudToken() {
		return RcloudToken;
	}

	public void setRcloudToken(String rcloudToken) {
		RcloudToken = rcloudToken;
	}

	public List<Tavern> getTavern() {
		return tavern;
	}

	public void setTavern(List<Tavern> tavern) {
		this.tavern = tavern;
	}

	public List<Include> getInclude() {
		return include;
	}

	public void setInclude(List<Include> include) {
		this.include = include;
	}

	public List<Order> getOrder() {
		return order;
	}

	public void setOrder(List<Order> order) {
		this.order = order;
	}

	public List<Address> getAddress() {
		return address;
	}

	public void setAddress(List<Address> address) {
		this.address = address;
	}

	public List<Evaluate> getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(List<Evaluate> evaluate) {
		this.evaluate = evaluate;
	}

	@ManyToOne
	public int getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRandomCode() {
		return randomCode;
	}

	public void setRandomCode(String randomCode) {
		this.randomCode = randomCode;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public void setDistribution(boolean isDistribution) {
		this.isDistribution = isDistribution;
	}

	public void setNewuser(boolean isNewuser) {
		this.isNewuser = isNewuser;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCardFront() {
		return cardFront;
	}

	public void setCardFront(String cardFront) {
		this.cardFront = cardFront;
	}

	public String getCardBack() {
		return cardBack;
	}

	public void setCardBack(String cardBack) {
		this.cardBack = cardBack;
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

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getCareer() {
		return career;
	}

	public void setCareer(String career) {
		this.career = career;
	}

	public boolean getIsDistribution() {
		return isDistribution;
	}

	public void setIsDistribution(boolean isDistribution) {
		this.isDistribution = isDistribution;
	}

	public boolean getIsNewuser() {
		return isNewuser;
	}

	public void setIsNewuser(boolean isNewuser) {
		this.isNewuser = isNewuser;
	}

}
