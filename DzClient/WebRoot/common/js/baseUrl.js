var BASE_URL = 'http://'+window.location.host;

LOGIN_ACTION={ 
	ORDER_DESTINE:'/Dz/order?destine',
	COMMPANY_TOP:'/Dz/company?companyTop',
	SEARCH_VIEW:'/Dz/search?searchTakeout',
	SEARCH_ORDER:'/Dz/search?order',
	GETGOODSLIST:'/Dz/goods?getGodsList',
	GETGODSLIST:'/Dz/goods?getGoodsList',
	CTOGOODS:'/Dz/goods?cTogoods',
	CTOGOODSMS:'/Dz/goods?cTogoodsMS',
	GETGOOD:'/Dz/goods?getGood',
	GETFICATION:'/Dz/ification?getFication',
	GETORDER:'/Dz/order?userView',
	ORDERVIEW:'/Dz/order?view',
	INCLUDEVIEW:'/Dz/include?view',
	INCLUDE_SAVE:'/Dz/include?saveORupdate',
	GET_ADDRESS:'/Dz/address?getAddress',
	USER_VIEW:'/Dz/order?userView',
	NUSER_VIEW:'/Dz/order?nuserView',
	FIND:'/Dz/company?find',
	FINDNAME:'/Dz/company?findName',
	COMPANYVIEW:'/Dz/evaluate?companyview',
	CHANGE_STATUS:'/Dz/reserve?changeStatus',
	SAVE:'/Dz/shop?save',
	EMPTY_SHOP:'/Dz/shop?emptyShop',
	SUB:'/Dz/shop?sub',
	SEND_EVALUATE:'/Dz/evaluate?sendEvaluate',
	LUPLOAD:'/Dz/evaluate?upload',
	CREATEORDER:'/Dz/order?createOrder',	
	GETDISTEIBUTION:'/Dz/company?getDisteibution',
	GETORDER_COMPANY:'/Dz/company?getOrder',
	DO_SEARCH:'/Dz/companyActivity?doSearch',
	TRACKVIEW:'/Dz/track?view',
	RECEIVEADDRESS:'/Dz/address?receiveAddress',
	ADDRESS_SAVEORUPDATE:'/Dz/address?saveORupdate',
	UPDATE_ADDR:'/Dz/address?update',
	UPDATEADDRESS:'/Dz/address?find',
	USERSEARCH:'/Dz/search?userSearch',
	USERDELETE:'/Dz/search?userDelete',
	SEARCHVIEW:'/Dz/search?view',
	RESERVEVIEW:'/Dz/reserve?view',
	RESERVEORDER:'/Dz/order?reserve',
	ORDERPAY:'/Dz/order?pay',
	NATRUE:'/Dz/nature?view',
	CATEVIEW:'/Dz/cate?view',
	OPEN_TABLE:'/Dz/cate?openTable',
	RESTULVIEW:'/Dz/order?restulView',
	LUSERVIEW:'/Dz/evaluate?userview',
	SNATCHVIEW:'/Dz/order?snatchView',
	PAYSUCCESS:'/Dz/order?paySuccess',
	//EVALUATECOMVIEW:'/Dz/evaluate?companyview',
	ADWAREINDEX:'/Dz/adware?indexShow',
	DISTRIVIEW:'/Dz/order?orderView',
	FINISH:'/Dz/order?finish',
	DISTRIBUTION:'/Dz/order?distribution',
	SNATCH:'/Dz/order?snatch',
	TABLEFIND:'/Dz/reserve?find',
	NATUREFIND:'/Dz/nature?find',
	RESUTL:'/Dz/track?resutl',
	INCLUDEDELECT:'/Dz/include?delete',
	COMPANY_ISREPLY:'/Dz/evaluate?companyIsreply',
	
	//商家app
	NEWORDER:'/Dz/order?newOrder',
	REMINDER:'/Dz/order?getReminder',
	UNUSUAL:'/Dz/order?getUnusual',
	GOODSSAVE:'/Dz/goods?save',
	ALLORDER:'/Dz/order?allOrder',
	BACKBALANCE:'/Dz/order?backBalance',
	GETIFICATION:'/Dz/ification?getIfication',
	GETIFICATIONCOMMON:'/Dz/ification?getIficationCommon',
	ATTRFIND:'/Dz/attribute?find',
	UPLOAD:'/Dz/images?upload',
	ATTRSAVE:'/Dz/attribute?saveORupdate',
	RECEIPT:'/Dz/order?receipt',
	BACK:'/Dz/order?back',
	APPINFO:'/Dz/company?appinfo',
	COMPANYUPDATE:'/Dz/company?update',
	LOGOUPLOAD:'/Dz/images?logoUpload',
	RELATING_SAVE:'/Dz/relating?newSave',
	RELATING_GET:'/Dz/relating?get',
	CHECK_MONEY:'/Dz/relating/checkMoney',
	DAY_COUNT:'/Dz/runManCount?dayCount',
	MON_COUNT:'/Dz/runManCount?monCount',
	ALL_ORDER_REFUND:'/Dz/order?refund',
	JOB_SAVE:'/Dz/job?save',
	JOB_DESTINE:'/Dz/job?destine',
	DEL_ADDRESS:'/Dz/address?delete',
	
	
	//===========================
	APPINFO:'/Dz/company?appinfo',
	DAYCOUNT:'/Dz/companyOrderCount?dayCount',
	COMPANYUPDATE:'/Dz/company?update',
	GETIFICATION:'/Dz/ification?getIfication',
	IFSAVEORUPDATE:'/Dz/ification?saveORupdate',
	GETTYPE:'/Dz/ification?getType',
	UPLOADSAVE:'/Dz/goods?uploadSave',
	IFICATIONFIND:'/Dz/ification?find',
	IFICATIONUPDATE:'/Dz/ification?update',
	GOODSDOWN:'/Dz/goods?down',
	GOCOMPANYGOODS:'/Dz/goods?companyGoods',
	GOCOMPANYGOODSMS:'/Dz/goods?companyGoodsMs',
	GOODSDELETE:'/Dz/goods?delete',
	ATTRFIND:'/Dz/attribute?find',
	UPLOAD:'/Dz/images?upload',
	ATTRSAVE:'/Dz/attribute?saveORupdate',
	ACTIVITYVIEW:'/Dz/activity?view',
	ACTIVITYSAVE:'/Dz/companyActivity?save',
	COMGETCOMPANYACTIVITY:'/Dz/companyActivity?getCompanyActivity',
	ACTIVITYFIND:'/Dz/companyActivity?find',
	ACTIVITYUPDATE:'/Dz/companyActivity?update',
	ACTIVITYUPDATESTATUS:'/Dz/companyActivity?updateStatus',
	ACTIVITYDELETE:'/Dz/companyActivity?delete',
	RESERVESAVE:'/Dz/reserve?save',
	RESERVEUPDATESTATUS:'/Dz/reserve?updateStatus',
	GETTABLENO:'/Dz/reserve?getTableNo',
	BOOKTIMEVIEW:'/Dz/bookTime?view',
	RESERVEALLVIEW:'/Dz/reserve?allView',
	RESERVEISOPEN:'/Dz/reserve?updateIsOpen',
	GETRESERVE:'/Dz/reserve?getReserve',
	RESERVEUPDATE:'/Dz/reserve?update',
	SETRECEIPT:'/Dz/company?setReceipt',
	SETCAMER:'/Dz/company?setCamer',
	GET_RECEIPT:'/Dz/company?getReceipt',
	GET_CAMBER:'/Dz/company?getCamer',
	GET_XG_TIME:'/Dz/company?getXGtime',
	SAVEORUPDATE:'/Dz/attribute?saveORupdate',
	ATTRIBUTEGETVIEW:'/Dz/attribute?getView',
    GETATTRIBUTE:'/Dz/attribute?getAttribute',
	ATTRIBUTESAVE:'/Dz/attribute?save',
	NATURESAVEORUPDATE:'/Dz/nature?saveORupdate',
	NATUREDELETE:'/Dz/nature?delete',
	GOODSUPDATEIMG:'/Dz/goods?updateImg',
	GOODSUPDATE:'/Dz/goods?update',
	COMPANYCOUNT:'/Dz/evaluate?companyCount',
	EVALUATE_DEL:'/Dz/evaluate?delete',
	EVALUATETYPE:'/Dz/evaluate?evaluateType',
	RESERVEDELETE:'/Dz/reserve?delete',
	RIDEREXAMINESAVE:'/Dz/riderExamine?save',
	SALEEXAMINESAVE:'/Dz/saleExamine?save',
	COMPANYEXAMINESAVE:'/Dz/companyExamine?save',
	EXAMINELOGOUPLOAD:'/Dz/companyExamine?logoUpload',
	COMPANY_EXAMINE_FIND:'/Dz/companyExamine?find',
	COMPANY_EXAMINE_SUBMIT:'/Dz/companyExamine?submit',
	VIEWQUALITY:'/Dz/company?viewQuality',
	DELICACYUPDATE:'/Dz/delicacy?update',
	DISTRIBUTIONUPDATE:'/Dz/distribution?update',
	BOOKTIMEBACKVIEW:'/Dz/bookTime?backView',
	ORDERUSERVIEWMS:'/Dz/order?userViewMS',
	UPDATEREMARK:'/Dz/order?updateRemark',
	GETCOMPANYACTIVITYMS:'/Dz/companyActivity?getCompanyActivityMS',
	GET_PAY:'/Dz/order?getPay',
	FINDMS:'/Dz/companyActivity?findMS',
	NUSERVIEWMS:'/Dz/order?nuserViewMS',
	GETCOMPANYACTIVITY:'/Dz/order?getCompanyActivity',
	GETCOMPANYACTIVITYW:'/Dz/order?getCompanyActivityW',
	
	OUNUSUAL:'/Dz/order?unusual',
	REBACKRUNNING:'/Dz/order?rebackRunning',
	MSCOUNT:'/Dz/companyOrderCount?MSCount',
	WMCOUNT:'/Dz/companyOrderCount?WMCount',
	GETCOUNT:'/Dz/companyOrderCount?getCount',
	GETDOINGCOUNT:'/Dz/companyOrderCount?getDoingCount',
	GETORDER:'/Dz/companyOrderCount?getOrder',
	GETDOINGLIST:'/Dz/companyOrderCount?getDoingList',
	GETLIST:'/Dz/companyOrderCount?getList',
	GETINLIST:'/Dz/companyOrderCount?getInList',
	GETINORDER:'/Dz/companyOrderCount?getInOrder',
	CHANGESTATUS:'/Dz/order?changeStatus',
	MAP_GRIL:'http://www.dongzhikj.com:8080/DzClient/common/img/rider_girl.png',
	UPDATEUNSUBSCRIBE:'/Dz/company?updateUnsubscribe',
	
	SERVICEUSERLOGIN:'/Dz/staff/login',
	STAFF_GETCODE:'/Dz/staff?getCode',
	STAFF_UPDATE_PASSWORD:'/Dz/staff?updatePassword',
	USER_GETCODE:'/Dz/user?getCodeForget',
	USER_UPDATE_PASSWORD:'/Dz/user?updatePassword',
	ADD_FEEDBACK:'/Dz/opinion?save',

	//电脑端
	SERVICEUSERLOGINCOM:'/Dz/staff//login',//登录 ynw
	RESERVEVIEWTABLE:'/Dz/reserve?viewTable',
	COMPOPENTABLE:'/Dz/cate?compOpenTable',	//开桌
	COMPANYGOODSMS:'/Dz/goods?companyGoodsMs',	//美食单类查询
	COMPUTERGOODSMS:'/Dz/goods?computerGoodsMs',	//美食单类查询(上架)			
	RELATING_ARRSAVEINSIDE:'/Dz/relating?arrSaveInside',	//美食线下批量加菜
	INSIDEORDER_BATCHSAVE:'/Dz/insideOrder//batchSave',//美食开桌点菜
	INSIDEORDER_OPENTABLE:'/Dz/insideOrder?openTable',	//美食开桌点菜
	INSIDEORDER_INSIDESAVE:'/Dz/job?insideSave',	//线下打印保存
	ONSIDEORDER_ONSIDESAVE:'/Dz/job?onsideSave',	//线上打印
	INSIDEORDER_INSIDEVIEW:'/Dz/job?insideView',	//美食打印显示
	BOOKTIMEINSIDEVIEW:'/Dz/bookTime?Insideview',	//网络订单
	BOOKTIMEINSIDERESERVEVIEW:'/Dz/bookTime?InsideReserveview',	//网路订菜了的订单
	BOOKTIMEINSIDENORESERVEVIEW:'/Dz/bookTime?InsideNoReserveview',		//网络未订菜的订单
	RELATING_GETINSIDE:'/Dz/relating?insideGet',  	//得到线下的订菜菜品
	RELATING_ARRDELINSIDE:'/Dz/relating?arrDelInside',	//美食线下批量减菜
	RELATING_ARRDELONSIDE:'/Dz/relating?arrDelOnside',	//美食线上批量减菜
	GOODSUPDATESTOCK:'/Dz/goods?updateStock',		//修改菜品库存
	RELATING_INSIDEDOING:'/Dz/relating?insideDoing',	//取到就餐中的insideID		
	//CATANETVIEW:'/Dz/cate?netView',	//线下网络订单状态改变
	RELATING_GETONSIDE:'/Dz/relating?onsideGet',  	//得到线上的订菜菜品	
	RELATING_ARRSAVEONSIDE:'/Dz/relating?arrSaveOnside',	//美食线上批量加菜
	CATA_ONSIDEANDINSIDE:'/Dz/cate?OnsideAndInside',		//在线上加菜到线下
	RELATING_NETADDINSIDE:'/Dz/relating?netAddInside',	//电击打印生成订单
	INSIDECHECKMONEY:'/Dz/insideOrder//checkMoney',
	NEWCHECKMONEY:'/Dz/insideOrder//newCheckMoney',
    INSIDEORDER_CHANGESTATUS:'/Dz/insideOrder?deleteInsideOrder',	//删除订单变桌子状态
    SUM_CHECK_MONEY:'/Dz/insideOrder/sumCheckMoney',
    COMPUTERCATE_KEEPPEOPLE:'/Dz/computerCate?keepPeople',	//线下餐桌调整人数
    BOOKTIME_KEEPPEOPLE:'/Dz/bookTime?keepPeople',	//线上预约人数的调整
	RELATING_ARRSAVEDELINSIDE:'/Dz/relating?arrSaveDelInside',	//美食线下记录退菜的数量
	LUSERCOUNT:'/Dz/company?userCount',
	JOB_SAVEINSIDE:'/Dz/job?saveInside',	//线下订单打印
	JOB_INSIDESAVEGOODS:'/Dz/job?insideSaveGoods',	//线下加菜订单打印  
	JOB_INSIDDELETEGOODS:'/Dz/job?insideDeleteGoods',	//线下退菜订单打印
	JOB_DELETEGOODS:'/Dz/job?deleteGoods',	//线下退菜订单打印   新增 2018-10-06  @Tyy
	ORDER_SEARCH:'/Dz/bookTime?searchOrder'	//线下退菜订单打印   新增 2018-10-06  @Tyy

};
