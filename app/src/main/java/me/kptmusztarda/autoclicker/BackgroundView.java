package me.kptmusztarda.autoclicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import java.util.List;

import me.kptmusztarda.autoclicker.gestures.Dispatchable;
import me.kptmusztarda.handylib.Logger;

public class BackgroundView extends View {

    private static final String TAG = "BackgroundView";
    private WindowManager.LayoutParams params;
    private Paint paint;
    private Profile profile;

    public BackgroundView(Context context) {
        super(context);

        int type;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else type = WindowManager.LayoutParams.TYPE_PHONE;

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                type,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 0;

        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorPrimaryDark, null));
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);

    }

    protected WindowManager.LayoutParams getParams()  {
        return params;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        List<Dispatchable> gestures = profile.getGestures();

        for(int i=0; i<gestures.size(); i++) {
            gestures.get(i).drawOnBackground(canvas, paint);
        }

    }

    void setProfile(Profile profile) {
        this.profile = profile;
    }
}
