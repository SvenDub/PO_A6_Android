
package nl.rgomiddelharnis.a6.po.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.view.Menu;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import nl.rgomiddelharnis.a6.po.DatabaseHandler;
import nl.rgomiddelharnis.a6.po.R;
import nl.rgomiddelharnis.a6.po.adapter.BeheerTafelPagerAdapter;
import nl.rgomiddelharnis.a6.po.fragment.BestellijstFragment;
import nl.rgomiddelharnis.a6.po.fragment.DrankFragment;
import nl.rgomiddelharnis.a6.po.fragment.HoofdgerechtFragment;
import nl.rgomiddelharnis.a6.po.fragment.NagerechtFragment;
import nl.rgomiddelharnis.a6.po.fragment.VoorgerechtFragment;
import nl.rgomiddelharnis.a6.po.task.BestellingenTask;
import nl.rgomiddelharnis.a6.po.task.ProductenTask;

/**
 * Beheert de tafels.
 * <p>
 * Deze Activity bevat meerdere fragmenten die gebruikt worden om bestellingen
 * toe te voegen, te bewerken of te verwijderen.
 * </p>
 * 
 * @author Sven Dubbeld <sven.dubbeld1@gmail.com>
 */
public class BeheerTafelActivity extends ProgressFragmentActivity implements TabListener {

    /**
     * Tag voor in logs.
     * 
     * @see android.util.Log
     */
    private static final String TAG = "BeheerTafelActivity";

    ActionBar mActionBar;
    
    int mTafel;
    
    // Pager
    BeheerTafelPagerAdapter mPagerAdapter;
    ViewPager mViewPager;

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
    public static final int TAB_VOORGERECHT_POS = 0;
    public static final int TAB_HOOFDGERECHT_POS = 1;
    public static final int TAB_NAGERECHT_POS = 2;
    public static final int TAB_DRANK_POS = 3;
    public static final int TAB_BESTELLIJST_POS = 4;

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
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Haal het meegegeven tafelnummer op
        mTafel = getIntent().getIntExtra(DatabaseHandler.KEY_TAFELNR, -1);
        
        if (mTafel != -1) {
        
            // Er is een tafelnummer meegegeven
            
            mActionBar = getSupportActionBar();
            mFragmentManager = getSupportFragmentManager();

            // Stel de layout in
            setContentView(R.layout.activity_beheer_tafel);

            // Maak de PagerAdapter aan
            mPagerAdapter = new BeheerTafelPagerAdapter(mFragmentManager, mTafel);
            
            // Haal bestellingen op
            DatabaseHandler mDb = new DatabaseHandler(getApplicationContext());
            // Bereid de verbindingsparameters voor
            List<NameValuePair> params = new ArrayList<NameValuePair>(3);
            params.add(new BasicNameValuePair("tag", "bestellingen"));
            params.add(new BasicNameValuePair("gebruikersnaam", mDb.getGebruikersnaam()));
            params.add(new BasicNameValuePair("wachtwoord", mDb.getWachtwoord()));
            
            new BestellingenTask(this).execute(params);
            
            // Stel de adapter in
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mPagerAdapter);
            mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                /**
                 * Wisselt naar een Tab zodra het bijbehorende Fragment wordt
                 * geselecteerd.
                 */
                @Override
                public void onPageSelected(int position) {
                    mActionBar.setSelectedNavigationItem(position);
                }
            });

            // Maak de Tabs aan
            mTabVoorgerecht = mActionBar.newTab().setTabListener(this)
                    .setText(R.string.voorgerecht);
            mTabHoofdgerecht = mActionBar.newTab().setTabListener(this)
                    .setText(R.string.hoofdgerecht);
            mTabNagerecht = mActionBar.newTab().setTabListener(this).setText(R.string.nagerecht);
            mTabDrank = mActionBar.newTab().setTabListener(this).setText(R.string.drank);
            mTabBestellijst = mActionBar.newTab().setTabListener(this)
                    .setText(R.string.bestellijst);

            // Voeg de tabs toe aan de ActionBar
            mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            mActionBar.addTab(mTabVoorgerecht, TAB_VOORGERECHT_POS, true);
            mActionBar.addTab(mTabHoofdgerecht, TAB_HOOFDGERECHT_POS);
            mActionBar.addTab(mTabNagerecht, TAB_NAGERECHT_POS);
            mActionBar.addTab(mTabDrank, TAB_DRANK_POS);
            mActionBar.addTab(mTabBestellijst, TAB_BESTELLIJST_POS);
            
            // Stel de titel in
            mActionBar.setTitle(getString(R.string.tafel) + " " + mTafel);
            
        } else {
            
            // Geen tafelnummer meegegeven
            
            // Stel de gebruiker op de hoogte
            Toast toast = Toast.makeText(getApplicationContext(), R.string.geen_tafel_geselecteerd, Toast.LENGTH_SHORT);
            toast.show();
            
        }
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
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub

    }
    
    /**
     * Haalt de PagerAdapter op.
     * 
     * @return {@link BeheerTafelPagerAdapter} De PagerAdapter
     */
    public BeheerTafelPagerAdapter getPagerAdapter() {
        return mPagerAdapter;
    }

}
