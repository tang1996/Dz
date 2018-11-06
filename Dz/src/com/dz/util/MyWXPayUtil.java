package com.dz.util;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Created by MPC on 2018/1/29.
 */
public class MyWXPayUtil {
	
	final static String key = "guangdongshengshenzhengshidongzi";

    public static String getRefundDecrypt(String reqInfoSecret) {
        String result = "";
        try {
            Security.addProvider(new BouncyCastleProvider());
            sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
            byte[] bt = decoder.decodeBuffer(reqInfoSecret);
            String b = new String(bt);
            String md5key = MD5Util.MD5(key).toLowerCase();
            SecretKey secretKey = new SecretKeySpec(md5key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] resultbt = cipher.doFinal(bt);
            result = new String(resultbt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
