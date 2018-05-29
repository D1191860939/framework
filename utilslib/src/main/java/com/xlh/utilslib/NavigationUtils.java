package com.xlh.utilslib;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * Created by sauce on 2017/3/27.
 * Version 1.0.0
 */
public class NavigationUtils {
    //    private static int navigationBarHeight =-1;
    private static boolean checkDeviceHasNavigationBar(Context context) {

        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return hasNavigationBar;
    }

    private static int getVirtualBarHeight(Context context) {
        int vh = 0;
        try {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics dm = new DisplayMetrics();
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }
//    private static int getNavigationBar(Context context) {
//        int navigationBarHeight = 0;
//        Resources rs = context.getResources();
//        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
//        if (id > 0 && checkDeviceHasNavigationBar(context)) {
//            navigationBarHeight = rs.getDimensionPixelSize(id);
//        }
//        return navigationBarHeight;
//    }

    public static int getNavigationBarHeight(Context context) {
        if (checkDeviceHasNavigationBar(context)) {
            return getVirtualBarHeight(context);
        } else {
            return 0;
        }
//        return navigationBarHeight ==-1?getNavigationBar(context): navigationBarHeight;
    }
}
