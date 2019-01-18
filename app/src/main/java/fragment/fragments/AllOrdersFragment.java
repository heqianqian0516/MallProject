package fragment.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bwei.mallproject.PayActivity;
import com.bwei.mallproject.R;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.Map;

import api.Apis;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import order.OrdelAllAdaper;
import order.OrderBean;
import order.TakeBean;
import presenter.IPresenterImpl;
import view.IView;

public class AllOrdersFragment extends Fragment implements IView {
    private static final String TAG = "AllOrdersFragment+++++++";
    @BindView(R.id.recycleview)
    XRecyclerView mRecycleview;
   /* @BindView(R.id.dingdan)
    TextView mDingdan;
    @BindView(R.id.dingdanhao)
    TextView mDingdanhao;
    @BindView(R.id.allordersDate)
    TextView mAllordersDate;
    @BindView(R.id.allordersRecycle)
    RecyclerView mAllordersRecycle;
    @BindView(R.id.relativeTv4)
    TextView mRelativeTv4;
    @BindView(R.id.allordersCount)
    TextView mAllordersCount;
    @BindView(R.id.relativeTv5)
    TextView mRelativeTv5;
    @BindView(R.id.allordersPrice)
    TextView mAllordersPrice;
    @BindView(R.id.yuan)
    TextView mYuan;*/

    private View view;
    private Unbinder unbinder;
    private int status = 0;
    private int page;
    private int count = 5;
    private IPresenterImpl presenter;
    private OrdelAllAdaper ordelAllAdaper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.allorders_layout, null);
        unbinder = ButterKnife.bind(this, view);
        presenter = new IPresenterImpl(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //  loadData();
        page = 1;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecycleview.setLayoutManager(linearLayoutManager);
        ordelAllAdaper = new OrdelAllAdaper(getActivity());
        mRecycleview.setAdapter(ordelAllAdaper);
        mRecycleview.setPullRefreshEnabled(true);
        mRecycleview.setLoadingMoreEnabled(true);
        mRecycleview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadData();
            }

            @Override
            public void onLoadMore() {
                loadData();
            }
        });
        loadData();
        //去支付
        ordelAllAdaper.setCallBackPay(new OrdelAllAdaper.CallBackPay() {
            @Override
            public void callBack(String orderId, Double payAmount) {
                Intent intent=new Intent(getActivity(),PayActivity.class);
                intent.putExtra("orderId",orderId);
                intent.putExtra("payAmount",payAmount);
                startActivity(intent);
            }
        });
        //确认收货
        ordelAllAdaper.setCallBackWait(new OrdelAllAdaper.CallBackWait() {
            @Override
            public void callBack(String orderId) {
                Map<String,String> map=new HashMap<>();
                map.put("orderId",orderId);
                presenter.startRequestPut(Apis.URL_CONFIRM_RECEIPT_PUT,null,TakeBean.class);
            }
        });
    }

    private void loadData() {
        presenter.startRequestGet(String.format(Apis.URL_FIND_ORDER_LIST_BYSTATUS_GET, status, page, count), null, OrderBean.class);
    }

    @Override
    public void getDataSuccess(Object data) {
        // Log.d(TAG, "getDataSuccess: +++++++" + OrderBean.class);
        if (data instanceof OrderBean) {
            OrderBean orderBean = (OrderBean) data;
            if (orderBean == null || !orderBean.isSuccess()) {
                Toast.makeText(getActivity(), orderBean.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                if (page == 1) {
                    ordelAllAdaper.setmOrder(orderBean.getOrderList());
                } else {
                    ordelAllAdaper.addmOrder(orderBean.getOrderList());
                }
                page++;
                mRecycleview.loadMoreComplete();
                mRecycleview.refreshComplete();
            }
        }else if (data instanceof TakeBean){
              TakeBean takeBean= (TakeBean) data;
              Toast.makeText(getActivity(),takeBean.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        presenter.onDetach();
    }
}
