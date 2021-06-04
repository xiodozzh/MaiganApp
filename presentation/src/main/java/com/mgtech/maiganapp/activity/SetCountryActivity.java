package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.LocationModel;
import com.mgtech.maiganapp.fragment.SetCountryFragment;
import com.mgtech.maiganapp.fragment.SetProvinceFragment;
import com.mgtech.maiganapp.viewmodel.SetCountryAndProvinceViewModel;

public class SetCountryActivity extends BaseActivity<SetCountryAndProvinceViewModel>{
    private SetCountryFragment setCountryFragment;
    private SetProvinceFragment setProvinceFragment;
    private boolean selectProvince;

    public static Intent getCallingIntent(Context context){
        return new Intent(context,SetCountryActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        setCountryFragment = new SetCountryFragment();
        setProvinceFragment = new SetProvinceFragment();
        initFragment();
        selectCountry();
    }

    public void initFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container,setCountryFragment);
        transaction.add(R.id.container,setProvinceFragment);
        transaction.hide(setProvinceFragment);
        transaction.hide(setCountryFragment);
        transaction.commit();
    }


    public void selectProvince(LocationModel model){
        selectProvince = true;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.right_in,R.anim.left_out);
        transaction.hide(setCountryFragment);
        transaction.show(setProvinceFragment);
        transaction.commit();
        setProvinceFragment.getProvinces(model);
    }

    public void selectCountry(){
        selectProvince = false;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.left_in,R.anim.right_out);
        transaction.hide(setProvinceFragment);
        transaction.show(setCountryFragment);
        transaction.commit();
    }


    @Override
    protected int getContentViewId() {
        return R.layout.activity_set_country;
    }


    @Override
    public void onBackPressed() {

        if (selectProvince){
            selectCountry();
        }else{
            super.onBackPressed();
        }
    }
}
