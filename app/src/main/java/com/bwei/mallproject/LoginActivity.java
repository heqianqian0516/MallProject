package com.bwei.mallproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import view.IView;
import api.Apis;
import app.MyApplication;
import bean.LoginBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import constants.Constants;
import fragment.CircleFragment;
import presenter.IPresenterImpl;
/**
 * 登录页面
 * */
public class LoginActivity extends AppCompatActivity implements IView {
    private static final String TAG = "LoginActivity++++++++";
   

    @BindView(R.id.img_account)
    ImageView mImgAccount;
    @BindView(R.id.et_account)
    EditText mEtAccount;
    @BindView(R.id.img_accounts)
    ImageView mImgAccounts;
    @BindView(R.id.et_accounts)
    EditText mEtAccounts;
    @BindView(R.id.img_eye)
    ImageButton mImgEye;
    @BindView(R.id.check)
    CheckBox mCheck;
    @BindView(R.id.regist)
    TextView mRegist;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    private IPresenterImpl iPresenter;
    final String REMEMBER_PWD_PREF = "rememberPwd";
    final String PHONE_PREF = "phone";
    final String PASSWORD_PREF = "password";
    private SharedPreferences sp;
    private CircleFragment circleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
     /*   FragmentManager manager=getFragmentManager();
        FragmentTransaction transation = manager.beginTransaction();
        circleFragment = new CircleFragment();*/

        //沉浸式标题栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        initPresenter();
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            //18911474114   123456
            @Override
            public void onClick(View v) {
                 String phone = mEtAccount.getText().toString();
                 String pwd = mEtAccounts.getText().toString();
                 if (phone.equals("18338733586")&&pwd.equals("123456")){
                     SharedPreferences.Editor editor = sp.edit();
                     if (mCheck.isChecked()){
                         editor.putBoolean(REMEMBER_PWD_PREF,true);
                         editor.putString(PHONE_PREF,phone);
                         editor.putString(PASSWORD_PREF,pwd);
                     }else{
                          editor.clear();
                     }
                     editor.apply();
                      //请求网络
                     loadData();
                 }else{
                     Toast.makeText(LoginActivity.this,"账号或密码不正确",Toast.LENGTH_SHORT).show();
                 }
            }
        });
        /*
        * 密码可见或隐藏
        * */
        mImgEye.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    mEtAccounts.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    mEtAccounts.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                return false;
            }
        });
       //快速注册
        mRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        //从 SharedPreferences 中获取【是否记住密码】参数
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember= sp.getBoolean(REMEMBER_PWD_PREF,false);
        if (isRemember){
            mEtAccount.setText(sp.getString(PHONE_PREF,""));
            mEtAccounts.setText(sp.getString(PASSWORD_PREF,""));
            mCheck.setChecked(true);
        }

    }

    private void initData(String userId,String sessionId) {
        SharedPreferences sharedPreferences = MyApplication.getApplication().getSharedPreferences("UserShao", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId",userId);
        editor.putString("sessionId",sessionId);
        editor.commit();

    }


    private void loadData() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(Constants.MAP_KEY_SEARCH_PRODUCTS_PHONE, mEtAccount.getText().toString());
        hashMap.put(Constants.MAP_KEY_SEARCH_PRODUCTS_PWD, mEtAccounts.getText().toString());
        iPresenter.startRequestPost(Apis.URL_LOGIN_POST, hashMap, LoginBean.class);
    }

    private void initPresenter() {
        iPresenter = new IPresenterImpl(this);
    }

    @Override
    public void getDataSuccess(Object data) {
        //13713657944   123456
        Log.d(TAG, "getDataSuccess: +++++++"+LoginBean.class);
        if (data instanceof LoginBean) {
            LoginBean bean = (LoginBean) data;
            final LoginBean.ResultBean result = bean.getResult();
            if (!bean.getMessage().equals("登录成功")) {
                Toast.makeText(LoginActivity.this, bean.getMessage(), Toast.LENGTH_LONG).show();
            }else{
                initData(result.getUserId()+"",result.getSessionId()+"");
                Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(LoginActivity.this,ShowActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iPresenter.onDetach();
    }

}
