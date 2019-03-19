package api;

import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import Interceptor.LoggingInterceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RetrofitManager<T> {
    private static final String TAG = "RetrofitManager++++++";
    //内网172.17.8.100
    //外网mobile.bwstudent.com
    private String BASE_URL="http://172.17.8.100/small/";
    private static RetrofitManager manager;
    private final BaseApis mBaseApis;


    public static synchronized RetrofitManager getInstance(){
        if (manager==null){
            manager = new RetrofitManager();
        }
        return manager;
    }
    public RetrofitManager(){
       /* HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);*/
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        builder.connectTimeout(15,TimeUnit.SECONDS);
        builder.readTimeout(15,TimeUnit.SECONDS);
        builder.writeTimeout(15,TimeUnit.SECONDS);
        builder.addInterceptor(new LoggingInterceptor());
        builder.retryOnConnectionFailure(true);
        OkHttpClient client=builder.build();
        Retrofit retrofit=new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .baseUrl(BASE_URL)
                .build();
        mBaseApis = retrofit.create(BaseApis.class);

    }
    public Map<String, RequestBody> generateRequestBody(Map<String,String> requestDataMap){
        Map<String, RequestBody> requestBodyMap=new HashMap<>();
        for (String key:requestBodyMap.keySet()){
            RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"),
                    requestDataMap.get(key)==null?"":requestDataMap.get(key));
            requestBodyMap.put(key,requestBody);
        }
        return requestBodyMap;
    }
    //get请求
    public void get(String url,HttpListener httpListener){
        mBaseApis.get(url)
                //后台执行在哪个线程中
                .subscribeOn(Schedulers.io())
                //最终完成后执行在哪个线程
                .observeOn(AndroidSchedulers.mainThread())
                //设置我们的rxJava
                .subscribe(getObserver(httpListener));

    }
    //表单post请求
    public void postFormBody(String url, Map<String, RequestBody> map,HttpListener httpListener){
        if (map==null){
            map=new HashMap<>();
        }
        mBaseApis.postFormBody(url,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(httpListener));

    }
    //普通post请求
    public void post(String url, Map<String,String> map,HttpListener httpListener){
        if (map==null){
            map=new HashMap<>();
        }
        mBaseApis.post(url, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(httpListener));

    }
    /**
     *  普通put
     * */
    public void put(String url,Map<String,String> map,HttpListener httpListener){
        if(map == null){
            map = new HashMap<>();
        }
        mBaseApis.put(url,map)
                // 后台执行在哪个线程
                .subscribeOn(Schedulers.io())
                //最终完成后执行在哪个线程
                .observeOn(AndroidSchedulers.mainThread())
                //设置rxjava
                .subscribe(getObserver(httpListener));

    }
    //delete请求
    public void delete(String url,HttpListener httpListener){
        mBaseApis.get(url)
                //后台执行在哪个线程中
                .subscribeOn(Schedulers.io())
                //最终完成后执行在哪个线程
                .observeOn(AndroidSchedulers.mainThread())
                //设置我们的rxJava
                .subscribe(getObserver(httpListener));
    }
    /**
     *上传头像
     * */
    public static MultipartBody filesMultipar(Map<String,String> map){
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for(Map.Entry<String,String> entry:map.entrySet()){
            File file = new File(entry.getValue());
            builder.addFormDataPart(entry.getKey(),"tp.png",
                    RequestBody.create(MediaType.parse("multipart/form-data"),file));
        }
        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }
    public void imagePost(String url, Map<String,String> map,HttpListener listener){
        if(map == null){
            map = new HashMap<>();
        }
        MultipartBody multipartBody = filesMultipar(map);
        mBaseApis.imagePost(url,multipartBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(listener));
    }
    /**
     * 上传多张图片
     */
    public void postFileMore(String url, Map<String, Object> map,HttpListener listener) {
        if (map == null) {
            map = new HashMap<>();
        }
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("commodityId", String.valueOf(map.get("commodityId")));
        if(!String.valueOf(map.get("orderId")).equals("")){
            builder.addFormDataPart("orderId", String.valueOf(map.get("orderId")));
        }
        builder.addFormDataPart("content", String.valueOf(map.get("content")));
        if (!map.get("image").equals("")) {
            List<String> image = (List<String>) map.get("image");
            for(int i=0;i<image.size();i++){
                File file = new File(image.get(i));
                builder.addFormDataPart("image", file.getName(),RequestBody.create(MediaType.parse("multipart/form-data"),file));
            }

        }
        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        mBaseApis.imagePost(url, multipartBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(listener));
    }

    private Observer getObserver(final HttpListener httpListener){
     Observer observer=new Observer<ResponseBody>(){


        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (httpListener != null) {
                httpListener.onFail(e.getMessage());
                Log.e(TAG, "onError: "+e.getMessage());
            }
        }

        @Override
        public void onNext(ResponseBody responseBody) {
            try {
                String data = responseBody.string();
                if (httpListener!=null){
                    httpListener.onSuccess(data);
                }
            } catch (IOException e) {
                e.printStackTrace();
                /*if (httpListener != null) {
                    httpListener.onFail(e.getMessage());
                }*/
            }
        }
    };
    return observer;
    }

    public interface HttpListener{
        void onSuccess(String data);
        void onFail(String error);
    }
}