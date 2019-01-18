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

import bean.QueryAddrBean;


public class AddrLstAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<QueryAddrBean.ResultBean> mList;
    private Context mContext;
    private Button adapterMyaddressBtnDelete;

    public AddrLstAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public void setData(List<QueryAddrBean.ResultBean> datas) {
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        LstViewHolder holder= (LstViewHolder) viewHolder;
        holder.adapterMyaddressTextName.setText(mList.get(i).getRealName());
        holder.adapterMyaddressTextPhone.setText(mList.get(i).getPhone());
        holder.adapterMyaddressTextAddress.setText(mList.get(i).getAddress());
        holder.adapterMyaddressRadioDefault.setChecked(true);
        holder.adapterMyaddressBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.adapterMyaddressBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
}
