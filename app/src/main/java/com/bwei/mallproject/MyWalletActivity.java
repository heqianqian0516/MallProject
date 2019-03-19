package com.bwei.mallproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import api.Apis;
import butterknife.BindView;
import butterknife.ButterKnife;
import mine.adapter.WalletAdaper;
import mine.bean.WalletBean;
import presenter.IPresenterImpl;
import view.IView;

public class MyWalletActivity extends AppCompatActivity implements IView {

    @BindView(R.id.wallet_back)
    ImageView mWalletBack;
    @BindView(R.id.balance)
    TextView mBalance;
    @BindView(R.id.money)
    TextView mMoney;
    @BindView(R.id.sum)
    TextView mSum;
    @BindView(R.id.time)
    TextView mTime;
    @BindView(R.id.view_line)
    View mViewLine;
    @BindView(R.id.view_linev)
    View mViewLinev;
    @BindView(R.id.wallet_recycle)
    RecyclerView mWalletRecycle;
    private WalletAdaper walletAdaper;
    private int mPage = 1;
    private int mCount = 20;
    private IPresenterImpl presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        ButterKnife.bind(this);
        presenter = new IPresenterImpl(this);
        initData();
        initView();
    }
   //初始化数据
    private void initView() {
        //创建布局管理器
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MyWalletActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mWalletRecycle.setLayoutManager(linearLayoutManager);
        //创建适配器
        walletAdaper = new WalletAdaper(MyWalletActivity.this);
        mWalletRecycle.setAdapter(walletAdaper);
    }

    //加载网络
    private void initData() {
      presenter.startRequestGet(String.format(Apis.URL_FIND_USER_WALLET_GET,mPage,mCount),null,WalletBean.class);
    }

    @Override
    public void getDataSuccess(Object data) {
         if (data instanceof WalletBean){
             WalletBean walletBean= (WalletBean) data;
             WalletBean.ResultBean result=walletBean.getResult();
             if (walletBean==null){
                 Toast.makeText(MyWalletActivity.this,walletBean.getMessage(),Toast.LENGTH_SHORT).show();

             }else{
                 mMoney.setText(result.getBalance()+"");
                 walletAdaper.setmBean(walletBean.getResult().getDetailList());
             }
         }
    }

    @Override
    public void getDataFail(String error) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       presenter.onDetach();
    }
}
