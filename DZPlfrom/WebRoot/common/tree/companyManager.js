var Tree = Ext.tree;

var root = new Ext.tree.TreeNode({
	id : "root",
	text : "树的根"
});



var companyManager = new Ext.tree.TreeNode({
	id : "companyManager",
	text : "商家",
	icon : './common/images/home.png',
	url : './page/company/html/companyManager.html',
	leaf : true
});
root.appendChild(companyManager);


var auditManager = new Ext.tree.TreeNode({
	id : "auditManager",
	text : "待审核商家",
	icon : './common/images/home.png',
	url : './page/company/html/auditManager.html',
	leaf : true
});
root.appendChild(auditManager);

var openManager = new Ext.tree.TreeNode({
	id : "openManager",
	text : "开店申请",
	icon : './common/images/home.png',
	url : './page/company/html/openManager.html',
	leaf : true
});
root.appendChild(openManager);

var operatingData = new Ext.tree.TreeNode({		//ynw
	id : "operatingData",
	text : "经营数据",
	icon : './common/images/home.png',
	url : './page/company/html/OperatingData.html',
	leaf : true
});
root.appendChild(operatingData);

var companyDetailedManager = new Ext.tree.TreeNode({
	id : "companyDetailedManager",
	text : "商家缴费明细",
	icon : './common/images/home.png',
	url : './page/company/html/companyDetailedManager.html',
	leaf : true
});
root.appendChild(companyDetailedManager);

var companyPayManager = new Ext.tree.TreeNode({
	id : "companyPayManager",
	text : "缴费审核",
	icon : './common/images/home.png',
	url : './page/company/html/companyPayManager.html',
	leaf : true
});
root.appendChild(companyPayManager);

var companyTree = new Tree.TreePanel({
	width : 200,
	height : 317,
	border : false,
	region : "west",
	root : root,
	rootVisible : false,
	iconCls : 'db', 
	autoScroll : true
});
companyTree.on("click", function(node) {
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