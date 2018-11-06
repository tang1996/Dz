//判断是否是数字
function isNumber(val) {//2018-11-01 @Tyy
    var regNeg = /^((([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
	if (regNeg.test(val)) {
		return true;
	} else {
		return false;
	}
}

//判断是否是固定号码
function isTele(val) {//2018-11-01 @Tyy
    var regPos = /^\d+(\-\d+)?$/; //加区号
    var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
    if(regPos.test(val) || regNeg.test(val)) {
        return true;
    } else {
    	return false;
    }
}