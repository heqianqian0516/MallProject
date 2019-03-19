package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


import com.bwei.mallproject.R;

import java.util.ArrayList;
import java.util.List;

import bean.AddAddrBean;
import bean.QueryAddrBean;
import bean.QueryBean;
import mine.bean.AddressBean;
import retrofit2.http.QueryMap;


public class AddrLstAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<AddressBean.ResultBean> mList;
    private Context mContext;
    private Button adapterMyaddressBtnDelete;
    private int position;
    public AddrLstAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public void setData(List<AddressBean.ResultBean> datas) {
        mList.clear();
        if (datas != null) {
            mList.addAll(datas);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_myaddr_tem,
                viewGroup, false);
        return new LstViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        LstViewHolder holder= (LstViewHolder) viewHolder;
        holder.adapterMyaddressTextName.setText(mList.get(i).getRealName());
        holder.adapterMyaddressTextPhone.setText(mList.get(i).getPhone());
        holder.adapterMyaddressTextAddress.setText(mList.get(i).getAddress());
        holder.adapterMyaddressRadioDefault.setChecked(true);
        holder.adapterMyaddressBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBackUpdata!=null){
                    callBackUpdata.callBack(mList.get(i));
                }
            }
        });
        if(mList.get(i).getWhetherDefault() == 1){
            holder.adapterMyaddressRadioDefault.setChecked(true);
        }else{
            holder.adapterMyaddressRadioDefault.setChecked(false);
        }
        holder.adapterMyaddressRadioDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置默认值
                position=i;
                setAllunCheck();
                if(callBackDefault!=null){
                    callBackDefault.callBack(mList.get(i).getId());
                }
            }
        });
        holder.adapterMyaddressBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    //改变默认值
    private void setAllunCheck() {
        int size = mList.size();
        for (int i=0;i<size;i++){
            if(i==position){
                mList.get(i).setWhetherDefault(1);
            }else{
                mList.get(i).setWhetherDefault(2);
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class LstViewHolder extends RecyclerView.ViewHolder {
        TextView adapterMyaddressTextName;
        TextView adapterMyaddressTextPhone;
        TextView adapterMyaddressTextAddress;
        CheckBox adapterMyaddressRadioDefault;
        Button adapterMyaddressBtnUpdate;
        Button adapterMyaddressBtnDelete;

        public LstViewHolder(@NonNull View itemView) {
            super(itemView);
            adapterMyaddressTextName=itemView.findViewById(R.id.adapter_myaddress_text_name);
            adapterMyaddressTextPhone=itemView.findViewById(R.id.adapter_myaddress_text_phone);
            adapterMyaddressTextAddress=itemView.findViewById(R.id.adapter_myaddress_text_address);
            adapterMyaddressRadioDefault=itemView.findViewById(R.id.adapter_myaddress_radio_default);
            adapterMyaddressBtnUpdate=itemView.findViewById(R.id.adapter_myaddress_btn_update);
            adapterMyaddressBtnDelete=itemView.findViewById(R.id.adapter_myaddress_btn_delete);
        }
    }
    //定义接口
    private CallBackUpdata callBackUpdata;
    public void setCallBackUpdata(CallBackUpdata callBackUpdata){
        this.callBackUpdata = callBackUpdata;
    }
    public interface CallBackUpdata{
        void callBack(AddressBean.ResultBean resultBean);
    }
    //定义默认选中的接口
    private CallBackDefault callBackDefault;
    public void setCallBackDefault(CallBackDefault callBackDefault){
        this.callBackDefault = callBackDefault;
    }
    public interface CallBackDefault{
        void callBack(int id);
    }
}
