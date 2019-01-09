package com.bwei.mallproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bean.EventBean;
import bean.FindShoppingCartBean;
import bean.GoodsBean;
import bean.ShoppingCarBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import presenter.IPresenterImpl;
import view.IView;

/**
 * 详情页面
 *
 * **/
public class DetailActivity extends AppCompatActivity implements IView {

    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.textPrice)
    TextView mTextPrice;
    @BindView(R.id.sold)
    TextView mSold;
    @BindView(R.id.textTitle)
    TextView mTextTitle;
    @BindView(R.id.text_kg)
    TextView mTextKg;
    @BindView(R.id.webview)
    WebView mWebview;
    @BindView(R.id.add_shop)
    ImageView mAddShop;
    @BindView(R.id.btn_buy)
    ImageView mBtnBuy;
    private GoodsBean goodsBean;
    private IPresenterImpl presenter;
    private int commodityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        presenter = new IPresenterImpl(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onEvent(EventBean eventBean) {
        if (eventBean.getName().equals("goods")) {
            goodsBean = (GoodsBean) eventBean.getClazz();
            initLoad();
        }
    }

    private void initLoad() {
        String details = goodsBean.getResult().getDetails();
        String picture = goodsBean.getResult().getPicture();
        String[] split = picture.split(",");
        List<String> list = Arrays.asList(split);
        mWebview.loadDataWithBaseURL(null, details, "text/html", "utf-8", null);
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setImages(list);
        mBanner.start();
        mTextPrice.setText("￥" + goodsBean.getResult().getPrice() + "");
        mTextTitle.setText(goodsBean.getResult().getCommodityName());
        mTextKg.setText("重量" + goodsBean.getResult().getWeight() + "kg");
        mSold.setText("已售" + goodsBean.getResult().getSaleNum());

    }

    @OnClick({R.id.banner, R.id.textPrice, R.id.sold, R.id.textTitle, R.id.text_kg, R.id.webview, R.id.add_shop, R.id.btn_buy})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.banner:
                break;
            case R.id.textPrice:
                break;
            case R.id.sold:
                break;
            case R.id.textTitle:
                break;
            case R.id.text_kg:
                break;
            case R.id.webview:
                break;
            case R.id.add_shop:

                break;
            case R.id.btn_buy:
                break;
        }
    }

    @Override
    public void getDataSuccess(Object data) {
          //先查询购物车,再添加到购物车
        if (data instanceof FindShoppingCartBean){
            FindShoppingCartBean findShoppingCartBean= (FindShoppingCartBean) data;
            if (findShoppingCartBean==null||findShoppingCartBean.isSuccess()){
                Toast.makeText(DetailActivity.this,findShoppingCartBean.getMessage(),Toast.LENGTH_SHORT).show();
            }else{
                //实例化shoppingcarbean
                List<ShoppingCarBean> list=new ArrayList<>();
                //得到查询购物车的集合
                List<FindShoppingCartBean.ResultBean> result=findShoppingCartBean.getResult();
                //遍历集合添加到shoppingcarBean
                for(FindShoppingCartBean.ResultBean re:result){
                     list.add(new ShoppingCarBean(re.getCommodityId(),re.getCount()));
                }
                //添加到购物车
                getAddShoppingCar(list);
            }

        }
    }



    @Override
    public void getDataFail(String error) {

    }
    /**
     * 同步购物车
     * **/
    private void getAddShoppingCar(List<ShoppingCarBean> list) {
         String str="[" ;
        for (int i = 0; i <list.size() ; i++) {
           if (Integer.valueOf(commodityId)==list.get(i).getCommodityId()){
               int count=list.get(i).getCount();
               count++;
               list.get(i).setCount(count);
               break;
           }else{

           }
        }
    }
    private class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
