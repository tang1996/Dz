Ext.onReady(function() {
	var infoDss = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : BASE_URL + LOGIN_ACTION.USER_VIEW
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list',
			id : 'id',
			successProperty : '@success',
			fields : [ 'id', 'user_name', 'img_url', 'name', 'nickname', 'phone',
				'age', 'company', 'postion', 'hobby', 'sign', 'career',
				'is_distribution', 'is_newuser', ]
		}),		
		listeners : {
			load : function() {
			Ext.getCmp('userCount').setText(this.reader.jsonData.totalCount)
		}
	}
	});

	var infoCbsm = new Ext.grid.CheckboxSelectionModel();
	var infoCm = new Ext.grid.ColumnModel([
		infoCbsm, {
			header : 'id',
			dataIndex : 'id',
			locked : true,
			hidden : true,
			width : (document.body.clientWidth - 193) / 18
		}, {
			header : '帐号',
			dataIndex : 'user_name',
			width : (document.body.clientWidth - 193) / 18,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '头像地址',
			dataIndex : 'img_url',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '姓名',
			dataIndex : 'name',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "昵称",
			dataIndex : 'nickname',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "联系方式",
			dataIndex : 'phone',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "年龄",
			dataIndex : 'age',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "工作单位",
			dataIndex : 'company',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "职务",
			dataIndex : 'position',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "爱好",
			dataIndex : 'hobby',
			hidden : true,
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "个性签名",
			dataIndex : 'sign',
			hidden : true,
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "从事职业",
			dataIndex : 'career',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : "是否是跑腿员",
			dataIndex : 'is_distribution',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}
		, {
			header : "是否是新用户",
			dataIndex : 'is_newuser',
			width : (document.body.clientWidth - 193) / 20,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}
	]);


	var infoMangerGrid = new Ext.grid.EditorGridPanel({
		ds : infoDss,
		cm : infoCm,
		sm : infoCbsm,
		closable : true,
		border : false,
		id : 'infoManager',
		stripeRows : true,
		height : document.body.clientHeight - 106,
		bbar : new Ext.PagingToolbar({
			store : infoDss,
			displayInfo : true,
			displayMsg : ' 显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : '没有记录'
		}),
		tbar : [ '用户账户', '-', new Ext.form.TextField({
			width : 100,
			id : 'userName'
		}), '&nbsp;', {
			text : '查询',
			iconCls : 'addUser',
			handler : function() {

				infoDss.baseParams = {
					userName : Ext.getCmp('userName').getValue(),
					start : 0,
					limit : 20
				};

				infoDss.load({
					params : {
						userName : Ext.getCmp('userName').getValue(),
						start : 0,
						limit : 20
					}
				});
			}
		}, '&nbsp;&nbsp;&nbsp;&nbsp;', {
			text : '添加',
			iconCls : 'addUser',
			handler : function() {
				infoDss.insert(0, new Ext.data.Record({
					user_name : '必填信息',
					password : '必填信息',
					img_url : '',
					name : '',
					nickname : '',
					token : '',
					phone : '',
					card_front : '',
					card_back : '',
					create_time : '',
					update_time : '',
					auditor : '',
					status : 0,
					age : 0,
					company : '',
					postion : '',
					hobby : '',
					sign : '',
					career : '',
					is_distribution : 0,
					is_newuser : 1,
				}));
			}
		}, '</br>\n', '&nbsp;&nbsp;', {
			text : '删除',
			iconCls : 'addUser',
			handler : function() {
				Ext.MessageBox.confirm('消息', '确定要删除此用户吗?', deleteuser);
			}
		},'&nbsp;&nbsp;用户总数:&nbsp;', new Ext.form.Label({
			width : 60,
			id : 'userCount'
		})
		]
	});

	function deleteuser(btn) {
		if (btn == 'yes') {
			var b = "";
			var rends = infoMangerGrid.getSelectionModel().getSelections();
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
					url : BASE_URL + LOGIN_ACTION.USER_DELETE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : b
					},
					success : function(form, action) {
						infoDss.reload();
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

	function saveORupdateinfoMangerGrid(obj) {
		var r = obj.record; // 获取被修改的行
		if (r.data.user_name != '必填信息' && r.data.password != '必填信息') {

			if (r.data.id == null || r.data.id == '') {
				Ext.Ajax.request({
					url : BASE_URL + LOGIN_ACTION.USER_UPDATE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						userName : r.data.user_name,
						password : r.data.password,
						imgUrl : r.data.img_url,
						name : r.data.name,
						nickname : r.data.nickname,
						token : r.data.token,
						phone : r.data.phone,
						auditor : r.data.auditor,
						status : r.data.status,
						age : r.data.age,
						company : r.data.company,
						postion : r.data.postion,
						hobby : r.data.hobby,
						sign : r.data.sign,
						career : r.data.career,
						isDistribution : r.data.is_distribution,
						isNewuser : r.data.is_newuser,
					},
					success : function(response, options) {
						infoDss.reload();
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
					url : BASE_URL + LOGIN_ACTION.USER_UPDATE,
					waitMsg : '处理中，请稍等..',
					waitTitle : '请稍等',
					params : {
						id : r.data.id,
						userName : r.data.user_name,
						password : r.data.password,
						imgUrl : r.data.img_url,
						name : r.data.name,
						nickname : r.data.nickname,
						token : r.data.token,
						phone : r.data.phone,
						cardFront : r.data.card_front,
						cardBack : r.data.card_back,
						createTime : r.data.create_time,
						updateTime : r.data.update_time,
						auditor : r.data.auditor,
						status : r.data.status,
						age : r.data.age,
						company : r.data.company,
						postion : r.data.postion,
						hobby : r.data.hobby,
						sign : r.data.sign,
						career : r.data.career,
						isDistribution : r.data.is_distribution,
						isNewuser : r.data.is_newuser,
					},
					success : function(response, options) {
						infoDss.reload();
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

	infoDss.load({
		params : {
			start : 0,
			limit : 20
		}
	});

	infoMangerGrid.addListener('afteredit', saveORupdateinfoMangerGrid);
	infoMangerGrid.render('grid-userlistManager');
});