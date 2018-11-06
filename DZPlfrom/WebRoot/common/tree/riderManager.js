var Tree = Ext.tree;

var root = new Ext.tree.TreeNode({
	id : "root",
	text : "树的根"
});



var riderListManager = new Ext.tree.TreeNode({
	id : "riderListManager",
	text : "骑手列表",
	icon : './common/images/home.png',
	url : './page/rider/html/riderListManager.html',
	leaf : true
});
root.appendChild(riderListManager);

var ScoreManager = new Ext.tree.TreeNode({	//ynw
	id : "ScoreManager",
	text : "评分管理",
	icon : './common/images/home.png',
	url : './page/rider/html/ScoreManager.html',
	leaf : true
});
root.appendChild(ScoreManager);

var RapplyManager = new Ext.tree.TreeNode({
	id : "RapplyManager",
	text : "骑手申请列表",
	icon : './common/images/home.png',
	url : './page/rider/html/RapplyManager.html',
	leaf : true
});
root.appendChild(RapplyManager);

var RorderListManager = new Ext.tree.TreeNode({
	id : "RorderListManager",
	text : "骑手订单列表",
	icon : './common/images/home.png',
	url : './page/rider/html/RorderListManager.html',
	leaf : true
});
root.appendChild(RorderListManager);

var RorderCountManager = new Ext.tree.TreeNode({
	id : "RorderCountManager",
	text : "骑手订单统计",
	icon : './common/images/home.png',
	url : './page/rider/html/RorderCountManager.html',
	leaf : true
});
root.appendChild(RorderCountManager);

var riderTree = new Tree.TreePanel({
	width : 200,
	height : 317,
	border : false,
	region : "west",
	root : root,
	rootVisible : false,
	iconCls : 'db', 
	autoScroll : true
});
riderTree.on("click", function(node) {
	var id = node.id;
	p = centerPanel.getComponent(id);
	if (node.leaf == true) {
		if (!p) {
			p = createTabUrl(node.attributes.url, "about", node.id, node.text);
			newMain.add(p);
		}
		newMain.activate(p);
	}
});