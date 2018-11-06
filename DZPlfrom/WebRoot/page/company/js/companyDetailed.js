Ext.onReady(function() {
	var companyDetailedDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.COMPANY_DETAILED_VIEW
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'id',
			successProperty : '@success',
			fields : [ 'id', 'companyId', 'salerId', 'salerName', 'salerPhone', 'companyName', 'companyPhone', 'city', 'address',
				'cost', 'RemainTime', 'expireTime' ]
		}),
	});

	var companyDetailedCbsm = new Ext.grid.CheckboxSelectionModel();
	var companyDetailedCm = new Ext.grid.ColumnModel([
		companyDetailedCbsm, {
			header : 'id',
			dataIndex : 'id',
			locked : true,
			hidden : true,
			width : (document.body.clientWidth - 193) / 18
		},{
			header : 'companyId',
			dataIndex : 'companyId',
			locked : true,
			hidden : true,
			width : (document.body.clientWidth - 193) / 18
		},{
			header : 'salerId',
			dataIndex : 'salerId',
			locked : true,
			hidden : true,
			width : (document.body.clientWidth - 193) / 18
		}, {
			header : '商家名称',
			dataIndex : 'companyName',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '商家联系方式',
			dataIndex : 'companyPhone',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '业务员名称',
			dataIndex : 'salerName',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '业务员联系方式',
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
			header : "到期时间",
			dataIndex : 'expireTime',
			width : (document.body.clientWidth - 193) / 20,
		}, {
			header : "剩余时间",
			dataIndex : 'RemainTime',
			width : (document.body.clientWidth - 193) / 20,
		}, {
			header : "商家地址",
			dataIndex : 'address',
			width : (document.body.clientWidth - 193) / 20,
		}, {
			header : "年费费用",
			dataIndex : 'cost',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "操作",
			dataIndex : "c_agree",
			width : 80,
			renderer : function(value, cellmeta, record) {
				return '<a href="#" onclick="clickShowInfo(\''
					+ record.data.companyId + '\')">缴费记录</a>';
			}
		}
	]);


	var companyDetailedMangerGrid = new Ext.grid.EditorGridPanel({
		ds : companyDetailedDss,
		cm : companyDetailedCm,
		sm : companyDetailedCbsm,
		closable : true,
		border : false,
		id : 'companyDetailedManager',
		stripeRows : true,
		height : document.body.clientHeight - 106,
		bbar : new Ext.PagingToolbar({
			store : companyDetailedDss,
			displayInfo : true,
			displayMsg : ' 显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : '没有记录'
		}),
		tbar : [ '销售员联系方式', '-', new Ext.form.TextField({
			width : 100,
			id : 'salerPhone'
		}),'&nbsp;&nbsp;&nbsp;城市&nbsp;&nbsp;&nbsp;', '-', new Ext.form.TextField({
			width : 100,
			id : 'city'
		}),'&nbsp;', {
			text : '查询',
			iconCls : 'addUser',
			handler : function() {
				companyDetailedDss.baseParams = {
					salerPhone : Ext.getCmp('salerPhone').getValue(),
					salerCity : Ext.getCmp('city').getValue(),
					start : 0,
					limit : 20
				};

				companyDetailedDss.load({
					params : {
						salerPhone : Ext.getCmp('salerPhone').getValue(),
						salerCity : Ext.getCmp('city').getValue(),
						start : 0,
						limit : 20
					}
				});
			}
		}, '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;开始时间&nbsp;&nbsp;&nbsp;', '-', '&nbsp;&nbsp;&nbsp;', new Ext.form.DateField({
			width : document.body.clientWidth / 20,
			format : 'Y-m-d',
			id : 'startDate'
		}), '&nbsp;&nbsp;&nbsp;结束时间&nbsp;&nbsp;&nbsp;', '-', '&nbsp;&nbsp;&nbsp;', new Ext.form.DateField({
			width : document.body.clientWidth / 20,
			format : 'Y-m-d',
			id : 'endDate'
		}),'</br>\n', '&nbsp;&nbsp;', {
			text : '导出',
			iconCls : 'addUser',
			handler : function() {
				companyExport(Ext.getCmp('startDate').getValue(), Ext.getCmp('endDate').getValue(), Ext.getCmp('city').getValue());
			}
		}
		]
	});

	companyDetailedDss.load({
		params : {
			start : 0,
			limit : 20
		}
	});

	companyDetailedMangerGrid.render('grid-companyDetailedManager');
});

