package fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.LogTime;
import com.bwei.mallproject.R;
import com.stx.xhb.xbanner.XBanner;
import com.stx.xhb.xbanner.transformers.Transformer;

import java.util.ArrayList;
import java.util.List;

import adapter.FirstCategoryAdapter;
import adapter.SecondCategoryAdapter;
import bean.FirstCategoryBean;
import bean.SecondCategoryBean;
import view.AppinfoiItemDecoration;
import view.IView;
import adapter.GridAdapter;
import adapter.LinerAdapter;
import adapter.ShowHoriAdapter;
import api.Apis;
import bean.ShowBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import presenter.IPresenterImpl;

public class HomeFragment extends Fragment implements IView {
    private static final String TAG = "HomeFragment++++++++";
    @BindView(R.id.image)
    ImageView mImage;
    @BindView(R.id.search)
    ImageView mSearch;
    @BindView(R.id.xbanner)
    XBanner mXbanner;
    @BindView(R.id.horiz_recy)
    RecyclerView mHorizRecy;
    @BindView(R.id.horiz_line)
    RecyclerView mHorizLine;
    @BindView(R.id.recy_grid)
    RecyclerView mRecyGrid;

    private View view;
    private Unbinder unbinder;
    private List<String> mImgesUrl;
    private IPresenterImpl presenter;
    private ShowHoriAdapter horiAdapter;
    private LinerAdapter linerAdapter;
    private GridAdapter gridAdapter;
    private FirstCategoryAdapter categoryAdapter;
    private RecyclerView mRecyTop;
    private RecyclerView mRecyBottom;
    private LinearLayoutManager layoutManager3;
    private PopupWindow popupWindow;
    private SecondCategoryAdapter categoryAdapter1;
    private int mCount = 5;
    private int mPage=1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.homefragment_layout, null);
        presenter = new IPresenterImpl(this);
        unbinder = ButterKnife.bind(this, view);
        mImgesUrl = new ArrayList<>();
        mImgesUrl.add("http://172.17.8.100/images/small/banner/cj.png");
        mImgesUrl.add("http://172.17.8.100/images/small/banner/hzp.png");
        mImgesUrl.add("http://172.17.8.100/images/small/banner/lyq.png");
        mImgesUrl.add("http://172.17.8.100/images/small/banner/px.png");
        mXbanner.setData(mImgesUrl,null);
        //xbanner适配数据
        mXbanner.loadImage(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
                Glide.with(getActivity()).load(mImgesUrl.get(position)).into((ImageView)view);
            }
        });
        //设置样式，里面有很多种样式可以自己都看看效果
        mXbanner.setPageTransformer(Transformer.Default);//横向移动
        mXbanner.setPageTransformer(Transformer.Alpha); //渐变，效果不明显
        mXbanner.setPageTransformer(Transformer.ZoomFade); // 缩小本页，同时放大另一页
        mXbanner.setPageTransformer(Transformer.ZoomCenter); //本页缩小一点，另一页就放大
        mXbanner.setPageTransformer(Transformer.ZoomStack); // 本页和下页同事缩小和放大
        mXbanner.setPageTransformer(Transformer.Stack);  //本页和下页同时左移
        mXbanner.setPageTransformer(Transformer.Depth);  //本页左移，下页从后面出来
        mXbanner.setPageTransformer(Transformer.Zoom);  //本页刚左移，下页就在后面
        //  设置xbanner求换页面的时间
        mXbanner.setPageChangeDuration(0);
        loadView();
        initView();
        //搜索
       // initSearch();

        //数据

        //loadView2();
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //类目录
                Toast.makeText(getActivity(),"点击了",Toast.LENGTH_SHORT).show();
                loadView1();
                initPop();

            }
        });
        return view;
    }



    private void initPop() {
             View view=View.inflate(getActivity(),R.layout.pop_item_home,null);
             //加载上面的布局
                mRecyTop = view.findViewById(R.id.recy_top);
                LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
               //一级条目布局管理器
                layoutManager2.setOrientation(OrientationHelper.HORIZONTAL);
                mRecyTop.setLayoutManager(layoutManager2);
                categoryAdapter = new FirstCategoryAdapter(getActivity());
                mRecyTop.setAdapter(categoryAdapter);
                //设置条目之间的间距
                AppinfoiItemDecoration decoration=new AppinfoiItemDecoration();
                mRecyTop.addItemDecoration(decoration);
                //加载下面的recycleView
                mRecyBottom = view.findViewById(R.id.recy_bottom);
                categoryAdapter1 = new SecondCategoryAdapter(getActivity());
                layoutManager3 = new LinearLayoutManager(getActivity());
                layoutManager3.setOrientation(OrientationHelper.HORIZONTAL);
                mRecyBottom.setLayoutManager(layoutManager3);
                mRecyBottom.setAdapter(categoryAdapter1);
                mRecyBottom.addItemDecoration(decoration);
                //设置popuWindow
                popupWindow = new PopupWindow(view,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
                //设置焦点
                popupWindow.setFocusable(true);
                //设置背景
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                //设置可触摸
                popupWindow.setTouchable(true);
                //设置位置
                popupWindow.showAsDropDown(view,Gravity.CENTER_VERTICAL,50,-340);

               categoryAdapter1.setCatagralTwoCallBack(new SecondCategoryAdapter.CatagralTwoCallBack() {
                   @Override
                   public void callBack(String id) {
                       //presenter.startRequestGet(String.format(Apis.URL_FIND_SECOND_CATEGORY_GET,id),SecondCategoryBean.class);
                       
                   }
               });
    }


    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        mHorizRecy.setLayoutManager(layoutManager);
        horiAdapter = new ShowHoriAdapter(getActivity());
        mHorizRecy.setAdapter(horiAdapter);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        mHorizLine.setLayoutManager(layoutManager1);
        linerAdapter = new LinerAdapter(getActivity());
        mHorizLine.setAdapter(linerAdapter);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
        mRecyGrid.setLayoutManager(gridLayoutManager);
        gridAdapter = new GridAdapter(getActivity());
        mRecyGrid.setAdapter(gridAdapter);



    }

    private void loadView() {
        presenter.startRequestGet(Apis.URL_COMMODITY_LIST_GET,null,ShowBean.class);

    }
    private void loadView1() {
        presenter.startRequestGet(Apis.URL_FIND_FIRST_CATEGORY_GET,null,FirstCategoryBean.class);

    }
    private void loadView2() {
       // presenter.startRequestGet(String.format(Apis.URL_FIND_SECOND_CATEGORY_GET,null),SecondCategoryBean.class);
       presenter.startRequestGet(Apis.URL_FIND_SECOND_CATEGORY_GET,null,SecondCategoryBean.class);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void getDataSuccess(Object data) {
        if (data instanceof ShowBean) {
            ShowBean bean = (ShowBean) data;
            if (bean == null || !bean.isSuccess()) {
                Toast.makeText(getActivity(), bean.getMessage(), Toast.LENGTH_LONG).show();
            }else{
                 horiAdapter.setmData(bean.getResult().getRxxp().get(0).getCommodityList());
                 linerAdapter.setmData(bean.getResult().getMlss().get(0).getCommodityList());
                 gridAdapter.setmData(bean.getResult().getPzsh().get(0).getCommodityList());
            }
            //一级目录
        }else if (data instanceof FirstCategoryBean) {
           Log.d(TAG, "getDataSuccess: ++++++" + FirstCategoryBean.class);
           FirstCategoryBean bean1 = (FirstCategoryBean) data;
           if (bean1 == null) {
               Toast.makeText(getActivity(), bean1.getMessage(), Toast.LENGTH_LONG).show();
           } else {
               categoryAdapter.setmData(bean1.getResult());
           }
           //二级目录
       }else if (data instanceof SecondCategoryBean){
            Log.d(TAG, "getDataSuccess: ++++++"+SecondCategoryBean.class);
            SecondCategoryBean bean2= (SecondCategoryBean) data;
            if (bean2==null){
                Toast.makeText(getActivity(), bean2.getMessage(), Toast.LENGTH_LONG).show();
            }else{
                categoryAdapter1.setmData(bean2.getResult());
            }
        }
    }

    @Override
    public void getDataFail(String error) {

    }
}
