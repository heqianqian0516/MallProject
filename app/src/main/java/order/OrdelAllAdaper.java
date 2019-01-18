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

public class OrdelAllAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<OrderBean.OrderListBean> mOrder;
    private Context mContext;
    //待付款
    private final int TYPE_OBLIGATION = 1;
    //待收货Wait
    private final int TYPE_WAIT = 2;
    //待评价
    private final int TYPE_REMAIT = 3;
    //已完成
    private final int TYPE_STOCKS = 9;
    private final int TYPE_NONE = -1;

    public OrdelAllAdaper(Context mContext) {
        this.mContext = mContext;
        mOrder = new ArrayList<>();
    }

    public void setmOrder(List<OrderBean.OrderListBean> orders) {
        mOrder.clear();
        if (orders != null) {
            mOrder.addAll(orders);
        }
        notifyDataSetChanged();
    }

    public void addmOrder(List<OrderBean.OrderListBean> orders) {
        if (orders != null) {
            mOrder.addAll(orders);
        }
        notifyDataSetChanged();
    }
    //删除
    public void setDel(int position){
        mOrder.remove(position);
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        if (mOrder.get(position).getOrderStatus() == TYPE_OBLIGATION) {
            return TYPE_OBLIGATION;
        } else if (mOrder.get(position).getOrderStatus() == TYPE_WAIT) {
            return TYPE_WAIT;
        } else if (mOrder.get(position).getOrderStatus() == TYPE_REMAIT) {
            return TYPE_REMAIT;
        } else if (mOrder.get(position).getOrderStatus() == TYPE_STOCKS) {
            return TYPE_STOCKS;
        }
        return TYPE_NONE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder holder = null;
        if (i == TYPE_OBLIGATION) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.order_obligation_fragment_item, viewGroup, false);
            holder = new ViewHolderObligation(view);
        } else if (i == TYPE_WAIT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.order_wait_fragment_item, viewGroup, false);
            holder = new ViewHolderWait(view);
        } else if (i == TYPE_REMAIT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.order_remait_fragment_item, viewGroup, false);
            holder = new ViewHolderRemait(view);
        } else if (i == TYPE_STOCKS) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.order_stocks_fragment_item, viewGroup, false);
            holder = new ViewHolderStocks(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        int type = getItemViewType(i);
        switch (type) {
            case TYPE_OBLIGATION:
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
                //取消订单
                holderObligation.cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(callBackDel!=null){
                            callBackDel.callBack(mOrder.get(i).getOrderId(),i);
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
                break;
            case TYPE_WAIT:
                ViewHolderWait holderWait = (ViewHolderWait) viewHolder;
                holderWait.mark.setText(mOrder.get(i).getOrderId());
                //设置时间类型
                String dateWait = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                        new java.util.Date(mOrder.get(i).getOrderTime()));
                holderWait.time.setText(dateWait);
                holderWait.unit.setText(mOrder.get(i).getExpressCompName());
                holderWait.num.setText(mOrder.get(i).getExpressSn());
                LinearLayoutManager layoutManagerWait = new LinearLayoutManager(mContext);
                layoutManagerWait.setOrientation(OrientationHelper.VERTICAL);
                holderWait.recycleTitle.setLayoutManager(layoutManagerWait);
                OrderWaitItemAdaper waitItemAdaper = new OrderWaitItemAdaper(mContext);
                holderWait.recycleTitle.setAdapter(waitItemAdaper);
                waitItemAdaper.setmData(mOrder.get(i).getDetailList());
                //确认收货
                holderWait.paymentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(callBackWait!=null){
                            callBackWait.callBack(mOrder.get(i).getOrderId());
                        }
                    }
                });
                break;
            case TYPE_REMAIT:
                ViewHolderRemait holderRemait = (ViewHolderRemait) viewHolder;
                holderRemait.mark.setText(mOrder.get(i).getOrderId());
                LinearLayoutManager layoutManagerRemait = new LinearLayoutManager(mContext);
                layoutManagerRemait.setOrientation(OrientationHelper.VERTICAL);
                holderRemait.recycleTitle.setLayoutManager(layoutManagerRemait);
                OrderRemaitItemAdaper remaitItemAdaper = new OrderRemaitItemAdaper(mContext);
                holderRemait.recycleTitle.setAdapter(remaitItemAdaper);
                remaitItemAdaper.setmData(mOrder.get(i).getDetailList());
                //删除
                holderRemait.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(callBackDel!=null){
                            callBackDel.callBack(mOrder.get(i).getOrderId(),i);
                        }
                    }
                });
                break;
            case TYPE_STOCKS:
                ViewHolderStocks holderStocks = (ViewHolderStocks) viewHolder;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mOrder.size();
    }

    //待付款
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

    //待收货Wait
    class ViewHolderWait extends RecyclerView.ViewHolder {
        @BindView(R.id.dingdan)
        TextView dingdan;
        @BindView(R.id.mark)
        TextView mark;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.recycle_title)
        RecyclerView recycleTitle;
        @BindView(R.id.payment_button)
        Button paymentButton;
        @BindView(R.id.company)
        TextView company;
        @BindView(R.id.unit)
        TextView unit;
        @BindView(R.id.numbers)
        TextView numbers;
        @BindView(R.id.num)
        TextView num;
        @BindView(R.id.order_constr)
        ConstraintLayout orderConstr;

        public ViewHolderWait(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //待评价
    class ViewHolderRemait extends RecyclerView.ViewHolder {
        @BindView(R.id.dingdan)
        TextView dingdan;
        @BindView(R.id.mark)
        TextView mark;
        @BindView(R.id.delete)
        TextView delete;
        @BindView(R.id.recycle_title)
        RecyclerView recycleTitle;
        @BindView(R.id.order_constr)
        ConstraintLayout orderConstr;

        public ViewHolderRemait(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //已完成
    class ViewHolderStocks extends RecyclerView.ViewHolder {
        @BindView(R.id.dingdan)
        TextView dingdan;
        @BindView(R.id.mark)
        TextView mark;
        @BindView(R.id.del)
        TextView del;
        @BindView(R.id.recycle_title)
        RecyclerView recycleTitle;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.order_constr)
        ConstraintLayout orderConstr;
        public ViewHolderStocks(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    /**定义接口删除订单*/
    private CallBackDel callBackDel;
    public void setCallBackDel(CallBackDel callBackDel){
        this.callBackDel = callBackDel;
    }
    public interface CallBackDel{
        void callBack(String orderId, int position);
    }
    /**定义接口去支付*/
    private CallBackPay callBackPay;
    public void setCallBackPay(CallBackPay callBackPay){
        this.callBackPay = callBackPay;
    }
    public interface CallBackPay{
        void callBack(String orderId, Double payAmount);
    }
   /** 定义接口确认收货*/
    private CallBackWait callBackWait;
    public void setCallBackWait(CallBackWait callBackWait){
        this.callBackWait = callBackWait;
    }
    public interface CallBackWait{
        void callBack(String orderId);
    }
}
