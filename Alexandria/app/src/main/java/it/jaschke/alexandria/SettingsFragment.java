package it.jaschke.alexandria;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Andrius-Baruckis on 2015-12-05.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
