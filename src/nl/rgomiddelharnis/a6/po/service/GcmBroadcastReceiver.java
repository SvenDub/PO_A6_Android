
package nl.rgomiddelharnis.a6.po.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    /**
     * Verwerkt de broadcast door de intent door te sturen naar de {@link GcmIntentService}.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // Laat GcmIntentService de intent verwerken.
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        // Start de service
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }

}
