package com.yxy.lib.base.ui.base;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.yxy.lib.base.app.ActivityStack;
import com.yxy.lib.base.dialog.LoadingDialog;
import com.yxy.lib.base.utils.InputMethodUtils;
import com.yxy.lib.base.widget.TitleTopBar;

import java.util.Stack;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/30.
 */
public abstract class BaseActivity extends AppCompatActivity implements TitleTopBar.OnTopBarClickCallback {

    private TitleTopBar titleTopBar;
    private LoadingDialog loadingDialog;
    protected Handler mHandler;

    private Bundle savedInstanceState;

    protected boolean isMainBefore() {
        return false;
    }

    protected boolean isKitkatVersionUp(){
        return Build.VERSION.SDK_INT >= 21;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isMainBefore()) {
            ActivityStack.getInstance().pushMainBeforeActivity(this);
        } else {
            ActivityStack.getInstance().pushMainAfterActivity(this);
        }
        mHandler = new Handler();
        //透明状态栏
//        if (isKitkatVersionUp()) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                window.setStatusBarColor(Color.TRANSPARENT);
//            }
//        }

        setContentView(activityResId());
        if (TopBarId() != 0) {
            titleTopBar = (TitleTopBar) findViewById(TopBarId());
        }
        ButterKnife.bind(this);
        attachTopBarAction();
        setupPage(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        String[] needPermission = needPermission();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && needPermission != null && needPermission.length > 0) {
            requestPess(needPermission);
        } else {
            permissGranted();
        }
    }

    protected void setupPage(Bundle savedInstanceState) {

    }


    protected void setStatusBarColor(int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(colorResId));
        }
    }

    protected String[] needPermission() {
        return new String[0];
    }

    private int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 100;

    private boolean needRequest(String[] needPermission) {
        for (String permission : needPermission) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    private void requestPess(String[] needPermission) {
        if (needRequest(needPermission)) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, needPermission, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        } else {
            permissGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                permissGranted();
            } else {
                // Permission Denied
                permissGranted();
            }
        }
    }

    private void permissGranted() {
        initView(savedInstanceState);
    }

    protected TitleTopBar getTitleTopBar() {
        return titleTopBar;
    }

    private void attachTopBarAction() {
        if (titleTopBar != null) {
            titleTopBar.setOnTopBarClickCallback(this);
        }
    }

    protected void showLoading(String text) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.loadStatusShow(text);
    }

    protected void showLoading() {
        showLoading("");
    }

    protected void showLoading(int text) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.loadStatusShow(text);
    }

    public void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.loadStatusHide();
        }
    }

    protected void buildTitle(int left, int title, int right) {
        getTitleTopBar().setLeftText(left);
        getTitleTopBar().setTitle(title);
        getTitleTopBar().setRightText(right);
    }


    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onTitileClick() {

    }

    @Override
    public void onRightClick() {

    }

    protected abstract int activityResId();

    protected abstract int TopBarId();

    protected abstract void initView(Bundle savedInstanceState);


    protected Stack<Fragment> fragmentList = new Stack<>();

    protected void addFragment(Fragment fragment){
        if (fragment == null) return;
        if (!fragmentList.contains(fragment)){
            fragmentList.add(fragment);
        }
    }

    protected void showThisFragment(int resId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!fragmentList.contains(fragment)) {
            fragmentList.add(fragment);
            fragmentTransaction.add(resId, fragment);
        }
        for (Fragment f : fragmentList) {
            if (f != fragment) {
                fragmentTransaction.hide(f);
            }
        }
        fragmentTransaction.show(fragment).commit();
    }

    protected void showThisFragment(int resId, Fragment fragment,String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!fragmentList.contains(fragment)) {
            fragmentList.add(fragment);
            fragmentTransaction.add(resId, fragment,tag);
        }
        for (Fragment f : fragmentList) {
            if (f != fragment) {
                fragmentTransaction.hide(f);
            }
        }
        fragmentTransaction.show(fragment).commit();
    }

    protected void removeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(fragment).commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

        if (isMainBefore()) {
            ActivityStack.getInstance().popMainBeforeActivity(this);
        } else {
            ActivityStack.getInstance().popMainAfterActivity(this);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onLeftClick();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        InputMethodUtils.hideEditTextInput(this);
        super.finish();
    }
}
