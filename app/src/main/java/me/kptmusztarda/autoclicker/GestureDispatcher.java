package me.kptmusztarda.autoclicker;

import android.accessibilityservice.GestureDescription;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.kptmusztarda.autoclicker.gestures.Dispatchable;
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

            List<Dispatchable> gestures = profile.getGestures();

            long startTime = System.currentTimeMillis();
            int cyclesToDispatch[] = new int[gestures.size()];
            Arrays.fill(cyclesToDispatch, 0);

            while(active) {
                for (int i = 0; i<gestures.size(); i++) {


                    if(cyclesToDispatch[i] == 0) {

                        try {
                            TimeUnit.MILLISECONDS.sleep(gestures.get(i).getDelay() + gestures.get(i).getTime());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (!active) break;


//                    dispatch(gestures.get(i).getGestureDescription());
                        Logger.log(TAG, "Gesture " + i + " dispatched: " + Boolean.toString(dispatch(gestures.get(i).getGestureDescription())) + " " + (System.currentTimeMillis() - startTime) + "ms");

                        cyclesToDispatch[i] = gestures.get(i).getDispatchEvery()-1;

                    } else cyclesToDispatch[i]--;

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

    void stop() {
        Logger.log(TAG, "Stopping");
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    boolean dispatch(GestureDescription gesture) {
        return false;
    }
}