package me.kptmusztarda.autoclicker;

import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.view.WindowManager;


public class RandomCircle extends Gesture {

    private static final String TAG = "RandomCircle";
    private int randomRadius;
    private int pointCoordinates[] = new int[2];


    public RandomCircle(Context context, int index, int x, int y) {
        super(context, index, TAG, x, y);

        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setTextColor(getResources().getColor(R.color.color_index, null));


    }






    protected void setPointCoordinates(int x, int y) {
        getParams().x = x - viewSize/2;
        getParams().y = y - viewSize/2;
//        Logger.log(TAG, "New point " + index + " coordinates are: " + params.x + " " + params.y);
    }

    protected int[] getPointCoordinates() {
        int coords[] = new int[2];
        coords[0] = getParams().x + (viewSize/2);
        coords[1] = getParams().y + (viewSize/2) + statusBarHeight;
        return coords;
    }


    protected void setRandomRadius(int r) {
        if(r > 0) randomRadius = r;
        else randomRadius = 0;
    }

    protected int getRandomRadius() {
        return randomRadius;
    }

    GestureDescription getGesture() {
        GestureDescription.Builder builder = new GestureDescription.Builder();
        CustomPath path = new CustomPath();

        int[] arr = getPointCoordinates();
        double a = Math.random() * 2 * Math.PI;
        double r = getRandomRadius() * Math.sqrt(Math.random());
        int x = (int) (arr[0] + r * Math.cos(a));
        int y = (int) (arr[1] + r * Math.sin(a));

        //Logger.log(TAG, "Point " + i);
//                        Logger.log(TAG, "coords=" + arr[0] + "," + arr[1]);
//                        Logger.log(TAG, "Random coords=" + x + "," + y);

        path.moveTo(x, y);

        builder.addStroke(new GestureDescription.StrokeDescription(path, 0, 10));

        return builder.build();
    }


}
