Ext.onReady(function() {
	var includeDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.INCLUDE_VIEW
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'id',
			successProperty : '@success',
			fields : [ 'id', 'user_id', 'company_id', 'goods_id'  ]
		}),
		listeners : {
			load : function() {
				Ext.getCmp('totalSum').setText(this.reader.jsonData.totalSum)
			}
		}
	});

	var includeCbsm = new Ext.grid.CheckboxSelectionModel();
	var includeCm = new Ext.grid.ColumnModel([
		includeCbsm, {
			header : 'id',
			dataIndex : 'id',
			locked : true,
			hidden : true,
			width : (document.body.clientWidth - 193) / 18
		}, {
			header : '用户id',
			dataIndex : 'user_id',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '商家id',
			dataIndex : 'company_id',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '商品id',
			dataIndex : 'goods_id',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}
	]);


	var includeMangerGrid = new Ext.grid.EditorGridPanel({
		ds : includeDss,
		cm : includeCm,
		sm : includeCbsm,
		closable : true,
		border : false,
		id : 'includeManager',
		stripeRows : true,
		height : document.body.clientHeight - 106,
		bbar : new Ext.PagingToolbar({
			store : includeDss,
			displayInfo : true,
			displayMsg : ' 显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : '没有记录'
		}),
		tbar : [ '用户id', '-', new Ext.form.TextField({
			width : 100,
			id : 'user_id'
		}), '&nbsp;', {
			text : '查询',
			iconCls : 'addUser',
			handler : function() {

				includeDss.baseParams = {
					userId : Ext.getCmp('user_id').getValue(),
					start : 0,
					limit : 20
				};

				adminDss.load({
					params : {
						userId : Ext.getCmp('user_id').getValue(),
						start : 0,
						limit : 20
					}
				});
			}
		}, '&nbsp;&nbsp;&nbsp;&nbsp;', {
			text : '添加',
			iconCls : 'addUser',
			handler : function() {
				includeDss.insert(0, new Ext.data.Record({
					user_id : '必填信息',
					company_id : '必填信息',
					goods_id : '', 
				}));
			}
		}, '</br>\n', '&nbsp;&nbsp;', {
			text : '删除',
			iconCls : 'addUser',
			handler : function() {
				Ext.MessageBox.confirm('消息', '确定要删除此用户吗?', deleteinclude);
			}
		}, '&nbsp;&nbsp;用户总数:&nbsp;', new Ext.form.Label({
			width : 60,
			id : 'totalSum'
		})
		]
	});

	function deleteinclude(btn) {
		if (btn == 'yes') {
			var b = "";
			var rends = includeMangerGrid.getSelectionModel().getSelections();
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
					url : BASE_URL + LOGIN_ACTION.INCLUDE_DELETE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : b
					},
					success : function(form, action) {
						includeDss.reload();
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

	function saveORupdateincludeMangerGrid(obj) {
		var r = obj.record; // 获取被修改的行
		if (r.data.user_id != '必填信息' && r.data.company_id != '必填信息') {

			if (r.data.id == null || r.data.id == '') {
				Ext.Ajax.request({
					url : BASE_URL + LOGIN_ACTION.INCLUDE_UPDATE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						userId : r.data.user_id,
						companyId : r.data.company_id,
						goodsId: r.data.goods_id, 
					},
					success : function(response, options) {
						includeDss.reload();
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
					url : BASE_URL + LOGIN_ACTION.INCLUDE_UPDATE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : r.data.id,
						userId : r.data.user_id,
						companyId : r.data.company_id,
						goodsId: r.data.goods_id,
					},
					success : function(response, options) {
						includeDss.reload();
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

	includeDss.load({
		params : {
			start : 0,
			limit : 20
		}
	});

	includeMangerGrid.addListener('afteredit', saveORupdateincludeMangerGrid);
	includeMangerGrid.render('grid-includeManager');
});