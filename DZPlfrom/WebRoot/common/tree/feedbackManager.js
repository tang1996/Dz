var Tree = Ext.tree;

var root = new Ext.tree.TreeNode({
	id : "root",
	text : "树的根"
});



var feedbacklistManager = new Ext.tree.TreeNode({
	id : "feedbacklistManager",
	text : "反馈列表",
	icon : './common/images/home.png',
	url : './page/feedback/html/feedbacklistManager.html',
	leaf : true
});
root.appendChild(feedbacklistManager);

var feedbackTree = new Tree.TreePanel({
	width : 200,
	height : 317,
	border : false,
	region : "west",
	root : root,
	rootVisible : false,
	iconCls : 'db', 
	autoScroll : true
});
feedbackTree.on("click", function(node) {
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