package adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwei.mallproject.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import bean.CircleBean;

public class CircleAdapter extends RecyclerView.Adapter<CircleAdapter.ViewHolder> {

   private List<CircleBean.ResultBean> list;
   private Context context;
   public CircleAdapter(Context context){
       this.context=context;
       list=new ArrayList<>();
   }
    public void setmData(List<CircleBean.ResultBean> datas){
        list.addAll(datas);
        notifyDataSetChanged();
    }
    public void addmResult(List<CircleBean.ResultBean> datas) {
        if (datas != null) {
            list.addAll(datas);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CircleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.circle_layout,parent,false);
        ViewHolder holder=new ViewHolder(view);
         return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CircleAdapter.ViewHolder viewHolder, final int position) {
          ViewHolder holder=viewHolder;
          holder.textName.setText(list.get(position).getNickName());
          holder.textTitle.setText(list.get(position).getContent());
          holder.times.setText(list.get(position).getCreateTime()+"");
          Uri uri=Uri.parse(list.get(position).getHeadPic());
          holder.sdv.setImageURI(uri);
          String image = list.get(position).getImage().split("\\|")[0].replace("https", "http");
          Glide.with(context).load(image).into(holder.mImg1);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                new java.util.Date(list.get(position).getCreateTime()));
        holder.times.setText(date);
          //设置点赞
        holder.textNum.setText(list.get(position).getGreatNum() + "");
        if (list.get(position).getWhetherGreat()==1){
            holder.imageNum.setImageResource(R.mipmap.common_btn_prise_s_hdpi);
        }else{
            holder.imageNum.setImageResource(R.mipmap.common_btn_prise_n_hdpi);
        }
        holder.imageNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCallBack != null) {
                    clickCallBack.callBack(list.get(position).getWhetherGreat(), position, list.get(position).getId());
                }
            }
        });
    }
    //点赞方法
    public void getGivePraise(int position){
        list.get(position).setWhetherGreat(1);
        list.get(position).setGreatNum(list.get(position).getGreatNum() + 1);
        notifyDataSetChanged();
    }
    //取消点赞
    public void getCancelPraise(int position){
        list.get(position).setWhetherGreat(2);
        list.get(position).setGreatNum(list.get(position).getGreatNum() - 1);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final SimpleDraweeView sdv;
        private final TextView textName;
        private final TextView textTitle;
        private final ImageView mImg1;
        private final TextView times;
        private final TextView textNum;
        private final ImageView imageNum;

        public ViewHolder(View itemView) {
            super(itemView);
            sdv = itemView.findViewById(R.id.sdv);
            textName = itemView.findViewById(R.id.textName);
            textTitle = itemView.findViewById(R.id.textTitle);
            mImg1 = itemView.findViewById(R.id.img);
            times = itemView.findViewById(R.id.times);
            textNum = itemView.findViewById(R.id.text_num);
            imageNum = itemView.findViewById(R.id.img_num);
        }
    }
    //定义接口
    private ClickCallBack clickCallBack;

    public void setClickCallBack(ClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public interface ClickCallBack {
        void callBack(int i, int position, int id);
    }
}
