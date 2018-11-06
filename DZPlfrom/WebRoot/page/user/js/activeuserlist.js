Ext.onReady(function() {
	var activeUserDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.USER_ACTIVEUSER
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'id',
			successProperty : '@success',
			fields : [ 'payCount', 'username',  'name', 'nickname',
			           'phone','orderCount',
			      ]
		}),	
		listeners : {
			load : function() {
			Ext.getCmp('activeUserCount').setText(this.reader.jsonData.totalCount)
			}
		}
	});
	
	var activeUserCbsm = new Ext.grid.CheckboxSelectionModel();
	var activeUserCm = new Ext.grid.ColumnModel([
 		activeUserCbsm, {
			header : '帐号',
			dataIndex : 'username',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		},{
			header : '姓名',
			dataIndex : 'name',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		},{
			header : "昵称",
			dataIndex : 'nickname',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		},{
			header : "联系方式",
			dataIndex : 'phone',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		},{
			header : "订单总数",
			dataIndex : 'orderCount',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		},{
			header : "支付总金额",
			dataIndex : 'payCount',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}                                                                          
	]);
	                                             
	var activeUserMangerGrid = new Ext.grid.EditorGridPanel({
		ds : activeUserDss,
		cm : activeUserCm,
		sm : activeUserCbsm,
		closable : true,
		border : false,
		id : 'activeUserManager',
		stripeRows : true,
		height : document.body.clientHeight - 106,
		bbar : new Ext.PagingToolbar({
			store : activeUserDss,
			displayInfo : true,
			displayMsg : ' 显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : '没有记录'
		}),
		tbar : [ '&nbsp;&nbsp;&nbsp;开始时间&nbsp;&nbsp;&nbsp;', '-', '&nbsp;&nbsp;&nbsp;', new Ext.form.DateField({
			width : document.body.clientWidth / 20,
			format : 'Y-m-d',
			id : 'startTime'
		}),'&nbsp;&nbsp;&nbsp;结束时间&nbsp;&nbsp;&nbsp;', '-', '&nbsp;&nbsp;&nbsp;', new Ext.form.DateField({
			width : document.body.clientWidth / 20,
			format : 'Y-m-d',
			id : 'endTime'
		}), '&nbsp;', {
			text : '查询',
			iconCls : 'addUser',
			handler : function() {
				activeUserDss.baseParams = {
					startTime : Ext.getCmp('startTime').getValue(),
					endTime : Ext.getCmp('endTime').getValue(),
					start : 0,
					limit : 20
				};
				
				activeUserDss.load({
					params :{
						startTime : Ext.getCmp('startTime').getValue(),
						endTime : Ext.getCmp('endTime').getValue(),
						start : 0,
						limit : 20
					}
				});
		}
		},'&nbsp;&nbsp;活跃用户总数:&nbsp;', new Ext.form.Label({
			width : 60,
			id : 'activeUserCount'
		})
	 ]
	});
	
	activeUserDss.load({
		params : {
			start : 0,
			limit : 20
		}
	});
	
	activeUserMangerGrid.render('grid-activeUserListManager');
});