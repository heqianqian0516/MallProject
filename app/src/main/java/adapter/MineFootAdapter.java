package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bwei.mallproject.DetailActivity;
import com.bwei.mallproject.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import bean.MineFootBean;
/**
 * 我的足迹
 * **/
public class MineFootAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MineFootBean.ResultBean> mList;
    private Context mContext;

    public MineFootAdapter(Context mContext) {
        this.mContext = mContext;
        mList=new ArrayList<>();
    }

    public void setList(List<MineFootBean.ResultBean> datas) {
        mList.clear();
        if (datas!=null){
            mList.addAll(datas);
        }
        notifyDataSetChanged();
    }
    public void addList(List<MineFootBean.ResultBean> datas) {
        if (datas!=null){
            mList.addAll(datas);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.item_myfoot,
                parent,false);
        return new MineFootViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MineFootViewHolder footViewHolder= (MineFootViewHolder) holder;
        Glide.with(mContext).load(mList.get(position).getMasterPic()).into(footViewHolder.imageView);
        footViewHolder.name.setText(mList.get(position).getCommodityName());
        footViewHolder.price.setText("￥"+mList.get(position).getPrice()+"");
        footViewHolder.llcs.setText("已浏览"+mList.get(position).getBrowseNum()+"次");
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                new java.util.Date(mList.get(position).getBrowseTime()));
        footViewHolder.llsj.setText(date);
        footViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,DetailActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public static class MineFootViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name;
        TextView price;
        TextView llcs;
        TextView llsj;
        public MineFootViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            name=itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.price);
            llcs=itemView.findViewById(R.id.llcs);
            llsj=itemView.findViewById(R.id.llsj);
        }
    }
    private SecondCategoryAdapter.CatagralTwoCallBack catagralTwoCallBack;
    public void setCatagralTwoCallBack(SecondCategoryAdapter.CatagralTwoCallBack catagralTwoCallBack){
        this.catagralTwoCallBack = catagralTwoCallBack;
    }
    public interface CatagralTwoCallBack{
        void callBack(int index);
    }
}
