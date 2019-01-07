package fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bwei.mallproject.R;

import view.IView;
import adapter.CircleAdapter;
import api.Apis;
import bean.CircleBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import presenter.IPresenterImpl;

public class CircleFragment extends Fragment implements IView {
    private static final String TAG = "CircleFragment++++++++";

    @BindView(R.id.recy)
    RecyclerView mRecy;
    private View view;
    private IPresenterImpl presenter;
    private Unbinder unbinder;
    private int page=1;
    private int count=5;
    private CircleAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.circlefragment_layout, null);
        presenter = new IPresenterImpl(this);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        return view;

    }

    private void initData() {
        Log.d(TAG, "initData: ++++++++"+Apis.URL_FIND_CIRCLE_LIST_GET+"*************");
     presenter.startRequestGet(String.format(Apis.URL_FIND_CIRCLE_LIST_GET,page,count),null,CircleBean.class);

    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecy.setLayoutManager(layoutManager);
        adapter = new CircleAdapter(getActivity());
        mRecy.setAdapter(adapter);
    }

    @Override
    public void getDataSuccess(Object data) {
        Log.d(TAG, "getDataSuccess: +++++++++"+CircleBean.class);
         if (data instanceof CircleBean){
             CircleBean bean= (CircleBean) data;
             if (!bean.getMessage().equals("查询成功")){
                 Toast.makeText(getActivity(),bean.getMessage(),Toast.LENGTH_SHORT).show();
             }else{
                 adapter.setmData(bean.getResult());
             }
         }
    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
