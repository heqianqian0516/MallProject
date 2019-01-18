package fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bwei.mallproject.AddAddressActivity;
import com.bwei.mallproject.MainFootActivity;
import com.bwei.mallproject.MyCircleActivity;
import com.bwei.mallproject.MyWalletActivity;
import com.bwei.mallproject.PersonalDataActivity;
import com.bwei.mallproject.R;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import bean.EventBean;
import bean.LoginBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import presenter.IPresenterImpl;
import view.IView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class MyFragment extends Fragment implements IView,View.OnClickListener {
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.personaldata)
    TextView personaldata;
    @BindView(R.id.mycircle)
    TextView mycircle;
    @BindView(R.id.footprint)
    TextView footprint;
    @BindView(R.id.Wallet)
    TextView Wallet;
    @BindView(R.id.shippingaddress)
    TextView shippingaddress;
    @BindView(R.id.mine_icon)
    SimpleDraweeView mineIcon;
    Unbinder unbinder;

    private IPresenterImpl iPresenter;
    private LoginBean loginBean;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private View view;
    private PopupWindow mPopupWindow;
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESIZE_REQUEST_CODE = 2;
    private static final String IMAGE_FILE_NAME = "header.jpg";
    private File file;
    private Button textXiang;
    private Button textPhono;
    private Button textCancel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myfragment_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iPresenter = new IPresenterImpl(this);
        sharedPreferences = getActivity().getSharedPreferences("Address", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        loadData();

    }

    private void loadData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.nickname, R.id.personaldata, R.id.mycircle, R.id.footprint, R.id.Wallet, R.id.shippingaddress, R.id.mine_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nickname:

                break;
            case R.id.personaldata:
                Intent intent = new Intent(getActivity(), PersonalDataActivity.class);
                intent.putExtra("Name", loginBean.getResult().getNickName());
                intent.putExtra("headIcon", loginBean.getResult().getHeadPic());
                startActivity(intent);
                break;
            case R.id.mycircle:
                Intent intent1 = new Intent(getActivity(), MyCircleActivity.class);
                startActivity(intent1);
                break;
            case R.id.footprint:
                Intent intent2 = new Intent(getActivity(), MainFootActivity.class);
                startActivity(intent2);
                break;
            case R.id.Wallet:
                Intent intent3 = new Intent(getActivity(), MyWalletActivity.class);
                startActivity(intent3);
                break;
            case R.id.shippingaddress:
                Intent intent5 = new Intent(getActivity(), AddAddressActivity.class);
                editor.putString("nickName", loginBean.getResult().getNickName());
                editor.putString("phone", loginBean.getResult().getPhone());
                editor.commit();
                startActivity(intent5);
                break;
            case R.id.mine_icon:
                //popwindow显示
                showPopWindow();
                break;
            default:
                break;

        }
    }
    //获取图片的URI
    private Uri getImageUri() {
        file = new File(Environment.getExternalStorageDirectory(),
                IMAGE_FILE_NAME);
        return Uri.fromFile(file);
    }
    //判断SD卡是否存在
    private boolean isSdcardExisting() {
        // 判断SD卡是否存在，并且是否具有读写权限
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            //存在
            return true;
        }else{
            //不存在
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){//成功
            switch (requestCode) {//通过请求码进行不同的执行
                case IMAGE_REQUEST_CODE:
                    resizeImage(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    if (isSdcardExisting()) {
                        resizeImage(getImageUri());
                    } else {
                        Toast.makeText(getActivity(), "未找到存储卡，无法存储照片！",
                                Toast.LENGTH_LONG).show();
                    }
                    break;
                case RESIZE_REQUEST_CODE:
                    // 从剪切图片返回的数据
                    if (data != null) {
                        Bitmap bitmap = data.getParcelableExtra("data");
                        //获得图片
                        mineIcon.setImageBitmap(bitmap);
                    }
                    break;

            }
        }
    }
    /**
     * 对图片进行处理
     * @param uri
     */
    private void resizeImage(Uri uri) {
        //裁剪图片 （不变的数据）
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, RESIZE_REQUEST_CODE);
    }

    /**
     * 展示头像
     *
     */
    private void showResizeImage(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            mineIcon.setImageDrawable(drawable);
            headUploding();
        }
    }

    private void headUploding() {
        //创建OkHttpClient请求对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建RequestBody 封装file参数
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        //创建RequestBody 设置类型等
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("file", file.getName(), fileBody).build();
        //创建Request
        Request request = new Request.Builder().url("http://www.zhaoapi.cn/file/upload?uid=14646").post(requestBody).build();
        //得到Call
        okhttp3.Call call = okHttpClient.newCall(request);
        //执行请求
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Log.d("成功", "onResponse: 开心");
            }
        });
    }

    private void showPopWindow() {
        //加载popWindow
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_window_cream_layout, null);
        textXiang = view.findViewById(R.id.text_xiang);
        textPhono = view.findViewById(R.id.text_phono);
        textCancel = view.findViewById(R.id.text_quxiao);
        //按钮设置监听
        textXiang.setOnClickListener(this);
        textPhono.setOnClickListener(this);
        textCancel.setOnClickListener(this);


        //设置填充
        mPopupWindow = new PopupWindow(view,FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT,true);
        mPopupWindow.setContentView(view);
        //显示popupWindow位置
        mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);


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
        if (evBean.getName().equals("main")) {
            loginBean = (LoginBean) evBean.getClazz();
            Uri uri = Uri.parse(loginBean.getResult().getHeadPic());
            mineIcon.setImageURI(uri);
            //  Glide.with(getActivity()).load(loginBean.getResult().getHeadPic()).into(mineIcon);
            nickname.setText(loginBean.getResult().getNickName());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
           case  R.id.text_xiang:
               //判断SD卡是否存在
               if(isSdcardExisting()){
                   //激活相机
                   Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                   //获取图片路径getImageUri   将拍摄的图片存在我们想存的地方
                   cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
                   //MediaStore.EXTRA_VIDEO_QUALITY- 此值在最低质量最小文件尺寸时是0，在最高质量最大文件尺寸时是１.
                   cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                   // 开启一个带有返回值的Activity，请求码为CAMERA_REQUEST_CODE
                   startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
               } else {
                   //吐司不存在SD卡
                   Toast.makeText(v.getContext(), "请插入sd卡", Toast.LENGTH_LONG) .show();
               }
               //popWindows取消
               mPopupWindow.dismiss();
               break;
            case  R.id.text_phono:
                //ACTION_PICK 选择数据
                Intent intentOne = new Intent(Intent.ACTION_PICK);
                //设置图片类型
                intentOne.setType("image/*");
                // 开启一个带有返回值的Activity，请求码为IMAGE_REQUEST_CODE
                startActivityForResult(intentOne, IMAGE_REQUEST_CODE);
                mPopupWindow.dismiss();
                break;
            case  R.id.text_quxiao:
                mPopupWindow.dismiss();
                break;
        }
    }
}
