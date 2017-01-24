package pl.microinteractive.flexocalculations;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;

/**
 * Created by kuba on 2017-01-24.
 */

public class UstawieniaActivity extends PreferenceActivity {

    SharedPreferences ust;

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            preference.setSummary(stringValue);
            return true;
        }
    };

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.ust_main);

        bindPreferenceSummaryToValue(findPreference("ust_procent_odpad"));
        bindPreferenceSummaryToValue(findPreference("ust_anilox_vol"));
        bindPreferenceSummaryToValue(findPreference("ust_zalanie"));

        ust = PreferenceManager.getDefaultSharedPreferences(UstawieniaActivity.this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(ust.getBoolean("ust_odpad_procent", true)) {
            MainActivity.odpad_tr.setVisibility(View.GONE);
        } else {
            MainActivity.odpad_tr.setVisibility(View.VISIBLE);
        }
    }
}
