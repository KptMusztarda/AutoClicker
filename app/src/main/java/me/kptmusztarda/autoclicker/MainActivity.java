package me.kptmusztarda.autoclicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Switch;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private Switch main;
    private boolean resuming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        main = findViewById(R.id.switchh);
        main.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!resuming) {
                if (isChecked) {
                    sendBroadcast(new Intent(Accessibility.ACTION_SHOW));
                } else {
                    sendBroadcast(new Intent(Accessibility.ACTION_HIDE));
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        resuming = true;
        main.setChecked(Accessibility.isOpened());
        resuming = false;
    }
}
