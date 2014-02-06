
package nl.rgomiddelharnis.a6.po.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockListFragment;

import nl.rgomiddelharnis.a6.po.DatabaseHandler;
import nl.rgomiddelharnis.a6.po.R;
import nl.rgomiddelharnis.a6.po.R.id;
import nl.rgomiddelharnis.a6.po.R.layout;
import nl.rgomiddelharnis.a6.po.activity.BeheerTafelActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Fragment met een lijst van alle tafels.
 * 
 * @author Sven Dubbeld <sven.dubbeld1@gmail.com>
 */
public class TafelsFragment extends SherlockListFragment {

    /**
     * Tag voor in logs.
     * 
     * @see android.util.Log
     */
    private static final String TAG = "TafelsFragment";

    DatabaseHandler mDb;
    
    public TafelsFragment() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Laat alle tafels zien.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mDb = new DatabaseHandler(getSherlockActivity());
        
        // Verkrijg data van database
        ArrayList<Map<String, Object>> data = mDb.getTafels();

        // Gegevens om te gebruiken...
        String[] from = {
                DatabaseHandler.KEY_ID, DatabaseHandler.KEY_STATUS
        };

        // ... en toe te voegen aan deze velden.
        int[] to = {
                R.id.lbl_tafel_nr, R.id.img_tafel_status
        };

        // Maak een SimpleAdapter aan voor het verwerken van de gegevens.
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, R.layout.list_item_tafel,
                from, to);

        // Pas de adapter toe op de lijst.
        setListAdapter(adapter);

        return inflater.inflate(R.layout.fragment_tafels, container, false);
    }
    
    /**
     * Start {@link BeheerTafelActivity} voor de gekozen tafel.
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        @SuppressWarnings("unchecked")
        // Haal het opgegeven item op
        Map<String, Object> selected = (Map<String, Object>) getListAdapter().getItem(position);
        
        // Start Activity
        Intent intent = new Intent(getActivity(), BeheerTafelActivity.class);
        intent.putExtra(DatabaseHandler.KEY_ID, Integer.parseInt(selected.get(DatabaseHandler.KEY_ID).toString()));
        startActivity(intent);
    }
}
