package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


import com.bwei.mallproject.R;
import com.bwei.mallproject.SubmitActivity;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.ShopCarAdapter;
import api.Apis;
import bean.AddAddrBean;
import bean.AddShoppingCarBean;
import bean.EventBean;
import bean.FindShoppingCartBean;
import bean.GoodsBean;
import bean.ShoppingCarBean;
import bean.ShowShoppingBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import presenter.IPresenterImpl;
import view.IView;

public class ShopFragment extends Fragment implements IView {
    private static final String TAG = "ShopFragment+++++";
    @BindView(R.id.shop_recy)
    RecyclerView mShopRecy;
    @BindView(R.id.cb_cart_all_select)
    CheckBox checkAll;
    @BindView(R.id.tv_cart_total_price)
    TextView mTotalPrice;
    @BindView(R.id.btn_cart_pay)
    Button mBtnCartPay;
    private View view;
    private Unbinder unbinder;
    private IPresenterImpl presenter;
    private ShopCarAdapter shopCarAdapter;
    private ArrayList<ShowShoppingBean.ResuleBean> checkList;
    private ShowShoppingBean showShoppingBean;
    private GoodsBean goodsBean;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shopfragment_layout, null);
        presenter = new IPresenterImpl(this);
        unbinder = ButterKnife.bind(this, view);
        initLoad();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        mShopRecy.setLayoutManager(linearLayoutManager);

        shopCarAdapter = new ShopCarAdapter(getActivity());
        mShopRecy.setAdapter(shopCarAdapter);
         //1选中
        checkAll.setTag(1);
        shopCarAdapter.setUpdateListener(new ShopCarAdapter.UpdateListener() {
            @Override
            public void setTotal(String total, String num, boolean allCheck) {
                //设置ui的改变
                mTotalPrice.setText("合计 :¥"+total+"元");//总价
                if (allCheck){
                    checkAll.setTag(2);
                  //  checkAll.setBackgroundResource(R.drawable.ic_action_selected);
                }else {
                    checkAll.setTag(1);
                   // checkAll.setBackgroundResource(R.drawable.ic_action_unselected);
                }
                checkAll.setChecked(allCheck);
            }
        });
        checkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用adapter里面的方法 ,,把当前checkAll状态传递过去
                int tag = (int) checkAll.getTag();
                if(tag==1){
                    checkAll.setTag(2);
                   // checkAll.setBackgroundResource(R.drawable.ic_action_selected);
                }else{
                    checkAll.setTag(1);
                   // checkAll.setBackgroundResource(R.drawable.ic_action_unselected);
                }
                shopCarAdapter.checkAll(checkAll.isChecked());

            }
        });
        shopCarAdapter.setShopCarListener(new ShopCarAdapter.ShopCarListener() {
            @Override
            public void callBack(List<ShowShoppingBean.ResuleBean> mlist) {
                showShoppingBean.getResult().clear();
                String s = new Gson().toJson(mlist);
                Map<String,String> map=new HashMap<>();
                map.put("data",s);
                presenter.startRequestPut(Apis.URL_SYNC_SHOPPING_CART_PUT,map,AddAddrBean.class);
                showShoppingBean.getResult().addAll(mlist);
                totalMoney();
            }
        });
        initLoad();
    }

    private void totalMoney() {
        int money=0;
        boolean ischeck=true;
        for (int i = 0; i < showShoppingBean.getResult().size(); i++) {
            if (showShoppingBean.getResult().get(i).isItem_check()){
                money+=showShoppingBean.getResult().get(i).getPrice()*showShoppingBean.getResult().get(i).getCount();
            }else {
                ischeck=false;
            }
        }

    }


    private void initLoad() {
        presenter.startRequestGet(Apis.URL_FIND_SHOPPING_CART_GET,null,ShowShoppingBean.class);
    }

    @Override
    public void getDataSuccess(Object data) {
        Log.d(TAG, "getDataSuccess: ++++++"+ShowShoppingBean.class);
         if (data instanceof ShowShoppingBean){
              showShoppingBean= (ShowShoppingBean) data;
             if (showShoppingBean==null|| !showShoppingBean.isSuceess()){
                 Toast.makeText(getActivity(),showShoppingBean.getMessage(),Toast.LENGTH_SHORT).show();
             }else{
                 shopCarAdapter.setmData(showShoppingBean.getResult());
             }
         }
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onEvent(EventBean evBean) {
        if (evBean.getName().equals("myDialog")){
            goodsBean = (GoodsBean) evBean.getClazz();
        }
    }

    @Override
    public void getDataFail(String error) {

    }
    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
    @OnClick({R.id.shop_recy, R.id.cb_cart_all_select, R.id.tv_cart_total_price, R.id.btn_cart_pay})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.shop_recy:
                break;
            case R.id.cb_cart_all_select:
                break;
            case R.id.tv_cart_total_price:
                break;
            case R.id.btn_cart_pay:
                ShowShoppingBean lists=new ShowShoppingBean();
                for (int i = 0; i < showShoppingBean.getResult().size(); i++) {
                    if (showShoppingBean.getResult().get(i).isItem_check()){
                        lists.addResult(showShoppingBean.getResult().get(i));
                    }
                }
                EventBus.getDefault().postSticky(new EventBean("list",lists));

                Intent intent=new Intent(getActivity(),SubmitActivity.class);

                startActivity(intent);

                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
