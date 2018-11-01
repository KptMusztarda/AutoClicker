package me.kptmusztarda.autoclicker;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import me.kptmusztarda.handylib.Logger;

public class Accessibility extends AccessibilityService {

    private static final String TAG = "Accessibility";
    public static final String ACTION_SHOW = "me.kptmusztarda.autoclicker.ACTION_SHOW";
    public static final String ACTION_HIDE = "me.kptmusztarda.autoclicker.ACTION_HIDE";

    private MyWindowManager windowManager;
    private SettingsLayout settingsLayout;
    private BackgroundView backgroundView;
    private DimView dimView;
    private GestureDescription gesture;
    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;
    private ImageButton mainButton, editButton, addButton, removeButton, dimButton, closeButton, increaseRadiusButton, decreaseRadiusButton;
    private View divider;
    private Timer timer;

//    private List<BezierPointView> bezierPoints;
    private boolean active = false;
    private boolean editMode = false;
    private boolean dimmed = false;
    private static  boolean isOpened = false;
    private List<PointView> points = new ArrayList<>();
    private int activePointIndex = 0;
    private long volumeDownPressTime;


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.log(TAG, "Received: " + intent.getAction());

            switch (intent.getAction()) {
                case ACTION_SHOW:

                    show();

                    break;
                case ACTION_HIDE:

                    breakLoop();
                    close();

                    break;
                case Intent.ACTION_SCREEN_OFF:

                    breakLoop();

                    break;
            }
        }
    };


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_HIDE);
        filter.addAction(ACTION_SHOW);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver, filter);
        Logger.log(TAG, "BroadcastReceiver registered");

        pref = getSharedPreferences(getPackageName() + ".prefs", MODE_PRIVATE);
        prefEditor = pref.edit();

        setupViews();


        createPointsFromString(pref.getString("profile_1", ""));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupViews() {
        windowManager = new MyWindowManager(getApplicationContext());

        backgroundView = new BackgroundView(this);
        settingsLayout = new SettingsLayout(this);
        dimView = new DimView(this);

        mainButton = settingsLayout.findViewById(R.id.mainButton);
        mainButton.setOnTouchListener((v, event) -> {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Logger.log(TAG, "Button clicked");
                    if(!active) {
                        if(editMode) enableEditMode(false);
                        loop();
                    } else breakLoop();
                    break;
            }
            return false;
        });
        settingsLayout.setMainSwitchColorToActive(false);

        editButton = settingsLayout.findViewById(R.id.editButton);
        editButton.setOnTouchListener((v, event) -> {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(editMode) enableEditMode(false);
                    else if(active) {
                        breakLoop();
                        enableEditMode(true);
                    } else enableEditMode(true);
                    break;
            }
            return false;
        });

        addButton = settingsLayout.findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            int coords[] = new int[2];
            v.getLocationOnScreen(coords);
            createNewPoint(coords[0] + v.getWidth() + 100, coords[1], 0, true);
        });
        addButton.setVisibility(View.GONE);

        removeButton = settingsLayout.findViewById(R.id.removeButton);
        removeButton.setOnClickListener(v -> {
            if(points.size() > 0) {
                int last = points.size()-1;
                windowManager.removeViewImmediate(points.get(last));
                if(last > 0) select(points.get(last-1));
                points.remove(last);
                backgroundView.removeLastPoint();
                backgroundView.invalidate();
            }
        });
        removeButton.setVisibility(View.GONE);

        dimButton = settingsLayout.findViewById(R.id.dimButton);
        dimButton.setOnTouchListener((v, event) -> {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dim();
                    break;
            }
            return false;
        });

        closeButton = settingsLayout.findViewById(R.id.closeButton);
        closeButton.setOnTouchListener((v, event) -> {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    close();
                    break;
            }
            return false;
        });

        divider = settingsLayout.findViewById(R.id.divider);
        divider.setVisibility(View.GONE);

        increaseRadiusButton = settingsLayout.findViewById(R.id.increaseRadiusButton);
        increaseRadiusButton.setOnClickListener(v -> {
            if(points.size() > 0) {
                PointView p = points.get(activePointIndex);
                p.setRandomRadius(p.getRandomRadius() + 10);
                backgroundView.updatePoint(activePointIndex, p.getPointCoordinates(), p.getRandomRadius());
                backgroundView.invalidate();
            }
        });
        increaseRadiusButton.setVisibility(View.GONE);

        decreaseRadiusButton = settingsLayout.findViewById(R.id.decreaseRadiusButtonimageButton2);
        decreaseRadiusButton.setOnClickListener(v -> {
            if(points.size() > 0) {
                PointView p = points.get(activePointIndex);
                p.setRandomRadius(p.getRandomRadius() - 10);
                backgroundView.updatePoint(activePointIndex, p.getPointCoordinates(), p.getRandomRadius());
                backgroundView.invalidate();
            }
        });
        decreaseRadiusButton.setVisibility(View.GONE);
    }

    private void loop() {

        Logger.log(TAG, "Activated");
        active = true;
        settingsLayout.setMainSwitchColorToActive(true);

        new Thread(() -> {

            int duration = 10;
            int delay = 20;


                while(active) {
                    for (int i = 0; i < points.size(); i++) {
                        GestureDescription.Builder builder = new GestureDescription.Builder();
                        CustomPath path = new CustomPath();

                        PointView p = points.get(i);

                        int[] arr = p.getPointCoordinates();
                        double a = Math.random() * 2 * Math.PI;
                        double r = p.getRandomRadius() * Math.sqrt(Math.random());
                        int x = (int) (arr[0] + r * Math.cos(a));
                        int y = (int) (arr[1] + r * Math.sin(a));

                        Logger.log(TAG, "Point " + i);
//                        Logger.log(TAG, "coords=" + arr[0] + "," + arr[1]);
//                        Logger.log(TAG, "Random coords=" + x + "," + y);

                        path.moveTo(x, y);

                        builder.addStroke(new GestureDescription.StrokeDescription(path, 0, duration));
                        GestureDescription gesture = builder.build();
                        if (!active) break;

                        Logger.log(TAG, "Gesture dispatched " + dispatchGesture(gesture, null, null) + " " +  System.currentTimeMillis() + "ms");

                        try {
                            TimeUnit.MILLISECONDS.sleep(delay + duration);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
//                    try {
//                        TimeUnit.MILLISECONDS.sleep(delay);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            Logger.log(TAG, "Loop thread ended");
        }).start();

        Logger.log(TAG, "Loop thread started");
    }

    @Override
    public boolean onKeyEvent(KeyEvent event) {
//        Logger.log(TAG, "Keycode " + event.getKeyCode());
        if(dimmed) {
            if ((event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    volumeDownPressTime = System.currentTimeMillis();
//                    Logger.log(TAG, "Down");
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
//                    Logger.log(TAG, "Up");
                    if (System.currentTimeMillis() - volumeDownPressTime > 500) {
                        dim();
                    } else {
                        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                        Objects.requireNonNull(audioManager).adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
//                        return super.onKeyEvent(event);
                    }
                }
                return true;
            } else return super.onKeyEvent(event);
        } else return super.onKeyEvent(event);
    }

    private void breakLoop() {
        Logger.log(TAG, "Deactivated");
        active = false;
        settingsLayout.setMainSwitchColorToActive(false);
//        timer.cancel();
//        timer.purge();
    }

    private GestureDescription getGesture() {

        GestureDescription.Builder builder = new GestureDescription.Builder();
        CustomPath path = new CustomPath();
        if(points.size() > 0) {
            int[] coords = points.get(0).getPointCoordinates();
            path.moveTo(coords[0], coords[1]);
        }
        for(int i=1; i<points.size(); i++) {
            int[] coords = points.get(i).getPointCoordinates();
            path.lineTo(coords[0], coords[1]);
        }
        builder.addStroke(new GestureDescription.StrokeDescription(path, 0,points.size() * 500));
        GestureDescription gesture = builder.build();

        return gesture;
    }

    private void enableEditMode(boolean b) {
        if(b) {
            windowManager.addView(backgroundView, backgroundView.getParams());
            for(PointView view : points) {
                windowManager.addView(view, view.getParams());
            }
            addButton.setVisibility(View.VISIBLE);
            removeButton.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
            increaseRadiusButton.setVisibility(View.VISIBLE);
            decreaseRadiusButton.setVisibility(View.VISIBLE);
        } else {
//            if(points.size() > 0) gesture = getGesture();
            prefEditor.putString("profile_1", pointsToString());
            prefEditor.apply();

            for(int i=points.size()-1; i>=0; i--) {
                windowManager.removeViewImmediate(points.get(i));
            }
            windowManager.removeViewImmediate(backgroundView);

            addButton.setVisibility(View.GONE);
            removeButton.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
            increaseRadiusButton.setVisibility(View.GONE);
            decreaseRadiusButton.setVisibility(View.GONE);
        }
        editMode = b;

//        windowManager.updateViewLayout(settingsLayout, settingsLayout.getParams());
    }

    private String pointsToString() {
        StringBuilder builder = new StringBuilder();
        for(PointView point : points) {
            int[] coords = point.getRawCoordinates();
            builder.append(coords[0]);
            builder.append(",");
            builder.append(coords[1]);
            builder.append(",");
            builder.append(point.getRandomRadius());
            builder.append(';');
        }
        Logger.log(TAG, builder.toString());
        return builder.toString();
    }

    private void createPointsFromString(String s) {
        Logger.log(TAG, "Saved string=\"" + s + "\"");
        if(!s.isEmpty()) {
            do {
                int ind;
                int x = Integer.parseInt(s.substring(0, ind = s.indexOf(',')));
                int y = Integer.parseInt(s.substring(ind + 1, ind = s.indexOf(',', ind + 1)));
                int r = Integer.parseInt(s.substring(ind + 1, ind = s.indexOf(';')));

                createNewPoint(x, y, r, false);

                s = s.substring(ind + 1);

            } while(s.length() > 0);
        }
    }

    private void select(PointView p) {
        if(points.size() > 0) points.get(activePointIndex).setColorToActive(false);
        activePointIndex = points.indexOf(p);
        p.setColorToActive(true);
    }

    private void dim() {
        if(dimmed) {
            windowManager.removeViewImmediate(dimView);
            dimmed = false;
        } else {
            windowManager.addView(dimView, dimView.getParams());
            dimmed = true;
        }
    }

    private void dim(boolean b) {
        if(b) {
            windowManager.addView(dimView, dimView.getParams());
            dimmed = true;
        } else {
            windowManager.removeViewImmediate(dimView);
            dimmed = false;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void createNewPoint(int x, int y, int r, boolean show) {
        PointView pointView = new PointView(this, points.size() + 1, x, y);
        Logger.log(TAG, "Creating point with raw cooirdinates=" + x + ","  + y);
        pointView.setRandomRadius(r);
        pointView.setOnTouchListener(new View.OnTouchListener() {
            private int lastAction;
            private int initialX;
            private int initialY;
            private int offsetX;
            private int offsetY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        select(pointView);

                        int[] coords = pointView.getRawCoordinates();
                        offsetX = (int) event.getRawX() - coords[0];
                        offsetY = (int) event.getRawY() - coords[1];
//                            Logger.log(TAG, "Action down; Offset=" + offsetX + " " + offsetY);

//                            initialX = coords[0];
//                            initialY = coords[1];
//
//                            initialTouchX = event.getRawX();
//                            initialTouchY = event.getRawY();

//                            lastAction = event.getAction();
                        break;
//
//                        case MotionEvent.ACTION_UP:
//                            lastAction = event.getAction();
//                            return true;

                    case MotionEvent.ACTION_MOVE:
                        pointView.setRawCoordinates((int) event.getRawX() - offsetX, (int) event.getRawY() - offsetY);
//                            Logger.log(TAG, "Action move; New position=" + ((int) event.getRawX() - offsetX) + " " + ((int) event.getRawY() - offsetY));
                        windowManager.updateViewLayout(pointView, pointView.getParams());
                        backgroundView.updatePoint(points.indexOf(pointView), pointView.getPointCoordinates(), pointView.getRandomRadius());
                        backgroundView.invalidate();
                        break;

                }

                return false;
            }
        });
        points.add(pointView);
        select(pointView);
        int arr[] = pointView.getPointCoordinates();
        Logger.log(TAG, "Adding to background view with coords=" + arr[0] + "," + arr[1]);
        backgroundView.addPoint(arr, pointView.getRandomRadius());
        if(show) {
            windowManager.addView(pointView, pointView.getParams());
            backgroundView.invalidate();
        }
    }

    private void close() {
        if(editMode) enableEditMode(false);
        breakLoop();
        dim(false);
        windowManager.removeViewImmediate(settingsLayout);
        isOpened = false;
    }

    private void show() {
        windowManager.addView(settingsLayout, settingsLayout.getParams());
        isOpened = true;
    }

    @Override
    public void onInterrupt() {

    }

    protected static boolean isOpened() {
        return isOpened;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        close();
        unregisterReceiver(receiver);
        Logger.log(TAG, "BroadcastReceiver unregistered");
    }
}