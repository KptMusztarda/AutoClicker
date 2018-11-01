package me.kptmusztarda.autoclicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import me.kptmusztarda.handylib.Logger;

public class BackgroundView extends View {

    private static final String TAG = "BackgroundView";
    private WindowManager.LayoutParams params;
    private int statusBarHeight;
    private Paint paint;
    private List<int[]> points;

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

        points = new ArrayList<>();

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

    }

    protected WindowManager.LayoutParams getParams()  {
        return params;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        if(points.size() > 1) {
//            for(int i=1; i<points.size(); i++) {
//                int[] start = points.get(i-1);
//                int[] end = points.get(i);
//                canvas.drawLine(start[0], start[1] - statusBarHeight, end[0], end[1] - statusBarHeight, paint);
////                Logger.log(TAG, "Line drawn");
//            }
//        }

//        Logger.log(TAG, "onDraw!");

        for(int i=0; i<points.size(); i++) {
            int[] arr = points.get(i);
            canvas.drawCircle(arr[0], arr[1] - statusBarHeight, arr[2], paint);

            Logger.log(TAG, "Circle drawn, coords=" + arr[0] + "," + arr[1] + " radius=" + arr[2]);
        }


//        canvas.drawText("dsfgdsfghsfhshdfgh", 600, 600, paint);
    }

    protected void addPoint(int[] coords, int r) {
        int arr[] = new int[3];
        arr[0] = coords[0];
        arr[1] = coords[1];
        arr[2] = r;
        points.add(arr);
    }

    protected void removeLastPoint() {
        points.remove(points.size() - 1);
    }

    protected void updatePoint(int i, int[] coords, int r) {
        int arr[] = new int[3];
        arr[0] = coords[0];
        arr[1] = coords[1];
        arr[2] = r;
        points.set(i, arr);
    }
}
