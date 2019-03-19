package util;

import android.text.TextUtils;

public class EmptyUtil {
    //非空判断
    public static boolean isNull(String name,String password){
        return !TextUtils.isEmpty(name)&&!TextUtils.isEmpty(password);
    }
    //正则验证手机号和密码
    public static boolean isMobileNO(String name,String password){
        String REGEX_MOBILE = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
        String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";
        return !name.matches(REGEX_MOBILE)&&!password.matches(REGEX_PASSWORD);
    }
}
