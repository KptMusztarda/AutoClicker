package me.kptmusztarda.autoclicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Switch;

import me.kptmusztarda.handylib.Logger;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Logger.log("","");

        Switch switchh = findViewById(R.id.switchh);
        switchh.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                sendBroadcast(new Intent(Accessibility.ACTION_ADD_VIEW));
            } else {
                sendBroadcast(new Intent(Accessibility.ACTION_REMOVE_VIEW));
            }
        });
    }
}
