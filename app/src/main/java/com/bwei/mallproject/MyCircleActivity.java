package com.bwei.mallproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import adapter.CircleAdapter;
import api.Apis;
import bean.CircleBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mine.adapter.MyCircleAdaper;
import mine.bean.DeleteCircleBean;
import mine.bean.MyCircleBean;
import presenter.IPresenterImpl;
import view.IView;

public class MyCircleActivity extends AppCompatActivity implements IView {
    private static final String TAG = "MyCircleActivity++++++++++";
    @BindView(R.id.delete)
    ImageView delete;
    @BindView(R.id.clrcle_recycle)
    XRecyclerView clrcleRecycle;

    private int mPage = 1;
    private boolean falg = true;
    private int mCount = 10;
    private IPresenterImpl presenter;
    private CircleAdapter circleAdaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_circle);
        ButterKnife.bind(this);

        initView();
        presenter = new IPresenterImpl(this);
        presenter.startRequestGet(String.format(Apis.URL_FIND_MYCIRCLE_BYID_GET, mPage, mCount), null, CircleBean.class);
    }

    private void initView() {
        mPage = 1;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyCircleActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        clrcleRecycle.setLayoutManager(linearLayoutManager);
        circleAdaper = new CircleAdapter(MyCircleActivity.this);
        clrcleRecycle.setAdapter(circleAdaper);
        clrcleRecycle.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage=1;
                initLoad();
            }

            @Override
            public void onLoadMore() {
                initLoad();
            }
        });
    }

    private void initLoad() {
        presenter.startRequestGet(String.format(Apis.URL_FIND_MYCIRCLE_BYID_GET, mPage, mCount), null, CircleBean.class);
    }

    @Override
    public void getDataSuccess(Object data) {

        if (data instanceof CircleBean) {
            CircleBean circleBean = (CircleBean) data;

            if (circleBean == null) {

                Toast.makeText(MyCircleActivity.this, circleBean.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                if (mPage == 1) {
                    circleAdaper.setmData(circleBean.getResult());
                } else {
                    circleAdaper.addmResult(circleBean.getResult());
                }
                mPage++;
                clrcleRecycle.refreshComplete();
                clrcleRecycle.loadMoreComplete();
                circleAdaper.notifyDataSetChanged();
            }

        } else if (data instanceof DeleteCircleBean) {
            DeleteCircleBean deleteCircleBean = (DeleteCircleBean) data;
            if (deleteCircleBean == null) {
                Toast.makeText(MyCircleActivity.this, deleteCircleBean.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                mPage = 1;
                initLoad();
            }
        }
    }

    @Override
    public void getDataFail(String error) {
         Toast.makeText(MyCircleActivity.this,error,Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
