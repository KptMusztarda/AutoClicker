package me.kptmusztarda.autoclicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

//import com.devs.vectorchildfinder.VectorChildFinder;
//import com.devs.vectorchildfinder.VectorDrawableCompat;


public class PointView extends TextView {

    private static final String TAG = "PointView";
    private WindowManager.LayoutParams params;
    private int number;
    private int randomRadius;
    private int pointCoordinates[] = new int[2];
    private int statusBarHeight;
    private int viewSize;
//    private Context context;

    public PointView(Context context, int index, int x, int y) {
        super(context);
//        this.context = context;
        number = index;
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setTextColor(getResources().getColor(R.color.color_index, null));
        setText(Integer.toString(number));
//        setColorToActive(true);

        viewSize = (int) (40 * getContext().getResources().getDisplayMetrics().density + 0.5f);

        int type;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else type = WindowManager.LayoutParams.TYPE_PHONE;

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        setRawCoordinates(x, y);


        params.gravity = Gravity.TOP | Gravity.START;
        params.height = params.width = viewSize;

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

    }

    protected WindowManager.LayoutParams getParams()  {
        return params;
    }

    protected void setPointCoordinates(int x, int y) {
//        params.x = x;
//        params.y = y;
        params.x = x - viewSize/2;
        params.y = y - viewSize/2;
//        Logger.log(TAG, "New point " + number + " coordinates are: " + params.x + " " + params.y);
    }

    protected int[] getPointCoordinates() {
        int coords[] = new int[2];
        coords[0] = params.x + (viewSize/2);
        coords[1] = params.y + (viewSize/2) + statusBarHeight;
        return coords;

    }
    protected void setRawCoordinates(int x, int y) {
        params.x = x;
        params.y = y;
    }


    protected int[] getRawCoordinates() {
        int[] arr = {params.x, params.y};
        return arr;
    }

    protected void setRandomRadius(int r) {
        if(r > 0) randomRadius = r;
        else randomRadius = 0;
    }

    protected int getRandomRadius() {
        return randomRadius;
    }

    protected void setColorToActive(boolean b) {
        Resources.Theme theme;
        if(b) {
           theme = new ContextThemeWrapper(getContext(), R.style.active).getTheme();
        } else {
            theme = new ContextThemeWrapper(getContext(), R.style.inactive).getTheme();
        }
        Drawable drawable = getResources().getDrawable(R.drawable.ic_auto_clicker_point, theme);
        setBackground(drawable);
    }
}
