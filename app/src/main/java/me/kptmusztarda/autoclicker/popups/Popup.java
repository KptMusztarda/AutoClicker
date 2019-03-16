package me.kptmusztarda.autoclicker.popups;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.constraint.ConstraintLayout;
import android.view.WindowManager;

import me.kptmusztarda.autoclicker.MyWindowManager;

public class Popup extends ConstraintLayout {

    MyWindowManager windowManager;

    Popup(Context context) {
        super(context);
        windowManager = new MyWindowManager(context);
    }

    public WindowManager.LayoutParams getParams()  {
        int type;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else type = WindowManager.LayoutParams.TYPE_PHONE;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        return params;
    }

    public void show() {
        windowManager.addView(this, getParams());
    }

    public void dismiss() {
        windowManager.removeViewImmediate(this);
    }
}
