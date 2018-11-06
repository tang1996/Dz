package com.dz.util.Rcloud.Test;

import com.dz.util.Rcloud.io.rong.RongCloud;
import com.dz.util.Rcloud.io.rong.models.response.TokenResult;
import com.dz.util.Rcloud.io.rong.models.user.UserModel;

/**
 * Demo class
 *
 * @author RongCloud
 *
 */
public class GetUserToken {
    /**
     * 此处替换成您的appKey
     * */
    private static final String appKey = "z3v5yqkbz1jk0";
    /**
     * 此处替换成您的appSecret
     * */
    private static final String appSecret = "LfymZ6UJD1ei6";
    /**
     * 自定义api地址
     * */
    private static final String api = "http://api.cn.ronghub.com";

    public static void main(String[] args) throws Exception {

        RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);
        //自定义 api 地址方式
        
        com.dz.util.Rcloud.io.rong.methods.user.User RcloudUser = rongCloud.user;

        /**
         * API 文档: http://www.rongcloud.cn/docs/server_sdk_api/user/user.html#register
         *
         * 注册用户，生成用户在融云的唯一身份标识 Token
         */
        UserModel user = new UserModel()
                .setId("18175603333")
                .setName("jack")
                .setPortrait("http://118.190.149.109:8081/DzClient/common/images/logo.png");
        TokenResult result = RcloudUser.register(user);
        System.out.println("getToken:  " + result.toString());

    }
}
