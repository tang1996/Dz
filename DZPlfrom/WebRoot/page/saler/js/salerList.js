Ext.onReady(function() {
	var salerListDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.SALER_LIST
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'id',
			successProperty : '@success',
			fields : [ 'id', 'name', 'phone', 'cardNo', 'createTime', 'age', 'education', 'city', 'sex', 'code' ]
		}),
	});

	var salerListCbsm = new Ext.grid.CheckboxSelectionModel();
	var salerListCm = new Ext.grid.ColumnModel([
		salerListCbsm, {
			header : 'id',
			dataIndex : 'id',
			locked : true,
			hidden : true,
			width : (document.body.clientWidth - 193) / 18
		}, {
			header : '姓名',
			dataIndex : 'name',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '手机号',
			dataIndex : 'phone',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "性别",
			dataIndex : 'sex',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "城市",
			dataIndex : 'city',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "身份证号",
			dataIndex : 'cardNo',
			width : (document.body.clientWidth - 193) / 10,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '申请时间',
			dataIndex : 'createTime',
			width : (document.body.clientWidth - 193) / 10,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '年龄',
			dataIndex : 'age',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "学历",
			dataIndex : 'education',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "推广码",
			dataIndex : 'code',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}
	]);


	var salerListMangerGrid = new Ext.grid.EditorGridPanel({
		ds : salerListDss,
		cm : salerListCm,
		sm : salerListCbsm,
		closable : true,
		border : false,
		id : 'salerListManager',
		stripeRows : true,
		height : document.body.clientHeight - 106,
		bbar : new Ext.PagingToolbar({
			store : salerListDss,
			displayInfo : true,
			displayMsg : ' 显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : '没有记录'
		}),
		tbar : [ '手机号', '-', new Ext.form.TextField({
			width : 100,
			id : 'phone'
		}), '&nbsp;', {
			text : '查询',
			iconCls : 'addUser',
			handler : function() {

				salerListDss.baseParams = {
					phone : Ext.getCmp('phone').getValue(),
					start : 0,
					limit : 20
				};

				salerListDss.load({
					params : {
						phone : Ext.getCmp('phone').getValue(),
						start : 0,
						limit : 20
					}
				});
			}
		}, '&nbsp;&nbsp;&nbsp;&nbsp;', {
			text : '添加',
			iconCls : 'addUser',
			handler : function() {
				salerListDss.insert(0, new Ext.data.Record({
					name : '必填信息',
					phone : '必填信息',
					cardNo : '必填信息',
					create_time : '必填信息',
					age : '必填信息',
				}));
			}
		}, '</br>\n', '&nbsp;&nbsp;', {
			text : '删除',
			iconCls : 'addUser',
			handler : function() {
				Ext.MessageBox.confirm('消息', '确定要删除此用户吗?', deletesaler);
			}
		}
		]
	});

	function applysaler(btn) {
		if (btn == 'yes') {
			var b = "";
			var rends = salerListMangerGrid.getSelectionModel().getSelections();
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
					url : BASE_URL + LOGIN_ACTION.RIDER_DELETE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : b
					},
					success : function(form, action) {
						salerListDss.reload();
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

	function deletesaler(btn) {
		if (btn == 'yes') {
			var b = "";
			var rends = salerListMangerGrid.getSelectionModel().getSelections();
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
					url : BASE_URL + LOGIN_ACTION.SALER_INFO_DELETE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : b
					},
					success : function(form, action) {
						salerListDss.reload();
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

	function saveORupdatesalerListMangerGrid(obj) {
		var r = obj.record; // 获取被修改的行
		if (r.data.user_name != '必填信息' && r.data.password != '必填信息') {

			if (r.data.id == null || r.data.id == '') {
				Ext.Ajax.request({
					url : BASE_URL + LOGIN_ACTION.RIDER_UPDATE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						name : r.data.name,
						phone : r.data.phone,
						cardNo : r.data.cardNo,
						create_time : r.data.create_time,
						age : r.data.age,
					},
					success : function(response, options) {
						salerListDss.reload();
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
					url : BASE_URL + LOGIN_ACTION.RIDER_UPDATE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : r.data.id,
						name : r.data.name,
						phone : r.data.phone,
						cardNo : r.data.cardNo,
						create_time : r.data.create_time,
						age : r.data.age,
					},
					success : function(response, options) {
						salerListDss.reload();
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

	salerListDss.load({
		params : {
			start : 0,
			limit : 20
		}
	});

	salerListMangerGrid.addListener('afteredit', saveORupdatesalerListMangerGrid);
	salerListMangerGrid.render('grid-salerListManager');
});