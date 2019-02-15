package me.kptmusztarda.autoclicker.gestures;

import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import me.kptmusztarda.autoclicker.R;
import me.kptmusztarda.autoclicker.ViewsManager;

public class RandomCircle extends Gesture implements Dispatchable {

    private static final String TAG = "RandomCircle";
    private static final int DRAWABLE_ID = R.drawable.ic_auto_clicker_point;
    private static final int TYPE = 0;
    private int radius;


    public RandomCircle(Context context, int index, int x, int y, int radius) {
        super(context, index, TAG, DRAWABLE_ID, x, y);

        setRadius(radius);


        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setTextColor(getResources().getColor(R.color.color_index, null));


    }

    public RandomCircle(Context context, int index, String str) {
        super(context, index, TAG, DRAWABLE_ID, 0, 0);

        String s[] = str.split(",");
        int x = Integer.parseInt(s[1]);
        int y = Integer.parseInt(s[2]);
        int r = Integer.parseInt(s[3]);

        setViewCoordinates(x, y);
        setRadius(r);

        drawableId = R.drawable.ic_auto_clicker_point;

        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setTextColor(getResources().getColor(R.color.color_index, null));
    }

    @Override
    public String toString() {
        return TYPE + "," + params.x + "," + params.y + "," + radius;
    }


    protected void setPointCoordinates(int x, int y) {
        getParams().x = x - viewSize/2;
        getParams().y = y - viewSize/2;
//        Logger.log(TAG, "New point " + index + " coordinates are: " + params.x + " " + params.y);
    }

    protected int[] getPointCoordinates() {
        int coords[] = new int[2];
        coords[0] = params.x + (viewSize/2);
        coords[1] = params.y + (viewSize/2) + ViewsManager.getInstance().getStatusBarHeight();
        return coords;
    }


    public void setRadius(int r) {
        if(r > 0) {
            radius = r;
            ViewsManager.getInstance().getBackgroundView().invalidate();
        }
        else radius = 0;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public GestureDescription getGestureDescription() {
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();

        int[] arr = getPointCoordinates();
        double a = Math.random() * 2 * Math.PI;
        double r = getRadius() * Math.sqrt(Math.random());
        int x = (int) (arr[0] + r * Math.cos(a));
        int y = (int) (arr[1] + r * Math.sin(a));

        //Logger.log(TAG, "Point " + i);
//                        Logger.log(TAG, "coords=" + arr[0] + "," + arr[1]);
//                        Logger.log(TAG, "Random coords=" + x + "," + y);

        path.moveTo(x, y);

        builder.addStroke(new GestureDescription.StrokeDescription(path, 0, 10));

        return builder.build();
    }

    @Override
    public void drawOnBackground(Canvas c, Paint paint) {
        int[] arr = getPointCoordinates();
        c.drawCircle(arr[0], arr[1], radius,  paint);
    }
}
