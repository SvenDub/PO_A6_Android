
package nl.rgomiddelharnis.a6.po;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockListFragment;

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

    public TafelsFragment() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Laat alle tafels zien.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Dummy data.
        // TODO: Verkrijg data van database.
        ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (int i = 1; i < 20; i++) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("id", Integer.toString(i));
            item.put("status", "free");
            data.add(item);
        }

        // Gegevens om te gebruiken...
        String[] from = {
                "id", "status"
        };

        // ... en toe te voegen aan deze velden.
        int[] to = {
                R.id.lbl_tafel_nr, R.id.lbl_tafel_status
        };

        // Maak een SimpleAdapter aan voor het verwerken van de gegevens.
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, R.layout.list_item_tafel,
                from, to);
        
        // Pas de adapter toe op de lijst.
        setListAdapter(adapter);
        
        return inflater.inflate(R.layout.fragment_tafels, container, false);
    }
}
