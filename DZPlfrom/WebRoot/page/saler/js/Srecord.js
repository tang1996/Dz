Ext.onReady(function() {
	var SrecordDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.SALER_COMPANY
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'id',
			successProperty : '@success',
			fields : [ 'id', 'createTime', 'salerName', 'salerPhone', 'companyName', 'city', 'companyPhone', 'address' ]
		}),
		listeners: { load: function () { Ext.getCmp('totalSum').setText(this.reader.jsonData.totalSum)} }
	});

	var SrecordCbsm = new Ext.grid.CheckboxSelectionModel();
	var SrecordCm = new Ext.grid.ColumnModel([
		SrecordCbsm, {
			header : 'id',
			dataIndex : 'id',
			locked : true,
			hidden : true,
			width : (document.body.clientWidth - 193) / 18
		}, {
			header : '推广员姓名',
			dataIndex : 'salerName',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '商家名称',
			dataIndex : 'companyName',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "推广员联系方式",
			dataIndex : 'salerPhone',
			width : (document.body.clientWidth - 193) / 15,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "所在城市",
			dataIndex : 'city',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "商家联系方式",
			dataIndex : 'companyPhone',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "入驻时间",
			dataIndex : 'createTime',
			width : (document.body.clientWidth - 193) / 10,
		}
		, {
			header : "详细地址",
			dataIndex : 'address',
			width : (document.body.clientWidth - 193) / 5,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}
	]);


	var SrecordMangerGrid = new Ext.grid.EditorGridPanel({
		ds : SrecordDss,
		cm : SrecordCm,
		sm : SrecordCbsm,
		closable : true,
		border : false,
		id : 'SrecordManager',
		stripeRows : true,
		height : document.body.clientHeight - 106,
		bbar : new Ext.PagingToolbar({
			store : SrecordDss,
			displayInfo : true,
			displayMsg : ' 显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : '没有记录'
		}),
		tbar : [ '销售员联系方式', '-', new Ext.form.TextField({
			width : 100,
			id : 'phone'
		}),'商家id', '-', new Ext.form.TextField({
			width : 100,
			id : 'companyId'
		}), '&nbsp;', {
			text : '查询',
			iconCls : 'addUser',
			handler : function() {
				SrecordDss.baseParams = {
					phone : Ext.getCmp('phone').getValue(),
					companyId : Ext.getCmp('companyId').getValue(),
					start : 0,
					limit : 20
				};

				SrecordDss.load({
					params : {
						phone : Ext.getCmp('phone').getValue(),
						companyId : Ext.getCmp('companyId').getValue(),
						start : 0,
						limit : 20
					}
				});
			}
		}, '&nbsp;&nbsp;&nbsp;总计:&nbsp;',  new Ext.form.Label({
			width:60,
			id:'totalSum'
		})
		]
	});

	SrecordDss.load({
		params : {
			start : 0,
			limit : 20
		}
	});

	SrecordMangerGrid.render('grid-SrecordManager');
});