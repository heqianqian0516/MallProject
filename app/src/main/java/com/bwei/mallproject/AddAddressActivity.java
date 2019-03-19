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


import java.util.HashMap;
import java.util.Map;

import adapter.AddrLstAdapter;
import api.Apis;
import bean.AddAddrBean;
import bean.QueryAddrBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mine.bean.AddressBean;
import mine.bean.DefaultAddressBean;
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
        loadData();
    }

    private void initView() {
        presenter=new IPresenterImpl(this);
        //创建适配器
        lstAdapter=new AddrLstAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(lstAdapter);

       lstAdapter.setCallBackUpdata(new AddrLstAdapter.CallBackUpdata() {
           @Override
           public void callBack(AddressBean.ResultBean resultBean) {
               Intent intent = new Intent(AddAddressActivity.this,CityListActivity.class);
               intent.putExtra("resultBean",resultBean);
               startActivityForResult(intent,100);
           }
       });
       lstAdapter.setCallBackDefault(new AddrLstAdapter.CallBackDefault() {
           @Override
           public void callBack(int id) {
               Map<String,String> map = new HashMap<>();
               map.put("id",String.valueOf(id));
               presenter.startImagHeadPost(Apis.URL_SET_DEFAULT_RECEIVE_ADDRESS_POST,map, DefaultAddressBean.class);
           }
       });

    }

    private void loadData() {
        presenter.startRequestGet(Apis.URL_RECEIVE_ADDRESS_GET,null, AddressBean.class);
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
                startActivityForResult(intent,100);
                break;
                default:
                    break;
        }
    }

    @Override
    public void getDataSuccess(Object data) {
        if(data instanceof AddressBean){
            AddressBean addressBean = (AddressBean) data;
            if(addressBean==null || !addressBean.isSuccess()){
                Toast.makeText(AddAddressActivity.this,addressBean.getMessage(),Toast.LENGTH_SHORT).show();
            }else{
                lstAdapter.setData(addressBean.getResult());
            }
        }else if(data instanceof DefaultAddressBean){
            DefaultAddressBean defaultAddressBean = (DefaultAddressBean) data;
            Toast.makeText(AddAddressActivity.this,defaultAddressBean.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(AddAddressActivity.this,"请添加地址",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode==200){
           // presenter.getRequest(Apis.URL_RECEIVE_ADDRESS_LIST_GET,AddressBean.class);
            presenter.startRequestGet(Apis.URL_RECEIVE_ADDRESS_GET,null, AddressBean.class);

        }
    }
}