//缴费记录-添加单击事件
function clickShowInfo(id) {
	Ext.Ajax.request({
		url : BASE_URL + LOGIN_ACTION.COMPANY_PAY_COMPANYVIEW,
		waitMsg : '处理中，请稍等..',
		waitTitle : '请稍等',
		params : {
			companyId : id
		},
		success : function(response, options) {
			Ext.getCmp('companyDetailedManager').getStore().reload();
			var json = Ext.util.JSON.decode(response.responseText);
			var list = json.list
			var table = [];
			for (var i = 0; i < list.length; i++) {
				var content = [ list[i].name, 
				                list[i].orderNo, 
				                list[i].balance,
				                list[i].date, 
				                list[i].renewTime, 
				                list[i].isAccount, 
				                ];
				table.push(content);
			}

			var store = new Ext.data.SimpleStore({
				fields : [
					{
						name : 'companyName'
					},
					{
						name : 'orderNo'
					},
					{
						name : 'payCount'
					},
					{
						name : 'payTime'
					},
					{
						name : 'renewTime'
					},
					{
						name : 'isAccount'
					}
				]
			});
			store.loadData(table);
			var info = new Ext.grid.GridPanel({
				store : store,
				columns : [
				{
						id : 'team',
						header : "商家名称",
						width : 150,
						sortable : true,
						dataIndex : 'companyName'
					},
					{
						header : "订单号",
						width : 175,
						sortable : true,
						dataIndex : 'orderNo'
					},
					{
						header : "支付金额",
						width : 100,
						sortable : true,
						dataIndex : 'payCount'
					},
					{
						header : "支付时间",
						width : 175,
						sortable : true,
						dataIndex : 'payTime'
					},
					{
						header : "续费时长",
						width : 100,
						sortable : true,
						dataIndex : 'renewTime'
					},
					{
						header : "是否到账",
						width : 100,
						sortable : true,
						dataIndex : 'isAccount'
					}
				],
				stripeRows : true,
				autoExpandColumn : 'team',
				height : 400,
				width : 800,
			});

			var changepasswordWin = new Ext.Window({
				title : '缴费记录',
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
				},'</br>\n', '&nbsp;&nbsp;', {
					text : '导出',
					iconCls : 'addUser',
					handler : function() {
						companyPayExport(id);
					}
				}
				]
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

//工具栏(导出)按钮-添加单击事件
function companyExport(startDate, endDate, city) {
	Ext.MessageBox.show({
		title : '温馨提示',
		msg : '正在导出文件，请稍等！',
		icon : Ext.MessageBox.WARNING
	});
	Ext.Ajax.request({
		url : BASE_URL + LOGIN_ACTION.COMPANYDETAILED_EXPORT,
		waitMsg : '处理中，请稍等..',
		waitTitle : '请稍等',
		params : {
			startTime : startDate,
			endTime : endDate,
			city : city,
		},
		success : function(response, options) {
			Ext.getCmp('companyDetailedManager').getStore().reload();
			var json = Ext.util.JSON.decode(response.responseText);
			window.location.href = BASE_URL + json.path;	//ynw
			Ext.MessageBox.show({
				title : '温馨提示',
				msg : json.message,
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.INFO
			});
		},
		failure : function(form, action) {
			var json = Ext.util.JSON.decode(action.responseText);
			Ext.MessageBox.show({
				title : '温馨提示',
				msg : json.message,
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.ERROR
			});
		}
	});
}

//缴费记录(导出)按钮-添加单击事件
function companyPayExport(id) {
	Ext.MessageBox.show({
		title : '温馨提示',
		msg : '正在导出文件，请稍等！',
		icon : Ext.MessageBox.WARNING
	});
	Ext.Ajax.request({
		url : BASE_URL + LOGIN_ACTION.COMPANYPAYRECORD_EXPORT,
		waitMsg : '处理中，请稍等..',
		waitTitle : '请稍等',
		params : {
			companyId : id
		},
		success : function(response, options) {
			Ext.getCmp('companyDetailedManager').getStore().reload();
			var json = Ext.util.JSON.decode(response.responseText);
			window.location.href = BASE_URL + json.path;	//ynw
			Ext.MessageBox.show({
				title : '温馨提示',
				msg : json.message,
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.INFO
			});
		},
		failure : function(form, action) {
			var json = Ext.util.JSON.decode(action.responseText);
			Ext.MessageBox.show({
				title : '温馨提示',
				msg : json.message,
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.ERROR
			});
		}
	});
}