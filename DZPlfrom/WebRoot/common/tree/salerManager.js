var Tree = Ext.tree;

var root = new Ext.tree.TreeNode({
	id : "root",
	text : "树的根"
});



var salerListManager = new Ext.tree.TreeNode({
	id : "salerListManager",
	text : "推广员列表",
	icon : './common/images/home.png',
	url : './page/saler/html/salerListManager.html',
	leaf : true
});
root.appendChild(salerListManager);

var SapplyManager = new Ext.tree.TreeNode({
	id : "SapplyManager",
	text : "推广员申请列表",
	icon : './common/images/home.png',
	url : './page/saler/html/SapplyManager.html',
	leaf : true
});
root.appendChild(SapplyManager);

var SrecordManager = new Ext.tree.TreeNode({
	id : "SrecordManager",
	text : "推广记录",
	icon : './common/images/home.png',
	url : './page/saler/html/SrecordManager.html',
	leaf : true
});
root.appendChild(SrecordManager);

var monRecordManager = new Ext.tree.TreeNode({
	id : "monRecordManager",
	text : "当月绩效统计",
	icon : './common/images/home.png',
	url : './page/saler/html/monRecordManager.html',
	leaf : true
});
root.appendChild(monRecordManager);

var totalRecordManager = new Ext.tree.TreeNode({
	id : "totalRecordManager",
	text : "总用户统计",
	icon : './common/images/home.png',
	url : './page/saler/html/totalRecordManager.html',
	leaf : true
});
root.appendChild(totalRecordManager);

var salerTree = new Tree.TreePanel({
	width : 200,
	height : 317,
	border : false,
	region : "west",
	root : root,
	rootVisible : false,
	iconCls : 'db', 
	autoScroll : true
});
salerTree.on("click", function(node) {
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