
package nl.rgomiddelharnis.a6.po.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;

import nl.rgomiddelharnis.a6.po.R;
import nl.rgomiddelharnis.a6.po.R.id;
import nl.rgomiddelharnis.a6.po.R.layout;
import nl.rgomiddelharnis.a6.po.R.menu;
import nl.rgomiddelharnis.a6.po.R.string;
import nl.rgomiddelharnis.a6.po.fragment.BestellijstFragment;
import nl.rgomiddelharnis.a6.po.fragment.DrankFragment;
import nl.rgomiddelharnis.a6.po.fragment.HoofdgerechtFragment;
import nl.rgomiddelharnis.a6.po.fragment.NagerechtFragment;
import nl.rgomiddelharnis.a6.po.fragment.VoorgerechtFragment;

/**
 * Beheert de tafels.
 * <p>
 * Deze Activity bevat meerdere fragmenten die gebruikt worden om bestellingen
 * toe te voegen, te bewerken of te verwijderen.
 * </p>
 * 
 * @author Sven Dubbeld <sven.dubbeld1@gmail.com>
 */
public class BeheerTafelActivity extends SherlockFragmentActivity implements TabListener {

    /**
     * Tag voor in logs.
     * 
     * @see android.util.Log
     */
    private static final String TAG = "BeheerTafelActivity";

    ActionBar mActionBar;

    // Fragments
    VoorgerechtFragment mVoorgerechtFragment;
    HoofdgerechtFragment mHoofdgerechtFragment;
    NagerechtFragment mNagerechtFragment;
    DrankFragment mDrankFragment;
    BestellijstFragment mBestellijstFragment;

    FragmentManager mFragmentManager;

    // Tabs
    Tab mTabVoorgerecht;
    Tab mTabHoofdgerecht;
    Tab mTabNagerecht;
    Tab mTabDrank;
    Tab mTabBestellijst;

    // Tab positions
    final int TAB_VOORGERECHT_POS = 0;
    final int TAB_HOOFDGERECHT_POS = 1;
    final int TAB_NAGERECHT_POS = 2;
    final int TAB_DRANK_POS = 3;
    final int TAB_BESTELLIJST_POS = 4;

    /**
     * <p>
     * Initialiseert het scherm voor het beheren van de tafels. Er wordt gebruik
     * gemaakt van de volgende Fragments:
     * </p>
     * <ul>
     * <li>{@link VoorgerechtFragment}</li>
     * <li>{@link HoofdgerechtFragment}</li>
     * <li>{@link NagerechtFragment}</li>
     * <li>{@link DrankFragment}</li>
     * <li>{@link BestellijstFragment}</li>
     * </ul>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionBar = getSupportActionBar();
        mFragmentManager = getSupportFragmentManager();

        // Stel de layout in
        setContentView(R.layout.activity_beheer_tafel);

        // Maak de Fragments aan
        mVoorgerechtFragment = new VoorgerechtFragment();
        mHoofdgerechtFragment = new HoofdgerechtFragment();
        mNagerechtFragment = new NagerechtFragment();
        mDrankFragment = new DrankFragment();
        mBestellijstFragment = new BestellijstFragment();

        // Maak de Tabs aan
        mTabVoorgerecht = mActionBar.newTab().setTabListener(this).setText(R.string.voorgerecht);
        mTabHoofdgerecht = mActionBar.newTab().setTabListener(this).setText(R.string.hoofdgerecht);
        mTabNagerecht = mActionBar.newTab().setTabListener(this).setText(R.string.nagerecht);
        mTabDrank = mActionBar.newTab().setTabListener(this).setText(R.string.drank);
        mTabBestellijst = mActionBar.newTab().setTabListener(this).setText(R.string.bestellijst);

        // Voeg de tabs toe aan de ActionBar
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mActionBar.addTab(mTabVoorgerecht, TAB_VOORGERECHT_POS, true);
        mActionBar.addTab(mTabHoofdgerecht, TAB_HOOFDGERECHT_POS);
        mActionBar.addTab(mTabNagerecht, TAB_NAGERECHT_POS);
        mActionBar.addTab(mTabDrank, TAB_DRANK_POS);
        mActionBar.addTab(mTabBestellijst, TAB_BESTELLIJST_POS);

        // Voeg het VoorgerechtFragment toe
        mFragmentManager.beginTransaction().replace(R.id.fragment_beheer_tafel,
                mVoorgerechtFragment).commit();
    }

    /**
     * Maakt het menu aan en voegt het toe aan de ActionBar.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.beheer_tafel, menu);
        return true;
    }

    /**
     * Wisselt naar het gewenste Fragment zodra er een Tab wordt geselecteerd.
     */
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        switch (tab.getPosition()) {
            case TAB_VOORGERECHT_POS:
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_beheer_tafel, mVoorgerechtFragment).commit();
                break;
            case TAB_HOOFDGERECHT_POS:
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_beheer_tafel, mHoofdgerechtFragment).commit();
                break;
            case TAB_NAGERECHT_POS:
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_beheer_tafel, mNagerechtFragment).commit();
                break;
            case TAB_DRANK_POS:
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_beheer_tafel, mDrankFragment).commit();
                break;
            case TAB_BESTELLIJST_POS:
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_beheer_tafel, mBestellijstFragment).commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub

    }

}
