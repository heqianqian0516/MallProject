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
import java.util.ArrayList;
import java.util.List;

import bean.FirstCategoryBean;


public class FirstCategoryAdapter  extends RecyclerView.Adapter<FirstCategoryAdapter.ViewHolder> {
    private List<FirstCategoryBean.ResultBean> mData;
    private Context context;
    public FirstCategoryAdapter(Context context){
        this.context=context;
        mData=new ArrayList<>();
    }
    public void setmData(List<FirstCategoryBean.ResultBean> datas) {
       mData.clear();
        if (datas!=null){
            mData.addAll(datas);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public FirstCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.first_category_layout,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FirstCategoryAdapter.ViewHolder holder, final int position) {
         ViewHolder holder1=holder;
         holder1.topView.setText(mData.get(position).getName());
         holder1.itemView.setOnClickListener(new View.OnClickListener() {
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

        private final TextView topView;

        public ViewHolder(View itemView) {
            super(itemView);
            topView = itemView.findViewById(R.id.top_text);
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

