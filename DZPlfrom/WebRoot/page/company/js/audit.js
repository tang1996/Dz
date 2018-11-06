Ext.onReady(function() {
	var auditDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.COMPANY_AUDIT
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'id',
			successProperty : '@success',
			fields : [ 'id', 'logo', 'name', 'phone', 'position', 'coordinates', 
				'business_time',  'honor', 'notice', 'info', 'assess', 'is_close', 'type_id', 'img', 'is_business','audit' ]
		}),
	});

	var auditCbsm = new Ext.grid.CheckboxSelectionModel();
	var auditCm = new Ext.grid.ColumnModel([
		auditCbsm, {
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
			header : '是否开店',
			dataIndex : 'is_close',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '商品数量',
			dataIndex : 'goodsNum',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '餐桌数量',
			dataIndex : 'tableNum',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '商家分类',
			dataIndex : 'type_id',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '是否营业',
			dataIndex : 'is_business',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '审核状态',
			dataIndex : 'audit',
			width : (document.body.clientWidth - 193) / 18,
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
		}
	]);


	var auditMangerGrid = new Ext.grid.EditorGridPanel({
		ds : auditDss,
		cm : auditCm,
		sm : auditCbsm,
		closable : true,
		border : false,
		id : 'auditManager',
		stripeRows : true,
		height : document.body.clientHeight - 106,
		bbar : new Ext.PagingToolbar({
			store : auditDss,
			displayInfo : true,
			displayMsg : ' 显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : '没有记录'
		}),
		tbar : [ '店名', '-', new Ext.form.TextField({
			width : 100,
			id : 'name'
		}), '&nbsp;', {
			text : '查询',
			iconCls : 'addUser',
			handler : function() {

				auditDss.baseParams = {
					name : Ext.getCmp('name').getValue(),
					start : 0,
					limit : 20
				};

				auditDss.load({
					params : {
						name : Ext.getCmp('name').getValue(),
						start : 0,
						limit : 20
					}
				});
			}
		}, '&nbsp;&nbsp;&nbsp;&nbsp;', {
			text : '审核通过',
			iconCls : 'addUser',
			handler : function() {
				Ext.MessageBox.confirm('消息', '确定要通过审核吗?', updateaudit);
			}
		}, '</br>\n', '&nbsp;&nbsp;', {
			text : '删除',
			iconCls : 'addUser',
			handler : function() {
				Ext.MessageBox.confirm('消息', '确定要删除此用户吗?', deleteaudit);
			}
		}
		]
	});

	function deleteaudit(btn) {
		if (btn == 'yes') {
			var b = "";
			var rends = auditMangerGrid.getSelectionModel().getSelections();
			if (rends.length == 1) {
				for (var i = 0; i < rends.length; i++) {
					var rend = rends[i];
					if (i == rends.length - 1) {
						b = b + rend.get("id");
					} else {
						b = b + rend.get("id") + ",";
					}
				}

				Ext.Ajax.request({
					url : BASE_URL + LOGIN_ACTION.COMPANY_DELETE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : b
					},
					success : function(form, action) {
						auditDss.reload();
						var json = Ext.util.JSON.decode(form.responseText);
						Ext.Msg.alert('温馨提示', json.message);
					},
					failure : function(form, action) {
						var json = Ext.util.JSON.decode(form.responseText);
						Ext.Msg.alert('温馨提示', json.message);
					}
				});
			} else {
				Ext.Msg.alert('温馨提示', '请选择一条需要删除的数据!');
			}
		}
	}
	;

	function updateaudit(btn) {
		if (btn == 'yes') {
			var b = "";
			var rends = auditMangerGrid.getSelectionModel().getSelections();
			if (rends.length == 1) {
				for (var i = 0; i < rends.length; i++) {
					var rend = rends[i];
					if (i == rends.length - 1) {
						b = b + rend.get("id");
					} else {
						b = b + rend.get("id") + ",";
					}
				}

				Ext.Ajax.request({
					url : BASE_URL + LOGIN_ACTION.COMPANY_UPDATEAUDIT,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : b
					},
					success : function(form, action) {
						auditDss.reload();
						var json = Ext.util.JSON.decode(form.responseText);
						Ext.Msg.alert('温馨提示', json.message);
						Ext.Ajax.request({
							url : BASE_URL + LOGIN_ACTION.COMPANY_SAVE,
							waitMsg : '处理中，请稍等..',
							waitTitle : '请稍等',
							params : {
								id : b
							},
							success : function(form, action) {
								auditDss.reload();
								var json = Ext.util.JSON.decode(form.responseText);
								Ext.Msg.alert('温馨提示', json.message);
							},
							failure : function(form, action) {
								var json = Ext.util.JSON.decode(form.responseText);
								Ext.Msg.alert('温馨提示', json.message);
							}
						});
					},
/*					success : function(form, action) {
						auditDss.reload();
						var json = Ext.util.JSON.decode(form.responseText);
						Ext.Msg.alert('温馨提示', json.message);
					},*/
					failure : function(form, action) {
						var json = Ext.util.JSON.decode(form.responseText);
						Ext.Msg.alert('温馨提示', json.message);
					}
				});
			} else {
				Ext.Msg.alert('温馨提示', '请选择一条需要删除的数据!');
			}
		}
	}
	;
	
	function saveORupdateauditMangerGrid(obj) {
		var r = obj.record; // 获取被修改的行
		if (r.data.name != '必填信息' && r.data.phone != '必填信息') {

			if (r.data.id == null || r.data.id == '') {
				Ext.Ajax.request({
					url : BASE_URL + LOGIN_ACTION.COMPANY_UPDATE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						logo : r.data.logo,
						name : r.data.name,
						phone: r.data.phone, 
						position: r.data.position,
						coordinates: r.data.coordinates,
						businessTime : r.data.business_time,
						honor : r.data.honor,
						notice: r.data.notice, 
						info: r.data.info,
						assess: r.data.assess,
						isClose : r.data.is_close,
						isBusiness : r.data.is_business,
						img : r.data.img,
						audit : r.data.audit,
					},
					success : function(response, options) {
						auditDss.reload();
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
			} else {
				Ext.Ajax.request({
					url : BASE_URL + LOGIN_ACTION.COMPANY_UPDATE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : r.data.id,
						logo : r.data.logo,
						name : r.data.name,
						phone: r.data.phone, 
						position: r.data.position,
						coordinates: r.data.coordinates,
						businessTime : r.data.business_time,
						honor : r.data.honor,
						notice: r.data.notice, 
						info: r.data.info,
						assess: r.data.assess,
						isClose : r.data.is_close,
						isBusiness : r.data.is_business,
						img : r.data.img,
						audit : r.data.audit,
					},
					success : function(response, options) {
						auditDss.reload();
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
		}
	}
	;

	auditDss.load({
		params : {
			start : 0,
			limit : 20
		}
	});

	auditMangerGrid.addListener('afteredit', saveORupdateauditMangerGrid);
	auditMangerGrid.render('grid-auditManager');
});