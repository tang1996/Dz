var Tree = Ext.tree;

var root = new Ext.tree.TreeNode({
	id : "root",
	text : "树的根"
});



var userlistManager = new Ext.tree.TreeNode({
	id : "userlistManager",
	text : "用户列表",
	icon : './common/images/home.png',
	url : './page/user/html/userlistManager.html',
	leaf : true
});
root.appendChild(userlistManager);

var collectionManager = new Ext.tree.TreeNode({
	id : "collectionManager",
	text : "用户收藏",
	icon : './common/images/home.png',
	url : './page/user/html/collectionManager.html',
	leaf : true
});
root.appendChild(collectionManager);

var userOrderManager = new Ext.tree.TreeNode({
	id : "userOrderManager",
	text : "用户订单",
	icon : './common/images/home.png',
	url : './page/user/html/userOrderManager.html',
	leaf : true
});
root.appendChild(userOrderManager);

var activeUserManager = new Ext.tree.TreeNode({		//ynw
	id : "activeUserManager",
	text : "活跃用户",
	icon : './common/images/home.png',
	url : './page/user/html/activeUserListManager.html',
	leaf : true
});
root.appendChild(activeUserManager);

var userTree = new Tree.TreePanel({
	width : 200,
	height : 317,
	border : false,
	region : "west",
	root : root,
	rootVisible : false,
	iconCls : 'db', 
	autoScroll : true
});
userTree.on("click", function(node) {
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