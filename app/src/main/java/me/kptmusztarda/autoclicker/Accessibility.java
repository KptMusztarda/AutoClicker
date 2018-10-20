package me.kptmusztarda.autoclicker;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import me.kptmusztarda.handylib.Logger;

public class Accessibility extends AccessibilityService {

    private static final String TAG = "Accessibility";
    public static final String ACTION_REMOVE_VIEW = "me.kptmusztarda.autoclicker.ACTION_REMOVE_VIEW";
    public static final String ACTION_ADD_VIEW = "me.kptmusztarda.autoclicker.ACTION_ADD_VIEW";
    private ImageButton mainButton, editButton, addButton, removeButton, closeButton;
//    private List<BezierPointView> bezierPoints;
    private List<PointView> points = new ArrayList<>();

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.log(TAG, "Received: " + intent.getAction());
            if(intent.getAction().equals(ACTION_REMOVE_VIEW)) {
                if(settingsLayout.isAttached()) windowManager.removeViewImmediate(settingsLayout);
            } else if(intent.getAction().equals(ACTION_ADD_VIEW)) {
                if(!settingsLayout.isAttached()) windowManager.addView(settingsLayout, settingsLayout.getParams());
            }
        }
    };
    private WindowManager windowManager;
    private SettingsLayout settingsLayout;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_ADD_VIEW);
        filter.addAction(ACTION_REMOVE_VIEW);
        registerReceiver(receiver, filter);
        Logger.log(TAG, "BroadcastReceiver registered");

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        settingsLayout = new SettingsLayout(this);
        windowManager.addView(settingsLayout, settingsLayout.getParams());

        mainButton = settingsLayout.findViewById(R.id.mainButton);
        mainButton.setOnClickListener(v -> {
            Logger.log(TAG, "Button clicked");
            dispatchGesture(getGesture(), null, null);
        });

        editButton = settingsLayout.findViewById(R.id.editButton);
        editButton.setOnClickListener(v -> {

        });

        addButton = settingsLayout.findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            int coords[] = new int[2];
            v.getLocationOnScreen(coords);
            PointView pointView = new PointView(this, points.size() + 1, coords[0] + v.getWidth() + 100, coords[1]);
            pointView.setOnTouchListener((v1, event) -> {
                pointView.setPointCoordinates((int)event.getRawX(), (int)event.getRawY());
                windowManager.updateViewLayout(pointView, pointView.getParams());
                return false;
            });
            points.add(pointView);
            windowManager.addView(pointView, pointView.getParams());
        });

        removeButton = settingsLayout.findViewById(R.id.removeButton);
        removeButton.setOnClickListener(v -> {

        });

        closeButton = settingsLayout.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> {

        });
    }

    private GestureDescription getGesture() {

        GestureDescription.Builder builder = new GestureDescription.Builder();
        CustomPath path = new CustomPath();
        path.moveTo(400, 1500);
        path.rQuadTo(200,300, 400,  0);
        path.rQuadTo(200,-300, 400,  0);
        builder.addStroke(new GestureDescription.StrokeDescription(path, 20,700));
        GestureDescription gesture = builder.build();

        return gesture;
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManager.removeViewImmediate(settingsLayout);
        unregisterReceiver(receiver);
        Logger.log(TAG, "BroadcastReceiver unregistered");
    }
}
