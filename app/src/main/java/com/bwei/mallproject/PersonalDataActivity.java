package com.bwei.mallproject;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import bean.EventBean;
import bean.LoginBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import presenter.IPresenterImpl;
import view.IView;
/**
 *  我的个人资料
 * **/
public class PersonalDataActivity extends AppCompatActivity implements IView {


    @BindView(R.id.userPicture)
    SimpleDraweeView mUserPicture;


    @BindView(R.id.nickname)
    TextView mNickname;

    @BindView(R.id.myPassword)
    TextView mMyPassword;

    private IPresenterImpl presenter;
    private LoginBean loginBean;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        ButterKnife.bind(this);

    }

    @Override
    public void getDataSuccess(Object data) {

    }

    @Override
    public void getDataFail(String error) {

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onEvent(EventBean evBean) {
        if (evBean.getName().equals("main")){
            loginBean = (LoginBean) evBean.getClazz();
            Uri uri = Uri.parse(loginBean.getResult().getHeadPic());
            mUserPicture.setImageURI(uri);

            //  Glide.with(getActivity()).load(loginBean.getResult().getHeadPic()).into(mineIcon);
            mNickname.setText(loginBean.getResult().getNickName());
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

}
