package order;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.bwei.mallproject.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * 待评价子条目Adaper
 * */
public class OrderRemaitItemAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OrderBean.OrderListBean.DetailListBean> mData;
    private Context mContext;

    public OrderRemaitItemAdaper(Context mContext) {
        this.mContext = mContext;
        mData = new ArrayList<>();
    }

    public void setmData(List<OrderBean.OrderListBean.DetailListBean> datas) {
        mData.clear();
        if (datas != null) {
            mData.addAll(datas);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_remait_child_item, viewGroup, false);
        ViewHolderRemateItem holderRemateItem = new ViewHolderRemateItem(view);
        return holderRemateItem;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderRemateItem holderRemateItem = (ViewHolderRemateItem) viewHolder;
        String imsge = mData.get(i).getCommodityPic().split("\\,")[0].replace("https","http");
        holderRemateItem.itemImage.setImageURI(Uri.parse(imsge));
        holderRemateItem.itemName.setText(mData.get(i).getCommodityName());
        holderRemateItem.itemPrice.setText("￥"+mData.get(i).getCommodityPrice());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolderRemateItem extends RecyclerView.ViewHolder {
        @BindView(R.id.item_image)
        SimpleDraweeView itemImage;
        @BindView(R.id.item_name)
        TextView itemName;
        @BindView(R.id.item_price)
        TextView itemPrice;
        @BindView(R.id.payment_button)
        Button paymentButton;
        @BindView(R.id.item_const)
        ConstraintLayout itemConst;
        public ViewHolderRemateItem(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
