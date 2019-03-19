package mine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import com.bwei.mallproject.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mine.bean.WalletBean;

/**
 * 我的钱包的Adaper
 * */
public class WalletAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<WalletBean.ResultBean.DetailListBean> mBean;
    private Context mContext;

    public WalletAdaper(Context mContext) {
        this.mContext = mContext;
        mBean = new ArrayList<>();
    }
    public void setmBean(List<WalletBean.ResultBean.DetailListBean> beans){
        mBean.clear();
        if(beans!=null){
            mBean.addAll(beans);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.wallet_item, viewGroup, false);
        ViewHolderWallet holderWallet = new ViewHolderWallet(view);
        return holderWallet;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderWallet holderWallet = (ViewHolderWallet) viewHolder;
        holderWallet.sum.setText("￥"+mBean.get(i).getAmount());
        //设置时间类型
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                new java.util.Date(mBean.get(i).getCreateTime()));
        holderWallet.time.setText(date);
    }

    @Override
    public int getItemCount() {
        return mBean.size();
    }

    class ViewHolderWallet extends RecyclerView.ViewHolder {
        @BindView(R.id.sum)
        TextView sum;
        @BindView(R.id.time)
        TextView time;
        public ViewHolderWallet(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
