package me.kptmusztarda.autoclicker;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import me.kptmusztarda.handylib.Logger;


public class PointView extends TextView {

    private static final String TAG = "PointView";
    private WindowManager.LayoutParams params;
    private int number;

    public PointView(Context context, int index, int x, int y) {
        super(context);
        number = index;
        setText(Integer.toString(number));
        setBackgroundColor(getResources().getColor(R.color.colorPoint, null));

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        setPointCoordinates(x, y);

    }

    protected WindowManager.LayoutParams getParams()  {
        return params;
    }

    protected void setPointCoordinates(int x, int y) {
        params.x -= getWidth()/2;
        params.y -= getHeight()/2;
        Logger.log(TAG, "New point " + number + " coordinates are: " + x + " " + y);
    }

    protected int[] getPointCoordinates() {
        int coords[] = new int[2];
        getLocationOnScreen(coords);
        coords[0] += getWidth()/2;
        coords[1] += getHeight()/2;
        return coords;

    }

}
