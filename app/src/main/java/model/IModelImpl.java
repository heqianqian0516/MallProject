package model;

import android.util.Log;

import com.bwei.mallproject.R;
import com.google.gson.Gson;

import java.util.Map;

import api.RetrofitManager;
import callback.MyCallBack;
import util.NetUtil;

public class IModelImpl implements IModel {
    @Override
    public void requestDataGet(String url, String params, final Class clazz, final MyCallBack myCallBack) {
       if (!NetUtil.hasNetWork()){
              myCallBack.onFail("网络不可用，请检查网络");
       }else{

        RetrofitManager.getInstance().get(url, new RetrofitManager.HttpListener() {
            @Override
            public void onSuccess(String data) {
                try {
                    Object o = new Gson().fromJson(data, clazz);
                    if (myCallBack != null) {
                        myCallBack.onSuccess(o);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (myCallBack != null) {
                        myCallBack.onFail(e.getMessage());
                    }
                }
            }

            @Override
            public void onFail(String error) {
                if (myCallBack != null) {
                    myCallBack.onFail(error);
                }
            }
        });
       }
    }

    @Override
    public void requestDataPost(String url, Map<String, String> params, final Class clazz, final MyCallBack myCallBack) {
        if (!NetUtil.hasNetWork()){
            myCallBack.onFail("网络不可用，请检查网络");
        }else {

            RetrofitManager.getInstance().post(url, params, new RetrofitManager.HttpListener() {
                @Override
                public void onSuccess(String data) {
                    try {
                        Object o = new Gson().fromJson(data, clazz);
                        if (myCallBack != null) {
                            myCallBack.onSuccess(o);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (myCallBack != null) {
                            myCallBack.onFail(e.getMessage());
                        }
                    }
                }

                @Override
                public void onFail(String error) {
                    if (myCallBack != null) {
                        myCallBack.onFail(error);
                    }
                }
            });
        }
    }

    @Override
    public void requestDataPut(String url, Map<String, String> params, final Class clazz, final MyCallBack myCallBack) {
        if (!NetUtil.hasNetWork()){
            myCallBack.onFail("网络不可用，请检查网络");
        }else {
            RetrofitManager.getInstance().put(url, params, new RetrofitManager.HttpListener() {
                @Override
                public void onSuccess(String data) {
                    try {
                        Object o = new Gson().fromJson(data, clazz);
                        if (myCallBack != null) {
                            myCallBack.onSuccess(o);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    /*if (myCallBack != null) {
                        myCallBack.onFail(e.getMessage());
                    }*/
                    }
                }

                @Override
                public void onFail(String error) {
                    if (myCallBack != null) {
                        myCallBack.onFail(error);
                    }
                }
            });
        }
    }

    @Override
    public void requestDataDelete(String url, final Class clazz, final MyCallBack myCallBack) {
        if (!NetUtil.hasNetWork()){
            myCallBack.onFail("网络不可用，请检查网络");
        }else {
            RetrofitManager.getInstance().delete(url, new RetrofitManager.HttpListener() {
                @Override
                public void onSuccess(String data) {
                    try {
                        Object o = new Gson().fromJson(data, clazz);
                        if (myCallBack != null) {
                            myCallBack.onSuccess(o);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (myCallBack != null) {
                            myCallBack.onFail(e.getMessage());
                        }
                    }
                }

                @Override
                public void onFail(String error) {
                    if (myCallBack != null) {
                        myCallBack.onFail(error);
                        Log.e("onError:M", "onFail: " + error);
                    }
                }
            });
        }
    }

    @Override
    public void requestImagePost(String url, Map<String, String> map, final Class clazz, final MyCallBack myCallBack) {
        if (!NetUtil.hasNetWork()){
            myCallBack.onFail("网络不可用，请检查网络");
        }else {
            RetrofitManager.getInstance().imagePost(url, map, new RetrofitManager.HttpListener() {
                @Override
                public void onSuccess(String data) {
                    try {
                        Object o = new Gson().fromJson(data, clazz);
                        if (myCallBack != null) {
                            myCallBack.onSuccess(o);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (myCallBack != null) {
                            myCallBack.onFail(e.getMessage());
                        }
                    }
                }

                @Override
                public void onFail(String error) {
                    if (myCallBack != null) {
                        myCallBack.onFail(error);
                    }
                }
            });
        }
    }
  //上传图片
    @Override
    public void imagePost(String url, Map<String, Object> map, final Class clazz, final MyCallBack myCallBack) {
         RetrofitManager.getInstance().postFileMore(url, map, new RetrofitManager.HttpListener() {
             @Override
             public void onSuccess(String data) {
                 try {
                     Object o = new Gson().fromJson(data, clazz);
                     if (myCallBack != null) {
                         myCallBack.onSuccess(o);
                     }
                 } catch (Exception e) {
                     e.printStackTrace();
                     if (myCallBack != null) {
                         myCallBack.onFail(e.getMessage());
                     }
                 }
             }

             @Override
             public void onFail(String error) {
                 if (myCallBack != null) {
                     myCallBack.onFail(error);
                 }
             }
         });
    }
}
