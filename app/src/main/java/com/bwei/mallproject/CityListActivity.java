package com.bwei.mallproject;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.lljjcoder.citypickerview.widget.CityPicker;

import java.util.HashMap;
import java.util.Map;

import api.Apis;
import bean.AddAddrBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import constants.Constants;
import mine.bean.AddAddressBean;
import presenter.IPresenterImpl;
import view.IView;

public class CityListActivity extends AppCompatActivity implements IView {
    @BindView(R.id.recipients_edit)
    EditText recipientsEdit;
    @BindView(R.id.telephone_edit)
    EditText telephoneEdit;
    @BindView(R.id.area_edit)
    EditText areaEdit;
    @BindView(R.id.area_image)
    ImageView areaImage;
    @BindView(R.id.detailed_edit)
    EditText detailedEdit;
    @BindView(R.id.postcode_edit)
    EditText postcodeEdit;
    @BindView(R.id.save)
    TextView save;
    private IPresenterImpl presenter;
    private String realName;
    private String phone;
    private String area;
    private String detailed;
    private String postcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        ButterKnife.bind(this);
        presenter = new IPresenterImpl(this);
        initView();
    }

    private void initView() {
    }


    @Override
    public void getDataSuccess(Object data) {
        if (data instanceof AddAddressBean) {
            AddAddressBean addressBean = (AddAddressBean) data;
            Toast.makeText(CityListActivity.this, addressBean.getMessage(), Toast.LENGTH_SHORT).show();
            setResult(200);
            finish();
        }
    }

    @Override
    public void getDataFail(String error) {

    }
    @OnClick({R.id.save, R.id.area_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.save:
                getData();
                break;
            case R.id.area_image:
                //判断输入法的隐藏状态
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    //调用CityPicker选取区域
                    selectAddress();
                }
                break;
            default:
                break;
        }
    }



    private void getData() {
        //获取输入框的值
        realName = recipientsEdit.getText().toString();
        phone = telephoneEdit.getText().toString();
        area = areaEdit.getText().toString();
        detailed = detailedEdit.getText().toString();
        postcode = postcodeEdit.getText().toString();
        Map<String, String> map = new HashMap();
        map.put("realName", realName);
        map.put("phone", phone);
        map.put("address", area + " " + detailed);
        map.put("zipCode", postcode);
        presenter.startRequestPost(Apis.URL_ADD_RECEIVE_ADDRESS_POST,map, AddAddressBean.class);
    }
    private void selectAddress() {
        CityPicker cityPicker = new CityPicker.Builder(CityListActivity.this)
                .textSize(14)
                .title("地址选择")
                .titleBackgroundColor("#FFFFFF")
                .confirTextColor("#696969")
                .cancelTextColor("#696969")
                .province("江苏省")
                .city("常州市")
                .district("天宁区")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        cityPicker.show();
        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
                String district = citySelected[2];
                //邮编
                String code = citySelected[3];
                //为TextView赋值
                areaEdit.setText(province.trim() + " " + city.trim() + " " + district.trim());
                postcodeEdit.setText(code);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}

