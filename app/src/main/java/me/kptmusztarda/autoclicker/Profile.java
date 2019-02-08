package me.kptmusztarda.autoclicker;

import android.content.Context;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import me.kptmusztarda.handylib.Logger;

public class Profile {

    private final static String TAG = "Profile";

    private Context context;
    private String name;
    private List<PointView> points = new ArrayList<>();
    private ViewsManager viewsManager = ViewsManager.getInstance();
    private MyWindowManager windowManager;
    private int selectedPoint;

    Profile(Context context, String name) {
        this.context = context;
        this.name = name;
        windowManager = new MyWindowManager(context);
    }

    void addPoint(int x, int y, int r, boolean show) {
        PointView pointView = new PointView(context, points.size(), x, y) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                selectPoint(getIndex());
                return super.onTouchEvent(event);
            }
        };
        pointView.setRandomRadius(r);


        int arr[] = pointView.getPointCoordinates();
        Logger.log(TAG, "Adding to background view with coords=" + arr[0] + "," + arr[1]);
//        viewsManager.getBackgroundView().addPoint(arr, pointView.getRandomRadius());
        if(show) {
            pointView.show();
            viewsManager.getBackgroundView().invalidate();
        }

        points.add(pointView);
        selectPoint(pointView.getIndex());
    }

    void removePoint(int index) {
        points.get(index).hide();
        points.remove(index);
    }

    void selectPoint(int i) {
        if(selectedPoint != i) {
            points.get(selectedPoint).setActive(false);
            selectedPoint = i;
            points.get(selectedPoint).setActive(true);
        }
    }

    int getSelectedPointIndex() {
        return selectedPoint;
    }

    PointView getSelectedPoint() {
        return points.get(selectedPoint);
    }

    List<PointView> getPoints() {
        return points;
    }

    void setPoints(List<PointView> points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }
}
