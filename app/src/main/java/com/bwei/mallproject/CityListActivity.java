package com.bwei.mallproject;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.lljjcoder.citypickerview.widget.CityPicker;

import java.util.HashMap;
import java.util.Map;

import api.Apis;
import bean.AddAddrBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import constants.Constants;
import presenter.IPresenterImpl;
import view.IView;

public class CityListActivity extends AppCompatActivity implements IView {

    @BindView(R.id.myName)
    TextView myName;
    @BindView(R.id.viewTwo)
    View viewTwo;
    @BindView(R.id.myPassword)
    TextView myPassword;
    @BindView(R.id.viewThree)
    View viewThree;
    @BindView(R.id.mycity)
    TextView mycity;
    @BindView(R.id.adresss)
    TextView adresss;
    @BindView(R.id.viewFour)
    View viewFour;
    @BindView(R.id.myXiangxicity)
    TextView myXiangxicity;
    @BindView(R.id.viewFive)
    View viewFive;
    @BindView(R.id.youxiao)
    TextView youxiao;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.myPhone)
    TextView myPhone;
    @BindView(R.id.iangxicity)
    TextView iangxicity;
    @BindView(R.id.dizhi)
    TextView dizhi;
    @BindView(R.id.viewsix)
    View viewsix;
    @BindView(R.id.addSubmit)
    Button addSubmit;
    @BindView(R.id.newName)
    EditText newName;
    @BindView(R.id.newPhone)
    EditText newPhone;
    @BindView(R.id.newAddr)
    EditText newAddr;

    private IPresenterImpl presenter;

    private String province;
    private String city;
    private String district;
    private String code;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String mAddr;
    private String mYb;
    private String name;
    private String phone;
    private CityPicker cityPicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        //cityPickerView = new CityPickerView(CityListActivity.this);
        presenter = new IPresenterImpl(this);
        sharedPreferences = getSharedPreferences("Address", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        mycity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCityPicker();
                cityPicker.show();
            }
        });

        name = sharedPreferences.getString("nickName", null);
        phone = sharedPreferences.getString("phone", null);
        mAddr = sharedPreferences.getString("address", null);
        mYb = sharedPreferences.getString("youxiao", null);
        nickname.setText(name);
        myPhone.setText(phone);
        adresss.setText(mAddr);
        youxiao.setText(mYb);
        addSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = adresss.getText().toString();
                String s1 = youxiao.getText().toString();
                editor.putString("address", s);
                editor.putString("youxiao", s1);
                editor.commit();
                loadData();
                finish();
            }
        });
        dizhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAddr.setVisibility(View.VISIBLE);
                String mNewAddr = newAddr.getText().toString();
                dizhi.setText(mNewAddr);
            }
        });
        nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newName.setVisibility(View.VISIBLE);
                String mNewName = newName.getText().toString();
                nickname.setText(mNewName);
            }
        });
        newName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容

                } else {
                    // 此处为失去焦点时的处理内容
                    newName.setVisibility(View.GONE);
                    nickname.setVisibility(View.VISIBLE);
                    nickname.setText(newName.getText().toString());
                }
            }
        });
        newPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容

                } else {
                    // 此处为失去焦点时的处理内容
                    newPhone.setVisibility(View.GONE);
                    myPhone.setVisibility(View.VISIBLE);
                    myPhone.setText(newPhone.getText().toString());
                }
            }
        });
        newAddr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容

                } else {
                    // 此处为失去焦点时的处理内容
                    newAddr.setVisibility(View.GONE);
                    adresss.setVisibility(View.VISIBLE);
                    adresss.setText(newAddr.getText().toString());
                }
            }
        });
        myPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPhone.setVisibility(View.VISIBLE);
                String mNewPhone = newPhone.getText().toString();


                myPhone.setText(mNewPhone);
            }
        });

    }

    private void initCityPicker() {
        //滚轮文字的大小
        //滚轮文字的颜色
        //省份滚轮是否循环显示
        //城市滚轮是否循环显示
        //地区（县）滚轮是否循环显示
        //滚轮显示的item个数
        //滚轮item间距
        //滚轮文字的大小
//.backgroundPop(0xa0000000)
//.titleTextColor("#000000")
//.backgroundPop(0xa0000000)
//滚轮文字的颜色
//省份滚轮是否循环显示
//城市滚轮是否循环显示
//地区（县）滚轮是否循环显示
//滚轮显示的item个数
//滚轮item间距
        cityPicker = new CityPicker.Builder(CityListActivity.this)
                //滚轮文字的大小
               .textSize(20)
               .title("地址选择")
                //.backgroundPop(0xa0000000)
                .titleBackgroundColor("#0CB6CA")
                //.titleTextColor("#000000")
                //.backgroundPop(0xa0000000)
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .province("xx省")
                .city("xx市")
                .district("xx区")
                //滚轮文字的颜色
                .textColor(Color.parseColor("#000000"))
                //省份滚轮是否循环显示
                .provinceCyclic(true)
                //城市滚轮是否循环显示
                .cityCyclic(false)
                //地区（县）滚轮是否循环显示
                .districtCyclic(false)
                //滚轮显示的item个数
                .visibleItemsCount(7)
                //滚轮item间距
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                province = citySelected[0];
                //城市
                city = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
                district = citySelected[2];
                //邮编
                code = citySelected[3];
                adresss.setText(province + city + district);
                youxiao.setText(code);
                editor.commit();
            }


        });

    }

    private void loadData() {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.URAL_ADD_SHOPPING_REALNAME, nickname.getText().toString());
        map.put(Constants.URAL_ADD_SHOPPING_PHONE, myPhone.getText().toString());
        map.put(Constants.URAL_ADD_SHOPPING_ADDRESS, adresss.getText().toString());
        map.put(Constants.URAL_ADD_SHOPPING_ZIPCODE, youxiao.getText().toString());
        presenter.startRequestPost(Apis.URL_ADD_RECEIVE_ADDRESS_POST, map, AddAddrBean.class);

    }

    @Override
    public void getDataSuccess(Object data) {

    }

    @Override
    public void getDataFail(String error) {

    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获取当前焦点所在的控件；
            View view = getCurrentFocus();
            if (view != null && view instanceof EditText) {
                Rect r = new Rect();
                view.getGlobalVisibleRect(r);
                int rawX = (int) ev.getRawX();
                int rawY = (int) ev.getRawY();
                // 判断点击的点是否落在当前焦点所在的 view 上；
                if (!r.contains(rawX, rawY)) {
                    view.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

}

