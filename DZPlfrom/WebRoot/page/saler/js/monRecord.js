Ext.onReady(function() {
	var monRecordDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.SALER_MON_COUNT
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'id',
			successProperty : '@success',
			fields : [ 'selfNum', 'otherNum', 'totalNum', 'salerName', 'salerPhone', 'id', 'city'
				, 'assess', 'power', 'mainNum', 'deputyNum', 'otherMainNum', 'otherDeputyNum' ]
		}),
	});

	var monRecordCbsm = new Ext.grid.CheckboxSelectionModel();
	var monRecordCm = new Ext.grid.ColumnModel([
		monRecordCbsm, {
			header : 'id',
			dataIndex : 'id',
			locked : true,
			hidden : true,
			width : (document.body.clientWidth - 193) / 18
		}, {
			header : '姓名',
			dataIndex : 'salerName',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '联系方式',
			dataIndex : 'salerPhone',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "地区",
			dataIndex : 'city',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "个人主营绩效",
			dataIndex : 'mainNum',
			width : (document.body.clientWidth - 193) / 15,
		}, {
			header : "个人副营绩效",
			dataIndex : 'deputyNum',
			width : (document.body.clientWidth - 193) / 15,
		}, {
			header : "个人商家绩效",
			dataIndex : 'selfNum',
			width : (document.body.clientWidth - 193) / 15,
		}, {
			header : "团队主营绩效",
			dataIndex : 'otherMainNum',
			width : (document.body.clientWidth - 193) / 15,
		}, {
			header : "团队副营绩效",
			dataIndex : 'otherDeputyNum',
			width : (document.body.clientWidth - 193) / 15,
		}, {
			header : "团队商家绩效",
			dataIndex : 'otherNum',
			width : (document.body.clientWidth - 193) / 15,
		}, {
			header : "当月总计",
			dataIndex : 'totalNum',
			width : (document.body.clientWidth - 193) / 20,
		}, {
			header : "考核",
			dataIndex : 'assess',
			width : (document.body.clientWidth - 193) / 20,
		}, {
			header : "职务",
			dataIndex : 'power',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "操作",
			dataIndex : "c_agree",
			width : 160,
			renderer : function(value, cellmeta, record) {
				if ("地市级代理" === record.data.power) {
					return '<a href="#" onclick="clickShowInfo(\''
						+ record.data.id + '\')">团队明细</a>';
				} else if ("区域经理" === record.data.power) {
					return '<a href="#" onclick="salerList(\''
						+ record.data.id + '\')">业务员列表</a> | <a href="#" onclick="riderList(\''
						+ record.data.id + '\')">骑手列表</a> ';
				} else {
					return '无操作';
				}
			}
		}
	]);


	var monRecordMangerGrid = new Ext.grid.EditorGridPanel({
		ds : monRecordDss,
		cm : monRecordCm,
		sm : monRecordCbsm,
		closable : true,
		border : false,
		id : 'monRecordManager',
		stripeRows : true,
		height : document.body.clientHeight - 106,
		bbar : new Ext.PagingToolbar({
			store : monRecordDss,
			displayInfo : true,
			displayMsg : ' 显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : '没有记录'
		}),
		tbar : [ '销售员联系方式', '-', new Ext.form.TextField({
			width : 100,
			id : 'phone'
		}), '城市', '-', new Ext.form.TextField({
			width : 100,
			id : 'city'
		}), '&nbsp;职务&nbsp;', '-', '&nbsp;&nbsp;&nbsp;', new Ext.form.ComboBox({
			typeAhead : true,
			triggerAction : 'all',
			lazyRender : true,
			mode : 'local',
			id : 'power',
			width : 80,
			store : new Ext.data.ArrayStore({
				id : 0,
				fields : [ 'statuscode', 'statusDesc' ],
				data : [ [ '0', '全部' ], [ '3', '业务员' ], [ '2', '区域经理' ], [ '1', '地市级代理' ] ]
			}),
			valueField : 'statuscode',
			displayField : 'statusDesc'
		}), '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;状态&nbsp;&nbsp;&nbsp;', '-', '&nbsp;&nbsp;&nbsp;', new Ext.form.ComboBox({
			typeAhead : true,
			triggerAction : 'all',
			lazyRender : true,
			mode : 'local',
			id : 'score',
			width : 80,
			store : new Ext.data.ArrayStore({
				id : 0,
				fields : [ 'statuscode', 'statusDesc' ],
				data : [ [ '', '全部' ], [ '1', 'A级' ], [ '2', 'B级' ], [ '3', 'C级' ], [ '4', 'D级' ] ]
			}),
			valueField : 'statuscode',
			displayField : 'statusDesc'
		}), '&nbsp;', {
			text : '查询',
			iconCls : 'addUser',
			handler : function() {
				monRecordDss.baseParams = {
					phone : Ext.getCmp('phone').getValue(),
					power : Ext.getCmp('power').getValue(),
					city : Ext.getCmp('city').getValue(),
					score : Ext.getCmp('score').getValue(),
					start : 0,
					limit : 20
				};

				monRecordDss.load({
					params : {
						phone : Ext.getCmp('phone').getValue(),
						power : Ext.getCmp('power').getValue(),
						city : Ext.getCmp('city').getValue(),
						score : Ext.getCmp('score').getValue(),
						start : 0,
						limit : 20
					}
				});
			}
		}
		]
	});

	monRecordDss.load({
		params : {
			start : 0,
			limit : 20
		}
	});

	monRecordMangerGrid.render('grid-monRecordManager');
});

