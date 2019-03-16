package me.kptmusztarda.autoclicker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.kptmusztarda.handylib.Logger;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private Switch accessibilitySwitch;
    private ProfileManager profileManager;
    private Spinner profilesList;
    private Button newButton, loadButton, deleteButton;
    private EditText nameEditText;
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

        Logger.setDirectory("", "AutoClicker.txt");

        accessibilitySwitch = findViewById(R.id.accessibility_switch);
        accessibilitySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!resuming) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            }
        });

        profileManager = new ProfileManager(this);
        profileManager.loadProfiles();

        newButton = findViewById(R.id.new_profile_button);
        newButton.setOnClickListener(v -> {

            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.new_profile_popup, null);

            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true;
            PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            popupWindow.showAtLocation(newButton, Gravity.CENTER, 0, 0);

            Button cancel = popupView.findViewById(R.id.new_profile_cancel);
            final boolean cancelDown[] = {false};
            cancel.setOnTouchListener((v12, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        cancelDown[0] = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        if(cancelDown[0]) popupWindow.dismiss();
                        cancelDown[0] = false;
                        break;
                }
                return false;
            });

            Button confirm = popupView.findViewById(R.id.new_profile_confirm);
            final boolean cancelUp[] = {false};
            confirm.setOnTouchListener((v12, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        cancelUp[0] = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        if(cancelUp[0]) {

                            EditText name = popupView.findViewById(R.id.new_form_name);
                            String s = name.getText().toString();

                            if(!s.isEmpty()) {
                                profileManager.addProfile(s);
                                popupWindow.dismiss();
                                updateProfileList();
                            } else {
                                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                            }
                        }
                        cancelUp[0] = false;
                        break;
                }
                return false;
            });
        });

        loadButton = findViewById(R.id.load_profile_button);
        loadButton.setOnClickListener(v -> {
            Intent intent = new Intent(Accessibility.ACTION_LOAD);
            intent.putExtra(Accessibility.EXTRA_PROFILE_ID, (int) profilesList.getSelectedItemId());
            sendBroadcast(intent);
        });

        deleteButton = findViewById(R.id.delete_profile_button);
        deleteButton.setOnClickListener(v -> {
            if(Accessibility.isOpened()) Toast.makeText(this, "Close the Auto Clicker to delete a profile", Toast.LENGTH_SHORT).show();
            else {
            profileManager.deleteProfile(profilesList.getSelectedItemPosition());
            updateProfileList();
            }
        });

        profilesList = findViewById(R.id.profiles_list);
        updateProfileList();

    }

    private void updateProfileList() {
        List<String> list = profileManager.getProfilesNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        profilesList.setAdapter(adapter);
        if(list.size() > 0) loadButton.setClickable(true);
        else loadButton.setClickable(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        resuming = true;
        accessibilitySwitch.setChecked(isAccessibilityEnabled());
        resuming = false;
    }

    private boolean isAccessibilityEnabled(){
        int accessibilityEnabled = 0;
        final String ACCESSIBILITY_SERVICE_NAME = getPackageName() + "/" + Accessibility.class.getName();
        boolean accessibilityFound = false;
        try {
            accessibilityEnabled = Settings.Secure.getInt(this.getContentResolver(),android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
//            Log.d(TAG, "ACCESSIBILITY: " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
//            Log.d(TAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
        }

        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled==1){
//            Log.d(TAG, "***ACCESSIBILIY IS ENABLED***: ");


            String settingValue = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
//            Log.d(TAG, "Setting: " + settingValue);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();
//                    Log.d(TAG, "Setting: " + accessabilityService);
                    if (accessabilityService.equalsIgnoreCase(ACCESSIBILITY_SERVICE_NAME)){
//                        Log.d(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }

//            Log.d(TAG, "***END***");
        }
        else{
//            Log.d(TAG, "***ACCESSIBILIY IS DISABLED***");
        }
        return accessibilityFound;
    }
}
