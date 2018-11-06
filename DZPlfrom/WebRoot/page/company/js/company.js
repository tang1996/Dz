Ext.onReady(function() {
	var companyDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.COMPANY_BASEVIEW
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'id',
			successProperty : '@success',
			fields : [ 'id', 'logo', 'name', 'phone', 'position', 'coordinates',
				'business_time', 'honor', 'notice', 'info', 'assess', 'classify_id', 'img', 'is_business', 'isOpen' ]
		}),
	});

	var companyCbsm = new Ext.grid.CheckboxSelectionModel();
	var companyCm = new Ext.grid.ColumnModel([
		companyCbsm, {
			header : 'id',
			dataIndex : 'id',
			locked : true,
			hidden : true,
			width : (document.body.clientWidth - 193) / 18
		}, {
			header : '店名',
			dataIndex : 'name',
			width : (document.body.clientWidth - 193) / 15,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '联系方式',
			dataIndex : 'phone',
			width : (document.body.clientWidth - 193) / 15,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '营业时间',
			dataIndex : 'business_time',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '所属区域',
			dataIndex : 'info',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '评分',
			dataIndex : 'assess',
			width : (document.body.clientWidth - 193) / 25,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '商家分类',
			dataIndex : 'classify_id',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '是否营业',
			dataIndex : 'is_business',
			width : (document.body.clientWidth - 193) / 25,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '店铺状态',
			dataIndex : 'isOpen',
			width : (document.body.clientWidth - 193) / 25,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '地理位置',
			dataIndex : 'position',
			width : (document.body.clientWidth - 193) / 3,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "操作",
			dataIndex : "c_agree",
			width : 80,
			renderer : function(value, cellmeta, record) {
				return '<a href="#" onclick="clickShowInfo(\''
					+ record.data.id + '\')">查看详情</a>';
			}
		}
	]);


	var companyMangerGrid = new Ext.grid.EditorGridPanel({
		ds : companyDss,
		cm : companyCm,
		sm : companyCbsm,
		closable : true,
		border : false,
		id : 'companyManager',
		stripeRows : true,
		height : document.body.clientHeight - 106,
		bbar : new Ext.PagingToolbar({
			store : companyDss,
			displayInfo : true,
			displayMsg : ' 显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : '没有记录'
		}),
		tbar : [ '店名', '-', new Ext.form.TextField({
			width : 100,
			id : 'name'
		}), '区域', '-', new Ext.form.TextField({
			width : 100,
			id : 'info'
		}), '&nbsp;', {
			text : '查询',
			iconCls : 'addUser',
			handler : function() {

				companyDss.baseParams = {
					name : Ext.getCmp('name').getValue(),
					info : Ext.getCmp('info').getValue(), //ynw
					start : 0,
					limit : 20
				};

				companyDss.load({
					params : {
						name : Ext.getCmp('name').getValue(),
						info : Ext.getCmp('info').getValue(),
						start : 0,
						limit : 20
					}
				});
			}
		}, '</br>\n', '&nbsp;&nbsp;', {
			text : '关闭商家',
			iconCls : 'addUser',
			handler : function() {
				Ext.MessageBox.confirm('消息', '确定要关闭此用户吗?', closecompany);
			}
		}, '</br>\n', '&nbsp;&nbsp;', {
			text : '开启商家',
			iconCls : 'addUser',
			handler : function() {
				Ext.MessageBox.confirm('消息', '确定要开启此用户吗?', opencompany);
			}
		}
		]
	});

	function closecompany(btn) {
		if (btn == 'yes') {
			var id = "";
			var rends = companyMangerGrid.getSelectionModel().getSelections();
			if (rends.length == 1) {
				for (var i = 0; i < rends.length; i++) {
					var rend = rends[i];
					if (i == rends.length - 1) {
						id = id + rend.get("id");
					} else {
						id = id + rend.get("id") + ",";
					}
				}

				Ext.Ajax.request({
					url : BASE_URL + LOGIN_ACTION.COMPANY_CLOSE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : id,
						isOpen : "false",
					},
					success : function(form, action) {
						companyDss.reload();
						var json = Ext.util.JSON.decode(form.responseText);
						Ext.Msg.alert('温馨提示', json.message);
					},
					failure : function(form, action) {
						var json = Ext.util.JSON.decode(form.responseText);
						Ext.Msg.alert('温馨提示', json.message);
					}
				});
			} else {
				Ext.Msg.alert('温馨提示', '请选择一条需要修改的数据!');
			}
		}
	}

	function opencompany(btn) {
		if (btn == 'yes') {
			var id = "";
			var rends = companyMangerGrid.getSelectionModel().getSelections();
			if (rends.length == 1) {
				for (var i = 0; i < rends.length; i++) {
					var rend = rends[i];
					if (i == rends.length - 1) {
						id = id + rend.get("id");
					} else {
						id = id + rend.get("id") + ",";
					}
				}

				Ext.Ajax.request({
					url : BASE_URL + LOGIN_ACTION.COMPANY_CLOSE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : id,
						isOpen : "true",
					},
					success : function(form, action) {
						companyDss.reload();
						var json = Ext.util.JSON.decode(form.responseText);
						Ext.Msg.alert('温馨提示', json.message);
					},
					failure : function(form, action) {
						var json = Ext.util.JSON.decode(form.responseText);
						Ext.Msg.alert('温馨提示', json.message);
					}
				});
			} else {
				Ext.Msg.alert('温馨提示', '请选择一条需要修改的数据!');
			}
		}
	}

	function saveORupdatecompanyMangerGrid(obj) {
		var r = obj.record; // 获取被修改的行
		Ext.Ajax.request({
			url : BASE_URL + LOGIN_ACTION.COMPANY_UPDATE,
			waitMsg : '处理中，请稍等..',
			waitTitle : '请稍等',
			params : {
				id : r.data.id,
				logo : r.data.logo,
				name : r.data.name,
				phone : r.data.phone,
				position : r.data.position,
				coordinates : r.data.coordinates,
				businessTime : r.data.business_time,
				honor : r.data.honor,
				notice : r.data.notice,
				info : r.data.info,
				assess : r.data.assess,
				isBusiness : r.data.is_business,
				img : r.data.img,
			},
			success : function(response, options) {
				companyDss.reload();
				var json = Ext.util.JSON.decode(response.responseText);
				Ext.Msg.alert('温馨提示', json.message);
			},
			failure : function(form, action) {
				Ext.MessageBox.show({
					title : '温馨提示',
					msg : '数据插入失败，请重新尝试，谢谢!',
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});
			}
		});
	}

	companyDss.load({
		params : {
			start : 0,
			limit : 20
		}
	});

	companyMangerGrid.addListener('afteredit', saveORupdatecompanyMangerGrid);
	companyMangerGrid.render('grid-companyManager');
});


