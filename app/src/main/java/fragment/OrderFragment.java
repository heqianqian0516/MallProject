package fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bwei.mallproject.R;

import java.util.ArrayList;
import java.util.List;

import adapter.ChildFragmentAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fragment.fragments.AllOrdersFragment;
import fragment.fragments.CompletedFragment;
import fragment.fragments.EvaluateFragment;
import fragment.fragments.GoodsFragment;
import fragment.fragments.PayMentFragment;

public class OrderFragment extends Fragment {
    @BindView(R.id.allorders)
    RadioButton mAllorders;
    @BindView(R.id.obligation)
    RadioButton mObligation;
    @BindView(R.id.forCollection)
    RadioButton mForCollection;
    @BindView(R.id.beevaluated)
    RadioButton mBeevaluated;
    @BindView(R.id.completed)
    RadioButton mCompleted;
    @BindView(R.id.group_divider)
    RadioGroup mGroupDivider;
    @BindView(R.id.purchaseViewPage)
    ViewPager mPurchaseViewPage;
    private View view;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.orderfragment_layout, null);
        unbinder = ButterKnife.bind(this, view);
        initData();
         initView();
        return view;
    }

    private void initView() {
        mGroupDivider.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.allorders:
                        mPurchaseViewPage.setCurrentItem(0);
                        break;
                    case R.id.obligation:
                        mPurchaseViewPage.setCurrentItem(1);
                        break;
                    case R.id.forCollection:
                        mPurchaseViewPage.setCurrentItem(2);
                        break;
                    case R.id.beevaluated:
                        mPurchaseViewPage.setCurrentItem(3);
                        break;
                    case R.id.completed:
                        mPurchaseViewPage.setCurrentItem(4);
                        break;

                }
            }
        });
    }


    private void initData() {
        List<Fragment> list=new ArrayList<>();
        list.add(new AllOrdersFragment());
        list.add(new PayMentFragment());
        list.add(new GoodsFragment());
        list.add(new EvaluateFragment());
        list.add(new CompletedFragment());
        ChildFragmentAdapter fragmentAdapter=new ChildFragmentAdapter(getChildFragmentManager(),list);
        mPurchaseViewPage.setAdapter(fragmentAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
