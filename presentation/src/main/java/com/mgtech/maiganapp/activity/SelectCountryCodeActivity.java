package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mgtech.domain.entity.CountryCode;
import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.DefaultViewModel;
import com.mgtech.maiganapp.widget.SideBarView;
import com.mgtech.maiganapp.widget.WrapContentLinearLayoutManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * @author zhaixiang
 * 选择国家
 */

public class SelectCountryCodeActivity extends BaseActivity<DefaultViewModel> {
    public static final int REQUEST = 12222;
    public static final String CODE = "code";
    public static final String NAME = "name";
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.side_bar)
    SideBarView sideBarView;
    @Bind(R.id.letter)
    TextView tvLetter;
    private RecyclerAdapter adapter;
    private boolean move;
    private int mIndex;
    private LinearLayoutManager mLinearLayoutManager;
    private List<Object> data = new ArrayList<>();
    private static Handler mHandler = new Handler();


    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SelectCountryCodeActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionbar();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Observable.create(new Observable.OnSubscribe<List<Object>>() {
            @Override
            public void call(Subscriber<? super List<Object>> subscriber) {
                final CountryCode countryCode = getCountryCode();
                subscriber.onNext(countryCode.toList());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(SelectCountryCodeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<Object> list) {
                        data.clear();
                        data.addAll(list);
                        adapter.notifyDataSetChanged();
                    }
                });

        sideBarView.setOnTouchingLetterChangedListener(new SideBarView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                mIndex = adapter.getPosition(s);
                moveToPosition(mIndex);
                tvLetter.setVisibility(View.VISIBLE);
                tvLetter.setText(s);
                mHandler.removeCallbacks(hideLetterViewRunnable);
                mHandler.postDelayed(hideLetterViewRunnable, 1000);
            }
        });
        initView();
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }

    private Runnable hideLetterViewRunnable = new Runnable() {
        @Override
        public void run() {
            tvLetter.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(hideLetterViewRunnable);
        super.onDestroy();
    }

    private void initView() {
        adapter = new RecyclerAdapter(data, recyclerView);
        recyclerView.setAdapter(adapter);
        mLinearLayoutManager = new WrapContentLinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerViewListener());
    }

    private CountryCode getCountryCode() {
        StringBuilder result = new StringBuilder();
        try {
            boolean isChinese = Utils.isLanguageChinese();

            int source = isChinese ? R.raw.country_code_ch : R.raw.country_code_en;
            InputStreamReader inputReader = new InputStreamReader(getResources().openRawResource(source));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            while ((line = bufReader.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        CountryCode countryCode = new CountryCode();
        countryCode.parse(result.toString());
        return countryCode;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_select_country_code;
    }


    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Object> data;
        private RecyclerView recyclerView;
        private static final int TITLE = 1;
        private static final int ENTITY = 2;

        public RecyclerAdapter(List<Object> data, RecyclerView recyclerView) {
            this.data = data;
            this.recyclerView = recyclerView;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == ENTITY) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_select_zone_fragment_item,
                        parent, false);
                CountryViewHolder viewHolder = new CountryViewHolder(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = recyclerView.getChildLayoutPosition(v);
                        CountryCode.Entity entity = (CountryCode.Entity) data.get(index);
                        Intent intent = new Intent();
                        intent.putExtra(CODE, entity.getCode());
                        intent.putExtra(NAME, entity.getName());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                return viewHolder;
            } else if (viewType == TITLE) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_select_zone_fragment_item_title,
                        parent, false);
                return new TitleViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof CountryViewHolder) {
                CountryViewHolder viewHolder = (CountryViewHolder) holder;
                CountryCode.Entity entity = (CountryCode.Entity) data.get(position);
                viewHolder.tvCountry.setText(entity.getName());
                viewHolder.tvZone.setText("+" + entity.getCode());
            } else if (holder instanceof TitleViewHolder) {
                TitleViewHolder viewHolder = (TitleViewHolder) holder;
                CountryCode.Title title = (CountryCode.Title) data.get(position);
                viewHolder.tvTitle.setText(title.getTitle());
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public int getPosition(String letter) {
            for (int i = 0; i < data.size(); i++) {
                Object obj = data.get(i);
                if (obj instanceof CountryCode.Title) {
                    CountryCode.Title title = (CountryCode.Title) obj;
                    if (letter.toUpperCase().charAt(0) <= title.getTitle().toUpperCase().charAt(0)) {
                        return i;
                    }
                }
            }
            return -1;
        }

        @Override
        public int getItemViewType(int position) {
            if (data.get(position) instanceof CountryCode.Title) {
                return TITLE;
            } else {
                return ENTITY;
            }
        }

        private class CountryViewHolder extends RecyclerView.ViewHolder {
            private TextView tvZone;
            private TextView tvCountry;

            CountryViewHolder(View itemView) {
                super(itemView);
                tvZone = itemView.findViewById(R.id.tv_zone);
                tvCountry = itemView.findViewById(R.id.tv_country);
            }
        }

        private class TitleViewHolder extends RecyclerView.ViewHolder {
            private TextView tvTitle;

            TitleViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_title);
            }
        }
    }


    class RecyclerViewListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //在这里进行第二次滚动（最后的100米！）
            if (move) {
                move = false;
                //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                int n = mIndex - mLinearLayoutManager.findFirstVisibleItemPosition();
                if (0 <= n && n < recyclerView.getChildCount()) {
                    //获取要置顶的项顶部离RecyclerView顶部的距离
                    int top = recyclerView.getChildAt(n).getTop();
                    //最后的移动
                    recyclerView.scrollBy(0, top);
                }
            }
        }
    }

    private void moveToPosition(int n) {
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            recyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = recyclerView.getChildAt(n - firstItem).getTop();
            recyclerView.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            recyclerView.scrollToPosition(n);
            //这里这个变量是用在RecyclerView滚动监听里面的
            move = true;
        }

    }
}
