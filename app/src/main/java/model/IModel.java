package model;

import java.util.Map;

import app.MyApplication;
import callback.MyCallBack;

public interface IModel {
    void requestDataGet(String url, String params, Class clazz, MyCallBack myCallBack);
    void requestDataPost(String url, Map<String,String> params,Class clazz,MyCallBack myCallBack);
    void requestDataPut(String url,Map<String,String> params,Class clazz,MyCallBack myCallBack);
    void requestDataDelete(String url, Class clazz, MyCallBack myCallBack);
    /**
     * 上传头像
     * */
    void requestImagePost(String url,Map<String,String> map,Class clazz,MyCallBack myCallBack);
    /**
     * 上传多张图片
     * */
    void imagePost(String url,Map<String,Object> map,Class clazz,MyCallBack myCallBack);
}
