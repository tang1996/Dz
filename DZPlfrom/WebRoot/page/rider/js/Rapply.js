Ext.onReady(function() {
	var RapplyDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.RIDER_EXAMINE
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'id',
			successProperty : '@success',
			fields : [ 'id', 'name', 'phone', 'cardNo', 'create_time', 'age', 'education', 'city', 'sex' ]
		}),
	});

	var RapplyCbsm = new Ext.grid.CheckboxSelectionModel();
	var RapplyCm = new Ext.grid.ColumnModel([
		RapplyCbsm, {
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
			header : "身份证号",
			dataIndex : 'cardNo',
			locked : true,
			hidden : true,
			width : (document.body.clientWidth - 193) / 20,
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
			header : '学历',
			dataIndex : 'education',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '年龄',
			dataIndex : 'sex',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '城市',
			dataIndex : 'city',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}
	]);


	var RapplyMangerGrid = new Ext.grid.EditorGridPanel({
		ds : RapplyDss,
		cm : RapplyCm,
		sm : RapplyCbsm,
		closable : true,
		border : false,
		id : 'RapplyManager',
		stripeRows : true,
		height : document.body.clientHeight - 106,
		bbar : new Ext.PagingToolbar({
			store : RapplyDss,
			displayInfo : true,
			displayMsg : ' 显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : '没有记录'
		}),
		tbar : ['&nbsp;&nbsp;&nbsp;&nbsp;', {
			text : '添加',
			iconCls : 'addUser',
			handler : function() {
				RapplyDss.insert(0, new Ext.data.Record({
					name : '必填信息',
					phone : '必填信息',
					cardNo : '必填信息',
					create_time : '',
					age : '',
					education : '',
					sex : '必填信息',
					city : '必填信息',
				}));
			}
		}, '</br>\n', '&nbsp;&nbsp;', {
			text : '审核通过',
			iconCls : 'addUser',
			handler : function() {
				Ext.MessageBox.confirm('消息', '确定要通过此用户吗?', Rapplyrider);
			}
		}, '</br>\n', '&nbsp;&nbsp;', {
			text : '删除',
			iconCls : 'addUser',
			handler : function() {
				Ext.MessageBox.confirm('消息', '确定要删除此用户吗?', deleterider);
			}
		}
		]
	});

	function deleterider(btn) {
		if (btn == 'yes') {
			var b = "";
			var rends = RapplyMangerGrid.getSelectionModel().getSelections();
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
					url : BASE_URL + LOGIN_ACTION.RIDER_EXAMINE_DELETE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : b
					},
					success : function(form, action) {
						RapplyDss.reload();
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
	
	function Rapplyrider(btn) {
		if (btn == 'yes') {
			var b = "";
			var rends = RapplyMangerGrid.getSelectionModel().getSelections();
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
					url : BASE_URL + LOGIN_ACTION.RIDER_EXAMINE_APPLY,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : b
					},
					success : function(form, action) {
						RapplyDss.reload();
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

	function saveORupdateRapplyMangerGrid(obj) {
		var r = obj.record; // 获取被修改的行
		if (r.data.name != '必填信息' && r.data.phone != '必填信息' && r.data.cardNo != '必填信息' && r.data.sex != '必填信息' && r.data.city != '必填信息') {

			if (r.data.id == null || r.data.id == '') {
				Ext.Ajax.request({
					url : BASE_URL + LOGIN_ACTION.RIDER_EXAMINE_SAVE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						name : r.data.name,
						phone : r.data.phone,
						cardNo : r.data.cardNo,
						create_time : r.data.create_time,
						age : r.data.age,
						education : r.data.education,
						sex : r.data.sex,
						city : r.data.city,
					},
					success : function(response, options) {
						RapplyDss.reload();
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
						education : r.data.education,
						sex : r.data.sex,
						city : r.data.city,
					},
					success : function(response, options) {
						RapplyDss.reload();
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

	RapplyDss.load({
		params : {
			start : 0,
			limit : 20
		}
	});

	RapplyMangerGrid.addListener('afteredit', saveORupdateRapplyMangerGrid);
	RapplyMangerGrid.render('grid-RapplyManager');
});