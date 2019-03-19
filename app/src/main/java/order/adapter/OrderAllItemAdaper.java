package order.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.bwei.mallproject.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import order.bean.OrderBean;

/**
 * 全部订单的子条目的Adaper
 * */
public class OrderAllItemAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OrderBean.OrderListBean.DetailListBean> mData;
    private Context mContext;

    public OrderAllItemAdaper(Context mContext) {
        this.mContext = mContext;
        mData = new ArrayList<>();
    }
    public void setmData(List<OrderBean.OrderListBean.DetailListBean> datas){
        mData.clear();
        if(datas!=null){
            mData.addAll(datas);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_all_child_item, viewGroup, false);
        ViewHolderAllItem holderAllItem = new ViewHolderAllItem(view);
        return holderAllItem;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderAllItem holderAllItem = (ViewHolderAllItem) viewHolder;
        String imsge = mData.get(i).getCommodityPic().split("\\,")[0].replace("https","http");
        holderAllItem.itemImage.setImageURI(Uri.parse(imsge));
        holderAllItem.itemName.setText(mData.get(i).getCommodityName());
        holderAllItem.itemPrice.setText("￥"+mData.get(i).getCommodityPrice());
        holderAllItem.itemNum.setText("共"+mData.get(i).getCommodityCount()+"件");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolderAllItem extends RecyclerView.ViewHolder {
        @BindView(R.id.item_image)
        SimpleDraweeView itemImage;
        @BindView(R.id.item_name)
        TextView itemName;
        @BindView(R.id.item_price)
        TextView itemPrice;
        @BindView(R.id.item_num)
        TextView itemNum;
        @BindView(R.id.item_const)
        ConstraintLayout itemConst;
        public ViewHolderAllItem(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
