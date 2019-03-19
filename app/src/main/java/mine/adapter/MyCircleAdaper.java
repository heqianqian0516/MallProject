package mine.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bwei.mallproject.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mine.bean.MyCircleBean;
import view.CustomMultiImageView;

public class MyCircleAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private List<MyCircleBean.ResultBean> list;
    private boolean falg;
    public MyCircleAdaper(Context mContext) {
        this.mContext = mContext;
        list = new ArrayList<>();
    }

    public void setList(List<MyCircleBean.ResultBean> lists) {
        list.clear();
        if (list != null) {
            list.addAll(lists);
        }
        notifyDataSetChanged();
    }

    public void addList(List<MyCircleBean.ResultBean> lists) {
        list.clear();
        if (list != null) {
            list.addAll(lists);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.my_circle_item_view_image, viewGroup, false);
           ViewHoldeImage holder = new ViewHoldeImage(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
                ViewHoldeImage holdeImage = (ViewHoldeImage) viewHolder;
                holdeImage.circleContent.setText(list.get(i).getContent());
                String[] image = list.get(i).getImage().split("\\,");
               /* List<String> sList = new ArrayList<>();
                for(int a = 0;a<image.length;a++){
                    sList.add(image[a]);
                }*/
               Glide.with(mContext).load(image).into(holdeImage.circleImage01);

              /*  holdeImage.circleImage01.setList(sList);*/
                String date = new SimpleDateFormat("yyyy-MM-dd").format(
                        new Date(list.get(i).getCreateTime()));
                holdeImage.circleTime.setText(date);
                holdeImage.circleNum.setText(list.get(i).getGreatNum()+"");
        //判断是否有点赞
        if (list.get(i).getGreatNum()>=1){
            holdeImage.circlePrise.setBackgroundResource(R.mipmap.common_btn_prise_n_hdpi);
        }else{
            holdeImage.circlePrise.setBackgroundResource(R.mipmap.common_btn_prise_s_hdpi);
        }
        //判断是否显示复选框
        if (falg){
            holdeImage.circleCheck.setVisibility(View.VISIBLE);
            //判断复学框是否选择
            if (list.get(i).isCheck()){
                holdeImage.circleCheck.setChecked(true);
            }else{
                holdeImage.circleCheck.setChecked(false);
            }
        }else{
            holdeImage.circleCheck.setVisibility(View.GONE);
        }
        //选择状态
        holdeImage.circleCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    list.get(i).setCheck(true);
                    // notifyDataSetChanged();

                }
            }
        });
    }
    //存入复选框显示与隐藏的状态值
    public void setCheckbox(boolean falg){
        this.falg=falg;
        notifyDataSetChanged();
    }

    public List<MyCircleBean.ResultBean> getList() {
        return list;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHoldeImage extends RecyclerView.ViewHolder {
        @BindView(R.id.circle_check)
        CheckBox circleCheck;
        @BindView(R.id.circle_content)
        TextView circleContent;
        @BindView(R.id.circle_image01)
        ImageView circleImage01;
        @BindView(R.id.circle_time)
        TextView circleTime;
        @BindView(R.id.circle_num)
        TextView circleNum;
        @BindView(R.id.circle_prise)
        ImageView circlePrise;

        public ViewHoldeImage(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            
        }
    }

}
