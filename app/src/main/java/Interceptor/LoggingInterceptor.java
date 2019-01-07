package Interceptor;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.IOException;

import app.MyApplication;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original  = chain.request();
        SharedPreferences sharedPreferences = MyApplication.getApplication().getSharedPreferences("UserShao", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        String sessionId = sharedPreferences.getString("sessionId", "");
        Request.Builder newBuilder = original.newBuilder();
        newBuilder.method(original.method(),original.body());
        if (!TextUtils.isEmpty(userId)&&!TextUtils.isEmpty(sessionId)){
            newBuilder.addHeader("userId",userId);
            newBuilder.addHeader("sessionId",sessionId);
        }
        Request request = newBuilder.build();
        return chain.proceed(request);
    }
}
