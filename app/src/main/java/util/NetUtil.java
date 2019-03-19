package util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import app.MyApplication;

public class NetUtil {
     //判断是否有网络
    public static boolean hasNetWork(){
        ConnectivityManager cm= (ConnectivityManager) MyApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=cm.getActiveNetworkInfo();
        return networkInfo!=null&&networkInfo.isAvailable();
    }
}
