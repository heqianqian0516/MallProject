package com.bwei.mallproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import adapter.MineFootAdapter;
import adapter.SecondCategoryAdapter;
import api.Apis;
import bean.EventBean;
import bean.GoodsBean;
import bean.MineFootBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import presenter.IPresenterImpl;
import view.IView;
/**
 * 我的足迹activity
 * */
public class MainFootActivity extends AppCompatActivity implements IView {

    @BindView(R.id.xrecycle)
    XRecyclerView mXrecycle;
    private IPresenterImpl presenter;
    private MineFootAdapter footAdapter;
    private int mPage;


    private MineFootBean footBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_foot);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        mPage=1;
        presenter = new IPresenterImpl(this);
        footAdapter=new MineFootAdapter(this);
        mXrecycle.setLayoutManager(new StaggeredGridLayoutManager(2,GridLayoutManager.VERTICAL));
        mXrecycle.setAdapter(footAdapter);
        mXrecycle.setLoadingMoreEnabled(true);
        mXrecycle.setPullRefreshEnabled(true);
        mXrecycle.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage=1;
                loadData();
            }

            @Override
            public void onLoadMore() {
                loadData();
            }
        });
        footAdapter.setCallBackFoot(new MineFootAdapter.CallBackFoot() {
            @Override
            public void callBack(int commodityId) {
                Intent intent=new Intent(MainFootActivity.this,DetailActivity.class);
                intent.putExtra("commodityId",commodityId);
                startActivity(intent);
            }
        });

    }

    private void loadData() {
        presenter.startRequestGet(Apis.URL_BROWSE_LIST_GET+"?page="+mPage+"&count=10",
                null, MineFootBean.class);
    }

    @OnClick(R.id.xrecycle)
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.xrecycle:
                break;
        }
    }

    @Override
    public void getDataSuccess(Object data) {
        if (data instanceof MineFootBean){
            footBean = (MineFootBean) data;
            if (footBean ==null||!footBean.isSuccess()){
                Toast.makeText(MainFootActivity.this,
                        footBean.getMessage(),Toast.LENGTH_LONG).show();
            }else {
                if (mPage==1){
                    footAdapter.setList(footBean.getResult());
                }else {
                    footAdapter.addList(footBean.getResult());
                }
                mPage++;
                mXrecycle.refreshComplete();
                mXrecycle.loadMoreComplete();
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
