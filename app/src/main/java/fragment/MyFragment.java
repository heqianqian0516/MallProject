package fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

import bean.EventBean;
import bean.LoginBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import presenter.IPresenterImpl;
import view.IView;

import static android.content.Context.MODE_PRIVATE;

public class MyFragment extends Fragment implements IView {
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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.myfragment_layout,null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iPresenter=new IPresenterImpl(this);
        sharedPreferences=getActivity().getSharedPreferences("Address",MODE_PRIVATE);
        editor=sharedPreferences.edit();

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
                Intent intent=new Intent(getActivity(),PersonalDataActivity.class);
                intent.putExtra("Name",loginBean.getResult().getNickName());
                intent.putExtra("headIcon",loginBean.getResult().getHeadPic());
                startActivity(intent);
                break;
            case R.id.mycircle:
                Intent intent1=new Intent(getActivity(),MyCircleActivity.class);
                startActivity(intent1);
                break;
            case R.id.footprint:
                Intent intent2=new Intent(getActivity(),MainFootActivity.class);
                startActivity(intent2);
                break;
            case R.id.Wallet:
                Intent intent3=new Intent(getActivity(),MyWalletActivity.class);
                startActivity(intent3);
                break;
            case R.id.shippingaddress:
                Intent intent5=new Intent(getActivity(),AddAddressActivity.class);
                editor.putString("nickName",loginBean.getResult().getNickName());
                editor.putString("phone",loginBean.getResult().getPhone());
                editor.commit();
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
        if (evBean.getName().equals("main")){
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
}
