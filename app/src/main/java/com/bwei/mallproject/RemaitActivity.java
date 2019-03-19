package com.bwei.mallproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.Apis;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import order.adapter.GridViewAdapter;
import order.bean.OrderBean;
import order.bean.ReleaseCircleBean;
import order.bean.RemaitBean;
import presenter.IPresenterImpl;
import view.IView;
import view.MainConstant;
import view.PictureSelectorConfig;
  /**
   * 发表评论
   *
   * */
public class RemaitActivity extends AppCompatActivity implements IView {
    @BindView(R.id.item_image)
    SimpleDraweeView itemImage;
    @BindView(R.id.item_name)
    TextView itemName;
    @BindView(R.id.item_price)
    TextView itemPrice;
    @BindView(R.id.write_remate)
    EditText writeRemate;
    @BindView(R.id.radio)
    RadioButton radio;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.publish)
    Button publish;
    @BindView(R.id.gridView)
    GridView gridView;

    private IPresenterImpl presenter;
    private String orderId;
    private int commodityId;
    /**
     * 上传的图片凭证的数据源
     */
    private ArrayList<String> mPicList = new ArrayList<>();
    /**
     * 展示上传的图片的适配器
     */
    private GridViewAdapter mGridViewAddImgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remait);
        ButterKnife.bind(this);
        presenter = new IPresenterImpl(this);
        initData();
        initGridView();
    }

    private void initData() {
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        OrderBean.OrderListBean.DetailListBean dataBean = (OrderBean.OrderListBean.DetailListBean) intent.getSerializableExtra("dataBean");
        commodityId = dataBean.getCommodityId();
        double commodityPrice = dataBean.getCommodityPrice();
        String commodityName = dataBean.getCommodityName();
        String img = dataBean.getCommodityPic().split("\\,")[0].replace("https", "http");
        itemImage.setImageURI(Uri.parse(img));
        itemName.setText(commodityName);
        itemPrice.setText("￥" + commodityPrice);
    }
    //初始化展示上传图片的GridView
    private void initGridView() {
        mGridViewAddImgAdapter = new GridViewAdapter(RemaitActivity.this, mPicList);
        gridView.setAdapter(mGridViewAddImgAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == parent.getChildCount() - 1) {
                    //如果“增加按钮形状的”图片的位置是最后一张，且添加了的图片的数量不超过9张，才能点击
                    if (mPicList.size() == MainConstant.MAX_SELECT_PIC_NUM) {
                        //最多添加9张图片
                        viewPluImg(position);
                    } else {
                        //添加凭证图片
                        selectPic(MainConstant.MAX_SELECT_PIC_NUM - mPicList.size());
                    }
                } else {
                    viewPluImg(position);
                }
            }
        });
    }
    /**
     * 打开相册或者照相机选择凭证图片，最多5张
     *
     * @param maxTotal 最多选择的图片的数量
     */
    private void selectPic(int maxTotal) {
        PictureSelectorConfig.initMultiConfig(this, maxTotal);
    }
    //查看大图
    private void viewPluImg(int position) {
        Intent intent = new Intent(RemaitActivity.this, PlusImageActivity.class);
        intent.putStringArrayListExtra(MainConstant.IMG_LIST, mPicList);
        intent.putExtra(MainConstant.POSITION, position);
        startActivityForResult(intent, MainConstant.REQUEST_CODE_MAIN);
    }
    // 处理选择的照片的地址
    private void refreshAdapter(List<LocalMedia> picList) {
        for (LocalMedia localMedia : picList) {
            //被压缩后的图片路径
            if (localMedia.isCompressed()) {
                //压缩后的图片路径
                String compressPath = localMedia.getCompressPath();
                //把图片添加到将要上传的图片数组中
                mPicList.add(compressPath);
                mGridViewAddImgAdapter.notifyDataSetChanged();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    refreshAdapter(PictureSelector.obtainMultipleResult(data));
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    break;
            }
        }
        if (requestCode == MainConstant.REQUEST_CODE_MAIN && resultCode == MainConstant.RESULT_CODE_VIEW_IMG) {
            //查看大图页面删除了图片
            ArrayList<String> toDeletePicList = data.getStringArrayListExtra(MainConstant.IMG_LIST);
            mPicList.clear();
            mPicList.addAll(toDeletePicList);
            mGridViewAddImgAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void getDataSuccess(Object data) {
        if (data instanceof RemaitBean) {
            RemaitBean remaitBean = (RemaitBean) data;
            Toast.makeText(RemaitActivity.this, remaitBean.getMessage(), Toast.LENGTH_SHORT).show();
        } else if (data instanceof ReleaseCircleBean) {
            ReleaseCircleBean releaseCircleBean = (ReleaseCircleBean) data;
            if (releaseCircleBean == null) {
                Toast.makeText(RemaitActivity.this, releaseCircleBean.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RemaitActivity.this, releaseCircleBean.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void getDataFail(String error) {

    }

    @OnClick(R.id.publish)
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.publish:
                if (radio.isChecked()) {
                    String content = writeRemate.getText().toString().trim();
                    Map<String, Object> map = new HashMap<>();
                    map.put("commodityId", String.valueOf(commodityId));
                    map.put("content", content);
                    map.put("image", mPicList);
                    presenter.startImagsPost(Apis.URL_RELEASE_CIRCLE_POST, map, ReleaseCircleBean.class);
                }
                String content = writeRemate.getText().toString().trim();
                Map<String, Object> map = new HashMap<>();
                map.put("commodityId", String.valueOf(commodityId));
                map.put("orderId", orderId);
                map.put("content", content);
                map.put("image", mPicList);
                presenter.startImagsPost(Apis.URL_ADD_COMMODITY_COMMENT_LIST_POST, map, RemaitBean.class);

                break;
        }
    }
}
