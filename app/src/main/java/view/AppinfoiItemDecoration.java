package view;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 * @author Peng
 * @time 2019/1/3 14:05
 */

public class AppinfoiItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //不是第一个的格子都设一个左边和底部的间距
        int pos = parent.getChildAdapterPosition(view);
        outRect.top = 30;
        outRect.left=24;
        if (pos != 0) {

            if (pos % 2 == 0) {  //下面一行
                outRect.left=20;
                outRect.right = 20;
            } else { //上面一行

                outRect.right = 20;
            }

        }
        
    }
}
