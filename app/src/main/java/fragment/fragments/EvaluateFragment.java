package fragment.fragments;

import android.content.Intent;
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
import com.bwei.mallproject.RemaitActivity;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import api.Apis;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mine.bean.DeleteCircleBean;
import order.bean.DeleteOrderBean;
import order.bean.OrderBean;
import order.OrderRemaitAdaper;
import presenter.IPresenterImpl;
import view.IView;
/**
 * 待评价
 * **/
public class EvaluateFragment extends Fragment implements IView {
    @BindView(R.id.recycleview)
    XRecyclerView mRecycleview;
    private View view;
    private Unbinder unbinder;
    private IPresenterImpl presenter;
    private int status = 0;
    private int page;
    private int count = 5;
    private OrderRemaitAdaper remaitAdaper;
    private List<OrderBean.OrderListBean> orderList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.evaluate_layout, null);
        unbinder = ButterKnife.bind(this, view);
        presenter = new IPresenterImpl(this);
        initLoad();
        return view;
    }

    private void initLoad() {
        presenter.startRequestGet(String.format(Apis.URL_FIND_ORDER_LIST_BYSTATUS_GET, status, page, count), null, OrderBean.class);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        page = 1;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecycleview.setLayoutManager(linearLayoutManager);
        remaitAdaper = new OrderRemaitAdaper(getActivity());
        mRecycleview.setAdapter(remaitAdaper);
        mRecycleview.setPullRefreshEnabled(true);
        mRecycleview.setLoadingMoreEnabled(true);
        mRecycleview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                initLoad();
            }

            @Override
            public void onLoadMore() {
                initLoad();
            }
        });
        //删除
        remaitAdaper.setCallBackDel(new OrderRemaitAdaper.CallBackDel() {
            @Override
            public void callBack(String orderId, int position) {
                remaitAdaper.setDel(position);
                presenter.startRequestDelete(String.format(Apis.URL_DELETE_ORDER_DELETE, orderId), DeleteOrderBean.class);
            }
        });
        //去评价
        remaitAdaper.setCallBackRemait(new OrderRemaitAdaper.CallBackRemait() {
            @Override
            public void callBackRem(String orderId, OrderBean.OrderListBean.DetailListBean dataBean) {
                Intent intent=new Intent(getActivity(),RemaitActivity.class);
                intent.putExtra("orderId", orderId);
                intent.putExtra("dataBean", dataBean);
                startActivity(intent);
            }
        });
    }



    @Override
    public void getDataSuccess(Object data) {
        if (data instanceof OrderBean) {
            OrderBean orderBean = (OrderBean) data;
            orderList = orderBean.getOrderList();
            if (orderBean == null || !orderBean.isSuccess()) {
                Toast.makeText(getActivity(), orderBean.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                if (page == 1) {
                    remaitAdaper.setmOrder(orderBean.getOrderList());
                    remaitAdaper.notifyDataSetChanged();
                } else {
                    remaitAdaper.addmOrder(orderBean.getOrderList());
                }
                page++;
                mRecycleview.loadMoreComplete();
                mRecycleview.refreshComplete();
                remaitAdaper.notifyDataSetChanged();
            }
        }
        else if (data instanceof DeleteOrderBean){
            DeleteOrderBean deleteOrderBean= (DeleteOrderBean) data;

            Toast.makeText(getActivity(),deleteOrderBean.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getDataFail(String error) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.onDetach();
    }
}
