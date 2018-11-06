Ext.onReady(function() {
	var totalRecordDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.SALER_TOTAL_COUNT
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'id',
			successProperty : '@success',
			fields : [ 'selfNum', 'otherNum', 'totalNum', 'salerName', 'salerPhone', 'id', 'city' ]
		}),
	});

	var totalRecordCbsm = new Ext.grid.CheckboxSelectionModel();
	var totalRecordCm = new Ext.grid.ColumnModel([
		totalRecordCbsm, {
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
			header : "个人绩效",
			dataIndex : 'selfNum',
			width : (document.body.clientWidth - 193) / 15,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "团队绩效",
			dataIndex : 'otherNum',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "当月总计",
			dataIndex : 'totalNum',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}
	]);


	var totalRecordMangerGrid = new Ext.grid.EditorGridPanel({
		ds : totalRecordDss,
		cm : totalRecordCm,
		sm : totalRecordCbsm,
		closable : true,
		border : false,
		id : 'totalRecordManager',
		stripeRows : true,
		height : document.body.clientHeight - 106,
		bbar : new Ext.PagingToolbar({
			store : totalRecordDss,
			displayInfo : true,
			displayMsg : ' 显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : '没有记录'
		}),
		tbar : [ '联系方式', '-', new Ext.form.TextField({
			width : 100,
			id : 'phone'
		}),'&nbsp;&nbsp;&nbsp;城市&nbsp;&nbsp;&nbsp;', '-', new Ext.form.TextField({
			width : 100,
			id : 'city'
		}), '&nbsp;', {
			text : '查询',
			iconCls : 'addUser',
			handler : function() {
				totalRecordDss.baseParams = {
					phone : Ext.getCmp('phone').getValue(),
					start : 0,
					limit : 20
				};

				totalRecordDss.load({
					params : {
						phone : Ext.getCmp('phone').getValue(),
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
		}), '</br>\n', '&nbsp;&nbsp;', {
			text : '导出',
			iconCls : 'addUser',
			handler : function() {
				salerExport(Ext.getCmp('startDate').getValue(), Ext.getCmp('endDate').getValue(), Ext.getCmp('city').getValue());
			}
		}
		]
	});

	totalRecordDss.load({
		params : {
			start : 0,
			limit : 20
		}
	});

	totalRecordMangerGrid.render('grid-totalRecordManager');
});

//添加单击事件
function salerExport(startDate, endDate, city) {
	Ext.MessageBox.show({
		title : '温馨提示',
		msg : '正在导出对账文件，请稍等！',
		icon : Ext.MessageBox.WARNING
	});
	Ext.Ajax.request({
		url : BASE_URL + LOGIN_ACTION.SALER_EXPORT,
		waitMsg : '处理中，请稍等..',
		waitTitle : '请稍等',
		params : {
			startTime : startDate,
			endTime : endDate,
			city : city,
		},
		success : function(response, options) {
			Ext.getCmp('totalRecordManager').getStore().reload();
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