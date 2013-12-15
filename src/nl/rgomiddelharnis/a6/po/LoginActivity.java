
package nl.rgomiddelharnis.a6.po;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * Verzorgt het inloggen.
 * 
 * @author Sven Dubbeld <sven.dubbeld1@gmail.com>
 */
public class LoginActivity extends SherlockFragmentActivity {

    /**
     * Tag voor in logs.
     * 
     * @see android.util.Log
     */
    private static final String TAG = "LoginAcivity";

    ActionBar mActionBar;
    EditText mTxtServer;
    EditText mTxtGebruiker;
    EditText mTxtWachtwoord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionBar = getSupportActionBar();

        // Stel de layout in
        setContentView(R.layout.activity_login);

        // Sla de tekstvakken op in variabelen
        mTxtServer = (EditText) findViewById(R.id.txt_server);
        mTxtGebruiker = (EditText) findViewById(R.id.txt_gebruiker);
        mTxtWachtwoord = (EditText) findViewById(R.id.txt_wachtwoord);

        // Pas het gedrag van de enter knop aan
        mTxtServer.setOnEditorActionListener(getOnEditorActionListener());
        mTxtGebruiker.setOnEditorActionListener(getOnEditorActionListener());
        mTxtWachtwoord.setOnEditorActionListener(getOnEditorActionListener());

    }

    /**
     * Maakt het menu aan en voegt het toe aan de ActionBar.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    /**
     * Geeft een {@link OnEditorActionListener} die het gedrag van de enter knop
     * aan past.
     * 
     * @return {@link OnEditorActionListener}
     */
    private OnEditorActionListener getOnEditorActionListener() {
        // Maak een Listener aan
        OnEditorActionListener l = new OnEditorActionListener() {

            /**
             * Gaat naar het volgende of vorige tekstvak, of bevestigt de
             * invoer.
             */
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                    KeyEvent event) {
                
                switch (actionId) {

                    case EditorInfo.IME_ACTION_DONE: // Invoer bevestigen
                        switch (v.getId()) {
                            case R.id.txt_server:
                                break;
                            case R.id.txt_gebruiker:
                                break;
                            case R.id.txt_wachtwoord:
                                // login();
                                break;
                        }
                        break;

                    case EditorInfo.IME_ACTION_PREVIOUS: // Naar vorig tekstvak
                        switch (v.getId()) {
                            case R.id.txt_server:
                                break;
                            case R.id.txt_gebruiker:
                                mTxtServer.requestFocus();
                                break;
                            case R.id.txt_wachtwoord:
                                mTxtGebruiker.requestFocus();
                                break;
                        }
                        break;

                    case EditorInfo.IME_ACTION_NEXT: // Naar volgend tekstvak
                        switch (v.getId()) {
                            case R.id.txt_server:
                                mTxtGebruiker.requestFocus();
                                break;
                            case R.id.txt_gebruiker:
                                mTxtWachtwoord.requestFocus();
                                break;
                            case R.id.txt_wachtwoord:
                                break;
                        }
                        break;

                }
                return false;
            }
            
        };
        
        return l;
    }
    
    /**
     * Verwerkt selecties in het menu.
     */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        
        switch (item.getItemId()) {
            case R.id.action_login:
                // login();
                break;
        }
        
        return super.onMenuItemSelected(featureId, item);
    }

}
