package presenter;

import java.util.Map;

import view.IView;
import callback.MyCallBack;
import model.IModelImpl;

public class IPresenterImpl implements IPresenter {

    private IModelImpl model;
    private  IView iView;

    public IPresenterImpl(IView iView){
        this.iView=iView;
        model = new IModelImpl();
    }
    @Override
    public void startRequestGet(String url, String params, Class clazz) {
        model.requestDataGet(url, params, clazz, new MyCallBack() {
            @Override
            public void onSuccess(Object data) {
                iView.getDataSuccess(data);
            }

            @Override
            public void onFail(String error) {
               iView.getDataFail(error);
            }
        });
    }

    @Override
    public void startRequestPost(String url, Map<String, String> params, Class clazz) {
         model.requestDataPost(url, params, clazz, new MyCallBack() {
             @Override
             public void onSuccess(Object data) {
                 iView.getDataSuccess(data);
             }

             @Override
             public void onFail(String error) {
                iView.getDataFail(error);
             }
         });
    }

    @Override
    public void startRequestPut(String url, Map<String, String> params, Class clazz) {
        model.requestDataPut(url, params, clazz, new MyCallBack() {
            @Override
            public void onSuccess(Object data) {
                iView.getDataSuccess(data);
            }

            @Override
            public void onFail(String error) {
               iView.getDataFail(error);
            }
        });
    }

    public void onDetach(){
        if (model!=null){
            model=null;
        }
        if (iView!=null){
            iView=null;
        }
    }
}
