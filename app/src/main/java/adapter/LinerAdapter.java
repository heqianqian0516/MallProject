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

public class LinerAdapter  extends RecyclerView.Adapter<LinerAdapter.ViewHolder>{
    private List<ShowBean.ResultBean.MlssBean.CommodityListBeanXX> mData;
    private Context mContext;

    public LinerAdapter(Context mContext) {
        this.mContext = mContext;
        mData = new ArrayList<>();
    }
    public void setmData(List<ShowBean.ResultBean.MlssBean.CommodityListBeanXX> datas){
        mData.clear();
        if (datas !=null){
            mData.addAll(datas);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LinerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.first_liner_layout,parent,false);
        LinerAdapter.ViewHolder holderLiner=new LinerAdapter.ViewHolder(view);

        return holderLiner;
    }

    @Override
    public void onBindViewHolder(@NonNull LinerAdapter.ViewHolder viewHolder, int position) {
        LinerAdapter.ViewHolder holderLiner=viewHolder;
        String image = mData.get(position).getMasterPic().split("\\|")[0].replace("https", "http");
        holderLiner.textName.setText(mData.get(position).getCommodityName());
        holderLiner.textPrice.setText("￥"+mData.get(position).getPrice()+"");
        Uri uri = Uri.parse(image);
        holderLiner.sdvView.setImageURI(uri);
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
            sdvView = itemView.findViewById(R.id.sdv_img);
            textName = itemView.findViewById(R.id.text_name1);
            textPrice = itemView.findViewById(R.id.text_price1);
        }
    }
}
