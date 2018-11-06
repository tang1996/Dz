Ext.onReady(function() {
	var adwareDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.ADWARE_VIEW
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'id',
			successProperty : '@success',
			fields : [ 'id', 'name', 'depict', 'type', 'region', 'source', 'seat',
				'click_rate', 'url', 'create_time', 'start_time', 'end_time', 'contacts',
				'e_mail', 'phone', 'is_close' ]
		}),
		listeners : {
			load : function() {
				Ext.getCmp('totalCount').setText(this.reader.jsonData.totalCount)
			}
		}
	});

	var adwareCbsm = new Ext.grid.CheckboxSelectionModel();
	var adwareCm = new Ext.grid.ColumnModel([
		adwareCbsm, {
			header : 'id',
			dataIndex : 'id',
			locked : true,
			hidden : true,
			width : (document.body.clientWidth - 193) / 18
		}, {
			header : '广告位名称',
			dataIndex : 'name',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '广告位描述',
			dataIndex : 'depict',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '广告代号',
			dataIndex : 'type',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '广告地址区域',
			dataIndex : 'region',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '页面来源',
			dataIndex : 'source',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '广告位置',
			dataIndex : 'seat',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '点击率',
			dataIndex : 'click_rate',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : 'url地址',
			dataIndex : 'url',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '添加广告时间',
			dataIndex : 'create_time',
			width : (document.body.clientWidth - 193) / 12,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '广告开始时间',
			dataIndex : 'start_time',
			width : (document.body.clientWidth - 193) / 12,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '广告结束时间',
			dataIndex : 'end_time',
			width : (document.body.clientWidth - 193) / 12,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '广告联系人',
			dataIndex : 'contacts',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '联系人邮箱',
			dataIndex : 'e_mail',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '联系人电话',
			dataIndex : 'phone',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '该广告是否关闭',
			dataIndex : 'is_close',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}
	]);


	var adwareMangerGrid = new Ext.grid.EditorGridPanel({
		ds : adwareDss,
		cm : adwareCm,
		sm : adwareCbsm,
		closable : true,
		border : false,
		id : 'adwareManager',
		stripeRows : true,
		height : document.body.clientHeight - 106,
		bbar : new Ext.PagingToolbar({
			store : adwareDss,
			displayInfo : true,
			displayMsg : ' 显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : '没有记录'
		}),
		tbar : [ '广告位id', '-', new Ext.form.TextField({
			width : 100,
			id : 'id'
		}), '&nbsp;', {
			text : '查询',
			iconCls : 'addUser',
			handler : function() {
				adwareDss.baseParams = {
					id : Ext.getCmp('id').getValue(),
					start : 0,
					limit : 20
				};

				adwareDss.load({
					params : {
						id : Ext.getCmp('id').getValue(),
						start : 0,
						limit : 20
					}
				});
			}
		}, '&nbsp;&nbsp;&nbsp;&nbsp;', {
			text : '添加',
			iconCls : 'addUser',
			handler : function() {
				adwareDss.insert(0, new Ext.data.Record({
					name : '必填信息',
					depict : '必填信息',
					region : '必填信息',
					start_time : '必填信息',
					end_time : '必填信息',
					contacts : '必填信息',
					e_mail : '必填信息',
					phone : '必填信息',
					is_close : '1',
				}));
			}
		}, '</br>\n', '&nbsp;&nbsp;', {
			text : '删除',
			iconCls : 'addUser',
			handler : function() {
				Ext.MessageBox.confirm('消息', '确定要删除此广告吗?', deleteadware);
			}
		}, '</br>\n', '&nbsp;&nbsp;', {
			text : '上传',
			iconCls : 'addUser',
			handler : function() {
				Ext.MessageBox.confirm('消息', '确定要修改此广告吗?', updateadware);
			}
		}, '&nbsp;&nbsp;广告总数:&nbsp;', new Ext.form.Label({
			width : 60,
			id : 'totalCount'
		})
		]
	});

	function deleteadware(btn) {
		if (btn == 'yes') {
			var b = "";
			var rends = adwareMangerGrid.getSelectionModel().getSelections();
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
					url : BASE_URL + LOGIN_ACTION.ADWARE_DELETE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : b
					},
					success : function(form, action) {
						adwareDss.reload();
						var json = Ext.util.JSON.decode(form.responseText);
						Ext.Msg.alert('温馨提示', json.message);
					},
					failure : function(form, action) {
						var json = Ext.util.JSON.decode(form.responseText);
						Ext.Msg.alert('温馨提示', json.message);
					}
				});
			} else {
				Ext.Msg.alert('温馨提示', '请选择一条需要删除的数据!');
			}
		}
	}
	;

	function updateadware(btn) {
		if (btn == 'yes') {
			var id = "";
			var rends = adwareMangerGrid.getSelectionModel().getSelections();
			if (rends.length == 1) {
				for (var i = 0; i < rends.length; i++) {
					var rend = rends[i];
					if (i == rends.length - 1) {
						id = id + rend.get("id");
					} else {
						id = id + rend.get("id") + ",";
					}
				}
				var cashFrom = new Ext.form.FormPanel({
					fileUpload : true,
					width : 500,
					frame : true,
					autoHeight : true,
					bodyStyle : 'padding: 10px 10px 0 10px;',
					labelWidth : 50,
					defaults : {
						anchor : '95%',
						allowBlank : false,
						msgTarget : 'side'
					},
					items : [ {
						xtype : 'fileuploadfield',
						id : 'form-file',
						Width : 200,
						readOnly : true,
						emptyText : '请选择图片...',
						fieldLabel : '路径',
						name : 'file',
						regex : /\.(jpg|JPG|jpeg|JPEG|png|PNG|bmp|BMP)$/,	//ynw
						regexText : '请上传jpg格式的图片',	//ynw
						buttonCfg : {
							text : '',
							iconCls : 'upload-icon'
						}
					}, new Ext.form.ComboBox({
						fieldLabel : "广告位置",
						hiddenName : "active",
						displayField : "object",
						mode : "local",
						width : 100,
						id : 'seat',
						store : new Ext.data.ArrayStore({
							id : 0,
							fields : [ 'statuscode', 'statusDesc' ],
							data : [ [ 'top_left', '首页左上角' ], [ 'top_right', '首页右上角' ], [ 'down_left', '首页左下角' ], [ 'down_right', '首页右下角' ], [ 'main', '主广告' ] ]
						}),
						valueField : 'statuscode',
						displayField : 'statusDesc'
					})
					]
				});

				var cashWin = new Ext.Window({
					title : '图片上传',
					width : 550,
					height : 150,
					minWidth : 350,
					minHeight : 150,
					layout : 'fit',
					resizable : false,
					bodyStyle : 'padding:5px;',
					closable : false,
					buttonAlign : 'center',
					items : cashFrom,
					modal : true,
					buttons : [ {
						text : '上传',
						handler : function() {
							// 判断验证是否过关
							if (cashFrom.getForm().isValid()) {
								cashFrom.getForm().submit({ // 提交细节
									waitMsg : '处理中，请稍等...',
									waitTitle : '请稍等',
									url : BASE_URL + LOGIN_ACTION.ADWARE_UPLOAD,
									params : {
										seat : Ext.getCmp('seat').getValue(),
										id : id,
									},
									success : function(form, action) {
										Ext.Msg.alert('温馨提示', action.result.message);
										cashFrom.getForm().destroy();
										cashWin.hide();
									},
									failure : function(form, action) {
										Ext.Msg.alert('温馨提示', action.result.message);
										cashFrom.getForm().destroy();
										cashWin.hide();
									}
								});
							} else {
								if(Ext.getCmp("form-file").getValue() != ""){		//ynw
									Ext.Msg.alert('上传','请选择JPG、PNG、JPEG或BMP格式的图片');
									return false;
								}else{
								Ext.MessageBox.show({
									title : '温馨提示',
									msg : '内容不能为空，请填写完毕!',
									buttons : Ext.MessageBox.OK,
									icon : Ext.MessageBox.ERROR
								});}
							}
						}
					}, {
						text : '取消',
						handler : function() {
							cashFrom.getForm().destroy();
							cashWin.hide();
						}
					} ]
				});
				cashWin.show();
			} else {
				Ext.Msg.alert('温馨提示', '请选择一条需要删除的数据!');
			}
		}
	}

	function saveORupdateadwareMangerGrid(obj) {
		var r = obj.record; // 获取被修改的行
		if (r.data.name != '必填信息' && r.data.depict != '必填信息' && r.data.start_time != '必填信息' && r.data.end_time != '必填信息'
			&& r.data.e_mail != '必填信息' && r.data.contacts != '必填信息' && r.data.phone != '必填信息') {

			if (r.data.id == null || r.data.id == '') {
				Ext.Ajax.request({
					url : BASE_URL + LOGIN_ACTION.ADWARE_UPDATE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						name : r.data.name,
						depict : r.data.depict,
						region : r.data.region,
						source : r.data.source,
						seat : r.data.seat,
						url : r.data.url,
						startTime : r.data.start_time,
						endTime : r.data.end_time,
						contacts : r.data.contacts,
						Email : r.data.e_mail,
						phone : r.data.phone,
						isClose : r.data.is_close,
					},
					success : function(response, options) {
						adwareDss.reload();
						var json = Ext.util.JSON.decode(response.responseText);
						Ext.Msg.alert('温馨提示', json.message);
					},
					failure : function(form, action) {
						Ext.MessageBox.show({
							title : '温馨提示',
							msg : '数据插入失败，请重新尝试，谢谢!',
							buttons : Ext.MessageBox.OK,
							icon : Ext.MessageBox.ERROR
						});
					}
				});
			} else {
				Ext.Ajax.request({
					url : BASE_URL + LOGIN_ACTION.ADWARE_UPDATE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : r.data.id,
						name : r.data.name,
						depict : r.data.depict,
						type : r.data.type,
						region : r.data.region,
						source : r.data.source,
						seat : r.data.seat,
						clickRate : r.data.click_rate,
						url : r.data.url,
						createTime : r.data.create_time,
						startTime : r.data.start_time,
						endTime : r.data.end_time,
						contacts : r.data.contacts,
						Email : r.data.e_mail,
						phone : r.data.phone,
						isClose : r.data.is_close,
					},
					success : function(response, options) {
						adwareDss.reload();
						var json = Ext.util.JSON.decode(response.responseText);
						Ext.Msg.alert('温馨提示', json.message);
					},
					failure : function(form, action) {
						Ext.MessageBox.show({
							title : '温馨提示',
							msg : '数据插入失败，请重新尝试，谢谢!',
							buttons : Ext.MessageBox.OK,
							icon : Ext.MessageBox.ERROR
						});
					}
				});
			}
		}
	}
	;

	adwareDss.load({
		params : {
			start : 0,
			limit : 20
		}
	});

	adwareMangerGrid.addListener('afteredit', saveORupdateadwareMangerGrid);
	adwareMangerGrid.render('grid-adwareManager');
});