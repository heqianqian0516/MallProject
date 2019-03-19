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
import order.bean.OrderBean;

/**
 * 待收货一级Adaper
 */
public class OrderWaitAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<OrderBean.OrderListBean> mOrder;
    private Context mContext;

    public OrderWaitAdaper(Context mContext) {
        this.mContext = mContext;
        mOrder = new ArrayList<>();
    }

    public void setmOrder(List<OrderBean.OrderListBean> orders) {
        //mOrder.clear();
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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_wait_fragment_item, viewGroup, false);
        ViewHolderWait holderWait = new ViewHolderWait(view);
        return holderWait;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        ViewHolderWait holderWait = (ViewHolderWait) viewHolder;
        holderWait.mark.setText(mOrder.get(i).getOrderId());
        //设置时间类型
        String date = new SimpleDateFormat("yyyy-MM-dd").format(
                new java.util.Date(mOrder.get(i).getOrderTime()));
        holderWait.time.setText(date);
        holderWait.unit.setText(mOrder.get(i).getExpressCompName());
        holderWait.num.setText(mOrder.get(i).getExpressSn());
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        holderWait.recycleTitle.setLayoutManager(layoutManager);
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
    }

    @Override
    public int getItemCount() {
        return mOrder.size();
    }

    class ViewHolderWait extends RecyclerView.ViewHolder {
        @BindView(R.id.mark)
        TextView mark;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.recycle_title)
        RecyclerView recycleTitle;
        @BindView(R.id.payment_button)
        Button paymentButton;
        @BindView(R.id.unit)
        TextView unit;
        @BindView(R.id.num)
        TextView num;
        @BindView(R.id.order_constr)
        ConstraintLayout orderConstr;
        public ViewHolderWait(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    //定义接口
    private CallBackWait callBackWait;
    public void setCallBackWait(CallBackWait callBackWait){
        this.callBackWait = callBackWait;
    }
    public interface CallBackWait{
        void callBack(String orderId);
    }
}
