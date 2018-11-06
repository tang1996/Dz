Ext.onReady(function() {
	var feedbacklistDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.FEEDBACK_VIEW
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'id',
			successProperty : '@success',
			fields : [ 'id', 'phone', 'content', 'create_time' ]
		}),
	});

	var feedbacklistCbsm = new Ext.grid.CheckboxSelectionModel();
	var feedbacklistCm = new Ext.grid.ColumnModel([
		feedbacklistCbsm, {
			header : 'id',
			dataIndex : 'id',
			locked : true,
			hidden : true,
			width : (document.body.clientWidth - 193) / 20
		}, {
			header : '联系方式',
			dataIndex : 'phone',
			width : (document.body.clientWidth - 193) / 15,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '反馈信息',
			dataIndex : 'content',
			width : (document.body.clientWidth - 193) / 2,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '反馈时间',
			dataIndex : 'create_time',
			width : (document.body.clientWidth - 193) / 12,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}
	]);


	var feedbacklistMangerGrid = new Ext.grid.EditorGridPanel({
		ds : feedbacklistDss,
		cm : feedbacklistCm,
		sm : feedbacklistCbsm,
		closable : true,
		border : false,
		id : 'feedbacklistManager',
		stripeRows : true,
		height : document.body.clientHeight - 106,
		bbar : new Ext.PagingToolbar({
			store : feedbacklistDss,
			displayInfo : true,
			displayMsg : ' 显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : '没有记录'
		}),
		tbar : [
			
		]
	});

	feedbacklistDss.load({
		params : {
			start : 0,
			limit : 20
		}
	});

	feedbacklistMangerGrid.render('grid-feedbacklistManager');
});