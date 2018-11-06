package com.dz.entity;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//商品实体类
@Entity
@Table(name = "dz_goods")
public class Goods implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)		//主键id
	private int id;
	
	@Column(name = "code")		//商品编号
	private String code;
	
	@Column(name = "name")		//商品名称
	private String name;
	
	@Column(name = "price")		//价格
	private String price;
	
	@Column(name = "svg_price")		//促销价格
	private String svgPrice;
	
	@Column(name = "start_time")		//促销开始时间
	private String startTime;
	
	@Column(name = "end_time")		//促销结束时间
	private String endTime;

	@Column(name = "unit")		//商品单位
	private String  unit;
	
	@Column(name = "ksy_word")		//关键字
	private String ksyWord;
	
	@Column(name = "is_activity")		//是否参加活动
	private boolean isActivity;
	
	@Column(name = "details")		//详情
	private String details;
	
	@Column(name = "brief")		//简介
	private String brief;
	
	@Column(name = "zoom_url")		//缩略图url
	private String zoomUrl;
	
	@Column(name = "original_url")		//原图url
	private String originalUrl;
	
	@Column(name = "create_time")		//商品添加时间
	private String createTime;
	
	@Column(name = "stock")		//商品库存
	private String stock;
	
	@Column(name = "boxPrice")		//包装费
	private String boxPrice;
	
	@Column(name = "recommend")		//是否推荐；0，否；1，是
	private boolean  recommend;
	
	@ManyToOne
	@JoinColumn(name="company_id")//商家id
	private Company companyId;

	@Column(name = "click")		//点击数
	private int click;
	
	@Column(name = "mon_sales")		//月销售量
	private int mon_sales;
	
	@Column(name = "total_sales")		//总销售量
	private int total_sales;
	
	@Column(name = "classIf")		//商家分类
	private String classIf;
	
	@Column(name = "print_name")		//打印机名称
	private String printName;
	
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "dz_shop_goods",joinColumns = @JoinColumn(name = "shop_id"),inverseJoinColumns = @JoinColumn(name = "goods_id"))
    private List<Shop> shops= new ArrayList<Shop>();  //购物车多对多关联
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "dz_order_goods",joinColumns = @JoinColumn(name = "goods_id"),inverseJoinColumns = @JoinColumn(name = "order_id"))
    private List<Order> orders= new ArrayList<Order>();  //订单多对多关联
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "goods_id")
	private List<Attribute> attribute; //外卖商品属性一对多关联
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "goods_id")
	private List<Stay> stay; //酒店商品属性一对多关联
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "goods_id")
	private List<Box> box; //KTV商品一对多关联
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "goods_id")
	private List<GoodsActivity> goodsActivity; //商品活动一对多关联
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "goods_id")
	private List<Parcel> parcel; //商品评价一对多关联
	
	@ManyToOne
	@JoinColumn(name = "ification_id")		//分类id
	private Ification ificationId;
	
	@Column(name = "shelves")		//是否上架；0，否；1，是
	private boolean shelves;
	
	@Column(name = "fine")		//是否精品；0，否；1，是
	private boolean fine;
	
	@Column(name = "is_new")		//是否新品；0，否；1，是
	private boolean isNew;
	
	@Column(name = "Selling")		//是否热销；0，否；1，是
	private boolean Selling;
	
	@Column(name = "is_svg")		//是否促销；0，否；1，是
	private boolean isSvg;
	
	@Column(name = "is_delete")		//是否删除；0，否；1，是
	private boolean isDelete;
	
	public boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public String getBoxPrice() {
		return boxPrice;
	}

	public void setBoxPrice(String boxPrice) {
		this.boxPrice = boxPrice;
	}

	public String getClassIf() {
		return classIf;
	}

	public void setClassIf(String classIf) {
		this.classIf = classIf;
	}

	public String getPrintName() {
		return printName;
	}

	public void setPrintName(String printName) {
		this.printName = printName;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public List<Box> getBox() {
		return box;
	}

	public void setBox(List<Box> box) {
		this.box = box;
	}

	public List<Stay> getStay() {
		return stay;
	}

	public void setStay(List<Stay> stay) {
		this.stay = stay;
	}

	public List<GoodsActivity> getGoodsActivity() {
		return goodsActivity;
	}

	public void setGoodsActivity(List<GoodsActivity> goodsActivity) {
		this.goodsActivity = goodsActivity;
	}

	public List<Parcel> getParcel() {
		return parcel;
	}

	public void setParcel(List<Parcel> parcel) {
		this.parcel = parcel;
	}

	public List<Attribute> getAttribute() {
		return attribute;
	}

	public void setAttribute(List<Attribute> attribute) {
		this.attribute = attribute;
	}

	public List<Shop> getShops() {
		return shops;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public void setShops(List<Shop> shops) {
		this.shops = shops;
	}

	public void setActivity(boolean isActivity) {
		this.isActivity = isActivity;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public void setSvg(boolean isSvg) {
		this.isSvg = isSvg;
	}

	public boolean getIsActivity() {
		return isActivity;
	}

	public void setIsActivity(boolean isActivity) {
		this.isActivity = isActivity;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSvgPrice() {
		return svgPrice;
	}

	public void setSvgPrice(String svgPrice) {
		this.svgPrice = svgPrice;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	
	public int getClick() {
		return click;
	}

	public void setClick(int click) {
		this.click = click;
	}

	public int getMon_sales() {
		return mon_sales;
	}

	public void setMon_sales(int mon_sales) {
		this.mon_sales = mon_sales;
	}

	public int getTotal_sales() {
		return total_sales;
	}

	public void setTotal_sales(int total_sales) {
		this.total_sales = total_sales;
	}

	public String getKsyWord() {
		return ksyWord;
	}

	public void setKsyWord(String ksyWord) {
		this.ksyWord = ksyWord;
	}
    
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getZoomUrl() {
		return zoomUrl;
	}

	public void setZoomUrl(String zoomUrl) {
		this.zoomUrl = zoomUrl;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	

	public boolean getRecommend() {
		return recommend;
	}

	public void setRecommend(boolean recommend) {
		this.recommend = recommend;
	}

	public Company getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Company companyId) {
		this.companyId = companyId;
	}

	public Ification getIficationId() {
		return ificationId;
	}

	public void setIficationId(Ification ificationId) {
		this.ificationId = ificationId;
	}

	public boolean getShelves() {
		return shelves;
	}

	public void setShelves(boolean shelves) {
		this.shelves = shelves;
	}

	public boolean getFine() {
		return fine;
	}

	public void setFine(boolean fine) {
		this.fine = fine;
	}

	public boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}

	public boolean getSelling() {
		return Selling;
	}

	public void setSelling(boolean selling) {
		Selling = selling;
	}

	public boolean getIsSvg() {
		return isSvg;
	}

	public void setIsSvg(boolean isSvg) {
		this.isSvg = isSvg;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
