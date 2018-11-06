var Tree = Ext.tree;

var root = new Ext.tree.TreeNode({
	id : "root",
	text : "树的根"
});



var orderManager = new Ext.tree.TreeNode({
	id : "orderManager",
	text : "订单列表",
	icon : './common/images/home.png',
	url : './page/order/html/orderManager.html',
	leaf : true
});
root.appendChild(orderManager);

var takeOutOrderManager = new Ext.tree.TreeNode({
	id : "takeOutOrderManager",
	text : "外卖订单列表",
	icon : './common/images/home.png',
	url : './page/order/html/takeOutOrderManager.html',
	leaf : true
});
root.appendChild(takeOutOrderManager);

//ynw start	美食订单
var cateOrderManager = new Ext.tree.TreeNode({
	id: "cateOrderManager",
	text: "美食订单列表",
	icon: './common/images/home.png',
	url: './page/order/html/cateOrderManager.html',
	leaf : true
});
root.appendChild(cateOrderManager);
//ynw end

var orderTree = new Tree.TreePanel({
	width : 200,
	height : 317,
	border : false,
	region : "west",
	root : root,
	rootVisible : false,
	iconCls : 'db', 
	autoScroll : true
});
orderTree.on("click", function(node) {
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