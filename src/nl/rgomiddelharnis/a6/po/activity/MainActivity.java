
package nl.rgomiddelharnis.a6.po.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;

import nl.rgomiddelharnis.a6.po.DatabaseHandler;
import nl.rgomiddelharnis.a6.po.R;
import nl.rgomiddelharnis.a6.po.fragment.TafelsFragment;

/**
 * Beginscherm van de app.
 * 
 * @author Sven Dubbeld <sven.dubbeld1@gmail.com>
 */
public class MainActivity extends ProgressFragmentActivity {

    /**
     * Log op DEBUG niveau.
     * 
     * @see android.util.Log
     */
    public static final boolean LOCAL_LOGD = true;
    /**
     * Log op VERBOSE niveau.
     * 
     * @see android.util.Log
     */
    public static final boolean LOCAL_LOGV = true;
    /**
     * Tag voor in logs.
     * 
     * @see android.util.Log
     */
    private static final String TAG = "MainActivity";

    ActionBar mActionBar;
    DatabaseHandler mDb;

    /**
     * <p>
     * Initialiseert het beginscherm van de app. Hier wordt gekeken of de
     * gebruiker is ingelogd en verwijst door naar {@link LoginActivity} als er
     * nog niet ingelogd is.
     * </p>
     * <p>
     * Als deze controle succesvol is verlopen wordt een lijst met tafels uit
     * {@link TafelsFragment} weergegeven.
     * </p>
     * 
     * @see LoginActivity
     * @see TafelsFragment
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionBar = getSupportActionBar();
        mDb = new DatabaseHandler(getApplicationContext());

        if (mDb.isGebruikerIngelogd()) {

            // Gebruiker is ingelogd

            // Stel de layout in
            setContentView(R.layout.activity_main);

            // Voeg het TafelsFragment toe die een lijst met tafels bevat.
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();

            TafelsFragment tafelsFragment = new TafelsFragment();

            fragmentTransaction.replace(R.id.fragment_tafels,
                    tafelsFragment);
            fragmentTransaction.commit();

        } else {

            // Gebruiker is niet ingelogd

            // Start LoginActivity
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }

    }

    /**
     * Maakt het menu aan en voegt het toe aan de ActionBar.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
