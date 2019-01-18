package order;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.bwei.mallproject.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * 待付款一级Recycleview的Adaper
 * */
public class OrderObligationAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OrderBean.OrderListBean> mOrder;
    private Context mContext;

    public OrderObligationAdaper(Context mContext) {
        this.mContext = mContext;
        mOrder = new ArrayList<>();
    }
    public void setmOrder(List<OrderBean.OrderListBean> orders){
        mOrder.clear();
        if(orders!=null){
            mOrder.addAll(orders);
        }
        notifyDataSetChanged();
    }
    public void addmOrder(List<OrderBean.OrderListBean> orders){
        if(orders!=null){
            mOrder.addAll(orders);
        }
        notifyDataSetChanged();
    }
    public void setDel(int position){
        mOrder.remove(position);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_obligation_fragment_item, viewGroup, false);
        ViewHolderObligation holderObligation = new ViewHolderObligation(view);
        return holderObligation;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        ViewHolderObligation holderObligation = (ViewHolderObligation) viewHolder;
        holderObligation.mark.setText(mOrder.get(i).getOrderId());
        //设置时间类型
        String date = new SimpleDateFormat("yyyy-MM-dd").format(
                new java.util.Date(mOrder.get(i).getOrderTime()));
        holderObligation.time.setText(date);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        holderObligation.recycleTitle.setLayoutManager(layoutManager);
        OrderAllItemAdaper allItemAdaper = new OrderAllItemAdaper(mContext);
        holderObligation.recycleTitle.setAdapter(allItemAdaper);
        allItemAdaper.setmData(mOrder.get(i).getDetailList());
        int num = 0;
        List<OrderBean.OrderListBean.DetailListBean> detailList = mOrder.get(i).getDetailList();
        for(OrderBean.OrderListBean.DetailListBean list:detailList){
                num += list.getCommodityCount();
        }
        holderObligation.numPrice.setText("共"+num+"件商品，需付款"+mOrder.get(i).getPayAmount());
        //删除订单
        holderObligation.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBackObligation!=null){
                    callBackObligation.callBack(mOrder.get(i).getOrderId(),i);
                }
            }
        });
        //去支付
        holderObligation.paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBackPay!=null){
                    callBackPay.callBack(mOrder.get(i).getOrderId(),mOrder.get(i).getPayAmount());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOrder.size();
    }

    class ViewHolderObligation extends RecyclerView.ViewHolder {
        @BindView(R.id.dingdan)
        TextView dingdan;
        @BindView(R.id.mark)
        TextView mark;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.recycle_title)
        RecyclerView recycleTitle;
        @BindView(R.id.num_price)
        TextView numPrice;
        @BindView(R.id.cancel_button)
        Button cancelButton;
        @BindView(R.id.payment_button)
        Button paymentButton;
        @BindView(R.id.order_constr)
        ConstraintLayout orderConstr;

        public ViewHolderObligation(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    //定义接口删除订单
    private CallBackObligation callBackObligation;
    public void setCallBackAll(CallBackObligation callBackObligation){
        this.callBackObligation = callBackObligation;
    }
    public interface CallBackObligation{
        void callBack(String orderId, int position);
    }
    //定义接口去支付
    private CallBackPay callBackPay;
    public void setCallBackPay(CallBackPay callBackPay){
        this.callBackPay = callBackPay;
    }
    public interface CallBackPay{
        void callBack(String orderId, Double payAmount);
    }
}
