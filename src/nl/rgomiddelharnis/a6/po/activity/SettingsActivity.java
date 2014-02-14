
package nl.rgomiddelharnis.a6.po.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

import nl.rgomiddelharnis.a6.po.R;
import nl.rgomiddelharnis.a6.po.R.string;
import nl.rgomiddelharnis.a6.po.R.xml;

import java.util.List;

/**
 * Laat een {@link PreferenceActivity} zien om instellingen voor de app te
 * kunnen aanpassen.
 */
public class SettingsActivity extends PreferenceActivity {
    /**
     * Laat altijd een enkele kolom zien in plaats van een master/flow layout op
     * tablets.
     */
    private static final boolean ALWAYS_SIMPLE_PREFS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Laat de {@link android.app.ActionBar} zien, als de API beschikbaar is.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Laat de Up knop zien
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Verwerkt het gedrag van de knoppen in het menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Sluit Activity
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Laat de opties zien nadat de Activity is geladen.
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setupSimplePreferencesScreen();
    }

    /**
     * Laat een enkele kolom zien als dit nodig is.
     */
    private void setupSimplePreferencesScreen() {
        if (!isSimplePreferences(this)) {
            return;
        }

        // Voeg 'general' opties toe
        addPreferencesFromResource(R.xml.pref_general);
        bindPreferenceSummaryToValue(findPreference("currency"));
    }

    /**
     * Controleert of multi-pane mode gebruikt moet worden.
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this) && !isSimplePreferences(this);
    }

    /**
     * Controleert of er sprake is van een XL-scherm.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
        & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Controleert of er een enkele kolom moet worden weergegeven.
     */
    private static boolean isSimplePreferences(Context context) {
        return ALWAYS_SIMPLE_PREFS
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
                || !isXLargeTablet(context);
    }

    /**
     * Laat de headers zien in de lijst.
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        if (!isSimplePreferences(this)) {
            loadHeadersFromResource(R.xml.pref_headers, target);
        }
    }

    /**
     * Vangt wijzigingen in de instellingen op en werkt de samenvatting van de
     * instelling bij.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        /**
         * Verwerkt veranderingen in een instelling.
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {

                // ListPreference aangepast

                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Laat waarde zien in samenvatting
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else {

                // Andere Preference aangepast

                // Laat waarde zien in samenvatting
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Bindt de Listener aan een Preference om wijzigingen te verwerken.
     * 
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Stel Listener in
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger de Listener met de huidige waarde
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    /**
     * Fragment voor de 'general' instellingen.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Voeg 'general' opties toe
            addPreferencesFromResource(R.xml.pref_general);
            bindPreferenceSummaryToValue(findPreference("currency"));
        }
    }
}
