package com.bwei.mallproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import api.Apis;
import bean.LoginBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mine.bean.ImageHeadBean;
import mine.bean.InformationBean;
import mine.bean.MineUpdateNameBean;
import mine.bean.MineUpdatePasswordBean;
import presenter.IPresenterImpl;
import util.ImageFileUtil;
import view.IView;

/**
 * 我的个人资料
 **/
public class PersonalDataActivity extends AppCompatActivity implements IView {


    @BindView(R.id.userPicture)
    SimpleDraweeView mUserPicture;


    @BindView(R.id.nickname)
    TextView mNickname;

    @BindView(R.id.myPassword)
    TextView mMyPassword;
    @BindView(R.id.text_pic)
    TextView mTextPic;
    @BindView(R.id.newpwd)
    TextView mNewpwd;
    @BindView(R.id.viewThree)
    View mViewThree;
    @BindView(R.id.icon_view)
    View mIconView;
    @BindView(R.id.myName)
    TextView mMyName;
    @BindView(R.id.viewTwo)
    View mViewTwo;

    private IPresenterImpl presenter;
    private LoginBean loginBean;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private AlertDialog dialog;

    private TextView camera;
    private TextView pick;
    private TextView cancel;
    private PopupWindow window;
    private String path = Environment.getExternalStorageDirectory()
            + "/image.png";
    private String file = Environment.getExternalStorageDirectory()
            + "/file.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        presenter = new IPresenterImpl(this);
        ButterKnife.bind(this);
        initData();
        initView();
    }


    //传值数据
    private void initData() {
        Intent intent = getIntent();
        InformationBean.ResultBean result = (InformationBean.ResultBean) intent.getSerializableExtra("result");
        String headPic = result.getHeadPic();
        String nickName = result.getNickName();
        String password = result.getPassword();
        mUserPicture.setImageURI(Uri.parse(headPic));
        mNickname.setText(nickName);
        mNewpwd.setText(password);
        setResult(200, intent);
    }

    /**
     * 判断sd卡是否挂载
     */
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    private void initView() {
        //加载视图
        View view_p = View.inflate(this, R.layout.popupwindow_item, null);
        camera = view_p.findViewById(R.id.text_camera);
        pick = view_p.findViewById(R.id.text_pick);
        cancel = view_p.findViewById(R.id.text_cancel);
        //创建PopupWindow
        window = new PopupWindow(view_p, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置焦点
        window.setFocusable(true);
        //设置背景
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        //设置可触摸
        window.setTouchable(true);
        //打开相机
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调取系统相机
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (hasSdcard()) {
                    // 存到sdcard中
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(path)));
                    //执行
                    startActivityForResult(intent, 100);
                    window.dismiss();
                }

            }
        });
        //打开相册
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //加载相册
                Intent intent = new Intent(Intent.ACTION_PICK);
                if (hasSdcard()) {
                    //设置图片格式
                    intent.setType("image/*");
                    //执行
                    startActivityForResult(intent, 300);
                    window.dismiss();
                }

            }
        });

        //取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
    }

    @Override
    public void getDataSuccess(Object data) {
        //修改昵称
        if (data instanceof MineUpdateNameBean ){
            MineUpdateNameBean mineUpdateNameBean= (MineUpdateNameBean) data;
            Toast.makeText(PersonalDataActivity.this,mineUpdateNameBean.getMessage(),Toast.LENGTH_SHORT).show();
        }
        //修改密码
        else if(data instanceof MineUpdatePasswordBean){
            MineUpdatePasswordBean mineUpdatePasswordBean= (MineUpdatePasswordBean) data;
            Toast.makeText(PersonalDataActivity.this,mineUpdatePasswordBean.getMessage(),Toast.LENGTH_SHORT).show();
        }
        //修改头像
        else if(data instanceof ImageHeadBean){
            ImageHeadBean imageHeadBean= (ImageHeadBean) data;
            Toast.makeText(PersonalDataActivity.this,imageHeadBean.getMessage(),Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void getDataFail(String error) {

    }



    @OnClick({R.id.text_pic, R.id.userPicture, R.id.icon_view, R.id.myName, R.id.nickname, R.id.viewTwo, R.id.myPassword, R.id.newpwd})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.text_pic:
                break;
            case R.id.userPicture:
                window.showAtLocation(View.inflate(PersonalDataActivity.this, R.layout.activity_personal_data, null),
                        Gravity.BOTTOM, 0, 0);
                break;
            case R.id.icon_view:
                break;
            case R.id.myName:
                break;
            case R.id.nickname:
                final AlertDialog.Builder builder = new AlertDialog.Builder(PersonalDataActivity.this);
                View viewname = View.inflate(this, R.layout.presonal_alertdalog_name, null);
                builder.setView(viewname);
                Button update = viewname.findViewById(R.id.updata_btn);
                Button cencal = viewname.findViewById(R.id.cancal_btn);
                final EditText updateedix = viewname.findViewById(R.id.updata_edix);
                //修改
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = updateedix.getText().toString().trim();
                        if (name.equals("")) {
                            Toast.makeText(PersonalDataActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            Map<String, String> map = new HashMap<>();
                            map.put("nickName", name);
                            presenter.startRequestPut(Apis.URL_UPDATE_NAME_PUT, map, MineUpdateNameBean.class);
                            mNickname.setText(name);
                        }
                        dialog.dismiss();
                    }
                });
                //取消
                cencal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(PersonalDataActivity.this, "取消", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();
                break;
            case R.id.viewTwo:
                break;
            case R.id.myPassword:
                break;
            case R.id.newpwd:
                final AlertDialog.Builder pbuilder = new AlertDialog.Builder(PersonalDataActivity.this);
                View viewpas = View.inflate(this, R.layout.presonal_alertdalog_password, null);
                pbuilder.setView(viewpas);
                final EditText former_edix = viewpas.findViewById(R.id.former_edix);
                final EditText new_edix = viewpas.findViewById(R.id.new_edix);
                Button updatep = viewpas.findViewById(R.id.updata_btn);
                Button cancelp = viewpas.findViewById(R.id.cancal_btn);
                //修改
                updatep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String formerPass = former_edix.getText().toString().trim();
                        String newPass = new_edix.getText().toString().trim();
                        if (formerPass.equals("") || newPass.equals("")) {
                            Toast.makeText(PersonalDataActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                        }else if(formerPass.equals(newPass)){
                            Toast.makeText(PersonalDataActivity.this, "两次输入的密码一样", Toast.LENGTH_SHORT).show();
                        } else {
                            Map<String, String> map = new HashMap<>();
                            map.put("oldPwd", formerPass);
                            map.put("newPwd", newPass);
                            presenter.startRequestPut(Apis.URL_UPDATE_PASSWORD_PUT, map, MineUpdatePasswordBean.class);
                            mNewpwd.setText(newPass);
                            dialog.dismiss();
                        }

                    }
                });
                //取消
                cancelp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(PersonalDataActivity.this, "取消", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog = pbuilder.create();
                dialog.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //相机
        if(requestCode == 100 && resultCode == RESULT_OK){
            //调取裁剪功能
            Intent intent = new Intent("com.android.camera.action.CROP");
            //将图片设置给裁剪
            intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
            //设置是否支持裁剪
            intent.putExtra("CROP", true);
            //设置宽高比
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //设置显示大小
            intent.putExtra("outputX", 50);
            intent.putExtra("outputY", 50);
            //将图片返回给data
            intent.putExtra("return-data", true);
            startActivityForResult(intent, 200);
        }
        //相册
        if(requestCode == 300 && resultCode == RESULT_OK){
            //获取相册路径
            Uri uri = data.getData();
            //调取裁剪功能
            Intent intent = new Intent("com.android.camera.action.CROP");
            //将图片设置给裁剪
            intent.setDataAndType(uri, "image/*");
            //设置是否支持裁剪
            intent.putExtra("CROP", true);
            //设置框高比
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //设置显示大小
            intent.putExtra("outputX", 50);
            intent.putExtra("outputY", 50);
            //将图片返回给data
            intent.putExtra("return-data", true);
            startActivityForResult(intent, 200);
        }
        if(requestCode == 200 && resultCode == RESULT_OK){
            Bitmap bitmap = data.getParcelableExtra("data");
            try {
                ImageFileUtil.setBitmap(bitmap,file,50);
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(PersonalDataActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            Map<String,String> map = new HashMap<>();
            map.put("image",file);
            presenter.startImagHeadPost(Apis.URL_UPDATE_HEADERIMAGE_POST,map,ImageHeadBean.class);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
