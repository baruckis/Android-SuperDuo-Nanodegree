package it.jaschke.alexandria;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Andrius-Baruckis on 2015-12-06.
 */
public class Utils {

    public static boolean isTablet(Context context) {
        return (context.getApplicationContext().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isLandscape(Context context) {
        return (context.getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity == null) return;
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
