package presenter;

import java.util.Map;

public interface IPresenter {
     void startRequestGet(String url,String params,Class clazz);
     void startRequestPost(String url, Map<String,String> params,Class clazz);
     void startRequestPut(String url,Map<String,String> params,Class clazz);
     void startRequestDelete(String url,Class clazz);
     /**
      * 上传头像
      * */
     void startImagHeadPost(String url,Map<String,String> map,Class clazz);
     /**
      * 多图上传
      * */
     void startImagsPost(String url,Map<String,Object> map,Class clazz);
}
