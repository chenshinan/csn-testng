package com.chenshinan.testng.utils;

/**
 * @author shinan.chen
 * @since 2019/1/8
 */
public class EncodeUtil {
    public static final String loginKey = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    public static String encodePassword(String source) {
        String output = "";
        char chr1, chr2, chr3;
        int enc1, enc2, enc3, enc4;
        for (int i = 0; i < source.length();) {
            chr1 = i<source.length()?source.charAt(i++):null;
            chr2 = i<source.length()?source.charAt(i++):null;
            chr3 = i<source.length()?source.charAt(i++):null;
            enc1 = chr1 >> 2;
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
            enc4 = chr3 & 63;
            if(String.valueOf(chr2).equals("")||String.valueOf(chr2)==null){
                enc3 = enc4 = 64;
            } else if (String.valueOf(chr3).equals("")||String.valueOf(chr3)==null) {
                enc4 = 64;
            }
            output = output + loginKey.charAt(enc1) + loginKey.charAt(enc2)
                    + loginKey.charAt(enc3) + loginKey.charAt(enc4);
        }
        return output;
    }
}
