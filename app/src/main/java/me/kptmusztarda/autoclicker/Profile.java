package me.kptmusztarda.autoclicker;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.kptmusztarda.autoclicker.gestures.Gestures;
import me.kptmusztarda.handylib.Logger;
import me.kptmusztarda.autoclicker.gestures.Dispatchable;
import me.kptmusztarda.autoclicker.gestures.RandomCircle;

public class Profile {

    private final static String TAG = "Profile";

    private Context context;
    private String name;
    private List<Dispatchable> gestures = new ArrayList<>();
    private ViewsManager viewsManager = ViewsManager.getInstance();
    private MyWindowManager windowManager;
    private int selectedGesture;

    Profile(Context context, String name) {
        this.context = context;
        this.name = name;
        windowManager = new MyWindowManager(context);
    }

    void addGesture(int type, int x, int y, int r, boolean show) {

        Dispatchable gesture = null;

        switch (Gestures.values()[type]) {
            case RANDOM_CIRCLE:
                gesture = new RandomCircle(context, gestures.size(), x, y, 0) {
                    @Override
                    public void setActive(boolean active) {
                        super.setActive(active);
                        if(active)selectPoint(getIndex());
                    }
                };
                break;
            case SINGLE_POINT:
                break;
        }

//        int arr[] = pointView.getPointCoordinates();
//        Logger.log(TAG, "Adding to background view with coords=" + arr[0] + "," + arr[1]);
//        viewsManager.getBackgroundView().addGesture(arr, pointView.getRadius());
        if(show) {
            gesture.show();
            viewsManager.getBackgroundView().invalidate();
        }

        gestures.add(gesture);
        selectPoint(gesture.getIndex());
    }

    void addGesture(String str, boolean show) {
        int type = Integer.parseInt(str.substring(0, str.indexOf(',')));
        Dispatchable gesture = null;
        switch (Gestures.values()[type]) {
            case RANDOM_CIRCLE:
                gesture = new RandomCircle(context, gestures.size(), str) {
                    @Override
                    public void setActive(boolean active) {
                        super.setActive(active);
                        if(active)selectPoint(getIndex());
                    }
                };
                break;
            case SINGLE_POINT:
                break;
        }
        if(show) {
            gesture.show();
            viewsManager.getBackgroundView().invalidate();
        }

        gestures.add(gesture);
        selectPoint(gesture.getIndex());
    }

    void createGesturesFromString(String string) {
        Logger.log(TAG, "Saved gestures: " + string);
        String str[] = string.split(";");
        for(String s: str) {
            if(!s.isEmpty()) addGesture(s, false);
        }
    }

    void removePoint(int index) {
        if(index < gestures.size()) {
            if (index > 0)
                selectPoint(index - 1);
            gestures.get(index).hide();
            gestures.remove(index);
            for (int i = 0; i < gestures.size(); i++)
                gestures.get(i).setIndex(i);
        }
    }

    void selectPoint(int i) {
        if(selectedGesture != i) {
            gestures.get(selectedGesture).setActive(false);
            selectedGesture = i;
            gestures.get(selectedGesture).setActive(true);
        }
    }

    int getSelectedGestureIndex() {
        return Objects.requireNonNull(selectedGesture);
    }

    Dispatchable getSelectedGesture() {
        return gestures.get(selectedGesture);
    }

    List<Dispatchable> getGestures() {
        return gestures;
    }

    void setGestures(List<Dispatchable> gestures) {
        this.gestures = gestures;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Dispatchable gesture : gestures) {
            builder.append(gesture.toString());
            builder.append(';');
        }
        return builder.toString();
    }

}
