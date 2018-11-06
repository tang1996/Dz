Ext.onReady(function() {
	var RorderCountDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.RIDER_ORDER_COUNT
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'id',
			successProperty : '@success',
			fields : [ 'name', 'num', 'balance', 'phone' ]
		}),
	});

	var RorderCountCbsm = new Ext.grid.CheckboxSelectionModel();
	var RorderCountCm = new Ext.grid.ColumnModel([
		RorderCountCbsm, {
			header : '骑手姓名',
			dataIndex : 'name',
			width : (document.body.clientWidth - 193) / 18,
		}, {
			header : '订单数',
			dataIndex : 'num',
			width : (document.body.clientWidth - 193) / 18,
		}, {
			header : "收益",
			dataIndex : 'balance',
			width : (document.body.clientWidth - 193) / 20,
		}, {
			header : '联系方式',
			dataIndex : 'phone',
			width : (document.body.clientWidth - 193) / 10,
		}
	]);


	var RorderCountMangerGrid = new Ext.grid.EditorGridPanel({
		ds : RorderCountDss,
		cm : RorderCountCm,
		sm : RorderCountCbsm,
		closable : true,
		border : false,
		id : 'RorderCountManager',
		stripeRows : true,
		height : document.body.clientHeight - 106,
		bbar : new Ext.PagingToolbar({
			store : RorderCountDss,
			displayInfo : true,
			displayMsg : ' 显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : '没有记录'
		}),
		tbar : [ '骑手账号', '-', new Ext.form.TextField({
			width : 100,
			id : 'userName'
		}), '&nbsp;&nbsp;&nbsp;开始时间&nbsp;&nbsp;&nbsp;', '-', '&nbsp;&nbsp;&nbsp;', new Ext.form.DateField({
			width : document.body.clientWidth / 20,
			format : 'Y-m-d',
			id : 'startDate'
		}), '&nbsp;&nbsp;&nbsp;结束时间&nbsp;&nbsp;&nbsp;', '-', '&nbsp;&nbsp;&nbsp;', new Ext.form.DateField({
			width : document.body.clientWidth / 20,
			format : 'Y-m-d',
			id : 'endDate'
		}), '&nbsp;', {
			text : '查询',
			iconCls : 'addUser',
			handler : function() {
				RorderCountDss.baseParams = {
					userName : Ext.getCmp('userName').getValue(),
					city : Ext.getCmp('city').getValue(),
					startTime : Ext.getCmp('startDate').getValue(),
					endTime : Ext.getCmp('endDate').getValue(),
					start : 0,
					limit : 20
				};

				RorderCountDss.load({
					params : {
						userName : Ext.getCmp('userName').getValue(),
						city : Ext.getCmp('city').getValue(),
						startTime: Ext.getCmp('startDate').getValue(),
						endTime: Ext.getCmp('endDate').getValue(),
						start : 0,
						limit : 20
					}
				});
			}
		},'城市', '-', new Ext.form.TextField({
			width : 100,
			id : 'city'
		}), '</br>\n', '&nbsp;&nbsp;', {
			text : '导出',
			iconCls : 'addUser',
			handler : function() {
				riderExport(Ext.getCmp('startDate').getValue(), Ext.getCmp('endDate').getValue(), Ext.getCmp('city').getValue());
			}
		}
		]
	});

	RorderCountDss.load({
		params : {
			start : 0,
			limit : 20
		}
	});

	RorderCountMangerGrid.render('grid-RorderCountManager');
});

//添加单击事件
function riderExport(startDate, endDate, city) {
	Ext.MessageBox.show({
		title : '温馨提示',
		msg : '正在导出对账文件，请稍等！',
		icon : Ext.MessageBox.WARNING
	});
	Ext.Ajax.request({
		url : BASE_URL + LOGIN_ACTION.RIDER_EXPORT,
		waitMsg : '处理中，请稍等..',
		waitTitle : '请稍等',
		params : {
			startTime : startDate,
			endTime : endDate,
			city : city,
		},
		success : function(response, options) {
			Ext.getCmp('RorderCountManager').getStore().reload();
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