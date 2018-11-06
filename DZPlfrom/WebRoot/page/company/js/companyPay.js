Ext.onReady(function() {
	var companyPayDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.COMPANY_PAY_VIEW
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'id',
			successProperty : '@success',
			fields : [ 'id', 'date', 'name', 'phone', 'address', 'balance', 'renewTime' ]
		}),
	});

	var companyPayCbsm = new Ext.grid.CheckboxSelectionModel();
	var companyPayCm = new Ext.grid.ColumnModel([
		companyPayCbsm, {
			header : 'id',
			dataIndex : 'id',
			locked : true,
			hidden : true,
			width : (document.body.clientWidth - 193) / 18
		}, {
			header : '商家名称',
			dataIndex : 'name',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '联系方式',
			dataIndex : 'phone',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '提交时间',
			dataIndex : 'date',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '缴费金额',
			dataIndex : 'balance',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "续费时长",
			dataIndex : 'renewTime',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "商家地址",
			dataIndex : 'address',
			width : (document.body.clientWidth - 193) / 20,
		}
	]);


	var companyPayMangerGrid = new Ext.grid.EditorGridPanel({
		ds : companyPayDss,
		cm : companyPayCm,
		sm : companyPayCbsm,
		closable : true,
		border : false,
		id : 'companyPayManager',
		stripeRows : true,
		height : document.body.clientHeight - 106,
		bbar : new Ext.PagingToolbar({
			store : companyPayDss,
			displayInfo : true,
			displayMsg : ' 显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : '没有记录'
		}),
		tbar : [ '商家联系方式', '-', new Ext.form.TextField({
			width : 100,
			id : 'phone'
		}), '&nbsp;', {
			text : '查询',
			iconCls : 'addUser',
			handler : function() {
				companyPayDss.baseParams = {
					phone : Ext.getCmp('phone').getValue(),
					start : 0,
					limit : 20
				};

				companyPayDss.load({
					params : {
						phone : Ext.getCmp('phone').getValue(),
						start : 0,
						limit : 20
					}
				});
			}
		}, '</br>\n', '&nbsp;&nbsp;', {
			text : '通过审核',
			iconCls : 'addUser',
			handler : function() {
				Ext.MessageBox.confirm('消息', '确定要通过审核吗?', apply);
			}
		}
		]
	});

	function apply(btn) {
		if (btn == 'yes') {
			var b = "";
			var rends = companyPayMangerGrid.getSelectionModel().getSelections();
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
					url : BASE_URL + LOGIN_ACTION.COMPANY_PAY_APPLY,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : b,
					},
					success : function(form, action) {
						companyPayDss.reload();
						var json = Ext.util.JSON.decode(form.responseText);
						Ext.Msg.alert('温馨提示', json.message);
					},
					failure : function(form, action) {
						var json = Ext.util.JSON.decode(form.responseText);
						Ext.Msg.alert('温馨提示', json.message);
					}
				});
			} else {
				Ext.Msg.alert('温馨提示', '请选择一条需要审核的数据!');
			}
		}
	}
	
	companyPayDss.load({
		params : {
			start : 0,
			limit : 20
		}
	});

	companyPayMangerGrid.render('grid-companyPayManager');
});