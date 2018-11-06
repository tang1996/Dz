var BASE_URL = 'http://39.108.6.102:8080';//'http://'+window.location.host;

LOGIN_ACTION={
	//用户接口
	USER_VIEW:'/Dz/base/user?view',
	USER_DELETE:'/Dz/base/user?delete',
	USER_UPDATE:'/Dz/base/user?saveORupdate',
	USER_ACTIVEUSER:'/Dz/base/user?avtiveUser',	//ynw 活跃用户
	//管理员接口
	ADMIN_LOGIN:'/Dz/base/admin/login',
	//收藏接口
	INCLUDE_VIEW:'/Dz/base/include?bastView',
	INCLUDE_DELETE:'/Dz/base/include?delete',
	INCLUDE_UPDATE:'/Dz/base/include?saveORupdate',
	//广告接口
	ADWARE_VIEW:'/Dz/base/adware?view',
	ADWARE_DELETE:'/Dz/base/adware?delete',
	ADWARE_UPDATE:'/Dz/base/adware?saveORupdate',
	ADWARE_UPLOAD:'/Dz/base/adware/upload',
	//商家接口
	COMPANY_VIEW:'/Dz/base/company?view',
	COMPANY_BASEVIEW:'/Dz/base/company?baseview',
	COMPANY_DELETE:'/Dz/base/company?delete',
	COMPANY_UPDATE:'/Dz/base/company?saveORupdate',
	COMPANY_CLOSE:'/Dz/base/company?close',
	COMPANY_FIND:'/Dz/base/company?companyview',
	COMPANY_AUDIT:'/Dz/base/company?auditview',
	COMPANY_UPDATEAUDIT:'/Dz/base/company?auditUpdate',
	COMPANY_SAVE:'/Dz/base/company?save',
	COMPANY_OPERATINGVIEW:'/Dz/base/company?operatingview',	//ynw 经营数据
	
	//商家缴费
	COMPANY_DETAILED_VIEW:'/Dz/base/companyDetailed?view',
	
	COMPANY_PAY_VIEW:'/Dz/base/companyPayRecord?payView',
	COMPANY_PAY_APPLY:'/Dz/base/companyPayRecord?update',
	COMPANY_PAY_COMPANYVIEW:'/Dz/base/companyPayRecord?companyPayView',	//ynw 商家缴费记录
	//商家审核接口
	COMPANY_OPENLIST:'/Dz/base/companyExamine?view',
	COMPANY_EXAMINE_SAVE:'/Dz/base/companyExamine?save',
	//订单接口
	ORDER_BASTVIEW:'/Dz/base/order?bastView',
	ORDER_TAKEOUT:'/Dz/base/order?takeView',
	ORDER_CATEVIEW:'/Dz/base/order?cateView',	//美食订单详情ynw
	//骑手接口
	RIDER_VIEW:'/Dz/base/user?riderView',
	RIDER_DELETE:'/Dz/base/user?delete',
	RIDER_UPDATE:'/Dz/base/user?saveORupdate',
	RIDER_ORDER_LIST:'/Dz/base/orderType?riderView',
	RIDER_ORDER_COUNT:'/Dz/base/runManCount?riderCount',
	RIDER_INFO_LIST:'/Dz/base/user?salerView',
	
	RIDER_EXPORT:'/Dz/base/user?export',
	//骑手审核接口
	RIDER_EXAMINE:'/Dz/base/riderExamine?view',
	RIDER_EXAMINE_DELETE:'/Dz/base/riderExamine?delete',
	RIDER_EXAMINE_APPLY:'/Dz/base/riderExamine?apply',
	RIDER_EXAMINE_SAVE:'/Dz/base/riderExamine?save',
	//骑手综合评分 ynw
	RIDER_MULTIPLE:'/Dz/base/runEvaluate?multiple',	//骑手综合评分ynw
	//推广员申请接口
	SALER_EXAMINE:'/Dz/base/saleExamine?view',
	SALER_EXAMINE_DELETE:'/Dz/base/saleExamine?delete',
	SALER_EXAMINE_APPLY:'/Dz/base/saleExamine?apply',
	SALER_EXAMINE_SAVE:'/Dz/base/saleExamine?save',
	//推广员接口
	SALER_LIST:'/Dz/base/salerInfo?view',
	SALER_INFO_DELETE:'/Dz/base/salerInfo?delete',
	SALER_INFO_SAVE:'/Dz/base/salerInfo?save',
	SALER_COMPANY:'/Dz/base/salerCompany?view',
	SALER_MON_COUNT:'/Dz/base/salerInfo?monCount',
	SALER_TOTAL_COUNT:'/Dz/base/salerInfo?totalCount',
	SALER_TEAM_COUNT:'/Dz/base/salerInfo?teamCount',
	
	SALER_EXPORT:'/Dz/base/salerInfo?export',
	COMPANYDETAILED_EXPORT:'/Dz/base/companyDetailed?export',		//ynw
	COMPANYPAYRECORD_EXPORT:'/Dz/base/companyPayRecord?export',	//ynw
	
	//反馈接口
	FEEDBACK_VIEW:'/Dz/base/opinion?view',
};
