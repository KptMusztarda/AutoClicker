package me.kptmusztarda.autoclicker.popups;

import android.content.Context;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import me.kptmusztarda.autoclicker.R;
import me.kptmusztarda.autoclicker.gestures.Dispatchable;

public class GesturePropertiesPopup extends Popup {

    private Button cancel, apply;
    private EditText delay, time;

    public GesturePropertiesPopup(Context context, Dispatchable gesture) {
        super(context);
        inflate(context, R.layout.gesture_properties_popup, this);

        delay = findViewById(R.id.gesture_properties_delay_edittext);
        delay.setText(String.valueOf(gesture.getDelay()));
        time = findViewById(R.id.gesture_properties_time_edittext);
        time.setText(String.valueOf(gesture.getTime()));


        cancel = findViewById(R.id.gesture_properties_cancel_button);
        cancel.setOnClickListener((v) -> dismiss());
        apply = findViewById(R.id.gesture_properties_apply_button);
        apply.setOnClickListener((v) -> {
            gesture.setDelay(Integer.valueOf(delay.getEditableText().toString()));
            gesture.setTime(Integer.valueOf(time.getEditableText().toString()));
            dismiss();
        });

    }

}
