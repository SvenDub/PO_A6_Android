
package nl.rgomiddelharnis.a6.po.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import nl.rgomiddelharnis.a6.po.activity.BeheerTafelActivity;
import nl.rgomiddelharnis.a6.po.fragment.BestellijstFragment;
import nl.rgomiddelharnis.a6.po.fragment.DrankFragment;
import nl.rgomiddelharnis.a6.po.fragment.HoofdgerechtFragment;
import nl.rgomiddelharnis.a6.po.fragment.NagerechtFragment;
import nl.rgomiddelharnis.a6.po.fragment.VoorgerechtFragment;

/**
 * Deze {@link FragmentPagerAdapter} beheert het wisselen tussen verschillende
 * <code>Fragments</code> in de {@link BeheerTafelActivity}.
 * 
 * @author Sven Dubbeld <sven.dubbeld1@gmail.com>
 */
public class BeheerTafelPagerAdapter extends FragmentPagerAdapter {

    int mTafel;

    /**
     * Maak een nieuwe <code>BeheerTafelPagerAdapter</code> aan om de
     * <code>Fragments</code> te beheren.
     * 
     * @param {@link FragmentManager} De te gebruiken FragmentManager
     * @param {@link Integer} Het nummer van de tafel
     */
    public BeheerTafelPagerAdapter(FragmentManager fm, int tafel) {
        super(fm);
        mTafel = tafel;
    }

    /**
     * Haalt een <code>Fragment</code> op.
     * 
     * @param {@link Integer} Het nummer van het Fragment.
     */
    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case BeheerTafelActivity.TAB_VOORGERECHT_POS:
                VoorgerechtFragment voorgerechtFragment = new VoorgerechtFragment();
                return voorgerechtFragment;
            case BeheerTafelActivity.TAB_HOOFDGERECHT_POS:
                HoofdgerechtFragment hoofdgerechtFragment = new HoofdgerechtFragment();
                return hoofdgerechtFragment;
            case BeheerTafelActivity.TAB_NAGERECHT_POS:
                NagerechtFragment nagerechtFragment = new NagerechtFragment();
                return nagerechtFragment;
            case BeheerTafelActivity.TAB_DRANK_POS:
                DrankFragment drankFragment = new DrankFragment();
                return drankFragment;
            case BeheerTafelActivity.TAB_BESTELLIJST_POS:
                BestellijstFragment bestellijstFragment = new BestellijstFragment();
                return bestellijstFragment;
            default:
                return null;
        }
    }

    /**
     * Haalt het aantal <code>Fragments</code> op. Dit is altijd <code>5</code>.
     */
    @Override
    public int getCount() {
        return 5;
    }

}
