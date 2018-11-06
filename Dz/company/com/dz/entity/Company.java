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

@Entity
@Table(name = "dz_company")
public class Company implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false) // 主键id
	private int id;

	@Column(name = "logo") // logo
	private String logo;

	@Column(name = "name") // 店名
	private String name;
	
	@Column(name = "monSales") // 月销量
	private String monSales;

	@Column(name = "phone") // 联系方式
	private String phone;

	@Column(name = "position") // 地理位置
	private String position;

	@Column(name = "coordinates") // 地理坐标
	private String coordinates;

	@Column(name = "business_time_start") // 开始营业时间
	private String businessTimeStart;
	
	@Column(name = "business_time_end") // 结束营业时间
	private String businessTimeEnd;

	@Column(name = "honor") // 营业资质
	private String honor;

	@Column(name = "notice") // 公告
	private String notice;

	@Column(name = "info") // 消费方式
	private String info;
	
	@Column(name = "receipt") // 自动接单
	private String receipt;

	@Column(name = "assess") // 评分 
	private String assess;

	@Column(name = "is_business") // 是否营业
	private boolean isBusiness;
	
	@Column(name = "unsubscribe_time") // 餐桌退订时间
	private String unsubscribe_time;

//	@ManyToOne
//	@JoinColumn(name = "classify_id") // 商家类型id
//	private Classify classifyId;
	
	@Column(name = "classify_id") // 商家类型id
	private String classifyId;
	
	@ManyToOne
	@JoinColumn(name = "salesman_id") // 业务员id
	private Salesman salesman;

	// @Column(name = "url") //商家连接
	// private String url;

	@Column(name = "is_open") // 是否开店
	private boolean isOpen;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private List<Goods> goods; //商品一对多关联
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private List<Order> order; //订单一对多关联
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private List<CompanyScore> companyScore; //商家评价一对多关联
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private List<Ification> ification; //商品分类一对多关联
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private List<Include> include; //收藏一对多关联
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private List<Reserve> reserve; //美食预定一对多关联
	
	@Column(name = "audit") // 审核状态
	private int audit;
	
	@Column(name = "img") // 门头图片
	private String img;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private List<CompanyActivity> companyActivity; //商品评价一对多关联
	
	@Column(name = "open_id") // 商家收银
	private String openId;
	
	@Column(name = "is_video") // 是否关闭摄像头
	private String isVideo;
	
	@Column(name = "camer_user") // 视频用户名
	private String camerUser;
	
	@Column(name = "camer_pass") // 视频密码
	private String camerPass;
	
	@Column(name = "print") // 前台打印机名称
	private String print;
	
	@Column(name = "type") // 类型子分类	1西餐, 2火锅, 3饮品, 
	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPrint() {
		return print;
	}

	public void setPrint(String print) {
		this.print = print;
	}

	public String getIsVideo() {
		return isVideo;
	}

	public void setIsVideo(String isVideo) {
		this.isVideo = isVideo;
	}

	public String getCamerUser() {
		return camerUser;
	}

	public void setCamerUser(String camerUser) {
		this.camerUser = camerUser;
	}

	public String getCamerPass() {
		return camerPass;
	}

	public void setCamerPass(String camerPass) {
		this.camerPass = camerPass;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public Salesman getSalesman() {
		return salesman;
	}

	public void setSalesman(Salesman salesman) {
		this.salesman = salesman;
	}

	public List<CompanyActivity> getCompanyActivity() {
		return companyActivity;
	}

	public void setCompanyActivity(List<CompanyActivity> companyActivity) {
		this.companyActivity = companyActivity;
	}

	public List<Reserve> getReserve() {
		return reserve;
	}

	public void setReserve(List<Reserve> reserve) {
		this.reserve = reserve;
	}

	public String getMonSales() {
		return monSales;
	}

	public void setMonSales(String monSales) {
		this.monSales = monSales;
	}

	public List<Include> getInclude() {
		return include;
	}

	public void setInclude(List<Include> include) {
		this.include = include;
	}

	public List<Ification> getIfication() {
		return ification;
	}

	public void setIfication(List<Ification> ification) {
		this.ification = ification;
	}

	public List<CompanyScore> getCompanyScore() {
		return companyScore;
	}

	public void setCompanyScore(List<CompanyScore> companyScore) {
		this.companyScore = companyScore;
	}

	public String getClassifyId() {
		return classifyId;
	}

	public void setClassifyId(String classifyId) {
		this.classifyId = classifyId;
	}

	public List<Order> getOrder() {
		return order;
	}

	public void setOrder(List<Order> order) {
		this.order = order;
	}

	public int getAudit() {
		return audit;
	}

	public void setAudit(int audit) {
		this.audit = audit;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public boolean getIsBusiness() {
		return isBusiness;
	}

	public void setBusiness(boolean isBusiness) {
		this.isBusiness = isBusiness;
	}

	public boolean getIsOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
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

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}

	public String getBusinessTimeStart() {
		return businessTimeStart;
	}

	public void setBusinessTimeStart(String businessTimeStart) {
		this.businessTimeStart = businessTimeStart;
	}

	public String getBusinessTimeEnd() {
		return businessTimeEnd;
	}

	public void setBusinessTimeEnd(String businessTimeEnd) {
		this.businessTimeEnd = businessTimeEnd;
	}

	public String getHonor() {
		return honor;
	}

	public void setHonor(String honor) {
		this.honor = honor;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getAssess() {
		return assess;
	}

	public void setAssess(String assess) {
		this.assess = assess;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Goods> getGoods() {
		return goods;
	}

	public void setGoods(List<Goods> goods) {
		this.goods = goods;
	}

	public String getUnsubscribe_time() {
		return unsubscribe_time;
	}

	public void setUnsubscribe_time(String unsubscribe_time) {
		this.unsubscribe_time = unsubscribe_time;
	}

	// public String getUrl() {
	// return url;
	// }
	//
	// public void setUrl(String url) {
	// this.url = url;
	// }

}
