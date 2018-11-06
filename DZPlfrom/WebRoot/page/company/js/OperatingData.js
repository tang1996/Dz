Ext.onReady(function() {
	var OperatingDataDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.COMPANY_OPERATINGVIEW	//经营数据
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'id',
			successProperty : '@success',
			fields : [ 'id', 'name', 'startTime', 'endTime', 'info', 'balance', 'orderCount' ]
		}),
	});

	var OperatingDataCbsm = new Ext.grid.CheckboxSelectionModel();
	var OperatingDataCm = new Ext.grid.ColumnModel([
		OperatingDataCbsm, {
			header : '店名',
			dataIndex : 'name',
			width : (document.body.clientWidth - 193) / 18,
		}, {
			header : '区域',
			dataIndex : 'info',
			width : (document.body.clientWidth - 193) / 18,
		}, {
			header : '总金额',
			dataIndex : 'balance',
			width : (document.body.clientWidth - 193) / 18,
		}, {
			header : '总订单数',
			dataIndex : 'orderCount',
			width : (document.body.clientWidth - 193) / 18,
		} 
	]);

	var OperatingDataMangerGrid = new Ext.grid.EditorGridPanel({
			ds : OperatingDataDss,
			cm : OperatingDataCm,
			sm : OperatingDataCbsm,
			closable : true,
			border : false,
			id : 'operatingData',	//将该对象渲染到对应ID的叶去
			stripeRows : true,
			height : document.body.clientHeight - 106,
			bbar : new Ext.PagingToolbar({	//底部工具条
				store : OperatingDataDss,
				displayInfo : true,
				displayMsg : ' 显示第 {0} 条到 {1} 条记录，一共 {2} 条',
				emptyMsg : '没有记录'
			}),
			tbar : ['店名', '-', new Ext.form.TextField({
				width : 100,
				id : 'name'
			}),'区域','-',new Ext.form.TextField({
				width :  100,
				id : 'info'
			}),'&nbsp;&nbsp;&nbsp;开始时间&nbsp;&nbsp;&nbsp;', '-', '&nbsp;&nbsp;&nbsp;', new Ext.form.DateField({
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
					OperatingDataDss.baseParams = {
						name : Ext.getCmp('name').getValue(),
						info : Ext.getCmp('info').getValue(),
						startTime : Ext.getCmp('startTime').getValue(),
						endTime : Ext.getCmp('endTime').getValue(),
						start : 0,
						limit : 20	
				};
				
				OperatingDataDss.load({
					params :{
						name : Ext.getCmp('name').getValue(),
						info : Ext.getCmp('info').getValue(),
						startTime : Ext.getCmp('startTime').getValue(),
						endTime : Ext.getCmp('endTime').getValue(),
						start : 0,
						limit : 20	
					}
				});
			}
		 }
		 ]
		});
	
	OperatingDataDss.load({
		params : {
		start : 0,
		limit : 20
	}
	});

	OperatingDataMangerGrid.render('grid-OperatingData');
});