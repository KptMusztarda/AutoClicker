package me.kptmusztarda.autoclicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageButton;

public class SettingsLayout extends ConstraintLayout {

    public SettingsLayout(Context context) {
        super(context);
        inflate(context, R.layout.settings_panel, this);
        this.setBackgroundColor(getResources().getColor(R.color.color_settings_background, null));
    }

    protected WindowManager.LayoutParams getParams()  {
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

}