//添加单击事件
function clickShowInfo(id) {
	Ext.Ajax.request({
		url : BASE_URL + LOGIN_ACTION.COMPANY_FIND,
		waitMsg : '处理中，请稍等..',
		waitTitle : '请稍等',
		params : {
			id : id
		},
		success : function(response, options) {
			//companyDss.reload();
			//Ext.getCmp('companyDss').reload();
			Ext.getCmp('companyManager').getStore().reload();
			var json = Ext.util.JSON.decode(response.responseText);
			var info = new Ext.FormPanel({
				labelWidth : 75,
				url : 'save-form.php',
				frame : true,
				bodyStyle : 'padding:5px 5px 0',
				width : 500,
				items : [ {
					xtype : 'fieldset',
					title : '基本信息',
					collapsible : true,
					autoHeight : true,
					defaults : {
						width : 300
					},
					defaultType : 'textfield',
					items : [ {
						fieldLabel : '商家名称',
						name : 'name',
						dataIndex : 'name',
						value : json.name,
						allowBlank : false
					}, {
						fieldLabel : '联系方式',
						name : 'phone',
						value : json.phone,
						allowBlank : false
					}, {
						fieldLabel : '地理位置',
						name : 'position',
						value : json.position,
						allowBlank : false
					}, {
						fieldLabel : '营业时间',
						name : 'business_time',
						value : json.business_time,
						allowBlank : false
					}, {
						fieldLabel : '资质',
						name : 'honor',
						value : json.honor,
						allowBlank : false
					}, {
						fieldLabel : '分类',
						name : 'type',
						value : json.type,
						allowBlank : false
					}, {
						fieldLabel : '所属区域',
						name : 'info',
						value : json.info,
						allowBlank : false
					}
					]
				}, {
					xtype : 'fieldset',
					title : '动态信息',
					collapsible : true,
					autoHeight : true,
					defaults : {
						width : 300
					},
					defaultType : 'textfield',
					items : [ {
						fieldLabel : '是否营业',
						name : 'is_business',
						value : json.is_business,
						allowBlank : false
					}, {
						fieldLabel : '公告',
						value : json.notice,
						allowBlank : false,
						name : 'notice'
					}, {
						fieldLabel : '评分 ',
						value : json.assess,
						allowBlank : false,
						name : 'assess'
					}, {
						fieldLabel : '门头图片',
						value : json.img,
						allowBlank : false,
						name : 'img'
					}, {
						fieldLabel : 'logo',
						value : json.logo,
						allowBlank : false,
						name : 'logo'
					}, {
						fieldLabel : '销量',
						value : json.monSales,
						allowBlank : false,
						name : 'monSales'
					}
					]
				}, {
					xtype : 'fieldset',
					title : '隐藏属性',
					collapsible : true,
					autoHeight : true,
					defaults : {
						width : 300
					},
					defaultType : 'textfield',
					//					collapsed : true,
					items : [ {
						fieldLabel : 'id',
						name : 'id',
						value : json.id,
						allowBlank : false,
					}, {
						fieldLabel : '坐标',
						value : json.coordinates,
						allowBlank : false,
						name : 'coordinates'
					}, {
						fieldLabel : '是否开店',
						value : json.is_close,
						allowBlank : false,
						name : 'is_close'
					}, {
						fieldLabel : '审核状态',
						value : json.audit,
						allowBlank : false,
						name : 'audit'
					}
					]
				}
				],

				buttons : [ {
					text : '修改',
					handler : function() {

						if (info.getForm().isValid()) {

							info.getForm().submit({ // 提交细节
								waitMsg : '处理中，请稍等...',
								waitTitle : '请稍等',
								url : BASE_URL + LOGIN_ACTION.PENDING,
								success : function(form, action) {
									info.getForm().reset();
									Ext.Msg.alert('温馨提示', '提交成功!');
									changepasswordWin.destroy();
									companyDss.reload();
								},
								failure : function(form, action) {
									Ext.Msg.alert('温馨提示', '数据添加或修改不成功！');
									changepasswordWin.destroy();
								}
							});
						} else {
							Ext.Msg.alert('温馨提示', '错误：不允许为空');
						}
					}
				}, {
					text : '取消',
					handler : function() {
						info.getForm().destroy();
						changepasswordWin.hide();
					}
				} ]
			});
			info.render(document.body);

			var changepasswordWin = new Ext.Window({
				title : '商家详情',
				width : 700,
				height : 900,
				minWidth : 500,
				minHeight : 700,
				layout : 'fit',
				resizable : false,
				bodyStyle : 'padding:5px;',
				closable : true,
				buttonAlign : 'center',
				items : info,
				modal : true,
				buttons : [ {
					text : '确定',
					handler : function() {
						info.getForm().destroy();
						changepasswordWin.hide();
					}
				} ]
			});
			changepasswordWin.show();
		},
		failure : function(form, action) {
			Ext.MessageBox.show({
				title : '温馨提示',
				msg : '数据插入失败，请重新尝试，谢谢!',
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.ERROR
			});
		}
	});
}