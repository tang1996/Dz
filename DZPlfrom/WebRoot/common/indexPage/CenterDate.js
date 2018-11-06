var centerPanel = new Ext.TabPanel({
		region:'center',
		deferredRender:false,
		activeTab:0,
		resizeTabs:true, // turn on tab resizing
        minTabWidth: 115,
        tabWidth:135,
        enableTabScroll:true,
        defaults: {autoScroll:true},
        plugins: new Ext.ux.TabCloseMenu(),
        html:'<img src="../images/timg.jpg"></img>'
});
