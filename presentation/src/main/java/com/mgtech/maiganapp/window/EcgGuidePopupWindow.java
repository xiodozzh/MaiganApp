package com.mgtech.maiganapp.window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.widget.GuideDotView;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


/**
 * 选择图片来源
 */

public class EcgGuidePopupWindow extends PopupWindow {

    public EcgGuidePopupWindow(final Context context) {
        super(context);
        List<View> viewList = new ArrayList<>();
        View view = LayoutInflater.from(context).inflate(R.layout.popup_window_ecg_guide, null);
        final ViewPager viewPager = view.findViewById(R.id.viewPager);
        final View left = view.findViewById(R.id.iv_left);
        final View right = view.findViewById(R.id.iv_right);
        final View tvNotShow = view.findViewById(R.id.tv_not_show);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
                viewPager.arrowScroll(View.FOCUS_LEFT);

            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                viewPager.arrowScroll(View.FOCUS_RIGHT);
            }
        });
        tvNotShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveUtils.guideMeasureEcgWatched();
                dismiss();
            }
        });
        final int[] imgRes = new int[]{
                R.drawable.measure_ecg_guide0,
                R.drawable.measure_ecg_guide1,
                R.drawable.measure_ecg_guide2
        };
        final GuideDotView[] dotsView = new GuideDotView[3];
        dotsView[0] = view.findViewById(R.id.dot0);
        dotsView[1] = view.findViewById(R.id.dot1);
        dotsView[2] = view.findViewById(R.id.dot2);
        dotsView[0].select();
        final String[] contents = new String[]{
                context.getString(R.string.guide_measure_ecg_content0),
                context.getString(R.string.guide_measure_ecg_content1),
                context.getString(R.string.guide_measure_ecg_content2)
        };

        view.findViewById(R.id.iv_close).setOnClickListener(dismissListener);
        for (int i = 0; i < 3; i++) {
            View page = LayoutInflater.from(context)
                    .inflate(R.layout.popup_window_ecg_guide_pager_view, null, false);
            ImageView iv = page.findViewById(R.id.iv);
            iv.setImageResource(imgRes[i]);
            TextView tv = page.findViewById(R.id.tv_content);
            tv.setText(contents[i]);
            viewList.add(page);
        }

        PagerAdapter adapter = new Adapter(viewList);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < 3; i++) {
                    if (position == i) {
                        dotsView[i].select();
                    } else {
                        dotsView[i].disSelect();
                    }
                }
                if (position == 0){
                    left.setVisibility(View.GONE);
                }else{
                    left.setVisibility(View.VISIBLE);
                }
                if (position == 2){
                    right.setVisibility(View.GONE);
                    tvNotShow.setVisibility(View.VISIBLE);
                }else{
                    right.setVisibility(View.VISIBLE);
                    tvNotShow.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);

        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //实例化一个ColorDrawable颜色为半透明
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popup_window);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private View.OnClickListener dismissListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
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
