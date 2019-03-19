package com.bwei.mallproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import adapter.MyFragmentAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fragment.CircleFragment;
import fragment.HomeFragment;
import fragment.MyFragment;
import fragment.OrderFragment;
import fragment.ShopFragment;
import view.NoScrollViewPager;

/**
   *
   * 主页面
   * **/
public class ShowActivity extends AppCompatActivity {

    @BindView(R.id.view_page)
    NoScrollViewPager mViewPage;
    @BindView(R.id.iamge1)
    RadioButton mIamge1;
    @BindView(R.id.iamge2)
    RadioButton mIamge2;
    @BindView(R.id.iamge3)
    RadioButton mIamge3;
    @BindView(R.id.iamge4)
    RadioButton mIamge4;
    @BindView(R.id.iamge5)
    RadioButton mIamge5;
    @BindView(R.id.redio_group)
    RadioGroup mRedioGroup;
    private MyFragmentAdapter fragmentAdapter;
    private List<Fragment> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        ButterKnife.bind(this);
        //数据
        initData();

        mRedioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.iamge1:
                        mViewPage.setCurrentItem(0);
                        break;
                    case R.id.iamge2:
                        mViewPage.setCurrentItem(1);
                        break;
                    case R.id.iamge3:
                        mViewPage.setCurrentItem(2);
                        break;
                    case R.id.iamge4:
                        mViewPage.setCurrentItem(3);
                        break;
                    case R.id.iamge5:
                        mViewPage.setCurrentItem(4);
                        break;
                }
            }
        });
      /*  mViewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mRedioGroup.check(mRedioGroup.getChildAt(position).getId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
    }

    private void initData() {
        list = new ArrayList<>();
        list.add(new HomeFragment());
        list.add(new CircleFragment());
        list.add(new ShopFragment());
        list.add(new OrderFragment());
        list.add(new MyFragment());
        fragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), list);
        mViewPage.setAdapter(fragmentAdapter);
    }


}
