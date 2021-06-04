package com.mgtech.maiganapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.ServiceCompanyModel;

import java.util.Objects;

/**
 * Created by zhaixiang on 2017/8/11.
 * 删除
 */
public class ServiceCompanySelectFragment extends AppCompatDialogFragment {
    private Callback callback;

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    public interface Callback{
        void select(int index);
    }

    public static ServiceCompanySelectFragment getInstance() {
        return new ServiceCompanySelectFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.fragment_service_company_select, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        setCancelable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);
        return builder.create();
    }

    private static class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>{
        @NonNull
        @Override
        public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        static class CompanyViewHolder extends RecyclerView.ViewHolder{

            public CompanyViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}
