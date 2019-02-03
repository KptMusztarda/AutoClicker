package me.kptmusztarda.autoclicker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Switch;

import me.kptmusztarda.handylib.Logger;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private Switch main;
    private boolean resuming;

    private static final String permissions[] = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private void checkPermissions() {
        for(String p : permissions)
            if(ContextCompat.checkSelfPermission(this, p) != 0) {
                ActivityCompat.requestPermissions(this, permissions, 2137);
                break;
            }
    }
    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        //if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

        Logger.setDirectory("", "AutoClicker.txt");

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
