package adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bwei.mallproject.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import bean.FindIdBean;
import bean.SearchBean;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<SearchBean.ResultBean> mData;
    private Context context;
    public SearchAdapter(Context context){
        this.context=context;
        mData=new ArrayList<>();
    }
    public void setmData(List<SearchBean.ResultBean> datas) {
        mData.clear();
        if (datas!=null){
            mData.addAll(datas);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.find_id_layout,parent,false);
        SearchAdapter.ViewHolder holder=new SearchAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder viewHolder, final int position) {
        SearchAdapter.ViewHolder holder=viewHolder;
        String image = mData.get(position).getMasterPic().split("\\|")[0].replace("https", "http");
        holder.textName.setText(mData.get(position).getCommodityName());
        holder.textPrice.setText(mData.get(position).getPrice()+"");
        Uri uri = Uri.parse(image);
        holder.sdv_view.setImageURI(uri);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (catagralTwoCallBack!=null){
                    catagralTwoCallBack.callBack(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private final SimpleDraweeView sdv_view;
        private final TextView textName;
        private final TextView textPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            sdv_view = itemView.findViewById(R.id.sdv_view);
            textName = itemView.findViewById(R.id.text_name);
            textPrice = itemView.findViewById(R.id.text_price);
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
