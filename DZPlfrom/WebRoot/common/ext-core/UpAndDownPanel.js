
Ext.ux.UpAndDownPanel = function(config){

			this.upPanel = config.upPanel;
		    this.downPanel = config.downPanel;
		    this.height = config.height;
		    this.southHeight=config.southHeight||0.5;
		    this.north = new Ext.Panel({
					region : 'center',
					layout : 'fit',
					border : false,
					items :[this.upPanel]
		    });
		
		    
			this.south=new Ext.Panel({
						region : 'south',
						split : true,
						height : 208,
						border : false,
						layout : 'fit',
						items :[this.downPanel]
					});
	
				
	Ext.ux.UpAndDownPanel.superclass.constructor.call(this,{
		items : [this.north,this.south],
		region : 'center',
		border : false,
		layout : 'border'
	});


}

Ext.extend(Ext.ux.UpAndDownPanel, Ext.Panel, {

});