
package nl.rgomiddelharnis.a6.po.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import nl.rgomiddelharnis.a6.po.DatabaseHandler;
import nl.rgomiddelharnis.a6.po.R;
import nl.rgomiddelharnis.a6.po.activity.BeheerTafelActivity;
import nl.rgomiddelharnis.a6.po.activity.MainActivity;
import nl.rgomiddelharnis.a6.po.activity.ProgressFragmentActivity;
import nl.rgomiddelharnis.a6.po.fragment.TafelsFragment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Deze class gebruikt een achtergrond thread om de bestellingen op te halen.
 * 
 * @author Sven Dubbeld <sven.dubbeld1@gmail.com>
 * @see AsyncTask
 */
public class BestellingenTask extends AsyncTask<List<NameValuePair>, Void, JSONObject> {

    /**
     * Tag voor in logs.
     * 
     * @see android.util.Log
     */
    private static final String TAG = "BestellingenTask";

    InputStream is = null;

    String json = "";
    String error = null;
    String url = "";

    Context mContext;
    ProgressFragmentActivity mActivity;
    DatabaseHandler mDb;

    /**
     * Start een nieuwe <code>BestellingenTask</code> om de bestellingen op te
     * halen.
     * 
     * @param context {@link Context} De Activity die de Task start.
     */
    public BestellingenTask(Context context) {
        this.mContext = context;
        mDb = new DatabaseHandler(context);
        this.url = mDb.getURL();
    }

    /**
     * Laat een <code>ProgressBar</code> zien.
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mActivity = (ProgressFragmentActivity) mContext;
        mActivity.setIndeterminate(true);
        mActivity.showProgressBar();
    }

    /**
     * Haalt de data op van de server.
     */
    @Override
    protected JSONObject doInBackground(List<NameValuePair>... params) {
        JSONObject jObj = null;

        // Verkrijg parameters
        List<NameValuePair> param = params[0];

        try {

            // Stel verbinding in
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams,
                    7500);
            HttpConnectionParams.setSoTimeout(httpParams, 15000);
            DefaultHttpClient httpClient = new DefaultHttpClient(
                    httpParams);

            // Maak HTTP request
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(param));

            // Voer HTTP request uit
            HttpResponse httpResponse = httpClient
                    .execute(httpPost);
            // Verkrijg HTTP response
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

            // Verwerk errors
        } catch (Exception e) {
            e.printStackTrace();
            error = e.toString();
        }

        try {
            // Zet response om naar String
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();

            // Verwerk errors
        } catch (Exception e) {
            e.printStackTrace();
            error = e.toString();
        }

        try {
            // Zet om naar JSONObject
            jObj = new JSONObject(json);

            // Verwerk errors
        } catch (JSONException e) {
            e.printStackTrace();
            error = e.toString();
        }

        return jObj;
    }

    /**
     * Verwerkt de verkregen data.
     */
    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);

        if (error == null) {

            // Geen errors

            try {

                // Haal response code op
                String res = result.getString(DatabaseHandler.KEY_SUCCESS);

                if (Integer.parseInt(res) == 1) {

                    // Response van server is goed

                    // Haal 'bestellingen' object op
                    JSONObject json_bestellingen = result.getJSONObject("bestellingen");
                    Iterator<?> bestellingen = json_bestellingen.keys();

                    // Voeg bestellingen toe aan database
                    mDb.leegBestellingen();
                    while (bestellingen.hasNext()) {
                        String bestelnummer = bestellingen.next().toString();

                        JSONObject besteldata = json_bestellingen.getJSONObject(bestelnummer);

                        String tafelnummer = besteldata.getString("tafelnummer");
                        String inlog_id = besteldata.getString("inlog_id");
                        String product = besteldata.getString("product");
                        String aantal_besteld = besteldata.getString("aantal_besteld");
                        String opmerking = besteldata.getString("opmerking");
                        String datum = besteldata.getString("datum");
                        String status = besteldata.getString("status");

                        mDb.voegBestellingToe(Integer.parseInt(bestelnummer),
                                Integer.parseInt(tafelnummer), Integer.parseInt(inlog_id),
                                Integer.parseInt(product), Integer.parseInt(aantal_besteld),
                                opmerking, datum, Integer.parseInt(status));
                    }

                    try {

                        if (mActivity.getClass() == BeheerTafelActivity.class) {

                            // Request kwam van BeheerTafelActivity dus herlaad
                            // het Fragment

                            ((BeheerTafelActivity) mActivity).getPagerAdapter().reloadAll();
                        }

                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                } else {

                    // Server response bevat een error

                    res = result.getString(DatabaseHandler.KEY_ERROR);

                    switch (Integer.parseInt(res)) {
                        case 1:

                            // Tag niet gestuurd

                            Toast toast1 = Toast.makeText(mContext, R.string.error_tag,
                                    Toast.LENGTH_SHORT);
                            toast1.show();
                            Log.e(TAG, mContext.getString(R.string.error_tag));

                            break;

                        case 2:

                            // Login niet correct

                            Toast toast2 = Toast.makeText(mContext, R.string.invalid_login,
                                    Toast.LENGTH_SHORT);
                            toast2.show();
                            Log.e(TAG, mContext.getString(R.string.invalid_login));

                            break;

                        default:

                            // Andere error

                            Toast toast = Toast.makeText(mContext, R.string.server_error,
                                    Toast.LENGTH_SHORT);
                            toast.show();
                            Log.e(TAG, mContext.getString(R.string.server_error));

                            break;
                    }

                }

            } catch (JSONException e) {
                // Verwerk errors
                e.printStackTrace();
            }

        } else {

            // Errors

            // Meld dat er een error is opgetreden
            Toast toast = Toast.makeText(mContext, R.string.server_error, Toast.LENGTH_SHORT);
            toast.show();
            Log.e(TAG, error);
        }

        // Verberg ProgressBar
        mActivity.hideProgressBar();
    }

}
