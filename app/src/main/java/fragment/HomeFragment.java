package fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bwei.mallproject.DetailActivity;
import com.bwei.mallproject.R;
import com.stx.xhb.xbanner.XBanner;
import com.stx.xhb.xbanner.transformers.Transformer;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import adapter.FindIdAdapter;
import adapter.FirstCategoryAdapter;
import adapter.GridAdapter;
import adapter.LinerAdapter;
import adapter.SearchAdapter;
import adapter.SecondCategoryAdapter;
import adapter.ShowHoriAdapter;
import api.Apis;
import bean.BannerBean;
import bean.EventBean;
import bean.FindIdBean;
import bean.FirstCategoryBean;
import bean.GoodsBean;
import bean.SearchBean;
import bean.SecondCategoryBean;
import bean.ShowBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import presenter.IPresenterImpl;
import view.AppinfoiItemDecoration;
import view.IView;

/**
 * 首页
 **/
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
    @BindView(R.id.byRecy)
    RecyclerView mByRecy;
    @BindView(R.id.home_ed)
    EditText mHomeEd;
    @BindView(R.id.home_tv)
    TextView mHomeTv;
    @BindView(R.id.scroll)
    ScrollView mScroll;
    @BindView(R.id.rxxp)
    TextView mRxxp;
    @BindView(R.id.mlss)
    TextView mMlss;
    @BindView(R.id.pzsh)
    TextView mPzsh;


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
    private FirstCategoryBean bean1;
    private SecondCategoryBean secondCategoryBean;
    private FindIdAdapter findIdAdapter;
    private SearchAdapter searchAdapter;
    private String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homefragment_layout, null);
        presenter = new IPresenterImpl(this);
        unbinder = ButterKnife.bind(this, view);
        mImgesUrl = new ArrayList<>();
        loadView();
        initView();
        initLoadBanner();

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //类目录
                // Toast.makeText(getActivity(), "点击了", Toast.LENGTH_SHORT).show();
                loadView1();
                initPop();

            }
        });

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHomeEd.setVisibility(View.VISIBLE);
                mHomeTv.setVisibility(View.VISIBLE);
                mSearch.setVisibility(View.INVISIBLE);
            }
        });
        mHomeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHomeEd.getText().toString().equals("")) {
                    mSearch.setVisibility(View.VISIBLE);
                    mHomeTv.setVisibility(View.GONE);
                    mHomeEd.setVisibility(View.INVISIBLE);
                } else {
                    presenter.startRequestGet(Apis.URL_FIND_COMMODITY_BYKEYWORD_GET + "?keyword=" + mHomeEd.getText().toString() + "&page=" + "1" + "&count=5", null, SearchBean.class);
                }
            }
        });
        return view;
    }

    private void initLoadBanner() {
        presenter.startRequestGet(Apis.URL_BANNER_SHOW_GET, null, BannerBean.class);
    }

    //popupWindow显示一级和二级类目
    private void initPop() {
        View view = View.inflate(getActivity(), R.layout.pop_item_home, null);
        //加载上面的布局
        mRecyTop = view.findViewById(R.id.recy_top);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        //一级条目布局管理器
        layoutManager2.setOrientation(OrientationHelper.HORIZONTAL);
        mRecyTop.setLayoutManager(layoutManager2);
        categoryAdapter = new FirstCategoryAdapter(getActivity());
        mRecyTop.setAdapter(categoryAdapter);
        //设置条目之间的间距
        AppinfoiItemDecoration decoration = new AppinfoiItemDecoration();
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
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //设置焦点
        popupWindow.setFocusable(true);
        //设置背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        //设置可触摸
        popupWindow.setTouchable(true);
        //设置位置
        popupWindow.showAsDropDown(view, Gravity.CENTER_VERTICAL, 70, -340);

        categoryAdapter.setCatagralTwoCallBack(new SecondCategoryAdapter.CatagralTwoCallBack() {
            @Override
            public void callBack(int index) {
                //presenter.startRequestGet(String.format(Apis.URL_FIND_SECOND_CATEGORY_GET,id),SecondCategoryBean.class);
                String id = bean1.getResult().get(index).getId();
                presenter.startRequestGet(Apis.URL_FIND_SECOND_CATEGORY_GET + id, null, SecondCategoryBean.class);

                categoryAdapter1.setCatagralTwoCallBack(new SecondCategoryAdapter.CatagralTwoCallBack() {
                    @Override
                    public void callBack(int index) {
                        String id1 = secondCategoryBean.getResult().get(index).getId();
                        //http://172.17.8.100/small/commodity/v1/findCommodityByKeyword
                        //public static final String URL_FIND_COMMODITY_BYCATEGORY_GET="commodity/v1/findCommodityByCategory";
                        presenter.startRequestGet(Apis.URL_FIND_COMMODITY_BYCATEGORY_GET + "?categoryId=" + id1 + "&page=1&count=10", null, FindIdBean.class);
                    }
                });

            }

        });


    }


    private void initView() {
        //热销新品
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        mHorizRecy.setLayoutManager(layoutManager);
        horiAdapter = new ShowHoriAdapter(getActivity());
        mHorizRecy.setAdapter(horiAdapter);
        //魔力时尚
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        mHorizLine.setLayoutManager(layoutManager1);
        linerAdapter = new LinerAdapter(getActivity());
        mHorizLine.setAdapter(linerAdapter);
        //品质生活
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyGrid.setLayoutManager(gridLayoutManager);
        gridAdapter = new GridAdapter(getActivity());
        mRecyGrid.setAdapter(gridAdapter);

        //二级目录查找商品
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 2);
        mByRecy.setLayoutManager(gridLayoutManager1);
        findIdAdapter = new FindIdAdapter(getActivity());
        mByRecy.setAdapter(findIdAdapter);
        //搜索商品
        GridLayoutManager grid = new GridLayoutManager(getActivity(), 2);
        mByRecy.setLayoutManager(grid);
        searchAdapter = new SearchAdapter(getActivity());
        mByRecy.setAdapter(searchAdapter);
    }

    private void loadView() {
        presenter.startRequestGet(Apis.URL_COMMODITY_LIST_GET, null, ShowBean.class);

    }

    private void loadView1() {
        presenter.startRequestGet(Apis.URL_FIND_FIRST_CATEGORY_GET, null, FirstCategoryBean.class);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void getDataSuccess(Object data) {
        if (data instanceof BannerBean) {
            BannerBean bannerBean = (BannerBean) data;
            if (bannerBean == null) {
                Toast.makeText(getActivity(), bannerBean.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < bannerBean.getResult().size(); i++) {
                    mImgesUrl.add(bannerBean.getResult().get(i).getImageUrl());
                    initImageData();
                }
            }
        } else if (data instanceof ShowBean) {
            final ShowBean bean = (ShowBean) data;
            if (bean == null) {
                Toast.makeText(getActivity(), bean.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                //热销新品数据
                horiAdapter.setmData(bean.getResult().getRxxp().getCommodityList());
                //魔力时尚数据
                linerAdapter.setmData(bean.getResult().getMlss().getCommodityList());
                //品质生活数据
                gridAdapter.setmData(bean.getResult().getPzsh().getCommodityList());
                horiAdapter.setHttpSetOnclickListener(new ShowHoriAdapter.HttpSetOnclickListener() {
                    @Override
                    public void onClickListener(int position) {
                        int commodityId = bean.getResult().getRxxp().getCommodityList().get(position).getCommodityId();
                        getGoods(commodityId);
                    }
                });
                linerAdapter.setHttpSetOnclickListener(new ShowHoriAdapter.HttpSetOnclickListener() {
                    @Override
                    public void onClickListener(int position) {
                        int commodityId = bean.getResult().getMlss().getCommodityList().get(position).getCommodityId();
                        getGoods(commodityId);
                    }
                });
                gridAdapter.setHttpSetOnclickListener(new ShowHoriAdapter.HttpSetOnclickListener() {
                    @Override
                    public void onClickListener(int position) {
                        int commodityId = bean.getResult().getPzsh().getCommodityList().get(position).getCommodityId();
                        getGoods(commodityId);
                    }
                });
            }
            //一级目录
        } else if (data instanceof FirstCategoryBean) {
            Log.d(TAG, "getDataSuccess: ++++++" + FirstCategoryBean.class);
            bean1 = (FirstCategoryBean) data;
            if (bean1 == null) {
                Toast.makeText(getActivity(), bean1.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                categoryAdapter.setmData(bean1.getResult());
            }
            //二级目录
        } else if (data instanceof SecondCategoryBean) {
            Log.d(TAG, "getDataSuccess: ++++++" + SecondCategoryBean.class);
            secondCategoryBean = (SecondCategoryBean) data;
            if (secondCategoryBean == null) {
                Toast.makeText(getActivity(), secondCategoryBean.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                categoryAdapter1.setmData(secondCategoryBean.getResult());
            }
            //二级目录查找商品
        } else if (data instanceof FindIdBean) {
            Log.d(TAG, "getDataSuccess11111: ++++++" + FindIdBean.class);
            final FindIdBean findIdBean = (FindIdBean) data;
            Log.d(TAG, "getDataSuccess222: ++++" + findIdBean.toString());
            mScroll.setVisibility(View.GONE);
            mByRecy.setVisibility(View.VISIBLE);
            FindIdAdapter findIdAdapter = new FindIdAdapter(getActivity());
            mByRecy.setAdapter(findIdAdapter);
            findIdAdapter.setmData(findIdBean.getResult());
            findIdAdapter.setCatagralTwoCallBack(new SecondCategoryAdapter.CatagralTwoCallBack() {
                @Override
                public void callBack(int index) {
                    getGoods(findIdBean.getResult().get(index).getCommodityId());
                }
            });
            //t通过搜索展示商品数据
        } else if (data instanceof SearchBean) {
            final SearchBean searchBean = (SearchBean) data;
            mScroll.setVisibility(View.GONE);
            mByRecy.setVisibility(View.VISIBLE);
            searchAdapter.setmData(searchBean.getResult());
            searchAdapter.setCatagralTwoCallBack(new SecondCategoryAdapter.CatagralTwoCallBack() {
                @Override
                public void callBack(int index) {
                    getGoods(searchBean.getResult().get(index).getCommodityId());
                }
            });
            if (id == "1002") {
                mRxxp.setVisibility(View.VISIBLE);
            } else if (id == "1003") {
                mMlss.setVisibility(View.VISIBLE);
            } else if (id == "1004") {
                mPzsh.setVisibility(View.VISIBLE);
            }
            backPage();
            //传数据跳转详情页面
        }
        else if (data instanceof GoodsBean) {
            GoodsBean goodsBean = (GoodsBean) data;
            EventBus.getDefault().postSticky(new EventBean("goods", data));
            startActivity(new Intent(getActivity(), DetailActivity.class));
        }


    }

    private void initImageData() {
        mXbanner.setData(mImgesUrl, null);
        mXbanner.loadImage(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
                Glide.with(getActivity()).load(mImgesUrl.get(position)).into((ImageView) view);
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
    }

    private void getGoods(int id) {
        // presenter.startRequestGet(Apis.URL_FIND_COMMODITY_DETAILS_BYID_GET + "?commodityId=" + id, null, GoodsBean.class);
        presenter.startRequestGet(String.format(Apis.URL_FIND_COMMODITY_DETAILS_BYID_GET, id), null, GoodsBean.class);
    }

    @Override
    public void getDataFail(String error) {

    }


    /**
     * 设置返回的监听
     */
    private long exitTime = 0;

    private void backPage() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                mScroll.setVisibility(View.VISIBLE);
                mByRecy.setVisibility(View.GONE);

                if (i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    mScroll.setVisibility(View.VISIBLE);
                    mByRecy.setVisibility(View.GONE);
                    mRxxp.setVisibility(View.GONE);
                    mMlss.setVisibility(View.GONE);
                    mPzsh.setVisibility(View.GONE);
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        exitTime = System.currentTimeMillis();
                    } else {
                        //启动一个意图,回到桌面
                        Intent backHome = new Intent(Intent.ACTION_MAIN);
                        backHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        backHome.addCategory(Intent.CATEGORY_HOME);
                        startActivity(backHome);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
    }


}
