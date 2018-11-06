function GetQueryString(name){
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}

//判断cookie里是否有用户信息	ynw
function loginCookie() {
  var loginCookie = new String(document.cookie);

  if (loginCookie.length == 0 || loginCookie.replace(/(^s*)|(s*$)/g, "").length == 0 || loginCookie == null || loginCookie == undefined || loginCookie == '') {
	  onlyOKPrompt("请您登录后再进行操作!!!","0");
  }
}

function getCookie(sName){
	var aCookie = document.cookie.split("; ");
	for (var i=0; i < aCookie.length; i++){
		var aCrumb = aCookie[i].split("=");
	if (sName == aCrumb[0])
		return unescape(aCrumb[1]);
	}
	return null;
}

function DelCookie(name) {	//ynw
	  var exp = new Date();
	  exp.setTime(exp.getTime() + (-1 * 24 * 60 * 60 * 1000));
	  var cval = getCookie(name);
	  document.cookie = name + "=" + cval + "; expires=" + exp.toGMTString();
}

function link(val){
	window.android.base(val);
}

function valLink(val, parameter){
	window.android.valLink(val, parameter);
}

function moreLink(val, parameter, secordVal){
	window.android.moreLink(val, parameter, secordVal);
}

function orderCheck(val, parameter, secordVal, thriVal ){
	window.android.orderCheck(val, parameter, secordVal, thriVal);
}

function moreValCheck(val, parameter, secordVal, thriVal, fourVal){
	window.android.moreValCheck(val, parameter, secordVal, thriVal, fourVal);
}


