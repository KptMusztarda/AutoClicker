package me.kptmusztarda.autoclicker.popups;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import me.kptmusztarda.autoclicker.R;
import me.kptmusztarda.autoclicker.gestures.Gestures;

public class NewGesturePopup extends Popup {

    Spinner list;

    public NewGesturePopup(Context context) {
        super(context);
        inflate(context, R.layout.new_gesture_popup, this);

        list = findViewById(R.id.new_gesture_list);
        list.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, Gestures.getNamesList()));

        findViewById(R.id.new_gesture_cancel).setOnClickListener((v) -> dismiss());

    }

    public long getSelectedId() {
        return list.getSelectedItemId();
    }

}
