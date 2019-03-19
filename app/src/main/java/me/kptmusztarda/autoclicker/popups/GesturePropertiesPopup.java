package me.kptmusztarda.autoclicker.popups;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import me.kptmusztarda.autoclicker.R;
import me.kptmusztarda.autoclicker.gestures.Dispatchable;
import me.kptmusztarda.handylib.Logger;

public class GesturePropertiesPopup extends PopupWindow {

    private static final String TAG = "GesturePropertiesPopup";

    private Button cancel, apply;
    private EditText delay, time, dispatchEvery;

    public GesturePropertiesPopup(Context context, int width, int height, Dispatchable gesture) {
        super();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setContentView(inflater.inflate(R.layout.gesture_properties_popup, null));
        setWidth(width);
        setHeight(height);
        setFocusable(true);

        delay = getContentView().findViewById(R.id.gesture_properties_delay_edittext);
        delay.setText(String.valueOf(gesture.getDelay()));
        time = getContentView().findViewById(R.id.gesture_properties_time_edittext);
        time.setText(String.valueOf(gesture.getTime()));
        dispatchEvery = getContentView().findViewById(R.id.gesture_properties_dispatch_every_edittext);
        dispatchEvery.setText(String.valueOf(gesture.getDispatchEvery()));


        cancel = getContentView().findViewById(R.id.gesture_properties_cancel_button);
        cancel.setOnClickListener((v) -> dismiss());
        apply = getContentView().findViewById(R.id.gesture_properties_apply_button);
        apply.setOnClickListener((v) -> {
            gesture.setDelay(Integer.valueOf(delay.getEditableText().toString()));
            gesture.setTime(Integer.valueOf(time.getEditableText().toString()));
            gesture.setDispatchEvery(Integer.valueOf(dispatchEvery.getEditableText().toString()));
            dismiss();
        });

    }

}
