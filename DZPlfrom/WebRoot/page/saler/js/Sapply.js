Ext.onReady(function() {
	var SapplyDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.SALER_EXAMINE
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'id',
			successProperty : '@success',
			fields : [ 'id', 'name', 'phone', 'cardNo', 'create_time', 'age', 'education', 'city', 'sex' ]
		}),
	});

	var SapplyCbsm = new Ext.grid.CheckboxSelectionModel();
	var SapplyCm = new Ext.grid.ColumnModel([
		SapplyCbsm, {
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
			dataIndex : 'create_time',
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
		}
	]);


	var SapplyMangerGrid = new Ext.grid.EditorGridPanel({
		ds : SapplyDss,
		cm : SapplyCm,
		sm : SapplyCbsm,
		closable : true,
		border : false,
		id : 'SapplyManager',
		stripeRows : true,
		height : document.body.clientHeight - 106,
		bbar : new Ext.PagingToolbar({
			store : SapplyDss,
			displayInfo : true,
			displayMsg : ' 显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : '没有记录'
		}),
		tbar : [ '销售员id', '-', new Ext.form.TextField({
			width : 100,
			id : 'salerId'
		}), '&nbsp;', {
			text : '查询',
			iconCls : 'addUser',
			handler : function() {

				SapplyDss.baseParams = {
					salerId : Ext.getCmp('salerId').getValue(),
					start : 0,
					limit : 20
				};

				SapplyDss.load({
					params : {
						salerId : Ext.getCmp('salerId').getValue(),
						start : 0,
						limit : 20
					}
				});
			}
		}, '&nbsp;&nbsp;&nbsp;&nbsp;', {
			text : '添加',
			iconCls : 'addUser',
			handler : function() {
				SapplyDss.insert(0, new Ext.data.Record({
					name : '必填信息',
					phone : '必填信息',
					cardNo : '必填信息',
					sex : '必填信息',
					age : '必填信息',
					education : '必填信息',
					city : '必填信息',
				}));
			}
		}, '</br>\n', '&nbsp;&nbsp;', {
			text : '成为业务员',
			iconCls : 'addUser',
			handler : function() {
				Ext.MessageBox.confirm('消息', '确定要通过此用户吗?', Sapplysaler1);
			}
		}, '</br>\n', '&nbsp;&nbsp;', {
			text : '成为区域经理',
			iconCls : 'addUser',
			handler : function() {
				Ext.MessageBox.confirm('消息', '确定要通过此用户吗?', Sapplysaler2);
			}
		}, '</br>\n', '&nbsp;&nbsp;', {
			text : '成为地市级代理',
			iconCls : 'addUser',
			handler : function() {
				Ext.MessageBox.confirm('消息', '确定要通过此用户吗?', Sapplysaler3);
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

	function Sapplysaler1(btn) {
		var power = btn;
		if (btn == 'yes') {
			var b = "";
			var rends = SapplyMangerGrid.getSelectionModel().getSelections();
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
					url : BASE_URL + LOGIN_ACTION.SALER_INFO_SAVE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : b,
						salerPowerId : 3
					},
					success : function(form, action) {
						SapplyDss.reload();
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
	function Sapplysaler2(btn) {
		var power = btn;
		if (btn == 'yes') {
			var b = "";
			var rends = SapplyMangerGrid.getSelectionModel().getSelections();
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
					url : BASE_URL + LOGIN_ACTION.SALER_INFO_SAVE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : b,
						salerPowerId : 2
					},
					success : function(form, action) {
						SapplyDss.reload();
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
	function Sapplysaler3(btn) {
		var power = btn;
		if (btn == 'yes') {
			var b = "";
			var rends = SapplyMangerGrid.getSelectionModel().getSelections();
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
					url : BASE_URL + LOGIN_ACTION.SALER_INFO_SAVE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : b,
						salerPowerId : 1
					},
					success : function(form, action) {
						SapplyDss.reload();
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
			var rends = SapplyMangerGrid.getSelectionModel().getSelections();
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
					url : BASE_URL + LOGIN_ACTION.SALER_EXAMINE_DELETE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : b
					},
					success : function(form, action) {
						SapplyDss.reload();
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

	function saveORupdateSapplyMangerGrid(obj) {
		var r = obj.record; // 获取被修改的行
		if (r.data.city != '必填信息' && r.data.sex != '必填信息' && r.data.phone != '必填信息' && r.data.cardNo != '必填信息'
			&& r.data.name != '必填信息' && r.data.education != '必填信息' && r.data.age != '必填信息') {

			if (r.data.id == null || r.data.id == '') {
				Ext.Ajax.request({
					url : BASE_URL + LOGIN_ACTION.SALER_EXAMINE_SAVE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						name : r.data.name,
						phone : r.data.phone,
						cardNo : r.data.cardNo,
						sex : r.data.sex,
						age : r.data.age,
						education : r.data.education,
						city : r.data.city,
					},
					success : function(response, options) {
						SapplyDss.reload();
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
						sex : r.data.sex,
						age : r.data.age,
						education : r.data.education,
						city : r.data.city,
					},
					success : function(response, options) {
						SapplyDss.reload();
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

	SapplyDss.load({
		params : {
			start : 0,
			limit : 20
		}
	});

	SapplyMangerGrid.addListener('afteredit', saveORupdateSapplyMangerGrid);
	SapplyMangerGrid.render('grid-SapplyManager');
});