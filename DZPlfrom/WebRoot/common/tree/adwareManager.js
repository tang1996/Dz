var Tree = Ext.tree;

var root = new Ext.tree.TreeNode({
	id : "root",
	text : "树的根"
});



var adwareManager = new Ext.tree.TreeNode({
	id : "adwareManager",
	text : "广告",
	icon : './common/images/home.png',
	url : './page/adware/html/adwareManager.html',
	leaf : true
});
root.appendChild(adwareManager);

//var evaluateManager = new Ext.tree.TreeNode({
//	id : "evaluateManager",
//	text : "评价",
//	icon : './common/images/home.png',
//	url : './page/evaluate/html/evaluateManager.html',
//	leaf : true
//});
//root.appendChild(evaluateManager);

var adwareTree = new Tree.TreePanel({
	width : 200,
	height : 317,
	border : false,
	region : "west",
	root : root,
	rootVisible : false,
	iconCls : 'db', 
	autoScroll : true
});
adwareTree.on("click", function(node) {
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