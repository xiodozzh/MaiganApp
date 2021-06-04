package com.mgtech.maiganapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.BR;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.fragment.PrivacyRejectDialogFragment;
import com.mgtech.maiganapp.utils.PermissionUtils;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.mgtech.maiganapp.viewmodel.EnterViewModel;
import com.mgtech.maiganapp.widget.GuideDotView;
import com.mgtech.maiganapp.widget.PermissionPrivacySpan;
import com.mgtech.maiganapp.widget.PermissionProtocolSpan;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class EnterActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST = 745;
    @Bind(R.id.ivStart)
    ImageView ivStart;
    private int[] imageSource = new int[]{R.drawable.activity_enter_guide0, R.drawable.activity_enter_guide1,
            R.drawable.activity_enter_guide2, R.drawable.activity_enter_guide3, R.drawable.activity_enter_guide4,
            R.drawable.activity_enter_guide5};
    @Bind({R.id.dot0, R.id.dot1, R.id.dot2, R.id.dot3, R.id.dot4, R.id.dot5})
    GuideDotView[] dotViews;
    private View[] pagers;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.btn_enter)
    View btnEnter;
    @Bind(R.id.tv_protocol)
    TextView tvProtocol;

    protected ViewDataBinding binding;
    protected EnterViewModel viewModel;
    private int permissionBackgroundSelected;
    private int permissionBackgroundUnSelected;
    private ImageView[] ivPermissionChecked;
    private ImageView[] ivPermission;
    @Bind({R.id.layout_permission_location, R.id.layout_permission_store, R.id.layout_permission_photo,
            R.id.layout_permission_contact, R.id.layout_permission_call})
    ViewGroup[] layoutPermissions;
    private boolean[] permissionChecked = {true, true, true, true, true};

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, EnterActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_enter);
        viewModel = ViewModelProviders.of(this).get(EnterViewModel.class);
        binding.setVariable(BR.model, viewModel);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    protected void init() {
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        viewModel.jumpPage.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewModel.isLogin) {
                    if (viewModel.personalInfoComplete) {
                        startActivity(MainActivity.getCallingIntent(EnterActivity.this));
                    } else {
                        startActivity(PersonalInfoInitActivity.getCallingIntent(EnterActivity.this,
                                SaveUtils.getUserId(getApplicationContext())));
                    }
                } else {
                    startActivity(LoginActivity.getCallingIntent(EnterActivity.this, viewModel.multiLogin));
                }
                finish();
            }
        });
        viewModel.showGuide.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewModel.showGuide.get()) {
                    enterGuide();
                }
            }
        });
        viewModel.checkIsNew();
        initProtocol();
        initPermission();
    }

    private void initProtocol() {
        String preString = getString(R.string.enter_protocol_agree)+" ";
        String userString = getString(R.string.activity_register_user_protocol);
        String privacyString = getString(R.string.activity_register_privacy);

        SpannableString ss = new SpannableString(preString + userString + ", " + privacyString);
        int protocolStart = preString.length();
        int protocolEnd = protocolStart + userString.length() + 2;
        int privacyEnd = protocolEnd + privacyString.length();
        ss.setSpan(new PermissionProtocolSpan(this), protocolStart, protocolEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new PermissionPrivacySpan(this), protocolEnd, privacyEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvProtocol.setText(ss);
        tvProtocol.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initPermission() {

        String[] permissionTitles = new String[]{
                getString(R.string.enter_permission_location_title),
                getString(R.string.enter_permission_store_title),
                getString(R.string.enter_permission_camera_title),
                getString(R.string.enter_permission_contact_title),
//                getString(R.string.enter_permission_album_title),
                getString(R.string.enter_permission_call_title)
        };
        String[] permissionContents = new String[]{
                getString(R.string.enter_permission_location_content),
                getString(R.string.enter_permission_store_content),
                getString(R.string.enter_permission_camera_content),
                getString(R.string.enter_permission_contact_content),
//                getString(R.string.enter_permission_album_content),
                getString(R.string.enter_permission_call_content)
        };
        int[] permissionImageSrc = new int[]{
                R.drawable.enter_permission_location,
                R.drawable.enter_permission_store,
                R.drawable.enter_permission_photo,
                R.drawable.enter_permission_contact,
//                R.drawable.enter_permission_album,
                R.drawable.enter_permission_call
        };
        ivPermissionChecked = new ImageView[layoutPermissions.length];
        ivPermission = new ImageView[layoutPermissions.length];
        permissionBackgroundSelected = R.drawable.enter_permission_image_background_selected;
        permissionBackgroundUnSelected = R.drawable.enter_permission_image_background_un_selected;
        for (int i = 0; i < layoutPermissions.length; i++) {
            ivPermissionChecked[i] = layoutPermissions[i].findViewById(R.id.iv_permission_checked);
            ivPermission[i] = layoutPermissions[i].findViewById(R.id.iv_permission);
            ivPermission[i].setImageResource(permissionImageSrc[i]);
            ivPermission[i].setBackgroundResource(permissionBackgroundSelected);
            ((TextView) (layoutPermissions[i].findViewById(R.id.tv_permission_title))).setText(permissionTitles[i]);
            ((TextView) (layoutPermissions[i].findViewById(R.id.tv_permission_content))).setText(permissionContents[i]);
            layoutPermissions[i].setOnClickListener(new OnClickListener(i));
        }
    }

    class OnClickListener implements View.OnClickListener {
        private int index;

        public OnClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View view) {
            if (index == 0) {
                Toast.makeText(EnterActivity.this, R.string.enter_auth_have_to_open, Toast.LENGTH_SHORT).show();
                return;
            }
            permissionChecked[index] = !permissionChecked[index];
            inflatePermission();
        }
    }

    private void inflatePermission() {
        for (int i = 0; i < layoutPermissions.length; i++) {
            ivPermissionChecked[i].setVisibility(permissionChecked[i] ? View.VISIBLE : View.INVISIBLE);
            ivPermission[i].setBackgroundResource(permissionChecked[i] ? permissionBackgroundSelected : permissionBackgroundUnSelected);
        }
    }

    @OnClick(R.id.btn_agree)
    void agreeProtocol() {
        viewModel.agreeSigned();
        SaveUtils.setPermissionAccessStorage(permissionChecked[1]);
        SaveUtils.setPermissionUseCamera(permissionChecked[2]);
        SaveUtils.setPermissionReadContact(permissionChecked[3]);

        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionChecked[1]) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionChecked[2]) {
            permissions.add(Manifest.permission.CAMERA);
        }
        if (permissionChecked[3]) {
            permissions.add(Manifest.permission.READ_CONTACTS);
        }
        SaveUtils.setAllowCustomerContact(permissionChecked[4]);
        PermissionUtils.openPermission(this, permissions, PERMISSION_REQUEST);
    }

    @OnClick(R.id.btn_not_agree)
    void disagreeProtocol() {
        PrivacyRejectDialogFragment fragment = PrivacyRejectDialogFragment.getInstance();
        fragment.setCallback(this::finish);
        fragment.show(getSupportFragmentManager(), "PrivacyRejectDialogFragment");
    }


    @OnClick(R.id.btn_skip)
    void skip() {
        viewModel.finishEnterWaiting();
    }


    private void enterGuide() {
        String[] titles = getResources().getStringArray(R.array.enter_title);
        String[] contents = getResources().getStringArray(R.array.enter_content);

        dotViews[0].select();

        pagers = new View[6];
        LayoutInflater inflater = LayoutInflater.from(this);
        int width = ViewUtils.getScreenWidth() - ViewUtils.dp2px(80);
        int height = width * 367 / 315;
        for (int i = 0; i < pagers.length; i++) {
            pagers[i] = inflater.inflate(R.layout.activity_enter_view_pager_item, null);
            TextView title = pagers[i].findViewById(R.id.tv_title);
            TextView content = pagers[i].findViewById(R.id.tv_content);
            ImageView iv = pagers[i].findViewById(R.id.iv);
            iv.setImageBitmap(ViewUtils.decodeSampleBitmapFromResource(getResources(), imageSource[i], width, height));
            title.setText(titles[i]);
            content.setText(contents[i]);
        }
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(onEnterPageChangeListener);
    }

    @OnClick(R.id.btn_enter)
    void enter() {
        SaveUtils.setDirtyUse(this);
        startActivity(LoginActivity.getCallingIntent(this, false));
        finish();
    }

    private ViewPager.OnPageChangeListener onEnterPageChangeListener = new ViewPager.OnPageChangeListener() {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            viewModel.lastPage.set(position == dotViews.length - 1);
            for (int i = 0; i < dotViews.length; i++) {
                if (i != position) {
                    dotViews[i].disSelect();
                } else {
                    dotViews[i].select();
                }
            }
            btnEnter.setVisibility(position == dotViews.length - 1 ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return pagers.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            if (container instanceof ViewPager) {
                container.addView(pagers[position]);
            }
            return pagers[position];
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            if (container instanceof ViewPager) {
                container.removeView(pagers[position]);
            }
        }
    };

}
