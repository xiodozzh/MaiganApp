package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mgtech.maiganapp.R;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class EcgGuideLayout extends RelativeLayout {
    private List<View> viewList = new ArrayList<>();
    private static final float MIN_SCALE = 1f;
    private static final float MIN_ALPHA = 1f;


    public EcgGuideLayout(Context context) {
        this(context,null);
    }

    public EcgGuideLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EcgGuideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_ecg_guide, this);

        final ViewPager viewPager = findViewById(R.id.viewPager);
        final int[] imgRes = new int[]{
                R.drawable.measure_ecg_guide0,
                R.drawable.measure_ecg_guide1,
                R.drawable.measure_ecg_guide2

        };
        for (int i = 0; i < 3; i++) {
            View page = LayoutInflater.from(context)
                    .inflate(R.layout.layout_ecg_guide_pager_view, null, false);
            page.findViewById(R.id.iv_close).setOnClickListener(dismissListener);
            ImageView iv = page.findViewById(R.id.iv);
            iv.setImageResource(imgRes[i]);
            viewList.add(page);
        }
        setElevation(100);
        PagerAdapter adapter = new Adapter(viewList);
//        viewPager.setPageTransformer(true, transformer);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);

    }

    private ViewPager.PageTransformer transformer = new ViewPager.PageTransformer() {
        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) {
                view.setAlpha(0);
            } else if (position <= 1) {
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }
                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                        / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    };

    private View.OnClickListener dismissListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setVisibility(View.GONE);
        }
    };

    private class Adapter extends PagerAdapter {
        private List<View> list;

        public Adapter(List<View> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = list.get(position);
            container.addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View item = (View) object;
            container.removeView(item);
        }
    }

}
