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

import api.Apis;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import order.bean.OrderBean;
import order.OrderStockAdaper;
import presenter.IPresenterImpl;
import view.IView;

/**
 * 已完成
 **/
public class CompletedFragment extends Fragment implements IView {
    @BindView(R.id.recycleview)
    XRecyclerView mRecycleview;
    private View view;
    private Unbinder unbinder;
    private int status = 9;
    private int page;
    private int count = 5;
    private IPresenterImpl presenter;
    private OrderStockAdaper stockAdaper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.completed_layout, null);
        unbinder = ButterKnife.bind(this, view);
        presenter = new IPresenterImpl(this);
        intiView();
       // initData();
        return view;
    }

    private void initData() {
        presenter.startRequestGet(String.format(Apis.URL_FIND_ORDER_LIST_BYSTATUS_GET,status,page,count),null,OrderBean.class);

    }

    private void intiView() {
        page = 1;


        //创建布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecycleview.setLayoutManager(layoutManager);
        //创建适配器
        stockAdaper = new OrderStockAdaper(getActivity());
        mRecycleview.setAdapter(stockAdaper);
        mRecycleview.setPullRefreshEnabled(true);
        mRecycleview.setPullRefreshEnabled(true);
        mRecycleview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
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
        if(data instanceof OrderBean){
            OrderBean orderBean = (OrderBean) data;
            if(orderBean==null || !orderBean.isSuccess()){
                Toast.makeText(getActivity(),orderBean.getMessage(),Toast.LENGTH_SHORT).show();
            }else{
                if(page == 1){
                    stockAdaper.setmOrder(orderBean.getOrderList());
                    stockAdaper.notifyDataSetChanged();
                }else{
                    stockAdaper.addmOrder(orderBean.getOrderList());
                }
                page++;
                mRecycleview.loadMoreComplete();
                mRecycleview.refreshComplete();
            }
        }

    }

    @Override
    public void getDataFail(String error) {

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }
}
