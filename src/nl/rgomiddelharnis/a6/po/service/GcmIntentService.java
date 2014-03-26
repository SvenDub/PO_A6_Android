
package nl.rgomiddelharnis.a6.po.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import nl.rgomiddelharnis.a6.po.DatabaseHandler;
import nl.rgomiddelharnis.a6.po.R;
import nl.rgomiddelharnis.a6.po.activity.BeheerTafelActivity;

public class GcmIntentService extends IntentService {

    private NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;
    GoogleCloudMessaging gcm;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        gcm = GoogleCloudMessaging.getInstance(this);

        // Haal intent op voor gcm
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {

            // Bericht ontvangen

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                // Send error
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                // Deleted from server
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // Regular message

                String tag = extras.getString("tag");

                if (tag.equals("bestelstatus")) {

                    // Bestelstatus is bijgewerkt

                    // Haal tafelnummer op
                    int tafelnr = Integer.parseInt(extras.getString("tafelnummer"));

                    // Haal NotificationManager op
                    mNotificationManager = (NotificationManager) this
                            .getSystemService(Context.NOTIFICATION_SERVICE);

                    // Stel de notificatie in
                    mBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_stat_bolhoed)
                            .setLargeIcon(
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.ic_launcher))
                            .setContentTitle(
                                    getString(R.string.tafel) + " "
                                            + extras.getString("tafelnummer"))
                            .setContentText(getString(R.string.bestelling_klaar))
                            .setTicker(
                                    getString(R.string.tafel) + " "
                                            + extras.getString("tafelnummer")
                                            + ": " + getString(R.string.bestelling_klaar))
                            .setNumber(tafelnr)
                            .setAutoCancel(true);

                    // Stel Intent om BeheerTafelActivity te openen zodra de
                    // notificatie wordt aangeklikt
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, tafelnr, new Intent(
                            this, BeheerTafelActivity.class).putExtra(DatabaseHandler.KEY_TAFELNR,
                            tafelnr), 0);
                    mBuilder.setContentIntent(pendingIntent);

                    // Laat de notificatie zien
                    mNotificationManager.notify(tafelnr,
                            mBuilder.build());
                }

            }

        }

        // Geef wake lock vrij
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

}
