Ext.onReady(function() {
	var relationDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.RELATION_VIEW
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'id',
			successProperty : '@success',
			fields : [ 'id', 'company_id', 'activity_id' ]
		}),
		listeners : {
			load : function() {
				Ext.getCmp('totalSum').setText(this.reader.jsonData.totalSum)
			}
		}
	});

	var relationCbsm = new Ext.grid.CheckboxSelectionModel();
	var relationCm = new Ext.grid.ColumnModel([
		relationCbsm, {
			header : 'id',
			dataIndex : 'id',
			locked : true,
			hidden : true,
			width : (document.body.clientWidth - 193) / 18
		}, {
			header : '商家id',
			dataIndex : 'company_id',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '活动id',
			dataIndex : 'activity_id',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}
	]);


	var relationMangerGrid = new Ext.grid.EditorGridPanel({
		ds : relationDss,
		cm : relationCm,
		sm : relationCbsm,
		closable : true,
		border : false,
		id : 'relationManager',
		stripeRows : true,
		height : document.body.clientHeight - 106,
		bbar : new Ext.PagingToolbar({
			store : relationDss,
			displayInfo : true,
			displayMsg : ' 显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : '没有记录'
		}),
		tbar : [ '商家id', '-', new Ext.form.TextField({
			width : 100,
			id : 'company_id'
		}), '&nbsp;', {
			text : '查询',
			iconCls : 'addUser',
			handler : function() {

				relationDss.baseParams = {
					companyId : Ext.getCmp('company_id').getValue(),
					start : 0,
					limit : 20
				};

				relationDss.load({
					params : {
						companyId : Ext.getCmp('company_id').getValue(),
						start : 0,
						limit : 20
					}
				});
			}
		}, '&nbsp;&nbsp;&nbsp;&nbsp;', {
			text : '添加',
			iconCls : 'addUser',
			handler : function() {
				relationDss.insert(0, new Ext.data.Record({
					company_id : '必填信息',
					activity_id : '必填信息',
				}));
			}
		}, '</br>\n', '&nbsp;&nbsp;', {
			text : '删除',
			iconCls : 'addUser',
			handler : function() {
				Ext.MessageBox.confirm('消息', '确定要删除此用户吗?', deleterelation);
			}
		}, '&nbsp;&nbsp;用户总数:&nbsp;', new Ext.form.Label({
			width : 60,
			id : 'totalSum'
		})
		]
	});

	function deleterelation(btn) {
		if (btn == 'yes') {
			var b = "";
			var rends = relationMangerGrid.getSelectionModel().getSelections();
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
					url : BASE_URL + LOGIN_ACTION.RELATION_DELETE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : b
					},
					success : function(form, action) {
						relationDss.reload();
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

	function saveORupdaterelationMangerGrid(obj) {
		var r = obj.record; // 获取被修改的行
		if (r.data.companyId != '必填信息' && r.data.activityId != '必填信息') {

			if (r.data.id == null || r.data.id == '') {
				Ext.Ajax.request({
					url : BASE_URL + LOGIN_ACTION.RELATION_UPDATE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						companyId : r.data.company_id,
						activityId : r.data.activity_id,
					},
					success : function(response, options) {
						relationDss.reload();
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
					url : BASE_URL + LOGIN_ACTION.RELATION_UPDATE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : r.data.id,
						companyId : r.data.company_id,
						activityId : r.data.activity_id,
					},
					success : function(response, options) {
						relationDss.reload();
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

	relationDss.load({
		params : {
			start : 0,
			limit : 20
		}
	});

	relationMangerGrid.addListener('afteredit', saveORupdaterelationMangerGrid);
	relationMangerGrid.render('grid-relationManager');
});