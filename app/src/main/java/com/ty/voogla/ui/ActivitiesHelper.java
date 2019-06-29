package com.ty.voogla.ui;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.ty.voogla.util.ZBLog;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 管理所有 Activity 任务栈
 *
 * @author TY
 */
public class ActivitiesHelper implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "ActivitiesHelper";

    private static ActivitiesHelper instance;

    private final LinkedList<WeakReference<Activity>> activities = new LinkedList<>();

    private WeakReference<Activity> lastResumeActivity;
    private WeakReference<Activity> lastStartActivity;

    private ActivitiesHelper(@NonNull Application application) {
        if (application == null) {
            throw new NullPointerException("application is NULL!");
        }
        application.registerActivityLifecycleCallbacks(this);
    }

    public static ActivitiesHelper get() {
        if (instance == null) {
            throw new NullPointerException("NOT init!");
        }
        return instance;
    }

    public static void init(Application application) {
        if (instance == null) {
            synchronized (ActivitiesHelper.class) {
                if (instance == null) {
                    instance = new ActivitiesHelper(application);
                }
            }
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Iterator<WeakReference<Activity>> it = activities.iterator();
        while (it.hasNext()) {
            WeakReference<Activity> ref = it.next();
            if (ref.get() == null || activity == ref.get()) {
                it.remove();
            }
        }
        activities.addLast(new WeakReference<>(activity));
        ZBLog.d(TAG, "onActivityCreated:size:" + activities.size());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ZBLog.d(TAG, "onActivityStarted:" + activity.getClass().getSimpleName());
        lastStartActivity = new WeakReference<>(activity);
        ZBLog.d(TAG, "onActivityStarted:size:" + activities.size());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ZBLog.d(TAG, "onActivityResumed:" + activity.getClass().getSimpleName());
        lastResumeActivity = new WeakReference<>(activity);
        ZBLog.d(TAG, "onActivityResumed:size:" + activities.size());
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ZBLog.d(TAG, "onActivityPaused:" + activity.getClass().getSimpleName());
        lastResumeActivity = null;
        ZBLog.d(TAG, "onActivityPaused:size:" + activities.size());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ZBLog.d(TAG, "onActivityStopped:" + activity.getClass().getSimpleName());
        lastStartActivity = null;
        ZBLog.d(TAG, "onActivityStopped:size:" + activities.size());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ZBLog.d(TAG, "onActivityDestroyed:" + activity.getClass().getSimpleName());
        Iterator<WeakReference<Activity>> it = activities.iterator();
        while (it.hasNext()) {
            WeakReference<Activity> ref = it.next();
            if (ref.get() == null || activity == ref.get()) {
                it.remove();
            }
        }
        ZBLog.d(TAG, "onActivityDestroyed:size:" + activities.size());
    }

    /**
     * 获取当前 Activity
     *
     * @return Activity
     */
    public Activity getLastActivity() {
        return activities.getLast().get();
    }

    public boolean isAppFrontOfUser() {
        return lastStartActivity != null;
    }

    public boolean isAppActive() {
        return lastStartActivity != null || lastResumeActivity != null;
    }

    public void finishAll() {
        for (int i = activities.size() - 1; i > -1; i--) {
            WeakReference<Activity> ref = activities.get(i);
            if (ref.get() != null) {
                ref.get().finish();
            }
        }
    }

    public void finishOthers(Activity activity) {
        for (WeakReference<Activity> ref : activities) {
            if (ref.get() != null && activity != ref.get()) {
                ref.get().finish();
            }
        }
    }
}
