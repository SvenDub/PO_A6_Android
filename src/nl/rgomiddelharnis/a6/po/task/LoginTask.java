
package nl.rgomiddelharnis.a6.po.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import nl.rgomiddelharnis.a6.po.DatabaseHandler;
import nl.rgomiddelharnis.a6.po.R;
import nl.rgomiddelharnis.a6.po.activity.MainActivity;
import nl.rgomiddelharnis.a6.po.activity.ProgressFragmentActivity;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Deze class gebruikt een achtergrond thread om de gebruiker in te loggen.
 * 
 * @author Sven Dubbeld <sven.dubbeld1@gmail.com>
 * @see AsyncTask
 */
public class LoginTask extends AsyncTask<List<NameValuePair>, Void, JSONObject> {

    /**
     * Tag voor in logs.
     * 
     * @see android.util.Log
     */
    private static final String TAG = "LoginTask";

    InputStream is = null;

    String json = "";
    String error = null;
    String url = "";
    String regid = "";

    Context mContext;
    ProgressFragmentActivity mActivity;
    DatabaseHandler mDb;

    /**
     * Start een nieuwe <code>LoginTask</code> om de gebruiker in te loggen.
     * 
     * @param context {@link Context} De Activity die de Task start.
     * @param url {@link String} De URL om te gebruiken.
     */
    public LoginTask(Context context, String url) {
        this.mContext = context;
        this.url = url;
        mDb = new DatabaseHandler(context);
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

        // Haal Google Cloud Messaging op
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(mContext);
        
        // Registreer bij Google Cloud Messaging
        try {
            regid = gcm.register(MainActivity.SENDER_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Verkrijg parameters
        List<NameValuePair> param = params[0];
        // Voeg gcm registratie toe
        param.add(new BasicNameValuePair("registration_id", regid));

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

                    // Haal 'user' object op
                    JSONObject json_user = result.getJSONObject("user");
                    
                    // Log de gebruiker in
                    mDb.login(json_user.getInt(DatabaseHandler.KEY_ID),
                            json_user.getString(DatabaseHandler.KEY_GEBRUIKER),
                            json_user.getString(DatabaseHandler.KEY_WACHTWOORD),
                            url, json_user.getString("regid"));

                    // Meld dat het inloggen gelukt is
                    Toast toast = Toast.makeText(mContext, R.string.login_success,
                            Toast.LENGTH_SHORT);
                    toast.show();

                    // Start MainActivity
                    Intent main = new Intent(mContext,
                            MainActivity.class);

                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(main);

                    ((ProgressFragmentActivity) mContext).finish();

                } else {

                    // Login niet correct

                    // Meld dat het inloggen niet gelukt is
                    Toast toast = Toast.makeText(mContext, R.string.invalid_login,
                            Toast.LENGTH_SHORT);
                    toast.show();
                    Log.e(TAG, mContext.getString(R.string.invalid_login));

                }

            } catch (JSONException e) {
                // Verwerk errors
                e.printStackTrace();
            }

        } else {

            // Errors

            // Meld dat het inloggen niet gelukt is
            Toast toast = Toast.makeText(mContext, R.string.server_error, Toast.LENGTH_SHORT);
            toast.show();
            Log.e(TAG, error);

        }

        // Verberg ProgressBar
        mActivity.hideProgressBar();
    }
}
