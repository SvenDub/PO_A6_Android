
package nl.rgomiddelharnis.a6.po;

import android.content.Context;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Bevat verschillende functies die nodig zijn voor de app.
 * 
 * @author Sven Dubbeld <sven.dubbeld1@gmail.com>
 */
public class Functions {

    /**
     * Controleert of er een dataverbinding beschikbaar is.
     * 
     * @param context {@link Context} De ApplicationContext.
     * @return {@link Boolean} True als er een dataverbinding beschikbaar is.
     */
    public static boolean dataIsVerbonden(Context context) {
        // Get ConnectivityManager
        ConnectivityManager connec = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get Wi-Fi
        android.net.NetworkInfo wifi = connec
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        // Get mobile data
        android.net.NetworkInfo mobile = connec
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isConnected() || mobile.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Haalt het {@link NumberFormat} op om te gebruiken voor de valuta zoals
     * gekozen in de Preferences.
     * 
     * @param context {@link Context} De ApplicationContext.
     * @return {@link NumberFormat} Het NumberFormat om te gebruiken.
     */
    public static NumberFormat getNumberFormat(Context context) {

        // Haal de te gebruiken valuta op
        Currency currency;
        int currencyPref = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context)
                .getString("currency", "0"));
        switch (currencyPref) {
            case 1:
                // Euro
                currency = Currency.getInstance("EUR");
                break;
            case 2:
                // Dollar
                currency = Currency.getInstance("USD");
                break;
            case 3:
                // Pound
                currency = Currency.getInstance("GBP");
                break;
            default:
                // Default
                currency = Currency.getInstance(Locale.getDefault());
                break;
        }

        // Stel de valuta in
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setCurrency(currency);

        return numberFormat;
    }

}
