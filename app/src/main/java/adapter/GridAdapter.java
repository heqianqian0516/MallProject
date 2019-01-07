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

public class GridAdapter  extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private List<ShowBean.ResultBean.PzshBean.CommodityListBeanX> mData;
    private Context mContext;

    public GridAdapter(Context mContext) {
        this.mContext = mContext;
        mData = new ArrayList<>();
    }
    public void setmData(List<ShowBean.ResultBean.PzshBean.CommodityListBeanX> datas){
        mData.clear();
        if (datas !=null){
            mData.addAll(datas);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public GridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.first_grid_layout,parent,false);
        GridAdapter.ViewHolder holderGrid=new GridAdapter.ViewHolder(view);

        return holderGrid;
    }

    @Override
    public void onBindViewHolder(@NonNull GridAdapter.ViewHolder viewHolder, int position) {
        GridAdapter.ViewHolder holderGrid=viewHolder;
        String image = mData.get(position).getMasterPic().split("\\|")[0].replace("https", "http");
        holderGrid.textName.setText(mData.get(position).getCommodityName());
        holderGrid.textPrice.setText("￥"+mData.get(position).getPrice()+"");
        Uri uri = Uri.parse(image);
        holderGrid.sdvView.setImageURI(uri);

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
            sdvView = itemView.findViewById(R.id.sdv_image2);
            textName = itemView.findViewById(R.id.text_name2);
            textPrice = itemView.findViewById(R.id.text_price2);
        }
    }
}
