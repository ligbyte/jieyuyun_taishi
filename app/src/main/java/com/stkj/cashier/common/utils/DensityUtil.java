package com.stkj.cashier.common.utils;

import android.content.Context;

import com.stkj.cashier.common.core.AppManager;


/**
 * 测量工具
 */
public class DensityUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = AppManager.INSTANCE.getApplication().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int dip2px(Context ctx, float dp) {
        if (ctx == null) {
            ctx = AppManager.INSTANCE.getApplication();
        }
        if (ctx == null) {
            return 0;
        }
        float density = ctx.getResources().getDisplayMetrics().density;
        //dp = px/density
        int px = (int) (dp * density + 0.5f);
        return px;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = AppManager.INSTANCE.getApplication().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(float sp) {
        final float scale = AppManager.INSTANCE.getApplication().getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    public static float getFrontScale() {
        float frontScale = AppManager.INSTANCE.getApplication().getResources().getConfiguration().fontScale;
        return frontScale;
    }

    /**
     * 获取屏幕高度（像素）
     */
    public static int getDisplayHeightPixels() {
        return AppManager.INSTANCE.getApplication().getResources().getDisplayMetrics().heightPixels;
    }

    public static float getDensity() {
        return AppManager.INSTANCE.getApplication().getResources().getDisplayMetrics().density;
    }

    /**
     * 获取屏幕宽度(像素)
     */
    public static int getDisplayWidthPixels() {
        return AppManager.INSTANCE.getApplication().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕宽度(dp)
     */
    public static float getDisplayWidthDp() {
        float density = getDensity();
        if (density == 0) {
            density = 1;
        }
        return getDisplayWidthPixels() / density;
    }

    /**
     * 获取屏幕宽度(dp)
     */
    public static float getDisplayHeightDp() {
        float density = getDensity();
        if (density == 0) {
            density = 1;
        }
        return getDisplayHeightPixels() / density;
    }

    // 获取屏幕内容高度(除去状态栏)
    public static int getContentHeight() {
        return getDisplayHeightPixels() - StatusBarUtils.getStatusBarHeight();
    }

}
