package me.kptmusztarda.autoclicker;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.WindowManager;

public class SettingsLayout extends ConstraintLayout {

    private boolean isAttached;

    public SettingsLayout(Context context) {
        super(context);
        inflate(context, R.layout.settings_panel, this);
        this.setBackgroundColor(getResources().getColor(R.color.color, null));
    }

    protected WindowManager.LayoutParams getParams()  {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 400;
        return params;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttached = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttached = false;
    }

    protected boolean isAttached() {
        return isAttached;
    }
}
