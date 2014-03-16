
package nl.rgomiddelharnis.a6.po.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockListFragment;

import java.util.ArrayList;
import java.util.Map;

import nl.rgomiddelharnis.a6.po.DatabaseHandler;
import nl.rgomiddelharnis.a6.po.R;
import nl.rgomiddelharnis.a6.po.activity.BeheerTafelActivity;

/**
 * Fragment met een lijst van alle bestellingen voor de geselecteerde tafel.
 * 
 * @author Sven Dubbeld <sven.dubbeld1@gmail.com>
 */
public class BestellijstFragment extends SherlockListFragment {

    /**
     * Tag voor in logs.
     * 
     * @see android.util.Log
     */
    @SuppressWarnings("unused")
    private static final String TAG = "BestellijstFragment";

    DatabaseHandler mDb;

    public BestellijstFragment() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Laat alle bestellingen zien.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        
        mDb = new DatabaseHandler(getSherlockActivity());
        
        // Verkrijg data van database
        ArrayList<Map<String, Object>> data = mDb.getBestellingen(((BeheerTafelActivity) getSherlockActivity()).getTafel());

        // Gegevens om te gebruiken...
        String[] from = {
                DatabaseHandler.KEY_GERECHT, DatabaseHandler.KEY_PRIJS_FORMATTED, DatabaseHandler.KEY_AANTAL
        };

        // ... en toe te voegen aan deze velden.
        int[] to = {
                R.id.lbl_gerecht, R.id.lbl_prijs, R.id.lbl_aantal
        };

        // Maak een SimpleAdapter aan voor het verwerken van de gegevens.
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, R.layout.list_item_bestelling,
                from, to);

        // Pas de adapter toe op de lijst.
        setListAdapter(adapter);

        return inflater.inflate(R.layout.fragment_bestellingen_list, container, false);
        
    }
    
}
