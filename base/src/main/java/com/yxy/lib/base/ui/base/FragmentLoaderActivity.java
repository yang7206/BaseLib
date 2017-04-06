package com.yxy.lib.base.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.yxy.lib.base.R;
import com.yxy.lib.base.widget.TitleTopBar;


/**
 * Created by Administrator on 2016/9/2.
 */
public class FragmentLoaderActivity extends BaseActivity {
    private OnTopbarBuildCallback callback;
    private BaseFragment fragment = null;

    private LinearLayout layout_fragment_loader_root;

    @Override
    protected int activityResId() {
        return R.layout.layout_topbar_content;
    }

    @Override
    protected int TopBarId() {
        return R.id.topbar;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        layout_fragment_loader_root = (LinearLayout) findViewById(R.id.layout_fragment_loader_root);
        buildContent(getIntent());
    }

    private void buildContent(Intent intent) {
        String type = intent.getStringExtra(KEY_TYPE);
        try {
            fragment = FragmentLoaderManager.getFragment(type).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Bundle bundle = getIntent().getBundleExtra(KEY_ARGS);
        if (fragment != null) {
            if (bundle != null) {
                fragment.setArguments(bundle);
            }
            showThisFragment(R.id.parent_content, fragment);
            if (fragment instanceof OnTopbarBuildCallback) {
                callback = (OnTopbarBuildCallback) fragment;
                buildTitleBar();
            }
        }
    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//
//        buildContent(intent);
//    }

//
//    @Override
//    public void finish() {
//        if (fragmentList.size() > 1) {
//            removeFragment(fragmentList.pop());
//            Fragment fragment = fragmentList.peek();
//            showThisFragment(R.id.parent_content, fragment);
//            if (fragment instanceof OnTopbarBuildCallback) {
//                callback = (OnTopbarBuildCallback) fragment;
//                buildTitleBar();
//            }
//        } else {
//            super.finish();
//        }
//    }

    private void buildTitleBar() {
        if (callback != null) {
            TitleTopBar.TopBarOption topBarOption = callback.getTopBarOption(this);
            if (topBarOption != null) {
                if (topBarOption.callback == null) {
                    topBarOption.callback = this;
                }
                if (topBarOption.pageBgResId != -1) {
                    layout_fragment_loader_root.setBackgroundResource(topBarOption.pageBgResId);
                }
                getTitleTopBar().setTopBarOption(topBarOption);
            } else {
                getTitleTopBar().setVisibility(View.GONE);
            }
        }
    }

    public interface OnTopbarBuildCallback {
        TitleTopBar.TopBarOption getTopBarOption(Context context);
    }


    private static final String KEY_TYPE = "KEY_TYPE";


    private static final String KEY_ARGS = "KEY_ARGS";

    public static void show(Context context, String type) {
        show(context, type, null);
    }

    public static void show(Context context, String type, Bundle bundle) {
        Intent intent = new Intent(context, FragmentLoaderActivity.class);
        intent.putExtra(KEY_TYPE, type);
        if (bundle != null) {
            intent.putExtra(KEY_ARGS, bundle);
        }
        context.startActivity(intent);
    }

    public static void showForResult(Activity activity, String type, Bundle bundle, int requestCode) {
        Intent intent = new Intent(activity, FragmentLoaderActivity.class);
        intent.putExtra(KEY_TYPE, type);
        if (bundle != null) {
            intent.putExtra(KEY_ARGS, bundle);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public static void showForResult(Activity activity, String type, int requestCode) {
        showForResult(activity, type, null, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (fragmentList.size() > 0) {
            fragmentList.peek().onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (fragment == null) {
            return super.onKeyDown(keyCode, event);
        }
        boolean onkey = fragment.onKeyDown(keyCode, event);
        if (!onkey) {
            return super.onKeyDown(keyCode, event);
        }
        return onkey;
    }
}
