package com.yxy.lib.base.app;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityStack {

    private static ActivityStack mStack;

    private List<Activity> mainBeforeActivitys = new ArrayList<Activity>();
    private List<Activity> mainAfterActivitys = new ArrayList<Activity>();

    private ActivityStack() {
    }

    public synchronized static ActivityStack getInstance() {
        if (mStack == null) {
            mStack = new ActivityStack();
        }
        return mStack;
    }

    public void pushMainBeforeActivity(Activity activity) {
        mainBeforeActivitys.add(activity);
    }

    public void popMainBeforeActivity(Activity activity) {
        if (mainBeforeActivitys.contains(activity)) {
            mainBeforeActivitys.remove(activity);
        }
    }

    public void exitMainBeforeActivity() {
        for (Activity activity : mainBeforeActivitys) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        mainBeforeActivitys.clear();
    }

    public void pushMainAfterActivity(Activity activity) {
        mainAfterActivitys.add(activity);
    }

    public boolean isInStackActivity(Class clsName) {
        for (Activity activity : mainAfterActivitys) {
            if (clsName.getSimpleName().equals(activity.getClass().getSimpleName())) {
                return true;
            }
        }
        return false;
    }


    public boolean isTopStack(Activity activity) {
        if (mainAfterActivitys.size() > 0) {
            Activity curActivity = mainAfterActivitys.get(mainAfterActivitys.size() - 1);
            if (activity.getClass().getSimpleName().equals(curActivity.getClass().getSimpleName())) {
                return true;
            }
        }
        return false;
    }

    public boolean isTopStack(Class clsName) {
        if (mainAfterActivitys.size() > 0) {
            Activity curActivity = mainAfterActivitys.get(mainAfterActivitys.size() - 1);
            if (clsName.getSimpleName().equals(curActivity.getClass().getSimpleName())) {
                return true;
            }
        }
        return false;
    }

    public void popMainAfterActivity(Activity activity) {
        if (mainAfterActivitys.contains(activity)) {
            mainAfterActivitys.remove(activity);
        }
    }

    public void exitMainAfterActivity() {
        for (Activity activity : mainAfterActivitys) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        mainAfterActivitys.clear();
    }

}
