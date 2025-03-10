package com.stkj.cashier.common.core;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;


import com.stkj.cashier.app.base.BaseActivity;
import com.stkj.cashier.util.util.ActivityUtils;

import java.lang.ref.WeakReference;

/**
 * 持有弱引用AC
 */
public abstract class ActivityWeakRefHolder implements LifecycleEventObserver {

    private WeakReference<Activity> activityWR;

    public ActivityWeakRefHolder(@NonNull Activity activity) {
        activityWR = new WeakReference<>(activity);
        if (MainThreadHolder.isOnUIThread()) {
            if (activity instanceof LifecycleOwner) {
                LifecycleOwner lifecycleOwner = (LifecycleOwner) activity;
                lifecycleOwner.getLifecycle().addObserver(ActivityWeakRefHolder.this);
            }
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (activity instanceof LifecycleOwner) {
                        LifecycleOwner lifecycleOwner = (LifecycleOwner) activity;
                        lifecycleOwner.getLifecycle().addObserver(ActivityWeakRefHolder.this);
                    }
                }
            });
        }
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_CREATE) {
            onActivityCreate();
        } else if (event == Lifecycle.Event.ON_START) {
            onActivityStart();
        } else if (event == Lifecycle.Event.ON_RESUME) {
            onActivityResume();
        } else if (event == Lifecycle.Event.ON_PAUSE) {
            onActivityPause();
        } else if (event == Lifecycle.Event.ON_STOP) {
            onActivityStop();
        } else if (event == Lifecycle.Event.ON_DESTROY) {
            onActivityDestroy();
        }
    }

    /**
     * 获取当前持有的AC
     */
    public @Nullable
    Activity getHolderActivity() {
        if (activityWR != null) {
            return activityWR.get();
        }
        return null;
    }

    /**
     * 获取当前持有的AC
     */
    public @Nullable
    Activity getHolderActivityWithCheck() {
        Activity holderActivity = getHolderActivity();
        boolean activityFinished = ActivityUtils.isActivityFinished(holderActivity);
        if (!activityFinished) {
            return holderActivity;
        }
        return null;
    }

    /**
     * 持有的AC是否销毁了
     */
    public boolean isHolderActivityFinished() {
        return ActivityUtils.isActivityFinished(getHolderActivity());
    }

    /**
     * 主线程执行任务（检查AC引用）
     *
     * @param task 任务
     */
    public void runUIThreadWithCheck(Runnable task) {
        Activity holderActivity = getHolderActivity();
        if (!ActivityUtils.isActivityFinished(holderActivity)) {
            holderActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Activity holderActivity = getHolderActivity();
                    if (!ActivityUtils.isActivityFinished(holderActivity)) {
                        task.run();
                    }
                }
            });
        }
    }

    /**
     * 清理持有的AC（在activity销毁的时候）
     */
    public void clear() {
        Activity holderActivity = getHolderActivity();
        if (holderActivity instanceof LifecycleOwner) {
            LifecycleOwner lifecycleOwner = (LifecycleOwner) holderActivity;
            lifecycleOwner.getLifecycle().removeObserver(this);
        }
        if (holderActivity instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) holderActivity;
            baseActivity.clearWeakRefHolder(this.getClass());
        }
        onClear();
        if (activityWR != null) {
            activityWR.clear();
            activityWR = null;
        }
    }

    /**
     * 清理资源
     */
    public abstract void onClear();

    protected void onActivityCreate() {

    }

    protected void onActivityStart() {

    }

    protected void onActivityResume() {

    }

    protected void onActivityPause() {

    }

    protected void onActivityStop() {

    }

    protected void onActivityDestroy() {
        clear();
    }

}
