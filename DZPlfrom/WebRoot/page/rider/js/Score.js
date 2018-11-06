Ext.onReady(function() {
	var ScoreDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.RIDER_MULTIPLE
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'runid',
			successProperty : '@success',
			fields : [ 'id', 'avg_manner', 'avg_speen', 'runid', 'order_count', 'user_phone', 'user_name', 'user_nickname', 'username']
		}),
		listeners : {
			load : function() {
			Ext.getCmp('userCount').setText(this.reader.jsonData.totalCount)
		}
	}
	});
	
	var ScoreCbsm = new Ext.grid.CheckboxSelectionModel();
	var ScoreCm = new Ext.grid.ColumnModel([
	    ScoreCbsm,{
			header : 'id',
			dataIndex : 'runid',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
			allowBlank : false
			})
	    },{
			header : '跑腿员姓名',
			dataIndex : 'user_name',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
			allowBlank : false
			})
	    },{
			header : '联系方式',
			dataIndex : 'user_phone',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
			allowBlank : false
			})
	    },{
			header : '跑腿员昵称',
			dataIndex : 'user_nickname',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
			allowBlank : false
			})
	    },{
			header : '跑腿员账号',
			dataIndex : 'username',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
			allowBlank : false
			})
	    },{
			header : '服务质量综合评分',
			dataIndex : 'avg_manner',
			width : (document.body.clientWidth - 193) / 10,
			editor : new Ext.form.TextField({
			allowBlank : false
			})
	    },{
			header : '配送速度综合评分',
			dataIndex : 'avg_speen',
			width : (document.body.clientWidth - 193) / 10,
			editor : new Ext.form.TextField({
			allowBlank : false
			})
	    },{
			header : '配送订单总数',
			dataIndex : 'order_count',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
			allowBlank : false
			})
	    }
	                                        
	]);
	    
	var ScoreMangerGrid = new Ext.grid.EditorGridPanel({
		ds :  ScoreDss,
		cm :  ScoreCm,
		sm :  ScoreCbsm,
		closable : true,
		border : false,
		id : 'ScoreManager',
		stripeRows : true,
		height : document.body.clientHeight - 106,
		tbar : [ /*'骑手账号', '-', new Ext.form.TextField({
			width : 100,
			id : 'userName'
		}),*/'骑手姓名', '-', new Ext.form.TextField({
			width : 100,
			id : 'user_Name'
		}),'骑手联系方式', '-', new Ext.form.TextField({
			width : 100,
			id : 'user_Phone'
		}),'&nbsp;', {
			text : '查询',
			iconCls : 'addUser',
			handler : function() {
				ScoreDss.baseParams = {
					user_Name : Ext.getCmp('user_Name').getValue(),
					user_Phone : Ext.getCmp('user_Phone').getValue(),
					start : 0,
					limit : 20
				};

				ScoreDss.load({
					params : {
						user_Name: Ext.getCmp('user_Name').getValue(),
						user_Phone: Ext.getCmp('user_Phone').getValue(),
						start : 0,
						limit : 20
					}
				});
			}
		},'&nbsp;&nbsp;骑手总数:&nbsp;', new Ext.form.Label({
			width : 60,
			id : 'userCount'
		})
		],
		bbar : new Ext.PagingToolbar({
			store : ScoreDss,
			displayInfo : true,
			displayMsg : ' 显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : '没有记录',
		})
	});
	
	ScoreDss.load({
		params : {
			start : 0,
			limit : 20
		}
	});

	ScoreMangerGrid.render('grid-ScoreManager');
});
/*Ext.onReady(function() {
	var ScoreDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.RIDER_MULTIPLE
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'id',
			successProperty : '@success',
			fields : [ 'id', 'avg_manner', 'avg_speen', 'runid', 'userid', 'orderid']
		}),
	});
	
	var ScoreCbsm = new Ext.grid.CheckboxSelectionModel();
	var ScoreCm = new Ext.grid.ColumnModel([
	    ScoreCbsm,{
			header : 'id',
			dataIndex : 'id',
			locked : true,
			hidden : true,
			width : (document.body.clientWidth - 193) / 18
	    },{
			header : '跑腿员ID',
			dataIndex : 'runid',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
			allowBlank : false
			})
	    },{
			header : '用户ID',
			dataIndex : 'userid',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
			allowBlank : false
			})
	    },{
			header : '服务质量综合评分',
			dataIndex : 'avg_manner',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
			allowBlank : false
			})
	    },{
			header : '配送速度综合评分',
			dataIndex : 'avg_speen',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
			allowBlank : false
			})
	    }
	                                        
	]);
	    
	var ScoreMangerGrid = new Ext.grid.EditorGridPanel({
		ds :  ScoreDss,
		cm :  ScoreCm,
		sm :  ScoreCbsm,
		closable : true,
		border : false,
		id : 'ScoreManager',
		stripeRows : true,
		height : document.body.clientHeight - 106,
		bbar : new Ext.PagingToolbar({
			store : ScoreDss,
			displayInfo : true,
			displayMsg : ' 显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : '没有记录',
			handler : function() {
				ScoreDss.baseParams = {
					start : 0,
					limit : 20
				};

				ScoreDss.load({
					params : {
						start : 0,
						limit : 20
					}
				});
			}
		})
	});
	
	ScoreDss.load({
		params : {
			start : 0,
			limit : 20
		}
	});

	ScoreMangerGrid.render('grid-ScoreManager');
});*/