//添加单击事件
function clickShowInfo(id) {
	Ext.Ajax.request({
		url : BASE_URL + LOGIN_ACTION.SALER_TEAM_COUNT,
		waitMsg : '处理中，请稍等..',
		waitTitle : '请稍等',
		params : {
			id : id
		},
		success : function(response, options) {
			Ext.getCmp('monRecordManager').getStore().reload();
			var json = Ext.util.JSON.decode(response.responseText);
			var list = json.list
			var table = [];
			for (var i = 0; i < list.length; i++) {
				var content = [
					list[i].id,
					list[i].name,
					list[i].phone,
					list[i].cardNo,
					list[i].selfNum,
					list[i].num,
					list[i].power,
					list[i].mainNum,
					list[i].deputyNum,
					list[i].otherMainNum,
					list[i].otherDeputyNum
				];
				table.push(content);
			}

			var store = new Ext.data.SimpleStore({
				fields : [
					{
						name : 'id'
					}, {
						name : 'name'
					}, {
						name : 'phone'
					}, {
						name : 'cardNo'
					}, {
						name : 'selfNum'
					}, {
						name : 'count'
					}, {
						name : 'power'
					}, {
						name : 'mainNum'
					}, {
						name : 'deputyNum'
					}, {
						name : 'otherMainNum'
					}, {
						name : 'otherDeputyNum'
					}
				]
			});
			store.loadData(table);
			var info = new Ext.grid.GridPanel({
				store : store,
				columns : [
					{
						id : 'team',
						header : "id",
						width : 60,
						sortable : true,
						locked : true,
						hidden : true,
						dataIndex : 'id'
					}, {
						header : "姓名",
						width : 60,
						sortable : true,
						dataIndex : 'name'
					}, {
						header : "联系方式",
						width : 80,
						sortable : true,
						dataIndex : 'phone'
					}, {
						header : "身份证号码",
						width : 130,
						sortable : true,
						dataIndex : 'cardNo'
					}, {
						header : "个人主营绩效",
						width : 80,
						sortable : true,
						dataIndex : 'mainNum'
					}, {
						header : "个人副营绩效",
						width : 80,
						sortable : true,
						dataIndex : 'deputyNum'
					}, {
						header : "个人商家绩效",
						width : 80,
						sortable : true,
						dataIndex : 'selfNum'
					}, {
						header : "团队主营绩效",
						width : 80,
						sortable : true,
						dataIndex : 'otherMainNum'
					}, {
						header : "团队副营绩效",
						width : 80,
						sortable : true,
						dataIndex : 'otherDeputyNum'
					}, {
						header : "团队商家绩效",
						width : 80,
						sortable : true,
						dataIndex : 'count'
					}, {
						header : "职务",
						width : 80,
						sortable : true,
						dataIndex : 'power'
					}, {
						header : "操作",
						dataIndex : "c_agree",
						width : 80,
						renderer : function(value, cellmeta, record) {
							if ("区域经理" === record.data.power) {
								return '<a href="#" onclick="salerList(\''
									+ record.data.id + '\')">业务员列表</a>';
							} else {
								return '无操作';
							}
						}
					}
				],
				stripeRows : true,
				autoExpandColumn : 'team',
				height : 400,
				width : 800,
			});

			var changepasswordWin = new Ext.Window({
				title : '团队明细',
				width : 800,
				height : 400,
				minWidth : 800,
				minHeight : 400,
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

//添加单击事件
function salerList(id) {
	Ext.Ajax.request({
		url : BASE_URL + LOGIN_ACTION.SALER_TEAM_COUNT,
		waitMsg : '处理中，请稍等..',
		waitTitle : '请稍等',
		params : {
			id : id
		},
		success : function(response, options) {
			Ext.getCmp('monRecordManager').getStore().reload();
			var json = Ext.util.JSON.decode(response.responseText);
			var list = json.list
			var table = [];
			for (var i = 0; i < list.length; i++) {
				var content = [
					list[i].id,
					list[i].name,
					list[i].phone,
					list[i].cardNo,
					list[i].num,
					list[i].power,
					list[i].mainNum,
					list[i].deputyNum
				];
				table.push(content);
			}

			var store = new Ext.data.SimpleStore({
				fields : [
					{
						name : 'id'
					}, {
						name : 'name'
					}, {
						name : 'phone'
					}, {
						name : 'cardNo'
					}, {
						name : 'count'
					}, {
						name : 'power'
					}, {
						name : 'mainNum'
					}, {
						name : 'deputyNum'
					}
				]
			});
			store.loadData(table);
			var info = new Ext.grid.GridPanel({
				store : store,
				columns : [
					{
						id : 'team',
						header : "id",
						width : 60,
						sortable : true,
						locked : true,
						hidden : true,
						dataIndex : 'id'
					}, {
						header : "姓名",
						width : 60,
						sortable : true,
						dataIndex : 'name'
					}, {
						header : "联系方式",
						width : 80,
						sortable : true,
						dataIndex : 'phone'
					}, {
						header : "身份证号码",
						width : 130,
						sortable : true,
						dataIndex : 'cardNo'
					}, {
						header : "当月主营绩效",
						width : 80,
						sortable : true,
						dataIndex : 'mainNum'
					}, {
						header : "当月副营绩效",
						width : 80,
						sortable : true,
						dataIndex : 'deputyNum'
					}, {
						header : "当月商家绩效",
						width : 80,
						sortable : true,
						dataIndex : 'count'
					}, {
						header : "职务",
						width : 80,
						sortable : true,
						dataIndex : 'power'
					}
				],
				stripeRows : true,
				autoExpandColumn : 'team',
				height : 300,
				width : 600,
			});

			var changepasswordWin = new Ext.Window({
				title : '业务员绩效明细',
				width : 600,
				height : 300,
				minWidth : 600,
				minHeight : 300,
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

//添加单击事件
function riderList(id) {
	Ext.Ajax.request({
		url : BASE_URL + LOGIN_ACTION.RIDER_INFO_LIST,
		waitMsg : '处理中，请稍等..',
		waitTitle : '请稍等',
		params : {
			bossId : id
		},
		success : function(response, options) {
			Ext.getCmp('monRecordManager').getStore().reload();
			var json = Ext.util.JSON.decode(response.responseText);
			var list = json.list
			var table = [];
			for (var i = 0; i < list.length; i++) {
				var content = [
					list[i].id,
					list[i].name,
					list[i].phone,
					list[i].cardNo,
					list[i].city,
					list[i].num,
					list[i].balance,
					list[i].score
				];
				table.push(content);
			}

			var store = new Ext.data.SimpleStore({
				fields : [
					{
						name : 'id'
					}, {
						name : 'name'
					}, {
						name : 'phone'
					}, {
						name : 'cardNo'
					}, {
						name : 'city'
					}, {
						name : 'num'
					}, {
						name : 'balance'
					},{
						name : 'score'
					}
				]
			});
			store.loadData(table);
			var info = new Ext.grid.GridPanel({
				store : store,
				columns : [
					{
						id : 'team',
						header : "id",
						width : 60,
						sortable : true,
						locked : true,
						hidden : true,
						dataIndex : 'id'
					}, {
						header : "姓名",
						width : 60,
						sortable : true,
						dataIndex : 'name'
					}, {
						header : "联系方式",
						width : 80,
						sortable : true,
						dataIndex : 'phone'
					}, {
						header : "月订单量",
						width : 60,
						sortable : true,
						dataIndex : 'num'
					}, {
						header : "月收益",
						width : 80,
						sortable : true,
						dataIndex : 'balance'
					}, {
						header : "综合评分",
						width : 60,
						sortable : true,
						dataIndex : 'score'
					}, {
						header : "身份证号码",
						width : 130,
						sortable : true,
						dataIndex : 'cardNo'
					}, {
						header : "城市",
						width : 80,
						sortable : true,
						dataIndex : 'city'
					}
				],
				stripeRows : true,
				autoExpandColumn : 'team',
				height : 300,
				width : 600,
			});

			var changepasswordWin = new Ext.Window({
				title : '骑手列表',
				width : 600,
				height : 300,
				minWidth : 600,
				minHeight : 300,
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