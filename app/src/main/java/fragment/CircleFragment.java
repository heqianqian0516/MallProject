package fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bwei.mallproject.R;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.Map;

import bean.CircleCanclePraiseBean;
import bean.CircleGivePraiseBean;
import view.IView;
import adapter.CircleAdapter;
import api.Apis;
import bean.CircleBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import presenter.IPresenterImpl;
/**
 * 我的圈子列表展示
 *
 * **/
public class CircleFragment extends Fragment implements IView {
    private static final String TAG = "CircleFragment++++++++";

    @BindView(R.id.recy)
    XRecyclerView mRecy;
    private View view;
    private IPresenterImpl presenter;
    private Unbinder unbinder;

    private int count=5;
    private CircleAdapter adapter;
    private int mPage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.circlefragment_layout, null);
        presenter = new IPresenterImpl(this);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        return view;

    }

    private void initData() {
        Log.d(TAG, "initData: ++++++++"+Apis.URL_FIND_CIRCLE_LIST_GET+"*************");
     presenter.startRequestGet(String.format(Apis.URL_FIND_CIRCLE_LIST_GET,mPage,count),null,CircleBean.class);

    }

    private void initView() {
        mPage=1;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecy.setLayoutManager(layoutManager);
        adapter = new CircleAdapter(getActivity());
        mRecy.setAdapter(adapter);
        adapter.setClickCallBack(new CircleAdapter.ClickCallBack() {
            @Override
            public void callBack(int i, int position, int id) {
                //取消点赞
                if (i==1){
                    presenter.startRequestDelete(String.format(Apis.URL_CANCLE_CIRCLE_GREAT_DELETE, id), CircleCanclePraiseBean.class);
                    adapter.getCancelPraise(position);
                }else if(i==2){
                    //点赞
                    Map<String, String> map = new HashMap<>();
                    map.put("circleId", String.valueOf(id));
                    presenter.startRequestPost(Apis.URL_ADD_CIRCLE_GREAT_POST, map, CircleGivePraiseBean.class);
                    adapter.getGivePraise(position);
                }
            }
        });
        mRecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage++;
                initData();
            }

            @Override
            public void onLoadMore() {
                initData();
            }
        });
    }

    @Override
    public void getDataSuccess(Object data) {
        Log.d(TAG, "getDataSuccess: +++++++++"+CircleBean.class);
         if (data instanceof CircleBean){
             CircleBean bean= (CircleBean) data;
             if (!bean.getMessage().equals("查询成功")){
                 Toast.makeText(getActivity(),bean.getMessage(),Toast.LENGTH_SHORT).show();
             }else{
                 adapter.setmData(bean.getResult());
                 if (mPage==1){
                     adapter.setmData(bean.getResult());
                 }else{
                     adapter.addmResult(bean.getResult());
                 }
                 mPage++;
                 mRecy.loadMoreComplete();
                 mRecy.refreshComplete();
             }
         }
         //点赞
         else if(data instanceof CircleGivePraiseBean){
             CircleGivePraiseBean givePraiseBean= (CircleGivePraiseBean) data;
             Toast.makeText(getActivity(), givePraiseBean.getMessage(), Toast.LENGTH_SHORT).show();
          //取消点赞
         }else if(data instanceof CircleCanclePraiseBean){
             CircleCanclePraiseBean canclePraiseBean= (CircleCanclePraiseBean) data;
             Toast.makeText(getActivity(), canclePraiseBean.getMessage(), Toast.LENGTH_SHORT).show();

         }


    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.onDetach();
    }


}
