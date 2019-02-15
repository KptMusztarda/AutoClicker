package me.kptmusztarda.autoclicker;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import me.kptmusztarda.autoclicker.gestures.Gestures;

public class NewGesturePopup extends ConstraintLayout {

    public NewGesturePopup(Context context) {
        super(context);
        inflate(context, R.layout.new_gesture_popup, this);

        Spinner list = findViewById(R.id.new_gesture_list);
        list.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, Gestures.getNamesList()));

    }

    protected WindowManager.LayoutParams getParams()  {
        int type;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else type = WindowManager.LayoutParams.TYPE_PHONE;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 700;
        return params;
    }

}
