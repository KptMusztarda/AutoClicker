package me.kptmusztarda.autoclicker;

import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class MyWindowManager {

    private WindowManager windowManager;

    MyWindowManager(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    Display getDefaultDisplay() {
        return windowManager.getDefaultDisplay();
    }

    void removeViewImmediate(View view) {
        try {
            windowManager.removeViewImmediate(view);
        } catch (Exception e) {
//            e.printStackTrace();
            if(!(e instanceof IllegalStateException)) {
                e.printStackTrace();
            }
        }
    }

    void addView(View view, ViewGroup.LayoutParams params) {
        try {
            windowManager.addView(view, params);
        } catch (Exception e) {
//            e.printStackTrace();
            if(!(e instanceof IllegalStateException)) {
                e.printStackTrace();
            }
        }
    }

    void updateViewLayout(View view, ViewGroup.LayoutParams params) {
        try {
            windowManager.updateViewLayout(view, params);
        } catch (Exception e) {
//            e.printStackTrace();
            if(!(e instanceof IllegalStateException)) {
//                e.printStackTrace();
            }
        }
    }

    void removeView(View view) {
        windowManager.removeView(view);
    }
}