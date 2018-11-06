Ext.onReady(function() {
	var RorderListDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.RIDER_ORDER_LIST
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'id',
			successProperty : '@success',
			fields : [ 'userPhone', 'userName', 'userAddress', 'addTime',
				'orderId', 'orderNo', 'status', 'finishTime',
				'riderPhone', 'riderName', 'cost', 'shippingTime' ]
		}),
	});

	var RorderListCbsm = new Ext.grid.CheckboxSelectionModel();
	var RorderListCm = new Ext.grid.ColumnModel([
		RorderListCbsm, {
			header : 'orderId',
			dataIndex : 'orderId',
			locked : true,
			hidden : true,
			width : (document.body.clientWidth - 193) / 18
		}, {
			header : '骑手姓名',
			dataIndex : 'riderName',
			width : (document.body.clientWidth - 193) / 18,
		}, {
			header : '骑手联系方式',
			dataIndex : 'riderPhone',
			width : (document.body.clientWidth - 193) / 18,
		}, {
			header : "配送费",
			dataIndex : 'cost',
			width : (document.body.clientWidth - 193) / 20,
		}, {
			header : '订单号',
			dataIndex : 'orderNo',
			width : (document.body.clientWidth - 193) / 10,
		}, {
			header : '订单状态',
			dataIndex : 'status',
			width : (document.body.clientWidth - 193) / 20,
		}, {
			header : '下单时间',
			dataIndex : 'addTime',
			width : (document.body.clientWidth - 193) / 10,
		}, {
			header : '要求送达时间',
			dataIndex : 'shippingTime',
			width : (document.body.clientWidth - 193) / 10,
		}, {
			header : '送达时间',
			dataIndex : 'finishTime',
			width : (document.body.clientWidth - 193) / 10,
		}, {
			header : '用户姓名',
			dataIndex : 'userName',
			width : (document.body.clientWidth - 193) / 20,
		}, {
			header : '用户联系方式',
			dataIndex : 'userPhone',
			width : (document.body.clientWidth - 193) / 20,
		}, {
			header : '用户地址',
			dataIndex : 'userAddress',
			width : (document.body.clientWidth - 193) / 20,
		}
	]);


	var RorderListMangerGrid = new Ext.grid.EditorGridPanel({
		ds : RorderListDss,
		cm : RorderListCm,
		sm : RorderListCbsm,
		closable : true,
		border : false,
		id : 'RorderListManager',
		stripeRows : true,
		height : document.body.clientHeight - 106,
		bbar : new Ext.PagingToolbar({
			store : RorderListDss,
			displayInfo : true,
			displayMsg : ' 显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : '没有记录'
		}),
		tbar : [ '骑手电话', '-', new Ext.form.TextField({
			width : 100,
			id : 'userName'
		}), '&nbsp;', {
			text : '查询',
			iconCls : 'addUser',
			handler : function() {

				RorderListDss.baseParams = {
					userName : Ext.getCmp('userName').getValue(),
					start : 0,
					limit : 20
				};

				RorderListDss.load({
					params : {
						userName : Ext.getCmp('userName').getValue(),
						start : 0,
						limit : 20
					}
				});
			}
		}
		]
	});

	RorderListDss.load({
		params : {
			start : 0,
			limit : 20
		}
	});

	RorderListMangerGrid.render('grid-RorderListManager');
});