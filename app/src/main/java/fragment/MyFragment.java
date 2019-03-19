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

import api.Apis;
import bean.EventBean;
import bean.LoginBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import mine.bean.InformationBean;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import presenter.IPresenterImpl;
import view.IView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class MyFragment extends Fragment implements IView {
    @BindView(R.id.nickname)
    TextView mNickName;
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
    private String headPic;
    private String nickName;
    private String password;
    private InformationBean.ResultBean result;


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
      iPresenter.startRequestGet(Apis.URL_QUERYBYID_GET,null,InformationBean.class);
    }


    @OnClick({R.id.nickname, R.id.personaldata, R.id.mycircle, R.id.footprint, R.id.Wallet, R.id.shippingaddress, R.id.mine_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nickname:

                break;
            case R.id.personaldata:
                Intent intent = new Intent(getActivity(), PersonalDataActivity.class);
                intent.putExtra("result",result);
                startActivityForResult(intent,100);
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
               /* editor.putString("nickName", loginBean.getResult().getNickName());
                editor.putString("phone", loginBean.getResult().getPhone());
                editor.commit();*/
                startActivity(intent5);
                break;
            case R.id.mine_icon:
                  break;
            default:
                break;

        }
    }


    @Override
    public void getDataSuccess(Object data) {
        if (data instanceof InformationBean){
            InformationBean informationBean= (InformationBean) data;
            headPic = informationBean.getResult().getHeadPic();
            nickName = informationBean.getResult().getNickName();
            password = informationBean.getResult().getPassword();
            result = informationBean.getResult();
            if (informationBean==null){
                Toast.makeText(getActivity(),informationBean.getMessage(),Toast.LENGTH_SHORT).show();
            }else{
                mineIcon.setImageURI(Uri.parse(headPic));
                mNickName.setText(nickName);
            }
        }
    }

    @Override
    public void getDataFail(String error) {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == 200){
            iPresenter.startRequestGet(Apis.URL_QUERYBYID_GET,null,InformationBean.class);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        iPresenter.onDetach();
    }

}
