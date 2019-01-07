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

import bean.ShowBean;

public class ShowHoriAdapter extends RecyclerView.Adapter<ShowHoriAdapter.ViewHolder> {
    private List<ShowBean.ResultBean.RxxpBean.CommodityListBean> mData;
    private Context mContext;

    public ShowHoriAdapter(Context mContext) {
        this.mContext = mContext;
        mData = new ArrayList<>();
    }
    public void setmData(List<ShowBean.ResultBean.RxxpBean.CommodityListBean> datas){
        mData.clear();
        if (datas !=null){
            mData.addAll(datas);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ShowHoriAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.first_horiz_layout,parent,false);
        ViewHolder holderHroiz=new ViewHolder(view);
        return holderHroiz;
    }

    @Override
    public void onBindViewHolder(@NonNull ShowHoriAdapter.ViewHolder viewHolder, int position) {
        ViewHolder holderHroiz=viewHolder;
        String image = mData.get(position).getMasterPic().split("\\|")[0].replace("https", "http");
        holderHroiz.textName.setText(mData.get(position).getCommodityName());
        holderHroiz.textPrice.setText("￥"+mData.get(position).getPrice()+"");
        Uri uri = Uri.parse(image);
        holderHroiz.sdvView.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final SimpleDraweeView sdvView;
        private final TextView textName;
        private final TextView textPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            sdvView = itemView.findViewById(R.id.sdv_view);
            textName = itemView.findViewById(R.id.text_name);
            textPrice = itemView.findViewById(R.id.text_price);
        }
    }
}
