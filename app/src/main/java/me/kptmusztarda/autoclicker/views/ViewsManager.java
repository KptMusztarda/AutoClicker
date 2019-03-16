package me.kptmusztarda.autoclicker.views;

import me.kptmusztarda.autoclicker.SettingsLayout;

public class ViewsManager {

    private static ViewsManager instance = new ViewsManager();
    private ViewsManager() {

    }
    public static ViewsManager getInstance() {
        return instance;
    }



    private BackgroundView backgroundView;
    private SettingsLayout settingsLayout;

    public BackgroundView getBackgroundView() {
        return backgroundView;
    }
    public void setBackgroundView(BackgroundView backgroundView) {
        this.backgroundView = backgroundView;
    }

    public SettingsLayout getSettingsLayout() {
        return settingsLayout;
    }
    public void setSettingsLayout(SettingsLayout settingsLayout) {
        this.settingsLayout = settingsLayout;
    }


    public int getStatusBarHeight() {
        int[] absolutePosition = new int[2];
        settingsLayout.getLocationOnScreen(absolutePosition);
//        System.out.println("Status bar height: " + (absolutePosition[1] - settingsLayout.getParams().y));
        return absolutePosition[1] - settingsLayout.getParams().y;
    }
}
