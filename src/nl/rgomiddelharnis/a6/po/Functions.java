
package nl.rgomiddelharnis.a6.po;

import android.content.Context;
import android.net.ConnectivityManager;

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

}
