package me.kptmusztarda.autoclicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import me.kptmusztarda.handylib.Logger;

public class SettingsLayout extends LinearLayout {

    private static final String TAG = "SettingsLayout";
    private ImageButton gestureEditButtons[] = new ImageButton[0];
    private boolean inEditMode;

    public SettingsLayout(Context context) {
        super(context);
        inflate(context, R.layout.settings_panel, this);
        setOrientation(VERTICAL);
        setEditMode(false);
        setBackground(getResources().getDrawable(R.drawable.settings_border, null));
    }

    public WindowManager.LayoutParams getParams()  {
        int type;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else type = WindowManager.LayoutParams.TYPE_PHONE;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    type,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);


        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 700;
        return params;
    }

    protected void setMainSwitchColorToActive(boolean b) {
        Resources.Theme theme;
        if(b) {
            theme = new ContextThemeWrapper(getContext(), R.style.on).getTheme();
        } else {
            theme = new ContextThemeWrapper(getContext(), R.style.off).getTheme();
        }
        Drawable drawable = getResources().getDrawable(R.drawable.ic_on_off, theme);
        ImageButton imageButton =  findViewById(R.id.mainButton);
        imageButton.setImageDrawable(drawable);
    }

    void setEditMode(boolean enabled) {
        if(enabled) {
            findViewById(R.id.addButton).setVisibility(VISIBLE);
            findViewById(R.id.removeButton).setVisibility(VISIBLE);
            for(ImageButton button : gestureEditButtons)
                button.setVisibility(VISIBLE);
            findViewById(R.id.propertiesButton).setVisibility(VISIBLE);
        } else {
            findViewById(R.id.addButton).setVisibility(GONE);
            findViewById(R.id.removeButton).setVisibility(GONE);
            for(ImageButton button : gestureEditButtons)
                button.setVisibility(GONE);
            findViewById(R.id.propertiesButton).setVisibility(GONE);
        }
        inEditMode = enabled;
    }

    public void setGestureEditButtons(ImageButton buttons[]) {

        LinearLayout layout = findViewById(R.id.settings_linear);

        for(ImageButton button : gestureEditButtons)
            layout.removeView(button);

        for(ImageButton button : buttons) {
            layout.addView(button);
            if(!inEditMode) button.setVisibility(GONE);
        }


        gestureEditButtons = buttons;

    }

}
