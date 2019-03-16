package me.kptmusztarda.autoclicker.gestures;

import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.widget.ImageButton;

import me.kptmusztarda.autoclicker.R;
import me.kptmusztarda.autoclicker.views.ViewsManager;


public class RandomCircle extends Gesture implements Dispatchable {

    private static final String TAG = "RandomCircle";
    private static final int DRAWABLE_ID = R.drawable.ic_auto_clicker_point;
    private static final int TYPE = 0;
    private int radius = 20;


    public RandomCircle(Context context, int index, int x, int y) {
        super(context, index, TAG, DRAWABLE_ID, 0, 0);
        init(x, y, 20, 10, 20);
    }

    public RandomCircle(Context context, int index, String str) {
        super(context, index, TAG, DRAWABLE_ID, 0, 0);

        String s[] = str.split(",");
        int x = Integer.parseInt(s[1]);
        int y = Integer.parseInt(s[2]);
        int d = Integer.parseInt(s[3]);
        int t = Integer.parseInt(s[4]);
        int r = Integer.parseInt(s[5]);

        init(x, y, d, t, r);
    }

    private void init(int x, int y, int d, int t, int r) {
        setViewCoordinates(x, y);
        setDelay(d);
        setTime(t);
        setRadius(r);
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setTextColor(getResources().getColor(R.color.color_index, null));
    }

    @Override
    public String toString() {
        return TYPE + "," + params.x + "," + params.y + "," + delay + "," + time + "," + radius;
    }

    protected int[] getPointCoordinates() {
        int coords[] = getViewCoordinates();
        coords[0] += viewSize/2;
        coords[1] += viewSize/2 + ViewsManager.getInstance().getStatusBarHeight();
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

        path.moveTo(x, y);
        builder.addStroke(new GestureDescription.StrokeDescription(path, 0, time));

        return builder.build();
    }

    @Override
    public void drawOnBackground(Canvas c, Paint paint) {
        int[] arr = getPointCoordinates();
        c.drawCircle(arr[0], arr[1] - ViewsManager.getInstance().getStatusBarHeight(), radius,  paint);
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        ViewsManager.getInstance().getSettingsLayout().setGestureEditButtons(getGestureEditButtons());
    }

    @Override
    public ImageButton[] getGestureEditButtons() {
        ImageButton[] buttons = new ImageButton[2];

        buttons[0] = new EditButton(getContext());
        buttons[0].setImageResource(R.drawable.ic_radius_increase);
        buttons[0].setOnClickListener(v -> setRadius(radius + 10));

        buttons[1] = new EditButton(getContext());
        buttons[1].setImageResource(R.drawable.ic_radius_decrease);
        buttons[1].setOnClickListener(v -> setRadius(radius - 10));

        return buttons;
    }

}


