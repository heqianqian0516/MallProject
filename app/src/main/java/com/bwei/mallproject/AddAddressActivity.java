package com.bwei.mallproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import adapter.AddrLstAdapter;
import api.Apis;
import bean.QueryAddrBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import presenter.IPresenterImpl;
import view.IView;

public class AddAddressActivity extends AppCompatActivity implements IView {

    @BindView(R.id.activity_myaddress_text_finish)
    TextView activityMyaddressTextFinish;
    @BindView(R.id.activity_myaddress_recyclerview_address)
    RecyclerView recyclerView;
    @BindView(R.id.activity_myaddress_btn_add)
    Button activityMyaddressBtnAdd;
    private AddrLstAdapter lstAdapter;
    private IPresenterImpl presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        presenter=new IPresenterImpl(this);
        lstAdapter=new AddrLstAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(lstAdapter);
        loadData();
    }

    private void loadData() {
        presenter.startRequestGet(Apis.URL_RECEIVE_ADDRESS_GET,null, QueryAddrBean.class);
    }

    @OnClick({R.id.activity_myaddress_text_finish, R.id.activity_myaddress_btn_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_myaddress_text_finish:
                finish();
                break;
            case R.id.activity_myaddress_btn_add:
                Intent intent=new Intent(AddAddressActivity.this,
                        CityListActivity.class);
                startActivity(intent);
                break;
                default:
                    break;
        }
    }

    @Override
    public void getDataSuccess(Object data) {
        if (data instanceof QueryAddrBean){
            QueryAddrBean addrBean= (QueryAddrBean) data;
            lstAdapter.setData(addrBean.getResult());
        }
    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(AddAddressActivity.this,"12123",Toast.LENGTH_LONG).show();
    }
}
