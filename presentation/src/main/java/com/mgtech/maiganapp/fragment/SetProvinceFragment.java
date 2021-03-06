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

public class SetProvinceFragment extends BaseFragment<SetCountryAndProvinceViewModel>{
    private SetCountryActivity activity;
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
    private int mIndex;
    private static Handler mHandler = new Handler();
    private LinearLayoutManager mLinearLayoutManager;
    private String guid;
    private LocationModel countryModel;

    @Override
    protected void init(Bundle savedInstanceState) {
        initObs();
        layoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getProvinces(guid);
            }
        });
        layoutRefreshError.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getProvinces(guid);
            }
        });
        adapter = new ActivitySetLocationAdapter(recyclerView, new ActivitySetLocationAdapter.SelectListener() {
            @Override
            public void onItemSelect(int index) {
                if (getActivity() != null){
                    LocationModel model = (LocationModel) viewModel.locationModels.get(index);
                    Intent intent = new Intent();
                    if (countryModel != null) {
                        intent.putExtra(PersonalInfoActivity.COUNTRY_ID, countryModel.id);
                        intent.putExtra(PersonalInfoActivity.PROVINCE_ID, model.id);
                        intent.putExtra(PersonalInfoActivity.LOCATION,countryModel.name + " " + model.name);
                    }
                    getActivity().setResult(Activity.RESULT_OK,intent);
                    getActivity().finish();
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
        recyclerView.addOnScrollListener(new RecyclerViewListener());
        recyclerView.setLayoutManager(mLinearLayoutManager);
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

    public void getProvinces(LocationModel model){
        this.guid = model.id;
        viewModel.getProvinces(guid);
        countryModel = model;
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

    @OnClick(R.id.btn_back)
    void back(){
        if (activity != null){
            activity.selectCountry();
        }
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
