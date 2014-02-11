
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

/**
 * Fragment met een lijst van alle voorgerechten om toe te voegen aan een
 * bestelling.
 * 
 * @author Sven Dubbeld <sven.dubbeld1@gmail.com>
 */
public class VoorgerechtFragment extends SherlockListFragment {

    /**
     * Tag voor in logs.
     * 
     * @see android.util.Log
     */
    private static final String TAG = "VoorgerechtFragment";

    DatabaseHandler mDb;

    public VoorgerechtFragment() {
        // TODO Auto-generated constructor stub
    }
    
    /**
     * Laat alle voorgerechten zien.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        
        mDb = new DatabaseHandler(getSherlockActivity());
        
     // Verkrijg data van database
        ArrayList<Map<String, Object>> data = mDb.getProducten(2);

        // Gegevens om te gebruiken...
        String[] from = {
                DatabaseHandler.KEY_GERECHT, DatabaseHandler.KEY_PRIJS_FORMATTED
        };

        // ... en toe te voegen aan deze velden.
        int[] to = {
                R.id.lbl_gerecht, R.id.lbl_prijs
        };

        // Maak een SimpleAdapter aan voor het verwerken van de gegevens.
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, R.layout.list_item_gerecht,
                from, to);

        // Pas de adapter toe op de lijst.
        setListAdapter(adapter);

        return inflater.inflate(R.layout.fragment_gerecht_list, container, false);
        
    }

}
