package com.mgtech.maiganapp.adapter;

import android.graphics.Canvas;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.mgtech.maiganapp.utils.ViewUtils;

import java.util.List;

/**
 * @author zhaixiang
 */
public class FragmentFriendRecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    private static final int BOTTOM_PADDING = ViewUtils.dp2px(60);

    private List dataList;

    public FragmentFriendRecyclerViewItemDecoration(List dataList) {
        this.dataList = dataList;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }


    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        if (dataList != null) {
//            if (parent.getChildLayoutPosition(view) == dataList.size() - 1) {
//                outRect.bottom = BOTTOM_PADDING;
//            }
//        }
    }
}
