package com.bwei.mallproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import api.Apis;
import util.EmptyUtil;
import bean.RegisterBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import presenter.IPresenterImpl;
import view.IView;

/**
 * 注册页面
 **/
public class RegisterActivity extends AppCompatActivity implements IView {
    private static final String TAG = "RegisterActivity+++++++";
    @BindView(R.id.img_account)
    ImageView mImgAccount;
    @BindView(R.id.et_account)
    EditText mEtAccount;
    @BindView(R.id.img_yan)
    ImageView mImgYan;
    @BindView(R.id.et_yan)
    EditText mEtYan;
    @BindView(R.id.img_accounts)
    ImageView mImgAccounts;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.img_eye)
    ImageView mImgEye;
    @BindView(R.id.btn_regist)
    Button mBtnRegist;
    private IPresenterImpl iPresenter;
    private String name;
    private String password;
    private final int RESULT_NUM = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initPresenter();
        ButterKnife.bind(this);
        //  loadData();
        mBtnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //请求网络
                loadData();

            }
        });
        /*
         * 密码可见或隐藏
         * */
        mImgEye.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mEtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mEtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                return false;
            }
        });
        mBtnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mEtAccount.getText().toString().trim();
                password = mEtPwd.getText().toString().trim();
                if (EmptyUtil.isNull(name, password)) {
                    loadData();
                } else {
                    Toast.makeText(RegisterActivity.this, "手机号和密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initPresenter() {
        iPresenter = new IPresenterImpl(this);

    }

    private void loadData() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("phone", mEtAccount.getText().toString().trim());
        hashMap.put("pwd", mEtPwd.getText().toString().trim());
        //  hashMap.put(Constants.MAP_KEY_SEARCH_PRODUCTS_PHONE, mEtAccount.getText().toString());
        //hashMap.put(Constants.MAP_KEY_SEARCH_PRODUCTS_PWD, mEtPwd.getText().toString());
        iPresenter.startRequestPost(Apis.URL_REGISTER_POST, hashMap, RegisterBean.class);
    }

    @Override
    public void getDataSuccess(Object data) {
        if (data instanceof RegisterBean) {
            RegisterBean bean = (RegisterBean) data;
            if (bean == null ) {
                Toast.makeText(RegisterActivity.this, bean.getMessage(), Toast.LENGTH_LONG).show();


                /*intent.putExtra("name",name);
                intent.putExtra("password",password);
                setResult(RESULT_NUM,intent);*/



            } else {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("password",password);
                setResult(RESULT_NUM,intent);
                finish();
                //startActivity(intent);
            }
        }


    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iPresenter.onDetach();
    }



}
