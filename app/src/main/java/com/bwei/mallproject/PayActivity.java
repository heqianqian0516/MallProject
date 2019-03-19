package com.bwei.mallproject;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import api.Apis;
import bean.PayOrderBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import presenter.IPresenterImpl;
import view.IView;

public class PayActivity extends AppCompatActivity implements IView {
    @BindView(R.id.way)
    TextView way;
    @BindView(R.id.balancepaid)
    ImageView balancepaid;
    @BindView(R.id.balancepaid_text)
    TextView balancepaidText;
    @BindView(R.id.alipay)
    ImageView alipay;
    @BindView(R.id.alipay_text)
    TextView alipayText;
    @BindView(R.id.wechat)
    ImageView wechat;
    @BindView(R.id.wechat_text)
    TextView wechatText;
    @BindView(R.id.alipay_radio)
    RadioButton alipayRadio;
    @BindView(R.id.balancepaid_radio)
    RadioButton balancepaidRadio;
    @BindView(R.id.wechat_radio)
    RadioButton wechatRadio;
    @BindView(R.id.text)
    TextView textView;
    @BindView(R.id.success)
    ImageView success;
    @BindView(R.id.success_text)
    TextView successText;
    @BindView(R.id.homebutton)
    Button homebutton;
    @BindView(R.id.examinebutton)
    Button examinebutton;
    @BindView(R.id.error)
    ImageView error;
    @BindView(R.id.error_text)
    TextView errorText;
    @BindView(R.id.zan)
    TextView zan;
    @BindView(R.id.yu)
    TextView yu;
    @BindView(R.id.goon)
    Button goon;
    @BindView(R.id.con_success)
    ConstraintLayout layout_success;
    @BindView(R.id.con_error)
    ConstraintLayout layout_error;
    @BindView(R.id.con_yv)
    ConstraintLayout layout_yv;

    private String orderId;
    private int type;
    private double payAmount;
    private IPresenterImpl presenter;
    private Map<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        presenter = new IPresenterImpl(this);
       // initDataLoad();
        //
        initView();

    }
    //加载数据
    private void initView() {
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        payAmount = intent.getDoubleExtra("payAmount", 0);
        textView.setText("余额支付"+ payAmount +"元");
    }


    //加载网络
    private void initDataLoad() {
        map = new HashMap<>();
        map.put("orderId",orderId);
        map.put("payType",String.valueOf(type));
        presenter.startRequestPost(Apis.URL_PAY_POST,map,PayOrderBean.class);
    }

    @Override
    public void getDataSuccess(Object data) {
         if (data instanceof PayOrderBean){
             PayOrderBean payOrderBean= (PayOrderBean) data;
             if (payOrderBean==null){
                 Toast.makeText(PayActivity.this,payOrderBean.getMessage(),Toast.LENGTH_SHORT).show();
                 layout_error.setVisibility(View.VISIBLE);
                 layout_success.setVisibility(View.GONE);
             }else{
                 layout_success.setVisibility(View.VISIBLE);
                 layout_error.setVisibility(View.GONE);
             }
         }
    }

    @Override
    public void getDataFail(String error) {

    }
    @OnClick({R.id.text, R.id.homebutton, R.id.examinebutton, R.id.goon,R.id.balancepaid_radio,R.id.wechat_radio,
            R.id.alipay_radio})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.balancepaid_radio:
                type = 1;
                textView.setText("余额支付"+payAmount+"元");
                break;
            case R.id.wechat_radio:
                type = 2;
                textView.setText("微信支付"+payAmount+"元");
                break;
            case R.id.alipay_radio:
                type = 3;
                textView.setText("支付宝支付"+payAmount+"元");
                break;
            case R.id.text:
                initDataLoad();
                break;
            case R.id.homebutton:
                finish();
                break;
            case R.id.examinebutton:
                finish();
                break;
            case R.id.goon:
                layout_success.setVisibility(View.GONE);
                layout_yv.setVisibility(View.VISIBLE);
                layout_error.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
