var typeVip = '';
var copyValue='初始化数据...';
var clip;
function toggleDetails(btn, pressed){
      var view = grid.getView();
      view.showPreview = pressed;
      view.refresh();
}

function createTab(node,icon)
{
	var obj = {
		id:node.id,
		title:node.text,
		closable:true,
		script:true,
		layout:"fit",
		bodyStyle : 'overflow-x:hidden; overflow-y:scroll',
		iconCls:icon
	};
	obj.html="<iframe src='"+node.attributes.url+"' frameborder='0'  width='100%' height='100%'></iframe>";
	return new Ext.Panel(obj);
}

//创建一个Tab by URL
function createTabUrl(url,icon,id,title){

	var obj={
		id:id,
		title:title,
		closable:true,
		layout:"fit",
		iconCls:icon,
		autoScroll:true,
		scripts: true
	};
	obj.autoLoad={url:url,scripts: true};
	return new Ext.Panel(obj);
}
newMain = centerPanel;

//创建一个Panel by URL
function newPanelByUrl(url,icon,id,title){
	p=newMain.getComponent(id);
	if(!p){
		p=createTabUrl(url,icon,id,title);
		newMain.add(p);
	}
		newMain.activate(p); 	
}

Ext.onReady(function(){

	var clock = new Ext.Toolbar.TextItem('');
    
	new Ext.Viewport({
		layout:'border',
		items:[
		 	north,
		  new Ext.Panel({
				region:'south',	
				height:30,
				split:true,
				minSize:100,
				maxSize:200,
				bbar: new Ext.ux.StatusBar({
		            id: 'word-status',
		            text: '亿源广西移动充值系统欢迎您!',
        			iconCls: 'ready-icon',
		            items: ['版权所有&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;', clock, ' ']
		        })
			}),
		      {
				region:'west',
                    id:'west-panel',
                    title:'功能導航',
                    split:true,
                    width: 180,
                    minSize: 130,
                    maxSize: 400,
                    collapsible: true,
                    margins:'0 0 0 5',
                    layout:'accordion',
                    layoutConfig:{
                        animate:true
                    },
                    items: [{
                        title:'充值管理',
                        border:false,
                        id:'chagre',
                        iconCls:'channgerStroe',
                        items:chagreTree
                    }]
			},
			centerPanel
		]
	});

	setTimeout(function(){
        Ext.get('loading').remove();
        Ext.get('loading-mask').fadeOut({remove:true});
    }, 250);
});