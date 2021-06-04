package com.mgtech.maiganapp.adapter;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author zhaixiang
 */
public class FragmentFriendViewPagerAdapter extends PagerAdapter {
    private List<View> pagers;

    public FragmentFriendViewPagerAdapter(List<View> pagers) {
        this.pagers = pagers;
    }

    @Override
    public int getCount() {
        return pagers.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
         container.addView(pagers.get(position),0);
         return pagers.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(pagers.remove(position));
    }


}
