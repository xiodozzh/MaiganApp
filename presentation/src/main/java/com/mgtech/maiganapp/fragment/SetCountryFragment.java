package com.mgtech.maiganapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.Observable;
import android.os.Bundle;
import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.activity.PersonalInfoActivity;
import com.mgtech.maiganapp.activity.SetCountryActivity;
import com.mgtech.maiganapp.adapter.ActivitySetLocationAdapter;
import com.mgtech.maiganapp.data.model.LocationModel;
import com.mgtech.maiganapp.viewmodel.SetCountryAndProvinceViewModel;
import com.mgtech.maiganapp.widget.SideBarView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class SetCountryFragment extends BaseFragment<SetCountryAndProvinceViewModel>{
    @Bind(R.id.layout_refresh_error)
    SwipeRefreshLayout layoutRefreshError;
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.side_bar)
    SideBarView sideBarView;
    @Bind(R.id.letter)
    TextView tvLetter;
    private boolean move;
    private ActivitySetLocationAdapter adapter;
    private SetCountryActivity activity;
    private int mIndex;
    private static Handler mHandler = new Handler();
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void init(Bundle savedInstanceState) {
        initObs();
        layoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getCountries();
            }
        });
        layoutRefreshError.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getCountries();
            }
        });
        adapter = new ActivitySetLocationAdapter(recyclerView, new ActivitySetLocationAdapter.SelectListener() {
            @Override
            public void onItemSelect(int index) {
                LocationModel model = (LocationModel) viewModel.locationModels.get(index);
                if (model.hasCities) {
                    if (activity != null){
                        activity.selectProvince(model);
                    }
                }else{
                    if (activity != null){
                        Intent intent = new Intent();
                        intent.putExtra(PersonalInfoActivity.COUNTRY_ID,model.id);
                        intent.putExtra(PersonalInfoActivity.LOCATION,model.name);
                        activity.setResult(Activity.RESULT_OK,intent);
                        activity.finish();
                    }
                }
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
        recyclerView.setAdapter(adapter);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerViewListener());
        viewModel.getCountries();
    }

    private Runnable hideLetterViewRunnable = new Runnable() {
        @Override
        public void run() {
            tvLetter.setVisibility(View.GONE);
        }
    };

    private void moveToPosition(int n) {
        //??????RecyclerView???LayoutManager????????????????????????????????????Position
        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        //??????????????????
        if (n <= firstItem) {
            //????????????????????????????????????????????????????????????
            recyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            //?????????????????????????????????????????????
            int top = recyclerView.getChildAt(n - firstItem).getTop();
            recyclerView.scrollBy(0, top);
        } else {
            //????????????????????????????????????????????????????????????
            recyclerView.scrollToPosition(n);
            //???????????????????????????RecyclerView?????????????????????
            move = true;
        }

    }
    private void initObs(){
        viewModel.loading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                layoutRefresh.setRefreshing(viewModel.loading.get());
                layoutRefreshError.setRefreshing(viewModel.loading.get());
            }
        });
        viewModel.reloadData.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                adapter.setData(viewModel.locationModels);
                sideBarView.setLetters(viewModel.letters);
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_set_country;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SetCountryActivity) {
            activity = (SetCountryActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @OnClick(R.id.btn_back)
    void back(){
        if (activity!= null) {
            activity.finish();
        }
    }

    class RecyclerViewListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //??????????????????????????????????????????100?????????
            if (move) {
                move = false;
                int n = mIndex - mLinearLayoutManager.findFirstVisibleItemPosition();
                if (0 <= n && n < recyclerView.getChildCount()) {
                    //??????????????????????????????RecyclerView???????????????
                    int top = recyclerView.getChildAt(n).getTop();
                    //???????????????
                    recyclerView.scrollBy(0, top);
                }
            }
        }
    }
}
