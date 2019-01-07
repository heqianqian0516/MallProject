package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bwei.mallproject.R;

import java.util.ArrayList;
import java.util.List;


import bean.SecondCategoryBean;


public class SecondCategoryAdapter extends RecyclerView.Adapter<SecondCategoryAdapter.ViewHolder> {
    private List<SecondCategoryBean.ResultBean> mData;
    private Context context;
    public SecondCategoryAdapter(Context context){
        this.context=context;
        mData=new ArrayList<>();
    }
    public void setmData(List<SecondCategoryBean.ResultBean> datas) {
        mData.addAll(datas);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public SecondCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.second_category_layout,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SecondCategoryAdapter.ViewHolder holder, final int position) {
         ViewHolder holder1=holder;
         holder.bottomView.setText(mData.get(position).getName());
         holder.bottomView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(catagralTwoCallBack!=null){
                     catagralTwoCallBack.callBack(mData.get(position).getId());
                 }
             }
         });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView bottomView;

        public ViewHolder(View itemView) {
            super(itemView);
            bottomView = itemView.findViewById(R.id.bottom_text);
        }
    }
    //定义接口
    private CatagralTwoCallBack catagralTwoCallBack;
    public void setCatagralTwoCallBack(CatagralTwoCallBack catagralTwoCallBack){
        this.catagralTwoCallBack = catagralTwoCallBack;
    }
    public interface CatagralTwoCallBack{
        void callBack(String id);
    }
}

