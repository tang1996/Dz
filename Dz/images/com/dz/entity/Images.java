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

//图片实体类
@Entity
@Table(name = "dz_images")
public class Images implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)		//主键id
	private int id;
	
	@Column(name = "custom_id")		//自定义id
	private String customId;
	
	@Column(name = "scene")		//场景
	private String scene;
	
	@ManyToOne
	@JoinColumn(name = "images_id")		//图片id
	private Evaluate imagesId;
	
	@Column(name = "original_url")		//原图地址
	private String originalUrl;
	
	@Column(name = "zoom_url")		//缩放图地址
	private String zoomUrl;
	
	@Column(name = "info")		//图片说明信息
	private String info;
	
	@Column(name = "url")		//图片说明信息
	private String url;

	public Evaluate getImagesId() {
		return imagesId;
	}

	public void setImagesId(Evaluate imagesId) {
		this.imagesId = imagesId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCustomId() {
		return customId;
	}

	public void setCustomId(String customId) {
		this.customId = customId;
	}

	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public String getZoomUrl() {
		return zoomUrl;
	}

	public void setZoomUrl(String zoomUrl) {
		this.zoomUrl = zoomUrl;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
