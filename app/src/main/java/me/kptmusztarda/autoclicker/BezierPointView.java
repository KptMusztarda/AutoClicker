package me.kptmusztarda.autoclicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class BezierPointView extends View {

    public BezierPointView(Context context) {
        super(context);
    }

    protected WindowManager.LayoutParams getParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 0;
        return params;
    }


    @Override
    protected void onDraw(Canvas canvas) {

    }
}
