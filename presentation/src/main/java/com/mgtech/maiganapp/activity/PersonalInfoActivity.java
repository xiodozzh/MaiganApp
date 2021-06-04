package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.Observable;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.ActivityPersonalInfoAdapter;
import com.mgtech.maiganapp.data.event.LoginOtherEvent;
import com.mgtech.maiganapp.data.model.IPersonalInfoModel;
import com.mgtech.maiganapp.utils.LoginUtils;
import com.mgtech.maiganapp.utils.PermissionUtils;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.mgtech.maiganapp.viewmodel.PersonalInfoViewModel;
import com.mgtech.maiganapp.window.SelectPicturePopupWindow;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class PersonalInfoActivity extends BaseActivity<PersonalInfoViewModel> {
    public static final String COUNTRY_ID = "countryId";
    public static final String PROVINCE_ID = "provinceId";
    public static final String LOCATION = "location";

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.root)
    View root;
    private ActivityPersonalInfoAdapter adapter;
    private AlertDialog goToOpenStoragePermissionDialog;
    private AlertDialog goToOpenCameraPermissionDialog;

    private static final int GET_COUNTRY = 371;
    private static final int CHOOSE_PIC_REQUEST = 787;
    private static final int CROP_PIC_REQUEST = 422;
    private static final int CHOOSE_CAMERA_REQUEST = 520;
    private SelectPicturePopupWindow popupWindow;
    private Calendar rangeCalendarStart = Calendar.getInstance();
    private Calendar rangeCalendarEnd = Calendar.getInstance();
    private Uri selectedUri = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "temp" + "/" + "small.jpg");


    public static Intent getCallingIntent(Context context) {
        return new Intent(context, PersonalInfoActivity.class);
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        EventBus.getDefault().register(this);
        adapter = new ActivityPersonalInfoAdapter(recyclerView, new ActivityPersonalInfoAdapter.Listener() {
            @Override
            public void onTypeClick(int type) {
                switch (type) {
                    case IPersonalInfoModel.TYPE_AVATAR:
                        selectAvatar();
                        break;
                    case IPersonalInfoModel.TYPE_NAME:
                        editName();
                        break;
                    case IPersonalInfoModel.TYPE_BIRTH:
                        editBirth();
                        break;
                    case IPersonalInfoModel.TYPE_HEIGHT:
                        editHeight();
                        break;
                    case IPersonalInfoModel.TYPE_WEIGHT:
                        editWeight();
                        break;
                    case IPersonalInfoModel.TYPE_PHONE:
//                        startActivity(PhoneUnBindActivity.getCallingIntent(PersonalInfoActivity.this));
                        break;
                    case IPersonalInfoModel.TYPE_PASSWORD:
                        changePassword();
                        break;
                    case IPersonalInfoModel.TYPE_SEX:
                        editSex();
                        break;
                    case IPersonalInfoModel.TYPE_WX:
                        bindWx();
                        break;
                    case IPersonalInfoModel.TYPE_QQ:
                        bindQQ();
                        break;
                    case IPersonalInfoModel.TYPE_LOCATION:
                        startActivityForResult(SetCountryActivity.getCallingIntent(PersonalInfoActivity.this), GET_COUNTRY);
                        break;
                    default:
                }
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getInfo();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int index = parent.getChildLayoutPosition(view);
                IPersonalInfoModel model = viewModel.list.get(index);
                outRect.top = model.getMarginTop();
                if (index == viewModel.list.size() - 1) {
                    outRect.bottom = ViewUtils.dp2px(24);
                }
            }
        });
        rangeCalendarStart.add(Calendar.YEAR, -120);
        initObs();
        viewModel.getInfo();
    }

    private void selectAvatar() {
        popupWindow = new SelectPicturePopupWindow(this, new SelectPicturePopupWindow.Callback() {

            @Override
            public void selectFromPic() {
                if (PermissionUtils.isStoragePermissionOpen(PersonalInfoActivity.this)) {
                    goToPicture();
                } else {
                    PermissionUtils.requestStoragePermission(PersonalInfoActivity.this, REQUEST_EXTERNAL_STORAGE);
                }
            }

            @Override
            public void selectFromCamera() {
                goToCamera();
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(root, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.transparent)));
        backgroundAlpha(0.6f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1);
            }
        });
    }

    /**
     * 去图片库
     */
    private void goToPicture() {
        Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picture, CHOOSE_PIC_REQUEST);
    }

    /**
     * 去相机
     */
    private void goToCamera() {
        if (PermissionUtils.isCameraPermissionOpen(this)) {
            takePhoto();
        } else {
            PermissionUtils.requestCameraPermission(this, REQUEST_CAMERA);
        }
    }

    private void takePhoto() {
        try {
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            // 获取文件
            File file = viewModel.getTempleFile();
            //拍照后原图回存入此路径下
            if (file.exists()) {
                file.delete();
            }
            Uri uri;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                uri = Uri.fromFile(file);
            } else {
                uri = FileProvider.getUriForFile(this, "com.mgtech.maiganapp.fileprovider", file);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, CHOOSE_CAMERA_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(PersonalInfoActivity.this, getString(R.string.open_camera_fail), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initObs() {
        viewModel.getModelListSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                adapter.setDataList(viewModel.list);
            }
        });
        viewModel.loading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                refreshLayout.setRefreshing(viewModel.loading.get());
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_personal_info;
    }

    @OnClick(R.id.btn_back)
    void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            return;
        }
        super.onBackPressed();
    }

    private void editName() {
        ViewUtils.showEditDialog(this, getString(R.string.edit_name), viewModel.model.userName, getString(R.string.personal_info_name), "",
                getString(R.string.please_input_name), InputType.TYPE_CLASS_TEXT, new ViewUtils.EditCallback() {
                    @Override
                    public void onEdit(String result) {
                        viewModel.setName(result);
                    }
                });
    }

    private void editSex() {
        final String[] sex = getResources().getStringArray(R.array.sex);
        int index = viewModel.model.gender - 1;
        if (index < 0) {
            index = 0;
        }
        ViewUtils.showSingleSelectDialog(this, getString(R.string.personal_info_sex), index, sex, new ViewUtils.SelectCallback() {
            @Override
            public void onSelect(float result) {
                int sex = (int) result + 1;
                viewModel.setGender(sex);
            }
        });
    }

    private void editBirth() {
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                viewModel.setBirthday(date);
            }
        })
                .setDate(viewModel.model.birthday)
                .setCancelColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setSubmitColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setRangDate(rangeCalendarStart, rangeCalendarEnd)
                .setSubmitText(getString(R.string.submit))
                .setCancelText(getString(R.string.cancel))
                .build();
        pvTime.show();
    }

    private void editHeight() {
        ViewUtils.showSelectHeightDialog(this, getString(R.string.submit), getString(R.string.cancel),
                Math.round(viewModel.model.height), new ViewUtils.SelectCallback() {
                    @Override
                    public void onSelect(float result) {
                        viewModel.setHeight(result);
                    }
                });
    }

    private void editWeight() {
        ViewUtils.showSelectWeightDialog(this, getString(R.string.submit), getString(R.string.cancel),
                viewModel.model.weight, new ViewUtils.SelectCallback() {
                    @Override
                    public void onSelect(float result) {
                        viewModel.setWeight(result);
                    }
                });
    }

    private void bindWx() {
        IWXAPI api = ((MyApplication) getApplication()).getWXApi();
        if (!api.isWXAppInstalled()) {
            showShortToast(getString(R.string.not_install_wechat));
            return;
        }
        LoginUtils.loginWeChat();

    }

    private void bindQQ() {
        LoginUtils.loginQQ();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginOtherEvent event) {
        if (event.type == NetConstant.QQ_LOGIN) {
            if (event.status == LoginOtherEvent.FAIL) {
                showShortToast(getString(R.string.qq_bind_fail));
            } else if (event.status == LoginOtherEvent.SUCCESS) {
                viewModel.bindQQ(event.openId, event.unionId, event.token, event.name, event.avatarUrl);
            }
        } else if (event.type == NetConstant.WE_CHAT_LOGIN) {
            if (event.status == LoginOtherEvent.FAIL) {
                showShortToast(getString(R.string.wechat_bind_fail));
            } else if (event.status == LoginOtherEvent.SUCCESS) {
                viewModel.bindWeChat(event.openId, event.unionId, event.token, event.name, event.avatarUrl);
            }
        }
    }


    private void changePassword() {
        Intent intent = ResetPasswordActivity.getCallingIntent(this);
        intent.putExtra(ResetPasswordActivity.PHONE, viewModel.phone);
        intent.putExtra(ResetPasswordActivity.ZONE, viewModel.zone);
        intent.putExtra(ResetPasswordActivity.EDITABLE, false);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_COUNTRY) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String countryId = data.getStringExtra(COUNTRY_ID);
                    String provinceId = data.getStringExtra(PROVINCE_ID);
                    String location = data.getStringExtra(LOCATION);
                    viewModel.setLocation(countryId, provinceId, location);
                }
            }
        } else if (requestCode == CHOOSE_PIC_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                cropImageUri(data.getData(), ViewUtils.dp2px(60), ViewUtils.dp2px(60));
            } else {
                showShortToast(getString(R.string.upload_image_cancel));
            }
        } else if (requestCode == CROP_PIC_REQUEST) {
            if (resultCode == RESULT_OK && selectedUri != null) {
                viewModel.compressAndUpload(selectedUri);
            } else {
                showShortToast(getString(R.string.upload_image_fail));
            }
        } else if (requestCode == CHOOSE_CAMERA_REQUEST) {
            Logger.i("CHOOSE_CAMERA_REQUEST" + resultCode + data);
            if (resultCode == RESULT_OK) {
                viewModel.compressAndUpload();
            } else {
                showShortToast(getString(R.string.upload_image_cancel));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE && grantResults.length >= 1) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                showGoToOpenStoragePermissionDialog();
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                goToPicture();
            }
        }
        if (requestCode == REQUEST_CAMERA && grantResults.length >= 1) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                showGoToOpenCameraPermissionDialog();
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            }
        }
    }


    private void cropImageUri(Uri uri, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 100);
        intent.putExtra("aspectY", 99);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, CROP_PIC_REQUEST);
    }

    private void showGoToOpenStoragePermissionDialog() {
        if (goToOpenStoragePermissionDialog != null && goToOpenStoragePermissionDialog.isShowing()) {
            return;
        }
        goToOpenStoragePermissionDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.personal_info_storage_permission_closed))
                .setMessage(getString(R.string.personal_info_storage_permission_is_open))
                .setNegativeButton(R.string.do_not_open, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(PersonalInfoActivity.this, R.string.personal_info_set_avatar_cannot_use,
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton(R.string.go_to_open, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(SettingSystemPermissionActivity.getCallingIntent(PersonalInfoActivity.this), REQUEST_PERMISSION_ACTIVITY);
                    }
                }).create();
        goToOpenStoragePermissionDialog.show();
    }

    private void showGoToOpenCameraPermissionDialog() {
        if (goToOpenCameraPermissionDialog != null && goToOpenCameraPermissionDialog.isShowing()) {
            return;
        }
        goToOpenCameraPermissionDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.personal_info_camera_permission_closed))
                .setMessage(getString(R.string.personal_info_camera_permission_is_open))
                .setNegativeButton(R.string.do_not_open, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(PersonalInfoActivity.this, R.string.personal_info_set_avatar_cannot_use,
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton(R.string.go_to_open, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(SettingSystemPermissionActivity.getCallingIntent(PersonalInfoActivity.this), REQUEST_PERMISSION_ACTIVITY);
                    }
                }).create();
        goToOpenCameraPermissionDialog.show();
    }
}
