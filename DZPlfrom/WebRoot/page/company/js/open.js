Ext.onReady(function() {
	var openDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.COMPANY_OPENLIST
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'id',
			successProperty : '@success',
			fields : [ 'id', 'name', 'phone', 'create_time', 'address', 'shopType', 'shopPhone', 'location' ]
		}),
	});

	var openCbsm = new Ext.grid.CheckboxSelectionModel();
	var openCm = new Ext.grid.ColumnModel([
		openCbsm, {
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
			header : '申请时间',
			dataIndex : 'create_time',
			width : (document.body.clientWidth - 193) / 12,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '商家类型',
			dataIndex : 'shopType',
			width : (document.body.clientWidth - 193) / 25,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '前台电话',
			dataIndex : 'shopPhone',
			width : (document.body.clientWidth - 193) / 15,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '业务员名称',
			dataIndex : 'salerName',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '业务员电话',
			dataIndex : 'salerPhone',
			width : (document.body.clientWidth - 193) / 15,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '店铺坐标',
			dataIndex : 'location',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '店铺地址',
			dataIndex : 'address',
			width : (document.body.clientWidth - 193) / 3,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}
	]);


	var openMangerGrid = new Ext.grid.EditorGridPanel({
		ds : openDss,
		cm : openCm,
		sm : openCbsm,
		closable : true,
		border : false,
		id : 'openManager',
		stripeRows : true,
		height : document.body.clientHeight - 106,
		bbar : new Ext.PagingToolbar({
			store : openDss,
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

				openDss.baseParams = {
					name : Ext.getCmp('name').getValue(),
					start : 0,
					limit : 20
				};

				openDss.load({
					params : {
						name : Ext.getCmp('name').getValue(),
						start : 0,
						limit : 20
					}
				});
			}
		}, '&nbsp;&nbsp;&nbsp;&nbsp;', {
			text : '同意开店',
			iconCls : 'addUser',
			handler : function() {
				Ext.MessageBox.confirm('消息', '确定要同意申请吗?', saveopen);
			}
		}, '</br>\n', '&nbsp;&nbsp;', {
			text : '添加商家',
			iconCls : 'addUser',
			handler : function() {
				openDss.insert(0, new Ext.data.Record({
					name : '必填信息',
					phone : '必填信息',
					address : '必填信息',
					shopType : '必填信息',
					shopPhone : '必填信息',
					location : '必填信息',
				}));
			}
		}, '</br>\n', '&nbsp;&nbsp;', {
			text : '删除',
			iconCls : 'addUser',
			handler : function() {
				Ext.MessageBox.confirm('消息', '确定要删除此用户吗?', deleteopen);
			}
		}
		]
	});

	function deleteopen(btn) {
		if (btn == 'yes') {
			var b = "";
			var rends = openMangerGrid.getSelectionModel().getSelections();
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
					url : BASE_URL + LOGIN_ACTION.CLASSIFY_DELETE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : b
					},
					success : function(form, action) {
						openDss.reload();
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

	function saveopen(btn) {
		if (btn == 'yes') {
			var b = "";
			var rends = openMangerGrid.getSelectionModel().getSelections();
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
					url : BASE_URL + LOGIN_ACTION.COMPANY_SAVE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : b
					},
					success : function(form, action) {
						openDss.reload();
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

	function saveORupdateopenMangerGrid(obj) {
		var r = obj.record; // 获取被修改的行
		if (r.data.name != '必填信息' && r.data.phone != '必填信息' && r.data.address != '必填信息' && r.data.shopType != '必填信息' && r.data.shopPhone != '必填信息' && r.data.location != '必填信息') {

			if (r.data.id == null || r.data.id == '') {
				Ext.Ajax.request({
					url : BASE_URL + LOGIN_ACTION.COMPANY_EXAMINE_SAVE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						name : r.data.name,
						phone : r.data.phone,
						address : r.data.address,
						shopType : r.data.shopType,
						shopPhone : r.data.shopPhone,
						location : r.data.location,
					},
					success : function(response, options) {
						openDss.reload();
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
					url : BASE_URL + LOGIN_ACTION.COMPANY_EXAMINE_SAVE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : r.data.id,
						name : r.data.name,
						phone : r.data.phone,
						address : r.data.address,
						shopType : r.data.shopType,
						shopPhone : r.data.shopPhone,
						location : r.data.location,
					},
					success : function(response, options) {
						openDss.reload();
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

	openDss.load({
		params : {
			start : 0,
			limit : 20
		}
	});

	openMangerGrid.addListener('afteredit', saveORupdateopenMangerGrid);
	openMangerGrid.render('grid-openManager');
});