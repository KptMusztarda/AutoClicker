package me.kptmusztarda.autoclicker;

class ViewsManager {

    private static ViewsManager instance = new ViewsManager();
    private ViewsManager() {

    }
    static ViewsManager getInstance() {
        return instance;
    }



    private BackgroundView backgroundView;

    BackgroundView getBackgroundView() {
        return backgroundView;
    }

    void setBackgroundView(BackgroundView backgroundView) {
        this.backgroundView = backgroundView;
    }
}
