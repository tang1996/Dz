Ext.onReady(function() {
	var collectionDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.INCLUDE_VIEW
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'id',
			successProperty : '@success',
			fields : [ 'id', 'companyName', 'companyId', 'userName', 'userId' ]
		}),
	});

	var collectionCbsm = new Ext.grid.CheckboxSelectionModel();
	var collectionCm = new Ext.grid.ColumnModel([
		collectionCbsm, {
			header : 'id',
			dataIndex : 'id',
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
		},{
			header : '商家序号',
			dataIndex : 'companyId',
			locked : true,
			hidden : true,
			width : (document.body.clientWidth - 193) / 18
		}, {
			header : '用户帐号',
			dataIndex : 'userName',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		},{
			header : '用户序号',
			dataIndex : 'userId',
			locked : true,
			hidden : true,
			width : (document.body.clientWidth - 193) / 18
		}
	]);


	var collectionMangerGrid = new Ext.grid.EditorGridPanel({
		ds : collectionDss,
		cm : collectionCm,
		sm : collectionCbsm,
		closable : true,
		border : false,
		id : 'collectionManager',
		stripeRows : true,
		height : document.body.clientHeight - 106,
		bbar : new Ext.PagingToolbar({
			store : collectionDss,
			displayInfo : true,
			displayMsg : ' 显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : '没有记录'
		}),
		tbar : [ '用户账户', '-', new Ext.form.TextField({
			width : 100,
			id : 'userName'
		}),'商家id', '-', new Ext.form.TextField({
			width : 100,
			id : 'companyId'
		}), '&nbsp;', {
			text : '查询',
			iconCls : 'addUser',
			handler : function() {

				collectionDss.baseParams = {
					userName : Ext.getCmp('userName').getValue(),
					companyId : Ext.getCmp('companyId').getValue(),
					start : 0,
					limit : 20
				};

				collectionDss.load({
					params : {
						userName : Ext.getCmp('userName').getValue(),
						companyId : Ext.getCmp('companyId').getValue(),
						start : 0,
						limit : 20
					}
				});
			}
		}
		]
	});

	collectionDss.load({
		params : {
			start : 0,
			limit : 20
		}
	});

	collectionMangerGrid.render('grid-collectionManager');
});