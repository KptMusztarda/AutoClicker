package me.kptmusztarda.autoclicker;

import android.accessibilityservice.GestureDescription;

import java.util.concurrent.TimeUnit;

import me.kptmusztarda.handylib.Logger;

public class GestureDispatcher {

    private static final String TAG = "GestureDispatcher";

    private Profile profile;

    private boolean active;

    GestureDispatcher() {

    }

    void start() {
        Logger.log(TAG, "Starting");
        active = true;

        new Thread(() -> {

            int duration = 10;
            int delay = 20;


            while(active) {
                for (int i = 0; i < profile.getPoints().size(); i++) {

                    if (!active) break;

                    dispatch(profile.getPoints().get(i).getGesture());
                    //Logger.log(TAG, "Gesture dispatched " + dispatchGesture(gesture, null, null) + " " +  System.currentTimeMillis() + "ms");

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
        onStart();
    }

    void stop() {
        Logger.log(TAG, "Stopping");
        active = false;
        onStop();
    }

    public boolean isActive() {
        return active;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    void dispatch(GestureDescription gesture) {}
    void onStart() {}
    void onStop() {}
}