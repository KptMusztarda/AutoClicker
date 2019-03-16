package me.kptmusztarda.autoclicker.gestures;

import android.accessibilityservice.GestureDescription;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageButton;

public interface Dispatchable {
    GestureDescription getGestureDescription();
    void show();
    void hide();
    void setIndex(int i);
    int getIndex();
    void setDelay(int delay);
    int getDelay();
    void setTime(int time);
    int getTime();
    void setActive(boolean b);
    void drawOnBackground(Canvas c, Paint paint);
    ImageButton[] getGestureEditButtons();
    String toString();
}
