package view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bwei.mallproject.R;

public class MyAddSubView extends LinearLayout  {
    private int number = 1;
    private TextView sub_tv;
    private TextView product_number_tv;
    private TextView add_tv;

    public MyAddSubView(Context context) {
        this(context, null);
    }

    public MyAddSubView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public MyAddSubView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = inflate(context, R.layout.add_remove_view_layout, this);

        sub_tv = view.findViewById(R.id.sub_tv);
        product_number_tv = view.findViewById(R.id.product_number_tv);
        add_tv = view.findViewById(R.id.add_tv);

        sub_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number>1){
                    --number;
                    product_number_tv.setText(number+"");
                    if (onNumberChangeListener !=null){
                        onNumberChangeListener.onNumberChange(number);
                    }
                }else{
                    Toast.makeText(getContext(),"不能再少了",Toast.LENGTH_SHORT).show();
                }
            }
        });

      add_tv.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
              ++number;
              product_number_tv.setText(number+"");
              if (onNumberChangeListener !=null){
                  onNumberChangeListener.onNumberChange(number);
              }
          }
      });

    }

  /*  @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sub_tv:
                if (number>1){
                    --number;
                    product_number_tv.setText(number+"");
                    if (onNumberChangeListener !=null){
                        onNumberChangeListener.onNumberChange(number);
                    }
                }else{
                    Toast.makeText(getContext(),"不能再少了",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.add_tv:
                ++number;
                product_number_tv.setText(number+"");
                if (onNumberChangeListener !=null){
                    onNumberChangeListener.onNumberChange(number);
                }
                break;
        }

    }*/
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
        product_number_tv.setText(number + "");
    }

    OnNumberChangeListener onNumberChangeListener;

    public void setOnNumberChangeListener(OnNumberChangeListener onNumberChangeListener) {
        this.onNumberChangeListener = onNumberChangeListener;
    }

    public interface OnNumberChangeListener {
        void onNumberChange(int num);
    }

}
