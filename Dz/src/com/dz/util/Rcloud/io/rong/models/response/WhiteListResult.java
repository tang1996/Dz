package com.dz.util.Rcloud.io.rong.models.response;

import com.dz.util.Rcloud.io.rong.models.Result;
import com.dz.util.Rcloud.io.rong.models.user.UserModel;
import com.dz.util.Rcloud.io.rong.util.GsonUtil;

/**
 * @author RongCloud
 */
public class WhiteListResult extends Result{

    private UserModel[] members;

    public WhiteListResult(Integer code, String msg, UserModel[] members) {
        super(code, msg);
        this.members = members;
    }

    public WhiteListResult(UserModel[] members) {
        this.members = members;
    }

    public UserModel[] getMembers() {
        return this.members;
    }

    public void setMembers(UserModel[] members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this, WhiteListResult.class);
    }

}
