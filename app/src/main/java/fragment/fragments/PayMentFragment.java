package fragment.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bwei.mallproject.PayActivity;
import com.bwei.mallproject.R;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import api.Apis;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import fragment.OrderFragment;
import order.bean.DeleteOrderBean;
import order.bean.OrderBean;
import order.OrderObligationAdaper;
import presenter.IPresenterImpl;
import view.IView;
/**
 * 代付款
 * **/
public class PayMentFragment extends Fragment implements IView {
    @BindView(R.id.recycleview)
    XRecyclerView mRecycleview;
    private View view;
    private Unbinder unbinder;
    private IPresenterImpl presenter;
    private int status = 0;
    private int page=1;
    private int count = 5;
    private OrderObligationAdaper obligationAdaper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payment_layout, null);
        presenter = new IPresenterImpl(this);

        unbinder = ButterKnife.bind(this, view);
        return view;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //创建布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecycleview.setLayoutManager(linearLayoutManager);
        //创建适配器
        obligationAdaper = new OrderObligationAdaper(getActivity());
        mRecycleview.setAdapter(obligationAdaper);
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

        //去支付
      obligationAdaper.setCallBackPay(new OrderObligationAdaper.CallBackPay() {
          @Override
          public void callBack(String orderId, Double payAmount) {
              Intent intent = new Intent(getActivity(),PayActivity.class);
              intent.putExtra("orderId",orderId);
              intent.putExtra("payAmount",payAmount);
              startActivity(intent);
          }
      });
      //取消订单

       obligationAdaper.setCallBackAll(new OrderObligationAdaper.CallBackObligation() {
           @Override
           public void callBack(String orderId, int position) {
               obligationAdaper.setDel(position);
               presenter.startRequestDelete(String.format(Apis.URL_DELETE_ORDER_DELETE,orderId), DeleteOrderBean.class);
           }
       });
    }
    //请求网络根据订单状态查询订单信息
    private void initLoad() {
        presenter.startRequestGet(String.format(Apis.URL_FIND_ORDER_LIST_BYSTATUS_GET, status, page, count), null, OrderBean.class);

    }

    @Override
    public void getDataSuccess(Object data) {
        if (data instanceof OrderBean) {
            OrderBean orderBean= (OrderBean) data;
            if (orderBean == null || !orderBean.isSuccess()) {
                Toast.makeText(getActivity(), orderBean.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                if (page == 1) {
                    obligationAdaper.setmOrder(orderBean.getOrderList());
                } else {
                    obligationAdaper.addmOrder(orderBean.getOrderList());
                }
                page++;
                mRecycleview.loadMoreComplete();
                mRecycleview.refreshComplete();
                obligationAdaper.notifyDataSetChanged();
            }
        }
    }
    @Override
    public void getDataFail(String error) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        presenter.onDetach();
    }
}
