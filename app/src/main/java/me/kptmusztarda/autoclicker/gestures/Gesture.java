package me.kptmusztarda.autoclicker.gestures;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import me.kptmusztarda.autoclicker.MyWindowManager;
import me.kptmusztarda.autoclicker.R;
import me.kptmusztarda.autoclicker.ViewsManager;
import me.kptmusztarda.handylib.Logger;

public class Gesture extends android.support.v7.widget.AppCompatTextView {

    class EditButton extends android.support.v7.widget.AppCompatImageButton {
        EditButton(Context context) {
            super(context);
            setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            setBackgroundColor(Color.TRANSPARENT);
            setScaleType(ScaleType.FIT_CENTER);
            setAdjustViewBounds(true);

            LinearLayout.MarginLayoutParams params = (LinearLayout.MarginLayoutParams) getLayoutParams();
            int margin = (int) (8 * (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT) + 0.5f);
            params.topMargin = margin;
            params.setMarginStart(margin);
            params.setMarginEnd(margin);
            setLayoutParams(params);
        }
    }


    private String TAG;

    private int index;
    private boolean active;
    int viewSize;
    int drawableID;
    WindowManager.LayoutParams params;

    private MyWindowManager windowManager;
    private ViewsManager viewsManager = ViewsManager.getInstance();

    public Gesture(Context context, int index, String TAG, int drawableID, int x, int y) {
        super(context);
        this.TAG = TAG;
        this.drawableID = drawableID;

        windowManager = new MyWindowManager(context);

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

        setViewCoordinates(x, y);
        Logger.log(TAG, "Creating point with raw coordinates=" + x + ","  + y);


        params.gravity = Gravity.TOP | Gravity.START;
        params.height = params.width = viewSize;

        setColorToActive(true);
        setIndex(index);
    }

    int offsetX = 0;
    int offsetY = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                setActive(true);

                int[] coords = getViewCoordinates();
                offsetX = (int) event.getRawX() - coords[0];
                offsetY = (int) event.getRawY() - coords[1];

                break;
            case MotionEvent.ACTION_MOVE:
                setViewCoordinates((int) event.getRawX() - offsetX, (int) event.getRawY() - offsetY);
//              Logger.log(TAG, "Action move; New position=" + ((int) event.getRawX() - offsetX) + " " + ((int) event.getRawY() - offsetY));
                onMove();

                break;
        }

        return super.onTouchEvent(event);
    }
    protected void onMove() {
        windowManager.updateViewLayout(this, getParams());
        viewsManager.getBackgroundView().invalidate();
    }

    protected WindowManager.LayoutParams getParams()  {
        return params;
    }

    protected void setViewCoordinates(int x, int y) {
        params.x = x;
        params.y = y;
    }
    protected int[] getViewCoordinates() {
        return new int[]{params.x, params.y};
    }

    public void show() {
        windowManager.addView(this, getParams());
    }
    public void hide() {
        windowManager.removeViewImmediate(this);
    }

    @SuppressLint("SetTextI18n")
    public void setIndex(int index) {
        this.index = index;
        setText(Integer.toString(this.index + 1));
    }
    public int getIndex() {
        return index;
    }

    public void setActive(boolean active) {
        this.active = active;
        setColorToActive(active);
    }
    public boolean isActive() {
        return active;
    }

    protected void setColorToActive(boolean b) {
        Resources.Theme theme;
        if(b) {
            theme = new ContextThemeWrapper(getContext(), R.style.active).getTheme();
        } else {
            theme = new ContextThemeWrapper(getContext(), R.style.inactive).getTheme();
        }
        Drawable drawable = getResources().getDrawable(drawableID, theme);
        setBackground(drawable);
    }

}
