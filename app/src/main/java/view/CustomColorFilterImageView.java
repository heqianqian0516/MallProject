package view;

import android.annotation.SuppressLint;
import android.content.Context;

import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class CustomColorFilterImageView extends ImageView implements View.OnTouchListener {
    public CustomColorFilterImageView(Context context) {
        super(context);
    }

    public CustomColorFilterImageView(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomColorFilterImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            // 按下时图像变灰
            case MotionEvent.ACTION_DOWN:
                setColorFilter(Color.GRAY, Mode.MULTIPLY);
                break;
            // 手指离开或取消操作时恢复原色
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setColorFilter(Color.TRANSPARENT);
                break;
            default:
                break;
        }
        return false;
    }
}
