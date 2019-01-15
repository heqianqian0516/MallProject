package com.bwei.mallproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import adapter.SubmitBean;
import adapter.SubmitCarAdapter;
import api.Apis;
import bean.EventBean;
import bean.FindShoppingCartBean;
import bean.ShowShoppingBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import presenter.IPresenterImpl;
import view.IView;

public class SubmitActivity extends AppCompatActivity implements IView {

    @BindView(R.id.sub_recy)
    RecyclerView mSubRecy;
    @BindView(R.id.goods_num)
    TextView mGoodsNum;
    @BindView(R.id.goods_pay)
    TextView mGoodsPay;
    private IPresenterImpl presenter;
    private SubmitCarAdapter submitCarAdapter;
    private List<SubmitBean> submitBeans;
    private int num=0;
    private int totalPrice1;
    private ArrayList<? extends ShowShoppingBean.ResuleBean> checkList;
    private ShowShoppingBean list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        ButterKnife.bind(this);
        presenter = new IPresenterImpl(this);

      //  initView();


      // ArrayList<FindShoppingCartBean.ResultBean> checkList=intent.getParcelableArrayListExtra("checkList");

        initLoad();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mSubRecy.setLayoutManager(linearLayoutManager);
        submitCarAdapter = new SubmitCarAdapter(SubmitActivity.this);
        mSubRecy.setAdapter(submitCarAdapter);
    }

    private void initView() {
        submitBeans = new ArrayList<>();
        //接收值
        Intent intent=getIntent();
        checkList = intent.getParcelableArrayListExtra("checkList");
        if (checkList != null) {
            for (ShowShoppingBean.ResuleBean re : checkList) {
                num += re.getCount();
                totalPrice1 += re.getCount() * re.getPrice();
                submitBeans.add(new SubmitBean(re.getCommodityId(), re.getCount()));
            }
        }
        //totalPrice.setText("共" + num + "件商品，需付款" + totalPrice1 + "元");
       // submitCarAdapter.setmData(checkList);

    }

    private void initLoad() {
        //presenter.startRequestGet(Apis.URL_FIND_SHOPPING_CART_GET, null, ShowShoppingBean.class);
    }

    @Override
    public void getDataSuccess(Object data) {
        if (data instanceof ShowShoppingBean) {
            ShowShoppingBean showShoppingBean = (ShowShoppingBean) data;
            if (showShoppingBean == null) {
                Toast.makeText(SubmitActivity.this, showShoppingBean.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
              submitCarAdapter.setmData(showShoppingBean.getResult());
            }
        }
    }

    @Override
    public void getDataFail(String error) {

    }

    @OnClick({R.id.sub_recy, R.id.goods_num, R.id.goods_pay})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.sub_recy:
                break;
            case R.id.goods_num:
                break;
            case R.id.goods_pay:
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onEvent(EventBean evBean) {
        if (evBean.getName().equals("list")) {
            list = (ShowShoppingBean) evBean.getClazz();
            submitCarAdapter.setmData(list.getResult());
           // totalMoney();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
