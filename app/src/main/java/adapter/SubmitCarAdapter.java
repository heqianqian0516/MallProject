package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwei.mallproject.R;

import java.util.ArrayList;
import java.util.List;

import bean.ShowShoppingBean;
import view.MyAddSubView;
/**
 * 结算
 * */
public class SubmitCarAdapter extends RecyclerView.Adapter<SubmitCarAdapter.ViewHolder> {
    private List<ShowShoppingBean.ResuleBean> mList;
    private Context context;

    public SubmitCarAdapter(Context context) {
        this.context = context;
        mList = new ArrayList<>();
    }

    public void setmData(List<ShowShoppingBean.ResuleBean> datas) {
        mList.clear();
        if (datas != null) {
            mList.addAll(datas);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.submit_goods_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
       final ViewHolder holder=viewHolder;
       holder.mTitleName.setText(mList.get(position).getCommodityName());
       holder.mProducePrice.setText("￥"+mList.get(position).getPrice()+"");
        //Uri parse = Uri.parse(mList.get(position).getPic());
        Glide.with(context).load(mList.get(position).getPic()).into(holder.mProductIcon);

       holder.mRemoveView.setOnNumberChangeListener(new MyAddSubView.OnNumberChangeListener() {
           @Override
           public void onNumberChange(int num) {
               mList.get(position).setCount(num);
               notifyDataSetChanged();
           }
       });


    }
    //view层调用这个方法, 点击quanxuan按钮的操作
    public void checkAll(boolean checked) {
        for (int i=0;i<mList.size();i++){
            mList.get(i).setItem_check(checked);
        }
        notifyDataSetChanged();
        sum(mList);
    }

    private void sum(List<ShowShoppingBean.ResuleBean> mlist) {
        //初始的总价为0
        int totalNum = 0;
        float totalMoney = 0.0f;
        boolean allCheck = true;
        for (int i=0;i<mlist.size();i++){
            //把 已经选中的 条目 计算价格
            if (mlist.get(i).isItem_check()){
                totalNum += mlist.get(i).getCount();
                totalMoney += mlist.get(i).getCount() * mlist.get(i).getPrice();
            }else{
                //如果有个未选中,就标记为false
                allCheck = false;
            }
        }
        updateListener.setTotal(totalMoney+"",totalNum+"",allCheck);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private final ImageView mProductIcon;
        private final TextView mTitleName;
        private final TextView mProducePrice;
        private final MyAddSubView mRemoveView;

        public ViewHolder(View itemView) {
            super(itemView);

            mProductIcon = itemView.findViewById(R.id.product_icon_iv);
            mTitleName = itemView.findViewById(R.id.product_title_name_tv);
            mProducePrice = itemView.findViewById(R.id.product_price_tv);
            mRemoveView = itemView.findViewById(R.id.add_remove_view);
        }
    }
    /////////////////////////定义接口的固定形式////////////////////
    UpdateListener updateListener;
    public void setUpdateListener(UpdateListener updateListener){
        this.updateListener = updateListener;
    }
    //接口
    public interface UpdateListener{
        public void setTotal(String total, String num, boolean allCheck);
    }
    /**
     * 定义条目点击接口
     */
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    /**
     * 定义条目长按事件的接口
     */
    public interface OnLongItemClickListener {
        void onItemLongClick(View itemView, int position);
    }
    private OnItemClickListener clickListener;
    private OnLongItemClickListener longItemClickListener;

    /**
     * 点击事件回调
     * @param clickListener
     */
    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * 长按事件回调
     * @param longItemClickListener
     */
    public void setOnLongItemClickListener(OnLongItemClickListener longItemClickListener) {
        this.longItemClickListener = longItemClickListener;
    }
    private ShopCarListener shopCarListener;
    public void setShopCarListener(ShopCarListener shopCarListener) {
        this.shopCarListener = shopCarListener;
    }
    public interface ShopCarListener {
        void callBack(List<ShowShoppingBean.ResuleBean> mlist);
    }
}
