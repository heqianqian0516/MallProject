package fragment.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bwei.mallproject.R;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.Map;

import api.Apis;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import order.bean.OrderBean;
import order.OrderWaitAdaper;
import order.bean.TakeBean;
import presenter.IPresenterImpl;
import view.IView;
/**
 * 待收货
 * */
public class GoodsFragment extends Fragment implements IView {
    @BindView(R.id.recycleview)
    XRecyclerView mRecycleview;
    private View view;
    private Unbinder unbinder;
    private IPresenterImpl presenter;

    private int status = 2;
    private int page;
    private int count = 5;
    private OrderWaitAdaper waitAdaper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goods_layout, null);
        unbinder = ButterKnife.bind(this, view);
        presenter = new IPresenterImpl(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        page = 1;


        //创建布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecycleview.setLayoutManager(layoutManager);
        waitAdaper = new OrderWaitAdaper(getActivity());
        mRecycleview.setAdapter(waitAdaper);
        mRecycleview.setPullRefreshEnabled(true);
        mRecycleview.setPullRefreshEnabled(true);
        mRecycleview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                initLaod();
            }

            @Override
            public void onLoadMore() {
                initLaod();
            }
        });
        initLaod();
        //确认收货
        waitAdaper.setCallBackWait(new OrderWaitAdaper.CallBackWait() {
            @Override
            public void callBack(String orderId) {
                Map<String,String> map = new HashMap<>();
                map.put("orderId",orderId);
                presenter.startRequestPut(Apis.URL_CONFIRM_RECEIPT_PUT,map,TakeBean.class);
            }
        });
    }

    private void initLaod() {
        presenter.startRequestGet(String.format(Apis.URL_FIND_ORDER_LIST_BYSTATUS_GET,status,page,count),null,OrderBean.class);
    }

    @Override
    public void getDataSuccess(Object data) {
        // Log.d(TAG, "getDataSuccess: +++++++" + OrderBean.class);
        if(data instanceof OrderBean){
            OrderBean orderBean = (OrderBean) data;
            if(orderBean==null || !orderBean.isSuccess()){
                Toast.makeText(getActivity(),orderBean.getMessage(),Toast.LENGTH_SHORT).show();
            }else{
                if(page == 1){
                    waitAdaper.setmOrder(orderBean.getOrderList());
                }else{
                    waitAdaper.addmOrder(orderBean.getOrderList());
                }
                page++;
                mRecycleview.loadMoreComplete();
                mRecycleview.refreshComplete();
                waitAdaper.notifyDataSetChanged();
            }
        }else if(data instanceof TakeBean){
            TakeBean takeBean = (TakeBean) data;
            Toast.makeText(getActivity(),takeBean.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(getActivity(), "确认收货失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.onDetach();
    }
}
